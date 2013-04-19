package test.java.pds;

import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.PhoneOperator;
import main.java.pds.Pizza;
import main.java.pds.Topping;
import junit.framework.TestCase;

/**
 * PhoneOperatorTest.java
 * 
 * This class tests the PhoneOperator class:
 * test the addCustomer method and the getCurrentOrderForCustomer method.
 *  
 * @author: Johanna Calderon
*/

public class PhoneOperatorTest extends TestCase {

	private PhoneOperator op;
	
	private OrderItem orderI1;//med pizza
	private OrderItem orderI2;//large pizza
	private OrderItem orderI3;//salad
	private OrderItem orderI4;//large item
	private OrderItem orderI5;//long cook time item
	private Order order;
	private ArrayList<OrderItem> o1;
	
    //constructor 
	public PhoneOperatorTest(String name) {
		super(name);
	}

	/**
	 * this method sets the hashMap ready to pass values for test
	 */
	public void setUp() {
		HashMap< String, Customer > customers = new HashMap< String, Customer >();
		HashMap< String, Vector< Order > > orders = new HashMap< String, Vector< Order > >();
		op = new PhoneOperator(customers,orders);
	
			//food set up, create order and add items
			//create toppings
			Topping jellyBeans = new Topping("Jelly Beans");
			Topping sausage = new Topping("Sausage");
			Topping peanutButter = new Topping("Peanut Butter");
			Topping clams = new Topping("Clams");
			//create pizzas
			Pizza pizza1= new Pizza(Pizza.Size.MEDIUM,11,1.5,7,12,9);
			Pizza pizza2= new Pizza(Pizza.Size.LARGE,14,2,10,20,13);
			//add toppings
			pizza1.addTopping(peanutButter);
			pizza1.addTopping(clams);
			pizza2.addTopping(sausage);
			pizza2.addTopping(jellyBeans);
			//Create other foods
			Food food1 = new Food("Pizza Logs",7,0,5,4);
			Food food2 = new Food("Hippo",145,15,120,40);
			Food food3 = new Food("Long cook, no prep",45,0,60,8);
			Customer c1 = new Customer("5559298338", "Gregg", Location.RIT);
			order = new Order(c1);
			//create orderItems
			orderI1 = order.addFood(pizza1);
			orderI2 = order.addFood(pizza2);
			orderI3 = order.addFood(food1);
			orderI4 = order.addFood(food2);
			orderI5 = order.addFood(food3);
		
			o1 = order.getOrderItems(); 
			
	}	
	
	/**
	 * this method tests the addCustomer(), passing some examples values
	 */
	public void testAddCustomer() {
		//populate customer
		Customer insertedCustomer1= new Customer("5852302145","johanna",Location.NAZ);
		Customer insertedCustomer2= new Customer("5852302145","joha",Location.RIT);
		op.addCustomer(insertedCustomer1);
		Customer storedCustomer = op.getCustomerForPhoneNumber("5852302145");
		//test if inserted customer match the stored customer in the hashMap
		assertEquals(insertedCustomer1,storedCustomer);  
		//test if not inserted customer does not match the stored customer 
		assertNotSame(insertedCustomer2,storedCustomer);
	}

	/**
	 * this method tests the getCurrentOrderForCustomer() 
	 */
	
	public void testgetCurrentOrderForCustomer() {
		
	/*
		Customer c2 = op.getCustomerForPhoneNumber("5559298338");
		Order cc = op.getCurrentOrderForCustomer(c2, false);	
		assertEquals(order, cc);
	*/	

	}	
		
	/**
	 * This method tests the remove item from order	
	 */
		
	public void testremoveOrderItem() {
		
		Food foodTemp = new Food("Pizza Logs",7,0,5,4);
		op.removeOrderItem(orderI3);         //	remove the Pizza Logs
		boolean a = o1.contains(foodTemp);
		assertFalse(a);  
 	}
	
	/**
	 * This method tests the time estimation for an order.
	 * 
	 * estimateTime() is equal to getNeededTime(), unless if there are 
	 * items on the queue for chef/oven
	 */
	
	public void testEstimateTime() {
		op.estimateTime(order);
		assertTrue(order.getEstimatedTime() >= order.getNeededTime());
	}
}