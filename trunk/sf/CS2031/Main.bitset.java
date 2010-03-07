import java.io.*; //Meh, too lazy to import indivdual libs
import java.util.BitSet;

class Main{


   
   public static void main(String args[]){
   
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      BitSet inputSet;

      Parity parityCheck = new Parity();
      
      //Allow user to pass input as a param.
      if(args.length > 0){
      inputSet = stringSet(args[0]);
      }else{
         //If noargument supplied, ask user for input
         System.out.printf("Please enter in the binary string :\n");
         String inputString = null;
         try{
            inputString = in.readLine();
         }catch(IOException ioexc){
            System.out.printf("Oh noes!\n%s",ioexc);
            System.exit(1);
         }
         
         inputSet = stringSet(inputString);
      }

      parityCheck.bitset2D(inputSet);

   }

   public static BitSet stringSet(String input){
      BitSet result = new BitSet(7);
      //Check the input string is a multiple of 7
      if((input.length() % 7) != 0){
            System.out.printf("Please enter a multiple of 7\n");
            System.exit(1);
         }
      //Write the string into a bitset
      for(int length = input.length() -1; length >= 0; length--){
         if(input.charAt(length) == '1'){
            result.set(length);
         }else if(input.charAt(length) != '0'){
            System.out.printf("Please enter a string of bits\n");
            System.exit(1);
         }else{
            result.clear(length);
         }
      }
      //System.out.printf("%s\n",result.cardinality());
      return result;

   }

}
