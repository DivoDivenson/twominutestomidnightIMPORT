package Attempt1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 
 * Receives messages
 */
public class MulticastServer {

	public static final String MCAST_ADDR = "230.0.0.1";
	public static final int MCAST_PORT = 9013;
	public static final int MAX_BUFFER = 1024;

	MulticastSocket socket;
	InetAddress address;
	Interface iface;

	public MulticastServer(Interface iface){
		this.iface = iface;
		try{
			address = InetAddress.getByName(MCAST_ADDR);
			socket = new MulticastSocket(MCAST_PORT);
			socket.joinGroup(address);

		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run(){
		DatagramPacket packet = null;
		byte[] buffer = null;
		String msg = null;
				
		try{
			while(true){
				System.out.println("Server waiting");
				buffer = new byte[MAX_BUFFER];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				msg = new String(String.format("%s%s%s", packet.getAddress(), " : ", (new String(buffer, 0, packet.getLength()))));
				
				iface.update(msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
