package Rating;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RatingScreen extends JFrame {
	// Area for GUI parameters - Begin
	private int viewBeginX = 20;
	private int viewBeginY = 100;
	private int rowGap = 20;
	private int columnGap = 30;
	private int rowHeight = 40;
	private int rateWidth = 60;
	private int titleWidth = 300;
	private int frameWidth = 1200;
	private int frameHeight = 500;
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
	private JButton btn_load;
	private JButton btn_save;
	// Area of GUI components - End
	
	// Area of Event Handler - Begin
	private RatingEntered ratingEnt;
	private TextFieldEntered tfenter;
	private ScrollBarEvent scrollAdj;
	// Area of Event Handler - End
	
	public RatingScreen()
	{
		// This is used to receive resize event only when it is finished (do not need to update continuously)
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		this.setSize(frameWidth, frameHeight);
		this.getContentPane().setLayout(null);

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
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				initUI();
				refreshUI();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		// Login
		String s = (String)JOptionPane.showInputDialog(null, "Input your username", JOptionPane.OK_CANCEL_OPTION);
		if (s == null || s.isEmpty())
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		userBox.setText(s);
		userBox.setEditable(false);
		loadData();
		
		//loadData();
	}
	
	private void loadData()
	{
		getData();
		
		// Refresh user Interface
		refreshUI();
		
	}
	
	private void saveData()
	{
		String rateperson = userBox.getText();
		for (int i = 0; i< data.size(); i++)
		{
			ArticleInfo af = data.get(i);
			if (af.isUpdated())
			{
				String perCol = "";
				String rateCol = "";
				if (af.getActiveRate() == 1)
				{
					perCol = "RATE_PERSON1";
					rateCol = "RATE1";
				}
				else
				{
					perCol = "RATE_PERSON2";
					rateCol = "RATE2";
				}
				
				String sql = "UPDATE ARTICLE2 SET " +rateCol+ "='" +af.getRate() +
						"',"+ perCol+ "='" + rateperson + "' WHERE ARTICLE_ID = " + Integer.toString(af.getID());
				DatabaseManager.update(sql);
				
				data.remove(i);
				i--;
			}
		}
		
		refreshUI();
	}
	
	private void closeSession()
	{
		DatabaseManager.disconnectToDatabase();
	}
	
	private void initUI()
	{
		// Remove all old components.
		this.getContentPane().removeAll();
		
		
		
		// Username text field
		userBox = new JTextField();
		userBox.setSize(new Dimension(80, 30));
		userBox.setLocation(new Point(20, 10));
		this.getContentPane().add(userBox);
		
		// Load data button
		btn_load = new JButton();
		btn_load.setSize(new Dimension(90, 30));
		btn_load.setLocation(new Point(120, 10));
		btn_load.setText("Load Data");
		btn_load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				loadData();
			}
		});
		//this.getContentPane().add(btn_load);
		
		// Save data button
		btn_save = new JButton();
		btn_save.setSize(new Dimension(btn_load.getWidth(), btn_load.getHeight()));
		btn_save.setLocation(btn_load.getX() + btn_load.getWidth() + 20, btn_load.getY());
		btn_save.setText("Save");
		btn_save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveData();
				
			}
			
		});
		this.getContentPane().add(btn_save);
		
		// Information boxes
		no_row = (this.getHeight() - (viewBeginY + 50))/(rowHeight + rowGap);
		rateBoxes = new JTextField[no_row];
		titleBoxes = new JTextField[no_row];
		titleWidth = (int) (this.getWidth() - (viewBeginX + rateWidth + columnGap + 70));
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
			rateBoxes[iRow].setName(Integer.toString(iRow));
			rateBoxes[iRow].addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					JTextField source = (JTextField)(arg0.getSource());
					cursorIndex = Integer.parseInt(source.getName());
				}
				
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
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

		vbar.getModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				BoundedRangeModel src = (BoundedRangeModel)arg0.getSource();
				displayIndex = src.getValue();
			}
			
		});
	}
	
	private void refreshUI()
	{
		// Set values for scroll bar
		int nData = data.size();
		vbar.setMaximum(nData);
		vbar.setMinimum(0);
		vbar.getModel().setExtent(no_row);
				
		int displayIndexEnd = Math.min(data.size() - 1, displayIndex + no_row - 1);
		for (int i = displayIndex; i<= displayIndex + no_row - 1; i++)
		{
			int boxIndex = i - displayIndex;
			if (i <= displayIndexEnd)
			{
				
				
				if (data.get(i).getRate() != null)
				{
					rateBoxes[boxIndex].setText(data.get(i).getRate());
				}
				else
				{
					rateBoxes[boxIndex].setEditable(true);
					rateBoxes[boxIndex].setText("");
				}
				
				titleBoxes[boxIndex].setEditable(true);
				titleBoxes[boxIndex].setText(data.get(i).getValue("title"));
				titleBoxes[boxIndex].setEditable(false);
			}
			else
			{
				rateBoxes[boxIndex].setText("");
				rateBoxes[boxIndex].setEditable(false);
				titleBoxes[boxIndex].setEditable(true);
				titleBoxes[boxIndex].setText("");
				titleBoxes[boxIndex].setEditable(false);
			}
			
		}
	}
	
	
	private class ScrollBarEvent implements AdjustmentListener
	{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			// TODO Auto-generated method stub
			if (displayIndex + no_row > data.size())
			{
				displayIndex = data.size() - no_row;
			}
			if (displayIndex < 0)
			{
				displayIndex = 0;
			}
			vbar.setValue(displayIndex);
			//displayIndex = vbar.getValue();
			//System.out.println(displayIndex);
			//System.out.println(vbar.getModel().getExtent());
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
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			String strKey = Character.toString(arg0.getKeyChar()).toUpperCase();
			char key = strKey.toUpperCase().charAt(0);
			JTextField source = (JTextField)(arg0.getSource());
			String finalKey = source.getText();
			int ar_id = cursorIndex + displayIndex;
			switch (key)
			{
				case 'S':
				{
					finalKey = "S";
					data.get(ar_id).setRateFromUser("S");
					tfenter.actionPerformed(null);
				} break;
				case 'D':
				{
					finalKey = "D";
					data.get(ar_id).setRateFromUser("D");
					tfenter.actionPerformed(null);
				} break;
				case 'Q':
				{
					finalKey = "Q";
					data.get(ar_id).setRateFromUser("Q");
					tfenter.actionPerformed(null);
				} break;
				case 'A':
				{
					int dlgX = (int) (source.getLocation().getX()+RatingScreen.this.getLocationOnScreen().getX() + rateWidth + 20);
					int dlgY = (int) (source.getLocation().getY()+RatingScreen.this.getLocationOnScreen().getY() + 50);
					ArticleInfo ar = data.get(displayIndex + cursorIndex);
					DetailInfoPanel dlg = new DetailInfoPanel(RatingScreen.this, 
							new Point(dlgX, dlgY));
							//SwingUtilities.convertPoint(source, new Point(source.getX(), source.getY()), RatingScreen.this));
					dlg.setModal(true);
					dlg.setContent(ar.getValue("abs"), ar.getValue("title"), "", "");
					dlg.setVisible(true);
					
					switch (dlg.getDecision())
					{
						case DetailInfoPanel.S_CHOSEN:
						{
							finalKey = "S";
							data.get(ar_id).setRateFromUser("S");
							tfenter.actionPerformed(null);
						} break;
						case DetailInfoPanel.D_CHOSEN:
						{
							finalKey = "D";
							data.get(ar_id).setRateFromUser("D");
							tfenter.actionPerformed(null);
						} break;
						case DetailInfoPanel.Q_CHOSEN:
						{
							finalKey = "Q";
							data.get(ar_id).setRateFromUser("Q");
							tfenter.actionPerformed(null);
						} break;
					}
					
				} break;
			}
			source.setText(finalKey);
			arg0.consume();
			return;
		}
	}
	
	public interface DialogCallback
	{
		void doS();
		void doD();
		void doQ();
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
					+ "(rate1 is null) or"
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
				//ar.setRateFromDB(rate1, 1);
				//ar.setRateFromDB(rate2, 2);
				
				
				
				boolean added = false;
				if (rate_person1 != null && rate_person1.equals(username))
				{
					if (rate1.equals("Q"))
					{
						ar.setRatePersonFromDB(rate_person1, 1);
						ar.setRateFromDB(rate1, 1);
						ar.setActiveRate(1);
						added = true;
					}
				}
				else
				{
					if (rate_person2 != null && rate_person2.equals(username))
					{
						if (rate2.equals("Q"))
						{
							ar.setRatePersonFromDB(rate_person2, 2);
							ar.setRateFromDB(rate2, 2);
							ar.setActiveRate(2);
							added = true;
						}
					}
					else
					{
						if (rate_person1 == null)
						{
							ar.setRatePersonFromDB(rate_person1, 1);
							ar.setRateFromDB(rate1, 1);
							ar.setActiveRate(1);
							added = true;
						}
						else
						{
							if (rate_person2 == null)
							{
								ar.setRatePersonFromDB(rate_person2, 2);
								ar.setRateFromDB(rate2, 2);
								ar.setActiveRate(2);
								added = true;
							}
						}
					}
				}
				
				
				//ar.setRate1FromDB(rate1);
				//ar.setRate2FromDB(null);			// Obviously, the 2nd rate should be null
				
				
				if (added == true)
				{
					data.add(ar);
				}
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
