package Rating;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
	//private int 
	// Area for GUI parameters - End
	
	JTextArea txt_abs;
	JTextField txt_title;
	JTextField txt_au;
	JTextField txt_jnl;
	JLabel lb_abs;
	JLabel lb_title;
	JLabel lb_au;
	JLabel lb_jnl;
	
	private JFrame parent;
	
	public DetailInfoPanel (JFrame _parent, Point _pos)
	{
		this.setLayout(null);
		this.parent = _parent;
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
		this.setSize(new Dimension(1200, 600));
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
		this.setUndecorated(true);
		
		// Text fields and text area
		txt_abs = new JTextArea();
		txt_abs.setWrapStyleWord(true);
		txt_abs.setSize(new Dimension(text_width, ta_height));
		txt_abs.setLocation(new Point(viewBeginX2, viewBeginY));
		this.getContentPane().add(txt_abs);
		
		txt_title = new JTextField();
		txt_title.setSize(new Dimension(text_width, tf_height));
		txt_title.setLocation(new Point(viewBeginX2, txt_abs.getY() + txt_abs.getHeight() + row_gap));
		this.getContentPane().add(txt_title);
		
		txt_au = new JTextField();
		txt_au.setSize(new Dimension(text_width, tf_height));
		txt_au.setLocation(new Point(viewBeginX2, txt_title.getY() + txt_title.getHeight() + row_gap));
		this.getContentPane().add(txt_au);
		
		txt_jnl = new JTextField();
		txt_jnl.setSize(new Dimension(text_width, tf_height));
		txt_jnl.setLocation(new Point(viewBeginX2, txt_au.getY() + txt_au.getHeight() + row_gap));
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
		lb_title.setLocation(viewBeginX1, txt_abs.getY() + txt_abs.getHeight() + row_gap);
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
}











