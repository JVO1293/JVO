package main.java.pds;

/**
 * The interface OrderItemWorker represents an actor in the system that 
 * can "work on" an order item. 
 * 
 * @author jhaberstro
 */
interface OrderItemWorker 
{
	/**
	 * A notification to the worker that an order item is being cancelled
	 * @param item - the order item being cancelled.
	 * @return true on success.
	 */
	public boolean cancel(OrderItem item);
	
	/**
	 * Get which stage this worker is apart of.
	 * @return
	 */
	public OrderStage getAssociatedStage();
}
