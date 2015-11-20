package Rating;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
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
	private int displayIndex;
	private int cursorIndex;
	
	private int no_row = 9;
	
	// Area of GUI components - Begin
	private JTextField[] rateBoxes;
	private JTextField[] titleBoxes;
	private JScrollBar vbar;
	private JTextField userBox;
	// Area of GUI components - End
	
	// Area of Event Handler - Begin
	private RatingEntered ratingEnt;
	private TextFieldEntered tfenter;
	private ScrollBarEvent scrollAdj;
	// Area of Event Handler - End
	
	public RatingScreen()
	{
		data = new ArrayList<ArticleInfo>();
		ratingEnt = new RatingEntered();
		tfenter = new TextFieldEntered();
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
		
		//loadData();
	}
	
	private void loadData()
	{
		getData();
		
		// Set values for scroll bar
		int nData = data.size();
		vbar.setMaximum(nData);
		vbar.setMinimum(0);
		vbar.getModel().setExtent(no_row);
		
		// Refresh user Interface
		refreshUI();
		
	}
	
	private void closeSession()
	{
		DatabaseManager.disconnectToDatabase();
	}
	
	private void initUI()
	{
		// This frame
		//this.pack();
		this.setPreferredSize(new Dimension(1400, 1200));
		this.pack();
		
		// Username text field
		userBox = new JTextField();
		userBox.setSize(new Dimension(80, 30));
		userBox.setLocation(new Point(20, 10));
		this.getContentPane().add(userBox);
		
		// Load data button
		JButton btn_load = new JButton();
		btn_load.setSize(new Dimension(90, 30));
		btn_load.setLocation(new Point(120, 10));
		btn_load.setText("Load Data");
		btn_load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				loadData();
			}
		});
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
			rateBoxes[iRow].addActionListener(tfenter);
			
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
		vbar.addAdjustmentListener(scrollAdj);
		vbar.setSize(new Dimension(20, no_row*(rowHeight + rowGap)));
		vbar.setLocation(viewBeginX + rateWidth + columnGap + titleWidth + 10, viewBeginY);
		vbar.setMaximum(0);
		vbar.setMinimum(0);
		this.getContentPane().add(vbar);
	}
	
	private void refreshUI()
	{
		int displayIndexEnd = Math.min(data.size() - 1, displayIndex + no_row - 1);
		for (int i = displayIndex; i<= displayIndexEnd; i++)
		{
			int boxIndex = i - displayIndex;
			
			if (data.get(i).getRate() != null)
			{
				rateBoxes[boxIndex].setText(data.get(i).getRate());
			}
			
			titleBoxes[boxIndex].setEditable(true);
			titleBoxes[boxIndex].setText(data.get(i).getValue("title"));
			titleBoxes[boxIndex].setEditable(false);
		}
	}
	
	
	private class ScrollBarEvent implements AdjustmentListener
	{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			// TODO Auto-generated method stub
			displayIndex = vbar.getValue();
			System.out.println(displayIndex);
			System.out.println(vbar.getModel().getExtent());
			refreshUI();
		}
	}
	
	private class TextFieldEntered implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			// Write value to 
			
			if (cursorIndex < no_row - 1)
			{
				cursorIndex++;
			}
			else	// This is the last row
			{
				// Check if this is the end of all records
				if (vbar.getValue() + vbar.getModel().getExtent() == data.size())	// The end of all records
				{
					// Do nothing
				}
				else	// Load the next data page
				{
					for (int i = 0; i< no_row; i++)
					{
						rateBoxes[i].setText("");
						titleBoxes[i].setText("");
					}
					int new_bar_value = Math.min(vbar.getValue() + no_row, data.size() - 1);
					vbar.setValue(new_bar_value);
					cursorIndex = 0;
				}
			}
			rateBoxes[cursorIndex].requestFocus();
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
	
	private void getData()
	{
		try
		{
			// Doing query
			DatabaseManager.connectToDatabase();
			String username = userBox.getText();
			if (username.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Please enter username");
				return;
			}
			String temp_usr = "'" + username +"'";
			String query = "SELECT article_id, abs, title, year, rate1, rate_person1, rate2, rate_person2 from article2"
					+ " where (rate2 is null) or "
					+ "((rate_person1 = "+temp_usr+") and (rate1 = 'Q')) or ((rate_person2 = "+temp_usr+") and (rate2 = 'Q'))";
			ResultSet rs = DatabaseManager.query(query);
			String[] cols = new String[] {"abs", "title", "year"};
			while (rs.next())
			{
				ArticleInfo ar = new ArticleInfo();
				
				// Set article id
				ar.setID(rs.getInt("article_id"));
				
				for (int i = 0; i< cols.length; i++)
				{
					String temp = rs.getString(cols[i]);
					ar.addValue(cols[i], temp);
				}
				
				// Set rating information
				String rate1 = rs.getString("rate1");
				String rate2 = rs.getString("rate2");
				String rate_person1 = rs.getString("rate_person1");
				String rate_person2 = rs.getString("rate_person2");
				if (rate_person2 != null)
				{
					
				}
				
				
				//ar.setRate1FromDB(rate1);
				//ar.setRate2FromDB(null);			// Obviously, the 2nd rate should be null
				
				
				
				data.add(ar);
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
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
