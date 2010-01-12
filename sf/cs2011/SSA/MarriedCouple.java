public class MarriedCouple extends List{

   public Item getWeekday(){
      return new Item("Baby Food","Pasta","Chicken","Milk","Nappies");
   }

   public Item getWeekend(){
      return new Item("Steak","Sugar Puffs","Fruit Juice","TV Guide","7up");
   }

   public Item getSummer(){
      return new Item("Sun Protection","Shades","Bucket","Spade","Kids Hat");
   }

   public Item getWinter(){
      return new Item("Scarf","Hat","Kids Gloves","Firelog","Board Game");
   }

   public String toString(){
      return "Married Couple";
   }

}
