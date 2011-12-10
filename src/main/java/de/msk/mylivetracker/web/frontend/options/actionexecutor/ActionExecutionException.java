package de.msk.mylivetracker.web.frontend.options.actionexecutor;

/**
 * ActionExecutionException.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionExecutionException extends Exception {

	private static final long serialVersionUID = 4413395103402993891L;

	public ActionExecutionException() {
		super();
	}

	public ActionExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ActionExecutionException(String arg0) {
		super(arg0);
	}

	public ActionExecutionException(Throwable arg0) {
		super(arg0);
	}	
}
