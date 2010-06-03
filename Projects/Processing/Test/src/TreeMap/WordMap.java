package TreeMap;

import java.util.HashMap;

import processing.core.PApplet;

import treemap.SimpleMapModel;

class WordMap extends SimpleMapModel {
	  HashMap words;
	  WordMap( ) {
	    words = new HashMap( );
	  }
	  void addWord(String word, PApplet p) {
	    WordItem item = (WordItem) words.get(word);
	    if (item == null) {
	      item = new WordItem(word, p);
	      words.put(word, item);
	    }
	    item.incrementSize( );
	  }
	  
	  void finishAdd( ) {
	    items = new WordItem[words.size( )];
	    words.values( ).toArray(items);
	  }
	}
