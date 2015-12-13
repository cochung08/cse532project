package import_john;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.ListMultimap;

public class dataCollection {
	LinkedHashMap<String, String> articleMap;
	List<String> keywordMap;
	List<String> authorMap;

	dataCollection(LinkedHashMap<String, String> articleData,
			List<String> keywordMap, List<String> authorMap) {
		this.articleMap = articleData;
		this.keywordMap = keywordMap;
		this.authorMap = authorMap;
	}
}