package TreeMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;

import treemap.Rect;
import treemap.SimpleMapModel;

class WordMap extends SimpleMapModel {
	HashMap words;


	Collection c;
	WordMap() {
		words = new HashMap();
		
	}

	void addWord(String word, PApplet p) {
		WordItem item = (WordItem) words.get(word);
		if (item == null) {
			item = new WordItem(word, p);
			words.put(word, item);
		}
		item.incrementSize();
	}

	void finishAdd() {
		items = new WordItem[words.size()];
		words.values().toArray(items);
		c = words.values();
	}
	
	//Iterate through a hash map very slow or just slow?
	
	public String mouseOver(int x, int y){
		System.out.println("Word called" + x + " " + y);
		WordItem temp;
		Iterator itr = c.iterator();
		while(itr.hasNext()){
			temp = (WordItem) itr.next();
			if(temp.MouseOver(x,y)){
				return String.format("%s %.0f times", temp.word , temp.getSize());
			}
		}
		
		
		return "";
	}
}
