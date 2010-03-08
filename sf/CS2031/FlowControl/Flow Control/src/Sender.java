import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;

/**
 * The class provides a Sender that attempts to transfer a message
 * to a receiver over a given channel.
 * 
 * @author sweber
 *
 */
public class Sender implements Runnable {

	public static int FRAMESIZE= 1000;			// size of frames the sender handles
	
	Channel in= null;							// channel for incoming information/frames
	Channel out= null;							// channel for outgoing information/frames
	byte[] msg= null;							// buffer for the message that is to be send
	byte order[] = new byte[1];					//Buffer to store the order/number of the frame being sent

	boolean resend;
	boolean ack;
	boolean sent;
	int index;
	byte[] buffer;
	//private Vector jobs= null; 		// Buffer for frames



	
	
	/**
	 * Constructor that accepts incoming and outgoing channels.
	 * 
	 * @param in	Channel for incoming information/frames
	 * @param out	Channel for outgoing information/frames
	 */
	Sender(Channel in, Channel out) {
		this(in, out, new byte[FRAMESIZE]);
	}
	

	/**
	 * Constructor that accepts incoming and outgoing channels.
	 * 
	 * @param in	Channel for incoming information/frames
	 * @param out	Channel for outgoing information/frames
	 * @param msg	Message that is to be send
	 */
	Sender(Channel in, Channel out, byte[] msg) {
		this.in= in;
		this.out= out;
		this.msg= msg;
		ack = true;
		order[0] = 0;
		index = 0;

	}
	
	public void setOrder(){
		if(order[0] == 0){
			order[0] = 1;
		}else{
			order[0] = 0;
		}
	}
	
	
	/**
	 * Thread that attempts to send the message through the 
	 * outgoing channel.
	 */
	public class sendingThread implements Runnable {
		
		byte temp[];
		public synchronized void run() {
			byte[] buffer;
			byte[] pframe = null;
			int index= 0;
			mark:
			while(index<msg.length){
				System.out.println("Sending");
				while(!ack){
					for(int x = 0; x <= 10;x++){
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Send again");
						out.send(pframe);
						continue mark;
					}
				}
				
				if((index+FRAMESIZE)>= msg.length){
					buffer = new byte[msg.length-index];
				}else{
					buffer = new byte[FRAMESIZE];
				}
				System.arraycopy(msg, index, buffer, 0, buffer.length);
				ack = false;
				out.send(buffer);
				pframe = buffer;
				index += buffer.length;
			}
			
				
		}
		
		public void send(){
			if(index<msg.length){
				if ((index+FRAMESIZE)>=msg.length) {		// send either a full frame or the
					buffer= new byte[msg.length-index];		// remained
				}
				else
					buffer= new byte[FRAMESIZE];
				
				System.out.println("Sender: "+index);	// debug info
				System.arraycopy(msg, index, buffer, 0, buffer.length);
				out.send(buffer);
				index+= buffer.length;
				temp = in.receive();
				/*notify();
				//index+= buffer.length;
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				System.out.println("Frame sent succesfully");
				
			}
		}
	}
	
	
	/**
	 * Thread that attempts to receive information through the 
	 * incoming channel.
	 */
	public class receivingThread implements Runnable {
		public synchronized  void run() {
			while(true){
				System.out.println("Sender recieving");
				byte[] temp = in.receive();
				if(temp != null){
					System.out.println("Acknowledged");
					ack = true;
				}
				
			}
			//System.out.println("Hello");
		}
	}	
	
	
	/**
	 * Main method of the Sender thread that starts the sending threat
	 * and the receiving threat.
	 */
	public void run() {
		//PipedOutputStream os = new PipedOutputStream();
		//PipedInputStream is = new PipedInputStream();
		
		Thread sender = new Thread(new sendingThread());
		//Thread receiver = new Thread(new receivingThread());
		sender.start();
		//receiver.start();
		
		 
		/*synchronized(receiver){
			try {
				receiver.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		
	}	
}



