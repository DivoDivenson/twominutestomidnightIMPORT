class Surface extends Gemoetry{

  private String type = "Surface";

  public Surface(){
    
  }

  /*public double Area(){   Requiers spatial thing-a-whck, leave out?

  }*/

  public Point centroid(){
    return -1; //-1 if unknown
  }

  /*public Point point_on_surface(){
    
  }*/

  public String geometryType(){
    return type;
  }

  public String asText(){
    return type;
  }




}
