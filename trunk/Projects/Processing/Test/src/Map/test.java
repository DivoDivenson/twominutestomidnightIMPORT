package Map;
import processing.core.*;

public class test extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PImage mapImage;
	Table locationTable;
	Table dataTable;
	float dataMax = 10;
	float dataMin = -10;
	int rowcount;

	PFont font;

	Integrator[] interpolators;

	public void setup() {
		size(640, 400);
		mapImage = loadImage("/src/data/map.png");
		locationTable = new Table("/src/data/locations.tsv", this);
		dataTable = new Table("/src/data/random.tsv", this);
		rowcount = locationTable.getRowCount();
		font = loadFont("/src/data/DejaVuSans-12.vlw");
		textFont(font);

		interpolators = new Integrator[rowcount];

		for (int row = 0; row < rowcount; row++) {
			float value = dataTable.getFloat(row, 1);
			interpolators[row] = new Integrator(value);
		}

		smooth();
		noStroke();

	}

	float closestDist;
	String closestText;
	float closestTextX;
	float closestTextY;

	public void draw() {
		background(255);
		image(mapImage, 0, 0);
		fill(192, 0, 0);

		for (int i = 0; i < rowcount; i++) {
			interpolators[i].update();
		}

		closestDist = MAX_FLOAT;

		for (int i = 0; i < rowcount; i++) {
			String temp = dataTable.getRowName(i);
			float x = locationTable.getFloat(i, 1);
			float y = locationTable.getFloat(i, 2);
			drawData(x, y, temp);
		}

		if (closestDist != MAX_FLOAT) {
			fill(0);
			textAlign(CENTER);
			text(closestText, closestTextX, closestTextY);
		}

	}

	private void drawData(float x, float y, String abbrev) {
		int row = dataTable.getRowIndex(abbrev);
		float value = interpolators[row].value;

		float radius = 0;
		if (value >= 0) {
			radius = map(value, 0, dataMax, 1.5f, 15);
			fill(68, 34, 204);
		} else {
			radius = map(value, 0, dataMin, 1.5f, 15);
			fill(255, 68, 34);
		}
		ellipseMode(RADIUS);
		ellipse(x, y, radius, radius);

		float d = dist(x, y, mouseX, mouseY);
		if ((d < radius + 2) && (d < closestDist)) {
			closestDist = d;
			String val = nfp(interpolators[row].target, 0, 2);
			closestText = abbrev + " " + val;
			closestTextX = x;
			closestTextY = y - radius - 4;
		}

	}

	public void keyPressed() {
		if (key == ' ') {
			updateTable();
		}
	}

	public void updateTable() {
		for (int row = 0; row < rowcount; row++) {
			float newValue = random(-10, 10);
			interpolators[row].target(newValue);
		}
	}

}
