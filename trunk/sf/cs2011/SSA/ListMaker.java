import java.util.InputMismatchException;
import java.util.Scanner;
class ListMaker{

   static Scanner sc = new Scanner(System.in);
   public static void main(String Args[]){
     List shoppinglist = new Singleperson(); //Init here to avoid errors
     
    
     shoppinglist = setProfile(shoppinglist);
     System.out.printf("\n%s selected\n", shoppinglist); 
     Item finalList = getShoppingList(shoppinglist);
     System.out.printf("\n%s\n",finalList); //Calls the Items toString method and prints the result

   }


   //Set shopper profile by casting list to profile selected by user
   public static List setProfile(List list){
     int selection;

     boolean valid = false;
     
     while(!valid){
        try{
            System.out.printf("Please select profile type:\nRetired(1)\nSingle(2)\nMarried(3)\nChoose :");
            selection = sc.nextInt();
            valid = true; //Assume true for the moment
        }catch (InputMismatchException InputMismatchException){
            selection = 0; //Stop the program from crashing, but keep "invalid" input
            sc.nextLine(); //Discard input so user can try again
                           //Using the switch to handle invalid input
        }
        switch(selection){
           case 1 : list = new Retired(); break;
           case 2 : list = new Singleperson(); break;
           case 3 : list = new MarriedCouple(); break;
           default :
              System.out.printf("Invalid choice\n"); 
              valid = false;
              break;
        }
     }
     return list;
   }

   //Returns the given lists Item(s)
   public static Item getShoppingList(List list){
      int selection;
      Item result = list.getWeekday();  //Init here to get the compiler to shut up
      boolean valid = false;
      while(!valid){
         try{
            System.out.printf("Please enter circumstances:\nWeekday(1)\nWeekend(2)\n");
            System.out.printf("Summer Holiday(3)\nWinter Holiday(4)\nChoose :");
            selection = sc.nextInt();
            valid = true; 
         }catch (InputMismatchException InputMismatchException){
            selection = 0;
            sc.nextLine();
         }
         switch(selection){
            case 1 : result = list.getWeekday(); break;
            case 2 : result = list.getWeekend(); break;
            case 3 : result = list.getSummer(); break;
            case 4 : result = list.getWinter(); break;
            default :
               System.out.printf("Invalid choice\n");
               valid = false;
               break;
         }
      }
      return result;
   }


  } //End class ListMaker
      
//}
