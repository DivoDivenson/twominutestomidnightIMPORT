using System;


	NEQU, LSSEQ, GTREQ, DEBUG, WRITEC //Additional relational operators

	public void printStack(){
		int i;
		Console.WriteLine("Printing stack");
		for(i = 0; i < stack.Length; i++){
			Console.WriteLine(i + " : " + stack[i]);
		}			
	}

	public void printCode(){
		int i;
		Console.WriteLine("Printing Code");
		for(i = 0; i < code.Length; i++){
			Console.WriteLine(i + " : " + code[i]);
		}			
	} 
				case Op.WRITEC:

		//Console.WriteLine("Next2 " + x + " : " + y + " and " + (x<<8));
		//Console.WriteLine("Interpreting");

					//Additional relational operators. LSS and GTR are backwards
					// because of the way items are stored on the stack
					case Op.NEQU:  Push(Int(Pop()!=Pop())); break;
					case Op.LSSEQ: Push(Int(Pop()>=Pop())); break;
					case Op.GTREQ: Push(Int(Pop()<=Pop())); break;

					//printStack();
					//printCode(); 
					return;} break;
					case Op.DEBUG: Console.WriteLine("Top of stack: " + top +"."); break;
					case Op.WRITEC: Console.WriteLine((char)Pop()); break;
		
