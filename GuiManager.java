import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.text.JTextComponent;

import org.jdesktop.xswingx.PromptSupport;

import com.google.common.base.Strings;
import com.google.common.collect.ListMultimap;

public class GuiManager {

	static final String PUDMED = "Pubmed";
	static final String COCHRANE = "Cochrane";

	public static void showMainPage() {
		JFrame mainFrame = new JFrame();
		final JPanel baseContainer = new JPanel();
		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 8;

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

		JButton searchButton = new JButton("search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				searchGUI();
			}
		});

		baseContainer.add(searchButton, c);

		JButton finalRatingButton = new JButton("finalRating");

		finalRatingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				int maxArticleId = QueryFunctions.getMaxArticleId();
				for (int i = 1; i <= maxArticleId; i++) {
					boolean result = GuiManager.finalRatingGui(String
							.valueOf(i));
					if (result == true) {
						System.out.println("result: " + i);
						break;
					}
				}

			}
		});

		baseContainer.add(finalRatingButton, c);

		mainFrame.add(baseContainer);
		mainFrame.setSize(1000, 500);
		mainFrame.setVisible(true);

		//

	}

	// public static void updateText()
	// {
	// JTextArea textArea = new JTextArea(5, 20);
	//
	// textArea.setText("2");
	//
	// textArea.setLineWrap(true);
	//
	// JScrollPane scrollPane = new JScrollPane(textArea);
	// textArea.setEditable(true);
	//
	// scrollPane
	// .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	// scrollPane.setPreferredSize(new Dimension(250, 250));
	//
	// //
	// JFrame textFrame = new JFrame();
	// textFrame.setSize(500, 500);
	// textFrame.add(scrollPane);
	//
	//
	//
	// JButton updateButton = new JButton("")
	//
	// textFrame.setVisible(true);
	// }

	public static void showsSearchListGUI(
			ArrayList<dataCollection> dataCollectionArray) {

		final JPanel baseContainer = new JPanel();

		JScrollPane JScrollPaneBase = new JScrollPane(baseContainer);
		JScrollPaneBase
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame jframe = new JFrame();
		jframe.add(JScrollPaneBase);

		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// ///////////
		final ArrayList<String> idList = new ArrayList<String>();

		for (dataCollection dataitem : dataCollectionArray) {
			String id = dataitem.articleMap.get("ARTICLE_ID");
			idList.add(id);
		}

		JButton exportButton = new JButton("export	");

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("export111");

				QueryFunctions.exportById(idList);

			}

		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		baseContainer.add(exportButton, c);
		// ///////////

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
			JTextField valueField = new JTextField(msg);
			Font f = valueField.getFont();
			Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 3);
			valueField.setFont(f2);
			Border bd1 = new EmptyBorder(10, 10, 10, 10);
			valueField.setBorder(bd1);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			baseContainer.add(valueField, c);

			final LinkedHashMap<String, String> articleDataCopy = (LinkedHashMap<String, String>) articleMap
					.clone();
			final List<String> keywordMapCopy = keywordMap;
			final List<String> authorMapCopy = authorMap;

			Action action = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {

					System.out.println("locate");
					showAllTable(articleDataCopy, keywordMapCopy, authorMapCopy);

				}
			};

			valueField.addActionListener(action);

		}

		jframe.setSize(1000, 500);
		jframe.setVisible(true); //

	}

	public static boolean finalRatingGui(String article_id) {

		LinkedHashMap<String, String> finalRatingData = QueryFunctions
				.getFinalRatingData(article_id);

		if (finalRatingData == null)
			return false;

		String title = finalRatingData.get("TITLE");
		String abstract1 = finalRatingData.get("ABS");
		String keywords = finalRatingData.get("KEYWORD");
		String authors = finalRatingData.get("AU_FULL");
		String volumn = finalRatingData.get("VOL");
		String pages = finalRatingData.get("ISSUE");
		String year = finalRatingData.get("YEAR");
		String first = finalRatingData.get("RATE1");
		String second = finalRatingData.get("RATE2");
		String finalRating = finalRatingData.get("FNLRATE");

		if (finalRating != null)
			return false;

		if (first != null && second != null && first.equals(second))
			return false;

		final JFrame baseContainer = new JFrame();
		baseContainer.setLayout(new GridLayout(6, 1, 5, 5));

		baseContainer.setLayout(new GridBagLayout());

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
		JFinalValue.addAncestorListener(new RequestFocusListener());

		final int index = Integer.valueOf(article_id);

		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = JFinalValue.getText();
				System.out.printf(text);
				QueryFunctions.updateArticleFinalRate(index, text);
				int maxArticleId = QueryFunctions.getMaxArticleId();
				for (int i = index + 1; i <= maxArticleId; i++) {
					boolean result = finalRatingGui(String.valueOf(i));
					if (result == true) {
						break;
					}
				}

				baseContainer.setVisible(false);
				baseContainer.dispose();

			}
		};

		JFinalValue.addActionListener(action);

		JPanel JPanel5 = new JPanel();
		JPanel5.setLayout(new GridLayout(1, 3, 5, 5));
		JPanel5.add(JfirstValue);
		JPanel5.add(JSecondValue);
		JPanel5.add(JFinalValue);

		JTextArea textArea = new JTextArea(20, 40);
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

		GridBagConstraints gbc = new GridBagConstraints();

		JButton jtitle = new JButton("title");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.weightx = 1;

		// baseContainer.add(jtitle, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 0, 0, 10);
		gbc.weightx = 1;
		baseContainer.add(JPanel1, gbc);

		JLabel lblAddress = new JLabel("Address");
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.weightx = 1;
		// baseContainer.add(lblAddress, gbc);

		JTextArea txtAreaAddress = new JTextArea(10, 20);
		JScrollPane pane = new JScrollPane(txtAreaAddress);
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(5, 0, 0, 10);
		gbc.weightx = 1;
		baseContainer.add(abstractScrollPane, gbc);

		JButton jtitle2 = new JButton("title");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.weightx = 1;

		// baseContainer.add(jtitle2, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.insets = new Insets(5, 0, 0, 10);
		gbc.weightx = 1;
		baseContainer.add(JPanel3, gbc);

		JButton jtitle3 = new JButton("title");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.weightx = 1;

		// baseContainer.add(jtitle3, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.insets = new Insets(5, 0, 0, 10);
		gbc.weightx = 1;
		baseContainer.add(JPanel4, gbc);

		JButton jtitle4 = new JButton("title");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.weightx = 1;

		// baseContainer.add(jtitle4, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.insets = new Insets(5, 0, 0, 10);
		gbc.weightx = 1;
		baseContainer.add(JPanel5, gbc);

		baseContainer.setSize(1500, 700);
		baseContainer.setExtendedState(JFrame.MAXIMIZED_BOTH);
		baseContainer.setVisible(true); //
		return true;

	}

	public static void showAllTable(LinkedHashMap<String, String> articleData,
			List<String> keywordList, List<String> authorList) {

		articleData.put("keyword", keywordList.toString());
		articleData.put("author", authorList.toString());

		final JPanel basePanel = new JPanel();
		JScrollPane keywordsScrollPane = new JScrollPane(basePanel);
		keywordsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JFrame baseFrame = new JFrame();
		baseFrame.add(keywordsScrollPane);

		basePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// final JFrame baseContainer = new JFrame();
		// baseContainer.setLayout(new GridBagLayout());
		// GridBagConstraints c = new GridBagConstraints();

		Vector<String> keyVector = new Vector<String>();
		Vector<JTextField> valueVector = new Vector<JTextField>();

		for (final String key : articleData.keySet()) {

			keyVector.add(key);

			JButton field = new JButton(key + ":	");

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			basePanel.add(field, c);

			final String v1 = articleData.get(key);
			final JTextField value = new JTextField(v1);
			value.setBorder(BorderFactory.createLineBorder(Color.black));
			valueVector.add(value);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			basePanel.add(value, c);

			field.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					final JFrame textFrame = new JFrame();

					final JTextArea textArea = new JTextArea(5, 20);

					textArea.setText(v1);

					textArea.setLineWrap(true);

					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setEditable(true);

					textArea.addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(KeyEvent e) {
							if (e.getKeyCode() == KeyEvent.VK_ENTER) {
								e.consume();
								value.setText(textArea.getText());
								textFrame.dispose();

							}
						}

						@Override
						public void keyTyped(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							// TODO Auto-generated method stub

						}
					});

					scrollPane
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane.setPreferredSize(new Dimension(250, 250));

					//

					textFrame.setSize(500, 500);
					textFrame.add(scrollPane);
					textFrame.setVisible(true);

				}
			});

		}

		final Vector<String> keyVectorCopy = (Vector<String>) keyVector.clone();
		final Vector<JTextField> valueVectorCopy = (Vector<JTextField>) valueVector
				.clone();

		JButton updateButton = new JButton("update	");

		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("update");

				LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();
				int size_ = keyVectorCopy.size();

				for (int i = 0; i < size_ - 2; i++) {
					System.out.println(keyVectorCopy.get(i) + "_"
							+ valueVectorCopy.get(i).getText());

					updateMap.put(keyVectorCopy.get(i), valueVectorCopy.get(i)
							.getText());
				}

				QueryFunctions.updateArticleTable(updateMap);

			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		basePanel.add(updateButton, c);

		// //////////////////
		JButton exportButton = new JButton("export	");

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("export");

				String article_id = valueVectorCopy.get(0).getText();

				ArrayList<String> idList = new ArrayList<String>();
				idList.add(article_id);

				QueryFunctions.exportById(idList);

			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		basePanel.add(exportButton, c);

		// exportById

		baseFrame.setSize(1000, 500);
		baseFrame.setVisible(true); //

	}

	public static void showAuthorTable(
			LinkedHashMap<String, String> requestedData) {

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
			value.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

	public static void searchGUI() {

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

				if (key.equals("TITLE") || key.equals("FNLRATE")
						|| key.equals("FNLDESIGN")) {
					keyVector.add(key);

					JButton field = new JButton(key + ":	");

					c.fill = GridBagConstraints.HORIZONTAL;
					c.gridx = 0;
					c.weightx = 1;
					basePanel.add(field, c);

					JTextField value = new JTextField();
					value.setBorder(BorderFactory.createLineBorder(Color.black));
					valueVector.add(value);

					c.fill = GridBagConstraints.HORIZONTAL;
					c.gridx = 1;
					c.weightx = 8;
					basePanel.add(value, c);
				}

			}

			keyVector.add("AU_FULL");

			JButton authorField = new JButton("AU_FULL" + ":	");

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.weightx = 1;
			basePanel.add(authorField, c);

			JTextField authorValue = new JTextField();
			authorValue.setBorder(BorderFactory.createLineBorder(Color.black));
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
			keywordValue.setBorder(BorderFactory.createLineBorder(Color.black));
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
							showsSearchListGUI(dataResultset);
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
