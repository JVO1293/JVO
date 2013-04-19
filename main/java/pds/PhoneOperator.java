package main.java.pds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * PhoneOperator.java
 * 
 * This class handles the phone operator operations: verify and create the 
 * customer information,
 * create a new order for the specific customer.
 *  
 * @author: Johanna Calderon
*/

public class PhoneOperator implements java.io.Serializable {
	
	private Map< String, Customer > customers = null;
	private Map< String, Vector< Order > > orders = null;
	
	//constructor
	public PhoneOperator(Map< String, Customer > customers, Map< String, Vector< Order > > orders) {
		this.customers = customers;
		this.orders = orders;
	}
	
	/**
	 * This method verify if the customer exist or not.
	 * @return null if customer doesn't exist.
	 */
	public Customer getCustomerForPhoneNumber(String phoneNumber) {
		return customers.get(phoneNumber);
	}
	
	/**
	 * this method add the customer information and the order in their hashMap 
	 * @param newCustomer       contains the phone number
	 */
	public void addCustomer(Customer newCustomer) {
		customers.put(newCustomer.getPhoneNumber(), newCustomer);
		orders.put(newCustomer.getPhoneNumber(), new Vector< Order >());
	}
	
	/**
	 * Return a list of orders that the customer has placed.
	 * @param customer
	 * @return the list of orders.
	 */
	public Vector< Order > getOrdersShallowCopyForCustomer(Customer customer) {
		synchronized(orders) {
			Vector< Order > ordersRef = orders.get(customer.getPhoneNumber()); 
			if (ordersRef == null) {
				return null;
			}
			
			synchronized(ordersRef) {
				return new Vector< Order >(ordersRef);
			}
		}
	}
	
	/**
	 * Gets the current Order for the customer searched by phone number
	 * if there is no order, creates a new one
	 * @param customer
	 * @param createNewOrder
	 * @return
	 */
	public Order getCurrentOrderForCustomer(Customer customer, boolean createNewOrder) {
		Vector< Order > customersOrders = orders.get(customer.getPhoneNumber());
		synchronized(customersOrders) {
			if (customersOrders != null && customersOrders.size() > 0) {
				for (Order o : customersOrders) {
					if (o.isOrderCompleted() == false) {
						return o;
					}
				}
			}
		}
		
		if (createNewOrder) {
			Order newOrder = new Order(customer);
			customersOrders.add(newOrder);
			return newOrder;
		}
		else {
			if (customersOrders.size() > 0) {
				return customersOrders.get(customersOrders.size() - 1);
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Validates if customer has current, previous or no orders
	 * @param phoneNumber
	 * @return  the order
	 */
	public Order getLastOrder(String phoneNumber) {
		Customer customer = getCustomerForPhoneNumber(phoneNumber);
		
		// the customer does not exist
		if(customer == null)
			return null;
			
		// the customer has a current order
		Order order = getCurrentOrderForCustomer(customer, false);
		return order;
	}
	
	/**
	 * Removes the item from the order
	 */
	public boolean removeOrderItem(OrderItem item) {
		return item.getOrder().cancelItem(item);
	}
	
	/**
	 * Estimate the time of the new Order to give information to the customer
	 * Calculates needed time and adds time of anything in the waiting queues 
	 * Implement as such, to avoid excess coupling, i.e. having access to all items
	 */
	public void estimateTime(Order newOrder){
		double total=0;
		synchronized(orders) {
			Set< Map.Entry< String, Vector<Order > > > entrySet = orders.entrySet();
			//loop through pairs in map
			for (Map.Entry< String, Vector< Order > > kv : entrySet) {
				Vector< Order > custOrders = kv.getValue();
				synchronized(custOrders) {
					//loop through  orders for that customer
					for (Order curOrder : custOrders) {
						synchronized(curOrder){
							//ignore completed orders
							if (curOrder.getCurrentStage() != OrderStage.COMPLETED){
								double orderTotal =0;
								OrderStage stage;
								//loop through all items in order
								for (OrderItem curItem : curOrder.getOrderItems()){
									stage = curItem.getCurrentStage();
									switch (stage){
										case PREPARATION_WAITING:
											orderTotal += curItem.getFood().getPrepTime();
											break;
										case COOKING_WAITING:
											orderTotal += curItem.getFood().getCookTime();
											break;
										default: break;
									}
									total += orderTotal;
								}
							}
						}
					}
				}
			}
		}
		total += newOrder.getNeededTime();
		newOrder.setEstimatedTime(total);
	}
	
}