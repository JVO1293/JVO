package main.java.pds;

/**
 * Abstract class representing a Stage of the order process.
 */
public abstract class Stage implements java.io.Serializable
{
	
	/** The next stage after this stage. */
	protected Stage nextStage;
	
	/**
	 * Set the next stage.
	 *
	 * @param nextStage the new next stage
	 */
	public void setNextStage(Stage nextStage) {
		this.nextStage = nextStage;
	}
	
	/**
	 * Add an order item.
	 *
	 * @param item the item
	 */
	public abstract void addOrderItem(OrderItem item);
}