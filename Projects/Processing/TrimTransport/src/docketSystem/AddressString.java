package docketSystem;

public class AddressString{
	//cut off new line to display
	
	String string;
	

	

	public AddressString(String string){
		this.string = string;
	}
	
	public String toString(){
		return string.split("\n")[0];
	}
	
	public String fullString(){
		return string;
	}
}
