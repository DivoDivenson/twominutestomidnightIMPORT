package MapMaker;
import java.io.PrintWriter;

import processing.core.*;

public class Maker extends PApplet {

	PImage mapImage;
	Table nameTable;
	int currentRow = -1;
	PrintWriter writer;
	public void setup( ) {
	  size(640, 400);
	  mapImage = loadImage("/src/data/map.png");
	  nameTable = new Table("/src/data/names.tsv", this);
	  writer = createWriter("locations.tsv");
	  cursor(CROSS); // make easier to pinpoint a location
	  println("Click the mouse to begin.");
	}
	public void draw( ) {
	  image(mapImage, 0, 0);
	}
	public void mousePressed( ) {
	  if (currentRow != -1) {
	    String abbrev = nameTable.getRowName(currentRow);
	    writer.println(abbrev + "\t" + mouseX + "\t" + mouseY);
	  }
	  currentRow++;
	  if (currentRow == nameTable.getRowCount( )) {
	    // Close the file and finish.
	    writer.flush( );
	    writer.close( );
	    exit( );
	  } else {
	    // Ask for the next coordinate.
	    String name = nameTable.getString(currentRow, 1);
	    println("Choose location for " + name + ".");
	  }
	}

}
