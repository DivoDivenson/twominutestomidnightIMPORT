package Facebook;

import processing.core.PApplet;
import processing.core.PImage;
import treemap.SimpleMapItem;

public class Person extends SimpleMapItem {

	String first;
	String last;
	long id;
	int level;
	PApplet p;
	PImage image;

	float textPadding = 8;
	float boxLeft, boxTop, boxRight, boxBottom;

	public Person(String name, String id, PApplet p, int order) {
		first = name.split(" ")[0];
		last = name.split(" ")[1];
		this.id = Long.parseLong(id);
		this.p = p;
		// this.order = order;
		size = p.random(1, 2);
		// size = 1;
		// System.out.println(first);
	}

	public Person(String name, String id, PApplet p, int order, PImage image) {
		this(name, id, p, order);
		this.image = image;
	}

	public void setImage(PImage image, String temp) {
		this.image = image;
		System.out.println("Images loaded for " + id + " " + temp);
		//image.resize((int) calcWidth(),(int) calcHeight());
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
	
	/*
	 * But in some sort of check so the images size does dnot have to be recalculted every time, just when the layout is changed
	 * @see treemap.SimpleMapItem#draw()
	 */
	
	public float calcWidth(){
		float result;
		if(image.width > image.height){
			result = w;
		}else{
			result = image.width - (image.height - h);
		}
		return result;
	}

	public float calcHeight(){
		float result;
		if(image.width > image.height){
			result = image.height - (image.width -w);
		}else{
			result = h;
		}
		
		return result;
	}
	public void draw() {
		p.fill(0);
		p.rect(x - 2, y - 2, w - 2, h - 2);
		p.imageMode(p.CORNER);
		p.image(image, x-2, y-2, w-2, h-2);
		//p.image(image, x - 2, y - 2, calcWidth() - 2, calcHeight() - 2);

		// if (w > p.textWidth(first) + 6) {
		// if (h > p.textAscent() + 6) {
		p.noStroke();
		p.fill(100, 122);
		p.rect(x - 2, y - 2, p.textWidth(first) + 10, p.textAscent() + 6);
		p.textAlign(p.CORNER, p.CORNER);
		p.fill(210);
		// String temp = new String(String.format(" %.2f", size));
		p.text(first, x + 3, y + 12);

		// }
		// }
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

	public String getFirst() {
		return this.first;
	}

	public String getName() {
		return this.first + " " + this.last;
	}

	/*
	 * public int compareTo(Person other){ return (int) (id - other.getID()); }
	 */

}
