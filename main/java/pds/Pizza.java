package main.java.pds;

import java.util.ArrayList;

/**
 * @author: Jenny Zhen; jxz6853@rit.edu
 * @name: Pizza.java
 * @date: 09.17.12
 */

/**
 * This class stores Pizza food item information.
 * 
 * @author jhaberstro
 */
public class Pizza extends Food implements java.io.Serializable
{
	
	private Size size;
	private double toppingPrice;
	private ArrayList< Topping > toppings = new ArrayList< Topping >();
	
	/**
	 * Pizze size constants 
	 * @author jhaberstro
	 */
	public enum Size
	{
		//Size pizzaSize = Size.SMALL;
		SMALL,
		MEDIUM,
		LARGE
	}
	
	/**
	 * Construct a pizza
	 * @param size
	 * @param price
	 * @param toppingPrice
	 * @param prepTime
	 * @param cookTime
	 * @param ovenSpace
	 */
	public Pizza(Size size, double price, double toppingPrice, double prepTime, double cookTime, int ovenSpace) {
		super("Pizza", price, prepTime, cookTime, ovenSpace);
		this.size = size;
		this.toppingPrice = toppingPrice;
	}
	
	/**
	 * Get the size of the pizza.
	 * @return size of the pizza
	 */
	public Size getSize() {
		return this.size;
	}
	
	/**
	 * Get price of the pizza, including the cost of each topping on the pizza.
	 * @return total price of pizza
	 */
	public double getPrice() {
		double totalToppingPrice = 0.0;
		for (Topping t : toppings) {
			totalToppingPrice += toppingPrice * (t.getCoverage() == Topping.Coverage.WHOLE ? 1.0 : 0.5);
		}
		
		return super.getPrice() + totalToppingPrice;
	}
	
	/**
	 * Add a topping to the pizza.
	 * @param topping
	 */
	public void addTopping(Topping topping) {
		for (Topping t : toppings) {
			if (t.getType().equals(topping.getType())) {
				toppings.remove(t);
				break;
			}
		}
		
		toppings.add(topping);
	}
	
	/**
	 * Change all the toppings
	 * @param the new toppings
	 */
	public void setToppings(ArrayList< Topping > newToppings) {
		toppings = newToppings;
	}
	
	/**
	 * Remove a topping from the pizza.
	 * @param topping
	 */
	public void removeTopping(Topping topping) {
		toppings.remove(topping);
	}
	
	/**
	 * Get how much a single topping costs.
	 * @return price of a topping.
	 */
	public double getToppingPrice() {
		return toppingPrice;
	}
	
	/**
	 * Get the toppings on the pizza.
	 * @return toppings
	 */
	public ArrayList< Topping > getToppings() {
		return toppings;
	}
	
	/**
	 * Get the type
	 * @return the type
	 */
	@Override
	public String getType() {
		return size + " " + super.getType();
	}
	
	/**
	 * Get a deep copy of this pizza object
	 * @return a deep copy
	 */
	public Food copy() {
		Pizza newPizza = new Pizza(size, getPrice(), toppingPrice, getPrepTime(), getCookTime(), getOvenSpace());
		for(Topping t : toppings) {
			newPizza.addTopping(new Topping(t));
		}
		return newPizza;
	}
	
	/**
	 * To sting method for pizzas
	 * @return string of size of pizza, toppings and coverage of toppings 
	 */
	@Override
	public String toString(){
		return this.getType();
	}
}
