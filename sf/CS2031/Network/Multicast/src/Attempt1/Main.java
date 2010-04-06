package Attempt1;

import javax.swing.UIManager;

public class Main {
	static MulticastServer server;
	static MulticastClient client;
	//static Interface gui;


	public static void main(String Args[]){
		
		client = new MulticastClient();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception evt) {}
		Interface gui = new Interface(client);
		//gui.setVisible(false);			//Don't let the user do anything until the backend is set up
		server = new MulticastServer(gui);
		server.run();
		client.setIface(gui);
		gui.setVisible(true);
	}
}
