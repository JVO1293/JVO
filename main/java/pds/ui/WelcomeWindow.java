package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import main.java.pds.Main;
import main.java.pds.Menu;
import main.java.pds.Stage;
import main.java.pds.PhoneOperator;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

public class WelcomeWindow extends JFrame {

	private JPanel contentPane;
	private JLabel labelWelcomeToThe;
	private JPanel panel;
	private JButton buttonEmployee;
	private JButton buttonManager;
	private JButton buttonExit;
	private JLabel labelPleasePickAn;

	PhoneOperatorWindow phoneOperatorWindow = null;
	ManagerWindow mw = new ManagerWindow();


	/**
	 * Create the frame.
	 */
	public WelcomeWindow(PhoneOperator phoneOperator, Menu menu, Stage startStage) {
		phoneOperatorWindow = new PhoneOperatorWindow(this, phoneOperator, menu, startStage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 632, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		labelWelcomeToThe = new JLabel("Welcome to the Pizza Delivery System!");
		labelWelcomeToThe.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
		labelWelcomeToThe.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelWelcomeToThe, BorderLayout.NORTH);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		buttonEmployee = new JButton("Employee");
		buttonEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				phoneOperatorWindow.present();
				WelcomeWindow.this.setVisible(false);
			}
		});
		panel.add(buttonEmployee);
		
		buttonManager = new JButton("Manager");
		buttonManager.addActionListener( new ActionListener () {
			public void actionPerformed(ActionEvent arg0) {
//<<<<<<< .mine
				mw.setVisible(true);
//				PDSViewManager.setWindowVisible(true); 
//				WelcomeWindow.this.setVisible(false);
			}
////=======
//				boolean visible = false;
//				PDSViewManager.initializeMainWindow(); 
//				WelcomeWindow.this.setVisible(false);
//				}
////>>>>>>> .r135
		});
		panel.add(buttonManager);
		
		buttonExit = new JButton("Exit");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				WindowEvent windowClosing = new WindowEvent(WelcomeWindow.this, WindowEvent.WINDOW_CLOSING);
				WelcomeWindow.this.dispatchEvent(windowClosing);			
			}
		});
		panel.add(buttonExit);
		
		labelPleasePickAn = new JLabel("Please pick an option:");
		labelPleasePickAn.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		labelPleasePickAn.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelPleasePickAn, BorderLayout.CENTER);
	}
}
