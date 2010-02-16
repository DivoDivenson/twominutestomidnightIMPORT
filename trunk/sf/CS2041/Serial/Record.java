

class Record implements Comparable<Record>{

   private int ID;
   private String lastname,firstname,position;
   private boolean x;
   private static char valueDel,fieldDel;

   public Record(int ID, String lastname, String firstname, String position, 
         boolean x){
      
      this.ID = ID;
      this.lastname = lastname;
      this.firstname = firstname;
      this.position = position;
      this.x = x;

      this.valueDel = '=';
      this.fieldDel = '|';
   }


   public int getID(){
      return this.ID;
   }

   public String getFN(){
      return this.firstname;
   }

   public String getLN(){
      return this.lastname;
   }

   public String getPos(){
      return this.position;
   }

   public boolean getX(){
      return this.x;
   }

   public void deleteRecord(){
      this.x = true;
   }

   public void setID(int ID){
      this.ID = ID;
   }

   public void setFN(String name){
      this.firstname = name;
   }

   public void setLN(String name){
      this.lastname = name;
   }

   public void setPOS(String position){
      this.position = position;
   }

   //Read in a string, just to keep things the same   
   public void setX(String xin){
      if(xin.compareTo("0") == 0){
         this.x = false;
      }else if(xin.compareTo("1") == 0){
         this.x = true;
      }//else do nothing


   }

   public void setDelim(char value, char field){
      this.valueDel = value;
      this.fieldDel = field;
   }

   public boolean equals(Record other){
      if(other.getID() == this.ID){
         return true;
      }else{
         return false;
      }
   }

   public int compareTo(Record other){
      return this.ID - other.getID();
   }
   public String toString(){
      int xint;
      if(this.x){
         xint = 1;
      }else{
         xint = 0;
      }
      return String.format("ID" + valueDel + ID + fieldDel + "FN" + valueDel + 
            firstname + fieldDel + "LN" + valueDel + lastname + fieldDel
            + "POS" + valueDel + position + fieldDel + "X" + valueDel + xint + '\n');
   }

}
