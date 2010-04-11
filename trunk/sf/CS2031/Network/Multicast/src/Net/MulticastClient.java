package Net;
/*
Steven Diviney 08462267
Eoin McCarthy 08484333
Mark O'Meara 08332240
*/



import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import flow.MulticastClient.updateAck;
import Encryption.DesEncrypter;

public class MulticastClient {

	public static final String MCAST_ADDR = "230.0.0.1"; // hardcoded address for the multicast group
	public static final int MCAST_PORT = 9013; // hardcoded port number for the multicast group

	public static final int MAX_BUFFER = 1024; // maximum size for data in a packet      

	MulticastSocket socket;
	InetAddress address;
	int port;
	Interface iface;
	DesEncrypter encryptor;
	
	int awaitingack; //Amount of acks waiting for
	String prevmessage;
	int peersnum;
	int ackreceived;
	Thread ac; //Thread for continuously checking for acknowledgements
	


	public MulticastClient() {
		try {
			address = InetAddress.getByName(MCAST_ADDR);
			socket = new MulticastSocket(MCAST_PORT);
			socket.joinGroup(address);
			port = MCAST_PORT;
			register();
			encryptor = new DesEncrypter(3);
			
			awaitingack = 0;
			peersnum = 1; //Includes self
			ackreceived = 0;
			ac = new Thread(new updateAck());
			ac.start();
			
		}
		catch(Exception e) {
			System.err.println("No network connection found. Abort");
			e.printStackTrace();
			//System.exit(1);
		}
	}
	/**
	 * Register presence with all existing clients.
	 */
	public void register(){
		notify((byte) 1);

	}
	
	public void logoff(){
		notify((byte) 2);

	}
	
	public void acknowledge(){
		notify((byte)3);
	}
	
	/**
	 * Used to send information (Ack etc) to other client.
	 * 1 == Register with peers
	 * 2 == Inform peers of program termination.
	 * 3 = Ack
	 * @param code
	 */
	private void notify(byte code){
		byte[] buffer = {code};
		DatagramPacket packet = null;
		try{
			packet = new DatagramPacket(buffer, buffer.length, address, port);
			System.out.println(buffer[0]);
			socket.send(packet);
			System.out.println("Logoff packet sent");
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void send(String msg){
		//Make room for leading byte
		msg = encryptor.encrypt(msg);
		byte[] buffer = new byte[(msg.getBytes().length)+1];
		System.arraycopy(msg.getBytes(), 0, buffer, 1, buffer.length -1);
		buffer[0] = 0;
		DatagramPacket packet = null;
		try{
			packet = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(packet);
			System.out.println("Message sent");
			prevmessage = msg;
            awaitingack = peersnum;
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void setpeersnum(int num){
		this.peersnum = num;
	}
	
	
	/**
	 * Setup link between the client and the interface
	 * @param iface
	 */
	public void setIface(Interface iface){
		this.iface = iface;
	}
	
	/**
	 *  Separate process that looks for acknowledgements. 
	 * @author Mark
	 *
	 */
public class updateAck implements Runnable{
		
		public updateAck(){}
		
		public void run(){
			int loopswaiting = 0;
			while(true){
				//If acknowledgement(s) received.
				if(ackreceived > 0){
					System.out.println("Ack received = " + ackreceived);
					awaitingack = 0;//awaitingack - ackreceived;
					//ackreceived -= ackreceived;
					ackreceived = 0;
				}
				if(awaitingack > 0){
					loopswaiting++;
				}
				else{
					loopswaiting = 0;
				}
				/*if(ackreceived < 0){
					ackreceived = 0;
				}*/
				if(loopswaiting > 3){
					//Resend previous message if the program has waiting
					//more than 3 loops to receive all acks
					send(prevmessage);
					awaitingack = peersnum;
					ackreceived = 0;
				}
				//System.out.println("loop");
				System.out.flush();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}


