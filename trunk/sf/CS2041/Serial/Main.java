class Main{
   
   public static void main(String args[]){

     SerialFile file = new SerialFile("A01.txt"); 
     System.out.println(file.printRecord(8832321));
     System.out.println(file.printRecord(123448));
     System.out.println(file.searchRecord("LN","Duggan"));
     file.deleteRecord(431223);
     file.addRecord(123444,"Hender","Mark","Scrub",false);
   }

   
}
