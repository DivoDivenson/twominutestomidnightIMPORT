

#include <wchar.h>
#include "Parser.h"
#include "Scanner.h"




void Parser::SynErr(int n) {
	if (errDist >= minErrDist) errors->SynErr(la->line, la->col, n);
	errDist = 0;
}

void Parser::SemErr(const wchar_t* msg) {
	if (errDist >= minErrDist) errors->Error(t->line, t->col, msg);
	errDist = 0;
}

void Parser::Get() {
	for (;;) {
		t = la;
		la = scanner->Scan();
		if (la->kind <= maxT) { ++errDist; break; }

		if (dummyToken != t) {
			dummyToken->kind = t->kind;
			dummyToken->pos = t->pos;
			dummyToken->col = t->col;
			dummyToken->line = t->line;
			dummyToken->next = NULL;
			coco_string_delete(dummyToken->val);
			dummyToken->val = coco_string_create(t->val);
			t = dummyToken;
		}
		la = t;
	}
}

void Parser::Expect(int n) {
	if (la->kind==n) Get(); else { SynErr(n); }
}

void Parser::ExpectWeak(int n, int follow) {
	if (la->kind == n) Get();
	else {
		SynErr(n);
		while (!StartOf(follow)) Get();
	}
}

bool Parser::WeakSeparator(int n, int syFol, int repFol) {
	if (la->kind == n) {Get(); return true;}
	else if (StartOf(repFol)) {return false;}
	else {
		SynErr(n);
		while (!(StartOf(syFol) || StartOf(repFol) || StartOf(0))) {
			Get();
		}
		return StartOf(syFol);
	}
}

void Parser::AddOp(int &op) {
		op = -1; 
		if (la->kind == 3) {
			Get();
			op = plus; 
		} else if (la->kind == 4) {
			Get();
			op = minus; 
		} else SynErr(29);
}

void Parser::Expr(int &type) {
		int type1, op; 
		SimExpr(type);
		if (la->kind == 14 || la->kind == 15 || la->kind == 16) {
			RelOp(op);
			SimExpr(type1);
			if (type != type1) Err(L"incompatible types");
			gen->Emit(op); type = boolean; 
		}
}

void Parser::SimExpr(int &type) {
		int type1, op; 
		Term(type);
		while (la->kind == 3 || la->kind == 4) {
			AddOp(op);
			Term(type1);
			if (type != integer || type1 != integer) 
			     Err(L"integer type expected");
			   gen->Emit(op); 
		}
}

void Parser::RelOp(int &op) {
		op = -1; 
		if (la->kind == 14) {
			Get();
			op = equ; 
		} else if (la->kind == 15) {
			Get();
			op = lss; 
		} else if (la->kind == 16) {
			Get();
			op = gtr; 
		} else SynErr(30);
}

void Parser::Factor(int &type) {
		int n; Obj *obj; wchar_t* name; 
		type = undef; 
		if (la->kind == 1) {
			Ident(name);
			obj = tab->Find(name); type = obj->type;
			if (obj->kind == var) {
			if (obj->level == 0) gen->Emit(LOADG, obj->adr);
			else gen->Emit(LOAD, obj->adr);
			             } else Err(L"variable expected"); 
		} else if (la->kind == 2) {
			Get();
			swscanf(t->val, L"%d", &n);	//n = Convert.ToInt32(t->val); 
			gen->Emit(CONST, n); type = integer; 
		} else if (la->kind == 4) {
			Get();
			Factor(type);
			if (type != integer) {
			 Err(L"integer type expected"); type = integer;
			}
			gen->Emit(NEG); 
		} else if (la->kind == 5) {
			Get();
			gen->Emit(CONST, 1); type = boolean; 
		} else if (la->kind == 6) {
			Get();
			gen->Emit(CONST, 0); type = boolean; 
		} else SynErr(31);
}

void Parser::Ident(wchar_t* &name) {
		Expect(1);
		name = coco_string_create(t->val); 
}

void Parser::MulOp(int &op) {
		op = -1; 
		if (la->kind == 7) {
			Get();
			op = times; 
		} else if (la->kind == 8) {
			Get();
			op = slash; 
		} else SynErr(32);
}

void Parser::ProcDecl() {
		wchar_t* name; Obj *obj; int adr; 
		Expect(9);
		Ident(name);
		obj = tab->NewObj(name, proc, undef); obj->adr = gen->pc;
		if (coco_string_equal(name, L"Main")) gen->progStart = gen->pc; 
		tab->OpenScope(); 
		Expect(10);
		Expect(11);
		Expect(12);
		gen->Emit(ENTER, 0); adr = gen->pc - 2; 
		while (StartOf(1)) {
			if (la->kind == 25 || la->kind == 26) {
				VarDecl();
			} else {
				Stat();
			}
		}
		Expect(13);
		gen->Emit(LEAVE); gen->Emit(RET);
		gen->Patch(adr, tab->topScope->nextAdr);
		tab->CloseScope(); 
}

void Parser::VarDecl() {
		wchar_t* name; int type; 
		Type(type);
		Ident(name);
		tab->NewObj(name, var, type); 
		while (la->kind == 27) {
			Get();
			Ident(name);
			tab->NewObj(name, var, type); 
		}
		Expect(18);
}

void Parser::Stat() {
		int type; wchar_t* name; Obj *obj;
		int adr, adr2, loopstart; 
		switch (la->kind) {
		case 1: {
			Ident(name);
			obj = tab->Find(name); 
			if (la->kind == 17) {
				Get();
				if (obj->kind != var) Err(L"cannot assign to procedure"); 
				Expr(type);
				Expect(18);
				if (type != obj->type) Err(L"incompatible types");
				if (obj->level == 0) gen->Emit(STOG, obj->adr);
				else gen->Emit(STO, obj->adr); 
			} else if (la->kind == 10) {
				Get();
				Expect(11);
				Expect(18);
				if (obj->kind != proc) Err(L"object is not a procedure");
				gen->Emit(CALL, obj->adr); 
			} else SynErr(33);
			break;
		}
		case 19: {
			Get();
			Expect(10);
			Expr(type);
			Expect(11);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr = gen->pc - 2; 
			Stat();
			if (la->kind == 20) {
				Get();
				gen->Emit(JMP, 0); adr2 = gen->pc - 2;
				gen->Patch(adr, gen->pc);
				adr = adr2; 
				Stat();
			}
			gen->Patch(adr, gen->pc); 
			break;
		}
		case 21: {
			Get();
			loopstart = gen->pc; 
			Expect(10);
			Expr(type);
			Expect(11);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr = gen->pc - 2; 
			Stat();
			gen->Emit(JMP, loopstart); gen->Patch(adr, gen->pc); 
			break;
		}
		case 22: {
			Get();
			Ident(name);
			Expect(18);
			obj = tab->Find(name);
			if (obj->type != integer) Err(L"integer type expected");
			gen->Emit(READ);
			if (obj->level == 0) gen->Emit(STOG, obj->adr);
			else gen->Emit(STO, obj->adr); 
			break;
		}
		case 23: {
			Get();
			Expr(type);
			Expect(18);
			if (type != integer) Err(L"integer type expected");
			gen->Emit(WRITE); 
			break;
		}
		case 12: {
			Get();
			while (StartOf(1)) {
				if (StartOf(2)) {
					Stat();
				} else {
					VarDecl();
				}
			}
			Expect(13);
			break;
		}
		default: SynErr(34); break;
		}
}

void Parser::Term(int &type) {
		int type1, op; 
		Factor(type);
		while (la->kind == 7 || la->kind == 8) {
			MulOp(op);
			Factor(type1);
			if (type != integer || type1 != integer) 
			 Err(L"integer type expected");
			gen->Emit(op); 
		}
}

void Parser::Taste() {
		wchar_t* name;
		InitDeclarations(); 
		Expect(24);
		Ident(name);
		tab->OpenScope(); 
		Expect(12);
		while (la->kind == 25 || la->kind == 26) {
			VarDecl();
		}
		while (la->kind == 9) {
			ProcDecl();
		}
		Expect(13);
		tab->CloseScope(); 
}

void Parser::Type(int &type) {
		type = undef; 
		if (la->kind == 25) {
			Get();
			type = integer; 
		} else if (la->kind == 26) {
			Get();
			type = boolean; 
		} else SynErr(35);
}



void Parser::Parse() {
	t = NULL;
	la = dummyToken = new Token();
	la->val = coco_string_create(L"Dummy Token");
	Get();
	Taste();
	Expect(0);
	Expect(0);
}

Parser::Parser(Scanner *scanner) {
	maxT = 28;

	dummyToken = NULL;
	t = la = NULL;
	minErrDist = 2;
	errDist = minErrDist;
	this->scanner = scanner;
	errors = new Errors();
}

bool Parser::StartOf(int s) {
	const bool T = true;
	const bool x = false;

	static bool set[3][30] = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, T,x,x,x, x,x,x,T, x,T,T,T, x,T,T,x, x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, T,x,x,x, x,x,x,T, x,T,T,T, x,x,x,x, x,x}
	};



	return set[s][la->kind];
}

Parser::~Parser() {
	delete errors;
	delete dummyToken;
}

Errors::Errors() {
	count = 0;
}

void Errors::SynErr(int line, int col, int n) {
	wchar_t* s;
	switch (n) {
			case 0: s = coco_string_create(L"EOF expected"); break;
			case 1: s = coco_string_create(L"ident expected"); break;
			case 2: s = coco_string_create(L"number expected"); break;
			case 3: s = coco_string_create(L"\"+\" expected"); break;
			case 4: s = coco_string_create(L"\"-\" expected"); break;
			case 5: s = coco_string_create(L"\"true\" expected"); break;
			case 6: s = coco_string_create(L"\"false\" expected"); break;
			case 7: s = coco_string_create(L"\"*\" expected"); break;
			case 8: s = coco_string_create(L"\"/\" expected"); break;
			case 9: s = coco_string_create(L"\"void\" expected"); break;
			case 10: s = coco_string_create(L"\"(\" expected"); break;
			case 11: s = coco_string_create(L"\")\" expected"); break;
			case 12: s = coco_string_create(L"\"{\" expected"); break;
			case 13: s = coco_string_create(L"\"}\" expected"); break;
			case 14: s = coco_string_create(L"\"==\" expected"); break;
			case 15: s = coco_string_create(L"\"<\" expected"); break;
			case 16: s = coco_string_create(L"\">\" expected"); break;
			case 17: s = coco_string_create(L"\"=\" expected"); break;
			case 18: s = coco_string_create(L"\";\" expected"); break;
			case 19: s = coco_string_create(L"\"if\" expected"); break;
			case 20: s = coco_string_create(L"\"else\" expected"); break;
			case 21: s = coco_string_create(L"\"while\" expected"); break;
			case 22: s = coco_string_create(L"\"read\" expected"); break;
			case 23: s = coco_string_create(L"\"write\" expected"); break;
			case 24: s = coco_string_create(L"\"program\" expected"); break;
			case 25: s = coco_string_create(L"\"int\" expected"); break;
			case 26: s = coco_string_create(L"\"bool\" expected"); break;
			case 27: s = coco_string_create(L"\",\" expected"); break;
			case 28: s = coco_string_create(L"??? expected"); break;
			case 29: s = coco_string_create(L"invalid AddOp"); break;
			case 30: s = coco_string_create(L"invalid RelOp"); break;
			case 31: s = coco_string_create(L"invalid Factor"); break;
			case 32: s = coco_string_create(L"invalid MulOp"); break;
			case 33: s = coco_string_create(L"invalid Stat"); break;
			case 34: s = coco_string_create(L"invalid Stat"); break;
			case 35: s = coco_string_create(L"invalid Type"); break;

		default:
		{
			wchar_t format[20];
			coco_swprintf(format, 20, L"error %d", n);
			s = coco_string_create(format);
		}
		break;
	}
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
	coco_string_delete(s);
	count++;
}

void Errors::Error(int line, int col, const wchar_t *s) {
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
	count++;
}

void Errors::Warning(int line, int col, const wchar_t *s) {
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
}

void Errors::Warning(const wchar_t *s) {
	wprintf(L"%ls\n", s);
}

void Errors::Exception(const wchar_t* s) {
	wprintf(L"%ls", s); 
	exit(1);
}



$$$
