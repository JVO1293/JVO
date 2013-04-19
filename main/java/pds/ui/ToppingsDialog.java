package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import main.java.pds.Menu;
import main.java.pds.Topping;

public class ToppingsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable toppingsTable;
	private JScrollPane scrollPane;
	private JTable addedToppingsTable;
	private JButton buttonRemove;
	private JButton buttonAdd;
	private JButton okButton;
	private JRadioButton radioButtonWhole;
	private JRadioButton radioButtonLeft;
	private JRadioButton radioButtonRight;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private DefaultTableModel toppingTableModel = null;
	private DefaultTableModel addedToppingsTableModel = null;

	/**
	 * Create the dialog.
	 */
	public ToppingsDialog(final Menu menu, final double price, ArrayList< Topping > existingToppings) {
		setBounds(100, 100, 448, 337);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 217, 183);
		contentPanel.add(scrollPane);
		
		toppingTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
		        return false;
		     }
		};
		toppingTableModel.setColumnCount(2);
		toppingTableModel.setColumnIdentifiers(new String[] { "Topping", "Price" });
		toppingsTable = new JTable(toppingTableModel);
		toppingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// disable moving of columns in tables
		toppingsTable.getTableHeader().setReorderingAllowed(false);
		for (Topping topping : menu.getToppings()) {
			toppingTableModel.addRow(new Object[] { topping, "$" + Double.toString(price)});
		}
		
		scrollPane.setViewportView(toppingsTable);
		{
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(235, 6, 209, 183);
			contentPanel.add(scrollPane_1);
			{
				addedToppingsTableModel = new DefaultTableModel() {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				addedToppingsTableModel.setColumnCount(3);
				addedToppingsTableModel.setColumnIdentifiers(new String[] { "Topping", "Price", "Area" });
				for (Topping topping : existingToppings) {
					double p = topping.getCoverage() == Topping.Coverage.WHOLE ? price : price / 2.0;
					addedToppingsTableModel.addRow(new Object[] { topping, "$" + Double.toString(p), topping.getCoverage()});
				}
				addedToppingsTable = new JTable(addedToppingsTableModel);
				addedToppingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				// disable moving of columns in tables
				addedToppingsTable.getTableHeader().setReorderingAllowed(false);
				scrollPane_1.setViewportView(addedToppingsTable);
			}
		}
		
		buttonRemove = new JButton("Remove");
		buttonRemove.setBounds(356, 201, 86, 29);
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int row = addedToppingsTable.getSelectedRow();
				if (row != -1) {
					addedToppingsTableModel.removeRow(row);
				}
			}
		});
		contentPanel.add(buttonRemove);
		
		buttonAdd = new JButton("Add");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = toppingsTable.getSelectedRow();
				if (row != -1) {
					Topping topping = new Topping(menu.getToppings().get(row));
					topping.setCoverage(radioButtonWhole.isSelected() ?
										Topping.Coverage.WHOLE :
											(radioButtonLeft.isSelected() ?
											Topping.Coverage.LEFT_HALF :
											Topping.Coverage.RIGHT_HALF));
					
					// Remove existing topping of same type
					int i = 0;
					for (Topping existingTopping : ToppingsDialog.this.getToppings()) {
						if (existingTopping.getType().equals(topping.getType())) {
							addedToppingsTableModel.removeRow(i);
							break;
						}
						++i;
					}
					
					addedToppingsTableModel.addRow(new Object[] { 
						topping,
						"$" + Double.toString(topping.getCoverage() == Topping.Coverage.WHOLE ? price : price / 2.0),
						topping.getCoverage()
					});
				}
			}
		});
		buttonAdd.setBounds(6, 201, 96, 29);
		contentPanel.add(buttonAdd);
		
		radioButtonWhole = new JRadioButton("Whole");
		radioButtonWhole.setSelected(true);
		buttonGroup.add(radioButtonWhole);
		radioButtonWhole.setBounds(6, 242, 70, 23);
		contentPanel.add(radioButtonWhole);
		
		radioButtonLeft = new JRadioButton("Left");
		buttonGroup.add(radioButtonLeft);
		radioButtonLeft.setBounds(88, 242, 56, 23);
		contentPanel.add(radioButtonLeft);
		
		radioButtonRight = new JRadioButton("Right");
		buttonGroup.add(radioButtonRight);
		radioButtonRight.setBounds(156, 242, 70, 23);
		contentPanel.add(radioButtonRight);
		
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
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ToppingsDialog.this.closeWindow();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void setOkButtonActionListener(final ActionListener l) {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				l.actionPerformed(event);
				ToppingsDialog.this.closeWindow();
			}
		});
	}
	
	/**
	 * Get the toppings listed in the added toppings table.
	 * @return array list of toppings.
	 */
	public ArrayList< Topping > getToppings() {
		ArrayList< Topping > toppings = new ArrayList< Topping >();
		for (int i = 0; i < addedToppingsTableModel.getRowCount(); ++i) {
			toppings.add((Topping)addedToppingsTableModel.getValueAt(i, 0));
		}
		return toppings;
	}
	
	public void closeWindow() {
		WindowEvent windowClosing = new WindowEvent(ToppingsDialog.this, WindowEvent.WINDOW_CLOSING);
		this.dispatchEvent(windowClosing);
	}
}
