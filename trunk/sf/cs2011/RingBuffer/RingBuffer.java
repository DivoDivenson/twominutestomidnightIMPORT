
class RingBuffer<E>{
   private E[] elements;
   private int insert;
   private int start;  
   private int size = 5; 

   public RingBuffer(){                 //Default size of 5 if none specified
      elements = (E[]) new Object[size];
   }

   public RingBuffer(int inputSize){
      if(inputSize > 0){            //If negative supplied or zero, just make ringBuffer default size
         elements = (E[]) new Object[inputSize];
         size = inputSize;          //Set the size and adjust for use as an array index.
      }
      
   }

   public void push ( E input){     //Add a new element to the ringBuffer
      if(insert == size){           //If ringBuffer is full increment the start of the ringBuffer
         insert = 0;
         start++;
      }
      if(start == size){            //If start goes "full circle", reset it
         start = 0;
      }
      elements[insert++] = input;
      

   }


   public String toString(){
      String result ="The ringBuffer contains:";
      for(E element : elements)
         result =  String.format("%s\n%s", result, element);

      return result;
   }

   public void reset(){             //Reset the buffer.
      elements = (E[]) new Object[size];
      insert = 0;
      start = 0;
   }

   public boolean equals(Object other){   //Check if other is already in the buffer
      boolean result = false;
      for( E element : elements){
         if(element.equals(other)){
             result = true;
             break;
         }
      }
      return result;

   }

}
