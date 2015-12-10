package Rating;

import java.util.HashMap;

public class ArticleInfo {
	HashMap<String, String> data;
	String[] rate;
	String[] rate_person;
	int activeRate;
	int id;
	boolean updated = false;
	
	public ArticleInfo()
	{
		data = new HashMap<String, String>();
		rate = new String[2];
		rate[0]	= null;
		rate[1] = null;
		rate_person = new String[2];
		rate_person[0] = null;
		rate_person[1] = null;
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
		rate_person[person_id - 1] = _str;
	}
	
	public void setRateFromUser(String _r)
	{
		rate[activeRate] = _r;
		updated = true;
	}
	
	public void setActiveRate(int _at)
	{
		activeRate = _at - 1;
	}
	
	public String getRate()
	{
		return rate[activeRate];
	}
	
	public int getActiveRate()
	{
		return activeRate + 1;
	}
	
	public String getPerson()
	{
		return rate_person[activeRate];
	}
	
	public boolean isUpdated ()
	{
		return updated;
	}
}
