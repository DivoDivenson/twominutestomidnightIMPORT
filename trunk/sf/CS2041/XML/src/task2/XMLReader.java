package task2;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import  java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
/**
 * Read in the books.xml file and generate a bunch of book objects from it
 * @author divo
 *
 */
public class XMLReader {
	File file;
	Document doc;
	/**
	 * The main bit. Returns a Vector of Book. The Books returned are read from the XML file supplied in the constructor.
	 * @return
	 */
	public XMLReader(String fileStr){
		file = new File(fileStr);
		doc = null;
		//read();

	}
	public Vector<Book> readBooks(){
		Vector <Book>books = new Vector<Book>();
		try {
			//Parse it into a DOM “Document”
			Document doc = null;
			org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
			doc = builder.build(file);
			//get the root “element”
			Element rt = doc.getRootElement();

			List nodeLst = rt.getChildren("BOOK");
			Element titleEle, pubEle, priceEle,editEle;
			List authorList;
			Book book;

			//iterate through the list of nodes and print out some Elements
			for (Iterator iter = nodeLst.iterator(); iter.hasNext();){
				Element empElement = (Element)(iter.next());
				book = new Book();

				titleEle = empElement.getChild("TITLE");
				authorList = empElement.getChildren("AUTHOR");
				pubEle = empElement.getChild("PUBLISHER");
				priceEle = empElement.getChild("PRICE");
				editEle = empElement.getChild("EDITOR");
				Attribute yearAttr = empElement.getAttribute("YEAR");
				book.setTitle(titleEle.getText());
				book.setYear(yearAttr.getIntValue());
				book.setPublisher(pubEle.getText());

				if(authorList != null){
					for(Iterator authorIter = authorList.iterator(); authorIter.hasNext();){
						Element tempEle = (Element)(authorIter.next());
						book.addAuthor(tempEle.getChildText("FIRST"), tempEle.getChildText("LAST"));
					}
				}
				book.setPrice(Double.parseDouble(priceEle.getText()));
				if(editEle != null){
					book.setEditor(editEle.getChildText("AFFILIATION"), editEle.getChildText("FIRST"), editEle.getChildText("LAST"));
				}
				books.add(book);

				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		return books;
	}
}
