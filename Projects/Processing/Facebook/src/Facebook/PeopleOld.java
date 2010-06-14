package Facebook;


import java.util.HashMap;

import treemap.MapLayout;
import treemap.MapModel;
import treemap.Mappable;
import treemap.PivotBySplitSize;
import treemap.SimpleMapModel;


public class PeopleOld extends SimpleMapModel {

	HashMap people;
	
	public PeopleOld(){
		people = new HashMap();
	}
	
	public void addPerson(Person person){
		people.put(person.getID(), person);
	}
	
	public void finsihAdd(){
		items = new Person[people.size()];
		people.values().toArray(items);
	}
	
	/*MapLayout algo = new PivotBySplitSize();
	Mappable[] people;
	
	public People(Person[] peopleArray){
		this.people = new Mappable[peopleArray.length];
		for(int i = 0; i < people.length; i++){
			people[i] = peopleArray[i];
		}
	}

	@Override
	public Mappable[] getItems() {
		return people;
	}*/
	
	
	
	
}
