package Rating;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class RatingScreen extends JFrame {
	// Area for GUI parameters - Begin
	private int viewBeginX = 20;
	private int viewBeginY = 20;
	private int rowGap = 20;
	private int columnGap = 30;
	private int rowHeight = 40;
	private int rateWidth = 60;
	private int titleWidth = 300;
	private int frameWidth = 800;
	private int frameHeight = 600;
	// Area for GUI parameters - End
	
	// Area for Rate value constants - Begin
	public static final int S_VALUE = 0;
	public static final int D_VALUE = 1;
	public static final int Q_VALUE = 2;
	public static final int A_VALUE = 3;
	// Area for Rate value constants - End
	
	
	private BriefArticleData data;
	private int[] rateValues;
	private int beginActiveRecords;
	private int no_activeRecords;
	
	private int no_row = 5;
	
	private JTextField[] rateBoxes;
	private JTextField[] titleBoxes;
	
	public RatingScreen()
	{
		initUI();
	}
	
	private void loadData()
	{
		
	}
	
	private void initUI()
	{
		this.setSize(frameWidth, frameHeight);
		this.getContentPane().setLayout(null);
		rateBoxes = new JTextField[no_row];
		titleBoxes = new JTextField[no_row];
		
		for (int iRow = 0; iRow< no_row; iRow++)
		{
			// Create one row
			int xRate = viewBeginX;
			int yRate = viewBeginY + iRow*(rowHeight + rowGap);
			rateBoxes[iRow] = new JTextField();
			rateBoxes[iRow].setSize(new Dimension(rateWidth, rowHeight));
			rateBoxes[iRow].setLocation(xRate, yRate);
			
			int yTitle = yRate;
			int xTitle = rateBoxes[iRow].getX() + rateBoxes[iRow].getWidth() + columnGap;
			titleBoxes[iRow] = new JTextField();
			titleBoxes[iRow].setSize(new Dimension(titleWidth, rowHeight));
			titleBoxes[iRow].setLocation(xTitle, yTitle);
			
			this.getContentPane().add(rateBoxes[iRow]);
			this.getContentPane().add(titleBoxes[iRow]);
		}
	}
	
	private void refreshUI()
	{
		
	}
	
	private class RatingEntered implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			String strKey = Character.toString(arg0.getKeyChar()).toUpperCase();
			char key = strKey.charAt(0);
			switch (key)
			{
				case 'S':
				{
					
				} break;
				case 'D':
				{
					
				} break;
				case 'Q':
				{
					
				} break;
				case 'A':
				{
					
				} break;
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
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
