package Facebook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import processing.core.PApplet;

import treemap.SimpleMapModel;

public class People extends SimpleMapModel {

	//HashMap people;
	PApplet p;

	public People(Person[] all) {
		System.out.println("Creating list of people");
		items = new Person[all.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = all[i];
		}
	}

	/*
	 * public void addPerson(Person person){ people.put(person.getID(), person);
	 * }
	 * 
	 * public void finishAdd(){ items = new Person[people.size()];
	 * people.values().toArray(items); }
	 * 
	 * public void addAll(Person[] all){ items = new Person[all.length]; for(int
	 * i = 0; i < items.length; i++){ items[i] = all[i]; } }
	 */

}
