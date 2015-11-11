import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Vector;

public class DatabaseConnection {
	static Connection conn;


	public static void connect(String databaseName, String user, String password) {

		try {
			String urlPrefix = "jdbc:db2://localhost:50000/";
			String url = urlPrefix + databaseName;
			Class.forName("com.ibm.db2.jcc.DB2Driver");

			System.out.println("**** Loaded the JDBC driver");
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
