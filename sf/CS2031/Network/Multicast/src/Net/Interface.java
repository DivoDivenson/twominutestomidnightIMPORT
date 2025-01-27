package Net;

/*
Steven Diviney 08462267
Eoin McCarthy 08484333
Mark O'Meara 08332240
*/


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/*
 * Interface.java
 *
 * Created on 04 April 2010, 21:47
 */



public class Interface extends javax.swing.JFrame {
	MulticastServer server;
	MulticastClient client;

	private AppendingTextPane receiver_pane;
	private JScrollPane scrollpane;
	private javax.swing.JButton send_button;
	private javax.swing.JTextField sender_input;
	private AppendingTextPane peer_list;;
	private JScrollPane scrollPeers;


	/** Creates new form Interface */
	public Interface(MulticastClient client) {
		this.server = server;
		this.client = client;
		initComponents();
	}

	

	/**
	 * Basic GUI stuff. 
	 */
	private void initComponents() {

		receiver_pane = new AppendingTextPane();
		sender_input = new javax.swing.JTextField();
		send_button = new javax.swing.JButton();
		peer_list = new AppendingTextPane();
		peer_list.setFocusable(false);
		scrollPeers = new JScrollPane();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		receiver_pane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		receiver_pane.setFocusable(false);
		receiver_pane.setFont(new java.awt.Font("Dialog", 0, 14));
		receiver_pane.setFocusable(false);

		//getContentPane().add(new JScrollPane(receiver_pane));
		scrollpane = new JScrollPane();
		scrollpane.setBounds(10,10,380,230);
		scrollpane.setViewportView(receiver_pane);
		scrollpane.setVisible(true);
		getContentPane().add(scrollpane);
		setVisible(true);


		scrollPeers.setViewportView(peer_list);
		scrollPeers.setBounds(400, 10, 170, 160);
		add(scrollPeers);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				closeWindow();
			}
		});





		getContentPane().add(sender_input);
		sender_input.setBounds(10, 257, 290, 30);

		send_button.setText("Send");
		send_button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				send_buttonActionPerformed(evt);
			}
		});
		getContentPane().add(send_button);
		send_button.setBounds(310, 253, 80, 40);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setResizable( false );

		setBounds((screenSize.width-586)/2, (screenSize.height-330)/2, 586, 330);
		//Have program add itself to own list of peers.
		Vector<InetAddress> temp = new Vector<InetAddress>();
		try {
			temp.add(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateList(temp);

	}// </editor-fold>



	private void send_buttonActionPerformed(java.awt.event.ActionEvent evt) {
		//System.out.println(sender_input.getText());
		//receiver_pane.appendText(sender_input.getText() + "\n");
		if(client.awaitingack == 0){ // Only if there are no acknowledgements pending, will the msg send
			client.send(sender_input.getText());
			sender_input.setText("");
		}
		//client.send(sender_input.getText());
		//sender_input.setText("");

	}

	public void update(String text){
		receiver_pane.appendText(text + "\n");
	}

	public void updateList(Vector<InetAddress> addressInput){
		addressInput.trimToSize();
		peer_list.setText("");	//Easiest just to clear the list first
		for(int i = 0; i < addressInput.size(); i++){
			peer_list.appendText(addressInput.elementAt(i).toString() + "\n");
		}
		client.setpeersnum(addressInput.size()-1); //Tell client how many peers are connected
		System.out.println("Should not be set in GUI");

	}

	/**
	 * Allow MulticastServer to tell the MulticatClient to send a registration packet. Silly doing it this way, but thats just how the object
	 * relations turned out.
	 */
	public void register(){
		client.register();
	}
	
	

	public void closeWindow(){
		client.logoff();
	}


}
