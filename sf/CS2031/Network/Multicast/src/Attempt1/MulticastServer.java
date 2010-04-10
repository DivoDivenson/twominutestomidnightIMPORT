package Attempt1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Vector;
import Encryption.DesEncrypter;

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
	Vector<InetAddress> users;		//List of all the other users connected
	DesEncrypter decryptor;


	public MulticastServer(Interface iface){
		users = new Vector<InetAddress>();
		this.iface = iface;
		decryptor = new DesEncrypter(3);
		
		try{
			address = InetAddress.getByName(MCAST_ADDR);
			socket = new MulticastSocket(MCAST_PORT);
			socket.joinGroup(address);

		}catch(Exception e){
			System.err.println("No network connection found. Abort");
			e.printStackTrace();
			//System.exit(1);
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
				if(buffer != null){
					//If data packet
					if(buffer[0] == 0){
						System.out.println("Data received");
						msg =new String(buffer, 1, packet.getLength()-1);
						msg = decryptor.decrypt(msg);
						msg = new String(String.format("%s%s%s", packet.getAddress().toString(), ": ", msg));
						iface.update(msg);
						//If register packet
					}else if(buffer[0] == 1){
						System.out.println("Register" + packet.getAddress());
						//If user not already on registered.
						if(!(users.contains(packet.getAddress()))){
							users.add(packet.getAddress());
							System.out.println("Addeding new client");
							//Update to gui
							iface.updateList(users);
							//Reply with own register packet.
							Thread.sleep(2000); //HACK. Give the other client a chance to get online. Have to do this because the
							//client (and thus the register packet) is sent before the server is set up and has a chance to respond. Bad design.
							iface.register();
						}
						//If logoff packet received
					}else if(buffer[0] == 2){
						if(users.contains(packet.getAddress())){
							users.remove(packet.getAddress());
							System.out.println("Removing client");
							iface.updateList(users);
						}
					}else{
						;//msg = new String(String.format("%s%s%s", packet.getAddress(), " : ", (new String(buffer, 0, packet.getLength()))));
						//iface.update(msg);
					}

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}




}
