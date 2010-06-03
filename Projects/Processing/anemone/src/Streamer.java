import java.io.*;
import java.util.ArrayList;


public class Streamer implements Runnable {
  static final int MAX_LIST_SIZE = 1024;
  ArrayList<Record> list = new ArrayList<Record>();
  
  BufferedReader reader;  
  Thread thread;

  
  public Streamer(BufferedReader reader) {
    this.reader = reader;

    thread = new Thread(this);
    thread.start();
  }


  public void run() {
    try {
      while (Thread.currentThread() == thread) {
        // Continue reading records until reached the max list size
        // (prevents from reading too much and getting out of control)
        while (list.size() < MAX_LIST_SIZE) {
          String line = reader.readLine();
          Record record = new Record(line);
          // If the record loaded properly
          if (record.ip != null) {
            // Add to the list in a thread-safe manner
            synchronized (list) {
              list.add(record);
            }
          } else {
            System.err.println("Could not parse " + line);
          }
        }
      }
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) { }
    } catch (IOException e) {
      System.err.println("Could not read from log file");
      e.printStackTrace();
    }
  }
  

  // Get the next record from this list
  public Record nextRecord() {
    synchronized (list) {
      if (list.size() == 0) {
        return null;
      }
      Record r = list.remove(0);
      return r;
    }
  }
}