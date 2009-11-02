class test{
   public static void main(String Args[]){
      /*Point Methods */

      System.out.println("Demo all Point methods.\n");
      Point point = new Point(5.0,6.9);
      System.out.printf("%s\n%f %f\n%s\n",point.geometryType(), point.x(), point.y(),point.asText());
      /*The final element of the printf argument demos Inheritance.
        asText() returns the objects type, as well as its superclasses type*/
      
      /*Polygon Methods */
      System.out.printf("\nDemo all polygon methods.\n");
      Polygon poly = new Polygon();

      poly.addPoint(5.6,4.8);
      poly.addPoint(3.7,4.0);
      poly.addPoint(10.9,13.6);
      System.out.printf("\n%s\n%s\n",poly.asText(),poly.showPoints());
      
      /*Surface Methods */
      System.out.printf("\nDemo all surface methods\n");
      Surface surface = new Surface();
      System.out.printf("Geometry Type: %s\nType: %s\n", surface.geometryType(), surface.asText());


      /* Demo Polymorphism by accessing a subclass method through a superclass variable.
         Allowed because asText() is a member of both the super and sub class.
         showPoints() is a subclass only member, and so calling it through a superclass varible would be a
         compiler error. */
      surface = poly;
      System.out.printf("\nDemo Polymorphism\n%s\n", surface.asText());
   }
}
