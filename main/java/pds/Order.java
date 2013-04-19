package main.java.pds;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * The class Order is responsible for maintaining information pertinent to an 
 * order, such as the list of food items that make up the order, the customer 
 * who placed the order, etc.
 * 
 * @author jhaberstro
 */
public class Order implements java.io.Serializable
{
	private ArrayList< OrderItem > orderItems = new ArrayList< OrderItem >();
	Customer customer;
	private double timeCreated;
	private double estimatedTime;
	private int orderId;
	
	// public so we can serialize it
	public static int orderCounter = 1;
	
	/**
	 * Construct a new Order with the customer's information.
	 * @param customer
	 */
	public Order(Customer customer) {
		this.customer = customer;
		this.orderId = orderCounter++;
	}
	
	/**
	 * Add a new food item to the order.
	 * @param food
	 * @return the newly created order item associated with this food.
	 */
	public synchronized OrderItem addFood(Food food) {
		OrderItem newItem = new OrderItem(this, food);
		orderItems.add(newItem);
		return newItem;
	}
	
	/**
	 * Tries to cancel an order item from the order. (remove)
	 * @return true on success, else false.
	 */
	public synchronized boolean cancelItem(OrderItem item) {
		if (orderItems.contains(item)) {
			if (item.cancel()) {
				orderItems.remove(item);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Cancel this order.
	 */
	public synchronized void cancel() {
		for (OrderItem oi : orderItems) {
			oi.cancel();
		}
		orderItems.clear();
	}
	
	/**
	 * Get the total price of this order.
	 * @return price of the order 
	 */
	public synchronized double getPrice() {
		double total = 0.0;
		for (OrderItem oi : orderItems) {
			total += oi.getFood().getPrice();
		}
		
		return total;
	}
	
	
	/**
	 * Returns the number of minutes needed to complete order
	 * without taking into account waiting time
	 * @return double of minimal minutes that are needed to complete order 
	 */
	public double getNeededTime(){
		double sum=0;
		int[] deliveryTimes = { 18, 12, 25, 21, 25, 18 };
		for (OrderItem item: orderItems){
			//add prep + cook time for each item to sum
			sum += item.getFood().getPrepTime();
			sum += item.getFood().getCookTime();
		}
		//add delivery time
		Location loc= this.getCustomer().getAddress();
		sum += deliveryTimes[loc.getValue()];
		return sum;
	}
	
	
	/**
	 * Calculate how long it took (or is taking, if the order is still in 
	 * progress) for this order to complete 
	 * @return the time it took for this order to complete (or is taking).
	 */
	public synchronized double calculateTotalTime() {
		double max = 0.0;
		max = getTimeCreated();
		OrderStage stage = this.getCurrentStage();
		for (OrderItem oi : orderItems) {
			TreeMap< OrderStage, TimeRange > durations = oi.getStageTimes();
			if (durations.size() > 0) {
				OrderStage lastStage = durations.lastKey();
				double endTime = durations.get(lastStage).getEnd();
				if (endTime > max) {
					max = endTime;
				}
			}
		}
		
		assert(max > getTimeCreated());
		return (max - getTimeCreated());
	}
	
	/**
	 * Totals time spent preparing for this order
	 * @return time spent preparing for this order
	 */
	public synchronized double getTimeSpentPreparing(){
		double orderPrepTime=0;
		for (OrderItem curItem: orderItems){
			TimeRange prepWait=curItem.getStageTimes().get(OrderStage.PREPARATION_WAITING);
			TimeRange prep=curItem.getStageTimes().get(OrderStage.PREPARATION);
			if (prep != null) orderPrepTime += prep.getDuration();
			if (prepWait != null) orderPrepTime += prepWait.getDuration();
		}
		return orderPrepTime;
	}
	
	/**
	 * Totals time spent cooking for this order
	 * @return time spent cooking for this order
	 */
	public synchronized double getTimeSpentCooking(){
		double orderCookTime=0;
		for (OrderItem curItem: orderItems){
			TimeRange cookWait=curItem.getStageTimes().get(OrderStage.COOKING_WAITING);
			TimeRange cook=curItem.getStageTimes().get(OrderStage.COOKING);
			if (cook != null) orderCookTime += cook.getDuration();
			if (cookWait != null) orderCookTime += cookWait.getDuration();
		}
		return orderCookTime;
	}
	
	/**
	 * Totals time spent delivering for this order
	 * @return time spent delivering for this order
	 */
	public synchronized double getTimeSpentDelivering(){
		double orderDeliveryTime=0;
		for (OrderItem curItem: orderItems){
			TimeRange deliveryWait=curItem.getStageTimes().get(OrderStage.DELIVERY_WAITING);
			TimeRange delivery=curItem.getStageTimes().get(OrderStage.DELIVERY);
			if (delivery != null) orderDeliveryTime += delivery.getDuration();
			if (deliveryWait != null) orderDeliveryTime += deliveryWait.getDuration();
		}
		return orderDeliveryTime;
	}
	
	/**
	 * Is the order complete (ie, delivered)?
	 * 
	 * @return true if complete else false.
	 */
	public synchronized boolean isOrderCompleted() {
		return getCurrentStage() == OrderStage.COMPLETED;
	}
	
	/**
	 * Get the current stage of the order. This will be the current stage of the slowest item in the order.
	 * @return the current stage.
	 */
	public synchronized OrderStage getCurrentStage() {
		if (orderItems.size() == 0) {
			return OrderStage.MODIFY;
		}
		
		int lowestStage = 10000;
		for (OrderItem item : orderItems) {
			if (item.getCurrentStage().ordinal() < lowestStage) {
				lowestStage = item.getCurrentStage().ordinal();
			}
		}
		
		return OrderStage.values()[lowestStage];
	}

	/**
	 * The time that the order was created.
	 * @return the timeCreated
	 */
	public synchronized double getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Set the time when the order was initially started/created.
	 * @param timeCreated the timeCreated to set
	 */
	public synchronized void setTimeCreated(double timeCreated) {
		this.timeCreated = timeCreated;
	}

	/**
	 * Get the time when the order was initially started/created.
	 * @return the estimatedTime
	 */
	public synchronized double getEstimatedTime() {
		return estimatedTime;
	}

	/**
	 * Set the estimated time it will take until this order is delivered.
	 * @param estimatedTime the estimatedTime to set
	 */
	public synchronized void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	/**
	 * Get a shallow copy of the list of items in the order.
	 * @return the orderItems
	 */
	public synchronized ArrayList<OrderItem> getOrderItems() {
		return new ArrayList< OrderItem >(orderItems);
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * Get the order identification number.
	 * @return the order ID.
	 */
	public int getOrderId() {
		return orderId;
	}
}
