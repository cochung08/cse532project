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

		String pathname1 = "data_files/Pubmed/pubmed.txt";
		String pathname2 = "data_files/Cochrane/cochrane.txt";

		DatabaseConnection.connect();

//		QueryFunctions.ifDuplicate("?", "?", "?");

		// DataLoading.loadDataFromPudmed(pathname1);
		// DataLoading.loadDataFromCochrane(pathname2);

		String article_value = "1";

		GuiManager guiManager = new GuiManager();

		// guiManager.searchGUI();

		// int maxArticleId = QueryFunctions.getMaxArticleId();
		// for (int i = 1; i <= maxArticleId; i++) {
		// boolean result = GuiManager.finalRatingGui(String.valueOf(i));
		// if (result == true) {
		// System.out.println("result: " + i);
		// break;
		// }
		// }

		// for (String key : authorData.keySet()) {
		// System.out.println(key + ": " + authorData.get(key));
		//
		// }

		// LinkedHashMap<String, String>
		// map=QueryFunctions.searchArticle("article3","article_id","2");
		// QueryFunctions.searchAll(map);

		// guiManager.showAuthorTable(requestedData);

		// ac.test1();

		GuiManager.showMainPage();
	}

}
