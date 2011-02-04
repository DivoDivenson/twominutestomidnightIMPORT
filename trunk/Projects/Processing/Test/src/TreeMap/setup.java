package TreeMap;

import java.awt.Rectangle;
import java.util.Comparator;
import java.util.Vector;


import processing.core.*;
import treemap.Rect;
import treemap.Treemap;

public class setup extends PApplet {

	WordMap mapData;
	int wordBox = 20;
	String word = "";

	Treemap map;
	public void setup( ) {
	  size(1200, 800);
	  smooth( );
	  strokeWeight(0.25f);
	  PFont font = createFont("Dialog.plain", 13);
	  textFont(font);
	  mapData = new WordMap( );
	  
	  String[] lines = loadStrings("src/data/newfile.txt");
	  String[] freq = loadStrings("src/data/blank.txt");
	  
	  MyComparator comp = new MyComparator();
	  java.util.Arrays.sort(freq, comp);
	  Vector<String> temp = new Vector();
	  
	  
	  for(int i = 0; i < freq.length; i++){
		  temp.add(freq[i]);
	  }
	  
	  for (int i = 0; i < lines.length; i++) {
		  if(!(temp.contains(lines[i]))){
			//  try{
			//	  Integer.parseInt(lines[i]);
		//	  }catch(NumberFormatException e){ //Holy hell thats shite code
				  if(!lines[i].contains("$")){
				  mapData.addWord(lines[i], this);
				  }
		//	  }
		  }else{
			  System.out.println(lines[i]);
		  }
		 
	  }
	  mapData.finishAdd( );
	  map = new Treemap(mapData, 0, 0, width, height - wordBox);
	 /* String[] fontList = PFont.list();
	  for(int i1 = 0; i1 < fontList.length; i1++){
		  System.out.println(fontList[i1]);
	  }*/
	  // Run draw( ) only once.
	  //noLoop( );
	}
	
	public void draw( ) {
	  background(255);
	  map.draw( );
	  //System.out.println(mapData.mouseOver(mouseX, mouseY));
	  renderWord();

	}




	public void mousePressed(){
		System.out.println(word = mapData.mouseOver(mouseX, mouseY));
	}
	
	public void renderWord(){
		fill(200);
		rect(0,height - wordBox, width, wordBox);
		fill(0);
		textAlign(CENTER, CENTER);
		text(word, width /2, height -wordBox/2 -3);
		      
		    
	}
}

class MyComparator implements Comparator<String> {
	public int compare(String strA, String strB) {
		return strA.compareToIgnoreCase(strB);
	}
}



