package docketSystem;

import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JList;

/**
 * A JList that queries an SQL database for contents
 * 
 * @author divo
 * 
 */
public class AddressList extends JList {

	String addressQuery = "SELECT Address from addresses ORDER BY weight DESC;";
	Connection connection;
	Statement statement;
	ResultSet resultSet;
	Vector addressStrings; // JUST USE STRINGS

	public AddressList(Connection connection) {
		super();
		this.connection = connection;
		initList();
		//setLayoutOrientation(JList.VERTICAL_WRAP);

	}

	public AddressString getSelectedValue(int i) {
		if (i < addressStrings.size()) {
			return (AddressString) addressStrings.elementAt(i);
		}
		return new AddressString("");
	}

	public void initList() {
		try {
			addressStrings = new Vector<AddressString>();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(addressQuery);
			System.out.println("Printing addresses");
			
			while (resultSet.next()) {
				if(resultSet.getString(1).compareTo("") !=0){ //Easiest way to keep the blank address from being displayed, 
															  //but will become expensive as the number of addresses gets big
					addressStrings.add(new AddressString(resultSet.getString(1)));
				}
			}
			setListData(addressStrings);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
