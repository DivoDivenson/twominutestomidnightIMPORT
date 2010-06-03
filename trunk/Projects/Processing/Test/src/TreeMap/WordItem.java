
package TreeMap;

import processing.core.PApplet;
import treemap.SimpleMapItem;

public class WordItem extends SimpleMapItem {
	  String word;
	  PApplet p;
	  WordItem(String word, PApplet p) {
	    this.word = word;
	    this.p = p;
	  }
	  public void draw( ) {
	    p.fill(255);
	    p.rect(x, y, w, h);
	    p.fill(0);
	    if (w > p.textWidth(word) + 6) {
	      if (h > p.textAscent( ) + 6) {
	        p.textAlign(p.CENTER, p.CENTER);
	        p.text(word, x + w/2, y + h/2);
	      }
	    }
	  }
	}
