package main.java.pds.ui;

import java.util.ArrayList;

import viewcontroller.GeneralController;

/**
 * This class provides an abstraction for the View. 
 */
public abstract class GeneralView {
	
	/**
	 * The controller that is in charge of managing this view.
	 */
	protected GeneralController controller;
	
	/**
	 * The abstraction of the input channel.
	public interface InputChannel {}
	
	/**
	 * The abstraction of the output channel.  Implementations of this should
	 *  be of the "enum" type.
	 */
	public interface OutputChannel {}
	
	/**
	 * @return This view's controller.
	 */
	public GeneralController getController() {
		return controller;
	}

	/**
	 * @param controller The new controller.
	 */
	public void setController(GeneralController controller) {
		this.controller = controller;
	}
	
	/**
	 * Test this view to enable input to the given input channel.
	 * This method must be implemented by all GeneralView subclasses.
	 * 
	 * @param inChannel	   The channel to be enabled.
	 * @param enabled 	   Whether or not to enable input.
	 */
	public abstract void setChannelEnabled( InputChannel inChannel, 
											boolean enabled ); 

	/**
	 * Writes the given string to the indicated output channel.  
	 * This method must be implemented by all GeneralView subclasses.
	 * 
	 * @param message		The string to display.
	 * @param outChannel	The channel through which the message should be
	 * 						 displayed.
	 */
	public abstract void displayString( String message, 
										OutputChannel outChannel );
	
	/**
	 * Writes the given object to the indicated output channel.  
	 *  The protocol for sending objects must be agreed upon by the view
	 *  and it controller.
	 * This method must be implemented by all GeneralView subclasses.
	 * 
	 * @param object 		The object to display.
	 * @param outChannel	The channel through which the message should be
	 * 						 displayed.
	 */
	public abstract void displayObject( Object object,
										OutputChannel outChannel );
	
	/**
	 * Writes the given list to the indicated output channel.

	 */
	public abstract <T> void displayList( ArrayList<T> list, 
									   OutputChannel outChannel );

}
