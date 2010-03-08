import java.util.Vector;

/**
 * The class provides a connection between two threads.
 * Frames can be transmitted through a channel from one
 * side to another. The delivery of a frame depends on 
 * the space available in a channel and the noise for
 * which the channel has been configured. 
 * 
 * @author sweber
 *
 */
public class Channel {

	public static int BUFFERSIZE= 10;  // number of simultaneous frames 
	
	Vector<byte[]> frames= null; 		// Buffer for frames
	int noise= 0;						// Noise applied to transmissions
	
	/**
	 * Empty constructor for channel
	 */
	Channel() {
		this(BUFFERSIZE);
	}
	
	/**
	 * Constructor that accepts the number of simultaneous frames.
	 * 
	 * @param count Number of simultaneous frames in the channel
	 */
	Channel(int count) {
		this(count, 0);
	}
	
	
	/**
	 * 
	 * Constructor that accepts the number of simultaneous frames
	 * and the noise applied to transmissions.
	 * 
	 * @param count Number of simultaneous frames in the channel
	 * @param noise Noise applied to transmissions
	 */
	Channel(int count, int noise) {
		frames= new Vector<byte[]>(BUFFERSIZE);
		this.noise= noise;
	}
	
	
	/**
	 * Sends a frame through the channel.
	 * 
	 * @param frame Frame to be transmitted
	 */
	public synchronized void send(byte[] frame) {
		if (frames.size()<BUFFERSIZE)
			if ((Math.random()*100)>noise) {    // if the signal is stronger than the noise
				frames.add(frame);              // transmit the frame
				notifyAll();
				try {wait(50);}catch(Exception e){e.printStackTrace();}
			}
			else {
				System.err.println("Frame lost due to noise");	// debug info
			}
	}
	
	
	/**
	 * Attempts to receive a frame from the channel.
	 * 
	 * @return A frame from the channel
	 */
	public synchronized byte[] receive() {
		byte[] frame= null;
		
		try {
			while (frames.size()<1) {wait(300);}	// check every 300 ms if a frame is available
		} catch(Exception e) {e.printStackTrace();}
		frame= frames.remove(0);					// take frame from channel and return it
		return frame;
	}
}