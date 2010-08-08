package docketSystem;

/*
 * 
 * ****************************
 * INVOICE = DOCKET in my own crazy mind
 * **************************** 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jopendocument.model.OpenDocument;
import org.jopendocument.panel.ODSViewerPanel;
import org.jopendocument.print.DefaultDocumentPrinter;

/**
 * This appears to have become the main class where everything is handled. It's
 * (original) purpose was to draw the main window of the application. Now it
 * ~Stores and manages the connection to the mySQL database ~Launches the
 * NewDocForm window ~Passes the newly created Invoice from NewDocForm. It then
 * opens the finished document in a print window ~Writes the newly created
 * Invoice to the database
 * 
 * @author divo
 * 
 */
public class Interface extends JFrame {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://localhost/trimtransport";
	static final String USERNAME = "trim";
	static final String PASSWORD = "truck";
	static final String DEFAULT_QUERY = "SELECT docket.Docket_Number, docket.Date_, addresses.address, addresses1.address,"
			+ " docket.Description, docket.Seal, docket.Customer, docket.Equipment, docket.Return_Empty, haz.Name, haz.UN_Number, docket.Size_, docket.Weight" +
					" FROM docket LEFT JOIN addresses ON docket.Deliver_to=addresses.ID LEFT" +
					" JOIN addresses AS addresses1 ON docket.Collect_From=addresses1.ID" +
					" LEFT JOIN haz ON docket.Haz=haz.ID" +
					" ORDER BY Docket_Number;";

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

	private ResultSetTableModel tableModel;
	private JScrollPane tableScroll;

	private Connection connection;

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
		tableScroll = new JScrollPane();

		// JTable for database of doc
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DATABASE_URL, USERNAME,
					PASSWORD);
			tableModel = new ResultSetTableModel(JDBC_DRIVER, connection,
					DEFAULT_QUERY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTable resultTable = new JTable(tableModel);

		tableScroll.setViewportView(resultTable);
		// End jtable

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
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												tableScroll,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												402, Short.MAX_VALUE)
										.addContainerGap()));
		mainLayout
				.setVerticalGroup(mainLayout
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
																tableScroll,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																249,
																Short.MAX_VALUE)
														.addGroup(
																mainLayout
																		.createSequentialGroup()
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
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
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
				750, 500);
		setFonts();
	}

	/**
	 * Insert an invoice object into the sql database.
	 */
	public int saveActionPreformed() {
		// First check to see if the addresses are already present
		System.out.println("Saved action");
		int collectID, deliverID, hazID = 0;

		collectID = insertAddress(invoice.getFrom());
		deliverID = insertAddress(invoice.getTo());
		if (invoice instanceof InvoiceHaz) {
			hazID = insertHaz();
		}

		return insertDocket(collectID, deliverID, hazID);

		// String insert = new String("INSERT INTO docket ")

	}

	public int insertDocket(int collectID, int deliverID, int hazID) {
		Statement statement;
		ResultSet resultSet;
		try {
			statement = connection.createStatement();
			statement
					.executeUpdate(new String(
							"INSERT INTO docket (Equipment, Customer, Seal, Description, Berth, Weight, Size_, Return_Empty, Deliver_to, Collect_from, Haz, Date_) values ('"
									+ invoice.getEqupNo()
									+ "', '"
									+ invoice.getCustomerRefer()
									+ "', '"
									+ invoice.getSeal()
									+ "', '"
									+ invoice.getDescript()
									+ "', '"
									+ invoice.getBerth()
									+ "', '"
									+ invoice.getWeight()
									+ "', '"
									+ invoice.getSize()
									+ "', '"
									+ invoice.getReturnEmpty()
									+ "', '"
									+ deliverID
									+ "', '"
									+ collectID
									+ "', '"
									+ hazID
									+ "', '"
									+ invoice.getTime()
									+ "');"));

			resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * The same as insertAddress. Checks if something matching the haz spec is
	 * in the database. If it is, use that, else write the new one in
	 * 
	 * @return SQL ID of haz entry
	 */
	public int insertHaz() {
		int result = 0;
		ResultSet resultSet;
		Statement statement;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(new String(
					"SELECT * FROM haz WHERE Name = '"
							+ ((InvoiceHaz) invoice).getName()
							+ "' AND UN_Number = '"
							+ ((InvoiceHaz) invoice).getUnNo()
							+ "' AND Primary_Class = '"
							+ ((InvoiceHaz) invoice).getClass1() + "'"));
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			} else {
				statement
						.executeUpdate(new String(
								"INSERT INTO haz (Name, UN_Number, Packing_Group, Primary_Class, Secondary_Class, Tunnel_code) values ('"
										+ ((InvoiceHaz) invoice).getName()
										+ "', '"
										+ ((InvoiceHaz) invoice).getUnNo()
										+ "', '"
										+ ((InvoiceHaz) invoice).getPg()
										+ "', '"
										+ ((InvoiceHaz) invoice).getClass1()
										+ "', '"
										+ ((InvoiceHaz) invoice).getClass2()
										+ "', '"
										+ ((InvoiceHaz) invoice).getTunnel()
										+ "');"));
				resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
				resultSet.next();
				result = resultSet.getInt(1);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public int insertAddress(String address) {
		ResultSet resultSet;
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			// Check the collect from address
			resultSet = statement.executeQuery(new String(
					"SELECT * FROM addresses WHERE Address = \"" + address
							+ "\""));
			// Presume the first object found is the address, as each is unique
			if (resultSet.next()) {
				// If found, store its id
				result = resultSet.getInt(1);
			}// Else insert the new address into addresses
			else {
				// Insert into address
				statement.executeUpdate(new String(
						"INSERT INTO addresses (Address) values ('" + address
								+ "');"));
				// Getting the number of rows would probably be sufficent to
				// determine the new ID, but an entry might get
				// deleted...maybe...prehaps not, anyway, get the ID.
				resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
				resultSet.next();
				result = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public void setFonts() {
		stuff = new JComponent[] { newDoc, searchButton, closeButton, file,
				edit };
		for (JComponent n : stuff) {
			n.setFont(new java.awt.Font("Dialog", 0, 11));
		}
	}

	private int getMaxID() {
		ResultSet resultSet;
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT max(Docket_Number) FROM docket");
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
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

	private void newDocActionPerformed(java.awt.event.ActionEvent evt) {

		// SET THE DOC NUMBER IF IT IS USED
		// newInvoice = new Invoice();
		if (newDocForm != null)
			newDocForm = null; // Just in case
		newDocForm = new NewDocForm(this);
		newDocForm.setVisible(true);

	}

	private void searchButttonActionPerformed() {
		;
	}

	private void closeButtonActionPerformed() {
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Set the invoice object and return it's ID. (Messy, but needed by
	 * NewDocForm / Document to render to the invoice)
	 * 
	 * @param invoice
	 * @return ID of invoice in SQL database, also used as the invoice number
	 */
	public int setInvoice(Invoice invoice) {
		this.invoice = invoice;
		invoice.setDocNo(getMaxID() + 1); // Set the docket number as the
		// previous most recent invoice
		return saveActionPreformed();
	}

	public Invoice getInvoice() {
		return invoice;
	}

	/*
	 * private void initMenu() { menu = new JMenuBar(); file = new JMenu();
	 * file.setText("File"); menu.add(file); setJMenuBar(menu); }
	 */

}
