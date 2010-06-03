package frac;

import java.awt.Color;

import processing.core.PApplet;

public class setup extends PApplet {
	
	private final int MIN = 0;
	private Color color = Color.blue;
	private int level;
	
	public void setup(){
		size(800,800);
		stroke(0);
		smooth();

	}
	
	public void draw(){
		background(255);
		drawFractal(level, 100, 90, 490, 400);


	}

	
	public void drawFractal(int level, int xA, int yA, int xB, int yB){
		if(level == 0){
			line(xA,yA,xB,yB);
		}else{
			int xC = (xA + xB)/2;
			int yC = (yA + yB)/2;
			
			int xD = xA + (xC - xA) /2 - (yC - yA)/2;
			int yD = yA + (yC - yA) /2 + (xC - xA)/2;
			
			drawFractal(level -1, xD, yD, xA, yA);
			drawFractal(level -1, xD, yD, xC, yC);
			drawFractal(level -1, xD, yD, xB, yB);
			
		}
	}
	
	public void mousePressed(){
		level++;
		System.out.println(level);		
	}
}
