package test.java.pds;

import java.util.ArrayList;

import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.OrderStage;
import main.java.pds.Oven;
import main.java.pds.Pizza;
import main.java.pds.TimeRange;
import main.java.pds.TimeSystem;
import main.java.pds.Topping;
import main.java.pds.WaitingQueue;
import junit.framework.TestCase;

public class OvenTest extends TestCase {
	private Oven oven; 
	private WaitingQueue deliveryWaitQueue = new WaitingQueue(OrderStage.DELIVERY_WAITING);
	private WaitingQueue cookingWaitingQueue = new WaitingQueue(OrderStage.COOKING_WAITING);
	private OrderItem order1;//med pizza
	private OrderItem order2;//large pizza
	private OrderItem order3;//salad
	private OrderItem order4;//large item
	private OrderItem order5;//long cook time item
	
	public void setUp(){
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
		Food food3 = new Food("Long cook",45,10,60,8);
		Customer c1 = new Customer("5559298338", "Gregg", Location.RIT);
		Order order = new Order(c1);
		//create orderItems
		order1 = order.addFood(pizza1);
		order2 = order.addFood(pizza2);
		order3 = order.addFood(food1);
		order4 = order.addFood(food2);
		order5 = order.addFood(food3);
		//add to cook waiting queue
		cookingWaitingQueue.addOrderItem(order1);
		cookingWaitingQueue.addOrderItem(order2);
		cookingWaitingQueue.addOrderItem(order3);
		cookingWaitingQueue.addOrderItem(order4);
		cookingWaitingQueue.addOrderItem(order5);
		
		//oven set up  
		oven = new Oven(40);
		oven.setNextStage(deliveryWaitQueue);
		oven.setWaitingQueue(cookingWaitingQueue);
	}
	
	public void testAddOrderItem() {
	// Test functionality of addOrderItem
			assertTrue(oven.getCookingList().isEmpty());
			OrderItem tempItem = null;
			if (cookingWaitingQueue.peekNextItem() != null){
				tempItem = cookingWaitingQueue.removeNextItem();
				oven.addOrderItem(tempItem);
			}
			assertFalse(oven.getCookingList().isEmpty());
	}

	public void testCancel() {
		// add 2 items
		oven.addOrderItem(order1);
		oven.addOrderItem(order2);
		oven.addOrderItem(order3);
		assertFalse(oven.getCookingList().isEmpty());
		//cancel second item
		oven.cancel(order2);
		//check to see if it is still in list
		assertFalse(oven.getCookingList().contains(order2));
		
	}

	public void testUpdate() {
		TimeSystem.setTimeScale(500.0);
		// Test that order items get done and are in cooking waiting queue
		while(cookingWaitingQueue.peekNextItem() != null || !oven.getCookingList().isEmpty())  {
			oven.update();
		}	
		ArrayList<OrderItem> orderItems =new ArrayList<OrderItem>();
		orderItems.add(order1);
		orderItems.add(order2);
		orderItems.add(order3);
		orderItems.add(order4);
		orderItems.add(order5);
		
		// Test that the time function works properly
		for(OrderItem item : orderItems) {
			// Check to see if item is in cooking queue
			assertTrue(deliveryWaitQueue.contains(item));
			// Get time spent preparing
			TimeRange prepTime = item.getStageTimes().get(oven.getAssociatedStage());
			double elapsedTime = prepTime.getEnd() - prepTime.getStart();
			elapsedTime /= 60;
			double correctTime = item.getFood().getCookTime();
			
			System.out.println("Correct cook Time: " + correctTime);
			System.out.println("Time spent: " + elapsedTime);
			// Check to make sure correct amount of time was spent on preparation
			assertTrue(elapsedTime >= correctTime);
		}
	}

}
