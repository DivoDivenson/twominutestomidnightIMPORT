package TreeMap;

import java.util.Comparator;
import java.util.Vector;


import processing.core.*;
import treemap.Treemap;

public class setup extends PApplet {


	Treemap map;
	public void setup( ) {
	  size(1200, 800);
	  smooth( );
	  strokeWeight(0.25f);
	  PFont font = createFont("Serif", 13);
	  textFont(font);
	  WordMap mapData = new WordMap( );
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
	  map = new Treemap(mapData, 0, 0, width, height);
	  // Run draw( ) only once.
	  noLoop( );
	}
	public void draw( ) {
	  background(255);
	  map.draw( );
	}

}

class MyComparator implements Comparator<String> {
	public int compare(String strA, String strB) {
		return strA.compareToIgnoreCase(strB);
	}
}

