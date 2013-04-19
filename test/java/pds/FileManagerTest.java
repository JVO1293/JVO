package test.java.pds;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import main.java.pds.FileManager;
import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Location;
import main.java.pds.Order;
import junit.framework.TestCase;

public class FileManagerTest extends TestCase {

	public void testWriteOrders() {
		Map< String, Vector< Order > > orders = Collections.synchronizedMap(new HashMap< String, Vector< Order > >());
		orders.put("555-555-5555", new Vector< Order >());
		Order newOrder = new Order(new Customer("555-555-5555", "Bob Saget", Location.RIT));
		newOrder.addFood(new Food("Cheez Stix", 0, 0, 0, 0));
		orders.get("555-555-5555").add(newOrder);
		
		try {
			FileManager.writeOrders(orders, "ordersTest.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testReadOrders() {
		Map< String, Vector< Order > > orders = Collections.synchronizedMap(new HashMap< String, Vector< Order > >());
		try {
			orders = FileManager.readOrders("ordersTest.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(orders.get("555-555-5555").size() == 1);
	}
	
	public void testWriteCustomers() {
		Map< String, Customer > customers = Collections.synchronizedMap(new HashMap< String, Customer >());
		customers.put("555-555-5555", new Customer("555-555-5555", "Bob Saget", Location.RIT));
		
		try {
			FileManager.writeCustomers(customers, "customersTest.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testReadCustomers() {
		Map< String, Customer > customers = Collections.synchronizedMap(new HashMap< String, Customer >());
		try {
			customers = FileManager.readCustomers("customersTest.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(customers.get("555-555-5555").getName().matches("Bob Saget"));
	}
}
