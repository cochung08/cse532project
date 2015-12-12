package Rating;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InformationPane extends JPanel
{	
	String user;
	int no_article;
	
	public JLabel lb;
	
	public InformationPane(String _usr, int _noAr)
	{
		this.setLayout(new BorderLayout());
		//this.setLayout(null);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		user = _usr;
		no_article = _noAr;
		
		lb = new JLabel();
		lb.setLocation(20,20);
		lb.setSize(new Dimension(100,100));
		lb.setHorizontalAlignment(SwingConstants.LEFT);
		lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(lb, BorderLayout.CENTER);
		//this.add(lb);
		
		String text = "<html>You are login as "+user+
						"<br>There are "+ no_article+ " articles displayed</html>";
		lb.setText(text);
	}
	
	public void updateInfo(String _usr, int _noAr)
	{
		user = _usr;
		no_article = _noAr;
		String text = "<html>You are login as "+user+
				"<br>There are "+ no_article+ " articles displayed</html>";	
		lb.setText(text);
	}
}
