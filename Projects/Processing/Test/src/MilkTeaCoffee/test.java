package MilkTeaCoffee;


import processing.core.*;

public class test extends PApplet {

	FloatTable data;
	float dataMin, dataMax;
	int yearMin, yearMax;
	int[] years;
	
	float plotX1, plotY1;
	float plotX2, plotY2;
	float labelX, labelY;
	PFont font;
	String title;
	
	float barWidth;
	int rowCount;
	
	int columnCount, currentColumn = 0;
	int yearInterval = 10;
	int volumeInterval = 10;
	int volIntervalMinor = 5;
	boolean press;
	boolean type;
	
	public void setup(){
		size(720, 405);
		data = new FloatTable("/src/data/milk-tea-coffee.tsv", this);
		press = false;
		
		dataMin = 0;
		//dataMax = data.getTableMax();
		dataMax = ceil(data.getTableMax() / volumeInterval) * volumeInterval;
		
		years = parseInt(data.getRowNames());
		yearMin = years[0];
		yearMax = years[years.length -1];
		
		plotX1 = 120;
		plotX2 = width  - 60;
		plotY1 = 50;
		plotY2 = height - 70;
		labelX = 50;
		labelY = height - 25;
		
		//font = loadFont("/src/data/DejaVuSans-12.vlw");
		font = createFont("SansSerif", 12);
		textFont(font);
		textSize(12);
		barWidth = ((plotX1 - plotX2) / data.getRowCount());
		rowCount = data.getRowCount();


		
		smooth();
		
		
	}
	
	public void draw(){
		
		background(255);
		
		
		fill(255);
		
		//rectMode(CORNERS);
		//noStroke();
		//rect(plotX1, plotY1, plotX2, plotY2);
		
		columnCount = data.getColumnCount();
		//drawYearLabels();

		strokeWeight(5);
		stroke(86, 121, 193);
		/*********/
		noFill();
		strokeWeight(2);
		drawDataCurve(currentColumn);
		//drawDataLine(currentColumn);
		//drawDataHighlight(currentColumn);		
		/*********/
		
		//drawDataPoints(currentColumn, true);
		if (!type) {
			switch (currentColumn) {
			case 0: {
				stroke(121, 86, 193);
				drawDataPoints(1);
				stroke(121, 193, 86);
				drawDataPoints(2);
				break;
			}
			case 1: {
				stroke(121, 86, 193);
				drawDataPoints(0);
				stroke(121, 193, 86);
				drawDataPoints(2);
				break;
			}
			case 2: {
				stroke(121, 86, 193);
				drawDataPoints(0);
				stroke(121, 193, 86);
				drawDataPoints(1);
				break;
			}
			default:
				break;

			}
		}
		
		/*stroke(121, 86, 193);
		drawDataPoints(1);
		stroke(193, 121, 86);
		drawDataPoints(2);*/

		
		/*title = data.getColumnName(1);
		text(title, 2, map(data.getFloat(0, 1), dataMin, dataMax, plotY2, plotY1));
		title = data.getColumnName(2);
		text(title, 2, map(data.getFloat(0, 2), dataMin, dataMax, plotY2, plotY1));*/
		
		
		drawTitle();
		if(press){
			//drawBar();
			drawDataHighlight(currentColumn);
		}
		drawVolumeLabel();
		drawAxisLabels();
		drawYearLabels();
	
		
		
	}
	
	void drawDataLine(int col) {
		  beginShape( );
		  int rowCount = data.getRowCount( );
		  for (int row = 0; row < rowCount; row++) {
		    if (data.isValid(row, col)) {
		      float value = data.getFloat(row, col);
		      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
		      float y = map(value, dataMin, dataMax, plotY2, plotY1);
		      vertex(x, y);
		    }
		  }
		  endShape( );
		}
	
	void drawDataCurve(int col) {
		  beginShape( );
		  int rowCount = data.getRowCount( );
		  for (int row = 0; row < rowCount; row++) {
		    if (data.isValid(row, col)) {
		      float value = data.getFloat(row, col);
		      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
		      float y = map(value, dataMin, dataMax, plotY2, plotY1);
		      curveVertex(x, y);
		      if((row == 0) || (row == rowCount -1)){
		    	  curveVertex(x,y);
		      }
		    }
		  }
		if (type) {
			fill(86, 121, 193);
			vertex(plotX2, plotY2);
			vertex(plotX1, plotY2);
			endShape(CLOSE);
		}else{
			endShape();
		}
		  //endShape( );
		}

	
	public void drawYearLabels(){
		fill(0);
		textSize(10);
		textAlign(CENTER, TOP);
		int rowCount = data.getRowCount();
		stroke(255);
		strokeWeight(1);
		for(int row = 0; row < rowCount; row++){
			if(years[row] % yearInterval ==0 ){
				float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
				text(years[row], x, plotY2 + 10);
				line(x, plotY1, x, plotY2);
			}
			
		}
	}
	
	public void drawAxisLabels(){
		fill(0);
		textSize(13);
		textLeading(15);
		
		textAlign(CENTER, CENTER);
		text("Gallons\nconsumed\nper capita",labelX, (plotY1+plotY2)/2);
		textAlign(CENTER);
		text("Year", (plotX2+plotX1)/2, labelY);
	}
	
	public void drawTitle(){
		fill(0);
		title = data.getColumnName(currentColumn);
		textSize(20);
		textAlign(LEFT);
		//text(title, 20, map(data.getFloat(0, currentColumn), dataMin, dataMax, plotY2, plotY1));
		text(title, plotX1 -20, plotY1 - 10);

	}
	
	public void drawDataPoints(int col){
		drawDataPoints(col, false);
	}
	
	public void drawDataPoints(int col, boolean stroke){
		int rowCount = data.getRowCount();
		if(type){
			strokeWeight(1);
			fill(168,241,255);
			beginShape();
		}else{
			if(stroke){
				strokeWeight(6);
			}else{
				strokeWeight(4);
			}
		}
		//beginShape();
		for(int row = 0; row < rowCount; row++){
			if(data.isValid(row, col)){
				float value = data.getFloat(row, col);
				float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
				//println(x);
				float y = map(value, dataMin, dataMax, plotY2, plotY1);
				if(type){
					vertex(x, y);
				}else{
					point(x, y);
				}
			}
		}
		if(type){
			vertex(plotX2, plotY2);
			vertex(plotX1, plotY2);
			endShape(CLOSE);
		}
	}
	
	public void drawVolumeLabel(){
		fill(0);
		textSize(10);
		textAlign(RIGHT, CENTER);
		strokeWeight(2);
		stroke(57,81,107);

		
		for(float v = dataMin; v < dataMax; v+= volIntervalMinor){
			if( v % volIntervalMinor == 0){
				float y = map(v, dataMin, dataMax, plotY2, plotY1);
				if( v % volumeInterval == 0){
					if(v == dataMin){
						textAlign(RIGHT);
					}else if(v == dataMax){
						textAlign(RIGHT, TOP);
					}else{
						textAlign(RIGHT, CENTER);
					}
					text(floor(v), plotX1 - 10, y);
					line(plotX1 - 4, y, plotX1, y);
				}else{
					line(plotX1 -2, y, plotX1, y);
				}
			}
			
		}
	}
	
	public void drawDataHighlight(int col){
		for(int row = 0; row < rowCount; row++){
			if(data.isValid(row,col)){
				float value = data.getFloat(row, col);
				float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
				float y = map(value, dataMin, dataMax, plotY2, plotY1);
				if(dist(mouseX, 0, x, 0) < 3){
				//if((mouseX - x) < 3 ){
					strokeWeight(10);
					point(x, y);
					fill(0);
					textSize(10);
					textAlign(CENTER);
					text(nf(value, 0, 2) + " (" + years[row] + ")", x,y-8);
				}
			}
		}
	}
	
	public void drawBar(){
		strokeWeight(0);
		fill(120, 50);
		stroke(57,81,107);

		float year;
		textAlign(CENTER, TOP);
		textSize(12);
		//float width =  data.getRowCount();
		//float x = map(mouseX, yearMin, yearMax, plotX1, plotY2);

		if(mouseX > plotX2){
			rect(plotX2, plotY1, plotX2 + barWidth, plotY2);
			fill(0);
			year = map(plotX2, plotX1, plotX2, yearMin, yearMax);
			//text(floor(year), plotX2, plotY2 + 30);
			//text(new String(String.format("%.1f",data.getFloat(floor(year - yearMin), currentColumn))), plotX2, plotY1 -20);

		}else if(mouseX + barWidth < plotX1){
			rect(plotX1 , plotY1, plotX1 - barWidth, plotY2);
			fill(0);
			year = map(plotX1, plotX1, plotX2, yearMin, yearMax);
			//text(floor(year), plotX1, plotY2 + 30);
			//text(new String(String.format("%.1f",data.getFloat(floor(year - yearMin), currentColumn))), plotX1, plotY1 -20);
		}else{
			rect(mouseX, plotY1, mouseX + barWidth , plotY2);
			fill(0);
			year = map(mouseX, plotX1, plotX2, yearMin, yearMax);
			//text(floor(year), mouseX, plotY2 + 30);				
			//text(new String(String.format("%.1f",data.getFloat(floor(year - yearMin), currentColumn))), mouseX, plotY1 -20);

		}
		textSize(15);
		text(floor(year), (plotX1 + plotX2) /2, labelY + 3);
		text(new String(String.format("%.1f",data.getFloat(floor(year - yearMin), currentColumn))), labelX, (plotY1 +plotY2)/2 + 30);

		
	}
	
	public void mousePressed(){
		press = !press;		
	}
	

	public void keyPressed() {
		if (key == '[') {
			currentColumn--;
			if (currentColumn < 0) {
				currentColumn = columnCount - 1;
			}
		} else if (key == ']') {
			currentColumn++;
			if (currentColumn >= columnCount) {
				currentColumn = 0;
			}
		}else if(key == 't'){
			type = !type;
		}
	}
}
