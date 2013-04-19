package main.java.pds;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The WaitingQueue class is responsible for storing OrderItems before they 
 * are ready to go out for either delivery, cooking, or preparation.
 * @author jhaberstro
 */
public class WaitingQueue extends Stage implements OrderItemWorker,
		Iterable<OrderItem>, java.io.Serializable
{
	private LinkedList< OrderItem > queue = new LinkedList< OrderItem >();
	private OrderStage associatedStage;
	
	/**
	 * Instantiates a new waiting queue.
	 *
	 * @param stage the stage
	 */
	public WaitingQueue(OrderStage stage) {
		this.associatedStage = stage;
	}
	
	/* (non-Javadoc)
	 * @see main.java.pds.Stage#addOrderItem(main.java.pds.OrderItem)
	 */
	public synchronized void addOrderItem(OrderItem item) {
		queue.add(item);
		item.setWorker(this);
		item.startStage(associatedStage);
	}
	
	/**
	 * Removes the next item.
	 *
	 * @return the order item
	 */
	public synchronized OrderItem removeNextItem() {
		OrderItem item = queue.remove();
		item.endStage(associatedStage);
		item.setWorker(null);
		return item;
	}	
	
	/**
	 * Peek next item.
	 *
	 * @return the order item
	 */
	public synchronized OrderItem peekNextItem() {
		return queue.peek();
	}
	
	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public synchronized LinkedList< OrderItem > getQueue() {
		return queue;
	}
	
	/**
	 * A notification to the worker that an order item is being cancelled.
	 *
	 * @param item - the order item being cancelled.
	 */
	public synchronized boolean cancel(OrderItem item) {
		return queue.remove(item);
	}
	
	/**
	 * Get which stage this worker is apart of.
	 *
	 * @return the associated stage
	 */
	public OrderStage getAssociatedStage() {
		return associatedStage;
	}

	/**
	 * return iterator for queue
	 * 
	 * @return iterator at head of list
	 */
	public synchronized Iterator<OrderItem> iterator() {
		return queue.iterator();
	}
	
	/**
	 * checks to see if queue contains an certain order item
	 * 
	 * @return boolean of whether or not item is in queue
	 */
	public synchronized boolean contains(OrderItem item) {
		return queue.contains(item);
	}

}
