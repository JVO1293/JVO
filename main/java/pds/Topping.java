package main.java.pds;

/**
 * @author: Jenny Zhen; jxz6853@rit.edu
 * @name: Pizza.java
 * @date: 09.17.12
 */

/**
 * This class stores Topping information.
 * 
 * @author jhaberstro
 */
public class Topping implements java.io.Serializable
{
	/**
	 * Constants representing which portion of a pizza a topping will cover.
	 * 
	 * @author jhaberstro
	 */
	public enum Coverage
	{
		WHOLE,
		LEFT_HALF,
		RIGHT_HALF
	};
	
	private String type;
	private Coverage coverage = Coverage.WHOLE;
	
	/**
	 * Construct a new Topping
	 * @param type - the name of the topping
	 */
	public Topping(String type) {
		this.type = type;
	}
	
	/**
	 * Copy constructor
	 * @param other
	 */
	public Topping(Topping other) {
		this.type = new String(other.type);
		this.coverage = other.getCoverage();
	}
	
	/**
	 * Get the name of the topping
	 * @return type name
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get the pizza coverage for this topping.
	 * @return coverage
	 */
	public Coverage getCoverage() {
		return coverage;
	}
	
	/**
	 * Set the coverage of the topping.
	 * @param coverage
	 */
	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}
	
	/**
	 * Overload toString to return type
	 */
	public String toString() {
		return this.getType();
	}
}
