package docketSystem;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class NewDocForm extends JFrame {

	private javax.swing.JTextField description;
	private javax.swing.JTextField berth;
	private javax.swing.JLabel berthLabel;
	private javax.swing.JTextField collect;
	private javax.swing.JLabel collectLabel;
	private javax.swing.JLabel customerLabel;
	private javax.swing.JTextField customerRef;
	private javax.swing.JTextField deliver;
	private javax.swing.JLabel deliverLabel;
	private javax.swing.JLabel descripLabel;
	private javax.swing.JLabel equipLabel;
	private javax.swing.JButton cancel;
	private javax.swing.JButton save;
	private javax.swing.JButton print;
	private javax.swing.JTextField equip;
	private javax.swing.JTextField jTextField7;
	private javax.swing.JLabel payLabel;
	private javax.swing.JTextField payLen;
	private javax.swing.JTextField weight;
	private javax.swing.JLabel weightLabel;

	private Invoice invoice;

	public NewDocForm(Invoice invoice) {
		initComponents();
		this.invoice = invoice;
	}

	private void cancelActionPerformed(ActionEvent evt) {
		this.setVisible(false);
		this.dispose();

	}
	
	private void saveActionPerformed(ActionEvent evt){
		invoice.setBerth(berth.getText());
		System.out.println(invoice.getBerth());
	}

	/*
	 * Big ol' chunk copy n' pastad from NetBeans. Ye be warned
	 */
	private void initComponents() {

		jTextField7 = new javax.swing.JTextField();
		equipLabel = new javax.swing.JLabel();
		payLabel = new javax.swing.JLabel();
		collectLabel = new javax.swing.JLabel();
		deliverLabel = new javax.swing.JLabel();
		descripLabel = new javax.swing.JLabel();
		customerLabel = new javax.swing.JLabel();
		equip = new javax.swing.JTextField();
		payLen = new javax.swing.JTextField();
		collect = new javax.swing.JTextField();
		deliver = new javax.swing.JTextField();
		berthLabel = new javax.swing.JLabel();
		weightLabel = new javax.swing.JLabel();
		description = new javax.swing.JTextField();
		customerRef = new javax.swing.JTextField();
		berth = new javax.swing.JTextField();
		weight = new javax.swing.JTextField();
		cancel = new javax.swing.JButton();
		save = new javax.swing.JButton();
		print = new javax.swing.JButton();

		JComponent stuff[] = { jTextField7, equipLabel, payLabel, collectLabel,
				deliverLabel, descripLabel, customerLabel, equip, payLen,
				collect, deliver, berthLabel, weightLabel, description,
				customerRef, berth, weight, cancel, save, print };

		for (JComponent n : stuff) {
			n.setFont(new java.awt.Font("Dialog", 0, 12));
		}

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(520, 350));
		setResizable(false);

		equipLabel.setText("Equipment No.");

		payLabel.setText("Pay Len.");

		collectLabel.setText("Collect from");

		deliverLabel.setText("Deliver to");

		descripLabel.setText("Description");

		customerLabel.setText("Customer Ref");

		berthLabel.setText("Berth");

		weightLabel.setText("Weight");

		cancel.setText("Cancel");
		cancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		cancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelActionPerformed(evt);
			}
		});

		save.setText("Save");
		save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		save.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});

		print.setText("Print");
		print.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		print.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

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
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								customerLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								descripLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								deliverLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								collectLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								payLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								equipLabel,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								114,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								customerRef)
																						.addComponent(
																								description)
																						.addGroup(
																								layout
																										.createSequentialGroup()
																										.addGroup(
																												layout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING,
																																false)
																														.addComponent(
																																payLen)
																														.addComponent(
																																equip,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																123,
																																Short.MAX_VALUE))
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																										.addGroup(
																												layout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING,
																																false)
																														.addComponent(
																																weightLabel,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE)
																														.addComponent(
																																berthLabel,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																67,
																																Short.MAX_VALUE))
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addGroup(
																												layout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING)
																														.addComponent(
																																weight,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																139,
																																Short.MAX_VALUE)
																														.addComponent(
																																berth)))
																						.addComponent(
																								collect,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								359,
																								Short.MAX_VALUE)
																						.addComponent(
																								deliver)))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				cancel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				80,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(
																				15,
																				15,
																				15)
																		.addComponent(
																				save,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				80,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(
																				15,
																				15,
																				15)
																		.addComponent(
																				print,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				80,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																equip,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																equipLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																15,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																berthLabel)
														.addComponent(
																berth,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																payLen,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(payLabel)
														.addComponent(
																weightLabel)
														.addComponent(
																weight,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																collect,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																collectLabel))
										.addGap(18, 18, 18)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																deliver,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																deliverLabel))
										.addGap(18, 18, 18)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																description,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																descripLabel))
										.addGap(18, 18, 18)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																customerRef,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																customerLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																cancel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																90,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																save,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																90,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																print,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																90,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(45, Short.MAX_VALUE)));
		// pack();
	}
}
