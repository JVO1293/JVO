package main.java.pds;

/**
 * Interface Updateable specifies the interface for an object that can be should
 * be updated once per frame/iteration of the main loop.
 * 
 * @author jhaberstro
 * @version 19-Sept-2012
 */
public interface Updateable
{
	/**
	 * Abstract method that when implemented is responsible for performing
	 * any per-iteration updates for implementor. 
	 */
	public void update();
}
