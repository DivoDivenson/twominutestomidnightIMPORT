import java.util.ArrayList;

class Polygon extends Surface{
     
   private String type = "Polygon";
   Point[] Points = new Point[10]; //Quick container. Limits the type of polygon you might have of course.
   private int pointIndex = 0;

   public Polygon(){

   }   

   public void addPoint(double x, double y){
      Points[pointIndex++] = new Point(x,y);
   }

   public String showPoints(){
      String result = "Points:\n";
      for(int i = 0; i < pointIndex; i++){
         result = String.format("%s\n %s",result, Points[i].asText());
      }
      return result;
   }

   public String asText(){
      return String.format("%s: %s\n",super.asText(),type);
   }

}
