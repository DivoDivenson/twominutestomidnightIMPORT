import java.util.Scanner;

public class Entry{
  int number;
  String address,name,forName,surName;

  public Entry(int number, String address,String name){
    this.number = number;
    this.address = address;
    this.name = name;
  }


  private void splitName(){
    String delims = "[ ]";
    String[] tokens = name.split(delims);
    this.forName = tokens[0];
    this.surName = tokens[1]; //I hope the don't have a middle name(Fix this later)
  }


  //Get/Set methods.
  public int getNumber(){
    return number;
  }

  public String getAddress(){
    return address;
  }

  public String getFor(){
    return forName;
  }

  public String getSur(){
    return surName;
  }

  //</Getters><Setters>(May be redundant,remove later?)

  public int setNumber(int input){
    number = input;
  }

  public String setAddress(String input){
    address = input;
  }

  public String setName(String input){
    name = input;
    splitName();
  }
    
  //</Setters>
}
