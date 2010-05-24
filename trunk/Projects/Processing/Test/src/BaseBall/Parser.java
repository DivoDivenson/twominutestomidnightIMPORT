package BaseBall;

import java.util.regex.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import processing.core.*;



public class Parser {

	PrintWriter standings;
	PrintWriter teams;
	FileOutputStream stand, team;
	
	String base;// = "http://mlb.mlb.com/components/game/year_2010/month_05/day_23/";
	
	/**
	 * No parameters because I'm lazy.
	 *   
	 */
	public Parser(PApplet p){
		base = "http://mlb.mlb.com/components/game/year_2010/month_05/day_23/";
		/*
		File f = new File("src");
		System.err.println(f.getAbsolutePath());
		System.err.println("***********************************************************");
		*/
		
		try {
			//Will fuckup if the file is not there already
			stand = new FileOutputStream("src/data/standings.tsv");
			team = new FileOutputStream("src/data/teams.tsv");
			standings = new PrintWriter(stand);
			teams = new PrintWriter(team);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		parseWinLoss(p.loadStrings(base + "standings_rs_ale.js"));
		parseWinLoss(p.loadStrings(base + "standings_rs_alw.js"));
		parseWinLoss(p.loadStrings(base + "standings_rs_alc.js"));
		parseWinLoss(p.loadStrings(base + "standings_rs_nle.js"));
		parseWinLoss(p.loadStrings(base + "standings_rs_nlw.js"));
		parseWinLoss(p.loadStrings(base + "standings_rs_nlc.js"));
		
		standings.close();
		teams.close();
		try {
			team.close();
			stand.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done parsing input");

		
	}
	
	public void parseWinLoss(String[] lines){
		Pattern p = Pattern.compile("\\s+([\\w\\d]+):\\s'(.*)',?");
		String teamCode = "", teamName = "";
		int wins = 0, losses = 0;
		
		for(int i = 0; i < lines.length; i++){
			Matcher m = p.matcher(lines[i]);
			
			if(m.matches()){
				String attr = m.group(1);
				String value = m.group(2);
				
				if(attr.equals("code")){
					teamCode = value;
				}else if (attr.equals("w")){
					wins = Integer.parseInt(value);
				}else if (attr.equals("l")){
					losses = Integer.parseInt(value);
				}else if (attr.equals("league_sensitive_team_name")){
					teamName = value;
				}
			}else{
				if(lines[i].startsWith("}")){
					standings.println(teamCode + '\t' + wins + '\t' + losses);
					teams.println(teamCode + '\t' + teamName);
				}
			}
		}
	}
	
}
