package Rating;
import java.awt.EventQueue;

import javax.swing.*;

public class RatingScreen extends JFrame {
	
	
	public RatingScreen()
	{
		initUI();
	}
	
	private void initUI()
	{
		setTitle("Rating Screen");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] agrs)
	{
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				RatingScreen rt = new RatingScreen();
				rt.setVisible(true);
			}
		});
	}
}
