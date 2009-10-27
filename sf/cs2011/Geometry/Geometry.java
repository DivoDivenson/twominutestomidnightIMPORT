abstract class Geometry{
  
  private String type = "Geometry";

  public Geometry(){

  }

  public int dimension(){
    return -1; //-1 if unknown
  }

  public String geometryType(){
    return type;
  }

  public String asText(){
    return type + "()";
  }

  public int isEmpty(){
    return -1; //1 if true, 0 if false, -1 if unknown
  }

  /*public Geometry envelop(){
    
  }*/

}
