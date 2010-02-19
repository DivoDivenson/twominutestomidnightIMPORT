//import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
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
import java.util.Collections;

/* Quick note. I was going to use RandomAccessFile but has trouble writing 
   out. Improper encoding was used on some platforms and as a result lots of
   garbage was written out. I could have used writeUTF and done some hackery
   to get things started(it writes out the number of bytes to be written
   out at the start of the write out(phew)), so I think I would have needed two
   read methods, one for original file and one for the new file with lengths at the
   start of each record, but instead I decided to use FileReader and FileWritter for
   simplicty. Even if the write are far more expensive */


class SerialFileRandom{

//   private final int RECORD_SIZE = 100;
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


   //Read in the file and create a series of records based on it
   //Nothing too exciting
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
            //writeOut();
      //System.out.println(searchRecord("LN","Hender"));
   }

   //Print out the record given by ID. Argument must be a valid integer
   public String printRecord(int ID){
      Record result = findRecord(ID);
      if(result != null && !(result.getX())){
         return result.toString();
      }else{
         return "Not Found";
      }

   }

   //Set the delete marker in record ID. ID must be a valid int
   public boolean deleteRecord(int ID){
      Record tempRecord;
      if((tempRecord = findRecord(ID)) != null){
         tempRecord.deleteRecord();
         writeOut();
         return true;
      }
      return false;
   }

   //As above. Arguments must be valid strings although any values can safely be supplied
   public boolean deleteRecord(String fieldname, String value){
      int ID;
      if((ID = searchRecord(fieldname,value)) != 0){
         deleteRecord(ID);
         return true;
      }
      return false;
   }

   //Modify the record ID. Input must be of valid types although the record need not be valid
   public boolean updateField(int ID, String field, String value){
      Record tempRecord;
      boolean result = true;
      if((tempRecord = findRecord(ID)) == null && !(tempRecord.getX())){ //Initial check for record
         return false;
      }

      switch(getField(field)){
         case 1: tempRecord.setID(Integer.parseInt(value)); break;
         case 2: tempRecord.setLN(value); break;
         case 3: tempRecord.setFN(value); break;
         case 4: tempRecord.setPOS(value); break;
         case 5: tempRecord.setX(value); break;
         default: result = false; break;
      }
      //if(result) etc
      writeOut();
      return result;


   }

   //Add new record to file. If record exists (Identified by ID) then return false.
   //Again, input must be of valid types
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

   //Open a PrintWriter and write out the entire intenal representation of the file,
   // erasing the previous contents. This aproach is explained above
   //Probably no need to close the file each time, needless expense and such, but
   //just to keep things clean
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
   public int searchRecord(String fieldname, String value){
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

   //Convert fieldname into a value betweem 1 and 5
   //This makes it easier for methods that take fieldname as an argument
   //as the relevant action can be decided by a switch. Makes the code neater
   private int getField(String fieldname){
    if(fieldname.compareTo("ID") == 0){
      return 1;
    }else if(fieldname.compareTo("LN") == 0){
      return 2;
    }else if(fieldname.compareTo("FN") == 0){
      return 3;
    }else if(fieldname.compareTo("POS") == 0){
      return 4;
    }else if(fieldname.compareTo("X") ==0){
      return 5;
    }
    return 0;

   }


   /* The next few methods all find a _single_ record 
      according to a field value. Which field 
      is searched should be obvious from the mathod names */


   //Return record specified by ID
   private Record findRecord(int ID){
      for(Record record : records){
         if(record.getID() == ID){
            return record;
         }
      }
      return null;
   }

   private Record findRecordLN(String ln){
      for(Record record : records){
        if((record.getLN()).compareToIgnoreCase(ln) == 0){
          return record;
        }
      }
      return null;
   }

   private Record findRecordFN(String fn){
      for(Record record : records){
        if((record.getFN()).compareToIgnoreCase(fn) == 0){
          return record;
        }
      }
      return null;
   }

   private Record findRecordPOS(String pos){
      for(Record record : records){
        if((record.getPos()).compareToIgnoreCase(pos) == 0){
          return record;
        }
      }
      return null;
   }

   /* End find methods. No find by deletion marker.
      Its not part of record data, its part of record managemt */


   //Extract the "fieldname" value of input string
   //ie,("FN",input) extracts the persons first name
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


   public void compress(){
      for(int x = 0; x < records.size(); x++){
         if((records.get(x)).getX()){
            records.remove(x);
         }
      }
      records.trimToSize();
      writeOut();
   }

   //Use comparator objects to sort the array by fieldname
   //Switch statement used as per all methods that operate on fieldname
   public void sort(String fieldname){
      boolean write = true;
      switch(getField(fieldname)){
         case 1: Collections.sort(records); break;
         case 2: Collections.sort(records, new RecordSortByLN()); break;
         case 3: Collections.sort(records, new RecordSortByFN()); break;
         case 4: Collections.sort(records, new RecordSortByPOS()); break;
         default: write = false; break;
      }
      if(write){
         writeOut();
      }
   }

   //Dumps out the entire set of records, including deleted ones
   //Mainly for debuging
   public String toString(){
      return records.toString();
   }

   //Not changing record delim as readLine() will no longer work
   //the next time the program runs. I could try to override it but im
   //not bothered
   public void changeDelim(char newValueDelim, char newFieldDelim){
      (records.get(0)).setDelim(newValueDelim,newFieldDelim);
      writeOut();
   }

   /*Because im sick of having to re-write this code so much
     Simply prints the exception and exit(1) */
   private void handleException(Throwable exception){
      exception.printStackTrace(System.err);
      System.err.println(exception);
      System.exit(1);
   }

}
