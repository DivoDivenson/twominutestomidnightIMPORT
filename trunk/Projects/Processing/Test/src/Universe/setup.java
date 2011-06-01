package Universe;

import java.io.File;
import java.util.Vector;

import treemap.*;
import processing.core.*;
import processing.xml.XMLElement;

public class setup extends PApplet {
	XMLElement xml;
	XmlParser parser;
	Vector<Period> periods;

	public void setup() {
		parser = new XmlParser();
		periods = new Vector<Period>();
		
		File file = new File("data/Universe");
		File files[] = file.listFiles();
		for(int i = 0; i < files.length; i++){
			//System.out.println(files[i].getAbsolutePath());
			addPeriods(parser.parseHis(files[i]));
		}
		//Period[] periodsArr = periods.toArray();
		size(200, 200);
		
	}

	

	public void draw() {
		
	}
	
	public void addPeriods(Period[] array){ //Could make generic, no point
		for(int i = 0; i < array.length; i++){
			//System.out.println(array[i]);
			periods.addElement(array[i]);
		}
		
	}
}
