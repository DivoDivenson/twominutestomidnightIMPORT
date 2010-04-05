package Attempt1;

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


	public MulticastClient() {
		try {
			address = InetAddress.getByName(MCAST_ADDR);
			socket = new MulticastSocket(MCAST_PORT);
			socket.joinGroup(address);
			port = MCAST_PORT;
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
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
