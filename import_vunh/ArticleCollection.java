package import_vunh;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import import_john.*;


public class ArticleCollection {
	private ArrayList<Article> articles;
	
	private String jdbcClassName = "com.ibm.db2.jcc.DB2Driver";
	private String url = "jdbc:db2://localhost:50000/hwone";		// Adjust your database's name here
	private String user;								// Put your database's username here
	private String password;					// Put your database's password here
	private String articleTable = import_john.DataLoading.articleTable;
	private String authorTable = import_john.DataLoading.authorTable;
	private String keywordTable = import_john.DataLoading.keywordTable;
	private Connection conn = null;
	private PreparedStatement ps_article = null;
	private PreparedStatement ps_author = null;
	private PreparedStatement ps_keyword = null;
	//private String[] fieldNames = new String[]{"TI", "AU", "AF", "AD", "SO", "LA", "PT", "AT"};
	
	int debug = 0;
	
	public ArticleCollection (String _databaseName, String _user, String _password)
	{
		// Connect to database
		url = "jdbc:db2://localhost:50000/" + _databaseName;
		user = _user;
		password = _password;
	}
	
	
	
	private void connectToDatabase()
	{
		try
		{
			Class.forName(jdbcClassName);
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void disconnectToDatabase()
	{
		try
		{
			if (ps_article != null)
				ps_article.close();
			if (ps_author != null)
				ps_author.close();
			if (ps_keyword != null)
				ps_keyword.close();
			if (conn != null)
				conn.close();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public void readFromTextFiles(String _textFilePaths, int _autoWriteToDatabase)
	{
		connectToDatabase();
		prepareStatement();
		
		articles = new ArrayList<Article>();
		
		File folder = new File(_textFilePaths);
		File[] fileList = folder.listFiles();
		for (int i = 0; i< fileList.length; i++)
		{
			if (fileList[i].isFile())
			{
				readTextFile(fileList[i].getAbsolutePath(), _autoWriteToDatabase);
			}
		}
		
		disconnectToDatabase();
	}
	
	public void readTextFile(String _textFilePath, int _autoWriteToDatabase)
	{
		try (BufferedReader br = new BufferedReader(new FileReader(_textFilePath)))
		{
			String line;
			Article ar = new Article();
			int iArticle = 0;
			while ((line = br.readLine()) != null)
			{
				if (line.isEmpty())
				{
					// Add article to collection and start a new article
					if (!ar.isEmpty())
					{
						
						iArticle++;
						articles.add(ar);
						ar = new Article();
					}
					
					// Auto write to database when the buffer is full
					if (_autoWriteToDatabase > 0 && articles.size() >= _autoWriteToDatabase)
					{
						//System.out.println(iArticle);
						writeToDatabase();
						
					}
				}
				else
				{
					String fieldName = line.substring(0, 2);
					String fieldValue = line.substring(4);
					ar.addField(fieldName, fieldValue);
				}
			}
			
			if (!ar.isEmpty())
			{
				articles.add(ar);
			}
			
			if (articles.size() > 0)
			{
				System.out.println(iArticle);
				writeToDatabase();
			}
		} catch(Exception ex)
		{
			System.out.println("Read Text File Exception");
			System.out.print(ex.getMessage());
		}
		
	}
	
	
	private void writeToDatabase()
	{
		try
		{
			if (debug == 124)
			{
				int a = 3;
				int b = a;
			}
			//System.out.println(debug);
			debug++;
			String[] fieldList = Article.getFieldList();
			for (int iArt = 0; iArt< articles.size(); iArt++)
			{
				Article art = articles.get(iArt);
				for (int iField = 0; iField < fieldList.length; iField++)
				{
					String fieldName = fieldList[iField];
					String fieldVal = art.getFieldValue(fieldName);
					
					switch (Article.getFieldType(fieldName))
					{
						case "varchar":
						{
							if (fieldVal != null)
							{
								ps_article.setString(iField + 1, fieldVal);
							}
							else
							{
								ps_article.setNull(iField + 1, java.sql.Types.VARCHAR);
							}
						} break;
						case "integer":
						{
							if (fieldVal != null)
							{
								ps_article.setInt(iField + 1, Integer.parseInt(fieldVal));
							}
							else
							{
								ps_article.setNull(iField + 1, java.sql.Types.INTEGER);
							}
						} break;
					}
					
				}
				ps_article.executeUpdate();
				
				ResultSet rs = ps_article.getGeneratedKeys();
				while (rs.next())
				{
					int idColVar = rs.getInt(1);
					
					// Insert Keyword
					String[] keywords = art.getKeywords();
					for (int iKeyword = 0; iKeyword < keywords.length; iKeyword++)
					{
						
						ps_keyword.setString(1, keywords[iKeyword]);
						ps_keyword.setInt(2, idColVar);
						ps_keyword.executeUpdate();
					}
					
					// Insert author (not implemented yet)
					String[] authors = art.getAuthors();
					for (int iAu = 0; iAu < authors.length; iAu++)
					{
						ps_author.setString(1, "");		// Need to fill to this short form of author later
						ps_author.setString(2, authors[iAu]);
						ps_author.setInt(3, idColVar);
						ps_author.executeUpdate();
					}
				}
			}
			
			
			
			/*
			for (int iArt = 0; iArt< articles.size(); iArt++)
			{
				Article art = articles.get(iArt);
				for (int iField = 0; iField < fieldNames.length; iField++)
				{
					String fieldName = fieldNames[iField];
					String fieldVal = art.getFieldValue(fieldName);
					if (fieldVal == null)
					{
						fieldVal = "NULL";
					}
					
					ps.setString(iField + 1, fieldVal);
					//ps.setString(iField + 1, Integer.toString(iField + 1));
				}
				
				ps.executeUpdate();
			}
			*/
			
			/*
			for (Record record : records) {
			    ps.setInt(1, record.id);
			    ps.setString(2, record.name);
			    ps.setInt(3, record.value);
			    ps.addBatch();
			}
			ps.executeBatch();
			*/
		}
		catch (Exception ex)
		{
			System.out.println("Write to DB Exception");
			System.out.print(ex.getMessage());
		}
		finally
		{
			try
			{
				// Reset article list
				articles.clear();
			}
			catch(Exception ex)
			{
				System.out.println("Write to DB Exception");
				System.out.println(ex.getMessage());
			}
		}
	}
	
	private void prepareStatement()
	{
		try {
			//conn.setAutoCommit(false);
			
			// statement for article table
			String[] fieldList = Article.getFieldList();
			String query = "INSERT INTO " + articleTable +" (";
			String questionMarkStr = "(";
			for (int i = 0; i< fieldList.length; i++)
			{
				query += fieldList[i];
				questionMarkStr += "?";
				if (i < fieldList.length - 1)
				{
					query += ",";
					questionMarkStr += ",";
				}
			}
			questionMarkStr += ")";
			query += ") VALUES " + questionMarkStr;
			
			ps_article = conn.prepareStatement(query, new String[] {"article_id"});
			
			// statement for keyword table
			String kw_query = "INSERT INTO " + keywordTable +" (KEYWORD, ARTICLE_ID) VALUES (?, ?)";
			ps_keyword = conn.prepareStatement(kw_query);
			
			// statement for author table
			String au_query = "INSERT INTO " + authorTable +" (AU_SHORT, AU_FULL, ARTICLE_ID) VALUES (?, ?, ?)";
			ps_author = conn.prepareStatement(au_query);
		} catch (SQLException e) {
			System.out.println("Exception at Prepared Statement");
			System.out.println(e.getMessage());
			//e.getMessage();
		}
	}
	
}
