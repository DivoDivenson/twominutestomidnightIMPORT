class Point extends Geometry{
   private double x,y;
   private String type = "Point";

   public Point(double x,double y){
      this.x = x;
      this.y = y;
   }

   public double x(){
      return x;
   }

   public double y(){
      return y;
   }

   public String geometryType(){
      return type;
   }

   public String asText(){
      return String.format("%s X: %f Y: %f", type, x, y);  
   }

  /*Leaving out isEmpty and dimension as neither make sense for a point */

}
