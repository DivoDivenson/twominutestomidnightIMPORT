using System;
	public int size;		//Size of data in memory. Only really used for arrays
		obj.size = size;
			//Console.WriteLine(topScope.nextAdr);
			obj.adr = topScope.nextAdr++;
			//Console.WriteLine("Address of " + obj.name + " is " + obj.adr);
			topScope.nextAdr += size -1; //Reserve space on the stack it an array is being created.
					
		}	
		
		//Console.WriteLine("Object stored at: " +name + " : "+ obj.adr);

	public Obj NewObj (string name, int kind, int type){
           return NewObj (name,  kind,  type, 1);
	}

	