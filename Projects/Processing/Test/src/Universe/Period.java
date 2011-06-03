package Universe;

import java.util.Iterator;
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

	public Event[] getEvents() {
		return (Event[])events.toArray(new Event[0]);
	}
	
	public int getEventsLength(){
		return events.size();
	}
	
	public String toString(){
		return name;
	}

	public String print() {
		String result = '\n' + name + '\n' + text;
		for(Iterator<Event> i = events.iterator(); i.hasNext(); ){
			Event temp = i.next();
			result += "\n\n\t" + temp.getName() + '\n';
			result += "\t" + temp.getTime();
		}
		return result;
		
	}
	
	

}
