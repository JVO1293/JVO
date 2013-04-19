package main.java.pds;

import java.util.Vector;

/**
 * Class TimeSystem is responsible for updating a set of objects that implement
 * the Updateable interface. It also provides a set of utility functions for
 * managing time.
 * 
 * @author jhaberstro
 * @version 19-Sept-2012
 */
public class TimeSystem implements Updateable
{
	private Vector< Updateable > updateableObjs = new Vector< Updateable >();
	private static double timeScale = 1.0;
	
	/**
	 * Get the current time 
	 * @return time in seconds
	 */
	public static double getCurrentTime() {
		return ((double)System.nanoTime()) * getTimeScale() * 1.0e-9;
	}
	
	/**
	 * Check to see if certain amount of time has elapsed.
	 * @param startTime - reference time from which you wish to count in seconds.
	 * @param desiredElapsedTime - how much time you want to pass in seconds.
	 * @return true if desiredElapsedTime has passed; otherwise false.
	 */
	public static boolean hasTimeElapsed(double startTime, double desiredElapsedTime) {
		return (getCurrentTime() - startTime) >= desiredElapsedTime;
	}

	/**
	 * Get the simulation time scale.
	 * @return the simulation time scale
	 */
	public static double getTimeScale() {
		return timeScale;
	}
	
	/**
	 * For example, a timeScale of 60.0 means that every
	 * second of real-time is equivalent to 1 minute simulation time.
	 * @param newTimeScale
	 */
	public static void setTimeScale(double newTimeScale) {
		timeScale = newTimeScale;
	}
		
	/**
	 * Add an object that should be updated every frame/iteration
	 * @param obj - the object to be updated
	 */
	public void addUpdateable(Updateable obj) {
		if (updateableObjs.contains(obj) == false) {
			updateableObjs.add(obj);
		}
	}
	
	/**
	 * Remove an object from being updated every frame/iteration
	 * @param obj - the object to no longer be updated
	 */
	public void removeUpdateable(Updateable obj) {
		updateableObjs.remove(obj);
	}
	
	/**
	 * Perform an update on every added updateable.
	 */
	public void update() {
		for(Updateable o : updateableObjs) {
			o.update();
		}
	}
}
