import java.util.Comparator;

public class RecordSortByFN implements Comparator<Record>{


   public int compare(Record r1, Record r2){
      return r1.getFN().compareToIgnoreCase(r2.getFN());
   }
}
