import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.ibm.db2.jcc.am.ResultSet;

public class QueryFunctions {

	public static int getMaxArticleId() {
		int max_ = 0;
		try {
			Statement st = DatabaseConnection.conn.createStatement();
			ResultSet rset = (ResultSet) st
					.executeQuery("SELECT MAX(ARTICLE_ID) AS max_ FROM article3");
			while (rset.next()) {

				max_ = rset.getInt("max_");

			}
			rset.close();

			return max_;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	public static void updateArticleFinalRate(int article_id, String finalRATE) {

		String tableName = "article3";

		try {

			String tmp0 = "UPDATE " + tableName + " SET ";

			String tmp1 = "FNLRATE" + " = ? ";

			String tmp2 = "WHERE ARTICLE_ID = " + article_id;

			String updated_Query = tmp0 + tmp1 + tmp2;

			System.out.println(updated_Query);

			PreparedStatement ps_update = DatabaseConnection.conn
					.prepareStatement(updated_Query);

			ps_update.setString(1, finalRATE);

			ps_update.executeUpdate();
			DatabaseConnection.conn.commit();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static void updateArticleTable(
			LinkedHashMap<String, String> updateMap) {

		String tableName = "article3";

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

			PreparedStatement ps_update = DatabaseConnection.conn
					.prepareStatement(updated_Query);

			int j = 1;
			for (String key : updateMap.keySet()) {
				if (!key.equalsIgnoreCase("ARTICLE_ID")) {
					ps_update.setString(j, updateMap.get(key));
					j++;
				}

			}

			ps_update.executeUpdate();
			DatabaseConnection.conn.commit();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static LinkedHashMap<String, String> searchArticle(
			String searchTable, String searchField, String searchValue) {

		try {

			LinkedHashMap<String, String> fieldValue = new LinkedHashMap<String, String>();
			;

			String search_query = "SELECT * from " + searchTable + " where "
					+ searchField + " = " + searchValue;

			Statement st = DatabaseConnection.conn.createStatement();
			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ searchTable);
			ResultSetMetaData md = rset.getMetaData();

			Vector<String> fieldInArticleTable = new Vector<String>();
			for (int i = 1; i <= md.getColumnCount(); i++) {

				fieldInArticleTable.add(md.getColumnLabel(i));
			}

			PreparedStatement ps_search = DatabaseConnection.conn
					.prepareStatement(search_query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			DatabaseConnection.conn.commit();

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

	public static ListMultimap<String, String> searchAuthorOrKeyword(
			String searchTable, String searchField, String searchValue) {

		try {

			// Map<String, List<String>> map = new HashMap<String,
			// List<String>>();

			ListMultimap<String, String> multiMap = ArrayListMultimap.create();

			String search_query = "SELECT * from " + searchTable + " where "
					+ searchField + " = " + searchValue;

			Statement st = DatabaseConnection.conn.createStatement();
			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ searchTable);
			ResultSetMetaData md = rset.getMetaData();

			Vector<String> fieldInArticleTable = new Vector<String>();
			for (int i = 1; i <= md.getColumnCount(); i++) {

				fieldInArticleTable.add(md.getColumnLabel(i));
			}

			PreparedStatement ps_search = DatabaseConnection.conn
					.prepareStatement(search_query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			DatabaseConnection.conn.commit();

			while (rs.next()) {

				for (int i = 0; i < fieldInArticleTable.size(); i++) {

					String tmpKey = fieldInArticleTable.get(i);
					String value = rs.getString(tmpKey);
					multiMap.put(tmpKey, value);
					// System.out.println(fieldInArticleTable.get(i) + ": " +
					// tmp);
				}

			}
			rs.close();

			return multiMap;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
