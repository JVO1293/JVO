package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PhoneNumberDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	public PhoneNumberDialog(ActionListener okListener, ActionListener cancelListener) {
		setTitle("Customer's Phone Number");
		setResizable(false);
		setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setBounds(100, 100, 450, 125);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel labelNewLabel = new JLabel("Enter Customer's Phone Number:");
			contentPanel.add(labelNewLabel);
		}
		{
			textField = new JTextField();
			textField.setText("555-555-5555");
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(okListener);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(cancelListener);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String getPhoneNumber() {
		return textField.getText();
	}

}
