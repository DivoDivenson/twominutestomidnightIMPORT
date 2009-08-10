import java.util.Scanner;

public class smlMachine{
  
  int memory[] = new int[100];
  int accumulator = 0;
  int instCount, opCode, operand;
  int  insRegister;
  static Scanner sc = new Scanner(System.in);
 
  public void smlStart(){ // Start reads in the comands from the user and stores them in memory
    int input = 0;
    do{
	System.out.print( Integer.toString(operand) + "? ");
	memory[operand] = sc.nextInt();
	if(memory[operand] < 9999 && memory[operand] > -9999){
	  operand++;
	  }
	} while(memory[operand] != -99999);
    memory[operand] = 0; //Delete the -99999
    operand = 0;
    smlProcess();
    System.out.println("###########Now dumping memory###########");
    for(int x = 0; x < memory.length;x++){
	System.out.println(Integer.toString(x) + " : " + Double.toString(memory[x]));
	}
  }

  public void smlProcess(){
    do{
	insRegister = memory[instCount];
	//System.out.println(Integer.toString(accumulator));
	  if(insRegister == 0){
	    System.out.println("Divide by zero warning! Will now terminate");
	    opCode = 43;
	  }else{
	    opCode = insRegister / 100;
	    operand = insRegister % 100;
	    smlExecute();
	    instCount++;
	  }
	} while (opCode != 43);
  }

  public void smlExecute(){
    //System.out.println("here i am");	
    switch(opCode){
	case 10: memory[operand] = sc.nextInt(); break;
	case 11: System.out.println(Double.toString(memory[operand])); break;
	case 20: accumulator = memory[operand]; break;
	case 21: memory[operand] = accumulator; break;
	case 30: accumulator += memory[operand]; break;
	case 31: accumulator -= memory[operand]; break;
	case 32: accumulator /= memory[operand]; break;
	case 33: accumulator *= memory[operand]; break;
	case 40:{
	   instCount = operand; 
	   instCount--;
	   break; //instCount deincremented to offset increment
	}
	case 41:{
	  if(accumulator < 0){
	    instCount = operand; 
	    instCount--;
	  }
	}
	case 42:{
	  if(accumulator == 0){
	   instCount = operand; 
	   instCount--;
	  }
	}
	
    }
  }
} 
		
