package task2;
import java.util.Iterator;
import java.util.Vector;


public class Main {

	public static void main(String Args[]){
		XMLReader reader = new XMLReader("src/resources/booksTask1.xml");
		Vector<Book> books = reader.readBooks();
		
		XMLWriter writer = new XMLWriter("src/resources/booksTask2.xml", books);
	}
}
