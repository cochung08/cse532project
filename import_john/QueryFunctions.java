package import_john;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.ibm.db2.jcc.am.ResultSet;

public class QueryFunctions {

	public static boolean isWhitespace(String str) {
		if (str == null) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static int getMaxArticleId() {
		int max_ = 0;
		try {
			Statement st = DatabaseConnection.conn.createStatement();
			ResultSet rset = (ResultSet) st
					.executeQuery("SELECT MAX(ARTICLE_ID) AS max_ FROM "
							+ DataLoading.articleTable);
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

	public static LinkedHashMap<String, String> getFinalRatingData(
			String article_value) {
		String searchTable = DataLoading.articleTable;
		String searchField = "ARTICLE_ID";

		LinkedHashMap<String, String> articleData = QueryFunctions
				.searchArticle(searchTable, searchField, article_value);

		if (articleData == null) {
			return null;
		}
		String searchTable3 = DataLoading.keywordTable;
		String searchField3 = "ARTICLE_ID";

		ListMultimap<String, String> keywordData = QueryFunctions
				.searchAuthorOrKeyword(searchTable3, searchField3,
						article_value);

		String searchTable2 = DataLoading.authorTable;
		String searchField2 = "ARTICLE_ID";

		ListMultimap<String, String> authorData = QueryFunctions
				.searchAuthorOrKeyword(searchTable2, searchField2,
						article_value);

		String ARTICLE_ID = articleData.get("ARTICLE_ID");
		String TITLE = articleData.get("TITLE");
		String VOL = articleData.get("VOL");
		String ISSUE = articleData.get("ISSUE");
		String YEAR = articleData.get("YEAR");
		String ABS = articleData.get("ABS");
		String RATE1 = articleData.get("RATE1");
		String RATE2 = articleData.get("RATE2");
		String RATEFINAL = articleData.get("FNLRATE");
		String KEYWORD = keywordData.get("KEYWORD").toString();
		String AUTHOR = authorData.get("AU_FULL").toString();

		LinkedHashMap<String, String> finalRatingData = new LinkedHashMap<String, String>();

		finalRatingData.put("ARTICLE_ID", ARTICLE_ID);
		finalRatingData.put("TITLE", TITLE);
		finalRatingData.put("VOL", VOL);
		finalRatingData.put("ISSUE", ISSUE);
		finalRatingData.put("YEAR", YEAR);
		finalRatingData.put("ABS", ABS);
		finalRatingData.put("RATE1", RATE1);
		finalRatingData.put("RATE2", RATE2);
		finalRatingData.put("FNLRATE", RATEFINAL);
		finalRatingData.put("KEYWORD", KEYWORD);
		finalRatingData.put("AU_FULL", AUTHOR);

		return finalRatingData;

	}

	public static void updateArticleFinalRate(int article_id, String finalRATE) {

		String tableName = DataLoading.articleTable;

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

		String tableName = DataLoading.articleTable;

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

			LinkedHashMap<String, String> articleMap = new LinkedHashMap<String, String>();
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

			// ,
			// ResultSet.TYPE_SCROLL_INSENSITIVE,
			// ResultSet.CONCUR_READ_ONLY

			ResultSet rs = (ResultSet) ps_search.executeQuery();

			DatabaseConnection.conn.commit();

			// if (!rs.isBeforeFirst()) {
			// System.out.println("No data");
			// return null;
			// } else {
			boolean ifEmpty = true;

			while (rs.next()) {
				ifEmpty = false;
				for (int i = 0; i < fieldInArticleTable.size(); i++) {

					String tmp = rs.getString(fieldInArticleTable.get(i));
					articleMap.put(fieldInArticleTable.get(i), tmp);

				}

			}

			rs.close();
			ps_search.close();
			DatabaseConnection.conn.close();
			DatabaseConnection.connect();

			if (ifEmpty == true)
				return null;
			else
				return articleMap;
			// }

		} catch (SQLException e) {

			if (e.getSQLState().equals("51002")) {

			}
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;

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
			ps_search.close();
			DatabaseConnection.conn.close();
			DatabaseConnection.connect();

			return multiMap;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getSQLState().equals("51002")) {
				System.out.println("ssssssssssss");

			}

			e.printStackTrace();

		}
		return null;

	}

	public static ArrayList<dataCollection> searchJoinTable(
			LinkedHashMap<String, String> Map) {

		String tableName = DataLoading.articleTable;
		ArrayList<String> matchedArtcileIdList = new ArrayList<String>();

		try {
			Statement st = DatabaseConnection.conn.createStatement();
			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ tableName);
			ResultSetMetaData md = rset.getMetaData();

			Vector<String> fieldInArticleTable = new Vector<String>();
			for (int i = 1; i <= md.getColumnCount(); i++) {

				fieldInArticleTable.add(md.getColumnLabel(i));
			}

			String joinStr = DataLoading.articleTable + " LEFT JOIN "
					+ DataLoading.authorTable + " ON "
					+ DataLoading.articleTable + ".ARTICLE_ID= "
					+ DataLoading.authorTable + ".ARTICLE_ID LEFT JOIN "
					+ DataLoading.keywordTable + " ON "
					+ DataLoading.keywordTable + ".ARTICLE_ID = "
					+ DataLoading.authorTable + ".ARTICLE_ID";

			String search_article_query = "SELECT * from " + joinStr
					+ " where ";

			String tmp2 = "";

			for (String key : Map.keySet()) {
				String value = Map.get(key);
				// System.out.println(value);
				if (isWhitespace(value) == false) {

					if (key.equalsIgnoreCase("year_begin")) {
						tmp2 = tmp2 + "year" + " >= " + value + " and ";
					} else if (key.equalsIgnoreCase("year_end")) {

						tmp2 = tmp2 + "year" + " <= " + value + " and ";
					} else if (key.equalsIgnoreCase("ARTICLE_ID")) {
						tmp2 = tmp2 + DataLoading.articleTable + ".ARTICLE_ID"
								+ " = " + value + " and ";

					} else {
						tmp2 = tmp2 + "LOWER(" + key + ")" + " like LOWER('%"
								+ value + "%') and ";
					}
				}
			}
			if (tmp2 != null)
				tmp2 = tmp2.substring(0, tmp2.length() - 4);

			search_article_query = search_article_query + tmp2;
			System.out.println(search_article_query);

			PreparedStatement search_article = DatabaseConnection.conn
					.prepareStatement(search_article_query);

			ResultSet rs = (ResultSet) search_article.executeQuery();

			while (rs.next()) {

				String article_id_key = "ARTICLE_ID";
				String article_id_value = rs.getString(article_id_key);
				if (matchedArtcileIdList.contains(article_id_value) == false)
					matchedArtcileIdList.add(article_id_value);
			}

			rs.close();

		} catch (SQLException e) {

			if (e.getSQLState().equals("51002")) {

			}

			e.printStackTrace();
		}

		// for (int i = 0; i < matchedArtcileIdList.size(); i++)
		System.out.println("size:" + matchedArtcileIdList.size());

		return getResultById(matchedArtcileIdList);

	}

	static ArrayList<dataCollection> getResultById(ArrayList<String> idList) {

		String article_id_key = "ARTICLE_ID";

		ArrayList<dataCollection> dataCollectionArray = new ArrayList<dataCollection>();

		for (String article_id_value : idList) {

			ListMultimap<String, String> keywordData = QueryFunctions
					.searchAuthorOrKeyword(DataLoading.keywordTable,
							article_id_key, article_id_value);

			LinkedHashMap<String, String> articleData = QueryFunctions
					.searchArticle(DataLoading.articleTable, article_id_key,
							article_id_value);

			ListMultimap<String, String> authorData = QueryFunctions
					.searchAuthorOrKeyword(DataLoading.authorTable,
							article_id_key, article_id_value);

			List<String> tmp = keywordData.get("KEYWORD");
			// System.out.println(tmp.toString());

			List<String> tmp1 = authorData.get("AU_FULL");
			// System.out.println(authorData.toString());

			// System.out.println(articleData.toString());

			dataCollection data = new dataCollection(articleData, tmp, tmp1);

			dataCollectionArray.add(data);
		}

		return dataCollectionArray;
	}

	static ArrayList<dataCollection> ifDuplicate(String title, String year,
			String issue, String author) {

		LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();
		updateMap.put("TITLE", title);
		ArrayList<dataCollection> dataResultset = searchJoinTable(updateMap);

		LinkedHashMap<String, String> updateMap2 = new LinkedHashMap<String, String>();

		// updateMap.put("TITLE", title);
		updateMap2.put("YEAR", year);
		updateMap2.put("ISSUE", issue);
		updateMap2.put("AU_FULL", author);

		ArrayList<dataCollection> dataResultset2 = searchJoinTable(updateMap2);

		if (dataResultset.size() > 1) {
			return dataResultset;
		} else if (dataResultset2.size() > 1) {
			return dataResultset2;
		} else
			return null;

		// if (dataResultset.size() > 0) {
		// System.out.println(1);
		// ArrayList<String> duplicatedIdList = new ArrayList<String>();
		// for (int i = 0; i < dataResultset.size(); i++) {
		// String dupId = dataResultset.get(0).articleMap
		// .get("ARTICLE_ID");
		// duplicatedIdList.add(dupId);
		// System.out.println(dupId);
		// }
		//
		// return duplicatedIdList;
		// } else {
		// if (dataResultset2.size() > 0) {
		// System.out.println(2);
		// ArrayList<String> duplicatedIdList = new ArrayList<String>();
		// for (int i = 0; i < dataResultset.size(); i++) {
		// String dupId = dataResultset2.get(0).articleMap
		// .get("ARTICLE_ID_");
		// duplicatedIdList.add(dupId);
		// System.out.println(dupId);
		// }
		// return duplicatedIdList;
		// } else
		// return null;
		// }

	}

	static void exportById(ArrayList<String> idList) {

		ArrayList<dataCollection> dataCollectionArray = QueryFunctions
				.getResultById(idList);

		for (dataCollection data : dataCollectionArray) {

			List<String> lines = new ArrayList<String>();

			LinkedHashMap<String, String> articleMap = data.articleMap;
			List<String> keywordMap = data.keywordMap;
			List<String> authorMap = data.authorMap;

			articleMap.put("KEY", keywordMap.toString());
			articleMap.put("AUTHOR", authorMap.toString());

			String id = articleMap.get("ARTICLE_ID");

			String strs = "";
			for (String key : articleMap.keySet()) {
				String value = articleMap.get(key);
				key = Strings.padEnd(key, 15, ' ');

				String tmp = key + "- " + value;
				lines.add(tmp);
			}

			try {

				Path file = Paths
						.get("data_files\\ExportFiles\\" + id + ".txt");
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	static void exportDuplicate(ArrayList<dataCollection> dataCollectionArray) {

		String id = dataCollectionArray.get(0).articleMap.get("ARTICLE_ID");

		List<String> lines = new ArrayList<String>();

		for (dataCollection data : dataCollectionArray) {

			LinkedHashMap<String, String> articleMap = data.articleMap;
			List<String> keywordMap = data.keywordMap;
			List<String> authorMap = data.authorMap;

			articleMap.put("KEY", keywordMap.toString());
			articleMap.put("AUTHOR", authorMap.toString());

			String strs = "";
			for (String key : articleMap.keySet()) {
				String value = articleMap.get(key);
				key = Strings.padEnd(key, 15, ' ');

				String tmp = key + "- " + value;
				lines.add(tmp);
			}

			lines.add("------------------------------------");

		}

		try {

			Path file = Paths.get("data_files\\ExportFiles\\" + id + ".txt");
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void removeDuplicate() {
		ArrayList<String> idList = new ArrayList<String>();
		try {

			ListMultimap<String, String> multiMap = ArrayListMultimap.create();

			String search_query = "SELECT ARTICLE_ID from "
					+ DataLoading.articleTable;

			System.out.println(search_query);

			PreparedStatement ps_search = DatabaseConnection.conn
					.prepareStatement(search_query);
			ResultSet rs = (ResultSet) ps_search.executeQuery();
			DatabaseConnection.conn.commit();

			while (rs.next()) {

				String value = rs.getString(1);
				idList.add(value);
				// System.out.println("sssqqq" + value);

			}
			rs.close();
			ps_search.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<dataCollection> dataCollectionArray = QueryFunctions
				.getResultById(idList);

		System.out.println("size: " + dataCollectionArray.size());

		// ifDuplicate
		List<String> lines = new ArrayList<String>();
		for (dataCollection data : dataCollectionArray) {

			LinkedHashMap<String, String> articleMap = data.articleMap;
			List<String> keywordMap = data.keywordMap;
			List<String> authorMap = data.authorMap;

			articleMap.put("KEY", keywordMap.toString());
			articleMap.put("AUTHOR", authorMap.get(0));

			String title = articleMap.get("TITLE");
			String year = articleMap.get("YEAR");
			String issue = articleMap.get("ISSUE");
			String author = articleMap.get("AUTHOR");

			ArrayList<dataCollection> dupList = QueryFunctions.ifDuplicate(
					title, year, issue, author);

			// System.out.println("dupIdList::" + dupIdList);

			exportDuplicate(dupList);

		}

	}
}
