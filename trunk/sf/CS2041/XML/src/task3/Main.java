package task3;
import java.util.Iterator;
import java.util.Vector;


public class Main {

	public static void main(String Args[]){
		XMLReader reader = new XMLReader("src/resources/booksTask2.xml");
		Vector<Book> books = reader.readBooks();
		
		XMLWriter writer = new XMLWriter("src/resources/booksTask3.xml", books);
	}
}
