package Rating;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ibm.db2.jcc.am.ResultSet;

public class DatabaseManager {
	static String username = "db2admin";		// You put your username here
	static String password = "1234567890";		// You put your password here
	static String jdbcClassName = "com.ibm.db2.jcc.DB2Driver";
	static String url = "jdbc:db2://localhost:50000/meddb";		// Adjust your database's name here
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
	
	public static ArrayList<ArticleInfo> query(String query, String[] queriedColumns)
	{
		ArrayList<ArticleInfo> res = new ArrayList<ArticleInfo>();
		try
		{
			
			
			
			PreparedStatement ps_search = conn.prepareStatement(query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			conn.commit();
	
			while (rs.next())
			{
				ArticleInfo ar = new ArticleInfo();
				for (int i = 0; i < queriedColumns.length; i++)
				{
					String val = rs.getString(queriedColumns[i]);
					ar.addValue(queriedColumns[i], val);
				}
				res.add(ar);
			}
		} catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return res;
	}
}
