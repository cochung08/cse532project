package Rating;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class DetailInfoPanel extends JDialog
{
	// Area for GUI parameters - Begin
	private int viewBeginX1 = 20;
	private int viewBeginX2 =200;
	private int viewBeginY = 20;
	private int label_width = 200;
	private int text_width = 800;
	private int ta_height = 200;
	private int tf_height = 50;
	private int label_height = tf_height;
	private int row_gap = 20;
	// Area for GUI parameters - End
	
	public static final int S_CHOSEN = 0;
	public static final int Q_CHOSEN = 1;
	public static final int D_CHOSEN = 2;
	private int decision = -1;
	
	JTextArea txt_abs;
	JTextArea txt_title;
	JTextArea txt_au;
	JTextArea txt_jnl;
	JLabel lb_abs;
	JLabel lb_title;
	JLabel lb_au;
	JLabel lb_jnl;
	
	JScrollPane scr_abs;
	
	KeyEntered kenter;
	
	private JFrame parent;
	private Font font;
	
	public DetailInfoPanel (JFrame _parent, Point _pos, Font _font)
	{
		kenter = new KeyEntered();
		
		this.setLayout(null);
		this.parent = _parent;
		this.font = _font;
		this.getContentPane().setBackground(new Color(190, 190, 190));
		initUI();
		this.setLocationRelativeTo(parent);
		this.setLocation(_pos);
		
		
	}
	
	public void setContent(String _abstract, String _title, String _author, String _journal)
	{
		txt_abs.setText(_abstract);
		txt_title.setText(_title);
		txt_au.setText(_author);
		txt_jnl.setText(_journal);
	}
	
	private void initUI()
	{
		// This dialog
		this.setSize(new Dimension(1050, 450));
		this.addWindowFocusListener(new WindowFocusListener(){

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				DetailInfoPanel.this.setVisible(false);
			}
			
		});
		this.addKeyListener(kenter);
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				DetailInfoPanel.this.requestFocus();
			}
			
		});
		this.setUndecorated(true);
		
		// Text fields and text area
		txt_abs = new JTextArea();
		txt_abs.addKeyListener(kenter);
		txt_abs.setLineWrap(true);
		txt_abs.setFont(font);
		
		scr_abs = new JScrollPane(txt_abs);
		scr_abs.setSize(new Dimension(text_width, ta_height));
		scr_abs.setLocation(new Point(viewBeginX2, viewBeginY));
		this.getContentPane().add(scr_abs);
		
		txt_title = new JTextArea();
		txt_title.setSize(new Dimension(text_width, tf_height));
		txt_title.setLocation(new Point(viewBeginX2, scr_abs.getY() + scr_abs.getHeight() + row_gap));
		txt_title.setLineWrap(true);
		txt_title.addKeyListener(kenter);
		txt_title.setFont(font);
		this.getContentPane().add(txt_title);
		
		txt_au = new JTextArea();
		txt_au.setSize(new Dimension(text_width, tf_height));
		txt_au.setLocation(new Point(viewBeginX2, txt_title.getY() + txt_title.getHeight() + row_gap));
		txt_au.setLineWrap(true);
		txt_au.addKeyListener(kenter);
		txt_au.setFont(font);
		this.getContentPane().add(txt_au);
		
		txt_jnl = new JTextArea();
		txt_jnl.setSize(new Dimension(text_width, tf_height));
		txt_jnl.setLocation(new Point(viewBeginX2, txt_au.getY() + txt_au.getHeight() + row_gap));
		txt_jnl.setLineWrap(true);
		txt_jnl.addKeyListener(kenter);
		txt_jnl.setFont(font);
		this.getContentPane().add(txt_jnl);
		
		
		
		
		// Labels
		lb_abs = new JLabel();
		lb_abs.setText("Abstract");
		lb_abs.setSize(new Dimension(label_width, label_height));
		lb_abs.setLocation(viewBeginX1, viewBeginY);
		this.getContentPane().add(lb_abs);
		
		lb_title = new JLabel();
		lb_title.setText("Title");
		lb_title.setSize(new Dimension(label_width, label_height));
		lb_title.setLocation(viewBeginX1, scr_abs.getY() + scr_abs.getHeight() + row_gap);
		this.getContentPane().add(lb_title);
		
		lb_au = new JLabel();
		lb_au.setText("Author");
		lb_au.setSize(new Dimension(label_width, label_height));
		lb_au.setLocation(viewBeginX1, txt_title.getY() + txt_title.getHeight() + row_gap);
		this.getContentPane().add(lb_au);
		
		lb_jnl = new JLabel();
		lb_jnl.setText("Author");
		lb_jnl.setSize(new Dimension(label_width, label_height));
		lb_jnl.setLocation(viewBeginX1, txt_au.getY() + txt_au.getHeight() + row_gap);
		this.getContentPane().add(lb_jnl);
		
		
	}
	
	public int getDecision()
	{
		return decision;
	}
	
	private class KeyEntered implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == arg0.VK_ESCAPE)
			{
				DetailInfoPanel.this.setVisible(false);
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			String strKey = Character.toString(arg0.getKeyChar()).toUpperCase();
			char key = strKey.toUpperCase().charAt(0);
			
			switch (key)
			{
				case 'S':
				{
					decision = DetailInfoPanel.S_CHOSEN;
					DetailInfoPanel.this.setVisible(false);
					
				} break;
				case 'D':
				{
					decision = DetailInfoPanel.D_CHOSEN;
					DetailInfoPanel.this.setVisible(false);
					
				} break;
				case 'Q':
				{
					decision = DetailInfoPanel.Q_CHOSEN;
					DetailInfoPanel.this.setVisible(false);
					
				} break;
			}
			
		}
		
	}
}











