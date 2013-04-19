package main.java.pds;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;


class ConfigParameters
{
	public int numChefs, numOvens, ovenCapacity, numDeliveryPeople;
}

/**
 * Class Main contains the program's main entry point.
 * 
 * @author jhaberstro
 * @version 19-Sept-2012
 */
public class Main {
	static Map< String, Vector< Order > > orders = Collections.synchronizedMap(new HashMap< String, Vector< Order > >());
	static Map< String, Customer > customers = Collections.synchronizedMap(new HashMap< String, Customer >());
	
	public static ArrayList< Order > getOrdersList() {
		ArrayList< Order > ordersList = new ArrayList< Order >();
		synchronized(orders) {
			Set< Map.Entry< String, Vector<Order > > > entrySet = orders.entrySet();
			for (Map.Entry< String, Vector<Order > > kv : entrySet) {
				Vector< Order > custOrders = kv.getValue();
				synchronized(custOrders) {
					for (Order order : custOrders) {
						ordersList.add(order);
					}
				}
			}
		}
		
		return ordersList;
	}
		
	/**
	 * The main entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileInputStream menuFileStream = null;
		try {
//			File file = new File("/Users/jhaberstro/Documents/School/RIT/Software Engineering/menu.txt"); 
			File file = new File("menu.txt"); 
			menuFileStream = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to open menu file. Exiting...");
			System.exit(1);
		}
		
		Menu menu = null;
		try {
			menu = new Menu(menuFileStream);
		}
		catch (Exception exception) {
			System.err.println(exception.getMessage());
			exception.printStackTrace(System.err);
			System.exit(1);
		}
		
		ConfigParameters configParams = parseConfigFile();
		
		// read databases
		try {
			File customerFile = new File("customers.db");
			File orderFile = new File("orders.db");
			if (customerFile.exists()) {
				customers = FileManager.readCustomers("customers.db");
			}
			if (orderFile.exists()) {
				orders = FileManager.readOrders("orders.db");
			}
		} catch (IOException e) {
			System.err.println("Error reading database(s).");
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		final TimeSystem timeSystem = new TimeSystem();
		TimeSystem.setTimeScale(180.0); // 1 second of real team == 3 minutes simulated time
		
		final Kitchen kitchen = new Kitchen();
		final DeliveryManager deliverySystem = new DeliveryManager(orders);
		kitchen.setNextStage(deliverySystem.getDeliveryWaitingQueue());
		
		for (int i = 0; i < configParams.numChefs; ++i) {
			Chef c = new Chef();
			kitchen.addChef(c);
			timeSystem.addUpdateable(c);
		}
		
		for (int i = 0; i < configParams.numOvens; ++i) {
			Oven o = new Oven(configParams.ovenCapacity);
			kitchen.addOven(o);
			timeSystem.addUpdateable(o);
		}
		
		for (int i = 0; i < configParams.numDeliveryPeople; ++i) {
			Delivery d = new Delivery();
			deliverySystem.addDeliveryPerson(d);
			timeSystem.addUpdateable(d);
		}
		
		timeSystem.addUpdateable(kitchen);
		timeSystem.addUpdateable(deliverySystem);
		
		Thread updateThread = new Thread() {
			public void run() {
				while (true) {
					timeSystem.update();
				}
			}
		};

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Main.saveDatabaseInfo();
			}
		});
		
		updateThread.start();
//		CommandLinePrompt.run(new PhoneOperator(customers, orders), menu, kitchen);
		GUI.run(new PhoneOperator(customers, orders), menu, kitchen);
	}
	
	public static void saveDatabaseInfo() {
		try {
			File customerFile = new File("customers.db");
			File orderFile = new File("orders.db");
			if (customerFile.exists()) {
				customerFile.delete();
			}
			if (orderFile.exists()) {
				orderFile.delete();
			}
			FileManager.writeCustomers(customers, "customers.db");
			FileManager.writeOrders(orders, "orders.db");
		} catch (IOException e) {
			System.err.println("Error writing database(s).");
			e.printStackTrace(System.err);
		}	
	}

	/**
	 * Parse the config file and return the config parameters.
	 * @return the config parameters
	 */
	private static ConfigParameters parseConfigFile() {
		FileInputStream fs = null;
		try {
//			fs = new FileInputStream("/Users/jhaberstro/Documents/School/RIT/Software Engineering/config.txt");
			fs = new FileInputStream("config.txt");
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to open config file. Exiting...");
			System.exit(1);
		}
		
		ConfigParameters params = new ConfigParameters();
		Scanner scanner = new Scanner(fs);
		params.numChefs = scanner.nextInt();
		params.numDeliveryPeople = scanner.nextInt();
		params.numOvens = scanner.nextInt();
		params.ovenCapacity = scanner.nextInt();
		
		if (params.numChefs <= 0 || params.numDeliveryPeople <= 0 || params.numOvens <= 0 || params.ovenCapacity <= 0) {
			System.err.println("Invalid negative number in config.txt. Exiting...");
			System.exit(1);
		}
		
		return params;
	}
}
