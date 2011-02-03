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
	PImage image, orgImage;
	boolean calcImage;

	float textPadding = 8;
	float imageX, imageY, imageW, imageH;

	public Person(String name, String id, PApplet p, int order) {
		first = name.split(" ")[0];
		last = name.split(" ")[1];
		//System.out.println(first);
		this.id = Long.parseLong(id);
		this.p = p;
		// this.order = order;
		size = p.random(1, 2);
		// size = 1;
		// System.out.println(first);
		imageX = x;
		imageY = y;
		imageW = w;
		imageH = h;
		//calcImage();
	}

	public Person(String name, String id, PApplet p, int order, PImage image) {
		this(name, id, p, order);
		this.image = image;
	}

	public void setImage(PImage image, String temp) {
		this.image = image;
		this.orgImage = image;
		System.out.println("Images loaded for " + id + " " + temp);
		// image.resize((int) calcWidth(),(int) calcHeight());
	}

	public String toString() {
		return new String(first + " " + last + " : " + id);
	}

	/*
	 * But in some sort of check so the images size does dnot have to be
	 * recalculted every time, just when the layout is changed
	 * 
	 * @see treemap.SimpleMapItem#draw()
	 */

	public float calcWidth() {
		float result;
		if (image.width > image.height) {
			result = w;
		} else {
			result = image.width - (image.height - h);
		}
		return result;
	}

	public float calcHeight() {
		float result;
		if (image.width > image.height) {
			result = image.height - (image.width - w);
		} else {
			result = h;
		}

		return result;
	}

	public void draw() {
		if (calcImage) {

			calcImage = false;
		}
		p.fill(0);
		p.rect(x - 2, y - 2, w - 2, h - 2);
		//System.err.println(first + last);
		p.imageMode(p.CORNER);
		p.image(image, x - 2, y  - 2, w - 2, h - 2);
		// p.image(image, x - 2, y - 2, calcWidth() - 2, calcHeight() - 2);

		// if (w > p.textWidth(first) + 6) {
		// if (h > p.textAscent() + 6) {
		p.noStroke();
		p.fill(100, 122);
		p.rect(x - 2, y +h - p.textAscent() - 12, p.textWidth(first) + 10, p.textAscent() + 6);
		p.textAlign(p.CORNER, p.CORNER);
		p.fill(210);
		// String temp = new String(String.format(" %.2f", size));
		p.text(first, x + 3, y + h - 9);

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
		p.text(first + size, x + w + textPadding, y + h - textPadding);
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

	public void calcImage() {
		System.out.println("calc");
		
		
		
		this.image = orgImage;
		float min,max;
		//fuck the resize code
	/*	if(this.image.height > this.image.width){ //crap but fuck it
			min = this.image.height;
			max = this.image.width;
			float loss = min - x;
			int prct = (int) (loss / (min / 100));
			max = max - ((max / 100) * prct);
			p.copy(image, 0, 0, image.width, image.height, 0, 0, (int) min,(int) max);
			
		}else{
			min = this.image.width;
			max = this.image.height;
			float loss = min - x;
			int prct = (int) (loss / (min / 100));
			max = max - ((max / 100) * prct);
			p.copy(image, 0, 0, image.width, image.height, 0, 0, (int) max,(int) min);
		}*/
		
		/*
		if (this.image != null) {
			System.out.println(image);
			if (image.width < image.height) {
				p.copy(image, 0, 0, image.width, image.height, 0, 0,
						image.width, (int) (image.height - (image.width - w)));
			} else {
				p.copy(image, 0, 0, image.width, image.height, 0, 0,
						(int) ((image.width) - (image.height - h)),
						image.height);
			}
		}*/
	}

	/*
	 * public int compareTo(Person other){ return (int) (id - other.getID()); }
	 */

}
