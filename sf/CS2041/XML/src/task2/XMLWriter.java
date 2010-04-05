package task2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import task2.Book.Editor;
import task2.Book.Name;

//import Book;

/**
 * Given a filename and a Vector of books this class write the Vector of books out to the file as XML
 * It does this when created. Create then dump it.
 */
public class XMLWriter {
	String file;
	Vector<Book> books;

	public XMLWriter(String filename, Vector<Book> books) {
		this.file = filename;
		this.books = books;
		Iterator bookIt = books.iterator();
		Element bib = new Element("BIB");
		while(bookIt.hasNext()){
			Book book = (Book) bookIt.next();

			Element aBook = new Element("BOOK");
			//Add the year as an element
			Element year = new Element("YEAR");
			year.addContent( new String(String.format("%d",book.getYear())));
			aBook.addContent(year);
			//Add the title to the book
			Element title = new Element("TITLE");
			title.addContent(book.getTitle());        
			aBook.addContent(title);
			//Add the author(s)
			Vector<Name> tempNames = book.getAuthors();
			Iterator<Name> it = tempNames.iterator();
			while(it.hasNext()){
				Element author = new Element("AUTHOR");
				Name tempName = it.next();
				Element first = new Element("FIRST");
				first.addContent(tempName.getFirst());
				Element last = new Element("LAST");
				last.addContent(tempName.getLast());
				author.addContent(first);
				author.addContent(last);
				aBook.addContent(author);

			}

			//Add the publisher
			Element publisher = new Element("PUBLISHER");
			publisher.addContent(book.getPublisher());
			aBook.addContent(publisher);
			//Add the price
			Element price = new Element("PRICE");
			price.addContent(new String(String.format("%.2f",book.getPrice())));
			aBook.addContent(price);
			//Add the editor if they exist
			if(book.getEditor()!=null){
				//Create the editor
				Editor tempEditor = book.getEditor();
				Element editor = new Element("EDITOR");
				//Create the editors name
				Name editName = tempEditor.getName();
				Element first = new Element("FIRST");
				first.addContent(editName.getFirst());
				Element last = new Element("LAST");
				last.addContent(editName.getLast());
				//Add the name
				editor.addContent(first);
				editor.addContent(last);
				//Create and add the affiliation
				Element affiliation = new Element("AFFILIATION");
				affiliation.addContent(tempEditor.getAffiliation());
				editor.addContent(affiliation);
				//Add the editor to the book
				aBook.addContent(editor);

			}
			bib.addContent(aBook);
		}




		try{

			XMLOutputter serializer = new XMLOutputter();
			serializer.setFormat(Format.getPrettyFormat().setIndent("\t"));
			FileWriter Xmlfile = new FileWriter(file, false);
			Document myDocument = new Document(bib);
			//serializer.output(myDocument, System.out);
			serializer.output(myDocument, Xmlfile);
			Xmlfile.flush();
			Xmlfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
