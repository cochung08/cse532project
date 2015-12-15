package import_john;

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

		// QueryFunctions.ifDuplicate("?", "?", "?");

		// DataLoading.loadDataFromPudmed(pathname1);
		// DataLoading.loadDataFromCochrane(pathname2);

		String article_value = "1";

		GuiManager guiManager = new GuiManager();

		

		GuiManager.showMainPage();
	}

}
