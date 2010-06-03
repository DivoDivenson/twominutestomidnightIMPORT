package FileSize;

import java.awt.Color;
import java.io.File;

import processing.core.*;
import treemap.SimpleMapItem;

class FileItem extends SimpleMapItem {
	FolderItem parent;
	File file;
	String name;
	int level;
	float textPadding = 8;
	float boxLeft, boxTop;
	float boxRight, boxBottom;
	setup p;
	int c;
	float hue;
	float brightness;
	boolean contentsVisible;


	FileItem(FolderItem parent, File file, int level, int order, setup p) {
		this.p = p;
		if(parent == null){
			parent = (FolderItem) this;
			System.out.println("Null");
		}else{
			this.parent = parent;
		}
		this.file = file;
		this.order = order;
		this.level = level;
		name = file.getName();
		size = file.length();
	}

	void calcBox() {
		boxLeft = x;
		boxTop = y;
		boxRight = x + w;
		boxBottom = y + h;
	}

	public void draw() {
		calcBox();
		p.fill(c);
		p.rect(boxLeft, boxTop, boxRight, boxBottom);
		if (textFits()) {
			drawTitle();
		} else if (mouseInside()) {
			p.rolloverItem = this;
		}
	}

	void drawTitle() {
		p.fill(255, 200);
		p.textAlign(p.LEFT);
		p.text(name, boxLeft + textPadding, boxBottom - textPadding);
	}

	boolean textFits() {
		float wide = p.textWidth(name) + textPadding * 2;
		float high = p.textAscent() + p.textDescent() + textPadding * 2;
		return (boxRight - boxLeft > wide) && (boxBottom - boxTop > high);
	}

	boolean mouseInside() {
		return (p.mouseX > boxLeft && p.mouseX < boxRight && p.mouseY > boxTop && p.mouseY < boxBottom);
	}

	boolean mousePressed() {
		if (mouseInside()) {
			if (p.mouseButton == p.RIGHT) {
				System.err.println(parent);
				parent.hideContents();
				return true;
			}
		}
		return false;
	}

	void updateColors() {
		if (parent != null) {
			hue = p.map(order, 0, parent.getItemCount(), 0, 360);
			//hue = 120;
		}
		brightness = p.random(20, 80);
		p.colorMode(p.HSB, 360, 100, 100);
		if (parent == p.rootItem) {
			c = p.color(hue, 80, 80);
		} else if (parent != null) {
			c = p.color(parent.hue, 80, brightness);
		}
		p.colorMode(p.RGB, 255);
	}

	void hideContents() {
		// Prevent the user from closing the root level.
		if (parent != null) {
			contentsVisible = false;
		}
	}

}
