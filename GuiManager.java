import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GuiManager {

	public void showAuthorTable(HashMap<String, String> requestedData) {

		final int size = 23;

		JFrame baseContainer = new JFrame();
		baseContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int i = 0;
		for (String key : requestedData.keySet()) {
			// System.out.println(key + ": " + requestedData.get(key));
			c.gridy = i;

			JLabel field = new JLabel(key + ":	");

			c.fill = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.weightx = 1;
			baseContainer.add(field, c);

			JTextField value = new JTextField(requestedData.get(key));
			value.setBorder(BorderFactory.createLineBorder(Color.red));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 8;
			baseContainer.add(value, c);
			i++;

		}

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
