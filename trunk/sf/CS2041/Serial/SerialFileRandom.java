import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Quick note. I was going to use RandomAccessFile but has trouble writing 
   out. Improper encoding was used on some platforms and as a result lots of
   garbage was written out. I could have used writeUTF and done some hackery
   to get things started(it writes out the number of bytes to be written
   out at the start of the write out(phew)), so I think I would have needed two
   read methods, one for original file and one for the new file with lengths at the
   start of each record, but instead I decided to use FileReader and FileWritter for
   simplicty. Even if the write are far more expensive */


class SerialFileRandom{

   private final int RECORD_SIZE = 100;
// private RandomAccessFile in;
   private File filepointer;
   private String filename;
   private ArrayList<Record> records;
   private char fieldDel,valueDel;
   private FileWriter writer;
   private PrintWriter out;

   private FileReader reader;
   private BufferedReader in;
   public SerialFileRandom(String filename,char fieldDelim, char valueDelim){
      this.filename = filename;
      this.fieldDel = fieldDelim;
      this.valueDel = valueDelim;
      records = new ArrayList<Record>();

      try{
        filepointer = new File(filename);
        reader = new FileReader(filepointer);
        in = new BufferedReader(reader);
        readIn();
        in.close();
        reader.close();
      /* Once the values have been read in initially there 
      is no need to read them again. All operations are preformed on an internal
      representation which is then(imedialty) pushed out to the file */

      }catch(FileNotFoundException notFound){
         handleException(notFound);
      }catch(IOException ioex){
         handleException(ioex);
      }

      
   }

   public SerialFileRandom(String filename){
      this(filename,'|','=');
   }


   private void readIn(){
      String input = new String();
      Record temprecord;
      boolean bool;
      try{
         while((input = in.readLine()) != null){
            if((parse("X",input)).equals("0")){
               bool = false;
            }else{
               bool = true;
            }
            temprecord = new Record(Integer.parseInt(parse("ID",input)),
                  parse("LN",input), parse("FN",input),
                  parse("POS",input), bool);
            records.add(temprecord);
         }
      }catch(IOException ioex){
         handleException(ioex);
      }
      //System.out.print(printRecord(431223));
      //writeOut();
      //System.out.println(addRecord(431543,"Hender","Mark","Scrub",false));
      System.out.println(searchRecord("LN","Hender"));
   }

   String printRecord(int ID){
      Record result = findRecord(ID);
      if(result != null){
         return result.toString();
      }else{
         return "Not Found";
      }

   }

   public boolean addRecord(int ID, String lastname, String firstname, String position,
         boolean x){
      boolean result = false;
      if(findRecord(ID) == null){
         records.add(new Record(ID, lastname, firstname, position, x));
         result = true;
         writeOut();
      }else{
         ;
      }

      return result;
   }

   private void writeOut(){
      try{
         writer = new FileWriter(filename);
         out = new PrintWriter(writer);
         for(Record record : records){
            out.print(record);
         }
         out.flush();
         writer.close();
         out.close();
      }catch(IOException ioex){
         handleException(ioex);
      }

   }

   /* Not implementing search by delete. It's not so much data on the person as it is
    * on the entry and should not be presented to the user*/
   int searchRecord(String fieldname, String value){
     int ID;
     Record tempRecord;
      switch(getField(fieldname)){
        case 1: tempRecord = findRecord(Integer.parseInt(value)); break;
        case 2: tempRecord = findRecordLN(value); break;
        case 3: tempRecord = findRecordFN(value); break;
        case 4: tempRecord = findRecordPOS(value); break;
        default: tempRecord = null; break;
      }
      if(tempRecord == null){
        ID = 0;
      }else{
        ID = tempRecord.getID();
      }
      return ID;
   }

   private int getField(String fieldname){
    if(fieldname.compareTo("ID") == 0){
      return 1;
    }else if(fieldname.compareTo("LN") == 0){
      return 2;
    }else if(fieldname.compareTo("FN") == 0){
      return 3;
    }else if(fieldname.compareTo("POS") == 0){
      return 4;
    }
    return 5;

   }
   //Write all records to in
   Record findRecord(int ID){
      for(Record record : records){
         if(record.getID() == ID){
            return record;
         }
      }
      return null;
   }

   Record findRecordLN(String ln){
      for(Record record : records){
        if((record.getLN()).compareTo(ln) == 0){
          return record;
        }
      }
      return null;
   }

   Record findRecordFN(String fn){
      for(Record record : records){
        if((record.getFN()).compareTo(fn) == 0){
          return record;
        }
      }
      return null;
   }

   Record findRecordPOS(String pos){
      for(Record record : records){
        if((record.getPOS()).compareTo(pos) == 0){
          return record;
        }
      }
      return null;
   }

   private String parse(String fieldname, String input){
      Pattern pattern = Pattern.compile(fieldname+this.valueDel+"[^"+this.fieldDel+"]+");
      Matcher matcher = pattern.matcher(input);
      matcher.find();
      try{
         matcher.group();      
      }catch(IllegalStateException invalidInput){
         System.err.println("Warning: File contains invalid field\n");
         handleException(invalidInput);
      }
      return (matcher.group()).split(fieldname + this.valueDel)[1]; 
      /* Chop off the ID= or whatever and return the value */
   }


   /*Because im sick of having to re-write this code so much
     Simply prints the exception and exit(1) */
   private void handleException(Throwable exception){
      exception.printStackTrace(System.err);
      System.err.println(exception);
      System.exit(1);
   }

}
