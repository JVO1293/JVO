package main.java.pds;

import java.util.TreeMap;

/**
 * The class OrderItem is wrapper around a Food, and will be owned by an Order 
 * instance. This class holds auxiliary information that is associated to a 
 * food item in an order, but is separate enough that it doesn't warrant being 
 * in the Food class.
 *
 * @author jhaberstro
 */

// Keeps track of which stage the item is currently in,
// and how long each stage took for that item 
public class OrderItem implements java.io.Serializable
{
	private Food food;
	private Order order;
	private OrderItemWorker worker;
	private TreeMap< OrderStage, TimeRange > stageTimes = new TreeMap< OrderStage, TimeRange >();
	private OrderStage currentStage = OrderStage.MODIFY;
	
	/**
	 * Construct a new OrderItem.
	 * @param order that this item is apart of
	 * @param food
	 */
	public OrderItem(Order order, Food food) {
		this.food = food;
		this.order = order;
		this.currentStage = OrderStage.PREPARATION_WAITING;
	}

	/**
	 * Notify this order item that is has been cancelled.
	 */
	public boolean cancel() {
		return worker == null || worker.cancel(this);
	}
	
	/**
	 * Indicate that this OrderItem is entering a new stage.
	 * @param stage
	 */
	public void startStage(OrderStage stage) {
		// set the start time of the stage to the current time
		assert(stageTimes.get(stage) == null);
		currentStage = stage;
		TimeRange tr = new TimeRange();
		tr.setStart(TimeSystem.getCurrentTime());
		stageTimes.put(stage, tr);
	}
	
	/**
	 * Indicate that this OrderItem is ending its current stage.
	 * @param stage
	 */
	public void endStage(OrderStage stage) {
		TimeRange tr = stageTimes.get(stage);
		assert(tr != null);
		tr.setEnd(TimeSystem.getCurrentTime());
	}

	/**
	 * Get the worker that is currently working on this order item.
	 * @return the owner
	 */
	public OrderItemWorker getWorker() {
		return worker;
	}

	/**
	 * Set the worker that will now be working on this order item.
	 * @param owner the owner to set
	 */
	public void setWorker(OrderItemWorker worker) {
		this.worker = worker;
	}

	/**
	 * Get the food that this order item wraps.
	 * @return the food
	 */
	public Food getFood() {
		return food;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * Get a shallow copy the start and end times of each stage.
	 * @return the stageTimes
	 */
	public TreeMap<OrderStage, TimeRange> getStageTimes() {
		return stageTimes;
	}
	
	/**
	 * Get the current stage of the order item.
	 * @return the stage
	 */
	public OrderStage getCurrentStage() {
		return currentStage;
	}
	
	/**
	 * Returns the food's string representation.
	 * Used for displaying in the UI.
	 * @return string rep
	 */
	public String toString() {
		return food.toString();
	}
}
