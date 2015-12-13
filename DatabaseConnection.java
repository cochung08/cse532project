import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Vector;

public class DatabaseConnection {
	static Connection conn;

	static Statement allStatement;

	public static void connect() {

		String databaseName = "MYDB";
		String user = "db2admin"; // You put your username here
		String password = "1";

		try {
			String urlPrefix = "jdbc:db2://localhost:50000/";
			String url = urlPrefix + databaseName;
			Class.forName("com.ibm.db2.jcc.DB2Driver");

			// System.out.println("**** Loaded the JDBC driver");
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(true);

			allStatement = DatabaseConnection.conn.createStatement();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
