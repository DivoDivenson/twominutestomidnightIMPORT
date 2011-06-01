package Universe;

import java.util.Vector;

public class Period {
	
	String name;
	String text;
	Vector<Event> events;
	
	public Period(String name, String text){
		this.name = name;
		this.text = text;
		events = new Vector<Event>();
	}
	
	public void addEvent(Event event){
		events.add(event);		
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public Vector<Event> getEvents() {
		return events;
	}
	
	public String toString(){
		return name;
	}
	
	

}
