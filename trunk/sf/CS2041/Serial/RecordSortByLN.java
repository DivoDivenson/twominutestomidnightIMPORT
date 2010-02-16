import java.util.Comparator;
public class RecordSortByLN implements Comparator<Record>{


   public int compare(Record r1, Record r2){
      return r1.getLN().compareToIgnoreCase(r2.getLN());
   }
}
