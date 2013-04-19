package main.java.pds;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import main.java.pds.Customer;


public class CommandLinePrompt
{
	public static void main(String [] args) {
		PhoneOperator phoneOperator = new PhoneOperator( new HashMap<String, Customer>(), new HashMap<String, Vector<Order>>());
		
		String input = 
				  "0|small|5.00|0.75|10|5|2\n"
				+ "1|Sausage\n"
				+ "2 |Salad|2|3|0|0\n"
				+ ""
				+ "# 0 means pizza\n"
				+ "# 1 means topping\n"
				+ "# 2 means other\n"
				+ "# price per topping is for a whole pizza; " +
						"half pizza is half the topping price\n";
		Menu menu = null;
		try {
			menu = new Menu( new ByteArrayInputStream(input.getBytes("UTF-8")));
		} catch (Exception e) {
			System.err.println("Whoops");
			e.printStackTrace();
		}
		
		CommandLinePrompt.run(phoneOperator, menu, null);
	}
		
	/**
	 * Run the main command promp loop
	 * @param phoneOperator
	 * @param menu
	 * @param nextStage - where the order should be sent to when placed.
	 */
	public static void run(PhoneOperator phoneOperator, Menu menu, Stage nextStage) {
		System.out.println("Hello, welcome to the Pizza Delivery System!");
		while (true) {
			int personChoice;
			do {
				personChoice = readInt("If you are a Manager please press 0. If you are an Employee please press 1. Press 2 to exit.");
			} while (personChoice != 0 && personChoice != 1 && personChoice != 2);
			if (personChoice == 0) {
				System.out.println("Sorry, the Manager view is not yet supported.");
			}
			else if (personChoice == 1) {
				String phoneNumber = readString( "Hello, please enter the phone number of the customer:");
				Customer customer = phoneOperator.getCustomerForPhoneNumber(phoneNumber);
				if (customer == null) {
					customer = newCustomerPrompt(phoneOperator, phoneNumber);
				}
				else {
					System.out.println("Found " + customer.getName());
				}
				
				while (true) {
					int choice = 0;
					do {
						choice = readInt("Please:\n\tPress 1 to Place/Modify an order.\n\tPress 2 to view the status of a current order.\n\tPress 3 to finish.");
					} while (choice < 1 && choice > 3);
					
					// Place or modify an order
					if (choice == 1) {
						Order order = phoneOperator.getCurrentOrderForCustomer(customer, true);
						if (order.getOrderItems().size() > 0) {
							System.out.println("Sorry, order modification is not currently supported.");
						}
						else {
							runCustomerOrderPrompt(phoneOperator, menu, order);
							System.out.println("Placing the order now. The total is $" + order.getPrice());
							for (OrderItem oi : order.getOrderItems()) {
								nextStage.addOrderItem(oi);
							}
						}
					}
					// View progress of current order
					else if (choice == 2) {
						// if the order is all ready in progress
						Order order = phoneOperator.getCurrentOrderForCustomer(customer, false);
						if (order != null) {
							System.out.println("The status of the current order is: " + order.getCurrentStage());
						}
						// they don't have a current order.
						else {
							System.out.println("An order for this customer have not yet been placed.");
						}
					}
					else if (choice == 3) {
						break;
					}
				}
			}
			else {
				System.out.println("Shutting down... Good bye!");
				System.exit(0);
			}
		}
	}
	
	/**
	 * Prompt for new customer information.
	 * @param phoneOperator
	 * @param phoneNumber
	 * @return the new customer object
	 */
	private static Customer newCustomerPrompt(PhoneOperator phoneOperator, String phoneNumber) {
		System.out.println("We see that this is a new customer. Please enter some information about the customer.");
		String customerName = readString( "Please enter the customer's full name: "); 
		Location address;
		while (true) {
			for (int i = 0; i < 6; ++i) {
				String addressName="";
				switch(i) {
					case 0: addressName = "RIT"; break;
					case 1: addressName = "UR"; break;
					case 2: addressName = "NAZ"; break;
					case 3: addressName = "FISH"; break;
					case 4: addressName = "RWC"; break;
					case 5: addressName = "MCC"; break;
				
				}
				System.out.println("\t" + i + ". " + addressName);
			}
			int locationChoice = readInt("Please enter the customer's location (number): ");
			
			// if this is a valid choice
			if (locationChoice >= 0 && locationChoice <= 5 ) {
				address = Location.values()[locationChoice];
				break;
			}
			else {
				System.out.println("Sorry, this is not a valid location. Please try again.");
			}
		}
		
		System.out.println("Will delivery to " + address + ", phone number " + phoneNumber );
		Customer customer = new Customer(phoneNumber, customerName, address);
		phoneOperator.addCustomer(customer);
		return customer;
	}
	
	/**
	 * Prompt customer for all the food selection for their order.
	 * @param phoneOperator
	 * @param menu
	 * @param order
	 */
	private static void runCustomerOrderPrompt(PhoneOperator phoneOperator, Menu menu, Order order) {
		System.out.println("Menu:\n------");
		ArrayList< Food > foods = menu.getFoodItems();
		for (int i = 0; i < foods.size(); ++i) {
			Food food = foods.get(i);
			System.out.println((i+1) + ". " + food.getType() + "\t$" + food.getPrice());
		}
		
		while (true) {
			int choice = readInt("Please enter the number that corresponds to the item the customer would like to add the order, or press 0 to finish.");
			if (choice < 0 || choice > foods.size()) {
				System.out.println("This is not a valid choice. Please try again.");
			}
			else if (choice == 0) {
				if (order.getOrderItems().size() == 0) {
					System.out.println("Please add at least one item before you finish the order.");
				}
				else {
					break;
				}
			}
			else {
				Food newFood = foods.get(choice-1).copy();
				if (newFood instanceof Pizza) {
					boolean addToppings = readString("Please press Y if you would like to add toppings (or press any other key to not to.").equals("Y");
					if (addToppings) {
						toppingsPrompt(menu, (Pizza)newFood);
					}
				}
				
				order.addFood(newFood);
				System.out.println(foods.get(choice-1).getType() + " added!");
			}
		}
	}
	
	/**
	 * Prompt to get the information for which toppings the customer would like to add to their pizza.
	 * @param menu
	 * @param pizza
	 */
	private static void toppingsPrompt(Menu menu, Pizza pizza) {
		System.out.println("Toppings:\n---------");
		ArrayList< Topping > toppings = menu.getToppings();
		for (int i = 0; i < toppings.size(); ++i) {
			System.out.println((i+1) + ", " + toppings.get(i).getType() + "\tWhole $" + pizza.getToppingPrice() + "\tHalf $" + pizza.getToppingPrice() / 2.0);
		}
		
		while (true) {
			int toppingChoice = -1;
			do {
				toppingChoice = readInt("Please enter the number that corresponds to the topping you would like to add to the pizza, or 0 to return to the menu.");
			} while (toppingChoice < 0 && toppingChoice > toppings.size());
			
			if (toppingChoice == 0) {
				return;
			}
			
			Topping topping = new Topping(toppings.get(toppingChoice-1));
			
			int coverageChoice = -1;
			do {
				coverageChoice = readInt("How much of the pizza would you like the topping to cover?\n\tPress 0 for all.\n\tPress 1 for the left half.\n\tPress 2 for the right half.");
			} while (coverageChoice < 0 && coverageChoice > 2);
			
			topping.setCoverage(Topping.Coverage.values()[coverageChoice]);
			pizza.addTopping(topping);
			System.out.println(topping.getType() + " toppings added to the pizza!");
		}
	}

	/**
	 * Read an int from the command line
	 * @param prompt
	 * @return
	 */
	private static int readInt(String prompt) {
		System.out.println(prompt);
	    java.util.Scanner keyboard = new java.util.Scanner(System.in);                                
	    int result = keyboard.nextInt();
	    System.out.println("");
	    return result;
	}
	
	/**
	 * Read a string from the command line
	 * @param prompt
	 * @return
	 */
	private static String readString(String prompt) {
	    System.out.println(prompt);
	    java.util.Scanner keyboard= new java.util.Scanner(System.in);
	    String result = keyboard.nextLine();
	    System.out.println("");
	    return result;
	}
}
