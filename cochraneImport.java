import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Vector;

public class cochraneImport {
	static Connection conn;

	cochraneImport(String databaseName, String user, String password) {

		try {
			String urlPrefix = "jdbc:db2://localhost:50000/";
			String url = urlPrefix + databaseName;

			// System.out.println("**** Enter class EzJava");
			// String[] tmp = { "MYDB", "db2admin", "1" };

			Class.forName("com.ibm.db2.jcc.DB2Driver");
			System.out.println("**** Loaded the JDBC driver");
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadData(String pathname) {

		String[] keyWords = null;
		Vector<String> authorsVector = new Vector<String>();

		String articleTable = "article2";

		try {

			int size = 24;
			String[] matchTable = new String[size];
			String[] insertTable = new String[size];
			for (int i = 0; i < size; i++) {
				matchTable[i] = null;
				insertTable[i] = null;
			}

			matchTable[2] = "ID";
			matchTable[4] = "TI";
			matchTable[8] = "YR";
			matchTable[6] = "VL";
			matchTable[7] = "NO";
			// matchTable[16] = "PT";
			matchTable[14] = "DOI";
			matchTable[12] = "AB";

			try {

				File file = new File(pathname);
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String line;

				while ((line = bufferedReader.readLine()) != null) {

					if (line.contains(":")) {

						String[] tmpStr = line.split(":", 2);
						String key = tmpStr[0];
						String value = tmpStr[1].substring(1);

						if (key.equals("KY"))
							keyWords = value.split(";");

						if (key.equals("AU"))
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

			PreparedStatement ps_keyword = null;
			String keywordTable = "keyword";
			String kw_query = "INSERT INTO " + keywordTable
					+ " (KEYWORD, ARTICLE_ID) VALUES (?, ?)";
			ps_keyword = conn.prepareStatement(kw_query);

			while (rsKey.next()) {
				int idColVar = rsKey.getInt(1);
				// System.out.println(keyWords.length);

				for (int iKeyword = 0; iKeyword < keyWords.length; iKeyword++) {

					ps_keyword.setString(1, keyWords[iKeyword]);
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

			System.out.println("cochraneLoaded");

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException(); // For drivers that support chained
											// exceptions

			}
		}
	}
}
