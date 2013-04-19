package main.java.pds;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * DeliveryManager is the class representing the delivery waiting area and 
 * collection of delivery people
 *
 * @author Richard Sperrazza, ras1668@rit.edu
 */
public class DeliveryManager implements Updateable, java.io.Serializable
{
	private WaitingQueue deliveryWaitingQueue = 
			new WaitingQueue(OrderStage.DELIVERY_WAITING);
	private ArrayList< Delivery > deliveryPeople = new ArrayList< Delivery >();
	
	//ArrayList of current orders, referenced in from main: 
	//assume name is currentOrders
	private Map< String, Vector< Order > > orders = null;
	
	public DeliveryManager(Map< String, Vector< Order > > orders) {
		this.orders = orders;
	}

	/**
	 * Creates a temporary list of current orders in the deliver_waiting area,
	 * checks for non-busy delivery people, hands them an order if it can,
	 * and removes the orders' items from the waiting area
	 */
	public void update() {		
		// Create a temporary list of current orders in the deliver_waiting area 
		ArrayList< Order > deliveryWaitingReadyOrders = new ArrayList< Order >();
		
		// Must serial access to concurrent map even when iterating on it.
		// http://docs.oracle.com/javase/6/docs/api/java/util/Collections.html#synchronizedMap(java.util.Map)
		synchronized(orders) {
			Set< Map.Entry< String, Vector<Order > > > entrySet = orders.entrySet();
			for (Map.Entry< String, Vector< Order > > kv : entrySet) {
				Vector< Order > custOrders = kv.getValue();
				synchronized(custOrders) {
					for (Order order : custOrders) {
						if (order.getCurrentStage() == OrderStage.DELIVERY_WAITING) {
							if (deliveryWaitingReadyOrders.isEmpty()) {
								deliveryWaitingReadyOrders.add(order);
							} else {
								for (int i = 0; i < deliveryWaitingReadyOrders.size(); i++) {
									if (deliveryWaitingReadyOrders.get(i).getTimeCreated() >= order.getTimeCreated()) {
										deliveryWaitingReadyOrders.add(i, order);
										break;
									} else if (i == (deliveryWaitingReadyOrders.size() - 1)) {
										deliveryWaitingReadyOrders.add(order);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (deliveryWaitingReadyOrders.isEmpty()) {
			return;
		}
		
		for (Delivery deliveryPerson : deliveryPeople) {
			if (!deliveryPerson.isBusy() && deliveryWaitingReadyOrders.isEmpty() == false) {
				// Get the order
				Order order = deliveryWaitingReadyOrders.get(0);
				deliveryWaitingReadyOrders.remove(0);
				
				// Remove the order items from the waiting queue
				for (OrderItem item : order.getOrderItems()) {
					item.endStage(deliveryWaitingQueue.getAssociatedStage());
					deliveryWaitingQueue.getQueue().remove(item);
				}
				
				// Start the actual delivery
				try {
					deliveryPerson.startDelivery(order);
				}
				catch (Exception exception) {
					System.out.println("Error while starting a delivery with a completed order.");
					System.err.println(exception.toString());
				}
				
			}
		}
	}
	
	/**
	 * Add a delivery person.
	 *
	 * @param delivery the delivery person
	 */
	public void addDeliveryPerson(Delivery person) {
		deliveryPeople.add(person);
	}

	/**
	 * Get the queue of items waiting for delivery
	 *
	 * @return the queue of items waiting for delivery
	 */
	public WaitingQueue getDeliveryWaitingQueue() {
		return deliveryWaitingQueue;
	}
	
	/**
	 * Get the queue of delivery people
	 *
	 * @return the queue of delivery people
	 */
	public ArrayList< Delivery > getDeliveryQueue() {
		return deliveryPeople;
	}
	
}
