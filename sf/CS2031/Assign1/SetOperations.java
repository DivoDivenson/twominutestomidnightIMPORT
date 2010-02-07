//Put the string as an atribute of the class and have the operations 
//be prefromed on it directly?
//Then again, user data methods etc...

class SetOperations{
   
   boolean checkSet(String input){
      boolean result = true;


      if(( input.length() % 7) != 0){
         result = false;
      }else{
         for(int i = 0; i < input.length(); i++){
            //WARNING: This next bit is a horrible hack and may cause brain malfunction
               int temp = getValue(input,i); //Ugh
               if(temp == 0 || temp == 1){
                  ;
               }else{
                  result = false;
               }
         }
      }

      return result;

   }


   String[] toGrid(String input){

      String result[] = new String[(input.length() / 7) + 1]; //+1 tp make room for col parity
      int rowi = 0;
      int coli = 0;
      int input_index = 0;

      //System.out.printf("%s\n",input.substring(0,14));
      while(input_index < input.length()){
        result[coli] = input.substring(input_index, (input_index + 7));
        input_index += 7;
        coli++;
      }

      return result;
      
   }


   //Calculate the parity of a row and write it to the end of a string
   String rowParity(String input){
   int count = 0;
      for(int x = 0; x < input.length();x++){
        count += getValue(input, x); 
      }
      input = String.format("%s%d",input,count % 2);
      return input;
   }


   private int getValue(String input, int index){
      return Character.getNumericValue(input.charAt(index));
   }

   String colParity(String input[]){
      //int coli = 0;
      int rowi = 0;
      int count = 0;
      String result = new String();

      while(rowi < 8){ //Everything else implictly relies on a valid set,
                        //May as well hard code it. Sigh
         for(int coli = 0; coli <= input.length -2; coli++){
            count += getValue(input[coli],rowi);
         }
         result = String.format("%s%d",result, count % 2);
         rowi++;
         count = 0;

      }

      return result;
   }

   //Strings of 7 "bits" only please
   String hamming(String input){
      String result;
      int i,r1,r2,r4,r8;
      //I could call check set here, but im gonna presum valid input
      //Parity of bits 11,9,7,5,3 (bits 7,5,4,2,1 in original input, 
      // the indexs may seem a bit insane, but java strings start at index 0,
      //ie, bit 7 is index 0. Yeah, its horrible but it works)
      r1 = getValue(input,0) + getValue(input,2) + getValue(input,3) + getValue(input,5) +
         getValue(input,6);
      r2 = getValue(input,0) + getValue(input,1) + getValue(input,3) + getValue(input,4) +
         getValue(input,6);
      r4 = getValue(input,3) + getValue(input,4) + getValue(input,5);
      r8 = getValue(input,0) + getValue(input,1) + getValue(input,2);
      //Write the result to a string
      result = String.format("%s%d",input,r2 % 2);
      System.out.printf("%s\n",result);
      result = String.format("%s%d",result,r1 % 2);
      //String.substring(inclusive,exclusive) Ugh. Better to read how hamming code works
      //and just take it as granted that those bits are indeed being accessed
      result = String.format("%s%d%s",result.substring(0,6),(r4 % 2), result.substring(6,9)); //Ugh
      result = String.format("%s%d%s",result.substring(0,3),(r8 % 2), result.substring(3,10));
      return result;


   }

   


}
