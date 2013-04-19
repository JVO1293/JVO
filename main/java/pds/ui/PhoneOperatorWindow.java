package main.java.pds.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import main.java.pds.Customer;
import main.java.pds.Menu;
import main.java.pds.Order;
import main.java.pds.OrderStage;
import main.java.pds.Stage;
import main.java.pds.PhoneOperator;

public class PhoneOperatorWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JLabel labelEmployee;
	private JLabel labelPleasePickAn;
	private JButton buttonPlacemodifyOrder;
	private JButton buttonViewOrders;
	private JButton buttonBackToWelcome;
	
	private WelcomeWindow welcomeWindow = null;
	private PhoneNumberDialog numberDialog = null;
	private PhoneOperator phoneOperator = null;
	private Customer customer = null;
	private JLabel labelCurrentCustomer;
	
	/**
	 * Create the frame.
	 */
	public PhoneOperatorWindow(WelcomeWindow welcomeWindow, PhoneOperator po, final Menu menu, final Stage startStage) {		
		this.welcomeWindow = welcomeWindow;
		this.phoneOperator = po;
		
		final ActionListener returnToWelcomeWindow = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numberDialog.setVisible(false);
				PhoneOperatorWindow.this.setVisible(false);
				PhoneOperatorWindow.this.welcomeWindow.setVisible(true);
			}
		};
		
		
		ActionListener numberDialogOkButtonAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String number = numberDialog.getPhoneNumber();
				if (number.matches("(\\d{3}-){2}\\d{4}") == false) return;
				
				Customer c = phoneOperator.getCustomerForPhoneNumber(numberDialog.getPhoneNumber());
				if (c != null) {
					PhoneOperatorWindow.this.setCustomer(c);
					numberDialog.setVisible(false);
					PhoneOperatorWindow.this.setVisible(true);
				}
				else {
					final NewCustomerDialog newCustomerDialog = new NewCustomerDialog();
					
					ActionListener newCustomerOkAction = new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							if (newCustomerDialog.getCustomerName().length() == 0) {
								return;
							}
							
							Customer c = new Customer(numberDialog.getPhoneNumber(), newCustomerDialog.getCustomerName(), newCustomerDialog.getCustomerLocation());
							phoneOperator.addCustomer(c);
							PhoneOperatorWindow.this.setCustomer(c);
							newCustomerDialog.setVisible(false);
							PhoneOperatorWindow.this.setVisible(true);
						}
					};
					
					ActionListener newCustomerCancelAction = new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							newCustomerDialog.setVisible(false);
							returnToWelcomeWindow.actionPerformed(event);
						}
					};
					
					newCustomerDialog.setOkButtonActionListener(newCustomerOkAction);
					newCustomerDialog.setCancelButtonActionListener(newCustomerCancelAction);
					numberDialog.setVisible(false);
					newCustomerDialog.setVisible(true);
				}
			}
		};
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		labelEmployee = new JLabel("Employee");
		contentPane.add(labelEmployee);
		labelEmployee.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
		labelEmployee.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelPleasePickAn = new JLabel("Please pick an option:");
		labelPleasePickAn.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(labelPleasePickAn);
		labelPleasePickAn.setHorizontalAlignment(SwingConstants.CENTER);
		labelPleasePickAn.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		labelCurrentCustomer = new JLabel("");
		labelCurrentCustomer.setVerticalAlignment(SwingConstants.BOTTOM);
		labelCurrentCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelCurrentCustomer);
		
		panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		buttonPlacemodifyOrder = new JButton("Place/Modify Order");
		buttonPlacemodifyOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				PhoneOperatorWindow.this.setVisible(false);
				OrderEntryWindow orderEntryWindow = new OrderEntryWindow(startStage, menu, phoneOperator, customer, PhoneOperatorWindow.this);
				orderEntryWindow.setVisible(true);
			}
		});
		panel.add(buttonPlacemodifyOrder);
		
		buttonViewOrders = new JButton("View Orders");
		buttonViewOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ViewCustomerOrders dialog = new ViewCustomerOrders(phoneOperator, customer);
				dialog.setVisible(true);
			}
		});
		panel.add(buttonViewOrders);
		
		buttonBackToWelcome = new JButton("Back to Welcome Window");
		buttonBackToWelcome.addActionListener(returnToWelcomeWindow);
		panel.add(buttonBackToWelcome);
		
		// launch phone number dialog
		numberDialog = new PhoneNumberDialog(numberDialogOkButtonAction, returnToWelcomeWindow);
	}
	
	public void present() {
		numberDialog.setVisible(true);
	}
	
	private void setCustomer(Customer customer) {
		this.customer = customer;
		labelCurrentCustomer.setText(customer.toString());
	}
}
