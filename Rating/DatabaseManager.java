package Rating;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import com.ibm.db2.jcc.am.ResultSet;

public class DatabaseManager {
	static String username = "db2admin";		// You put your username here
	static String password = "1";		// You put your password here
	static String jdbcClassName = "com.ibm.db2.jcc.DB2Driver";
	static String url = "jdbc:db2://localhost:50000/mydb";		// Adjust your database's name here
	static Connection conn = null;
	
	public static void connectToDatabase()
	{
		try
		{
			Class.forName(jdbcClassName);
			conn = DriverManager.getConnection(url, username, password);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void disconnectToDatabase()
	{
		try
		{
			if (conn != null)
				conn.close();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ResultSet query(String query)
	{
		ResultSet rs = null;
		try
		{
			//PreparedStatement ps_search = conn.prepareStatement(query);
			Statement stmt = conn.createStatement();
			stmt.executeQuery(query);
			rs = (ResultSet) stmt.executeQuery(query);
			//conn.commit();
	
			
		} catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return rs;
	}
	
	public static void update(String query)
	{
		try
		{
			//PreparedStatement ps_search = conn.prepareStatement(query);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			
		}
	}
	
	public static void deleteRecord(int articleID)
	{
		// Delete in Keyword table
		String kw_sql = "DELETE FROM KEYWORD9 WHERE ARTICLE_ID = "+articleID;
		
		// Delete in Author table
		String au_sql = "DELETE FROM AUTHOR9 WHERE ARTICLE_ID = "+articleID;
		
		// Delete in Article table
		String ar_sql = "DELETE FROM ARTICLE9 WHERE ARTICLE_ID = "+articleID;
		
		try
		{
			//PreparedStatement ps_search = conn.prepareStatement(query);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(kw_sql);
			stmt.executeUpdate(au_sql);
			stmt.executeUpdate(ar_sql);
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			
		}
	}
}
