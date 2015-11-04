import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class EzJava {
	public static void main(String[] args) {
		String urlPrefix = "jdbc:db2:";
		String url;
		String user;
		String password;
		String empNo;
		Connection con;
		Statement stmt;
		ResultSet rs;
		System.out.println("**** Enter class EzJava");

		String[] tmp = { "//localhost:50000/MYDB", "db2admin", "1" };

		url = urlPrefix + tmp[0];
		user = tmp[1];
		password = tmp[2];
		try {
			// Load the driver
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			System.out.println("**** Loaded the JDBC driver");
			// Create the connection using the IBM Data Server Driver for JDBC
			// and SQLJ
			con = DriverManager.getConnection(url, user, password);
			// Commit changes manually
			con.setAutoCommit(false);
			// System.out
			// .println("**** Created a JDBC connection to the data source");
			// // Create the Statement
			stmt = con.createStatement();
			// System.out.println("**** Created JDBC Statement object");
			// // Execute a query and generate a ResultSet instance
			// rs = stmt.executeQuery("SELECT * FROM cochrane");
			// System.out.println("**** Created JDBC ResultSet objecf");
			// // Print all of the employee numbers to standard output device
			// while (rs.next()) {
			// empNo = rs.getString(1);
			// System.out.println("Employee number = " + empNo);
			// }
			// System.out.println("**** Fetched all rows from JDBC ResultSet");
			// // Close the ResultSet
			// rs.close();
			// System.out.println("**** Closed JDBC ResultSet");
			// // Close the Statement
			// stmt.close();
			// System.out.println("**** Closed JDBC Statement");
			// Connection must be on a unit-of-work boundary to allow close

			Map<String, String> dataMap = new Hashtable<String, String>();
			ArrayList<String> dataList = new ArrayList<String>();
			try {
				File file = new File("data_files/Cochrane/cochrane.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer stringBuffer = new StringBuffer();
				String line;

				while ((line = bufferedReader.readLine()) != null) {

					if (line.contains(":")) {
						String[] tmpStr = line.split(":", 2);
						String key = tmpStr[0];
						String value = tmpStr[1];

						if (dataMap.containsKey(key) == true)
							value = dataMap.get(key).concat("|" + value);

						dataMap.put(key, value);

					}
				}

				for (String key : dataMap.keySet()) {
					dataList.add(dataMap.get(key));
				}

				System.out.println(dataList.size());

				fileReader.close();
				// System.out.println("Contents of file:");
				// System.out.println(stringBuffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// String insertTableSQL = "INSERT INTO COCHRANE"
			// + "(ID, AU,TI,SO,YR,VL,NO,PG,PM,PT,KY,DOI,AB,US) VALUES"
			// + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String insertTableSQL = "INSERT INTO article2"

					+ "(ARTICLE_ID, CIT,REF,AU_CORR,TITLE,JNL_TIT,VOL,ISSUE,YEAR,PG_ST,PG_EN,LANG,ABS,PUBID,DOI,URL"
					+ ",PUBTYPE,FUND,RATE,SCR,FNLRATE,FNLDESIGN,MCNRATE,MCNCONF) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement preparedStatement = con
					.prepareStatement(insertTableSQL);

			// String insertTableSQL = "INSERT INTO COCHRANE" + "(ID) VALUES"
			// + "(?)";
			// PreparedStatement preparedStatement = con
			// .prepareStatement(insertTableSQL);

			// preparedStatement.setString(1, "ss");

			// dataList.get(i)

			for (int i = 1; i < 25; i++) {
				// String.valueOf(i) +
				// System.out.println(dataList.get(i));
				preparedStatement.setString(i , "w");
			}
			// dataList.get(i)
			preparedStatement.executeUpdate();
			//
			// try {
			//
			// ArrayList<String> pudMedDataList = new ArrayList<String>();
			//
			// File fXmlFile = new File("pudmed.xml");
			// DocumentBuilderFactory dbFactory = DocumentBuilderFactory
			// .newInstance();
			// DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			// Document doc = dBuilder.parse(fXmlFile);
			//
			// // optional, but recommended
			// // read this -
			// //
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			// doc.getDocumentElement().normalize();
			//
			// System.out.println("Root element :"
			// + doc.getDocumentElement().getNodeName());
			//
			// NodeList nList = doc.getElementsByTagName("PubmedArticle");
			//
			// System.out.println("----------------------------");
			//
			// for (int i = 0; i < nList.getLength(); i++) {
			//
			// Node nNode = nList.item(i);
			//
			// // System.out.println("\nCurrent Element :" +
			// // nNode.getNodeName());
			//
			// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			//
			// Element eElement = (Element) nNode;
			//
			// // System.out.println("Staff id : "
			// // + eElement.getAttribute("id"));
			//
			// String PMIDStr = eElement.getElementsByTagName("PMID")
			// .item(0).getTextContent();
			//
			// // System.out.println("PMID : " + PMIDStr);
			//
			// String VolumeStr = eElement
			// .getElementsByTagName("Volume").item(0)
			// .getTextContent();
			//
			// // System.out.println("Volume : " + VolumeStr);
			//
			// String Issue = eElement.getElementsByTagName("Issue")
			// .item(0).getTextContent();
			//
			// // System.out.println("Issue : " + Issue);
			//
			// String PubDate = eElement
			// .getElementsByTagName("PubDate").item(0)
			// .getTextContent();
			//
			// PubDate = PubDate.replaceAll("\n", "|");
			// PubDate = PubDate.replaceAll(" ", "");
			//
			// // System.out.println("PubDate : " + PubDate);
			//
			// String ArticleTitle = eElement
			// .getElementsByTagName("ArticleTitle").item(0)
			// .getTextContent();
			//
			// // System.out.println("ArticleTitle : " + ArticleTitle);
			//
			// String MedlinePgn = eElement
			// .getElementsByTagName("MedlinePgn").item(0)
			// .getTextContent();
			//
			// // System.out.println("MedlinePgn : " + MedlinePgn);
			//
			// String ELocationID = eElement
			// .getElementsByTagName("ELocationID").item(0)
			// .getTextContent();
			//
			// // System.out.println("ELocationID : " + ELocationID);
			//
			// String Language = eElement
			// .getElementsByTagName("Language").item(0)
			// .getTextContent();
			//
			// // System.out.println("Language : " + Language);
			//
			// NodeList PublicationTypeListNodeList = eElement
			// .getElementsByTagName("PublicationTypeList");
			//
			// String publicationType = "";
			//
			// for (int j = 0; j < PublicationTypeListNodeList
			// .getLength(); j++) {
			// Node PublicationTypeListNode = PublicationTypeListNodeList
			// .item(j);
			// // System.out.println("\nCurrent Element :");
			// // System.out.println(PublicationTypeListNode
			// // .getNodeName());
			// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			// Element PublicationTypeListElement = (Element)
			// PublicationTypeListNode;
			//
			// NodeList PublicationTypeNodeList = PublicationTypeListElement
			// .getElementsByTagName("PublicationType");
			//
			// for (int k = 0; k < PublicationTypeNodeList
			// .getLength(); k++) {
			// String tmpStr = PublicationTypeNodeList
			// .item(k).getTextContent();
			// // System.out.println("tmpStr       " +
			// // tmpStr);
			//
			// publicationType = publicationType
			// .concat(tmpStr + "|");
			//
			// }
			// }
			// }
			//
			// // System.out.println("publicationType : "
			// // + publicationType);
			//
			// String authorStr = "";
			//
			// NodeList authorNodeList = eElement
			// .getElementsByTagName("AuthorList");
			//
			// for (int j = 0; j < authorNodeList.getLength(); j++) {
			// Node authorNode = authorNodeList.item(j);
			// // System.out.println("\nCurrent Element :");
			// // System.out.println(authorNode.getNodeName());
			// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			// Element authorElement = (Element) authorNode;
			//
			// NodeList LastNameNodeList = authorElement
			// .getElementsByTagName("LastName");
			// NodeList ForeNameNodeList = authorElement
			// .getElementsByTagName("ForeName");
			// for (int count = 0; count < LastNameNodeList
			// .getLength(); count++) {
			// Node lastNameNode = LastNameNodeList
			// .item(count);
			// Node foreNameNode = ForeNameNodeList
			// .item(count);
			// if (lastNameNode.getNodeType() == lastNameNode.ELEMENT_NODE) {
			// Element lastNameElement = (Element) lastNameNode;
			// Element foreNameElement = (Element) foreNameNode;
			//
			// String tmp2 = lastNameElement
			// .getTextContent()
			// + "_"
			// + foreNameElement
			// .getTextContent() + "|";
			//
			// authorStr = authorStr.concat(tmp2);
			//
			// // System.out.println("Last and fore Name : "+tmp);
			//
			// }
			// }
			// }
			// }
			//
			// // System.out.println("authorStr : " + authorStr);
			//
			// String MedlineTA = eElement
			// .getElementsByTagName("MedlineTA").item(0)
			// .getTextContent();
			//
			// // System.out.println("MedlineTA : " + MedlineTA);
			//
			// String Title = eElement.getElementsByTagName("Title")
			// .item(0).getTextContent();
			//
			// String meshHeading = "";
			//
			// NodeList MeshHeadingNodeList = eElement
			// .getElementsByTagName("MeshHeading");
			//
			// for (int j = 0; j < MeshHeadingNodeList.getLength(); j++) {
			// Node MeshHeadingNode = MeshHeadingNodeList.item(j);
			// // System.out.println("\nCurrent Element :");
			// // System.out.println(MeshHeadingNode.getNodeName());
			// // System.out.println(MeshHeadingNode.getTextContent());
			//
			// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			// Element MeshHeadingElement = (Element) MeshHeadingNode;
			//
			// NodeList DescriptorNameNodeList = MeshHeadingElement
			// .getElementsByTagName("DescriptorName");
			//
			// for (int count = 0; count < DescriptorNameNodeList
			// .getLength(); count++) {
			// Node DescriptorNameNode = DescriptorNameNodeList
			// .item(count);
			//
			// if (DescriptorNameNode.getNodeType() ==
			// DescriptorNameNode.ELEMENT_NODE) {
			// Element DescriptorNameElement = (Element) DescriptorNameNode;
			//
			// String attr = "MajorTopicYN";
			// String attrValue = DescriptorNameElement
			// .getAttribute("MajorTopicYN");
			// String content = DescriptorNameElement
			// .getTextContent();
			//
			// // System.out
			// // .println("DescriptorNameAttribute : "
			// // + DescriptorNameElement
			// // .getAttribute("MajorTopicYN"));
			// // System.out
			// // .print("DescriptorNameElement : ");
			// // System.out.println(content);
			// String con1 = attrValue + "," + content
			// + "|";
			//
			// meshHeading = meshHeading.concat(con1);
			//
			// }
			// }
			// }
			// }
			//
			// // System.out.println("Title : " + Title);
			//
			// // System.out.println("meshHeading : " + meshHeading);
			//
			// String ArticleId = eElement
			// .getElementsByTagName("ArticleId").item(0)
			// .getTextContent();
			//
			// // System.out.println("ArticleId : " + ArticleId);
			//
			// String insertTableSQL2 = "INSERT INTO PUDMED"
			// +
			// "(PMID,Volume,Issue,PubDate ,ArticleTitle ,MedlinePgn  ,ELocationID ,Language ,publicationType  ,authorList  ,MedlineTA ,Title ,meshHeading,ArticleId ) VALUES"
			// + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// PreparedStatement preparedStatement2 = con
			// .prepareStatement(insertTableSQL2);
			//
			// pudMedDataList.add(PMIDStr);
			// pudMedDataList.add(VolumeStr);
			// pudMedDataList.add(Issue);
			// pudMedDataList.add(PubDate);
			// pudMedDataList.add(ArticleTitle);
			// pudMedDataList.add(MedlinePgn);
			// pudMedDataList.add(ELocationID);
			// pudMedDataList.add(Language);
			// pudMedDataList.add(publicationType);
			// pudMedDataList.add(authorStr);
			// pudMedDataList.add(MedlineTA);
			// pudMedDataList.add(Title);
			// pudMedDataList.add(meshHeading);
			// pudMedDataList.add(ArticleId);
			//
			// for (int j = 0; j < pudMedDataList.size(); j++) {
			// // String.valueOf(i) +
			// // System.out.println(dataList.get(j));
			// preparedStatement2.setString(j + 1,
			// pudMedDataList.get(j));
			// }
			// // dataList.get(i)
			// preparedStatement2.executeUpdate();
			// }
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			// preparedStatement.setFloat(2, 123);

			// for (String key : dataMap.keySet()) {
			// System.out
			// .println("------------------------------------------------");
			// System.out.println("key: " + key + " value: "
			// + dataMap.get(key));
			// }

			// execute insert SQL stetement
			// preparedStatement .executeUpdate();

			// Statement st = con.createStatement();
			// ResultSet rset = st.executeQuery("SELECT * FROM COCHRANE");
			// ResultSetMetaData md = rset.getMetaData();
			// for (int i=1; i<=md.getColumnCount(); i++)
			// {
			// System.out.println(md.getColumnLabel(i));
			// }

			// DatabaseMetaData md = con.getMetaData();
			// ResultSet rs1 = md.getTables(null, null, "%", null);
			// while (rs1.next()) {
			// System.out.println(rs1.getString(3));
			// }

			// String catalog = null;
			// String schemaPattern = null;
			// String tableNamePattern = "COCHRANE";
			// String columnNamePattern = null;
			//
			// DatabaseMetaData databaseMetaData = con.getMetaData();
			// ResultSet result = databaseMetaData.getColumns(catalog,
			// schemaPattern, tableNamePattern, columnNamePattern);
			//
			// while (result.next()) {
			// String columnName = result.getString(4);
			// System.out.println(columnName);
			// int columnType = result.getInt(5);
			//
			// }

			con.commit();
			System.out.println("**** Transaction committed");
			// Close the connection
			con.close();
			System.out.println("**** Disconnected from data source");

			System.out.println("**** JDBC Exit from class EzJava - no errors");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load JDBC driver");
			System.out.println("Exception: " + e);
			e.printStackTrace();
		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException(); // For drivers that support chained
											// exceptions

			}
		}
	} // End main
} // End EzJava