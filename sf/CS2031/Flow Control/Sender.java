package work;


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
	boolean ack;
	byte[] order = new byte[10];
	boolean sent;

	
	
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
		this.ack = false;
		order[0] = 0;
	}
	
	void nextOrder(){
		if(order[0] == 1){
			order[0] = 0;
		}else{
			order[0] = 1;
		}
	}
	/**
	 * Thread that attempts to send the message through the 
	 * outgoing channel.
	 */
	public class sendingThread implements Runnable {
		public void run() {
			byte[] buffer;
			int index= 0;
			int count = 0;
			ack = true; //set true to send the first frame
			//label1:
			while(index<msg.length) {	
				System.out.println("Ack reset");
				ack = false;
															// depending on the remaining data 
				if ((index+FRAMESIZE)>=msg.length) {		// send either a full frame or the
					buffer= new byte[msg.length-index];		// remained
				}
				else
					buffer= new byte[FRAMESIZE];
				
				System.out.println("Sender: "+index);	// debug info
				System.arraycopy(msg, index, buffer, 0, buffer.length);
				out.send(buffer);
				
				
				sent = true; //presume it sent correctly
				count = 0;	 //
				while(!ack){ //Wait ~1000ms for an acknowledgement (Such a long time so as you can actually _see_ it
					System.out.println(count);
					if(count==10){
						System.out.println("Resend frame " + index);
						sent = false;
						break;
					}
					count++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(sent){
					System.out.println("\nSend next frame\n");
					index += buffer.length;
				}
			}			
		}
	}
	
	
	/**
	 * Thread that attempts to receive information through the 
	 * incoming channel.
	 */
	public class receivingThread implements Runnable {
		public void run() {
			byte[] temp;
			while(true){
				temp = in.receive();
				if(temp[0] == order[0]){ //Packet acknowledged
					System.out.println("Ack set");
					ack = true;
					//sent = true;
					nextOrder();
				}
			}
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