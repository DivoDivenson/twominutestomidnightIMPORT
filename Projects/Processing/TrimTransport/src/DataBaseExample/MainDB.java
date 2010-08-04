package DataBaseExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
 

public class MainDB {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://localhost/books";

	public static void main(String Args[]) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DATABASE_URL, "trim",
					"truck");
			statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT authorID, firstName, lastName FROM authors");
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfCols = metaData.getColumnCount();
			for(int i = 1; i <= numberOfCols; i++){
				System.out.printf("%-8s\t", metaData.getColumnName(i));
			}
			System.out.println();
			
			while(resultSet.next()){
				for(int i = 1; i<=numberOfCols;i++){
					System.out.printf("%-8s\t", resultSet.getObject(i));
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			statement.close();
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
