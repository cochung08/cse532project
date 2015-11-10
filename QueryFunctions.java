import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Vector;
import com.ibm.db2.jcc.am.ResultSet;

public class QueryFunctions {
	static String tableName = "article2";

	public static void updateArticleTable(
			LinkedHashMap<String, String> updateMap) {

		try {

			String tmp0 = "UPDATE " + tableName + " SET ";

			String tmp1 = "";
			for (String key : updateMap.keySet()) {
				if (!key.equalsIgnoreCase("ARTICLE_ID"))
					tmp1 = tmp1 + key + " = ?, ";

			}

			tmp1 = tmp1.substring(0, tmp1.length() - 2) + " ";

			String tmp2 = "WHERE ARTICLE_ID = " + updateMap.get("ARTICLE_ID");

			String updated_Query = tmp0 + tmp1 + tmp2;

			System.out.println(updated_Query);

			PreparedStatement ps_update = DataLoading.conn
					.prepareStatement(updated_Query);

			int j = 1;
			for (String key : updateMap.keySet()) {
				if (!key.equalsIgnoreCase("ARTICLE_ID")) {
					ps_update.setString(j, updateMap.get(key));
					j++;
				}

			}

			ps_update.executeUpdate();
			DataLoading.conn.commit();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static LinkedHashMap<String, String> searchQuery(String searchTable,
			String searchField, String searchValue) {

		try {

			LinkedHashMap<String, String> fieldValue = new LinkedHashMap<String, String>();
			;

			String search_query = "SELECT * from " + searchTable + " where "
					+ searchField + " = " + searchValue;

			Statement st = DataLoading.conn.createStatement();
			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ tableName);
			ResultSetMetaData md = rset.getMetaData();

			Vector<String> fieldInArticleTable = new Vector<String>();
			for (int i = 1; i <= md.getColumnCount(); i++) {

				fieldInArticleTable.add(md.getColumnLabel(i));
			}

			PreparedStatement ps_search = DataLoading.conn
					.prepareStatement(search_query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			DataLoading.conn.commit();

			while (rs.next()) {

				for (int i = 0; i < fieldInArticleTable.size(); i++) {

					String tmp = rs.getString(fieldInArticleTable.get(i));
					fieldValue.put(fieldInArticleTable.get(i), tmp);
					// System.out.println(fieldInArticleTable.get(i) + ": " +
					// tmp);
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
