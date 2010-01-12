public class Retired extends List{
   
   public Item getWeekday(){
     return new Item("Fruit","TV Guide","Stew","Milk","Wine"); 
   }

   public Item getWeekend(){
      return new Item("Roast","Orange Juice","Oats","Book","Beans");
   }

   public Item getSummer(){
      return new Item("Strong Sun Protection","Book","Towel","Straw Hat","Water");
   }

   public Item getWinter(){
      return new Item("Hat","Scarf","Gloves","Brandy","Cake");
   }

   public String toString(){
      return "Retired Person";
   }

}


