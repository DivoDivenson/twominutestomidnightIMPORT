/* Steven Diviney
   08462267 */

import java.io.*; //Meh, too lazy to import indivdual libs

class Main{


  
   //This program is so stupidly complicated. I hate it
   public static void main(String args[]){
   
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      SetOperations SetOps = new SetOperations();
     
      String inputString = null;
      //Allow user to pass input as a param.
      if(args.length > 0){
         inputString = args[0];
      }else{
         //If noargument supplied, ask user for input
         System.out.printf("Please enter in the binary string, no spaces :\n");
         try{
            inputString = in.readLine();
         }catch(IOException ioexc){
            System.out.printf("Oh noes!\n%s",ioexc);
            System.exit(1);
         }
         
      }

      //Check user enterd a valid set
      if(!SetOps.checkSet(inputString)){
         System.out.printf("Invalid set\n");
         System.exit(1);
      }

      //Take the input set and make it into a 2D array
      //This will make values easier to calcualte, print etc
      String grid[] = SetOps.toGrid(inputString);

      //Ask user which op to prefrom
      System.out.printf("Preform 2D parity check (1) ir Hamming Code calculation(2) :");
      boolean again = true;
      int choice = 1;
      while(again){
         try{
            choice = in.read();
            again = false;
         }catch(IOException ioex){
            System.out.printf("I'm sorry Dave, I can't do that\n");
         }
      }
      if(choice == '1'){
         for(int count = 0; count < grid.length -1; count++){ 
            grid[count] = SetOps.rowParity(grid[count]);
         }
         //Calcualte the parity of each row. -1 because last row is null. Ugh
         grid[grid.length -1] = SetOps.colParity(grid);
      }else if(choice == '2'){
         for(int count = 0; count < grid.length -1; count++){ 
            grid[count] = SetOps.hamming(grid[count]);
         }
      }
      printGrid(grid);

   }

   //Prints a _completed_ grid
   public static void printGrid(String grid[]){
      for(int count = 0; count < grid.length ; count++){ 
         System.out.printf("%d : %s\n",count,grid[count]);
      }
   }

}
