import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import com.ibm.db2.jcc.am.ResultSet;

public class QueryFunctions {

	public static void updateArticleTable(String articleId,
			HashMap<String, String> updateMap) {
		String tableName = "article2";
		try {

			String[] fieldInArticleTable = null;

			Statement st = DataLoading.conn.createStatement();
			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ tableName);
			ResultSetMetaData md = rset.getMetaData();

			fieldInArticleTable = new String[md.getColumnCount() - 1];
			for (int i = 2; i <= md.getColumnCount(); i++) {
				 
				fieldInArticleTable[i - 2] = md.getColumnLabel(i);
			}

			String tmp0 = "UPDATE " + tableName + " SET ";

			String tmp1 = "";
			for (String key : fieldInArticleTable) {
				tmp1 = tmp1 + key + " = ?, ";
			}

			tmp1 = tmp1.substring(0, tmp1.length() - 2) + " ";

			String tmp2 = "WHERE ARTICLE_ID = " + articleId;

			String updated_Query = tmp0 + tmp1 + tmp2;

			System.out.println(updated_Query);

			PreparedStatement ps_update = DataLoading.conn
					.prepareStatement(updated_Query);

			int j = 0;
			for (String key : updateMap.keySet()) {
				tmp1 = tmp1 + key + " = ?, ";
				ps_update.setString(j + 1,
						updateMap.get(fieldInArticleTable[j]));
				j++;
			}

			ps_update.executeUpdate();
			DataLoading.conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static HashMap<String, String> searchQuery(String searchTable,
			String searchField, String searchValue) {

		try {
			Vector<String> requestedData = new Vector<String>();
			HashMap<String, String> fieldValue = new HashMap<String, String>();
			;

			String search_query = "SELECT * from " + searchTable + " where "
					+ searchField + " = " + searchValue;

			String[] fieldInArticleTable = { "CIT", "REF", "AU_CORR", "TITLE",
					"JNL_TIT", "VOL", "ISSUE", "YEAR", "PG_ST", "PG_EN",
					"LANG", "ABS", "PUBID", "DOI", "URL", "PUBTYPE", "FUND",
					"RATE", "SCR", "FNLRATE", "FNLDESIGN", "MCNRATE", "MCNCONF" };

			PreparedStatement ps_search;

			ps_search = DataLoading.conn.prepareStatement(search_query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			DataLoading.conn.commit();

			while (rs.next()) {

				for (int i = 0; i < 23; i++) {

					String tmp = rs.getString(fieldInArticleTable[i]);
					fieldValue.put(fieldInArticleTable[i], tmp);
					// System.out.println(fieldInArticleTable[i] + ": " + tmp);
				}

			}
			rs.close();

			return fieldValue;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
