import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.ListMultimap;

//import com.google.common.collect.Multimap;

public class MainFunctionForJohn {

	private static String psycInfoLabelMeaningFile = "PsycInfoLabelMeaning.txt";

	public static void main(String[] args) {

		// Converter.convertPubmed();

		String databaseName = "MYDB";
		String username = "db2admin"; // You put your username here
		String password = "1"; // You put your password here
		// ArticleCollection ac = new ArticleCollection(databaseName, username,
		// password);
		// ac.readFromTextFiles(data_files, 2);

		// String pathname1 = "data_files/Pubmed/pubmed.txt";
		// String pathname2 = "data_files/Cochrane/cochrane.txt";

		DatabaseConnection.connect(databaseName, username, password);
		DataLoading.inital();
		// DataLoading.loadDataFromPudmed(pathname1);
		// DataLoading.loadDataFromCochrane(pathname2);

		String searchTable3 = "KEYWORD";
		String searchField3 = "ARTICLE_ID";
		String searchValue3 = "108";

		ListMultimap<String, String> keywordData = QueryFunctions
				.searchAuthorOrKeyword(searchTable3, searchField3, searchValue3);

		String searchTable = "ARTICLE2";
		String searchField = "ARTICLE_ID";
		String searchValue = "3";

		LinkedHashMap<String, String> articleData = QueryFunctions
				.searchArticle(searchTable, searchField, searchValue3);

		String TITLE = articleData.get("TITLE");
		String VOL = articleData.get("VOL");
		String ISSUE = articleData.get("ISSUE");
		String YEAR = articleData.get("YEAR");
		String ABS = articleData.get("ABS");

		List<String> tmp = keywordData.get("KEYWORD");
		String KEYWORD = tmp.toString();
	
		
		

		String searchTable2 = "AUTHOR";
		String searchField2 = "ARTICLE_ID";
		String searchValue2 = "148";

		ListMultimap<String, String> authorData = QueryFunctions
				.searchAuthorOrKeyword(searchTable2, searchField2, searchValue3);
		List<String> tmp2 = authorData.get("AU_FULL");

		String AUTHOR = tmp2.toString();
		

//		for (String key : authorData.keySet()) {
//			System.out.println(key + ": " + authorData.get(key));
//
//		}

		GuiManager guiManager = new GuiManager();
		guiManager.showFinalRatingTable(TITLE, ABS, KEYWORD, AUTHOR, VOL,
				ISSUE, YEAR, "0.5", "0.5");

		// guiManager.showAuthorTable(requestedData);

		// for (String key : requestedData.keySet()) {
		// System.out.println(key + ": " + requestedData.get(key));
		//
		// }

		// System.out.println(fieldInArticleTable[i] + ": " + tmp);

		// ac.test1();
	}
}
