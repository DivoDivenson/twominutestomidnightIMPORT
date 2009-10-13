import java.util.Scanner;

class Interface {
static  Phonebook phonebook = new Phonebook();
  static Scanner sc = new Scanner(System.in);
    public static void main (String Args[]){
            for(int x = 0; x < 2; x++){
        phonebook.newEntry();
      }

      //Create a basic command interface.
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

    public static void execute(String command,String argument){
      if(command.equals("findSur")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findSur(argument)));
      }else if (command.equals("findFor")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findFor(argument)));
      }else if (command.equals("findNum")){
            int argumentInt = Integer.parseInt(argument);
            System.out.printf("%s", phonebook.displayEntry(phonebook.findEntry(argumentInt)));
      }else if (command.equals("findAdd")){
            System.out.printf("%s", phonebook.displayEntry(phonebook.findEntry(argument)));
      }



    }

}

