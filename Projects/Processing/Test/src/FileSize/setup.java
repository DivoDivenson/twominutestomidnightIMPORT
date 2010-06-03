package FileSize;

import java.io.File;

import processing.core.*;
import treemap.*;

public class setup extends PApplet {
	FolderItem rootItem;
	PFont font;
	public FileItem rolloverItem;

	public void setup() {
		size(800, 600);
		rectMode(CORNERS);
		smooth();
		noStroke();
		font = createFont("SansSerif", 9);
		setRoot(new File("/home/divo/media/sata/Video"));
		// noLoop();
		cursor(CROSS);
	}

	void setRoot(File folder) {
		FolderItem tm = new FolderItem(null, folder, 0, 0, this);
		tm.setBounds(0, 0, width, height);
		tm.contentsVisible = true;
		rootItem = tm;
		rootItem.updateColors( );
	}

	public void draw() {
		background(255);
		textFont(font);
		rolloverItem = null;
		if (rootItem != null) {
			rootItem.draw();
		}
		if (rolloverItem != null) {
			rolloverItem.drawTitle();
		}

	}
	
	public void mousePressed( ) {
		  if (rootItem != null) {
		    rootItem.mousePressed( );
		  }
		}


}
