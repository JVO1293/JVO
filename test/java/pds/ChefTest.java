/**
 * @author: Jenny Zhen; jxz6853@rit.edu
 * @name: Food.java
 * @date: 10.06.12
 */

package test.java.pds;

import java.util.ArrayList;
import main.java.pds.Chef;
import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.OrderStage;
import main.java.pds.Pizza;
import main.java.pds.TimeRange;
import main.java.pds.TimeSystem;
import main.java.pds.Topping;
import main.java.pds.WaitingQueue;
import junit.framework.TestCase;

/**
 * Test case for the Chef().
 */
public class ChefTest extends TestCase {
	static final double PIZZA_PRICE = 10.0;
	static final double TOPPING_PRICE = 1.0;
	private WaitingQueue preparationWaitingQ = 
			new WaitingQueue(OrderStage.PREPARATION_WAITING);
	private  WaitingQueue cookingWaitingQ = 
			new WaitingQueue(OrderStage.COOKING_WAITING);
	private Chef chef = new Chef();
	private ArrayList<OrderItem> orderItems;
	
	public void setUp() {
		chef.setWaitingQueue(preparationWaitingQ);
		chef.setNextStage(cookingWaitingQ);
		
		// Create food items (all items must have prep time > 0)
		Pizza pizza = new Pizza(Pizza.Size.LARGE, 
				PIZZA_PRICE, TOPPING_PRICE, 6.0, 12.0, 10);
		
		Topping peppersWhole = new Topping("Peppers");
		pizza.addTopping(peppersWhole);
		
		Food frenchFries = new Food("French Fries", 11.00, 5.0, 10.0, 1);
		Food pizzaLogs = new Food("Pizza Logs", 20.00, 8.0, 10.0, 2);
		Food salad = new Food("Salad", 2.00, 4.0, 0.0, 0);
	
		
		// Create order with food items
		Customer customer = new Customer("7182250509", "Jenny", Location.RIT);
		Order order = new Order(customer);
		
		order.addFood(pizza);
		order.addFood(frenchFries);
		order.addFood(pizzaLogs);
		order.addFood(salad);
		
		// Put food items into prep queue
		orderItems = order.getOrderItems(); 
		for(OrderItem item : orderItems) {
			if(item.getFood().getPrepTime() > 0)
				preparationWaitingQ.addOrderItem(item);
		}
		
	}
	
	public void testAddOrderItem(){
		// Test functionality of addOrderItem
		assertNull(chef.getCurrentItem());
		OrderItem tempItem = null;
		if(preparationWaitingQ.peekNextItem() != null){
			tempItem = preparationWaitingQ.removeNextItem();
			chef.addOrderItem(tempItem);
		}
		assertNotNull(chef.getCurrentItem());
		
		
		// Try to add item to chef with item already being made by chef
		if (preparationWaitingQ.peekNextItem() != null){
			chef.addOrderItem(preparationWaitingQ.peekNextItem());
		}
		assertEquals(tempItem, chef.getCurrentItem());
	}
	
	public void testUpdate(){
		TimeSystem.setTimeScale(180.0);
		// Test that order items get done and are in cooking waiting queue
		System.out.println("Before while loop");
		while(preparationWaitingQ.peekNextItem() != null || chef.getCurrentItem() != null)  {
			chef.update();
		}
		
		System.out.println("After while loop");
		
		// Test that the time function works properly
		for(OrderItem item : orderItems) {
			// Check to see if item is in cooking queue
			assertTrue(cookingWaitingQ.contains(item));
			// Get time spent preparing
			TimeRange prepTime = item.getStageTimes().
					get(chef.getAssociatedStage());
			double elapsedTime = prepTime.getEnd() - prepTime.getStart();
			elapsedTime /= 60;
			double correctTime = item.getFood().getPrepTime();
			
			System.out.println("Correct prep Time: " + correctTime);
			System.out.println("Time spent: " + elapsedTime);
			// Check to make sure correct amount of time was spent on preparation
			assertTrue(elapsedTime >= correctTime);
		}
	}
	
	public void testCancel() {
		System.out.println("# order items: " + orderItems.size());
		
		// add item to chef
		chef.addOrderItem(orderItems.get(0));
		assertEquals(chef.getCurrentItem(), orderItems.get(0));
		
		// cancel said item
		assertTrue(chef.cancel(orderItems.get(0)));
		assertNull(chef.getCurrentItem());
		
		// cancel an item that is not currently worked on
		assertFalse(chef.cancel(orderItems.get(1)));
		
		// add said item to chef
		chef.addOrderItem(orderItems.get(1));
		assertEquals(chef.getCurrentItem(), orderItems.get(1));
		
		// cancel said item
		assertTrue(chef.cancel(orderItems.get(1)));
		assertNull(chef.getCurrentItem());
	}
	
}
