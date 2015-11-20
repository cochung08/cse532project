package Rating;

import java.util.HashMap;

public class ArticleInfo {
	HashMap<String, String> data;
	String[] rate;
	String[] rate_person;
	int activeRate;
	int id;
	
	public ArticleInfo()
	{
		data = new HashMap<String, String>();
		rate = new String[2];
		rate[0]	= null;
		rate[1] = null;
		activeRate = 0;
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
	
	public void setRateFromDB(String _r, int person_id)
	{
		rate[person_id - 1] = _r;
	}
	
	public void setRatePersonFromDB (String _str, int person_id)
	{
		rate[person_id - 1] = _str;
	}
	
	public void setRateFromUser(String _r)
	{
		rate[activeRate] = _r;
	}
	
	public void setActiveRate(int _at)
	{
		activeRate = _at - 1;
	}
	
	public String getRate()
	{
		return rate[activeRate];
	}
}
