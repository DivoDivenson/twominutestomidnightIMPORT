package GEB;

import processing.core.*;

//import processing.core.PFont;

public class setup extends PApplet {
	PFont font;
	int i;
	int count;
	int strokeHeight = 36;
	int radius = 190;
	int padding = 25;
	int heightPadding = 15;
	int x, y;
	float position;

	int series[];

	public void setup() {
		size(2800, 2800);
		// frameRate(1);
		font = createFont("FFScala", 20);
		textFont(font);
		position = 1;

		// outer = createFont("FFScala", strokeHeight);
		smooth();
		background(255);

		i = 10;
		count = 19;
		x = padding + radius / 2;
		y = heightPadding + radius / 2 + 20;

		// Code to generate first 100 numbers in Hofstadter Figure-figure
		// sequence
		series = new int[20];
		int s = 1;
		series[0] = 0;
		for (int i = 1; i < series.length; i++) {
			series[i] = series[i - 1] + s;
			s++;
			
			for (int j = 0; j < series.length; j++) { // linear cous im lazy, do
														// binary as it will be
														// orderd. COULDENT  get that to work for some stupid fucking reason. oh well
				if (series[j] == s) {
					s++;
				}
			}
			System.out.println(series[i] + " " + s);
		}

	}

	public void draw() {
		// background(255);

		colorMode(HSB);
		fill(i, 255, 255);
		noStroke();
		ellipse(width / 2, height / 2, series[count] *10, series[count] *10);
		fill(255 - i, 150, 200);
		arcText(Integer.toString(series[count]), width / 2 - 8, height / 2, (series[count] *10) / 2 - 20,
				position, 1, 0);
		i += 30;
		position *= 1.61803399;
		count--;
		if (i >= 246) {
			i = 0;
			//noLoop();
		}
		if(count == -1)
			noLoop();

	}

	public void keyPressed() {
		if (key == 's') {
			save("GEB.tif");
			System.out.println("Saved");
		}
	}

	

	/*
	 * void calcWidth(){ System.out.println(count % 4); if(count % 4 == 0 && i
	 * != 0){ x = padding + radius /2; return; } x = x + radius + (padding*2); }
	 * 
	 * void calcHeight(){ if(count % 4 == 0 && i != 0){ y += heightPadding* 2 +
	 * radius;
	 * 
	 * }
	 * 
	 * }
	 */

	void arcText(String text, float x, float y, float radius, float startAngle,
			float arcScale, int direction) {
		if (g.textFont == null)
			return;
		if (text == null)
			return;
		if (text.length() <= 0)
			return;

		textFont(g.textFont);

		float totalWidth = textWidth(text);
		float circumference = PI * radius * 2;

		pushMatrix();
		translate(x, y);

		float widthAccumulate = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			float percent = widthAccumulate / circumference;
			float angle = radians(startAngle + percent * 360 * arcScale)
					* (direction >= 0 ? 1 : -1);
			widthAccumulate += textWidth(c);

			float px = cos(angle) * radius;
			float py = sin(angle) * radius;

			pushMatrix();
			translate(px, py);

			// this rotates from the -center- of the letter
			translate(textWidth(c) / 2, 0);
			rotate(angle + radians(90));
			translate(-textWidth(c) / 2, 0);
			text(c, 0, 0);

			popMatrix();
		}

		popMatrix();
	}

}
