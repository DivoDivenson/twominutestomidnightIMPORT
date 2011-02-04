
package TreeMap;

import processing.core.PApplet;
import treemap.SimpleMapItem;

public class WordItem extends SimpleMapItem {
	  public String word;
	  PApplet p;
	  int colour;
	  WordItem(String word, PApplet p) {
	    this.word = word;
	    this.p = p;
	    this.colour =  (int) p.random(0,255);
	  }
	  public void draw( ) {
		p.colorMode(p.HSB);
	    p.fill(colour, 200,255);
	    p.rect(x, y, w, h);
	    p.fill(0);
	    if (w > p.textWidth(word) + 6) {
	      if (h > p.textAscent( ) + 6) {
	        p.textAlign(p.CENTER, p.CENTER);
	        p.text(word, x + w/2, y + h/2);
	      }
	    }
	  }
	  
	  public boolean MouseOver(int xcord, int ycord){
		if(xcord > this.x && xcord < this.x + this.w){
			//System.out.println(word);
			if(ycord > this.y && ycord < this.y + this.h){
			//System.out.println("MOuse over " + word);
				return true;
			}
		}
		//System.out.println("MOuse not over " + word);
		return false;
	  
	  }
	}
