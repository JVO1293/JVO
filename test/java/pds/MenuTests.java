package test.java.pds;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import main.java.pds.Menu;
import main.java.pds.Pizza;
import main.java.pds.Food;
import main.java.pds.Topping;
import junit.framework.TestCase;

public class MenuTests extends TestCase
{
	public void testMenuCreationAndValidation() {
		String input = 
				  "0|small|5.00|0.75|10|5|2\n"
				+ "1|Sausage\n"
				+ "2 |Salad|2|3|0|0\n"
				+ ""
				+ "# 0 means pizza\n"
				+ "# 1 means topping\n"
				+ "# 2 means other\n"
				+ "# price per topping is for a whole pizza; half pizza is half the topping price\n";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNotNull(menu);
		assertEquals(menu.getFoodItems().size(), 2);
		assertEquals(menu.getToppings().size(), 1);
		
		assertTrue(menu.getFoodItems().get(0) instanceof Pizza);
		assertTrue(menu.getFoodItems().get(1) instanceof Food);
		
		Pizza pizza = (Pizza)menu.getFoodItems().get(0);
		assertEquals(pizza.getSize(), Pizza.Size.SMALL);
		assertEquals(pizza.getPrice(), 5.0);
		assertEquals(pizza.getToppingPrice(), 0.75);
		assertEquals(pizza.getPrepTime(), 10.0);
		assertEquals(pizza.getCookTime(), 5.0);
		assertEquals(pizza.getOvenSpace(), 2);
		
		Food salad = menu.getFoodItems().get(1);
		assertEquals(salad.getType(), "Salad");
		assertEquals(salad.getPrice(), 2.0);
		assertEquals(salad.getPrepTime(), 3.0);
		assertEquals(salad.getCookTime(), 0.0);
		assertEquals(salad.getOvenSpace(), 0);
		
		Topping sausage = menu.getToppings().get(0);
		assertEquals(sausage.getType(), "Sausage");
	}

	public void testMenuUnknownID() {
		String input = "3|small|5.00|0.75|10|5|2";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNull(menu);
	}
	
	public void testMenuBadSize() {
		String input = "0|smal|5.00|0.75|10|5|2";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNull(menu);
	}
	
	public void testMenuWrongNumberOfPizzaArguments() {
		String input = "0|small|5.00|0.75|10|5";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNull(menu);
	}
	
	public void testMenuWrongNumberOfFoodArguments() {
		String input = "2|Salad|2|3|0|0|0";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNull(menu);
	}
	
	public void testMenuEmptyFile() {
		String input = "";
		Menu menu = null;
		try {
			menu = new Menu(new ByteArrayInputStream(input.getBytes("UTF-8")));
		}
		catch (Exception e) {
			fail("Error in actual test (not the code being tested.");
		}
		
		assertNotNull(menu);
	}
}
