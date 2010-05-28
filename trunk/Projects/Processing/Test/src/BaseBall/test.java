package BaseBall;

import java.io.File;
import java.util.HashMap;

import processing.core.*;

public class test extends PApplet {

	Parser parser;

	int teamCount = 30;
	String[] teamNames;
	String[] teamCodes;
	HashMap teamIndices;

	StandingsList standings;
	SalaryList salaries;

	StandingsList[] season;

	PImage[] logos;
	double logoWidth;
	double logoHeight;

	PFont font;
	SimpleDate date;

	static final int ROW_HEIGHT = 23;
	static final float HALF_ROW_HEIGHT = (float) (ROW_HEIGHT / 2.0);
	static final int SIDE_PADDING = 30;

	int dateSelectorX;
	int dateSelectorY = 30;

	public void setup() {
		size(480, 750);
		date = new SimpleDate();

		parser = new Parser(this);
		//parser.parseMlb();
		setupTeams();
		setupSalaries();
		setupStandings();
		setupLogos();


		font = createFont("Georgia", 12);
		textFont(font);

		System.out.println("Done");

	}

	public void draw() {
		background(255);
		smooth();

		translate(SIDE_PADDING, SIDE_PADDING);

		float leftX = 160;
		float rightX = 335;

		textAlign(LEFT, CENTER);

		for (int i = 0; i < teamCount; i++) {
			fill(128);
			System.out.println("Season " +season[0]);
			float standingsY = standingsPosition[i].value * ROW_HEIGHT + HALF_ROW_HEIGHT;
			image(logos[i], (float) 0, (float) (standingsY - logoHeight / 2),
					(float) logoWidth, (float) logoHeight);
			textAlign(LEFT, CENTER);
			text(teamNames[i], 28, standingsY);
			textAlign(RIGHT, CENTER);
			text(standings.getTitle(i), leftX - 10, standingsY);

			float salaryY = salaries.getRank(i) * ROW_HEIGHT + HALF_ROW_HEIGHT;

			if (salaryY >= standingsY) {
				stroke(33, 85, 156); // Blue for positive (or equal) difference.
			} else {
				stroke(206, 0, 82); // Red for wasting money.
			}
			float weight = map(salaries.getValue(i), salaries.getMinValue(),
					salaries.getMaxValue(), (float) 0.25, (float) 6);
			strokeWeight(weight);

			line(leftX, standingsY, rightX, salaryY);
			fill(128);
			textAlign(LEFT, CENTER);
			text(salaries.getTitle(i), rightX + 10, salaryY);

			text(salaries.getTitle(i), rightX + 10, salaryY);

		}
		drawDateSelector();
		boolean updated = false;
		for (int i = 0; i < teamCount; i++) {
		  if (standingsPosition[i].update()) {
		    updated = true;
		  }
		}
		if (!updated) {
		  noLoop( );
		}

	}

	public void setupTeams() {
		String[] lines = loadStrings("src/data/teams.tsv");
		File file = new File("src/data/teams.tsv");
		System.out.println((file.exists()) ? "File exists" : "No file");

		teamCount = lines.length;
		System.out.println(lines.length);
		teamCodes = new String[teamCount];
		teamNames = new String[teamCount];
		teamIndices = new HashMap();

		String[] peices;
		for (int i = 0; i < teamCount; i++) {
			// System.out.println(lines[i]);
			peices = split(lines[i], TAB);
			teamCodes[i] = peices[0];
			teamNames[i] = peices[1];

			// Associate each of the team codes with a numeric index
			teamIndices.put(teamCodes[i], new Integer(i));

		}
	}

	// Draw a series of lines for selecting the date.
	void drawDateSelector() {
		dateSelectorX = (width - date.getDateCount() * 2) / 2;
		strokeWeight(1);
		translate(0, -10);
		for (int i = 0; i < date.getDateCount(); i++) {
			int x = dateSelectorX + i * 2;
			// If this is the currently selected date, draw it differently.
			if (i == date.getDateIndex()) {
				stroke(0);
				line(x, 0, x, 13);
				textAlign(CENTER, TOP);
				text(date.getDatePretty()[date.getDateIndex()], x, 15);
			} else {
				// If this is a viewable date, make the line darker.
				if ((i >= date.getMinDateIndex())
						&& (i <= date.getMaxDateIndex())) {
					stroke(128); // Viewable date
				} else {
					stroke(204); // Not a viewable date
				}
				line(x, 0, x, 7);
			}
		}
	}

	public int teamIndex(String teamCode) {
		Integer index = (Integer) teamIndices.get(teamCode);
		return index.intValue();
	}

	public void setupSalaries() {
		String[] lines = loadStrings("salaries.tsv");
		System.err.println("SALARY HERE: " + lines[15]);
		salaries = new SalaryList(lines, teamCount, teamIndices);
	}

	public void setupStandings() {
		System.err.println("max date: " + date.getMaxDateIndex());
		season = new StandingsList[(date.getMaxDateIndex()) + 1];
		int max = date.getMaxDateIndex() + 1;
		for (int i = date.getMinDateIndex(); i < max -1; i++) {
			String[] lines = parser.acquireStandings(date.getDateStamp(i));
			season[i] = new StandingsList(lines, teamCount, teamIndices,
					salaries);
		}

	}

	public void setupLogos() {
		logos = new PImage[teamCount];
		for (int i = 0; i < teamCount; i++) {
			logos[i] = loadImage("src/data/small/" + teamCodes[i] + ".gif");
		}
		logoWidth = logos[0].width / 2.0;
		logoHeight = logos[0].height / 2.0;
	}

	void setDate(int index) {
		  date.setDateIndex(index);
		  standings = season[date.getDateIndex()];
		  for (int i = 0; i < teamCount; i++) {
		    standingsPosition[i].target(standings.getRank(i));
		  }
		  // Re-enable the animation loop.
		  loop( );
		}


	public void mousePressed() {
		handleMouse();
	}

	public void mouseDragged() {
		handleMouse();
	}

	void handleMouse() {
		if (mouseY < dateSelectorY) {
			int date = (mouseX - dateSelectorX) / 2;
			setDate(constrain(date, this.date.getMinDateIndex(), this.date.getMaxDateIndex()));
		}
	}
	
	Integrator[] standingsPosition;
	void setupRanking( ) {
	  standingsPosition = new Integrator[teamCount];
	  for (int i = 0; i < teamCodes.length; i++) {
	    standingsPosition[i] = new Integrator(i);
	  }
	}



}
