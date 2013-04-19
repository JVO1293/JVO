package test.java.pds;

import java.util.ArrayList;

import main.java.pds.Chef;
import main.java.pds.Customer;
import main.java.pds.Food;
import main.java.pds.Kitchen;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.OrderStage;
import main.java.pds.Oven;
import main.java.pds.Pizza;
import main.java.pds.TimeSystem;
import main.java.pds.Topping;
import main.java.pds.WaitingQueue;
import junit.framework.TestCase;

public class KitchenTest extends TestCase {
	private Kitchen kitchen;
	private Oven oven1; 
	private Oven oven2;
	private Chef chef1;
	private Chef chef2;
	private WaitingQueue preparationWaitQueue = new WaitingQueue(OrderStage.PREPARATION_WAITING);
	private WaitingQueue deliveryWaitQueue = new WaitingQueue(OrderStage.DELIVERY_WAITING);
	private WaitingQueue cookingWaitingQueue = new WaitingQueue(OrderStage.COOKING_WAITING);
	private OrderItem order1;//med pizza
	private OrderItem order2;//large pizza
	private OrderItem order3;//salad
	private OrderItem order4;//large item
	private OrderItem order5;//long cook time item
	private ArrayList<OrderItem> orderItems =new ArrayList<OrderItem>();;
	
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
		Order order = new Order(c1);
		//create orderItems
		order1 = order.addFood(pizza1);
		order2 = order.addFood(pizza2);
		order3 = order.addFood(food1);
		order4 = order.addFood(food2);
		order5 = order.addFood(food3);
		//add items to list of orderItems
		orderItems.add(order1);
		orderItems.add(order2);
		orderItems.add(order3);
		orderItems.add(order4);
		orderItems.add(order5);
		//chef set up
		chef1 = new Chef();
		chef2 = new Chef();
		//oven set up  
		oven1 = new Oven(40);
		oven2 = new Oven(40);
		//kitchen set up
		kitchen =new Kitchen();
		kitchen.setNextStage(deliveryWaitQueue);
	}

	public void testAddOrderItem() {
		TimeSystem.setTimeScale(500);
		kitchen.addChef(chef1);
		kitchen.addOven(oven1);
		kitchen.addOrderItem(order1);
		kitchen.addOrderItem(order2);
		do{
			chef1.update();
			oven1.update();
		}while(chef1.getCurrentItem()!=null || !oven1.getCookingList().isEmpty());
		assertTrue(chef1.getCurrentItem()==null);
		assertTrue(oven1.getCookingList().isEmpty());
		assertTrue(deliveryWaitQueue.contains(order1));
		assertTrue(deliveryWaitQueue.contains(order2));

	}

	public void testAddChef() {
		kitchen.addChef(chef1);
		assertTrue(chef1.getAssociatedStage().equals(OrderStage.PREPARATION));
	}

	public void testAddOven() {
		kitchen.addOven(oven1);
		assertTrue(oven1.getAssociatedStage().equals(OrderStage.COOKING));
	}



}
