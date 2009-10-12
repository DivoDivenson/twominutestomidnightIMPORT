import java.util.Scanner;
class Phonebook{
  
  Scanner sc = new Scanner(System.in);
  Entry entry[] = new Entry[5]; /*Array of entries at the moment
                     Change to arraylist sometime */
  int entryNo =0; 
  public Phonebook(){
  
  }

  public void newEntry(){
    String name,address;
    int number;
    //Basic read in with no error checking (Thats for version 0.2)
    System.out.printf("Please enter full name: ");
    name = sc.next();
    System.out.printf("\nPlease enter address: ");
    address = sc.next();
    System.out.printf("\nPlease enter phone number: ");
    number = sc.nextInt();
    entry[entryNo] = new Entry(number,address,name);
    entryNo++;
  }


  /* Method to return the index of an Entry object in entry[] with the same
   (phone) number as supplied by argument */
  public int findEntry(int number){
    int result; //If the desierd entry cannot be found, 1st entry is returned.
    for(int x = 0; x < entryNo; x++){
      if(entry[entryNo].getNumber() = number){
        result = entryNo;
      }
    }
  }



}
