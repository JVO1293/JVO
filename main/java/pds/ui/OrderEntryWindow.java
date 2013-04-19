package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.Stage;
import main.java.pds.PhoneOperator;
import main.java.pds.Pizza;
import main.java.pds.Menu;
import main.java.pds.TimeSystem;
import main.java.pds.Topping;

import java.awt.ScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class OrderEntryWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel labelMenu;
	private JButton buttonAdd;
	private JTable orderTable;
	private JScrollPane orderScrollPane;
	private JLabel priceLabel;
	private JLabel labelPrice;
	private JButton buttonRemove;
	private JButton buttonAddTopping;
	private JButton buttonFinish;
	private JLabel labelOrder;
	private JLabel labelOrderNumber;
	private JLabel labelOrder_1;
	private boolean isModifying = false;
	
	private DefaultTableModel orderTableModel = new DefaultTableModel();
	

	/**
	 * Create the frame.
	 */
	public OrderEntryWindow(final Stage startStage, final Menu menu, 
			final PhoneOperator phoneOperator, final Customer customer, 
			final PhoneOperatorWindow operatorWindow) {
		final Order lastOrder = phoneOperator.getLastOrder(customer.getPhoneNumber());
		final Order currentOrder = phoneOperator.getCurrentOrderForCustomer(customer, true);
		final ArrayList< OrderItem > newOrderItems = new ArrayList< OrderItem >();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 637, 403);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 53, 263, 266);
		contentPane.add(scrollPane);
		
		table = new JTable(new MenuTableModel(menu));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// disable moving of columns in tables
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		
		labelMenu = new JLabel("Menu");
		labelMenu.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelMenu.setHorizontalAlignment(SwingConstants.CENTER);
		labelMenu.setBounds(133, 25, 61, 16);
		contentPane.add(labelMenu);
		
		buttonAdd = new JButton("Add");
		buttonAdd.setBounds(34, 331, 117, 29);
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int selectedMenuRow = table.getSelectedRow();
				if (selectedMenuRow != -1) {
					Food selectedFood = menu.getFoodItems().get(selectedMenuRow);
					OrderItem newItem = currentOrder.addFood(menu.instantiateFood(selectedFood));
					newOrderItems.add(newItem);
					orderTableModel.addRow(new Object[] { newItem, "$" + selectedFood.getPrice() });
				}
			}
		});
		contentPane.add(buttonAdd);
		
		orderScrollPane = new JScrollPane();
		orderScrollPane.setBounds(337, 53, 263, 238);
		contentPane.add(orderScrollPane);
		
		orderTableModel = new DefaultTableModel() {
		    public boolean isCellEditable(int row, int column) {
		        return false;
		     }
		};
		orderTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent arg0) {
				if (priceLabel == null) return;
				double totalPrice = 0.0;
				
				for (int i = 0; i < orderTableModel.getRowCount(); ++i) {
					Food f = ((OrderItem)orderTableModel.getValueAt(i, 0)).getFood();
					totalPrice += f.getPrice();
				}
				priceLabel.setText("$" + Double.toString(totalPrice));
			}
		});
		orderTableModel.setColumnCount(2);
		orderTableModel.setColumnIdentifiers(new Object[] { "Item", "Price" });
		orderTable = new JTable(orderTableModel);
		orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// disable moving of columns in tables
		orderTable.getTableHeader().setReorderingAllowed(false);
		orderScrollPane.setViewportView(orderTable);
		
		priceLabel = new JLabel("$0.00");
		priceLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		priceLabel.setBounds(542, 303, 61, 16);
		contentPane.add(priceLabel);
		
		labelPrice = new JLabel("Total:");
		labelPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		labelPrice.setBounds(469, 303, 61, 16);
		contentPane.add(labelPrice);
		
		buttonRemove = new JButton("Remove");
		buttonRemove.setForeground(Color.RED);
		buttonRemove.setBounds(486, 331, 117, 29);
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int selectedRow = orderTable.getSelectedRow();
				if (selectedRow != -1) {
					OrderItem item = (OrderItem)orderTableModel.getValueAt(selectedRow, 0);
					if (phoneOperator.removeOrderItem(item)) {
						orderTableModel.removeRow(selectedRow);
						newOrderItems.remove(item);
					}
				}
			}
		});
		contentPane.add(buttonRemove);
		
		buttonAddTopping = new JButton("Add Topping");
		buttonAddTopping.setBounds(357, 331, 117, 29);
		buttonAddTopping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Food food = null;
				final int row = orderTable.getSelectedRow();
				OrderItem item = row >= 0 ? (OrderItem)orderTableModel.getValueAt(row, 0) : null; 
				if (row != -1 && item.getFood() instanceof Pizza) {
					final Pizza pizza = (Pizza)item.getFood();
					final ToppingsDialog toppingsDialog = new ToppingsDialog(menu, pizza.getToppingPrice(), pizza.getToppings());
					toppingsDialog.setOkButtonActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							ArrayList<Topping> toppings= toppingsDialog.getToppings();
							pizza.setToppings(toppings);
							
							// update price shown in table
							orderTableModel.setValueAt("$" + pizza.getPrice(), row, 1);
							
						}
					});
					OrderEntryWindow.this.setVisible(false);
					toppingsDialog.addWindowListener(new WindowAdapter(){
						@Override
						public void windowClosing( WindowEvent closeEvent){
							OrderEntryWindow.this.setVisible(true);
						}
					});
					toppingsDialog.setVisible(true);
				}
			}
		});
		contentPane.add(buttonAddTopping);
		
		buttonFinish = new JButton("Finish");
		buttonFinish.setForeground(Color.BLUE);
		buttonFinish.setBounds(228, 331, 117, 29);
		buttonFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!isModifying && currentOrder.getOrderItems().size() == 0) {
					return;
				}
				//estimate time
				phoneOperator.estimateTime(currentOrder);
				double estimatedTime = currentOrder.getEstimatedTime();
				int days =(int) estimatedTime/60/24;
				int hours =(int) estimatedTime/60 %24;
				int min =(int) estimatedTime %60;

				
				if (!isModifying && currentOrder.getOrderItems().size() > 0) {
					JFrame popUp = new JFrame();
					JOptionPane.showMessageDialog(popUp, "Time until food is ready is: " + days + "d:" + hours + "h:" + min+"m", 

						"Order Sent", JOptionPane.PLAIN_MESSAGE);
				}
				
				currentOrder.setTimeCreated(TimeSystem.getCurrentTime());
				for(OrderItem item : newOrderItems) {
					startStage.addOrderItem(item);
				}
				
				WindowEvent windowClosing = new WindowEvent(OrderEntryWindow.this, WindowEvent.WINDOW_CLOSING);
				OrderEntryWindow.this.dispatchEvent(windowClosing);
			}
		});
		contentPane.add(buttonFinish);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				operatorWindow.setVisible(true);
			}
		});
		
		labelOrder = new JLabel("Order");
		labelOrder.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelOrder.setBounds(337, 26, 61, 16);
		contentPane.add(labelOrder);
		
		labelOrderNumber = new JLabel(Integer.toString(currentOrder.getOrderId()));
		labelOrderNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		labelOrderNumber.setBounds(572, 26, 31, 16);
		contentPane.add(labelOrderNumber);
		
		labelOrder_1 = new JLabel("Order #");
		labelOrder_1.setHorizontalAlignment(SwingConstants.TRAILING);
		labelOrder_1.setBounds(499, 26, 61, 16);
		contentPane.add(labelOrder_1);
		
		// Fill out preferences if needed
		if (lastOrder != null && lastOrder != currentOrder) {
			for (OrderItem item : lastOrder.getOrderItems()) {
				OrderItem newItem = currentOrder.addFood(item.getFood().copy());
				newOrderItems.add(newItem);
			}
		}
		
		isModifying = lastOrder == currentOrder;
		
		// Add the items to the order (that were either just created from the 
		// preference, or because you are editing an order.
		for (OrderItem item :currentOrder.getOrderItems()) {
			orderTableModel.addRow(new Object[] { item, "$" + item.getFood().getPrice() });
		}
	}
}
