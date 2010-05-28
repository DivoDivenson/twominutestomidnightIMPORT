package ZipCodeMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import processing.core.*;

public class ParseMapData extends PApplet {
	// Indices for each of the columns
	int ZIP_CODE = 0;
	int LATITUDE = 1;
	int LONGITUDE = 2;
	int ZIP_CLASS = 3;
	int PONAME = 4;
	int STATE_FIPS = 5;
	int COUNTY = 6;

	// USGS uses standard parallels at 45.5 ̊N and 29.5 ̊N
	// with a central meridian value of 96 ̊W.
	// Latitude value is phi, longitude is lambda.
	;

	public void setup() {
		// Load the state FIPS codes into a table.
		Hashtable fipsTable = new Hashtable();
		String[] fipsLines = loadStrings("src/data/fips.tsv");
		for (int i = 0; i < fipsLines.length; i++) {
			// Split each line on the tab characters.
			String[] pieces = split(fipsLines[i], TAB);
			// The FIPS code is in column 1,
			// and the state abbreviation in column 2
			// (keep in mind that columns are numbered from zero).
			fipsTable.put(pieces[1], pieces[2]);
		}

		String[] lines = loadStrings("src/data/zipnov99.csv");
		// Set the minimum and maximum values arbitrarily large.
		float minX = 1;
		float maxX = -1;
		float minY = 1;
		float maxY = -1;
		
		// Set up an array for the cleaned data.
		String[] cleaned = new String[lines.length];
		// Number of cleaned entries found
		int placeCount = 0;

		// Start at row 1, because the first row is the column titles.
		for (int row = 1; row < lines.length; row++) {
		  // Split the row into pieces on each comma.
		  String[] data = split(lines[row], ',');
		  scrubQuotes(data);
		  // Remove extra whitespace on either side of each column.
		  data = trim(data);
		  
		  String stateAbbrev = (String) fipsTable.get(data[STATE_FIPS]);
		// If the abbreviation was not found, skip this line,
		// because that means it's an outlying territory.
		if (stateAbbrev == null) continue;
		// For now, also skip Alaska and Hawaii.
		if (stateAbbrev.equals("AK") || stateAbbrev.equals("HI")) continue;
		
		// Attempt to fix the capitalization of the city/town name.
		String placeName = fixCapitals(data[PONAME]) + ", " + stateAbbrev;
		
		  float lat = Float.parseFloat(data[LATITUDE]);
		  float lon = Float.parseFloat(data[LONGITUDE]);
		
		  // Albers equal-area conic projection.
		  // USGS uses standard parallels at 45.5 ̊N and 29.5 ̊N
		  // with a central meridian value of 96 ̊W.
		  // Latitude value is phi, longitude is lambda.
		  float phi0 = 0;
		  float lambda0 = radians(-96);
		  float phi1 = radians(29.5f);
		  float phi2 = radians(45.5f);
		  float phi = radians(lat);
		  float lambda = radians(lon);
		  float n = 0.5f * (sin(phi1) + sin(phi2));
		  float theta = n * (lambda - lambda0); //radians(lon - lambda0);
		  float c = sq(cos(phi1)) + 2*n*sin(phi1);
		  float rho = sqrt(c - 2*n*sin(phi)) / n;
		  float rho0 = sqrt(c - 2*n*sin(phi0)) / n;
		  float x = rho * sin(theta);
		  float y = rho0 - rho*cos(theta);
		  
		  if (x > maxX) maxX = x;
		  if (x < minX) minX = x;
		  if (y > maxY) maxY = y;
		  if (y < minY) minY = y;
		  // Add a cleaned version of the line, separated by tabs, to the list.
		  cleaned[placeCount++] = data[ZIP_CODE] + "\t" + x + "\t" + y + "\t" + placeName;
		}
		
		// Write to a file called "zips.tsv" in the sketch folder.
		FileOutputStream zips;
		try {
			zips = new FileOutputStream("src/data/zips.tsv");
			PrintWriter tsv = new PrintWriter(zips);
			System.out.println("File open");
			// Use the first line to specify the number of data points in the file,
			// along with the minimum and maximum latitude and longitude coordinates.
			// Use a # to mark the line as different from the other data.
			tsv.println("# " + placeCount +
			            "," + minX + "," + maxX + "," + minY + "," + maxY);
			  // Write each line of the cleaned data.
			  for (int i = 0; i < placeCount; i++) {
			    tsv.println(cleaned[i]);
			  }
			  // Flush and close the file buffer.
			  zips.flush();
			  zips.close();
			  tsv.flush( );
			  tsv.close( );
			  // Finished; quit the program.
			  println("Finished.");
			  exit( );
			

		}catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("File not open");
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		






		  

	}

	// Capitalize the first letter of each word in a string.
	String fixCapitals(String title) {
		char[] text = title.toCharArray();
		// If set to true, the next letter will be capitalized.
		boolean capitalizeNext = true;
		for (int i = 0; i < text.length; i++) {
			if (Character.isSpace(text[i])) {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				text[i] = Character.toUpperCase(text[i]);
				capitalizeNext = false;
			} else {
				text[i] = Character.toLowerCase(text[i]);
			}
		}
		return new String(text);
	}

	// Parse quotes from CSV data. Quotes around a column are common,
	// and actual double quotes (") are specified by two double quotes ("").
	void scrubQuotes(String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].length() > 2) {
				// Remove quotes at start and end, if present.
				if (array[i].startsWith("\"") && array[i].endsWith("\"")) {
					array[i] = array[i].substring(1, array[i].length() - 1);
				}
			}
			// Make double quotes into single quotes.
			array[i] = array[i].replaceAll("\"\"", "\"");
		}
	}

}
