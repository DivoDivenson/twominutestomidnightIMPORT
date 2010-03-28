package task2;
import java.util.Iterator;
import java.util.Vector;



public class Book {



	String title, publisher;
	double price;
	double priceEuro;
	Vector <Name>authors = new Vector<Name>();
	Editor editor;


	int year;
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		//this.priceEuro = price * 0.642178269;
	}

	public void addAuthor(String firstname, String lastname){
		Name tempName = new Name(firstname, lastname);
		authors.add(tempName);
	}

	public void convertToUpper(){
		Iterator it = authors.iterator();
		while(it.hasNext()){
			((Name) it.next()).toUpper();
		}
	}

	public Vector<Name> getAuthors(){
		return this.authors;
	}



	public Editor getEditor() {
		return editor;
	}

	public void setEditor(String affiliation, String firstname, String lastname) {
		Name tempName = new Name(firstname, lastname);
		this.editor = new Editor(affiliation, tempName);
	}


	public class Name{
		String first, last;

		public String getFirst() {
			return first;
		}

		public void setFirst(String first) {
			this.first = first;
		}

		public String getLast() {
			return last;
		}

		public void setLast(String last) {
			this.last = last;
		}

		public String toString(){
			return new String(String.format("%s %s", first,last));
		}

		public void toUpper(){
			this.first = this.first.toUpperCase();
			this.last = this.last.toUpperCase();

		}

		public Name(String first, String last){
			this.first = first;
			this.last = last;
		}
	}

	public class Editor{
		String affiliation;
		public String getAffiliation() {
			return affiliation;
		}

		public void setAffiliation(String affiliation) {
			this.affiliation = affiliation;
		}

		public Name getName() {
			return name;
		}

		public void setName(Name name) {
			this.name = name;
		}

		Name name;

		public Editor(String affiliation, Name name){
			this.affiliation = affiliation;
			this.name = name;
		}
	}
}
