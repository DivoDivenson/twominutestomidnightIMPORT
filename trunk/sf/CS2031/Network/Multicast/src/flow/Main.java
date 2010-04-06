package flow;

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
		gui.setVisible(true);
		server = new MulticastServer(gui);
		server.run();
	}
}
