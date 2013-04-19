package main.java.pds;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * This class represents a menu for the PDS. It is immutable. 
 * 
 * @author jhaberstro
 */
public class Menu
{
	private ArrayList< Food > foodItems = null;
	private ArrayList< Topping > toppings = null;
	
	/**
	 * Construct a menu from a file.
	 * The syntax of the menu file is as follows:
	 * 		0 | size | price | price per topping | prep time | cook time | 
	 * 			oven space units
	 * 		1 | name
	 * 		2 | name | price | prep time | cook time | oven space units
	 * 		# 0 means pizza
	 * 		# 1 means topping
	 * 		# 2 means other
	 * 		# price per topping is for a whole pizza; 
	 * 			half pizza is half the topping price
	 * 
	 * @param input - an input stream to the file.
	 */
	public Menu(InputStream input) throws Exception {
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		
		String line = null;
		ArrayList< Food > foods = new ArrayList< Food >();
		ArrayList< Topping > toppings = new ArrayList< Topping >();
		while ((line = br.readLine()) != null) {
			// Skip whitespace lines and comments
			if (line.trim().length() == 0 || (line.length() > 0 && 
					line.charAt(0) == '#')) {
				continue;
			}
			
			String[] splits = line.split("\\|");
			for (int i = 0; i < splits.length; ++i) {
				splits[i] = splits[i].trim();
			}
			
			final int NUM_PIZZA_PARAMS = 7;
			final int NUM_TOPPINGS_PARAMS = 2;
			final int NUM_OTHER_PARAMS = 6;
			if (splits.length == NUM_PIZZA_PARAMS && splits[0].equals("0")) {
				// must be a pizza
				Pizza.Size size;
				if (splits[1].equals("small")) {
					size = Pizza.Size.SMALL;
				}
				else if (splits[1].equals("medium")) {
					size = Pizza.Size.MEDIUM;
				}
				else if (splits[1].equals("large")) {
					size = Pizza.Size.LARGE;
				}
				else {
					throw new Exception("Unable to parse pizza size.");
				}
				
				double price = Double.parseDouble(splits[2]);
				double pricePerToppings = Double.parseDouble(splits[3]);
				double prepTime = Double.parseDouble(splits[4]);
				double cookTime = Double.parseDouble(splits[5]);
				int ovenSpace = Integer.parseInt(splits[6]);
				foods.add(new Pizza(size, price, pricePerToppings, 
						prepTime, cookTime, ovenSpace));
			}
			else if (splits.length == NUM_TOPPINGS_PARAMS && splits[0].equals("1")) {
				toppings.add(new Topping(splits[1]));
			}
			else if (splits.length == NUM_OTHER_PARAMS && splits[0].equals("2")) {
				String name = splits[1];
				double price = Double.parseDouble(splits[2]);
				double prepTime = Double.parseDouble(splits[3]);
				double cookTime = Double.parseDouble(splits[4]);
				int ovenSpace = Integer.parseInt(splits[5]);
				foods.add(new Food(name, price, prepTime, 
						cookTime, ovenSpace));
			}
			else {
				throw new Exception("Unable to parse menu file -- error found.");
			}
		}
		
		this.foodItems = foods;
		this.toppings = toppings;
	}
	
	/**
	 * Create a new Menu with the given foods and toppings.
	 * @param foodItems
	 * @param toppings
	 */
	public Menu(ArrayList< Food > foodItems, ArrayList< Topping > toppings) {
		this.foodItems = foodItems;
		this.toppings = toppings;
	}
	
	/**
	 * This method MUST be used when creating an food item from the menu.
	 * @param food
	 * @return
	 */
	public Food instantiateFood(Food food) {
		return food.copy();
	}
	
	/**
	 * Returns a shallow copy of the food items in this menu.
	 * @return a list of the food items.
	 */
	public ArrayList< Food > getFoodItems() {
		ArrayList< Food > deepCopy = new ArrayList< Food >();
		for (Food t : foodItems) {
			deepCopy.add(t.copy());
		}
		return deepCopy;
	}
	
	/**
	 * Returns a shallow copy of the toppings in this menu.
	 * @return a list of the toppings.
	 */
	public ArrayList< Topping > getToppings() {
		ArrayList< Topping > deepCopy = new ArrayList< Topping >();
		for (Topping t : toppings) {
			deepCopy.add(new Topping(t));
		}
		return deepCopy;
	}
}
