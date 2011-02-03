/*----------------------------------------------------------------------
Compiler Generator Coco/R,
Copyright (c) 1990, 2004 Hanspeter Moessenboeck, University of Linz
extended by M. Loeberbauer & A. Woess, Univ. of Linz
with improvements by Pat Terry, Rhodes University

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by the 
Free Software Foundation; either version 2, or (at your option) any 
later version.

This program is distributed in the hope that it will be useful, but 
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
for more details.

You should have received a copy of the GNU General Public License along 
with this program; if not, write to the Free Software Foundation, Inc., 
59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

As an exception, it is allowed to write an extension of Coco/R that is
used as a plugin in non-free software.

If not otherwise stated, any source code generated by Coco/R (other than 
Coco/R itself) does not fall under the GNU General Public License.
-----------------------------------------------------------------------*/

using System;
using System.Collections;

namespace Taste {



public class Parser {
	public const int _EOF = 0;
	public const int _ident = 1;
	public const int _number = 2;
	public const int _character = 3;
	public const int maxT = 44;

	const bool T = true;
	const bool x = false;
	const int minErrDist = 2;
	
	public Scanner scanner;
	public Errors  errors;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;

const int // types
	  undef = 0, integer = 1, boolean = 2, character = 3;

	const int // object kinds
	  var = 0, proc = 1, constant =3, array = 4;

	public SymbolTable   tab;
	public CodeGenerator gen;
  
/*--------------------------------------------------------------------------*/


	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (string msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) { ++errDist; break; }

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	bool StartOf (int s) {
		return set[s, la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}


	bool WeakSeparator(int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) {Get(); return true;}
		else if (StartOf(repFol)) {return false;}
		else {
			SynErr(n);
			while (!(set[syFol, kind] || set[repFol, kind] || set[0, kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}

	
	void AddOp(out Op op) {
		op = Op.ADD; 
		if (la.kind == 4) {
			Get();
		} else if (la.kind == 5) {
			Get();
			op = Op.SUB; 
		} else SynErr(45);
	}

	void Expr(out int type) {
		int type1; Op op; 
		SimExpr(out type);
		if (StartOf(1)) {
			RelOp(out op);
			SimExpr(out type1);
			if (type != type1) SemErr("incompatible types");
			gen.Emit(op); type = boolean; 
		}
	}

	void SimExpr(out int type) {
		int type1; Op op; 
		Term(out type);
		while (la.kind == 4 || la.kind == 5) {
			AddOp(out op);
			Term(out type1);
			if (type != integer || type1 != integer) 
			 SemErr("integer type expected");
			gen.Emit(op); 
		}
	}

	void RelOp(out Op op) {
		op = Op.EQU; 
		switch (la.kind) {
		case 17: {
			Get();
			break;
		}
		case 18: {
			Get();
			op = Op.LSS; 
			break;
		}
		case 19: {
			Get();
			op = Op.GTR; 
			break;
		}
		case 20: {
			Get();
			op = Op.NEQU; 
			break;
		}
		case 21: {
			Get();
			op = Op.LSSEQ; 
			break;
		}
		case 22: {
			Get();
			op = Op.GTREQ; 
			break;
		}
		default: SynErr(46); break;
		}
	}

	void Factor(out int type) {
		int n; Obj obj; string name; 
		type = undef; 
		switch (la.kind) {
		case 1: {
			Ident(out name);
			obj = tab.Find(name); type = obj.type; n =0;
			
			if (la.kind == 6) {
				Get();
				Expect(2);
				n = Convert.ToInt32(t.val);
				if(n < 0 || n > obj.size) SemErr("Array index out of bounds"); 
				Expect(7);
			}
			if (obj.kind == var || obj.kind == constant) {
			if (obj.level == 0) gen.Emit(Op.LOADG, obj.adr + n);
			else gen.Emit(Op.LOAD, obj.adr + n);
			                   } else SemErr("variable expected or constant");
			                   
			break;
		}
		case 2: {
			Get();
			n = Convert.ToInt32(t.val); 
			gen.Emit(Op.CONST, n); type = integer; 
			break;
		}
		case 3: {
			Get();
			gen.Emit(Op.CONST, t.val.ToCharArray()[1]); //Pull out the charater from between the ' '
			type = character;
			   
			break;
		}
		case 5: {
			Get();
			Factor(out type);
			if (type != integer) {
			 SemErr("integer type expected"); type = integer;
			}
			gen.Emit(Op.NEG); 
			break;
		}
		case 8: {
			Get();
			gen.Emit(Op.CONST, 1); type = boolean; 
			break;
		}
		case 9: {
			Get();
			gen.Emit(Op.CONST, 0); type = boolean; 
			break;
		}
		default: SynErr(47); break;
		}
	}

	void Ident(out string name) {
		Expect(1);
		name = t.val; 
	}

	void MulOp(out Op op) {
		op = Op.MUL; 
		if (la.kind == 10) {
			Get();
		} else if (la.kind == 11) {
			Get();
			op = Op.DIV; 
		} else SynErr(48);
	}

	void ProcDecl() {
		string name; Obj obj; int adr; 
		Expect(12);
		Ident(out name);
		obj = tab.NewObj(name, proc, undef); obj.adr = gen.pc;
		if (name == "Main") gen.progStart = gen.pc; 
		tab.OpenScope(); 
		Expect(13);
		Expect(14);
		Expect(15);
		gen.Emit(Op.ENTER, 0); adr = gen.pc - 2; 
		while (StartOf(2)) {
			if (la.kind == 39 || la.kind == 40 || la.kind == 41) {
				VarDecl();
			} else if (StartOf(3)) {
				Stat();
			} else {
				ConstDecl();
			}
		}
		Expect(16);
		gen.Emit(Op.LEAVE); gen.Emit(Op.RET);
		gen.Patch(adr, tab.topScope.nextAdr);
		tab.CloseScope(); 
	}

	void VarDecl() {
		string name; int type; int kind = var; int size =1;/*Size is a bad solution, fix later*/ 
		Type(out type);
		Ident(out name);
		if (la.kind == 6) {
			Get();
			Expect(2);
			size = Convert.ToInt32(t.val); 
			Expect(7);
		}
		tab.NewObj(name, kind, type, size);
		while (la.kind == 42) {
			Get();
			Ident(out name);
			tab.NewObj(name, var, type); 
		}
		Expect(24);
	}

	void Stat() {
		int type,type1,type2; string name; Obj obj;
		string name2; Obj obj2;
		int adr, adr2, loopstart; 
		int loopUpdate; 
		switch (la.kind) {
		case 1: {
			Ident(out name);
			obj = tab.Find(name); 
			if (la.kind == 23) {
				Get();
				if (obj.kind != var) SemErr("cannot assign to procedure or constant");
				if (obj.size != 1) SemErr("Array index expected"); //If this line is removed, array[x] = 10; (array = 5;) is equiv to (array[0] = 5;)
				 
				Expr(out type);
				if (StartOf(4)) {
					if (la.kind == 24) {
						Get();
					}
					if (type != obj.type) SemErr("incompatible types");
					if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
					else gen.Emit(Op.STO, obj.adr); 
				} else if (la.kind == 25) {
					if(type != boolean) SemErr("Boolean expression expected");
					gen.Emit(Op.FJMP, 0); adr = gen.pc -2; 
					if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
					else gen.Emit(Op.STO, obj.adr); 
					Get();
					Expr(out type1);
					Expect(26);
					gen.Emit(Op.JMP,0); adr2 = gen.pc -2;
							   gen.Patch(adr,gen.pc); adr = adr2;
					if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
					else gen.Emit(Op.STO, obj.adr); 
						 
					Expr(out type2);
					Expect(24);
					gen.Patch(adr, gen.pc); 
				} else SynErr(49);
			} else if (la.kind == 13) {
				Get();
				Expect(14);
				Expect(24);
				if (obj.kind != proc) SemErr("object is not a procedure");
				gen.Emit(Op.CALL, obj.adr); 
			} else if (la.kind == 6) {
				Get();
				if (la.kind == 1) {
					Ident(out name);
					obj2 = tab.Find(name); //obj2 is index, obj is array start
					if(obj.type != integer) SemErr("Integer expected as index");
					
					                      
					Expect(7);
					Expect(23);
					if(obj.level == 0) gen.Emit(Op.LOADG, obj2.adr);
					              else gen.Emit(Op.LOAD, obj2.adr); 
					Expr(out type1);
					Expect(24);
					if(type1 != obj.type) SemErr("incompatible types");
					     
					                         // gen.Emit(Op.ADD);
					                          gen.Emit(Op.STO, obj.adr);
					                           
				} else if (la.kind == 2) {
					Get();
					adr = Convert.ToInt32(t.val);
					if(adr < 0 || adr > obj.size) SemErr("Array index out of bounds");
					Expect(7);
					Expect(23);
					Expr(out type1);
					Expect(24);
					gen.Emit(Op.STO, obj.adr + adr); 
				} else SynErr(50);
			} else SynErr(51);
			break;
		}
		case 27: {
			Get();
			Expect(13);
			Ident(out name);
			obj = tab.Find(name);  adr2 = 0; 
			ArrayList jumps = new ArrayList(); //Arraylist is used to store the address of the jmp instructions after a "break;"
			if(obj.type != integer) SemErr("Integer type expected");
			if(obj.level == 0) gen.Emit(Op.LOADG, obj.adr);
			else gen.Emit(Op.LOAD, obj.adr);
			
			Expect(14);
			Expect(15);
			Expect(28);
			Expr(out type);
			if(type != obj.type) SemErr("Incompatible types in case"); 
			gen.Emit(Op.EQU); 
			gen.Emit(Op.FJMP, 0); adr = gen.pc -2;
			Expect(26);
			Stat();
			if (la.kind == 29) {
				Get();
				gen.Emit(Op.JMP, 0); adr2 = 1; //Record a break has been used
				jumps.Add((gen.pc -2)); 
				
			}
			while (la.kind == 28) {
				Get();
				gen.Patch(adr, gen.pc);
				if(obj.level == 0) gen.Emit(Op.LOADG, obj.adr);
				else gen.Emit(Op.LOAD, obj.adr); 
				Expr(out type);
				if(type != obj.type) SemErr("Incompatible types in case"); 
				gen.Emit(Op.EQU); 
				gen.Emit(Op.FJMP, 0); adr = gen.pc -2;
				Expect(26);
				Stat();
				if (la.kind == 29) {
					Get();
					gen.Emit(Op.JMP, 0); adr2 =1; jumps.Add((gen.pc -2)); 
					
				}
			}
			if (la.kind == 30) {
				Get();
				Expect(26);
				gen.Patch(adr, gen.pc); 
				Stat();
				if (la.kind == 29) {
					Get();
				}
			}
			Expect(16);
			gen.Patch(adr, gen.pc);
			if(adr2 != 0){
			   foreach(int i in jumps){
			      gen.Patch(i, gen.pc);
			   }
			}
			
			break;
		}
		case 31: {
			Get();
			Expect(13);
			Expr(out type);
			Expect(14);
			if (type != boolean) SemErr("boolean type expected");
			gen.Emit(Op.FJMP, 0); adr = gen.pc - 2; 
			Stat();
			if (la.kind == 32) {
				Get();
				gen.Emit(Op.JMP, 0); adr2 = gen.pc - 2;
				gen.Patch(adr, gen.pc);  //Patch the address to branch to should the boolean check fail
				adr = adr2; 
				Stat();
			}
			gen.Patch(adr, gen.pc); 
			break;
		}
		case 33: {
			Get();
			loopstart = gen.pc; 
			Expect(13);
			Expr(out type);
			Expect(14);
			if (type != boolean) SemErr("boolean type expected");
			gen.Emit(Op.FJMP, 0); adr = gen.pc - 2; 
			Stat();
			gen.Emit(Op.JMP, loopstart); gen.Patch(adr, gen.pc); 
			break;
		}
		case 34: {
			Get();
			loopstart = gen.pc; 
			Stat();
			Expect(33);
			Expect(13);
			Expr(out type);
			Expect(14);
			if(type != boolean) SemErr("Boolean type expected");
			gen.Emit(Op.FJMP, 0); adr = gen.pc -2;
			gen.Emit(Op.JMP, loopstart);
			gen.Patch(adr, gen.pc); 
			break;
		}
		case 35: {
			Get();
			Ident(out name);
			Expect(24);
			obj = tab.Find(name);
			if (obj.type != integer && obj.type != character) SemErr("integer type expected");
			gen.Emit(Op.READ);
			if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
			else gen.Emit(Op.STO, obj.adr); 
			break;
		}
		case 36: {
			Get();
			Expr(out type);
			Expect(24);
			if (type == integer) gen.Emit(Op.WRITE);
			else if(type == character) gen.Emit(Op.WRITEC);
			else SemErr("integer or character type expected");
			
			break;
		}
		case 37: {
			Get();
			Expect(13);
			Stat();
			loopstart = gen.pc; 
			Expr(out type);
			Expect(24);
			if(type != boolean) SemErr("boolean type expected");
			gen.Emit(Op.FJMP, 0); adr = gen.pc -2; //Jump if check fails
			gen.Emit(Op.JMP, 0); adr2 = gen.pc -2; //Jump to loop body
			loopUpdate = gen.pc; 
			Stat();
			gen.Emit(Op.JMP, loopstart); 
			Expect(14);
			gen.Patch(adr2, gen.pc); 
			Stat();
			gen.Emit(Op.JMP, loopUpdate); gen.Patch(adr, gen.pc); 
			break;
		}
		case 15: {
			Get();
			while (StartOf(2)) {
				if (StartOf(3)) {
					Stat();
				} else if (la.kind == 39 || la.kind == 40 || la.kind == 41) {
					VarDecl();
				} else {
					ConstDecl();
				}
			}
			Expect(16);
			break;
		}
		default: SynErr(52); break;
		}
	}

	void ConstDecl() {
		string name; int type; int n; Obj obj; 
		Expect(43);
		Type(out type);
		Ident(out name);
		obj = tab.NewObj(name, constant, type); 
		Expect(23);
		if (la.kind == 2) {
			Get();
			n = Convert.ToInt32(t.val); 
			  gen.Emit(Op.CONST, n); type = integer;
			if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
			else gen.Emit(Op.STO, obj.adr);
			Console.WriteLine("Constant " + n + " : " + obj.type); 
			
		} else if (la.kind == 5) {
			Get();
			Expect(2);
			n = Convert.ToInt32(t.val); 
			      n = n * -1; //Use negitive flag
			gen.Emit(Op.CONST, n); type = integer;
			if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
			else gen.Emit(Op.STO, obj.adr); 
		} else if (la.kind == 3) {
			Get();
			gen.Emit(Op.CONST, t.val.ToCharArray()[1]); //Pull out the charater from between the ' '
			type = character; 
			   
		} else if (la.kind == 8) {
			Get();
			gen.Emit(Op.CONST, 1); type = boolean;
			if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
			else gen.Emit(Op.STO, obj.adr);
			
		} else if (la.kind == 9) {
			Get();
			gen.Emit(Op.CONST, 0); type = boolean;
			if (obj.level == 0) gen.Emit(Op.STOG, obj.adr);
			else gen.Emit(Op.STO, obj.adr); 
		} else SynErr(53);
		Expect(24);
	}

	void Term(out int type) {
		int type1; Op op; 
		Factor(out type);
		while (la.kind == 10 || la.kind == 11) {
			MulOp(out op);
			Factor(out type1);
			if (type != integer || type1 != integer) 
			 SemErr("integer type expected");
			gen.Emit(op); 
		}
	}

	void Taste() {
		string name; 
		Expect(38);
		Ident(out name);
		tab.OpenScope(); 
		Expect(15);
		while (StartOf(5)) {
			if (la.kind == 39 || la.kind == 40 || la.kind == 41) {
				VarDecl();
			} else if (la.kind == 43) {
				ConstDecl();
			} else {
				ProcDecl();
			}
		}
		Expect(16);
		tab.CloseScope();
		if (gen.progStart == -1) SemErr("main function never defined");
		
	}

	void Type(out int type) {
		type = undef; 
		if (la.kind == 39) {
			Get();
			type = integer; 
		} else if (la.kind == 40) {
			Get();
			type = boolean; 
		} else if (la.kind == 41) {
			Get();
			type = character; 
		} else SynErr(54);
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Taste();
		Expect(0);

	}
	
	static readonly bool[,] set = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,T,T,T, T,T,T,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,T, x,T,T,T, T,T,x,T, T,T,x,T, x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,T, x,T,T,T, T,T,x,x, x,x,x,x, x,x},
		{x,T,T,T, x,T,x,x, T,T,x,x, x,x,T,T, T,x,x,x, x,x,x,x, T,x,x,T, T,T,T,T, T,T,T,T, T,T,x,T, T,T,x,T, x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,T, T,T,x,T, x,x}

	};
} // end Parser


public class Errors {
	public int count = 0;                                    // number of errors detected
	public System.IO.TextWriter errorStream = Console.Out;   // error messages go to this stream
	public string errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text

	public virtual void SynErr (int line, int col, int n) {
		string s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "number expected"; break;
			case 3: s = "character expected"; break;
			case 4: s = "\"+\" expected"; break;
			case 5: s = "\"-\" expected"; break;
			case 6: s = "\"[\" expected"; break;
			case 7: s = "\"]\" expected"; break;
			case 8: s = "\"true\" expected"; break;
			case 9: s = "\"false\" expected"; break;
			case 10: s = "\"*\" expected"; break;
			case 11: s = "\"/\" expected"; break;
			case 12: s = "\"void\" expected"; break;
			case 13: s = "\"(\" expected"; break;
			case 14: s = "\")\" expected"; break;
			case 15: s = "\"{\" expected"; break;
			case 16: s = "\"}\" expected"; break;
			case 17: s = "\"==\" expected"; break;
			case 18: s = "\"<\" expected"; break;
			case 19: s = "\">\" expected"; break;
			case 20: s = "\"!=\" expected"; break;
			case 21: s = "\"<=\" expected"; break;
			case 22: s = "\">=\" expected"; break;
			case 23: s = "\"=\" expected"; break;
			case 24: s = "\";\" expected"; break;
			case 25: s = "\"?\" expected"; break;
			case 26: s = "\":\" expected"; break;
			case 27: s = "\"switch\" expected"; break;
			case 28: s = "\"case\" expected"; break;
			case 29: s = "\"break;\" expected"; break;
			case 30: s = "\"default\" expected"; break;
			case 31: s = "\"if\" expected"; break;
			case 32: s = "\"else\" expected"; break;
			case 33: s = "\"while\" expected"; break;
			case 34: s = "\"do\" expected"; break;
			case 35: s = "\"read\" expected"; break;
			case 36: s = "\"write\" expected"; break;
			case 37: s = "\"for\" expected"; break;
			case 38: s = "\"program\" expected"; break;
			case 39: s = "\"int\" expected"; break;
			case 40: s = "\"bool\" expected"; break;
			case 41: s = "\"char\" expected"; break;
			case 42: s = "\",\" expected"; break;
			case 43: s = "\"const\" expected"; break;
			case 44: s = "??? expected"; break;
			case 45: s = "invalid AddOp"; break;
			case 46: s = "invalid RelOp"; break;
			case 47: s = "invalid Factor"; break;
			case 48: s = "invalid MulOp"; break;
			case 49: s = "invalid Stat"; break;
			case 50: s = "invalid Stat"; break;
			case 51: s = "invalid Stat"; break;
			case 52: s = "invalid Stat"; break;
			case 53: s = "invalid ConstDecl"; break;
			case 54: s = "invalid Type"; break;

			default: s = "error " + n; break;
		}
		errorStream.WriteLine(errMsgFormat, line, col, s);
		count++;
	}

	public virtual void SemErr (int line, int col, string s) {
		errorStream.WriteLine(errMsgFormat, line, col, s);
		count++;
	}
	
	public virtual void SemErr (string s) {
		errorStream.WriteLine(s);
		count++;
	}
	
	public virtual void Warning (int line, int col, string s) {
		errorStream.WriteLine(errMsgFormat, line, col, s);
	}
	
	public virtual void Warning(string s) {
		errorStream.WriteLine(s);
	}
} // Errors


public class FatalError: Exception {
	public FatalError(string m): base(m) {}
}
}