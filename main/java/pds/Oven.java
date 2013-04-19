/**
 * Oven.java
 *
 * Version:
 * $Id$
 *
 * Revisions:
 * $Log$
 * 
 * @author: Gregg Miller 
 * @name: Oven.java
 * @date: 10.1.2012
 * 
 */
package main.java.pds;

import java.util.ArrayList;
import java.util.Iterator;


public class Oven extends Stage implements OrderItemWorker, Updateable, java.io.Serializable
{
	private WaitingQueue waitingQueue = null;
	private ArrayList<OrderItem> cookingList = new ArrayList<OrderItem>();
	private final int capacity;
	private int freeSpace;
	private final OrderStage stage;
	
	/**
	 * Constructor
	 * creates Oven ands sets capacity initially to zero
	 * 
	 * @param size max capacity of oven 
	 */
	public Oven(int size) {
		capacity=size;
		freeSpace=size;
		stage=OrderStage.COOKING;
	}
	
	/**
	 * Let the oven know about the cooking stage waiting queue.
	 * @param waitingQueue
	 */
	public void setWaitingQueue(WaitingQueue waitingQueue) {
		this.waitingQueue = waitingQueue;
	}
	
	/**
	 * for testing purposes only
	 */
	public ArrayList<OrderItem> getCookingList(){
		return cookingList;
	}
	
	/**
	 * Get the capacity of the oven
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Will remove OrderItem from cooking list
	 * if not found will do nothing
	 * @param item item to be removed from stage.
	 */
	public boolean cancel(OrderItem item) {
		if(cookingList.remove(item)){
			freeSpace+= item.getFood().getOvenSpace();
			return true;
		}
		
		return false;
	}

	/**
	 * Get which stage this worker is apart of.
	 * 
	 * @return Stage enum of the classes stage.
	 */
	public OrderStage getAssociatedStage() {
		return stage;
	}

	/**
	 * Starts cooking an item
	 * Warning: does not check to see if Oven can fit item
	 * 
	 * @param item Item to start cooking 
	 */
	public void addOrderItem(OrderItem item) {
		cookingList.add(item);
		freeSpace-= item.getFood().getOvenSpace();
		item.startStage(stage);
		item.setWorker(this);
	}

	/**
	 * Updates the oven:
	 * For each item in oven, it checks to see if it's done
	 * Then it checks to see if there is anything that can be added to cook queue.
	 */
	public void update() {
		Iterator<OrderItem> it =cookingList.iterator();
		TimeRange tempTRange;
		OrderItem currentItem;		

		//loop through list checking for done items
		while(it.hasNext()){
			currentItem=it.next();
			double cookingTime= currentItem.getFood().getCookTime() * 60.0;
			tempTRange = currentItem.getStageTimes().get(stage);
			double elapsedTime = TimeSystem.getCurrentTime() - tempTRange.getStart();
			
			if (elapsedTime >=cookingTime){
				//set end cook time
				currentItem.endStage(stage);
				//free oven space
				freeSpace += currentItem.getFood().getOvenSpace();
				//remove from cook queue
				it.remove(); 
				currentItem.setWorker(null);
				//change item's stage
				//nextStage should be the Delivery system
				nextStage.addOrderItem(currentItem);
				
			}//else do nothing
		}
		
		//check for any items to cook
		/*
		 * Warning:
		 * Does not dequeue but looks through entire queue starting
		 * with the first added then moves through queue to last enqueued
		 * May starve larger items
		 */
		synchronized(waitingQueue) {
			it = waitingQueue.iterator();
			while(it.hasNext() && freeSpace !=0){
				currentItem=it.next();
				//if it fits, it cooks
				if (currentItem.getFood().getOvenSpace()<= freeSpace){
					this.addOrderItem(currentItem);
					it.remove();
				}
			}
		}
	}
}
