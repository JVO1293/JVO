package test.java.pds;

import java.util.HashMap;
import java.util.Vector;

import junit.framework.TestCase;
import main.java.pds.Customer;
import main.java.pds.Delivery;
import main.java.pds.DeliveryManager;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.Food;
import main.java.pds.Pizza;
import main.java.pds.TimeSystem;

public class DeliveryManagerTest extends TestCase 
{
	protected void setUp() {
		TimeSystem.setTimeScale(180.0); // 1 real second equals 3 simulated minutes
	}
	
	public void testAddDeliveryPerson(){
		DeliveryManager deliveryManager = new DeliveryManager(null);
		Delivery deliveryPerson = new Delivery();
		
		try {
			deliveryManager.addDeliveryPerson(deliveryPerson);
		}
		catch (Exception exception) {
			fail("Unable to add delivery person.");
		}
		assertTrue(deliveryManager.getDeliveryQueue().size() == 1);
		assertTrue(deliveryManager.getDeliveryQueue().contains(deliveryPerson));
	}
	
	public void testWaitingArea() {
		HashMap< String, Vector< Order > > orders = new HashMap< String, Vector< Order > >();
		Customer customer = new Customer("555-555-5555", "Bob Bobby", Location.RIT);
		Order order = new Order(customer);
		order.addFood(new Food("Salad", 5.0, 2, 0, 0));
		order.addFood(new Food("Extreme Chicken", 25.0, 0, 10, 5));
		order.addFood(new Pizza(Pizza.Size.LARGE, 10, 5, 0.0, 0.0, 0));
		Vector< Order > orderArray = new Vector< Order>();
		orderArray.add(order);
		orders.put(customer.getPhoneNumber(), orderArray);
		
		DeliveryManager deliveryManager = new DeliveryManager(orders);
		Delivery deliveryPerson = new Delivery();
		try { deliveryManager.addDeliveryPerson(deliveryPerson); }
		catch (Exception exception) { fail("Unable to add delivery person."); }
		assertTrue(deliveryManager.getDeliveryQueue().size() == 1);
		assertTrue(deliveryManager.getDeliveryQueue().contains(deliveryPerson));
		
		
		try {
			for ( OrderItem item : order.getOrderItems()){
				deliveryManager.getDeliveryWaitingQueue().addOrderItem(item);
			}
		}
		catch (Exception exception) {
			fail("Unable to add order items to waiting area/queue.");
		}
		
		try {
			deliveryManager.update();
			assertTrue(deliveryPerson.isBusy());
			
		}
		catch (Exception exception) {
			fail("Unable to pick up order from waiting area/queue.");
		}
		
	}
}
