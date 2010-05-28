package ZipCodeMap;

import java.io.BufferedReader;
import java.io.IOException;

class Slurper implements Runnable {
	ZipMap zip;

	Slurper(ZipMap zip) {
		this.zip = zip;
		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		try {
			BufferedReader reader = zip.createReader("src/data/zips.tsv");
			// First get the info line.
			String line = reader.readLine();
			zip.parseInfo(line);
			zip.places = new Place[zip.totalCount];
			// Parse each of the rest of the lines.
			while ((line = reader.readLine()) != null) {
				zip.places[zip.placeCount] = zip.parsePlace(line);
				zip.placeCount++;
				if (zip.placeCount % 20 == 0) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
