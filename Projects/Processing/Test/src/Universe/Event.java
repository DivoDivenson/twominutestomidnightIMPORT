package Universe;

public class Event {
	
	private String name;
	private long time;
	
	public Event(String name, long time){
		this.name = name;
		this.time = time;
		
	}

	
	public String getName() {
		return name;
	}

	public long getTime() {
		return time;
	}
}
