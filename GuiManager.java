import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import org.jdesktop.xswingx.PromptSupport;

import com.google.common.collect.ListMultimap;

public class GuiManager {

	static final String PUDMED = "Pubmed";
	static final String COCHRANE = "Cochrane";

	public void showMainPage() {
		JFrame mainFrame = new JFrame();

	}

	public void showListGUI(ArrayList<dataCollection> dataCollectionArray) {

		final JPanel baseContainer = new JPanel();

		JScrollPane JScrollPaneBase = new JScrollPane(baseContainer);
		JScrollPaneBase
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame jframe = new JFrame();
		jframe.add(JScrollPaneBase);

		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		for (dataCollection data : dataCollectionArray) {
			LinkedHashMap<String, String> articleMap = data.articleMap;
			List<String> keywordMap = data.keywordMap;
			List<String> authorMap = data.authorMap;

			String strs = "";
			for (String str : articleMap.keySet()) {
				String value = articleMap.get(str);
				if (str.equals("ABS") == false && value != null)
					strs = strs + str + ": " + articleMap.get(str) + ", ";
			}

			String msg = strs + "//Author: " + authorMap.toString()
					+ "//Keyword: " + keywordMap.toString();
			System.out.println("msg: " + msg);
			JTextField value = new JTextField(msg);
			Font f = value.getFont();
			Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 3);
			value.setFont(f2);
			Border bd1 = new EmptyBorder(10, 10, 10, 10);
			value.setBorder(bd1);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			baseContainer.add(value, c);

		}

		jframe.setSize(1000, 500);
		jframe.setVisible(true); //

	}

	public void finalRatingGui(String article_id, String title,
			String abstract1, String keywords, String authors, String volumn,
			String pages, String year, String first, String second) {
		final JFrame baseContainer = new JFrame();
		baseContainer.setLayout(new GridLayout(6, 1, 5, 5));

		// baseContainer.setLayout(new GridBagLayout());
		// GridBagConstraints c = new GridBagConstraints();

		Border softBevelBorder = new SoftBevelBorder(BevelBorder.RAISED,
				Color.RED, Color.RED.darker(), Color.PINK,
				Color.PINK.brighter());

		JPanel JPanel1 = new JPanel();
		JLabel Jtitle = new JLabel("title: " + title);
		JPanel1.add(Jtitle);

		JLabel JkeyWords = new JLabel("keywords: " + keywords);

		JPanel JPanel3 = new JPanel();
		JLabel Jauthors = new JLabel("authors: " + authors);
		JPanel3.add(Jauthors);

		JPanel JPanel4 = new JPanel();
		JLabel JJournal = new JLabel("vol: " + volumn + ",pages: " + pages
				+ ",year: " + year);
		JPanel4.add(JJournal);

		JLabel JfirstValue = new JLabel("first rate: " + first);
		JLabel JSecondValue = new JLabel("second rate: " + second);
		final JTextField JFinalValue = new JTextField();
		PromptSupport.setPrompt("final rate", JFinalValue);

		final int index = Integer.valueOf(article_id);

		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = JFinalValue.getText();
				System.out.printf(text);
				QueryFunctions.updateArticleFinalRate(index, text);
				int maxArticleId = QueryFunctions.getMaxArticleId();
				for (int i = index + 1; i < maxArticleId; i++)
					if (showFinalRatingGui(String.valueOf(i))) {

						baseContainer.setVisible(false);
						baseContainer.dispose();
						// System.out.println("exist");
						break;
					}

			}
		};

		JFinalValue.addActionListener(action);

		JPanel JPanel5 = new JPanel();
		JPanel5.setLayout(new GridLayout(1, 3, 5, 5));
		JPanel5.add(JfirstValue);
		JPanel5.add(JSecondValue);
		JPanel5.add(JFinalValue);

		JTextArea textArea = new JTextArea(5, 20);
		textArea.setText(abstract1);
		textArea.setEditable(false);
		// textArea.setEnabled(false);
		textArea.setLineWrap(true);

		Font f = textArea.getFont();
		Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 2);

		Jtitle.setFont(f2);
		textArea.setFont(f2);
		JkeyWords.setFont(f2);
		Jauthors.setFont(f2);
		JJournal.setFont(f2);

		JScrollPane keywordsScrollPane = new JScrollPane(JkeyWords);
		keywordsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane abstractScrollPane = new JScrollPane(textArea);
		textArea.setEditable(true);

		abstractScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Border bd1 = new EmptyBorder(10, 100, 10, 100);
		Border bd2 = BorderFactory.createLineBorder(Color.black);
		keywordsScrollPane.setBorder(new CompoundBorder(bd1, bd2));
		abstractScrollPane.setBorder(new CompoundBorder(bd1, bd2));
		JPanel1.setBorder(new CompoundBorder(bd1, bd2));
		JPanel3.setBorder(new CompoundBorder(bd1, bd2));
		JPanel4.setBorder(new CompoundBorder(bd1, bd2));
		JPanel5.setBorder(new CompoundBorder(bd1, bd2));

		baseContainer.add(JPanel1);
		baseContainer.add(abstractScrollPane);
		baseContainer.add(keywordsScrollPane);
		baseContainer.add(JPanel3);
		baseContainer.add(JPanel4);
		baseContainer.add(JPanel5);

		baseContainer.setSize(1000, 500);
		baseContainer.setVisible(true); //

	}

	public boolean showFinalRatingGui(String article_value) {
		String searchTable3 = "KEYWORD";
		String searchField3 = "ARTICLE_ID";

		ListMultimap<String, String> keywordData = QueryFunctions
				.searchAuthorOrKeyword(searchTable3, searchField3,
						article_value);

		String searchTable = "ARTICLE3";
		String searchField = "ARTICLE_ID";

		LinkedHashMap<String, String> articleData = QueryFunctions
				.searchArticle(searchTable, searchField, article_value);

		String searchTable2 = "AUTHOR";
		String searchField2 = "ARTICLE_ID";

		ListMultimap<String, String> authorData = QueryFunctions
				.searchAuthorOrKeyword(searchTable2, searchField2,
						article_value);

		String ARTICLE_ID = articleData.get("ARTICLE_ID");
		String TITLE = articleData.get("TITLE");
		String VOL = articleData.get("VOL");
		String ISSUE = articleData.get("ISSUE");
		String YEAR = articleData.get("YEAR");
		String ABS = articleData.get("ABS");
		String RATE1 = articleData.get("RATE1");
		String RATE2 = articleData.get("RATE2");
		String RATEFINAL = articleData.get("FNLRATE");

		List<String> tmp = keywordData.get("KEYWORD");
		String KEYWORD = tmp.toString();

		List<String> tmp2 = authorData.get("AU_FULL");

		String AUTHOR = tmp2.toString();

		if (ARTICLE_ID != null && RATEFINAL == null) {
			finalRatingGui(article_value, TITLE, ABS, KEYWORD, AUTHOR, VOL,
					ISSUE, YEAR, RATE1, RATE2);
			return true;
		} else
			return false;
	}

	public void showAuthorTable(LinkedHashMap<String, String> requestedData) {

		final JFrame baseContainer = new JFrame();

		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Vector<String> keyVector = new Vector<String>();
		Vector<JTextField> valueVector = new Vector<JTextField>();

		for (final String key : requestedData.keySet()) {
			// System.out.println(key + ": " + requestedData.get(key));
			// c.gridy = i;

			keyVector.add(key);

			JButton field = new JButton(key + ":	");

			// field.addMouseListener(new MouseAdapter() {
			// public void mousePressed(MouseEvent me) {
			// System.out.println(me);
			// }
			// });

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			baseContainer.add(field, c);

			final String v1 = requestedData.get(key);
			JTextField value = new JTextField(v1);
			value.setBorder(BorderFactory.createLineBorder(Color.red));
			valueVector.add(value);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			baseContainer.add(value, c);

			field.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					JTextArea textArea = new JTextArea(5, 20);

					textArea.setText(v1);

					textArea.setLineWrap(true);

					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setEditable(true);

					scrollPane
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane.setPreferredSize(new Dimension(250, 250));

					//
					JFrame textFrame = new JFrame();
					textFrame.setSize(500, 500);
					textFrame.add(scrollPane);
					textFrame.setVisible(true);

				}
			});

		}

		JButton okButton = new JButton("ok	");

		final Vector<String> keyVectorCopy = (Vector<String>) keyVector.clone();

		final Vector<JTextField> valueVectorCopy = (Vector<JTextField>) valueVector
				.clone();

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("dd");
				LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();

				for (int k = 0; k < keyVectorCopy.size(); k++) {
					updateMap.put(keyVectorCopy.get(k), valueVectorCopy.get(k)
							.getText());

				}
				QueryFunctions.updateArticleTable(updateMap);

			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		baseContainer.add(okButton, c);

		JButton openButton = new JButton("loadData");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser("data_files");
				chooser.setMultiSelectionEnabled(true);
				int option = chooser.showOpenDialog(baseContainer);
				if (option == JFileChooser.APPROVE_OPTION) {
					File[] sf = chooser.getSelectedFiles();

					for (int i = 0; i < sf.length; i++) {

						String path = sf[i].getAbsolutePath();
						// System.out.println(path);

						if (path.contains(PUDMED)) {
							DataLoading.loadDataFromPudmed(path);

						} else if (path.contains(COCHRANE)) {

							DataLoading.loadDataFromCochrane(path);
						}
					}
				}
			}
		});

		baseContainer.add(openButton, c);

		baseContainer.setSize(1000, 500);
		baseContainer.setVisible(true); //

	}

	public void searchGUI() {

		final JPanel basePanel = new JPanel();
		JScrollPane keywordsScrollPane = new JScrollPane(basePanel);
		keywordsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JFrame baseFrame = new JFrame();
		baseFrame.add(keywordsScrollPane);

		basePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Vector<String> keyVector = new Vector<String>();
		Vector<JTextField> valueVector = new Vector<JTextField>();

		try {
			Statement st = DatabaseConnection.conn.createStatement();

			ResultSet rset = (ResultSet) st.executeQuery("SELECT * FROM "
					+ "article3");
			ResultSetMetaData md = rset.getMetaData();

			Vector<String> fieldInArticleTable = new Vector<String>();
			for (int i = 1; i <= md.getColumnCount(); i++) {

				fieldInArticleTable.add(md.getColumnLabel(i));
			}

			for (final String key : fieldInArticleTable) {

				keyVector.add(key);

				JButton field = new JButton(key + ":	");

				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.weightx = 1;
				basePanel.add(field, c);

				JTextField value = new JTextField();
				value.setBorder(BorderFactory.createLineBorder(Color.red));
				valueVector.add(value);

				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 1;
				c.weightx = 8;
				basePanel.add(value, c);

			}

			keyVector.add("AU_FULL");

			JButton authorField = new JButton("AU_FULL" + ":	");

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			basePanel.add(authorField, c);

			JTextField authorValue = new JTextField();
			authorValue.setBorder(BorderFactory.createLineBorder(Color.red));
			valueVector.add(authorValue);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			basePanel.add(authorValue, c);

			// ///keyword

			keyVector.add("KEYWORD");

			JButton keywordField = new JButton("KEYWORD" + ":	");

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			basePanel.add(keywordField, c);

			JTextField keywordValue = new JTextField();
			keywordValue.setBorder(BorderFactory.createLineBorder(Color.red));
			valueVector.add(keywordValue);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			basePanel.add(keywordValue, c);

			// ////////

			JButton okButton = new JButton("search	");

			final Vector<String> keyVectorCopy = (Vector<String>) keyVector
					.clone();

			final Vector<JTextField> valueVectorCopy = (Vector<JTextField>) valueVector
					.clone();

			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String str = "";
					for (int k = 0; k < valueVectorCopy.size(); k++) {
						str = str + valueVectorCopy.get(k).getText();
					}

					if (QueryFunctions.isWhitespace(str) == false) {
						LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();

						for (int k = 0; k < keyVectorCopy.size(); k++) {
							updateMap.put(keyVectorCopy.get(k), valueVectorCopy
									.get(k).getText());

						}
						ArrayList<dataCollection> dataResultset = QueryFunctions
								.searchJoinTable(updateMap);

						System.out.println("searchAllTable.size()"
								+ dataResultset.size());

						if (dataResultset != null)
							showListGUI(dataResultset);
					}
				}
			});

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			basePanel.add(okButton, c);

			baseFrame.setSize(1000, 500);
			baseFrame.setVisible(true); //

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
