import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Article {
	//private HashMap<String, String> fields;
	private boolean isEmpty = true;
	private static String psycInfoLabelMeaningFile = "E:\\Study\\PhD\\CSE532-Theory of Database Systems\\Project\\PsycInfoLabelMeaning.txt";
	private static String psycInfoFieldDesFile = "E:\\Study\\PhD\\CSE532-Theory of Database Systems\\Project\\PsycInfoFieldDescription.txt";
	private static HashMap<String, String> labelMeaning;	// Key is label (in importing files, value is the corresponding field
	private static HashMap<String, String> labelSplit;		// Key is label (in importing files, value is the string splitter
	private static HashMap<String, String> fieldList;		// Key is field name, value is data type of the field
	private static String[] fieldNames;						// List of field names
	
	private HashMap<String, String> fieldValues;
	private ArrayList<String> keywords;
	private ArrayList<String> authors;
	
	static
	{
		staticInitialization();
	}
	
	public Article()
	{
		keywords = new ArrayList<String>();
		authors = new ArrayList<String>();
		fieldValues = new HashMap<String, String>();
		for (String key : fieldList.keySet())
		{
			fieldValues.put(key, null);
		}
	}
	
	private static void staticInitialization()
	{
		fieldList = new HashMap<String, String>();
		labelMeaning = new HashMap<String, String>();
		labelSplit = new HashMap<String, String>();
		
		// Read label file
		try (BufferedReader br = new BufferedReader(new FileReader(psycInfoLabelMeaningFile)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (!line.isEmpty())
				{
					String[] parts = line.split("\t");
					labelMeaning.put(parts[0], parts[1]);
					labelSplit.put(parts[0], parts[2]);
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
		// Read field description file
		try (BufferedReader br = new BufferedReader(new FileReader(psycInfoFieldDesFile)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (!line.isEmpty())
				{
					String[] parts = line.split("\t");
					fieldList.put(parts[0], parts[1]);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		// initialize list of field names
		Set<String> keys = fieldList.keySet();
		fieldNames = new String[keys.size() - 2];
		int index = 0;
		for (String key:keys)
		{
			if ((!key.equals("AUTHOR")) && (!key.equals("KEYWORD")))
			{
				fieldNames[index] = key;
				index++;
			}
		}
	}
	
	public void addField(String label, String value)
	{
		String fieldName = labelMeaning.get(label);
		
		if (fieldName != null)
		{
			String[] singleVals;
			String splitter = labelSplit.get(label);
			if (splitter.equals("N/A"))
			{
				singleVals = new String[]{value};
			}
			else
			{
				singleVals = value.split(splitter);
			}
			
			if (fieldName.equals("KEYWORD"))
			{
				for (int i = 0; i< singleVals.length; i++)
				{
					keywords.add(singleVals[i]);
				}
				isEmpty = false;
			}
			else
			{
				if (fieldName.equals("AUTHOR"))
				{
					for (int i = 0; i< singleVals.length; i++)
					{
						authors.add(value);
					}
					isEmpty = false;
				}
				else
				{
					
					
					String addedStr = "";
					if (fieldValues.get(fieldName) != null)
					{
						addedStr += "|" + singleVals[0];
					}
					else
					{
						addedStr += singleVals[0];
					}
					
					for (int iVal = 1; iVal< singleVals.length; iVal++)
					{
						addedStr += "|" + singleVals[iVal];
						
					}
					fieldValues.put(fieldName, addedStr);
					isEmpty = false;
				}
			}
		}
		
	}
	
	public boolean isEmpty ()
	{
		return isEmpty;
	}
	
	public String getFieldValue (String fieldName)
	{
		return fieldValues.get(fieldName);
	}
	
	public String[] getKeywords ()
	{
		String[] res = new String[keywords.size()];
		res = keywords.toArray(res);
		return res;
	}
	
	public String[] getAuthors()
	{
		String[] res = new String[authors.size()];
		res = authors.toArray(res);
		return res;
	}
	
	public static String getFieldType (String fieldName)
	{
		return fieldList.get(fieldName);
	}
	
	
	// This return list of every fields except author and keyword
	public static String[] getFieldList()
	{
		return fieldNames;
	}
}
