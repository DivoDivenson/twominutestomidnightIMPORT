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
	Interface iface;


	public MulticastClient() {
		try {
			address = InetAddress.getByName(MCAST_ADDR);
			socket = new MulticastSocket(MCAST_PORT);
			socket.joinGroup(address);
			port = MCAST_PORT;
			register();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	/**
	 * Register presence with all existing clients.
	 */
	public void register(){
		byte[] buffer = {1};
		DatagramPacket packet = null;
		try{
			packet = new DatagramPacket(buffer, buffer.length, address, port);
			System.out.println(buffer[0]);
			socket.send(packet);
			System.out.println("Register packet sent");
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void logoff(){
		byte[] buffer = {2};
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
		byte[] buffer;
		DatagramPacket packet = null;
		try{
			packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
			System.out.println(msg.getBytes());
			socket.send(packet);
			System.out.println("Message sent");
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * Setup link between the client and the interface
	 * @param iface
	 */
	public void setIface(Interface iface){
		this.iface = iface;
	}

}
