package BaseBall;
import java.util.HashMap;

import processing.core.PApplet;
public class StandingsList extends RankedList{

	HashMap teamIndices;
	SalaryList salary;
	public StandingsList(String[] lines, int teamCount, HashMap teamIndices, SalaryList salary){
		super(teamCount, false);
		this.salary = salary;
		this.teamIndices = teamIndices;
		for(int i = 0; i < teamCount; i++){
			String[] peices = PApplet.split(lines[i], PApplet.TAB);
			int index = teamIndex(peices[0]);
			int wins = PApplet.parseInt(peices[1]);
			int losses = PApplet.parseInt(peices[2]);
			value[index] = (float) wins/ (float) (wins+losses);
			title[index] = wins + "\u2013" + losses;	
		}
		
		update();
		
		
		
	}
	
	public int teamIndex(String teamCode){
		Integer index = (Integer) teamIndices.get(teamCode);
		return index.intValue();
	}
	
	public float compare(int a, int b){
		float amt = super.compare(a, b);
		if(amt != 0) return amt;
		return salary.compare(a, b);
	}
	
}
