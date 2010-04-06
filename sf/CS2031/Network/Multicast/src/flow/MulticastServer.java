package flow;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Vector;

/**
 * 
 * Receives messages
 */
public class MulticastServer implements Runnable {

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
				if(buffer[0] == 0){
					System.out.println(packet.getAddress());
				}else{
					msg = new String(String.format("%s%s%s", packet.getAddress(), " : ", (new String(buffer, 0, packet.getLength()))));
				}
				iface.update(msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Maintains a list of all the clients on the network
	 */
	class PeerList implements Runnable{
		Vector<AddressEntry> peers;

		public void add(InetAddress address){
			if(peers.contains(address)){
				;
			}else{
				peers.add(new AddressEntry(address));
			}
		}
		/**
		 * Loop every time interval and see if there are any old peers that need to be flushed.
		 */
		public void run(){

		}
	}

	/**
	 * 
	 * An InetAddress with a timestamp.
	 *
	 */
	class AddressEntry{
		private InetAddress address;
		private long creationTime;

		public AddressEntry(InetAddress address){
			this.address = address;
			creationTime = System.currentTimeMillis();
		}
		
		public void refresh(){
			creationTime = System.currentTimeMillis();
		}

		public boolean equals(AddressEntry obj){
			return this.address.equals(obj.getAddress());
		}

		public InetAddress getAddress(){
			return this.address;
		}
	}


}
