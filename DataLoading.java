import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Vector;

public class DataLoading {

	static String articleTable = "article9";
	static String authorTable = "author9";
	static String keywordTable = "keyword9";
	static int sizeOfArticle = 29;

	static String insert_query = "INSERT INTO "
			+ articleTable
			+ "( CIT,REF,AU_CORR,TITLE,JNL_TIT,VOL,ISSUE,YEAR,PG_ST,PG_EN,LANG,ABS,PUBID,DOI,URL"
			+ ",PUBTYPE,FUND,SUBJECT_TAG,RATE,SCR,FNLRATE,FNLDESIGN,MCNRATE,MCNCONF,RATE1,RATE_PER1,RATE2,RATE_PER2) VALUES"
			+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	static String au_query = "INSERT INTO " + authorTable
			+ " (AU_SHORT, AU_FULL, ARTICLE_ID) VALUES (?, ?, ?)";

	static String kw_query = "INSERT INTO " + keywordTable
			+ " (KEYWORD, ARTICLE_ID) VALUES (?, ?)";

	public static void loadDataFromPudmed(String pathname) {
		PreparedStatement ps_author = null;
		PreparedStatement ps_keyword = null;
		PreparedStatement ps_article = null;

		try {

			ps_author = DatabaseConnection.conn.prepareStatement(au_query);
			ps_keyword = DatabaseConnection.conn.prepareStatement(kw_query);
			ps_article = DatabaseConnection.conn.prepareStatement(insert_query,
					new String[] { "article_id" });

		} catch (Exception e) {
			e.printStackTrace();
		}

		Vector<String> authorsVector = new Vector<String>();
		Vector<String> keyWordsVector = new Vector<String>();

		try {

			int size = sizeOfArticle;
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

			String title = null;
			String year = null;
			String issue = null;
			String author = null;

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

						if (key.equals("IP")) {
							issue = value;
						}

						if (key.equals("TI")) {

							title = value;
						}

						if (key.equals("DP")) {
							value = value.substring(0, 4);
							year = value;
							System.out.println("hahhh" + value);
						}

						// System.out.println(key);
						// System.out.println(value);

						if (key.equals("MH"))
							keyWordsVector.add(value);

						if (key.equals("FAU")) {
							authorsVector.add(value);
							author = value;
						}

						for (int i = 0; i < size; i++) {
							if (matchTable[i] != null)
								if (matchTable[i].equals(key)) {
									insertTable[i] = value;
									System.out.println("insert: " + value);
								}

						}
					}
				}

				fileReader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("title:" + title);
			System.out.println("year:" + year);
			System.out.println("issue:" + issue);

			ArrayList<Integer> dupIdList = QueryFunctions.ifDuplicate(title,
					year, issue, author);
			if (dupIdList != null) {
				System.out.println("duplicate,dont add");
				return;
			} else {
				System.out.println("no duplicate,add");
			}

			for (int w = 1; w < size; w++) {

				if (insertTable[w] == null) {
					ps_article.setNull(w, java.sql.Types.VARCHAR);
				} else
					ps_article.setString(w, insertTable[w]);

			}

			ps_article.executeUpdate();
			DatabaseConnection.conn.commit();

			ResultSet rsKey = ps_article.getGeneratedKeys();

			while (rsKey.next()) {
				int idColVar = rsKey.getInt(1);
				// System.out.println(keyWords.length);

				for (int iKeyword = 0; iKeyword < keyWordsVector.size(); iKeyword++) {

					ps_keyword.setString(1, keyWordsVector.get(iKeyword));
					ps_keyword.setInt(2, idColVar);
					ps_keyword.executeUpdate();
					DatabaseConnection.conn.commit();
				}

				// Insert author (not implemented yet)

				for (int iAu = 0; iAu < authorsVector.size(); iAu++) {
					ps_author.setString(1, ""); // Need to fill to this short
					// form of author later
					ps_author.setString(2, authorsVector.get(iAu));
					ps_author.setInt(3, idColVar);
					ps_author.executeUpdate();
					DatabaseConnection.conn.commit();
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

	public static void loadDataFromCochrane(String pathname) {

		PreparedStatement ps_author = null;
		PreparedStatement ps_keyword = null;
		PreparedStatement ps_article = null;

		try {

			ps_author = DatabaseConnection.conn.prepareStatement(au_query);
			ps_keyword = DatabaseConnection.conn.prepareStatement(kw_query);
			ps_article = DatabaseConnection.conn.prepareStatement(insert_query,
					new String[] { "article_id" });

		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] keyWords = null;
		Vector<String> authorsVector = new Vector<String>();

		String title = null;
		String year = null;
		String issue = null;
		String vol = null;
		String author = null;

		try {

			int size = sizeOfArticle;
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

						if (key.equals("TI"))
							title = value;

						if (key.equals("YR"))
							year = value;

						if (key.equals("VL"))
							vol = value;

						if (key.equals("NO"))
							issue = value;

						if (key.equals("KY"))
							keyWords = value.split(";");

						if (key.equals("AU")) {
							authorsVector.add(value);
							author = value;
						}

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

			ArrayList<Integer> dupIdList = QueryFunctions.ifDuplicate(title,
					year, issue, author);
			if (dupIdList != null) {
				System.out.println("duplicate,dont add");
				return;
			} else {
				System.out.println("no duplicate,add");
			}

			for (int w = 1; w < size; w++) {

				if (insertTable[w] == null) {
					ps_article.setNull(w, java.sql.Types.VARCHAR);
				} else
					ps_article.setString(w, insertTable[w]);

			}

			ps_article.executeUpdate();
			DatabaseConnection.conn.commit();

			ResultSet rsKey = ps_article.getGeneratedKeys();

			while (rsKey.next()) {
				int idColVar = rsKey.getInt(1);
				// System.out.println(keyWords.length);

				for (int iKeyword = 0; iKeyword < keyWords.length; iKeyword++) {

					ps_keyword.setString(1, keyWords[iKeyword]);
					ps_keyword.setInt(2, idColVar);
					ps_keyword.executeUpdate();
					DatabaseConnection.conn.commit();
				}

				// Insert author (not implemented yet)

				for (int iAu = 0; iAu < authorsVector.size(); iAu++) {
					ps_author.setString(1, ""); // Need to fill to this short
					// form of author later
					ps_author.setString(2, authorsVector.get(iAu));
					ps_author.setInt(3, idColVar);
					ps_author.executeUpdate();
					DatabaseConnection.conn.commit();
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
