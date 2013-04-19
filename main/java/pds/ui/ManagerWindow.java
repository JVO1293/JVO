package main.java.pds.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Timer;

import main.java.pds.Main;
import main.java.pds.Order;

/**
 * The Manager's view window that displays order tracking information and the manager report.
 * 
 * @author jhaberstro
 */
public class ManagerWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTable orderTrackingTable;
	private JTable reportTable;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private Timer refreshTimer;
	
	private DefaultTableModel orderTrackingModel;
	private DefaultTableModel reportModel;

	DecimalFormat moneyFormatter = new DecimalFormat("$0.00");
	DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Create the frame.
	 */
	public ManagerWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 712, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		orderTrackingModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		orderTrackingModel.setColumnCount(8);
		orderTrackingModel.setColumnIdentifiers(new String[] {
			"Order #", "Customer Name", "Delivery Location", "Order Cost", "Time Order Taken", "Estimated Delivery Time", "Actual Delivery Time", "Status"
		});
		this.refreshOrderTrackingTable();
		orderTrackingTable = new JTable(orderTrackingModel);
		scrollPane.setViewportView(orderTrackingTable);
		
		scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1);
		
		reportModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reportModel.setColumnCount(3);
		this.refreshReportTable();
		reportTable = new JTable(reportModel);
		scrollPane_1.setViewportView(reportTable);
		
		refreshTimer = new Timer(250, this);
		refreshTimer.start();
	}
	
	/**
	 * The timer event-action.
	 */
	public void actionPerformed(ActionEvent event) {
		if (this.isVisible()) {
			this.refreshOrderTrackingTable();
			this.refreshReportTable();
		}
	}
	
	/**
	 * Set the row data for a particular table model
	 * @param model
	 * @param row
	 * @param rowData
	 */
	private void setRow(DefaultTableModel model, int row, Object[] rowData) {
		if (row >= model.getRowCount()) {
			model.addRow(rowData);
		}
		else {
			for (int i = 0; i < rowData.length; ++i) {
				model.setValueAt(rowData[i], row, i);
			}
		}
	}
	
	/**
	 * Update the data in the order tracking table
	 */
	private void refreshOrderTrackingTable() {

		ArrayList< Order > orders = Main.getOrdersList();
		
		int row = 0;
		for (Order order : orders) {
			//if (order.isOrderCompleted() == false) continue;
			Object[] rowData = new Object[] {
				order.getOrderId(),
				order.getCustomer().getName(),
				order.getCustomer().getAddress(),
				moneyFormatter.format(order.getPrice()),
				timeFormatter.format(order.getTimeCreated()),
				timeFormatter.format(order.getEstimatedTime()),
				timeFormatter.format(order.calculateTotalTime()),
				order.getCurrentStage()
			};
			
			setRow(orderTrackingModel, row, rowData);
			row += 1;
		}
	}
	
	/**
	 * Update the data in the manager report table.
	 */
	private void refreshReportTable() {
		ArrayList< Order > orders = Main.getOrdersList();
		int totalOrders = orders.size();
		
		setRow(reportModel, 0, new Object[] { "Total Number of Orders: " + totalOrders });
		
		if (totalOrders > 1) {
			double averageCost = 0.0;
			
			double maxPrepTime = -1.0; int maxPrepId = -1;
			double averagePrepTime = 0.0; 
			
			double maxCookTime = -1.0; int maxCookId = -1;
			double averageCookTime = 0.0;
			
			double maxDeliveryTime = -1.0; int maxDeliveryId = -1;
			double averageDeliveryTime = 0.0;
			
			double maxTotalTime = -1.0; int maxTotalId = -1;
			double averageTotalTime = 0.0;
			
			int count = 0;
			for (Order order : orders) {
				if (order.isOrderCompleted() == false) {
					continue;
				}
				count += 1;
				
				averageCost += order.getPrice();
				averagePrepTime += order.getTimeSpentPreparing();
				averageCookTime += order.getTimeSpentCooking();
				averageDeliveryTime += order.getTimeSpentDelivering();
				averageTotalTime += order.calculateTotalTime();
				
				if (order.getTimeSpentPreparing() > maxPrepTime) {
					maxPrepTime = order.getTimeSpentPreparing();
					maxPrepId = order.getOrderId();
				}
				
				if (order.getTimeSpentCooking() > maxCookTime) {
					maxCookTime = order.getTimeSpentCooking();
					maxCookId = order.getOrderId();
				}
				
				if (order.getTimeSpentDelivering() > maxDeliveryTime) {
					maxDeliveryTime = order.getTimeSpentDelivering();
					maxDeliveryId = order.getOrderId();
				}
				
				if (order.calculateTotalTime() > maxTotalTime) {
					maxTotalTime = order.calculateTotalTime();
					maxTotalId = order.getOrderId();
				}
			}
			
			if (count > 0) {
				averageCost /= count;
				averagePrepTime /= count;
				averageCookTime /= count;
				averageDeliveryTime /= count;
				averageTotalTime /= count;
				
				setRow(reportModel, 1, new Object[] { "Average cost: " + moneyFormatter.format(averageCost) });
				
				setRow(reportModel, 2, new Object[] {
						"Average Prep Time: " + timeFormatter.format(averagePrepTime),
						"Max Prep Time: " + timeFormatter.format(maxPrepTime),
						"Max Prep Order #: " + maxPrepId
				});
				
				setRow(reportModel, 3, new Object[] {
						"Average Cook Time: " + timeFormatter.format(averageCookTime),
						"Max Cook Time: " + timeFormatter.format(maxCookTime),
						"Max Cook Order #: " + maxCookId
				});
				
				setRow(reportModel, 4, new Object[] {
						"Average Delivery Time: " + timeFormatter.format(averageDeliveryTime),
						"Max Delivery Time: " + timeFormatter.format(maxDeliveryTime),
						"Max Delivery Order #: " + maxDeliveryId
				});
				
				setRow(reportModel, 5, new Object[] {
						"Average Total Time: " + timeFormatter.format(averageTotalTime),
						"Max Total Time: " + timeFormatter.format(maxTotalTime),
						"Max Total Order #: " + maxTotalId
				});
			}
		}
	}

}
