package Rating;

import java.util.HashMap;

public class ArticleInfo {
	HashMap<String, String> data;
	
	public ArticleInfo()
	{
		
	}
	
	public void addValue (String key, String value)
	{
		data.put(key, value);
	}
	
	public String getValue (String key)
	{
		return data.get(key);
	}
}
