package test.java.pds;

import junit.framework.TestCase;
import main.java.pds.Pizza;
import main.java.pds.Topping;

public class PizzaTests extends TestCase
{
	static final double PIZZA_PRICE = 10.0;
	static final double TOPPING_PRICE = 1.0;
	
	public void testPrice() {
		Pizza pizza = new Pizza(Pizza.Size.LARGE, PIZZA_PRICE, TOPPING_PRICE, 0.0, 0.0, 0);
		assertEquals(pizza.getPrice(), 10.0);
		
		Topping wholeTopping = new Topping("Whole Topping");
		wholeTopping.setCoverage(Topping.Coverage.WHOLE);
		pizza.addTopping(wholeTopping);
		assertEquals(pizza.getPrice(), 11.0);
		
		Topping leftTopping = new Topping("Left Topping");
		leftTopping.setCoverage(Topping.Coverage.LEFT_HALF);
		pizza.addTopping(leftTopping);
		assertEquals(pizza.getPrice(), 11.5);
		
		Topping rightTopping = new Topping("Right Topping");
		rightTopping.setCoverage(Topping.Coverage.RIGHT_HALF);
		pizza.addTopping(rightTopping);
		assertEquals(pizza.getPrice(), 12.0);
	}
	
	public void testAddTopping() {
		Pizza pizza = new Pizza(Pizza.Size.LARGE, PIZZA_PRICE, TOPPING_PRICE, 0.0, 0.0, 0);
		assertEquals(pizza.getToppings().size(), 0);
		
		Topping olivesWhole = new Topping("Olives");
		pizza.addTopping(olivesWhole);
		assertEquals(pizza.getToppings().size(), 1);
		
		Topping olivesHalf = new Topping("Olives");
		olivesHalf.setCoverage(Topping.Coverage.LEFT_HALF);
		pizza.addTopping(olivesHalf);
		assertEquals(pizza.getToppings().size(), 1);
		assertEquals(pizza.getToppings().get(0).getCoverage(), Topping.Coverage.LEFT_HALF);
	}
	
	public void testRemoveTopping() {
		Pizza pizza = new Pizza(Pizza.Size.LARGE, PIZZA_PRICE, TOPPING_PRICE, 0.0, 0.0, 0);
		assertEquals(pizza.getToppings().size(), 0);
		
		// add a topping
		Topping bubblesWhole = new Topping("Bubbles");
		pizza.addTopping(bubblesWhole);
		assertEquals(pizza.getToppings().size(), 1);
		
		//remove a topping
		pizza.removeTopping(bubblesWhole);
		assertEquals(pizza.getToppings().size(), 0);
		
		Topping bubblesHalf = new Topping("Bubbles Half");
		bubblesHalf.setCoverage(Topping.Coverage.LEFT_HALF);
		
		// add a topping
		pizza.addTopping(bubblesHalf);
		assertEquals(pizza.getToppings().size(), 1);
		assertEquals(pizza.getToppings().get(0).getCoverage(), Topping.Coverage.LEFT_HALF);
		
		// remove a topping
		pizza.removeTopping(bubblesHalf);
		assertEquals(pizza.getToppings().size(), 0);
	}
}
