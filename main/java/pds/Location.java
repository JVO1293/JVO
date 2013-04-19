package main.java.pds;

/**
 * Location.java
 * 
 * This Enum type contains the list of locations available for delivery.
 *  
 * @author: Johanna Calderon
*/

public enum Location implements java.io.Serializable
{
	RIT(0),
	UR(1),
	NAZ(2),
	FISH(3),
	RWC(4),
	MCC(5);
	
	private final int value;   //declared variable for the value
	
	//this pass the value of the location selected in the command line
	Location(int i) {
		this.value = i;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
}