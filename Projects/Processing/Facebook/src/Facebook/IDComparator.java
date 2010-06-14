package Facebook;

import java.util.Comparator;

public class IDComparator implements Comparator {

	public int compare(Object one, Object two){
		return  (int) ( ((Person) one).getID() - ((Person) two).getID());
	}

	
	
}
