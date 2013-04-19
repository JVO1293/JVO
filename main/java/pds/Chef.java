package main.java.pds;

/**
 * The Chef is responsible for performing order item preparations and 
 * then passing the order items onto the next stage.
 * 
 * @author jhaberstro
 */
public class Chef extends Stage implements Updateable, OrderItemWorker, java.io.Serializable
{
	private WaitingQueue waitingQueue = null;
	private OrderItem currentOrderItem = null;
	
	/**
	 * Let the chef know about the preparation waiting queue.
	 * @param waitingQueue
	 */
	public void setWaitingQueue(WaitingQueue waitingQueue) {
		this.waitingQueue = waitingQueue;
	}
	
	/**
	 * only for testing purposes 
	 */
	public OrderItem getCurrentItem(){
		return this.currentOrderItem;
	}

	/* (non-Javadoc)
	 * @see main.java.pds.OrderItemWorker#cancel(main.java.pds.OrderItem)
	 */
	public boolean cancel(OrderItem item) {
		if (item == currentOrderItem) {
			currentOrderItem = null;
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see main.java.pds.OrderItemWorker#getAssociatedStage()
	 */
	public OrderStage getAssociatedStage() {
		// get the stage that the item is in, relative to the item association
		// item is in preparation if called through the chef
		return OrderStage.PREPARATION;
	}

	/* (non-Javadoc)
	 * @see main.java.pds.Updateable#update()
	 */
	public void update() {
		// check to see if the chef has an item or not
		
		// if the chef is working on an item
		if (currentOrderItem != null) {
			// get the elapsed time since chef first started preparing item
			TimeRange timeRange = currentOrderItem.getStageTimes().get(getAssociatedStage());
			double elapsedTime = TimeSystem.getCurrentTime() - timeRange.getStart(); 
			
			// convert elapsedTime to from seconds to minutes
			elapsedTime /= 60.0;
			
			// if elapsed is greater than prep time required, item is done
			if (elapsedTime > currentOrderItem.getFood().getPrepTime()) {
				currentOrderItem.endStage(getAssociatedStage());
				currentOrderItem.setWorker(null);
				
				// nextStage links the different stages between associations; 
				// move current item to the next stage from chef
				nextStage.addOrderItem(currentOrderItem);
				currentOrderItem = null;
			}
		}
		
		// if the chef not working on an item and there is an item on the queue
		if (currentOrderItem == null && waitingQueue.peekNextItem() != null) {
			// chef works on the first item on the queue
			addOrderItem(waitingQueue.removeNextItem());
		}
	}

	/* (non-Javadoc)
	 * Starts working on entered item
	 * @see main.java.pds.Stage#addOrderItem(main.java.pds.OrderItem)
	 */
	public void addOrderItem(OrderItem item) {
		// checks to see if chef already has an item 
		if (currentOrderItem == null) {
			item.startStage(getAssociatedStage());
			item.setWorker(this);
			currentOrderItem = item;
		}
	}
}
