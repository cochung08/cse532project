package Rating;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InstructionPane extends JPanel {
	
	public InstructionPane()
	{
		//this.setSize(new Dimension(400 300));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String content = "<html><pre>   S   Select the article"+
							"<br>   D   Delete the article"+
							"<br>   Q   Unclear article, review later"+
							"<br>   A   for more detail information</pre></html>";
		JLabel lb = new JLabel(content);
		lb.setHorizontalAlignment(SwingConstants.LEFT);
		lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lb.setLocation(20,20);
		lb.setSize(new Dimension(100,100));
		this.add(lb, BorderLayout.CENTER);
	}
}
