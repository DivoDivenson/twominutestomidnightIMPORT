import java.util.Scanner;
class Phonebook{
  
  Scanner sc = new Scanner(System.in);
  private Entry entry[] = new Entry[10]; /*Array of entries at the moment
                                           Does the job  */
  private int entryNo =0; 
  public Phonebook(){
  
  }

  public void newEntry(){
    String name,address,number;
    //Basic read in with no error checking (Thats for version 0.2)
    System.out.printf("\nPlease enter full name: ");
    name = sc.nextLine();
    System.out.printf("\nPlease enter address: ");
    address = sc.nextLine();
    System.out.printf("\nPlease enter phone number: ");
    number = sc.nextLine();  //Read in string and convert it to int. nextInt() messess up the line order.

    entry[entryNo] = new Entry(number,address,name);
    entryNo++;
  }


  /* Method to return the index of an Entry object in entry[] with the same
   (phone) number as supplied by argument
   
   Various searching methods. Very simple. If no match is found, -1 is returned
   PROBLEM, if more than one entries share the same infomation only the last one will be "found".*/
  public int findNumber(String number){
    int result=-1; 
    for(int x = 0; x < entryNo; x++){
      if((entry[x].getNumber()).equals(number)){
        result = x;
      }        
    }
    return result;
  }
  
  public int findEntry(String inputAddr){ //Find with address
   int result=-1; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getAddress()).equals(inputAddr)){
        result = x;
      }
    }
    return result;
  }
  
  public int findFor(String inputFor){ //Find with Forname
   int result=-1; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getFor()).equals(inputFor)){
        result = x;
      }
    }
    return result;
  }
  
 public int findSur(String inputSur){ //Find with surname
   int result=-1; 
   for(int x = 0; x < entryNo; x++){
      if((entry[x].getSur()).equals(inputSur)){
        result = x;
      }
    }
    return result;
  }
  

  /* Displays the entry givin it's index number in entry[].
   * If -1 is supplied it returns a not found message (-1 given out
   * by search methods if an entry cannot be found)(Kinda lazy)*/
  public String displayEntry(int entNo){
    if(entNo == -1){
      return "Not found";
    }else{
      return entry[entNo].toString();
    }
  }

  public int bookSize(){
      return entryNo;
  }

}
