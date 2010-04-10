package flow;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {

	public static final String MCAST_ADDR = "230.0.0.1"; // hardcoded address for the multicast group
	public static final int MCAST_PORT = 9013; // hardcoded port number for the multicast group

	public static final int MAX_BUFFER = 1024; // maximum size for data in a packet      

	MulticastSocket socket;
	InetAddress address;
	int port;
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
			awaitingack = 0;
			peersnum = 1; //Includes self
			ackreceived = 0;
			ac = new Thread(new updateAck());
			ac.start();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	
	public void send(String msg){
		byte[] buffer;
		DatagramPacket packet = null;
		try{
			packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
			System.out.println(msg.getBytes()[1]);
			socket.send(packet);
			System.out.println("Message sent");
			prevmessage = msg;
			awaitingack = peersnum;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class updateAck implements Runnable{
		
		public updateAck(){}
		
		public void run(){
			int loopswaiting = 0;
			while(true){
				if(ackreceived > 0){
					System.out.println("Ack received = " + ackreceived);
					awaitingack = awaitingack - ackreceived;
					ackreceived = 0;
				}
				if(awaitingack > 0){
					loopswaiting++;
				}
				else{
					loopswaiting = 0;
				}
				if(loopswaiting > 3){
					//Resend previous message if the program has waiting
					//more than 3 loops to receive all acks
					send(prevmessage);
					awaitingack = peersnum;
					ackreceived = 0;
				}
				System.out.println("loop");
				System.out.flush();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}