import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

public class GuiManager {

	static final String PUDMED = "Pubmed";
	static final String COCHRANE = "Cochrane";

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
