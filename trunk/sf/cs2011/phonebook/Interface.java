import java.util.Scanner;

class Interface {
static  Phonebook phonebook = new Phonebook();
  static Scanner sc = new Scanner(System.in);
    public static void main (String Args[]){
            for(int x = 0; x < 1; x++){
        phonebook.newEntry();
      }

      //Create a basic command interface.
      System.out.println("\"exit\" to exit");
      System.out.printf("Phonebook: $ ");
      String input = sc.nextLine();
      while(!(input.equals("exit"))){
        execute();
        System.out.printf("\nPhonebook: $ ");
        input = sc.nextLine();
      }

    
    }

    public static void execute(){
      switch(input)
        case (input.equals
    }

}
