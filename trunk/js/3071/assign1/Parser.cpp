

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

void Parser::Calc() {
		int p; char *name; 
		while (la->kind == 1) {
			Ident(name);
			Expect(3);
			Expr(p);
			Expect(4);
			map<string, int>::iterator it = tab->find(string(name));
			if(it == tab->end()){
			    tab->insert(pair<string, int>(name, p));
			}else{
			    tab->erase(string(name)); //Bit clunky, but does the job
			    tab->insert(pair<string, int>(name, p));
			}
			
		}
		while (la->kind == 5) {
			Get();
			Expr(p);
			Ident(name);
			if (la->kind == 4 || la->kind == 6 || la->kind == 7) {
				if (la->kind == 6) {
					Get();
					printf("%s = 0x%x\n",name, p); 
				} else if (la->kind == 7) {
					Get();
					printf("%s = 0o%o\n",name, p); 
				} else {
					Get();
					printf("%s = %d\n",name, p); 
				}
			}
		}
		if (la->kind == 8) {
			Get();
			exit(0); 
		}
}

void Parser::Ident(char* &name) {
		Expect(1);
		name = coco_string_create_char(t->val); 
}

void Parser::Expr(int &q) {
		int r; 
		Term(q);
		while (la->kind == 9 || la->kind == 10) {
			if (la->kind == 9) {
				Get();
				Term(r);
				q = q + r; 
			} else {
				Get();
				Term(r);
				q = q - r; 
			}
		}
}

void Parser::Term(int &q) {
		int r; 
		Factor(q);
		while (la->kind == 11 || la->kind == 12 || la->kind == 13) {
			if (la->kind == 11) {
				Get();
				Factor(r);
				q = q * r; 
			} else if (la->kind == 12) {
				Get();
				Factor(r);
				q = q / r; 
			} else {
				Get();
				Factor(r);
				q = q % r; 
			}
		}
}

void Parser::Factor(int &q) {
		int r; 
		Expon(q);
		while (la->kind == 14) {
			Get();
			Expon(r);
			int origQ = q;
			while(r != 1){
			q = q*origQ;
			r--;
			}
			
		}
}

void Parser::Expon(int &p) {
		char *name; 
		if (la->kind == 2) {
			Get();
			swscanf(t->val, L"%d",&p);  
		} else if (la->kind == 1) {
			Get();
			map<string, int>::iterator it = tab->find(coco_string_create_char(t->val));
			if(it != tab->end()){
			   p = it->second;
			}else{
			   p = 0;
			   printf("Unknowen var\n");
			 }
			
		} else if (la->kind == 15) {
			Get();
			Expr(p);
			Expect(16);
		} else SynErr(18);
}



void Parser::Parse() {
	t = NULL;
	la = dummyToken = new Token();
	la->val = coco_string_create(L"Dummy Token");
	Get();
	Calc();

	Expect(0);
}

Parser::Parser(Scanner *scanner) {
	maxT = 17;

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

	static bool set[1][19] = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x}
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
			case 3: s = coco_string_create(L"\":=\" expected"); break;
			case 4: s = coco_string_create(L"\";\" expected"); break;
			case 5: s = coco_string_create(L"\"display\" expected"); break;
			case 6: s = coco_string_create(L"\":hex;\" expected"); break;
			case 7: s = coco_string_create(L"\":oct;\" expected"); break;
			case 8: s = coco_string_create(L"\"halt\" expected"); break;
			case 9: s = coco_string_create(L"\"+\" expected"); break;
			case 10: s = coco_string_create(L"\"-\" expected"); break;
			case 11: s = coco_string_create(L"\"*\" expected"); break;
			case 12: s = coco_string_create(L"\"/\" expected"); break;
			case 13: s = coco_string_create(L"\"%\" expected"); break;
			case 14: s = coco_string_create(L"\"^\" expected"); break;
			case 15: s = coco_string_create(L"\"(\" expected"); break;
			case 16: s = coco_string_create(L"\")\" expected"); break;
			case 17: s = coco_string_create(L"??? expected"); break;
			case 18: s = coco_string_create(L"invalid Expon"); break;

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



