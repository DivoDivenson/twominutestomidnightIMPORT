package task1;
import java.util.Iterator;
import java.util.Vector;


public class Main {

	public static void main(String Args[]){
		XMLReader reader = new XMLReader("src/resources/books.xml");
		Vector<Book> books = reader.readBooks();
		
		XMLWriter writer = new XMLWriter("src/resources/booksTask1.xml", books);
	}
}
