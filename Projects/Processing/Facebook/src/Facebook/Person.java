package Facebook;

import processing.core.PApplet;
import treemap.SimpleMapItem;

public class Person extends SimpleMapItem {

	String first;
	String last;
	long id;
	int level;
	PApplet p;

	float textPadding = 8;
	float boxLeft, boxTop, boxRight, boxBottom;

	public Person(String name, String id, PApplet p, int order) {
		first = name.split(" ")[0];
		last = name.split(" ")[1];
		this.id = Long.parseLong(id);
		this.p = p;
		// this.order = order;
		//size =  p.random(1, 3);
		//size = 1;
		// System.out.println(first);
	}

	public String toString() {
		return new String(first + " " + last + " : " + id);
	}

	private void calcBox() {
		boxLeft = x;
		boxTop = y;
		boxRight = x + w;
		boxBottom = y + w;
	}

	public void draw() {
		p.fill(0);
		p.rect(x-2, y-2, w-2, h-2);
		
		//if (w > p.textWidth(first) + 6) {
			//if (h > p.textAscent() + 6) {
		p.fill(100,122);
		p.rect(x-2, y-2, p.textWidth(first) + 12, p.textAscent() + 7);
				p.textAlign(p.CORNER, p.CORNER);
				p.fill(150);
				String temp = new String(String.format(" %.2f", size));
				p.text(first + temp, x + 3, y + 12);
				
			//}
		//}
	}

	/*
	 * public void draw(){ calcBox(); p.fill(200); p.rect(boxLeft, boxTop,
	 * boxRight, boxBottom); //p.rect(x, y, w, h); System.out.println(first);
	 * 
	 * drawTitle();
	 * 
	 * }
	 */

	private void drawTitle() {
		p.fill(0);
		p.textAlign(p.LEFT);
		p.text(first + size, boxLeft + textPadding, boxBottom - textPadding);
	}

	/*
	 * public void draw() { System.out.println("Drawing"); calcBox();
	 * p.fill((int) p.random(100, 255)); p.rectMode(p.CORNER);
	 * 
	 * p.rect(x-2,y-2,w-2,h-2); //p.rect(boxLeft, boxTop, boxRight, boxBottom);
	 * p.fill(0); // if(textFits()){ drawTitle(); //} }
	 * 
	 * void drawTitle() { p.fill(0); p.textAlign(p.LEFT); //p.text(first + " : "
	 * + size, boxLeft + textPadding, boxBottom - textPadding); p.text(first +
	 * " : " + size, x +30,y-200); }
	 * 
	 * boolean textFits() { float wide = p.textWidth(first) + textPadding * 2;
	 * float high = p.textAscent() + p.textDescent() + textPadding * 2; return
	 * (boxRight - boxLeft > wide) && (boxBottom - boxTop > high); }
	 */
	public long getID() {
		return this.id;
	}
	
	public String getFirst(){
		return this.first;
	}
	
	public String getName(){
		return this.first + " " + this.last;
	}

	/*
	 * public int compareTo(Person other){ return (int) (id - other.getID()); }
	 */

}
