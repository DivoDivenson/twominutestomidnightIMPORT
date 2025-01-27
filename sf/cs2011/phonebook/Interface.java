import java.util.Scanner;
import tcdIO.Terminal;
class Interface {
static  Phonebook phonebook = new Phonebook();
static  Terminal terminal = new Terminal("Phonebook");
  
  static Scanner sc = new Scanner(System.in);
    public static void main (String Args[]){
            for(int x = 0; x < 2; x++){
        phonebook.newEntry();
      }
      //Create a basic command interface.
      System.out.printf("Phonebook v0.1\nPlease enter first record.\n\n");
      System.out.println("\"exit\" to exit"); //Write a quick comamnd summary
      System.out.printf("Phonebook: $ ");
      String input = sc.nextLine();
      String argument;
      while(!(input.equals("exit"))){
        String[] tokens = input.split(" "); //Split input into comamnd and argument. Creats a new string each time...
        input = tokens[0];
        if(tokens.length > 1){
          argument = tokens[1];
        }else{
          argument = null;
        }
        execute(input, argument);

        System.out.printf("\nPhonebook: $ ");
        input = sc.nextLine();
      }

    
    }
    /* Basic terminal interface. Reads in command and calls the right Phonebook method.
       If argument is null, checks for a command requiring it are skipped.
        Again, assuming valid input for everything. */
    public static void execute(String command,String argument){
      if(argument != null){
        if(command.equals("findSur")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findSur(argument)));
        }else if (command.equals("findFor")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findFor(argument)));
        }else if (command.equals("findNum")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findNumber(argument)));
        }else{
            System.out.printf("Comamnd not found or incorrect. Try using \"help\".\n");
        } 
      }else{
         if (command.equals("newEntry")){
            phonebook.newEntry();
        }else if (command.equals("printAll")){
            for(int x = 0; x < phonebook.bookSize(); x++){
                System.out.printf("%s\n", phonebook.displayEntry(x));
              }
        }else if (command.equals("findAdd")){
            System.out.printf("Please enter the address");
            String address = sc.nextLine();
            System.out.printf("%s", phonebook.displayEntry(phonebook.findEntry(address)));
        }else if (command.equals("help")){
          helpMe();
        }else{
            System.out.printf("Comamnd not found or incorrect. Try using \"help\".\n");
         }
       }
    }

    public static void helpMe(){ //Print one big help string.
      System.out.printf("Input commands in command <argumet> format\nfindSur: Find surname.\nfindFor: Find forname.\nfindAdd: Find address.\nnewEntry: Add a new entry.\nprintAll: Print the contents of the phonebook.\n");
    }
       terminal.printf("Input commands in command <argumet> format\nfindSur: Find surname.\nfindFor: Find forname.\nfindAdd: Find address.\nnewEntry: Add a new entry.\nprintAll: Print the contents of the phonebook.\n");
    }

}

