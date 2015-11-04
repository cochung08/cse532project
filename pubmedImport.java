import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class pubmedImport {

	static Connection conn;

	pubmedImport(String databaseName, String user, String password) {

		try {
			String urlPrefix = "jdbc:db2://localhost:50000/";
			String url = urlPrefix + databaseName;

			Class.forName("com.ibm.db2.jcc.DB2Driver");
			System.out.println("**** Loaded the JDBC driver");
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadData(String pathname) {

		Vector<String> authorsVector = new Vector<String>();
		Vector<String> keyWordsVector = new Vector<String>();

		String articleTable = "article2";

		try {

			int size = 24;
			String[] matchTable = new String[size];
			String[] insertTable = new String[size];
			for (int i = 0; i < size; i++) {
				matchTable[i] = null;
				insertTable[i] = null;
			}

			matchTable[2] = "PMID";
			matchTable[4] = "TI";
			matchTable[8] = "DP";
			matchTable[6] = "VI";
			matchTable[7] = "IP";
			matchTable[11] = "LANG";
			// matchTable[16] = "PT";
			matchTable[14] = "LID";
			matchTable[12] = "AB";

			try {

				File file = new File(pathname);
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String line;

				while ((line = bufferedReader.readLine()) != null) {

					if (line.contains("-")) {

						String key = line.substring(0, 3)
								.replaceAll("\\s+", "");
						String value = line.substring(5).substring(1);

						System.out.println(key);
						System.out.println(value);

						if (key.equals("MH"))
							keyWordsVector.add(value);

						if (key.equals("FAU"))
							authorsVector.add(value);

						for (int i = 0; i < size; i++) {
							if (matchTable[i] != null)
								if (matchTable[i].equals(key))
									insertTable[i] = value;

						}
					}
				}

				fileReader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			String insertTableSQL = "INSERT INTO "
					+ articleTable
					+ "( CIT,REF,AU_CORR,TITLE,JNL_TIT,VOL,ISSUE,YEAR,PG_ST,PG_EN,LANG,ABS,PUBID,DOI,URL"
					+ ",PUBTYPE,FUND,RATE,SCR,FNLRATE,FNLDESIGN,MCNRATE,MCNCONF) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps_article = conn.prepareStatement(
					insertTableSQL, new String[] { "article_id" });

			for (int w = 1; w < size; w++) {

				if (insertTable[w] == null) {
					ps_article.setNull(w, java.sql.Types.VARCHAR);
				} else
					ps_article.setString(w, insertTable[w]);

			}

			ps_article.executeUpdate();
			conn.commit();

			ResultSet rsKey = ps_article.getGeneratedKeys();

			String keywordTable = "keyword";
			PreparedStatement ps_keyword = null;
			String kw_query = "INSERT INTO " + keywordTable
					+ " (KEYWORD, ARTICLE_ID) VALUES (?, ?)";
			ps_keyword = conn.prepareStatement(kw_query);

			while (rsKey.next()) {
				int idColVar = rsKey.getInt(1);
				// System.out.println(keyWords.length);

				for (int iKeyword = 0; iKeyword < keyWordsVector.size(); iKeyword++) {

					ps_keyword.setString(1, keyWordsVector.get(iKeyword));
					ps_keyword.setInt(2, idColVar);
					ps_keyword.executeUpdate();
					conn.commit();
				}

				String authorTable = "author";
				PreparedStatement ps_author = null;
				String au_query = "INSERT INTO " + authorTable
						+ " (AU_SHORT, AU_FULL, ARTICLE_ID) VALUES (?, ?, ?)";
				ps_author = conn.prepareStatement(au_query);

				// Insert author (not implemented yet)

				for (int iAu = 0; iAu < authorsVector.size(); iAu++) {
					ps_author.setString(1, ""); // Need to fill to this short
					// form of author later
					ps_author.setString(2, authorsVector.get(iAu));
					ps_author.setInt(3, idColVar);
					ps_author.executeUpdate();
					conn.commit();
				}
			}

			System.out.println("pubmedLoaded");

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();

			}
		}
	}
}
