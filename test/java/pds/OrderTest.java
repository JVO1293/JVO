package test.java.pds;

import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.OrderStage;
import main.java.pds.Pizza;
import main.java.pds.Topping;
import junit.framework.TestCase;

public class OrderTest extends TestCase {
	private OrderItem orderI1;//med pizza
	private OrderItem orderI2;//large pizza
	private OrderItem orderI3;//salad
	private OrderItem orderI4;//large item
	private OrderItem orderI5;//long cook time item
	private Order order;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		//food set up
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
	}

	public void testGetPrice() {
		double price = orderI1.getFood().getPrice()+orderI2.getFood().getPrice()+7+145+45;
		assertEquals(order.getPrice(),price);
	}
	
	public void testCancelItem() {
		// remove an item in the order
		assertTrue(order.getOrderItems().contains(orderI1));
		assertTrue(order.cancelItem(orderI1));
		assertFalse(order.getOrderItems().contains(orderI1));
		
		// remove the item that was removed
		assertFalse(order.cancelItem(orderI1));
	}
	
	public void testCancel() {
		// order contains items
		assertTrue(order.getOrderItems().contains(orderI1));
		assertTrue(order.getOrderItems().contains(orderI2));
		assertTrue(order.getOrderItems().contains(orderI3));
		assertTrue(order.getOrderItems().contains(orderI4));
		assertTrue(order.getOrderItems().contains(orderI5));
		
		// order cancels all items
		order.cancel();
		
		// order is empty
		assertEquals(order.getOrderItems().size(), 0);
	}

	/**
	 * Testing will be difficult as the time spent for each stage can't be
	 * verified accurately.
	 */
	public void testCalculateTotalTime() {
		/**
		Customer nobody = new Customer("1234567890", "Jenzie", Location.RIT);
		
		Order empty_order = new Order(nobody);
		
		// an empty order takes zero total time
		assertEquals(order.calculateTotalTime(), 0.0);
		
		Pizza pizza1= new Pizza(Pizza.Size.MEDIUM,11,1.5,7,12,9);
		Pizza pizza2= new Pizza(Pizza.Size.LARGE,14,2,10,20,13);
		
		empty_order.addFood(pizza1);
		assertEquals(empty_order.calculateTotalTime(), pizza1.getPrepTime()s);
		*/
	}
}