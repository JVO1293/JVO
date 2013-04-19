package test.java.pds;

import junit.framework.TestCase;
import main.java.pds.Customer;
import main.java.pds.Delivery;
import main.java.pds.Location;
import main.java.pds.Order;
import main.java.pds.OrderItem;
import main.java.pds.Food;
import main.java.pds.OrderStage;
import main.java.pds.TimeSystem;

public class DeliveryTests extends TestCase
{
	protected void setUp() {
		TimeSystem.setTimeScale(180.0); // 1 real second equals 3 simulated minutes
	}
	
	public void testDeliveryStart() {
		Customer customer = new Customer("555-555-5555", "Bob Bobby", Location.RIT);
		Order order = new Order(customer);
		order.addFood(new Food("Salad", 5.0, 2, 0, 0));
		Delivery deliveryPerson = new Delivery();
		
		try {
			deliveryPerson.startDelivery(order);
		}
		catch (Exception exception) {
			fail("Unable to start delivery.");
		}
		assertTrue(deliveryPerson.isBusy());
	}
	
	public void testDeliveryUpdate() {
		Customer customer = new Customer("555-555-5555", "Bob Bobby", Location.RIT);
		Order order = new Order(customer);
		OrderItem saladItem = order.addFood(new Food("Salad", 5.0, 2, 0, 0));
		
		Delivery deliveryPerson = new Delivery();
		try { deliveryPerson.startDelivery(order); }
		catch (Exception exception) { fail("Unable to start delivery."); }
		assertTrue(deliveryPerson.isBusy());
		
		double startTime = TimeSystem.getCurrentTime();
		while (!TimeSystem.hasTimeElapsed(startTime, 18 * 60.0)) {
			deliveryPerson.update();
			
			// It's possible that the time elapsed while in deliveryPerson.update(),
			// so we don't want to perform these asserts if that has happened
			if (TimeSystem.hasTimeElapsed(startTime, 18.0 * 60.0) == false) {
				assertTrue(deliveryPerson.isBusy());
				assertTrue(order.isOrderCompleted() == false);
				assertTrue(saladItem.getCurrentStage() == OrderStage.DELIVERY);
			}
		}
		
		deliveryPerson.update();
		assertTrue(deliveryPerson.isBusy() == false);
		assertTrue(order.isOrderCompleted());
		assertTrue(saladItem.getCurrentStage() == OrderStage.COMPLETED);
	}
}
