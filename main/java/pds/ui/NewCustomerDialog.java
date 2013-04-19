package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;

import main.java.pds.Location;
import java.awt.Font;
import java.awt.event.ActionListener;

public class NewCustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTextField;
	private JComboBox locationComboxBox;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public NewCustomerDialog() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("New Customer");
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 213);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(3, 0, 0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			{
				JLabel labelNewCustomer = new JLabel("New Customer");
				labelNewCustomer.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				panel.add(labelNewCustomer);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignOnBaseline(true);
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel);
			{
				JLabel labelName = new JLabel("Name:");
				labelName.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(labelName);
			}
			{
				nameTextField = new JTextField();
				panel.add(nameTextField);
				nameTextField.setColumns(20);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel);
			{
				JLabel labelNewLabel = new JLabel("Location:");
				panel.add(labelNewLabel);
			}
			{
				locationComboxBox = new JComboBox(Location.values());
				panel.add(locationComboxBox);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void setOkButtonActionListener(ActionListener al) {
		okButton.addActionListener(al);
	}
	
	public void setCancelButtonActionListener(ActionListener al) {
		cancelButton.addActionListener(al);
	}

	public String getCustomerName() {
		return nameTextField.getText();
	}
	
	public Location getCustomerLocation() {
		return (Location)locationComboxBox.getSelectedItem();
	}
}
