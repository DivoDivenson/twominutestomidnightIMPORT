package ZipCodeMap;

import java.awt.Color;

import processing.core.*;

public class ZipMap extends PApplet {

	// column numbers in the data file
	static final int CODE = 0;
	static final int X = 1;
	static final int Y = 2;
	static final int NAME = 3;

	int totalCount; // total number of places
	Place[] places;
	int placeCount; // number of places loaded

	// min/max boundary of all points
	float minX, maxX;
	float minY, maxY;

	// Border of where the map should be drawn on screen
	float mapX1, mapY1;
	float mapX2, mapY2;

	Color backgroundColor = new Color(51, 51, 51); // dark background color
	Color dormantColor = new Color(153, 153, 102); // initial color of the map
	Color highlightedColor = new Color(203, 203, 203); // color for selected
	// points
	Color unhighlightedColor = new Color(102, 102, 76); // color for points that
	// are not selected
	Color badColor = new Color(255, 255, 102); // text color when nothing found

	PFont font;
	String typedString = "";
	char typedChars[] = new char[5];
	protected int typedCount;
	int typedPartials[] = new int[6];
	float messageX, messageY;
	int foundCount;

	Place chosen;

	public void setup() {
		size(720, 453, P3D);
		mapX1 = 30;
		mapX2 = width - mapX1;
		mapY1 = 20;
		mapY2 = height - mapY1;

		readData();

		rectMode(CENTER);
		font = createFont("Georgia", 14);
		textFont(font);
		textMode(SCREEN);
		messageX = 40;
		messageY = height - 40;

	}

	public void draw() {
		background(0);
		for (int i = 0; i < placeCount; i++) {
			places[i].draw();
		}
		if (typedCount != 0) {
			if (foundCount > 0) {
				if (typedCount == 4) {
					System.out.println(chosen);
					// Redraw the chosen ones, because they're often occluded
					// by the non-selected points.
					for (int i = 0; i < placeCount; i++) {
						if (places[i].matchDepth == typedCount) {
							places[i].draw();
						}
					}
				}
				if (chosen != null) {
					chosen.drawChosen();
					System.out.println("Drawing");
				}
				fill(highlightedColor.getRGB());
				textAlign(LEFT);
				text(typedString, messageX, messageY);
			} else {
				fill(badColor.getRGB());
				text(typedString, messageX, messageY);
			}
		}

	}
	void readData( ) {
		  new Slurper(this);
	}


	void readData(boolean luls) {
		String[] lines = loadStrings("src/data/zips.tsv");
		parseInfo(lines[0]); // read the header line
		places = new Place[totalCount];
		for (int i = 1; i < lines.length; i++) {
			// System.out.println(placeCount);
			places[placeCount] = parsePlace(lines[i]);
			placeCount++;
		}
	}

	void parseInfo(String line) {
		String infoString = line.substring(2); // remove the #
		String[] infoPieces = split(infoString, ',');
		totalCount = Integer.parseInt(infoPieces[0]);
		minX = Float.parseFloat(infoPieces[1]);
		maxX = Float.parseFloat(infoPieces[2]);
		minY = Float.parseFloat(infoPieces[3]);
		maxY = Float.parseFloat(infoPieces[4]);
	}

	Place parsePlace(String line) {
		String pieces[] = split(line, TAB);
		int zip = Integer.parseInt(pieces[CODE]);
		float x = Float.parseFloat(pieces[X]);
		float y = Float.parseFloat(pieces[Y]);
		String name = pieces[NAME];
		return new Place(zip, name, x, y, this);
	}

	float TX(float x) {
		return map(x, minX, maxX, mapX1, mapX2);
	}

	float TY(float y) {
		return map(y, minY, maxY, mapY2, mapY1);
	}
	

	public void keyPressed() {
		if ((key == BACKSPACE) || (key == DELETE)) {
			if (typedCount > 0) {
				typedCount--;
			}
			updateTyped();
		} else if ((key >= '0') && (key <= '9')) {
			if (typedCount != 5) { // Stop at 5 digits.
				if (foundCount >= 0) { // If nothing found, ignore further
					// typing.
					typedChars[typedCount++] = key;
				}
			}
		}
		updateTyped();
	}

	void updateTyped() {
		typedString = new String(typedChars, 0, typedCount);
		if (typedString.equalsIgnoreCase("")) {
			typedPartials[typedCount] = 0;
		} else {
			typedPartials[typedCount] = Integer.parseInt(typedString);
		}
		for (int j = typedCount - 1; j > 0; --j) {
			typedPartials[j] = typedPartials[j + 1] / 10;
		}
		foundCount = 0;
		//chosen = null;
		for (int i = 0; i < placeCount; i++) {
			// Update boundaries of selection
			// and identify whether a particular place is chosen.
			places[i].check();
		}
	}

}
