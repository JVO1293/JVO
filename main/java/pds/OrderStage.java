package main.java.pds;

/**
 * The enum OrderStage represents each stage of the PDS that an order 
 * must go through.
 * 
 * @author jhaberstro
 */
public enum OrderStage implements java.io.Serializable
{
	MODIFY,
	PREPARATION_WAITING,
	PREPARATION,
	COOKING_WAITING,
	COOKING,
	DELIVERY_WAITING,
	DELIVERY,
	COMPLETED
}
