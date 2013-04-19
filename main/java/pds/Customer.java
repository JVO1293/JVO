package main.java.pds;

/**
 * Customer.java
 * 
 * This class is to manage the customer information
 *  
 * @author: Johanna Calderon
*/
public class Customer implements java.io.Serializable
{
	private String phoneNumber;
	private String name;
	private Location address;
	
	/**
	 * @param phoneNumber     
	 * @param name
	 * @param address
	 */
	public Customer(String phoneNumber, String name, Location address) {
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.address = address;
	}
	
	/**
	 * Human readable string representation
	 * @return the string representation.
	 */
	public String toString() {
		return name + " - " + address + " - " + phoneNumber;
	}

	/**
	 * Get the customer's address.
	 * @return the address
	 */
	public Location getAddress() {
		return address;
	}

	/**
	 * Set the customer's address.
	 * @param address     the address to set
	 */
	public void setAddress(Location address) {
		this.address = address;
	}

	/**
	 * Get the customer's phone number
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Get the customer's name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}	
}