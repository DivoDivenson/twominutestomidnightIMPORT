package docketSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class NewDocForm extends JFrame {

	CheckListener checkListener;
	protected javax.swing.JTextField berth;
	private javax.swing.JLabel berthLabel;
	private javax.swing.JButton cancelButton;
	protected javax.swing.JTextArea collect;
	private javax.swing.JLabel collectLabel;
//	private javax.swing.JList collectList;
	private AddressList collectList;
	protected javax.swing.JTextField customer;
	private javax.swing.JLabel customerLabel;
	private javax.swing.JLabel decriptionLabel;
	protected javax.swing.JTextArea deliver;
	private javax.swing.JLabel deliverLabel;
//	private javax.swing.JList deliverList;
	private AddressList deliverList;
	protected javax.swing.JTextField description;
	protected javax.swing.JTextField equip;
	private javax.swing.JLabel equipLabel;
	protected javax.swing.JCheckBox hazCheckBox;
	private javax.swing.JPanel hazPanel;
	//private javax.swing.JList jList2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane5;
	protected javax.swing.JTextField name; //UN name
	private javax.swing.JLabel nameLabel;
	private javax.swing.JLabel packLabel;
	private javax.swing.JComboBox packing;
	private javax.swing.JComboBox primary;
	private javax.swing.JLabel primaryLabel;
	private javax.swing.JButton printButton;
	protected javax.swing.JTextField returnEmpty;
	private javax.swing.JLabel returnEmptyLabel;
	private javax.swing.JButton saveButton;
	protected javax.swing.JTextField seal;
	private javax.swing.JLabel sealLabel;
	private javax.swing.JComboBox secondary;
	private javax.swing.JLabel secondaryLabel;
	protected javax.swing.JTextField size;
	private javax.swing.JLabel sizeLabel;
	private javax.swing.JPanel standardPanel;
	private javax.swing.JTextField tunnel;
	private javax.swing.JLabel tunnelLabel;
	protected javax.swing.JTextField un;
	private javax.swing.JLabel unLabel;
	protected javax.swing.JTextField weight;
	private javax.swing.JLabel weightLabel;
	
	protected boolean save;
	

	JComponent stuff[];
	JComponent haz[];

	private Invoice invoice;
	protected Interface interfaceRef; // Reference to the main interface

	public NewDocForm(Interface interfaceRef) {
		this.interfaceRef = interfaceRef;
		save = false;

		initComponents();
		// invoice = new Invoice();
		// this.invoice = interfaceRef.getInvoice();
	}
	
	public NewDocForm(){
		;
	}

	protected void hazCheckBoxActionPerformed() {
		if (haz[0].isVisible()) { // Check to see if the haz stuff is on or off
			for (JComponent n : haz) {
				n.setVisible(false);
				setSize(new java.awt.Dimension(545, 590));

			}
		} else {
			for (JComponent n : haz) {
				n.setVisible(true);
				setSize(new java.awt.Dimension(545, 660));
			}
		}
	}

	private void initInvoice() {

		if (hazCheckBox.isSelected()) {
			InvoiceHaz invoicehaz = new InvoiceHaz();
			invoicehaz.setName(name.getText());
			invoicehaz.setClass1((String) primary.getSelectedItem());
			invoicehaz.setClass2((String) secondary.getSelectedItem());
			invoicehaz.setPg((String) packing.getSelectedItem());
			invoicehaz.setTunnel(tunnel.getText());
			invoicehaz.setUnNo(un.getText());
			invoice = invoicehaz;
		} else {
			invoice = new Invoice();
		}
		invoice.setBerth(berth.getText());
		invoice.setCustomerRefer(customer.getText());
		invoice.setDescript(description.getText());
		invoice.setEqupNo(equip.getText());
		invoice.setFrom(collect.getText());
		invoice.setSeal(seal.getText());
		invoice.setSize(size.getText());
		invoice.setTo(deliver.getText());
		invoice.setWeight(weight.getText());
		invoice.setReturnEmpty(returnEmpty.getText());

	}

	private void saveButtonActionPerformed() {
		save = true;
		initInvoice();		
		interfaceRef.setInvoice(invoice);
		deliverList.initList();
		collectList.initList();
	}

	private void printButtonActionPerformed() {
		save = true;
		initInvoice();
		interfaceRef.setInvoice(invoice);
		interfaceRef.printActionPreformed(invoice);
		deliverList.initList();
		collectList.initList();
	}

	protected void cancelButtonActionPerformed() {
		if(save){
			interfaceRef.saveActionPreformed();
		}
		this.setVisible(false);
		this.dispose();
	}

	/*
	 * private void cancelActionPerformed(ActionEvent evt) {
	 * this.setVisible(false); this.dispose();
	 * 
	 * }
	 * 
	 * private void setInvoice() { invoice.setBerth(berth.getText());
	 * invoice.setCustomerRefer(customerRef.getText()); invoice.setDate(new
	 * Date()); invoice.setDescript(description.getText()); //
	 * invoice.setDocNo(docNo) invoice.setEqupNo(equip.getText());
	 * invoice.setFrom(collect.getText()); invoice.setSize(payLen.getText());
	 * invoice.setTo(deliver.getText()); invoice.setWeight(weight.getText()); }
	 * 
	 * private void saveActionPerformed(ActionEvent evt) { setInvoice();
	 * interfaceRef.setInvoice(invoice); }
	 * 
	 * private void printActionPreformed(ActionEvent evt) { setInvoice();
	 * interfaceRef.setInvoice(invoice);
	 * interfaceRef.printActionPreformed(invoice); }
	 */

	/*
	 * Big ol' chunk copy n' pastad from NetBeans. Ye be warned
	 */

	private void initComponents() {

		jScrollPane4 = new javax.swing.JScrollPane();
		//jList2 = new javax.swing.JList();
		standardPanel = new javax.swing.JPanel();
		equipLabel = new javax.swing.JLabel();
		equip = new javax.swing.JTextField();
		berthLabel = new javax.swing.JLabel();
		berth = new javax.swing.JTextField();
		customerLabel = new javax.swing.JLabel();
		customer = new javax.swing.JTextField();
		weightLabel = new javax.swing.JLabel();
		weight = new javax.swing.JTextField();
		sealLabel = new javax.swing.JLabel();
		seal = new javax.swing.JTextField();
		sizeLabel = new javax.swing.JLabel();
		size = new javax.swing.JTextField();
		decriptionLabel = new javax.swing.JLabel(); // Yeah yeah, it's
													// misspelled
		deliverLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		deliver = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		deliverList = new AddressList(interfaceRef.getConnection() );
		collectLabel = new javax.swing.JLabel();
		jScrollPane3 = new javax.swing.JScrollPane();
		collect = new javax.swing.JTextArea();
		jScrollPane5 = new javax.swing.JScrollPane();
		collectList = new AddressList(interfaceRef.getConnection());
		description = new javax.swing.JTextField();
		hazPanel = new javax.swing.JPanel();
		hazCheckBox = new javax.swing.JCheckBox();
		primaryLabel = new javax.swing.JLabel();
		packLabel = new javax.swing.JLabel();
		nameLabel = new javax.swing.JLabel();
		name = new javax.swing.JTextField();
		un = new javax.swing.JTextField();
		primary = new javax.swing.JComboBox();
		packing = new javax.swing.JComboBox();
		tunnel = new javax.swing.JTextField();
		secondary = new javax.swing.JComboBox();
		unLabel = new javax.swing.JLabel();
		secondaryLabel = new javax.swing.JLabel();
		tunnelLabel = new javax.swing.JLabel();
		printButton = new javax.swing.JButton();
		saveButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
        returnEmpty = new javax.swing.JTextField();
        returnEmptyLabel = new javax.swing.JLabel();
        
		stuff = new JComponent[] { equipLabel, equip, berthLabel, berth,
				customerLabel, customer, weightLabel, weight, sealLabel, seal,
				sizeLabel, size, collectLabel, collect, collectList,
				decriptionLabel, deliverLabel, deliver, deliverList,
				hazCheckBox, unLabel, primaryLabel, secondaryLabel, packLabel,
				tunnelLabel, nameLabel, name, un, primary, packing, tunnel,
				secondary, printButton, saveButton, cancelButton, returnEmpty, returnEmptyLabel };

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		this.setLocation(screenSize.width / 2, (screenSize.height) / 2);
		haz = new JComponent[] { unLabel, primaryLabel, secondaryLabel,
				packLabel, tunnelLabel, nameLabel, name, un, primary, packing,
				tunnel, secondary };

		for (JComponent n : stuff) {
			n.setFont(new java.awt.Font("Dialog", 0, 11));
		}

		for (JComponent n : haz) {
			n.setVisible(false);
		}

		/*jList2.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4",
					"Item 5" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		//jScrollPane4.setViewportView(jList2);*/

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent e){
				cancelButtonActionPerformed();
				
			}
		});
		setSize(new java.awt.Dimension(545, 590));
		setResizable(false);

		standardPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(""));

		equipLabel.setText("Equipment No.");

		berthLabel.setText("Berth");

		customerLabel.setText("Customer Ref");

		weightLabel.setText("Weight");

		sealLabel.setText("Seal No.");

		sizeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
		sizeLabel.setText("Size");

		decriptionLabel.setText("Description");

		deliverLabel.setText("Deliver To");

		deliver.setColumns(13);
		deliver.setRows(5);
		deliver.setWrapStyleWord(true);
		deliver.setLineWrap(true);
		jScrollPane1.setViewportView(deliver);
		
		deliverList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deliver.setText(deliverList.getSelectedValue(deliverList.getSelectedIndex()).fullString()); //Ugh. It sets the text, look away.
            }
        });
		collectList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	collect.setText(collectList.getSelectedValue(collectList.getSelectedIndex()).fullString()); //Ugh. It sets the text, look away.
            }
        });

		/*deliverList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4",
					"Item 5" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});*/
		jScrollPane2.setViewportView(deliverList);

		collectLabel.setText("Collect From");

		collect.setColumns(13);
		collect.setRows(5);
		collect.setWrapStyleWord(true);
		collect.setLineWrap(true);
		jScrollPane3.setViewportView(collect);

		/*collectList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4",
					"Item 5" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});*/
		jScrollPane5.setViewportView(collectList);

		returnEmptyLabel.setText("Return Empty");
		// returnEmpty.setAlignmentY(0.0F);

		javax.swing.GroupLayout standardPanelLayout = new javax.swing.GroupLayout(
				standardPanel);
		standardPanel.setLayout(standardPanelLayout);
		standardPanelLayout
				.setHorizontalGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			            .addGroup(standardPanelLayout.createSequentialGroup()
			                    .addContainerGap()
			                    .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, standardPanelLayout.createSequentialGroup()
			                            .addComponent(deliverLabel)
			                            .addGap(69, 69, 69)
			                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
			                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
			                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
			                        .addGroup(standardPanelLayout.createSequentialGroup()
			                            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
			                                .addComponent(returnEmptyLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                .addComponent(decriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                .addComponent(sealLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                .addComponent(customerLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                .addComponent(equipLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
			                                .addComponent(collectLabel, javax.swing.GroupLayout.Alignment.LEADING))
			                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			                                .addGroup(standardPanelLayout.createSequentialGroup()
			                                    .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
			                                        .addComponent(seal, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
			                                        .addComponent(customer, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
			                                        .addComponent(equip, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
			                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                                    .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
			                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, standardPanelLayout.createSequentialGroup()
			                                            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
			                                                .addComponent(sizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                                .addComponent(weightLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			                                                .addComponent(berthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
			                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			                                            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			                                                .addComponent(size, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
			                                                .addComponent(weight, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
			                                                .addComponent(berth, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)))
			                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
			                                .addComponent(description, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
			                                .addComponent(returnEmpty, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))))
			                    .addContainerGap())
			            );
		 standardPanelLayout.setVerticalGroup(
		            standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(standardPanelLayout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(equipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
		                    .addComponent(equip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                    .addComponent(berthLabel)
		                    .addComponent(berth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(12, 12, 12)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(customerLabel)
		                    .addComponent(customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                    .addComponent(weightLabel)
		                    .addComponent(weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(12, 12, 12)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(sealLabel)
		                    .addComponent(seal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                    .addComponent(sizeLabel)
		                    .addComponent(size, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(decriptionLabel)
		                    .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(returnEmptyLabel)
		                    .addComponent(returnEmpty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		                .addGap(19, 19, 19)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
		                    .addComponent(deliverLabel, javax.swing.GroupLayout.Alignment.LEADING)
		                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(standardPanelLayout.createSequentialGroup()
		                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
		                        .addContainerGap())
		                    .addGroup(standardPanelLayout.createSequentialGroup()
		                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
		                        .addContainerGap())
		                    .addGroup(standardPanelLayout.createSequentialGroup()
		                        .addComponent(collectLabel)
		                        .addGap(62, 62, 62))))
		        );

		hazPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

		hazCheckBox.setText("Hazardous");
		checkListener = new CheckListener(){
			public void actionPerformed(ActionEvent evt) {
				//System.out.println(evt.getActionCommand());
				hazCheckBoxActionPerformed();
			}	
			
			public void simulateAction(){
				hazCheckBox.setSelected(true);
				hazCheckBoxActionPerformed();
			}
		};
		hazCheckBox.addActionListener(checkListener);
		primaryLabel.setText("Primary Class");

		packLabel.setText("Packing Group");

		nameLabel.setText("Name");

		primary.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"1", "2", "3", "4.1", "4.2", "4.3", "5.1", "5.2", "6.1", "6.2",
				"7", "8", "9" }));

		packing.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"III", "II", "I" }));

		secondary.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"None", "1", "2", "3", "4.1", "4.2", "4.3", "5.1", "5.2",
				"6.1", "6.2", "7", "8", "9" }));

		unLabel.setText("UN Number");

		secondaryLabel.setText("Secondary Hazard");

		tunnelLabel.setText("Tunnel Code");

		javax.swing.GroupLayout hazPanelLayout = new javax.swing.GroupLayout(
				hazPanel);
		hazPanel.setLayout(hazPanelLayout);
		hazPanelLayout
				.setHorizontalGroup(hazPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								hazPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												hazCheckBox,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												96,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(37, 37, 37)
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																hazPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				nameLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				name))
														.addGroup(
																hazPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				hazPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								primaryLabel)
																						.addComponent(
																								packLabel))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				hazPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								packing,
																								0,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								primary,
																								0,
																								54,
																								Short.MAX_VALUE))))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																tunnelLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																unLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																secondaryLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																un,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																54,
																Short.MAX_VALUE)
														.addComponent(
																secondary, 0,
																54,
																Short.MAX_VALUE)
														.addComponent(
																tunnel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																67,
																Short.MAX_VALUE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		hazPanelLayout
				.setVerticalGroup(hazPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								hazPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																hazCheckBox)
														.addComponent(nameLabel)
														.addComponent(
																name,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(unLabel)
														.addComponent(
																un,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																primaryLabel)
														.addComponent(
																primary,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																secondaryLabel)
														.addComponent(
																secondary,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(11, 11, 11)
										.addGroup(
												hazPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(packLabel)
														.addComponent(
																packing,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																tunnel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																tunnelLabel))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		printButton.setText("Print");
		printButton.setIcon(new ImageIcon("data/resources/print.png"));
		printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				printButtonActionPerformed();
			}
		});

		saveButton.setText("Save");
		saveButton.setIcon(new ImageIcon("data/resources/save.png"));
		saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveButtonActionPerformed();
			}
		});

		cancelButton.setText("Close");
		cancelButton.setIcon(new ImageIcon("data/resources/close.png"));
		cancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelButtonActionPerformed();
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				cancelButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				77,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				saveButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				77,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				printButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				77,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																layout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				false)
																		.addComponent(
																				standardPanel,
																				javax.swing.GroupLayout.Alignment.LEADING,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				hazPanel,
																				javax.swing.GroupLayout.Alignment.LEADING,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												standardPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												hazPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																printButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																77,
																Short.MAX_VALUE)
														.addComponent(
																saveButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																77,
																Short.MAX_VALUE)
														.addComponent(
																cancelButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																77,
																Short.MAX_VALUE))
										.addContainerGap()));
		// pack();
	}
}
