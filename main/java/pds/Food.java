package main.java.pds;

/**
 * @author: Jenny Zhen; jxz6853@rit.edu
 * @name: Food.java
 * @date: 09.17.12
 */

/**
 * Container for the food.
 */
public class Food implements java.io.Serializable
{
	private String type;
	private double price;
	private double prepTime;
	private double cookTime;
	private int ovenSpace;
	
	/**
	 * Create a new food with the given parameters.
	 * @param type - the name of the food type.
	 * @param price - the price it costs
	 * @param prepTime - how long it takes to prepare this food (in minutes)
	 * @param cookTime - how long it takes to cook this food (in minutes)
	 * @param ovenSpace - how many oven units this food occupies
	 */
	public Food(String type, double price, double prepTime, double cookTime, int ovenSpace) {
		this.type = type;
		this.price = price;
		this.prepTime = prepTime;
		this.cookTime = cookTime;
		this.ovenSpace = ovenSpace;
	}
	
	/**
	 * Get the food type name.
	 * @return food type name
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get how much this food costs to buy.
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}
	
	/** 
	 * Get how much time (in minutes) this food takes to prepare.
	 * @return the prep time
	 */
	public double getPrepTime() {
		return this.prepTime;
	}
	
	/**
	 * Get how much time (in minutes) this food takes to cook.
	 * @return the cook time
	 */
	public double getCookTime() {
		return this.cookTime;
	}
	
	/**
	 * Get how many oven units this food occupies.
	 * @return oven units
	 */
	public int getOvenSpace() {
		return this.ovenSpace;
	}
	
	/**
	 * Create a deep copy of this food object.
	 * @return a deep copy
	 */
	public Food copy() {
		return new Food(type, price, prepTime, cookTime, ovenSpace);
	}
	
	public String toString() {
		return this.getType();
	}
}
