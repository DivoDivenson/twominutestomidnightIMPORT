class Test{

   public static void main(String Args[]){
      RingBuffer ringBuffer = new RingBuffer(); //Create ringBuffer of default size 5
      //Examples for integer
      for(int x = 0; x <= 6; x++){   //Quickly fill the ringBuffer with some ints
         ringBuffer.push(x);
      }
      System.out.printf("%s\n\n", ringBuffer); //Display all contents
      
      if(ringBuffer.equals(5)){            /*Uses class Object equal's method. Only works if the ringBuffer contains 
                                        a reference to the argument supplied. (Or just the value with primatives)*/
         System.out.printf(" RingBuffer contains %d\n",5);
      }

      ringBuffer.reset(); //RingBuffer is now empty
      System.out.printf("%s\n",ringBuffer);

      /* </integers>
         <String> Finally, half fill the ringBuffer to demo */
      ringBuffer.push("Hello");
      ringBuffer.push("World");
      System.out.printf("%s\n",ringBuffer);
      System.out.println("\nDemo the equals method for string:\n" + ringBuffer.equals("Hello"));

      /*</String>
        Finaly demo the ringBuffer for user created objects. Point*/
      RingBuffer secondRingBuffer = new RingBuffer(3); //Make ringBuffer of arbitray size
      secondRingBuffer.push(new Point(56.7,86.8)); //This is overwritten

      Point point = new Point(34.6,56.8);
      secondRingBuffer.push(point);
      secondRingBuffer.push(new Point(3,5));
      secondRingBuffer.push(new Point(12.5,123.6));
      System.out.printf("\n%s\n", secondRingBuffer);
      System.out.println("\nWith reference types, Object.equals() only returns true if the two objects"+
            " passed are a refernce to the same object.\n"+
            secondRingBuffer.equals(point) + " : "+
            secondRingBuffer.equals(new Point(34.6,56.8)));

      //</Point>
   }
}
