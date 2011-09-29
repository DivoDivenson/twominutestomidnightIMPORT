package Universe;
/**
 * This code is not readable.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import treemap.*;
import processing.core.*;
import processing.xml.XMLElement;

public class setup extends PApplet {
	XMLElement xml;
	XmlParser parser;
	Vector<Period> periods;
	int HEIGHT = 800;
	int WIDTH = 800;
	int CENTER = 200;
	int[] check;
	Event[] eventArray;
	int colourFrom = -200000;
	Event[][] eventPos; //Array of events orderd by position

	public void setup() {
		parser = new XmlParser();
		periods = new Vector<Period>();
		

		File file = new File("data/Universe");
		File files[] = file.listFiles();
		for(int i = 0; i < files.length; i++){
			//System.out.println(files[i].getName());
			if(files[i].getName().contains(".xml")){
				addPeriods(parser.parseHis(files[i]));
			}
		}
		//Object[] periodsArr = periods.toArray();

		Vector<Event> events = new Vector<Event>();
		for(Iterator<Period> i = periods.iterator(); i.hasNext();){
			Event[] temp = i.next().getEvents();
			for(int j = 0; j < temp.length; j++){
				events.add(temp[j]);
				//System.out.println(temp[j]);
			}
		}

		java.util.Collections.sort(events);
		eventArray = events.toArray(new Event[0]);
		eventPos = new Event[WIDTH][];
		//writeOut(eventArray);


		size(WIDTH, HEIGHT);
		background(255);
		check = new int[WIDTH];
		int last = 0;
		for(int i = 0; i < eventArray.length; i++){
			double hack = eventArray[i].getTime();
			/*double hack = eventArray[i].getTime() - 2010;
			if(hack < 0){
				hack *= -1;
			}*/
			//float m = (float) map(hack ,0.0,  13700002010.0,  20.0, (WIDTH - 20.0));
			if(hack > colourFrom){
				stroke(255, 0,0);
			}else{
				stroke(0);
			}
			float m = (float) map(hack, -13700000000.0, 2010.0, 20.0, (WIDTH - 20.0));
			eventArray[i].setPos(m);
			//System.out.println((int) m);
							
			point(m, CENTER);
			//line(m, (CENTER+ (check[(int) m]/2)) + 2, m, (CENTER - (check[(int) m] /2 )) -2);
			check[(int) m] += 1;

		}
		

	

		//save("data/history.tif");
		//line(20, 20, 200, 200);


	}

	public void mousePressed(){
		
		
	}
		



	public void draw() {

		background(255); 
		//Redraw all the event cous fuck it. Size is 1306 so not that bad really
		for(int i = 0; i < eventArray.length; i++){
			double time = eventArray[i].getTime();
			float pos = eventArray[i].getPos();
						
			if(time > colourFrom){
				stroke(255,0,0);				
			}else{
				stroke(0);
			}
			line(pos, (CENTER+ (check[(int) pos]/2)), pos, (CENTER - (check[(int) pos] /2 )));
			//point(pos, CENTER);

			
		}
		stroke(150);
		line(mouseX, 0, mouseX, HEIGHT);
		
	}

	public double map(double value, double istart, double istop, double ostart, double ostop){
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

	public void writeOut(Object[] array){
		try{
			FileWriter fstream = new FileWriter("data/History_of_universe.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			for(int i = 0; i < array.length; i++){
				out.write(array[i].toString());
				System.out.println(array[i]);
			}
			out.close();
			fstream.close();
		}catch(IOException e){
			e.printStackTrace();
		}


		System.out.println("Done");

	}
	public void addPeriods(Period[] array){ //Could make generic, no point
		for(int i = 0; i < array.length; i++){
			//System.out.println(array[i]);
			periods.addElement(array[i]);
		}

	}
}
