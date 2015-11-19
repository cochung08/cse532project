package Rating;

import java.util.HashMap;

public class ArticleInfo {
	HashMap<String, String> data;
	String rate1;
	String rate2;
	String activeRate;
	int id;
	
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
	
	public void setID(int _id)
	{
		id = _id;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setRate1FromDB(String _r)
	{
		rate1 = _r;
	}
	
	public void setRate2FromDB(String _r)
	{
		rate2 = _r;
	}
	
	public void setRateFromUser(String _r)
	{
		if (rate1 == null)
		{
			rate1 = _r;
		}
		else
		{
			rate2 = _r;
		}
	}
	
	public String getRate1()
	{
		return rate1;
	}
	
	public String getRate2()
	{
		return rate2;
	}
}
