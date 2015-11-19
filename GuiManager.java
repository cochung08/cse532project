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
import java.util.LinkedHashMap;
import java.util.Vector;

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
import javax.swing.border.EmptyBorder;

public class GuiManager {

	static final String PUDMED = "Pubmed";
	static final String COCHRANE = "Cochrane";

	public void showMainPage() {
		JFrame mainFrame = new JFrame();

	}

	public void showFinalRatingTable(String title, String abstract1,
			String keywords, String authors, String volumn, String pages,
			String year, String first, String second) {
		final JFrame baseContainer = new JFrame();
		baseContainer.setLayout(new GridLayout(6, 1, 5, 5));
		// baseContainer.setLayout(new GridBagLayout());
		// GridBagConstraints c = new GridBagConstraints();

		JPanel JPanel1 = new JPanel();
		JLabel Jtitle = new JLabel("title: " + title);
		JPanel1.add(Jtitle);
		//JPanel1.setBorder(new EmptyBorder(30, 60, 30, 60));
		

		JPanel JPanel2 = new JPanel();
		JLabel JkeyWords = new JLabel("keywords: " + keywords);

		// JPanel2.add(JkeyWords);

		JScrollPane keywordsScrollPane = new JScrollPane(JkeyWords);
		keywordsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel JPanel3 = new JPanel();
		JLabel Jauthors = new JLabel("authors: " + authors);
		JPanel3.add(Jauthors);

		JPanel JPanel4 = new JPanel();
		JLabel JJournal = new JLabel("vol: " + volumn + ",pages: " + pages
				+ ",year: " + year);
		JPanel4.add(JJournal);

		JLabel JfirstValue = new JLabel("first value: " + first);
		JLabel JSecondValue = new JLabel("second value: " + second);
		JTextField JFinalValue = new JTextField("finalCode");

		JPanel JPanel5 = new JPanel();
		JPanel5.setLayout(new GridLayout(1, 3, 5, 5));
		// JPanel3.setBorder(new EmptyBorder(30, 60, 30, 60));
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
		textArea.setFont(f2);
		JkeyWords.setFont(f2);

		JScrollPane abstractScrollPane = new JScrollPane(textArea);
		textArea.setEditable(true);

		abstractScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane.setPreferredSize(new Dimension(250, 250));

		baseContainer.add(JPanel1);
		baseContainer.add(abstractScrollPane);
		baseContainer.add(keywordsScrollPane);
		baseContainer.add(JPanel3);
		baseContainer.add(JPanel4);
		baseContainer.add(JPanel5);

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 0;
		// c.weightx = 1;

		// JPanel jPanel1 = new JPanel();
		// jPanel1.setBorder(new EmptyBorder(30, 60, 30, 60));
		// JTextField jTitle = new JTextField("ddd");
		// jPanel1.add(jTitle);
		//
		// baseContainer.add(jPanel1, c);
		//
		// JPanel jPanel2 = new JPanel();
		// jPanel2.setBorder(new EmptyBorder(30, 60, 30, 60));
		// JTextField jAbstract = new JTextField("aaaaa");
		// jPanel2.add(jAbstract);
		//
		// baseContainer.add(jPanel2, c);
		//
		// JPanel jPanel3 = new JPanel();
		// jPanel3.setBorder(new EmptyBorder(30, 60, 30, 60));
		// JLabel jKeywords = new JLabel("aaaaa");
		// jPanel3.add(jKeywords);
		//
		// baseContainer.add(jPanel3, c);

		// JTextArea textArea = new JTextArea(5, 20);
		//
		// textArea.setText("ssssss");
		//
		// textArea.setLineWrap(true);
		//
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 0;
		// c.weightx = 1;
		// baseContainer.add(textArea, c);

		baseContainer.setSize(1000, 500);
		baseContainer.setVisible(true); //

		// JPanel idPanel = new JPanel();
		// // idPanel.setLayout(new GridLayout(1, 5, 5, 0));
		// idPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
		// idPanel.add(textArea);

		// JPanel Row12Panel = new JPanel();
		// Row12Panel.setLayout(new GridLayout(2, 1, 5, 5));
		// Row12Panel.add(connectionButton);
		// Row12Panel.add(idPanel);
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

					// JTextField textField = new JTextField(10);
					// // textField.setSize(500, 500); // set frame size
					// // textField.setVisible(true);
					// textField.setText(v1);
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
}
