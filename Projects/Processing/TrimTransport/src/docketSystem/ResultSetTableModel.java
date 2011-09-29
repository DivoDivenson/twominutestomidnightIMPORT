package docketSystem;

// Fig. 25.28: ResultSetTableModel.java
// A TableModel that supplies ResultSet data to a JTable.
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).

public class ResultSetTableModel extends AbstractTableModel {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	// keep track of database connection status
	private boolean connectedToDatabase = false;

	// constructor initializes resultSet and obtains its meta data object;
	// determines number of rows
	public ResultSetTableModel(String driver, Connection connection,
			String query) throws SQLException, ClassNotFoundException {
		// load database driver class
		// Class.forName( driver );

		// connect to database
		this.connection = connection;

		// create Statement to query database
		statement = connection.createStatement(
				//ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

		// update database connection status
		connectedToDatabase = true;
		System.out.println("Connected");

		// set query and execute it
		setQuery(query);
	} // end constructor ResultSetTableModel

	// get class that represents column type
	public Class getColumnClass(int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine Java class of column
		try {
			String className = metaData.getColumnClassName(column + 1);

			// return Class object that represents className
			// return Class.forName( className );
			return String.class;
		} // end try
		catch (Exception exception) {
			exception.printStackTrace();
		} // end catch

		return Object.class; // if problems occur above, assume type Object
	} // end method getColumnClass

	// get number of columns in ResultSet
	public int getColumnCount() throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine number of columns
		try {
			return metaData.getColumnCount();
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return 0; // if problems occur above, return 0 for number of columns
	} // end method getColumnCount

	// get name of a particular column in ResultSet
	public String getColumnName(int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine column name
		try {

			//Hack to display Deliver to / Collect from instead of address
			if(column == 2){
				return new String ("Deliver to");
			}else if(column ==3){
				return new String ("Collect from");
			}
			return metaData.getColumnName(column + 1).replaceAll("_", " ");
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return ""; // if problems, return empty string for column name
	} // end method getColumnName

	// return number of rows in ResultSet
	public int getRowCount() throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		return numberOfRows;
	} // end method getRowCount

	// obtain value in particular row and column
	public Object getValueAt(int row, int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// obtain a value at specified ResultSet row and column
		try {
			
			resultSet.absolute(row + 1);
			// System.out.println("Called");
			/*
			 * (Ugly) Hack to get the date's rendering as dates, not longs
			 */
			if (column + 1 == 2) {
				Date date = new Date((long) resultSet.getLong(column + 1));
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				return new String(format.format(date));

			}
			//</Ugly hack 2>
			return resultSet.getObject(column + 1);
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return ""; // if problems, return empty string object
	} // end method getValueAt

	// set new database query string
	public void setQuery(String query) throws SQLException,
			IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// specify query and execute it
		resultSet = statement.executeQuery(query);

		// obtain meta data for ResultSet
		metaData = resultSet.getMetaData();

		// determine number of rows in ResultSet
		//resultSet.last(); // move to last row
		//numberOfRows = resultSet.getRow(); // get row number

		// notify JTable that model has changed
		fireTableStructureChanged();
	} // end method setQuery

	// close Statement and Connection
	public void disconnectFromDatabase() {
		if (!connectedToDatabase)
			return;

		// close Statement and Connection
		try {
			statement.close();
			connection.close();
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch
		finally // update database connection status
		{
			connectedToDatabase = false;
		} // end finally
	} // end method disconnectFromDatabase
} // end class ResultSetTableModel


