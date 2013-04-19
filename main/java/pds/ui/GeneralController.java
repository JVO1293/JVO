package viewcontroller;

import main.java.pds.ui.GeneralView;
import main.java.pds.ui.GeneralView.InputChannel;
import managerreport.ManagerReportView;

/**
 * This class provides an abstraction for the Logic Controller.  Its 
 *  responsibilities include: 
 */
public abstract class GeneralController {
	
	/** 
	 * An abstraction of the set of states.
	 */
	protected interface ControllerState {};
	
	/**
	 * The set of states maintained by this controller.  Each subclass should
	 * define its own controllerState implementation.
	 */
	protected ControllerState currentState;
	
	/**
	 * The GeneralView that this controller is in charge of.
	 */
	protected GeneralView view;
	
	/**
	 * Indicates if this controller is active.
	 */
	public boolean active = false;

	/**
	 * @return This controller's view.
	 */
	public GeneralView getView() {
		return view;
	}

	/**
	 * @param view The new view.
	 */
	public void setView(GeneralView view) {
		this.view = view;
	}
	
	/**
	 * @return This controller's current state.
	 */
	public ControllerState getState(){
		return currentState;
	}
	
	/**
	 * @return If the controller is active.
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * This method is called by the previous controller to indicate that
	 *  this controller should assume control of the flow of the program.  
	 *  Each GeneralController subclass enters into it designated initial 
	 *  state here.
	 */
	public abstract void enterInitialState();

	/**
	 * This is the method called by the view to send input.
	 *
	 * @param    message        The input message, formatted as a string.
	 * @param   channel        The input channel, which indicates the source
	 */
	public abstract void respondToInput( String message, ManagerReportView.ManagerReportInputChannel channel );
	
	/**
	 * Handles an error in the input that is sent from the view.  This 
	 *  simply writes an error message to the error output, and maintains
	 *  the same state (which is usually waiting for some input).
	 *  
	 *  @param  message		the error message to be printed out
	 */
	public abstract void handleInputError( String message );

}
