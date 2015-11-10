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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GuiManager {

	class Eavesdropper implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.out.println("hello");
		}
	}

	public void showAuthorTable(HashMap<String, String> requestedData) {

		final int size = 23;

		JFrame baseContainer = new JFrame();
		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Vector<String> keyVector = new Vector<String>();
		Vector<JTextField> valueVector = new Vector<JTextField>();

		int i = 0;
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
			i++;

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

				HashMap<String, String> updateMap = new HashMap<String, String>();

				for (int k = 0; k < keyVectorCopy.size(); k++) {
					updateMap.put(keyVectorCopy.get(k), valueVectorCopy.get(k)
							.getText());

				}
				QueryFunctions.updateArticleTable("1", updateMap);

			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;
		baseContainer.add(okButton, c);

		// for (int i = 0; i < size; ++i) {
		// c.gridy = i;
		//
		// // Create the slider
		// JLabel field = new JLabel("IDd:");
		//
		// c.fill = GridBagConstraints.CENTER;
		// c.gridx = 0;
		// c.weightx = 1;
		// baseContainer.add(field, c);
		//
		// // Create the checkbox
		//
		// // Create the current label
		// JTextField value = new JTextField();
		// value.setBorder(BorderFactory.createLineBorder(Color.red));
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.gridx = 1;
		// c.weightx = 8;
		// baseContainer.add(value, c);
		// }

		//

		baseContainer.setSize(1000, 500); // set frame size
		baseContainer.setVisible(true); //

	}
}
