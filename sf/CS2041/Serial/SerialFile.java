import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
/*Would love to read in the whole file and just preform ops
  on a buffer, with ins and strings all that goodness,
  but it looks like everything has to read/written from/to the file*/

/* Important note, use "\\|" when tokenizing with | */
class SerialFile{

   private BufferedReader in;
   private FileReader reader;
   private FileWriter writer;
   private PrintWriter out;
   private String filename;

   public SerialFile(String filename){
      this.filename = filename;
      try{
         reader = new FileReader(filename);
         writer = new FileWriter(filename,true);
      }catch(FileNotFoundException exception){
         handleException(exception);
      }catch(IOException exception){
         handleException(exception);
      }
      in = new BufferedReader(reader);
      out = new PrintWriter(writer);
   }


   /* Return the record ID for given field and value
      Return 0 if not found 
    Reset spcifies if the file pointer is to reset after a record is found
    ie, if you want to search for multiple records through multiple calls set it to flase*/
   int searchRecord(String fieldname, String value, boolean reset){
    int resultID = 0;
    String temp,tokens[];
    fieldname = fieldname.concat("=");
    try{
      while((temp = in.readLine()) != null){
         tokens = temp.split(fieldname);
         tokens = tokens[1].split("\\|");
         if(value.equals(tokens[0])){  //equals works by value for strings, not ref
            tokens = temp.split("ID=");
            tokens = tokens[1].split("\\|");
            resultID = Integer.parseInt(tokens[0]);
            break;
         }
      }
    }catch(IOException ioexception){
      handleException(ioexception);

      /* A note on the following catch
         If fieldname is not a valid field or is not contaied in the file
         then tokens[0] will contain the entire line, and token[1] will will be null
         Hence, the Out of Bounds exception */
    }catch(ArrayIndexOutOfBoundsException bounds){
         System.err.println("File contains bad values or field does not exist!");
         handleException(bounds);
    }

    if(reset){
      resetFile();
    }
    return resultID;
      
   }
   
   /* Presentation method. Searches for a record and resets the file position */
   int searchRecord(String fieldname, String value){
      return(searchRecord(fieldname,value, true));
   }

   boolean deleteRecords(String fieldname,String value){
      int ID;
      while((ID = searchRecord(fieldname,value,false)) != 0){
         deleteRecord(ID);
      }
      resetFile();

      return true;
   }

   boolean addRecord(int ID, String lastname, String firstname, String position,
         boolean x){
      out.print("ID=" + ID + "|" + "LN=" + lastname + "|" + "FN=" + firstname + "|");
      out.print("POS=" + position + "|" + "X=");
      if(x){
         out.println(1);
      }else{
         out.println(0);
      }
      out.flush();

      return true;
   }

   boolean deleteRecord(int ID){
      //Create a new file to write into
      try{
         out.close();
         writer.close();
         writer = new FileWriter("temp");
         out = new PrintWriter(writer);
      
         String temp,tokens[];
         int fileID;
      
         while((temp = in.readLine()) != null){
            tokens = temp.split("ID=");
            tokens = tokens[1].split("\\|");
            fileID = Integer.parseInt(tokens[0]);
            if(fileID == ID){ //Copy record char by char until delete flag is seen. 
                              //Sorry, must be my C habbits coming through
               for(int x = 0; x < temp.length(); x++){
                  if(temp.charAt(x) == 'X'){
                     out.print(temp.charAt(x));
                     if(temp.charAt(x+1) == '='){
                        out.print(temp.charAt(x+1));
                        out.print(1);
                        x = x+2;
                     }
                  }else{
                     out.print(temp.charAt(x));
                  }
               }
               out.print('\n');
            }else{
               out.println(temp);
               //Do I need to manually flush?
            }
         }
         out.flush();
         resetFile();
         File tempFile = new File("temp");
         tempFile.renameTo(new File(this.filename));

      }catch(IOException ioexc){
         handleException(ioexc);
      }
      return true;

   }


   //Return the record specified by ID
   //Note: Returning a string of the record. I like to keep my
   //printing to screen in Main.
   public String printRecord(int ID){
      String result = new String("Not found");
      String temp;
      String tokens[];
      int fileID;

      if(findRecord(ID)){
         try{
            result = in.readLine();
         }catch(IOException ioex){
            handleException(ioex);
         }
         resetFile();
      }
       return result;
   }
   
   //Places a mark at record ID
   //Does this by placing a mark at every record, the last one 
   //being the record searched for. As a result, the file must be
   //re-intilized after use
   //Returns true if record found
   public boolean findRecord(int ID){
      String temp,tokens[];
      int fileID;
      try{
         in.mark(0);
          while((temp = in.readLine()) != null){
            tokens = temp.split("ID=");
            tokens = tokens[1].split("\\|");
            fileID = Integer.parseInt(tokens[0]);
            if(fileID == ID){
              in.reset();  //Input stream now set to record ID
              return true;
            }
            in.mark(0);  //Mark the next entry. Coudl be else to above if
         }
      }catch(IOException ioexception){
         handleException(ioexception);
      }
      resetFile();
      return false;
   }

/*Reset the file. The easiest way i can think of is to simply close it and reopen
   it. This is ugly and probably very expensive, but it (and findRecord) make
   the code so much cleaner */
   private void resetFile(){
      try{
         reader.close();
         in.close();
         writer.close();
         out.close();

         reader = new FileReader(filename);
         writer = new FileWriter(filename,true);
      }catch(FileNotFoundException exception){
         System.err.println("File not found");
         System.exit(1);
      }catch(IOException ioex){
         handleException(ioex);
      }
      in = new BufferedReader(reader);
      out = new PrintWriter(writer);

   }
   
   //Because im sick of having to re-write this code  
   //Perhaps a better name would be ignoreException and crash
   private void handleException(Throwable exception){
      System.err.println(exception);
      System.exit(1);
   }

}
