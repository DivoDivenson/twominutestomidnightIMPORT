import java.io.*; //Meh, too lazy to import indivdual libs
import java.util.BitSet;

class Main{


   
   public static void main(String args[]){
   
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      BitSet inputSet;
      
      //Allow user to pass input as a param.
      if(args.length > 0){
      inputSet = stringSet(args[0]);
      }else{
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

   }

   public static BitSet stringSet(String input){
      BitSet result = new BitSet();
      for(int length = input.length() -1; length >= 0; length--){
         if(input.charAt(length) == '1'){
            result.set(length);
         }
      }
      System.out.printf("%s\n",result);
      return result;

   }

}
