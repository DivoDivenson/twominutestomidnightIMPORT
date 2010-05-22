
import processing.core.PApplet;
import processing.core.PImage;

PImage mapImage;

public class hello extends PApplet{

	public void setup(){
		size(200, 200);
		background(255);
	}
	
	public void draw(){
		stroke(123,123,123);
		line(mouseX, 20, 20, mouseY);
	}
	
	public void mousePressed(){
		background(255);
	}
}
