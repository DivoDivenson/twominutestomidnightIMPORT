import java.util.Comparator;

public class RecordSortByPOS implements Comparator<Record>{


   public int compare(Record r1, Record r2){
      return r1.getPos().compareToIgnoreCase(r2.getPos());
   }
}
