package Facebook;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.meterware.httpunit.*;

import processing.core.*;
import treemap.*;

public class setup extends PApplet {

	Treemap map;
	int i;
	Person[] people;
	People contacts;

	public void setup() {
		size(800, 800);
		// rectMode(CORNERS);
		smooth();
		strokeWeight(0.25f);
		PFont font = createFont("DejaVu Sans Condensed Bold", 13);
		textFont(font);
		people = loadFriends();

		contacts = new People(people);
		//getImages();
		/*
		 * for(Person n : people){ contacts.addPerson(n); }
		 * contacts.finishAdd();
		 */
		//contacts.addAll(people);

		map = new Treemap(contacts, 0, 0, width, height);
		map.setLayout(new SquarifiedLayout());
		//noLoop();

	}

	public void draw() {
		background(255);
		fill(0);
		// contacts.draw();
		map.draw();

	}

	/**
	 * Switch between the various drawing algorithms
	 */
	public void keyPressed() {
		System.out.println("Press");
		people = loadFriends();
		contacts = new People(people);
		map = new Treemap(contacts, 0, 0, width, height);
		if (key == 'r') {
			map.setLayout(new SquarifiedLayout());
			map.updateLayout();
			System.out.println("Squareified");
		} else if (key == 't') {
			map.setLayout(new PivotByMiddle());
			map.updateLayout();
			System.out.println("Middle");
		} else if (key == 'y') {
			map.setLayout(new PivotBySize());
			map.updateLayout();
			System.out.println("Size");
		} else if (key == 'u') {
			map.setLayout(new SliceLayout());
			map.updateLayout();
			System.out.println("Slice");
		} else if (key == 'i') {
			map.setLayout(new PivotBySplitSize());
			map.updateLayout();
			System.out.println("Split Size");
		} else if (key == 'o') {
			map.setLayout(new StripTreemap());
			map.updateLayout();
			System.out.println("Strip");
		} else if (key == 'p') {
			map.setLayout(new BinaryTreeLayout());
			map.updateLayout();
			System.out.println("Binary");
		} else if (key == 'e') {
			map.setLayout(new OrderedTreemap());
			map.updateLayout();
			System.out.println("ordered");
		}
	}

	/**
	 * Get the profile picture for each Person
	 */
	public void getImages() {
		WebConversation wc = new WebConversation();
		ClientProperties cltprops = wc.getClientProperties();
		cltprops.setAutoRedirect(true);
		Pattern locationP = Pattern.compile("LOCATION: [^\\n]+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		for (int i = 0; i < people.length; i++) {
			String url = new String("http://graph.facebook.com/"
					+ people[i].getID() + "/picture");
			String filename = new String("src/data/facebook/pictures/"
					+ people[i].getID() + ".jpg");
			File file = new File(filename);
			if (!(file.exists())) {
				try {
					WebRequest req = new GetMethodWebRequest(url);
					WebResponse res = wc.getResource(req);
					String complete = res.toString();
					// System.out.println(complete);
					Matcher m = locationP.matcher(complete);
					if (m.find()) {
						// System.out.println(m.group(0));
						String newUrl = m.group(0).split(" ")[1];
						// I really REALLY hope they only use 'q' in the image
						// name
						newUrl = newUrl.replace('q', 'n');
						System.out.println(people[i].getName() + " : "
								+ newUrl);
						// Lazy, if it's not a jpg just use the default image.
						// Append proper file extension later
						// Also, if the user has never changed their image, use
						// the default onr
						if (!(newUrl.contains(".jpg"))
								|| (newUrl.contains("static"))) {
							saveStream(filename,
									"src/data/facebook/pictures/default.jpg");
						} else {
							saveStream(filename, newUrl);
						}
						System.out.println("Imaged saved");

					} else {
						throw new Exception("Image not found");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("File exists");
			}

		}
	}

	/**
	 * Read in a text file (graph.facebook.com/me/friends) and parse all of the
	 * people in it into person objects
	 * 
	 * @return
	 */
	public Person[] loadFriends() {
		System.out.println("Loading");
		String[] friends = loadStrings("src/data/facebook/friends.txt");
		Vector<Person> people = new Vector<Person>();
		String name = "";
		String id = "";
		Pattern namePattern = Pattern.compile("\\w+ \\w+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Pattern idPattern = Pattern.compile("\\d+");

		// Assuming a more less completly valid data set for the moment
		for (String n : friends) {
			if (n.contains("name")) {
				Matcher m = namePattern.matcher(n);
				if (m.find()) {
					name = m.group(0);
				}

			} else if (n.contains("id")) {
				Matcher m = idPattern.matcher(n);
				if (m.find()) {
					id = m.group(0);
					people.add(new Person(name, id, this, 4));
				}
			} else {
				// System.out.println();
			}
		}

		Person[] peopleArray = new Person[people.size()];
		for (int i = 0; i < peopleArray.length; i++) {
			peopleArray[i] = people.elementAt(i);
		}

		return peopleArray;
	}

}
