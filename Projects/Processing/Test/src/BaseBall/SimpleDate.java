package BaseBall;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDate {

	String firstDateStamp = "20100401";
	String lastDateStamp = "20100524";
	String todayDateStamp;
	static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	// The number of days in the entire season.
	int dateCount;
	


	// The current date being shown.
	int dateIndex;
	
	// Don't show the first 10 days; they're too erratic.
	int minDateIndex = 10;
	// The last day of the season, or yesterday, if the season is ongoing.
	// This is the maximum date that can be viewed.
	int maxDateIndex;
	// This format makes "20070704" from the date July 4, 2007.
	DateFormat stampFormat = new SimpleDateFormat("yyyyMMdd");
	// This format makes "4 July 2007" from the same.
	DateFormat prettyFormat = new SimpleDateFormat("d MMMM yyyy");
	// All dates for the season formatted with stampFormat.
	String[] dateStamp;
	// All dates in the season formatted with prettyFormat.
	String[] datePretty;
	
	//PApplet p;
	
	public SimpleDate(){//(PApplet p){
	//	this.p = p;
		setupDates();
	}
	
	
	void setupDates( ) {
		
	  try {
	    Date firstDate = stampFormat.parse(firstDateStamp);
	    long firstDateMillis = firstDate.getTime( );
	    Date lastDate = stampFormat.parse(lastDateStamp);
	    long lastDateMillis = lastDate.getTime( );
	    // Calculate number of days by dividing the total milliseconds
	    // between the first and last dates by the number of milliseconds per day.
	    dateCount = (int)
	      ((lastDateMillis - firstDateMillis) / MILLIS_PER_DAY) + 1;
	    maxDateIndex = dateCount;
	    dateStamp = new String[dateCount];
	    datePretty = new String[dateCount];
	   // todayDateStamp = p.year() + p.nf(p.month( ), 2) + p.nf(p.day( ), 2);
	    // Another option method of doing the same thing using Java's APIs
	    Date today = new Date( );
	    String todayDateStamp = stampFormat.format(today);
	    for (int i = 0; i < dateCount; i++) {
	      Date date = new Date(firstDateMillis + MILLIS_PER_DAY*i);
	      datePretty[i] = prettyFormat.format(date);
	      dateStamp[i] = stampFormat.format(date);
	      // If this value for 'date' is today, set the previous
	      // day as the maximum viewable date, because it means the season is
	      // still ongoing. The previous day is used because unless it is late
	      // in the evening, the updated numbers for the day will be unavailable
	      // or incomplete.
	      if (dateStamp[i].equals(todayDateStamp)) {
	        maxDateIndex = i-1;
	      }
	    }
	  } catch (ParseException e) {
	    e.printStackTrace();
	  }
	}
	
	public int getDateCount() {
		return dateCount;
	}
	public int getDateIndex() {
		return dateIndex;
	}
	
	public void setDateIndex(int in){
		dateIndex = in;
	}

	public String[] getDatePretty() {
		return datePretty;
	}

	public int getMinDateIndex() {
		return minDateIndex;
	}

	public int getMaxDateIndex() {
		return maxDateIndex;
	}
	
	public String getDateStamp(int index){
		return dateStamp[index];
	}



}
