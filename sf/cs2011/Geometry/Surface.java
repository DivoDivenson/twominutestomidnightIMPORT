class Surface extends Geometry{

   private String type = "Surface";

   public Surface(){
    
   }

   public String geometryType(){
      return type;
   }

   public String asText(){
      return type;
   }

   //All other elements outlined in the spec require spatial reference system?


}
