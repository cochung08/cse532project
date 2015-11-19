package Rating;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

public class RatingScreen extends JFrame {
	// Area for GUI parameters - Begin
	private int viewBeginX = 20;
	private int viewBeginY = 100;
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
	
	
	private ArrayList<ArticleInfo> data;
	private int[] rateValues;
	private int displayIndex;
	
	private int no_row = 5;
	
	private JTextField[] rateBoxes;
	private JTextField[] titleBoxes;
	private JScrollBar vbar;
	
	// Area of Event Handler - Begin
	private RatingEntered ratingEnt;
	private ScrollBarEvent scrollAdj;
	// Area of Event Handler - End
	
	public RatingScreen()
	{
		ratingEnt = new RatingEntered();
		scrollAdj = new ScrollBarEvent();
		initUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
	            {
					closeSession();
					e.getWindow().dispose();
	            }
			});
	}
	
	private void loadData()
	{
		DatabaseManager.connectToDatabase();
		String query = "SELECT abs, title, year";
		DatabaseManager.query(query, new String[]{"abs", "title", "year"});
		
		int nData = data.size();
		vbar.setMaximum(nData - no_row + 1);
		vbar.setMinimum(1);
	}
	
	private void closeSession()
	{
		DatabaseManager.disconnectToDatabase();
	}
	
	private void initUI()
	{
		// Load data
		JButton btn_load = new JButton();
		btn_load.setSize(new Dimension(90, 40));
		btn_load.setLocation(new Point(20, 10));
		btn_load.setText("Load Data");
		this.getContentPane().add(btn_load);
		
		// Information boxes
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
			rateBoxes[iRow].addKeyListener(ratingEnt);
			
			int yTitle = yRate;
			int xTitle = rateBoxes[iRow].getX() + rateBoxes[iRow].getWidth() + columnGap;
			titleBoxes[iRow] = new JTextField();
			titleBoxes[iRow].setSize(new Dimension(titleWidth, rowHeight));
			titleBoxes[iRow].setLocation(xTitle, yTitle);
			titleBoxes[iRow].setEditable(false);
			
			this.getContentPane().add(rateBoxes[iRow]);
			this.getContentPane().add(titleBoxes[iRow]);
		}
		
		vbar = new JScrollBar(JScrollBar.VERTICAL);
		
	}
	
	private void refreshUI()
	{
		int displayIndexEnd = Math.min(data.size() - 1, displayIndex + no_row - 1);
		for (int i = displayIndex; i< displayIndexEnd; i++)
		{
			int boxIndex = i - displayIndex;
			titleBoxes[boxIndex].setEditable(true);
			titleBoxes[boxIndex].setText(data.get(i).getValue("title"));
			titleBoxes[boxIndex].setEditable(false);
		}
	}
	
	private void moveCursorToNextRow()
	{
		
	}
	
	private class ScrollBarEvent implements AdjustmentListener
	{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			// TODO Auto-generated method stub
			displayIndex = vbar.getValue();
			refreshUI();
		}
		
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
					moveCursorToNextRow();
				} break;
				case 'D':
				{
					moveCursorToNextRow();
				} break;
				case 'Q':
				{
					moveCursorToNextRow();
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
