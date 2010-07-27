package CircleColor;

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

	public void setup() {
		size(1000, 1400);
		//frameRate(1);
		font = createFont("FFScala", 20);
		textFont(font);

		// outer = createFont("FFScala", strokeHeight);
		smooth();
		background(255);

		i = 10;
		count = 1;
		x = padding + radius/2;
		y = heightPadding + radius/2 + 20;

	}

	public void draw() {
		//background(255);

		colorMode(HSB);
		fill(i, 255, 255);
		noStroke();
		ellipse(x, y, radius, radius);
		// fill(0,255,255);
		// offset of 266
		arcText(Integer.toString(i), x -8, y, radius/2 - 1, 250, 1, 0);
		calcWidth();
		calcHeight();

		i += 10;
		count++;
		if (i >= 246) {
			i = 0;
			noLoop();
		}

	}
	
	public void keyPressed(){
		if(key == 's'){
			save("circles.tif");
			System.out.println("Saved");
		}
	}
	
	void calcWidth(){
		System.out.println(count % 4);
		if(count % 4 == 0 && i != 0){
			x = padding + radius /2;
			return;
		}
		x = x +  radius + (padding*2);
	}
	
	void calcHeight(){
		if(count % 4 == 0 && i != 0){
			y += heightPadding* 2 + radius;
		
		}
		
	}

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
