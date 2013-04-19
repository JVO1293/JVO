package main.java.pds;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * FileManager is the class that handles reading and writing the order database.
 * 
 * @author Richard Sperrazza
 *
 */
public class FileManager {

	/**
	 * Writes the order database to a file
	 * @param orders The map of the orders
	 * @param file Name of the database file (specified for testing)
	 * @throws IOException
	 */
	public static void writeOrders(Map< String, Vector< Order > > orders, String file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(orders);
	    oos.writeInt(Order.orderCounter);
	    oos.close();
	}
	
	/**
	 * Reads the order database
	 * @param file Name of the database file (specified for testing)
	 * @return The map of the orders
	 * @throws IOException
	 */
	public static Map< String, Vector< Order > > readOrders(String file) throws IOException {
		Map< String, Vector< Order > > orders = Collections.synchronizedMap(new HashMap< String, Vector< Order > >());
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    try {
			orders = Collections.synchronizedMap((Map<String, Vector<Order>>) ois.readObject());
			Order.orderCounter = ois.readInt();
		} catch (ClassNotFoundException e) {
			System.err.println("Error reading orders database.");
			e.printStackTrace();
		}
	    ois.close();
	    return orders;
	}
	
	/**
	 * Writes the customer database to a file
	 * @param customers The map of the customers
	 * @param file Name of the database file (specified for testing)
	 * @throws IOException
	 */
	public static void writeCustomers(Map< String, Customer > customers, String file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(customers);
	    oos.close();
	}
	
	/**
	 * Reads the customer database
	 * @param file Name of the database file (specified for testing)
	 * @return The map of the customers
	 * @throws IOException
	 */
	public static Map< String, Customer > readCustomers(String file) throws IOException {
		Map< String, Customer > customers = Collections.synchronizedMap(new HashMap< String, Customer >());
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    try {
			customers = Collections.synchronizedMap((Map<String, Customer>) ois.readObject());
		} catch (ClassNotFoundException e) {
			System.err.println("Error reading customers database.");
			e.printStackTrace();
		}
	    ois.close();
	    return customers;
	}
}
