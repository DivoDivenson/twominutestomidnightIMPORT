package BaseBall;

import java.util.Iterator;
import java.util.Vector;
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
	FileOutputStream stand, team, winloss;
	PApplet p;
	Vector<String> teamCodeVec;

	String base;

	/**
	 * No parameters because I'm lazy.
	 * 
	 */
	public Parser(PApplet p) {
		this.p = p;
		base = "http://mlb.mlb.com/components/game" + "/year_"
				+ p.nf(p.year(), 4) + "/month_" + p.nf(p.month(), 2) + "/day_"
				+ p.nf(p.day(), 2) + "/";

		File f = new File("src");
		System.err.println(f.getAbsolutePath());
		System.err
				.println("***********************************************************");

		teamCodeVec = new Vector();
		/*
		 * System.out.println(acquireStandings(2010, 5, 24));
		 * 
		 * 
		 * parseMlb(); //Get team codes/wins/loses
		 * 
		 * grabLogos("small", "http://mlb.mlb.com/mlb/images/team_logos/logo_",
		 * "_small.gif");
		 * 
		 * grabLogos("50x50", "http://mlb.mlb.com/mlb/images/team_logos/50x50/",
		 * ".gif");
		 * 
		 * grabLogos("79x76", "http://mlb.mlb.com/mlb/images/team_logos/logo_",
		 * "_79x76.jpg");
		 * 
		 * grabLogos("standings",
		 * "http://mlb.mlb.com/mlb/images/team_logos/51x21/",
		 * "_standings_logo.gif");
		 * 
		 * grabLogos("100x100",
		 * "http://mlb.mlb.com/mlb/images/team_logos/100x100/", ".gif");
		 */

	}

	String[] acquireStandings(String stamp) {
		int year = Integer.parseInt(stamp.substring(0, 4));
		int month = Integer.parseInt(stamp.substring(4, 6));
		int day = Integer.parseInt(stamp.substring(6, 8));
		return acquireStandings(year, month, day);
	}

	String[] acquireStandings(int year, int month, int day) {
		String filename = year + p.nf(month, 2) + p.nf(day, 2) + ".tsv";
		String path = "src/data/standingsData/" + filename;
		File file = new File(path);
		System.err.println("Standings" + file.getAbsolutePath());
		if (!file.exists() || (file.length() == 0)) {
			System.out.println("Downloading standings file " + filename);
			PrintWriter writer;

			try {
				/*try {
					//Some sort of shitty hack to try and get it to open the fucking file
					 //I think the problem is with eclipse?
					winloss = new FileOutputStream(file);
					writer = new PrintWriter(winloss);
					writer.flush();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				winloss = new FileOutputStream(file);
				writer = new PrintWriter(winloss);

				String base = "http://mlb.mlb.com/components/game" + "/year_"
						+ year + "/month_" + p.nf(month, 2) + "/day_"
						+ p.nf(day, 2) + "/";
				// American League (AL)
				parseStandings(base + "standings_rs_ale.js", writer);
				parseStandings(base + "standings_rs_alc.js", writer);
				parseStandings(base + "standings_rs_alw.js", writer);
				// National League (NL)
				parseStandings(base + "standings_rs_nle.js", writer);
				parseStandings(base + "standings_rs_nlc.js", writer);
				parseStandings(base + "standings_rs_nlw.js", writer);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				System.err.println("SHIT FUCKED UP HERE");
				e.printStackTrace();
			}

		} else {
			System.out.println("File exists");
		}
		return p.loadStrings(file);
	}

	void parseStandings(String filename, PrintWriter writer) {
		String[] lines = p.loadStrings(filename);
		Pattern p = Pattern.compile("\\s+([\\w\\d]+):\\s'(.*)',?");
		String teamCode = "";
		int wins = 0;
		int losses = 0;
		for (int i = 0; i < lines.length; i++) {
			Matcher m = p.matcher(lines[i]);
			if (m.matches()) {
				String attr = m.group(1);
				String value = m.group(2);
				if (attr.equals("code")) {
					teamCode = value;
				} else if (attr.equals("w")) {
					wins = this.p.parseInt(value);
				} else if (attr.equals("l")) {
					losses = this.p.parseInt(value);
				}
			} else {
				if (lines[i].startsWith("}")) {
					// This is the end of a group, write these values
					writer.println(teamCode + this.p.TAB + wins + this.p.TAB
							+ losses);
				}
			}
		}
	}

	public void parseMlb() {
		// teamVec = new Vector();
		try {
			// Will fuckup if the file is not there already
			stand = new FileOutputStream("src/data/standings.tsv");
			team = new FileOutputStream("src/data/teams.tsv");
			System.err.println("Files opened");
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

	public void grabLogos(String folder, String prefix, String suffix) {
		String extension = suffix.substring(suffix.length() - 4);
		Iterator it = teamCodeVec.iterator();

		while (it.hasNext()) {
			String temp = (String) it.next();
			String filename = "src/data/" + folder + "/" + temp + extension;
			String url = prefix + temp + suffix;
			System.out.println(filename + " : " + url);
			p.saveStream(filename, url);
		}

	}

	public void parseWinLoss(String[] lines) {
		Pattern p = Pattern.compile("\\s+([\\w\\d]+):\\s'(.*)',?");
		String teamCode = "", teamName = "";
		int wins = 0, losses = 0;

		for (int i = 0; i < lines.length; i++) {
			Matcher m = p.matcher(lines[i]);

			if (m.matches()) {
				String attr = m.group(1);
				String value = m.group(2);

				if (attr.equals("code")) {
					teamCode = value;
					teamCodeVec.add(value);
					System.out.println(value);
				} else if (attr.equals("w")) {
					wins = Integer.parseInt(value);
				} else if (attr.equals("l")) {
					losses = Integer.parseInt(value);
				} else if (attr.equals("league_sensitive_team_name")) {
					teamName = value;
				}
			} else {
				if (lines[i].startsWith("}")) {
					standings.println(teamCode + '\t' + wins + '\t' + losses);
					teams.println(teamCode + '\t' + teamName);
				}
			}
		}
	}

}
