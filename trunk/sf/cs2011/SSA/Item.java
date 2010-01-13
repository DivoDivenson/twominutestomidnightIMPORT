public class Item{ //Simply stores 5 strings
   String[] items = new String[5]; //Space for 5 items as per spec. Any more are simply dropped

   public Item(String item0, String item1, String item2, String item3, String item4){ 
      /*Just have all items as constructor arguments to save time */
      items[0] = item0;
      items[1] = item1;
      items[2] = item2;
      items[3] = item3;
      items[4] = item4;
   }

   //Main purpose of this class is to provide a quick method get a string representation
   //of the array or "shopping list"
   public String toString(){
      String result = "Items:\n";
      for(String element : items){
         result = String.format("%s\n%s",result,element);
      }
      return result;
   }

}
