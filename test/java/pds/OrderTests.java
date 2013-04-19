package test.java.pds;

import main.java.pds.Order;
import main.java.pds.Pizza;
import main.java.pds.Food;
import main.java.pds.Topping;
import junit.framework.TestCase;

public class OrderTests extends TestCase
{
	Pizza pizza = new Pizza(Pizza.Size.LARGE, 10.0, 1.0, 0.0, 0.0, 1);
	Food salad = new Food("Salad", 1.50, 0.0, 0.0, 1);
	Topping topping = new Topping("Topping");
	
	public void testOrderPrice() {
		Pizza pizzaItem = (Pizza)pizza.copy();
		pizzaItem.addTopping(topping);
		Order order = new Order(null);
		order.addFood(pizzaItem);
		order.addFood(salad.copy());
		
		assertEquals(order.getPrice(), 10.0 + 1.50 + 1.0);
	}
}
