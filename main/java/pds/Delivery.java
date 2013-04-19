package main.java.pds;

/**
 * Delivery is the class representing a single delivery person in the PDS
 *
 * @author Richard Sperrazza, ras1668@rit.edu
 */
public class Delivery extends Stage implements Updateable, OrderItemWorker, java.io.Serializable
{
	//listings of time-to-travel
	private static int[] deliveryTimes = { 18, 12, 25, 21, 25, 18 };
	
	//time attributes for update looping
	private int deliveryTime = -1;
	private double time;
	
	//holder for the order being delivered
	private Order deliveryOrder = null;
	
	private final OrderStage stage = OrderStage.DELIVERY;

	
	/**
	 * delivery persons can be left in the list of delivery persons;
	 * simply iterate through the list checking this boolean
     * @return boolean status representing state of delivery person
     */
	public boolean isBusy() {
		return deliveryOrder != null;
	}
	
	/**
	 * configuration for the delivery object to be updatedable
	 * @return this object, so it can be placed in the list of
	 * items to be updated with each loop
	 */
	public Updateable initialize() {
		time = TimeSystem.getCurrentTime();
		return this;
	}
	
	/**
	 * configuration for the delivery object to be updatedable
	 */
	public void update() {
		if (deliveryOrder == null) {
			return;
		}
		
		// if the delivery time has elapsed.
		if (TimeSystem.hasTimeElapsed(time, deliveryTime * 60.0)) {
			//set order as completed -> all orderitems as completed
			for (OrderItem item : deliveryOrder.getOrderItems()) {
				item.endStage(OrderStage.DELIVERY);
				item.startStage(OrderStage.COMPLETED);
				item.endStage(OrderStage.COMPLETED);
				item.setWorker(null);
			}
			
			/* report completed order details to managerial reports section here
			double completedTime = deliveryOrder.calculateTotalTime();
			*/
			
			//reset delivery person to an accepting state
			deliveryOrder = null;
			deliveryTime = -1;
		}
	}
	
	/**
	 * sets the delivery person as busy and delivers the order to the
	 * proper location, waiting out the correct amount of time before
	 * finishing the order.
     * @param orderToDeliver Order object passed in from the delivery queue
     */
	public void startDelivery(Order orderToDeliver) throws Exception {
		if (this.isBusy()) {
			throw new Exception("Cannot start delivery on busy delivery person.");
		}
		
		deliveryOrder = orderToDeliver;
		Location location = deliveryOrder.getCustomer().getAddress();
		deliveryTime = deliveryTimes[location.getValue()];
		time = TimeSystem.getCurrentTime();
		
		for (OrderItem item : orderToDeliver.getOrderItems()) {
			item.setWorker(this);
			item.startStage(OrderStage.DELIVERY);
		}
	}

	/**
	 * Would cancel the items in the order, but cannot cancel a delivery in progress
	 */
	public boolean cancel(OrderItem item) {
		return false;
	}

	/**
	 * Get which stage this worker is apart of.
	 * @return the stage the worker is apart of
	 */
	public OrderStage getAssociatedStage() {
		return stage;
	}

	/**
	 * Required by OrderItemWorker; unused.
	 * Delivery person receives whole orders, not singular orderItems.
	 */
	public void addOrderItem(OrderItem item) {
		//Unused, orders are passed in entirely
	}

}
