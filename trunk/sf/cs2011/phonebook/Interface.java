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
      String argument;
      while(!(input.equals("exit"))){
        String[] tokens = input.split(" "); //Split input into comamnd and argument. Creats a new string each time... I'm sorry RAM.
        input = tokens[0];
        argument = tokens[1];
        execute(input, argument);
        System.out.printf("\nPhonebook: $ ");
        input = sc.nextLine();
      }

    
    }

    public static void execute(String command,String argument){
      if(command.equals("findSur")){
            System.out.printf("Hello\n");
          }
    }

}

