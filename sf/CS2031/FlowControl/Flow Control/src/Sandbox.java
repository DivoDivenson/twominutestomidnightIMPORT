/**
 * The class provides an environment for a sender and
 * a receiver to run in.
 * 
 * @author sweber
 *
 */
public class Sandbox {

	/**
	 * Main method that creates a buffer, fills the buffer with content, 
	 * creates 2 channels to connect a sender and a receiver and then
	 * creates a Sender and a Receiver thread.
	 *  
	 * @param args Arguments from the command-line to the main method
	 */
	public static void main(String[] args) {
		Thread r,s;
		
		System.out.println("Start of Sandbox");

		try {
			byte[] buffer= new byte[7500];			// fill message with random data
			for(int i=0; i<buffer.length; i++) {buffer[i]= (byte) (i % 255); }
			buffer[buffer.length-1]= (byte) 255;
			buffer[buffer.length-2]= (byte) 255;
			
			Channel c1 = new Channel(10);
			Channel c2 = new Channel();
			
			r= new Thread(new Receiver(c1, c2), "Receiver");;
			s= new Thread(new Sender(c2, c1, buffer), "Sender");
		
			r.start();
			s.start();
		}
		catch(Exception e) {e.printStackTrace();}
		
		System.out.println("End of Sandbox");
	}	
}