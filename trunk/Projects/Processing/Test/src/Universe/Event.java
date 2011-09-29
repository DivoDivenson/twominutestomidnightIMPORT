package Universe;

public class Event implements Comparable {
	
	private String name;
	private long time;
	private float pos;
	
	public Event(String name, long time){
		this.name = name;
		this.time = time;
		
	}

	
	public Event() {
		// TODO Auto-generated constructor stub
	}


	public String getName() {
		return name;
	}

	public long getTime() {
		return time;
	}
	
	public String toString(){
		return String.format("\n %d : %s", time, name);
	}
	
	
	
	public float getPos() {
		return pos;
	}


	public void setPos(float pos) {
		this.pos = pos;
	}


	public int compareTo(Object event){
		if(this.time > ((Event)event).getTime()){
			return 1;
		}
		return 0;
	}


	


	
	
}
