package docketSystem;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.jopendocument.model.OpenDocument;
import org.jopendocument.panel.ODSViewerPanel;
import org.jopendocument.print.DefaultDocumentPrinter;

public class Interface extends JFrame {

	private JPanel main;

	private JMenuBar menu;

	private JButton newDoc;

	private Invoice invoice;
	private NewDocForm newDocForm;

	private Document document;
	private JComponent stuff[];


	private javax.swing.JButton closeButton;
	private javax.swing.JMenu file;
	private javax.swing.JMenu edit;
	private javax.swing.JMenuBar jMenuBar1;

	private javax.swing.JButton searchButton;

	public Interface() {
		initComponents();
	}

	private void initComponents() {

		
		main = new javax.swing.JPanel();
		newDoc = new javax.swing.JButton();
		searchButton = new javax.swing.JButton();
		closeButton = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		file = new javax.swing.JMenu();
		edit = new javax.swing.JMenu();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		newDoc.setText("New");
		newDoc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newDocActionPerformed(evt);
			}
		});

		searchButton.setText("Find");
		searchButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchButttonActionPerformed();
			}
		});

		closeButton.setText("Close");
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeButtonActionPerformed();
			}
		});

		javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
		main.setLayout(mainLayout);
		mainLayout
				.setHorizontalGroup(mainLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																newDoc,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																66,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																searchButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																66,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																closeButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																66,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(426, Short.MAX_VALUE)));
		mainLayout
				.setVerticalGroup(mainLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												newDoc,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												68,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												searchButton,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												68,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												closeButton,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												68,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(33, Short.MAX_VALUE)));

		file.setText("File");
		jMenuBar1.add(file);

		edit.setText("Edit");
		jMenuBar1.add(edit);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(main,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(main,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 514) / 2, (screenSize.height - 322) / 2,
				514, 322);
		setFonts();
	}

	public void saveActionPreformed() {
		;
	}
	
	public void setFonts(){
		stuff = new JComponent[]{newDoc, searchButton, closeButton, file, edit};
		for(JComponent n : stuff){
			n.setFont(new java.awt.Font("Dialog", 0, 11));
		}
	}

	public void printActionPreformed(Invoice invoice) {
		document = new Document(invoice);
		final OpenDocument doc = new OpenDocument();
		doc.loadFrom("src/data/invoice1.ods");

		// Show time !
		final JFrame mainFrame = new JFrame("Viewer");
		DefaultDocumentPrinter printer = new DefaultDocumentPrinter();

		ODSViewerPanel viewerPanel = new ODSViewerPanel(doc, printer, true);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		mainFrame.setContentPane(viewerPanel);
		mainFrame.pack();
		mainFrame.setLocation(screenSize.width / 2, (screenSize.height) / 2);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setVisible(true);
	}

	/*
	 * private void initPanel() { newDoc = new JButton("new");
	 * newDoc.addActionListener(new java.awt.event.ActionListener() { public
	 * void actionPerformed(java.awt.event.ActionEvent evt) {
	 * newDocActionPerformed(evt); } });
	 * 
	 * // I'm using netbeans to create GUI code, so it's gonna be shit....
	 * javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
	 * main.setLayout(mainLayout);
	 * mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(
	 * javax.swing.GroupLayout.Alignment.LEADING).addGroup(
	 * mainLayout.createSequentialGroup().addContainerGap()
	 * .addComponent(newDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 66,
	 * javax.swing.GroupLayout.PREFERRED_SIZE) .addContainerGap(322,
	 * Short.MAX_VALUE)));
	 * mainLayout.setVerticalGroup(mainLayout.createParallelGroup(
	 * javax.swing.GroupLayout.Alignment.LEADING).addGroup(
	 * mainLayout.createSequentialGroup().addContainerGap()
	 * .addComponent(newDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 68,
	 * javax.swing.GroupLayout.PREFERRED_SIZE) .addContainerGap(201,
	 * Short.MAX_VALUE)));
	 * 
	 * }
	 */

	private void newDocActionPerformed(java.awt.event.ActionEvent evt) {

		// SET THE DOC NUMBER IF IT IS USED
		// newInvoice = new Invoice();
		if (newDocForm != null)
			newDocForm = null; // Just in case
		newDocForm = new NewDocForm(this);
		newDocForm.setVisible(true);

	}
	
	private void searchButttonActionPerformed(){
		;
	}
	
	private void closeButtonActionPerformed(){
		this.setVisible(false);
		this.dispose();
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
		saveActionPreformed();
		// printActionPreformed(this.invoice);
	}

	public Invoice getInvoice() {
		return invoice;
	}

	/*
	 * private void initMenu() { menu = new JMenuBar(); file = new JMenu();
	 * file.setText("File"); menu.add(file); setJMenuBar(menu); }
	 */

}
