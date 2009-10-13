import java.util.Scanner;
class Phonebook{
  
  Scanner sc = new Scanner(System.in);
  private Entry entry[] = new Entry[10]; /*Array of entries at the moment
                     Change to arraylist sometime */
  int entryNo =0; 
  public Phonebook(){
  
  }

  public void newEntry(){
    String name,address;
    int number;
    //Basic read in with no error checking (Thats for version 0.2)
    System.out.printf("\nPlease enter full name: ");
    name = sc.nextLine();
    System.out.printf("\nPlease enter address: ");
    address = sc.nextLine();
    System.out.printf("\nPlease enter phone number: ");
    number = Integer.parseInt(sc.nextLine());  //Read in string and convert it to int. nextInt() messess up the line order.

    entry[entryNo] = new Entry(number,address,name);
    entryNo++;
  }


  /* Method to return the index of an Entry object in entry[] with the same
   (phone) number as supplied by argument */
  public int findEntry(int number){
    int result=0; //If the desierd entry cannot be found, 1st entry is returned.
    for(int x = 0; x < entryNo; x++){
      if(entry[x].getNumber() == number){
        result = x;
      }
    }
    return result;
  }
  
  public int findEntry(String inputAddr){ //Find with address
   int result=0; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getAddress()).equals(inputAddr)){
        result = x;
      }
    }
    return result;
  }
  
  public int findFor(String inputFor){ //Find with Forname
   int result=0; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getFor()).equals(inputFor)){
        result = x;
      }
    }
    return result;
  }
  
 public int findSur(String inputSur){ //Find with surname
   int result=0; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getSur()).equals(inputSur)){
        result = x;
      }
    }
    return result;
  }
  


  public String displayEntry(int entNo){
     return entry[entNo].toString();
  }

}
