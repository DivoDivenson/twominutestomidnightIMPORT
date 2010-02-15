

class Record{

   private int ID,size;
   private String lastname,firstname,position;
   private boolean x;
   private char valueDel,fieldDel;

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
