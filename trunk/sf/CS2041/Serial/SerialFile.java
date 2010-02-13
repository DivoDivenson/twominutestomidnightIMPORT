import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/* Important note, use "\\|" when tokenizing with | */
class SerialFile{

   BufferedReader in;
   FileReader reader;
   FileWriter writer;
   StringTokenizer tokeniz;

   public SerialFile(String filename){
      try{
         reader = new FileReader(filename);
      }catch(FileNotFoundException exception){
         System.err.println("File not found");
         System.exit(1);
      }
      in = new BufferedReader(reader);
   }


   //Return the record ID
   /*int searchRecord(String fieldname, String value){
      
      
   }*/


   //Return the record specified by ID
   //Note: Returning a string of the record. I like to keep my
   //printing to screen in Main.
   public String printRecord(int ID){
      String result = new String();
      String temp = new String();

      try{
         temp = in.readLine();
      }catch(IOException ioexception){
         System.err.println(ioexception);
         System.exit(1);
      }
      String t[];
      t = temp.split("\\|");
      System.out.println(t[0]);

      return result;
   }
}
