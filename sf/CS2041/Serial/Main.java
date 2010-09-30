class Main{
   
   public static void main(String args[]){

     SerialFileRandom file = new SerialFileRandom("A01.txt");

     System.out.println("Demo addRecord\nOriginal:\n" + file);
     file.addRecord(433521,"Hender","Mark","Scrub",false);
     System.out.println("Added:\n" + file); //Show record added

     System.out.println("Demo delete record");
     //Any method requiring a fieldname, valid arguments (ones that produce output) are:
     // ID, FN, LN, POS. X is nit implemented. Not sense filtering deleted records.
     //They are deleted :)
     file.deleteRecord(433521);
     System.out.println(file);
     file.deleteRecord("POS","Programmer");
     System.out.println("printRecord will not return a deleted file");
     System.out.println(file.printRecord(999800));
     System.out.println("But the toString method will show a deleted file");
     System.out.println(file);

     System.out.println("Demo updateField");
     System.out.println(file.updateField(276987,"FN","Ross"));
     System.out.println(file.printRecord(276987));

     System.out.println("Demo search record");
     System.out.println(file.searchRecord("LN","Ryan"));
     System.out.println(file);

     System.out.println("Demo compress");
     file.compress();
     System.out.println(file);

     System.out.println("Demo sort");
     file.sort("FN"); //Can put ID, FN, LN, POS in here
     System.out.println(file);
     System.out.println("Demo changeDelim");
     file.changeDelim('~','#');
     System.out.println(file);

   }

   
}
