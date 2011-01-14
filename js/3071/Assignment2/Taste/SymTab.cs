using System;namespace Taste {public class Obj {  // object describing a declared name	public string name;		// name of the object	public int type;			// type of the object (undef for proc)	public Obj	next;			// to next object in same scope	public int kind;      // var, proc, scope	public int adr;				// address in memory or start of proc	public int level;			// nesting level; 0=global, 1=local	public Obj locals;		// scopes: to locally declared objects	public int nextAdr;		// scopes: next free address in this scope
	public int size;		//Size of data in memory. Only really used for arrays}public class SymbolTable {	const int // types		undef = 0, integer = 1, boolean = 2, character =3;	const int // object kinds		var = 0, proc = 1, scope = 2, constant =3,array = 4;	public int curLevel;	// nesting level of current scope	public Obj undefObj;	// object node for erroneous symbols	public Obj topScope;	// topmost procedure scope		Parser parser;		public SymbolTable(Parser parser) {		this.parser = parser;		topScope = null;		curLevel = -1;		undefObj = new Obj();		undefObj.name  =  "undef"; undefObj.type = undef; undefObj.kind = var;		undefObj.adr = 0; undefObj.level = 0; undefObj.next = null;	}	// open a new scope and make it the current scope (topScope)	public void OpenScope () {		Obj scop = new Obj();		scop.name = ""; scop.kind = scope; 		scop.locals = null; scop.nextAdr = 0;		scop.next = topScope; topScope = scop; 		curLevel++;	}	// close the current scope	public void CloseScope () {		topScope = topScope.next; curLevel--;	}	// create a new object node in the current scope	public Obj NewObj (string name, int kind, int type, int size){		Obj p, last, obj = new Obj();		obj.name = name; obj.kind = kind; obj.type = type;		obj.level = curLevel;		p = topScope.locals; last = null;
		obj.size = size;		while (p != null) { 			if (p.name == name) parser.SemErr("name declared twice");			last = p; p = p.next;		}		if (last == null) topScope.locals = obj; else last.next = obj;		if (kind == var) {
			//Console.WriteLine(topScope.nextAdr);
			obj.adr = topScope.nextAdr++;
			//Console.WriteLine("Address of " + obj.name + " is " + obj.adr);
			topScope.nextAdr += size -1;
					
		}	
		
		//Console.WriteLine("Object stored at: " +name + " : "+ obj.adr);		return obj;	}

	public Obj NewObj (string name, int kind, int type){
           return NewObj (name,  kind,  type, 1);
	}

		// search the name in all open scopes and return its object node	public Obj Find (string name) {		Obj obj, scope;		scope = topScope;		while (scope != null) {  // for all open scopes			obj = scope.locals;			while (obj != null) {  // for all objects in this scope				if (obj.name == name) return obj;				obj = obj.next;			}			scope = scope.next;		}		parser.SemErr(name + " is undeclared");		return undefObj;	}} // end SymbolTable} // end namespace
