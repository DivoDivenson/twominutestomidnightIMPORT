package Recursion;

import java.io.File;

import processing.core.*;

public class setup extends PApplet {

	Node[] folders = new Node[10];
	int folderCount;
	int folderIndex;

	public void setup() {
		size(400,200);
		File rootFile = new File("/home/divo/media/sata/Video");
		Node rootNode = new Node(rootFile, this);
		rootNode.printList();
		PFont font = createFont("SansSerif", 11);
		textFont(font);

	}

	void addFolder(Node folder) {
		if (folderCount == folders.length) {
			folders = (Node[]) expand(folders);
		}
		folders[folderCount++] = folder;
	}

	void nextFolder() {
		if (folderIndex != folderCount) {
			Node n = folders[folderIndex++];
			n.check();
		}
	}
	
	public void draw(){
		background(255);
		nextFolder();
		drawStatus();
	}

	void drawStatus() {
		float statusX = 30;
		float statusW = width - statusX * 2;
		float statusY = 60;
		float statusH = 20;
		fill(0);
		if (folderIndex != folderCount) {
			text("Reading " + nfc(folderIndex + 1) + " out of "
					+ nfc(folderCount) + " folders...", statusX, statusY - 10);
		} else {
			text("Done reading.", statusX, statusY - 10);
			/*System.out.println(folders.length);
			folders[0].printList();
			System.exit(0);*/
		}
		fill(128);
		rect(statusX, statusY, statusW, statusH);
		float completedW = map(folderIndex + 1, 0, folderCount, 0, statusW);
		fill(255);
		rect(statusX, statusY, completedW, statusH);
	}

}
