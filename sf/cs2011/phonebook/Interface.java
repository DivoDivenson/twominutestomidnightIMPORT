class Interface {
    public static void main (String Args[]){
      Phonebook phonebook = new Phonebook();
      phonebook.newEntry();
      System.out.println(phonebook.toString(0));
    }
}
