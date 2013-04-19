package main.java.pds;

/**
 * The class TimeRange represents a definite duration of time.
 * 
 * @author jhaberstro
 */
public class TimeRange implements java.io.Serializable
{
	private double start, end;
	
	/**
	 * Default constructor
	 */
	public TimeRange() {
		
	}
	
	/**
	 * Constructor a TimeRange object with a start and end time.
	 * @param start
	 * @param end
	 */
	public TimeRange(double start, double end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Get the duration of the time range (end - start).
	 * @return the duration
	 */
	public double getDuration() {
		return end - start;
	}

	/**
	 * Get the start time.
	 * @return the start
	 */
	public double getStart() {
		return start;
	}

	/**
	 * Set the start time.
	 * @param start - the start to set
	 */
	public void setStart(double start) {
		this.start = start;
	}

	/**
	 * Get the end time.
	 * @return the end
	 */
	public double getEnd() {
		return end;
	}

	/**
	 * Set the end time.
	 * @param end - the end to set
	 */
	public void setEnd(double end) {
		this.end = end;
	}
}