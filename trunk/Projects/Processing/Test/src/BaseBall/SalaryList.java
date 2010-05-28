package BaseBall;

import java.util.HashMap;

import processing.core.PApplet;

class SalaryList extends RankedList {
	
	HashMap teamIndices;

	SalaryList(String[] lines, int teamCount, HashMap teamIndices) {
		super(teamCount, false);
		
		this.teamIndices = teamIndices;

		
		for (int i = 0; i < teamCount; i++) {
			String pieces[] = PApplet.split(lines[i], PApplet.TAB);
			// First column is the team's 2- or 3-digit team code.
			int index = teamIndex(pieces[0]);
			// Second column is the salary as a number.
			value[index] = PApplet.parseInt(pieces[1]);
			// Make the title in the format $NN,NNN,NNN.
			int salary = (int) value[index];
			title[index] = "$" + PApplet.nfc(salary);
			//System.err.println("***" + lines[i] + " : "+ index + " : " + title[index] +  " ::::" + i +"***");
		}
		//System.err.println("Done adding sal\n");
		update();
	}
	
	public int teamIndex(String teamCode){
		Integer index = (Integer) teamIndices.get(teamCode);
		return index.intValue();
	}

}
