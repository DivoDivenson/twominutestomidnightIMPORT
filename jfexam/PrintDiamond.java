import java.lang.*;
class PrintDiamond{
//Diamond diamond = new Diamond();

  public static void main(String args[]){
   int firstArg;
    firstArg = Integer.parseInt(args[0]);
    System.out.println(Integer.toString(firstArg));
    Diamond diamond = new Diamond(firstArg); 
    }

}
