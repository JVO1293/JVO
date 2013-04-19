package main.java.pds;

import java.util.ArrayList;

/**
 * The Kitchen class encapsulates all cooks, ovens, and the interactions 
 * necessary between cooks and ovens.
 * 
 * @author jhaberstro
 */
public class Kitchen extends Stage implements Updateable, java.io.Serializable
{
	private WaitingQueue preparationWaitingQueue = new WaitingQueue(OrderStage.PREPARATION_WAITING);
	private WaitingQueue cookingWaitingQueue = new WaitingQueue(OrderStage.COOKING_WAITING);
	private ArrayList< Chef > chefs = new ArrayList< Chef >();
	private ArrayList< Oven > ovens = new ArrayList< Oven >();
	
	/**
	 * Add an OrderItem to the kitchen.
	 *
	 * @param item the item
	 */
	public synchronized void addOrderItem(OrderItem item) {
		if (item.getFood().getPrepTime() > 0) {
			preparationWaitingQueue.addOrderItem(item);
		}
		else if (item.getFood().getCookTime() > 0) {
			cookingWaitingQueue.addOrderItem(item);
		}
		else {
			this.nextStage.addOrderItem(item);
		}
	}
	
	/**
	 * Add a chef to the kitchen.
	 *
	 * @param chef the chef
	 */
	public synchronized void addChef(Chef chef) {
		chefs.add(chef);
		chef.setNextStage(cookingWaitingQueue);
		chef.setWaitingQueue(preparationWaitingQueue);
	}
	
	/**
	 * Add an oven to the kitchen.
	 *
	 * @param oven the oven
	 */
	public synchronized void addOven(Oven oven) {
		assert(this.nextStage != null);
		ovens.add(oven);
		oven.setWaitingQueue(cookingWaitingQueue);
		oven.setNextStage(this.nextStage);
	}
	
	/* (non-Javadoc)
	 * @see main.java.pds.Updateable#update()
	 */
	public synchronized void update() {
	}
}
