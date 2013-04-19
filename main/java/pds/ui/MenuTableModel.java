package main.java.pds.ui;

import javax.swing.table.AbstractTableModel;

import main.java.pds.Food;
import main.java.pds.Menu;

public class MenuTableModel extends AbstractTableModel
{
	private Menu menu;
	
	public MenuTableModel(Menu menu) {
		this.menu = menu;
	}
	
	public int getColumnCount() {
		return 2;
	}
	
	public int getRowCount() {
		return menu.getFoodItems().size();
	}
	
	public Object getValueAt(int row, int col) {
		Food food = menu.getFoodItems().get(row);
		if (col == 0) {
			return food.getType();
		}
		else {
			return "$" + food.getPrice();
		}
	}
	
	public String getColumnName(int col) {
		String[] values = { "Item", "Price" };
		return values[col];
	}
}
