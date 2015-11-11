import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

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

//		String pathname1 = "data_files/Pubmed/pubmed.txt";
//		String pathname2 = "data_files/Cochrane/cochrane.txt";

		DatabaseConnection.connect(databaseName, username, password);
		DataLoading.inital();
//		DataLoading.loadDataFromPudmed(pathname1);
//		DataLoading.loadDataFromCochrane(pathname2);

		String searchTable = "article2";
		String searchField = "ARTICLE_ID";
		String searchValue = "2";

		LinkedHashMap<String, String> requestedData = QueryFunctions
				.searchQuery(searchTable, searchField, searchValue);

		GuiManager guiManager = new GuiManager();
		guiManager.showAuthorTable(requestedData);

		// for (String key : requestedData.keySet()) {
		// System.out.println(key + ": " + requestedData.get(key));
		//
		// }

		// System.out.println(fieldInArticleTable[i] + ": " + tmp);

		// ac.test1();
	}
}
