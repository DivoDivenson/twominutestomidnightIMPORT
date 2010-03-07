/**
 * The class provides a Receiver that attempts to accept a message
 * from a sender over a given channel.
 * 
 * @author sweber
 *
 */
public class Receiver implements Runnable {

	Channel in= null;							// channel for incoming information/frames
	Channel out= null;							// channel for outgoing information/frames
	byte buffer[]= null;						// buffer for the message that is to be receiver
	byte order[] = new byte[1];					//Buffer to store the order/number of the frame being sent
	int offset;

	
	/**
	 * Constructor that accepts incoming and outgoing channels.
	 * 
	 * @param in	Channel for incoming information/frames
	 * @param out	Channel for outgoing information/frames
	 */
	Receiver(Channel in, Channel out) {
		this(in, out, 10000);
	}
	
	
	/**
	 * Constructor that accepts incoming and outgoing channels
	 * and the length of the buffer that should be reserved.
	 * 
	 * @param in	Channel for incoming information/frames
	 * @param out	Channel for outgoing information/frames
	 * @param length Length of the buffer that should be reserved
	 */
	Receiver(Channel in, Channel out, int length) {
		this.in= in;
		this.out= out;
		buffer= new byte[length];
		order[0] = 0;
		offset = 0;
	}

	
	/**
	 * Thread that attempts to send the message through the 
	 * outgoing channel.
	 */
	public class sendingThread implements Runnable {
		public void run() {
		}
	}

	public void setOrder(){
		if(order[0] == 0){
			order[0] = 1;
		}else{
			order[0] = 0;
		}
	}
	
	/**
	 * Thread that attempts to receive information through the 
	 * incoming channel.
	 */
	public class receivingThread implements Runnable {
		public void run() {
			byte[] frame= null;
			int offset= 0;
			boolean complete= false;
			
			while(!complete) {
				frame= in.receive();
				if (frame!=null) {
					System.out.println("Receiver: "+offset+":"+frame.length); // debug info
					System.arraycopy(frame, 0, buffer, offset, frame.length);
					offset+= frame.length;					
					if ((frame[frame.length-1]== (byte)255) && (frame[frame.length-2]==(byte)255)) complete= true;
				}
			}
			System.out.println("Receive complete");
		}			
	}	

	
	/**
	 * Main method of the Sender thread that starts the sending threat
	 * and the receiving threat.
	 */
	public void run() {
		(new Thread(new receivingThread())).start();	
		(new Thread(new sendingThread())).start();	
	}
}




