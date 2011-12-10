package de.msk.mylivetracker.web.uploader.processor.interpreter.util;

/**
 * InterpreterException.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class InterpreterException extends Exception {
	private static final long serialVersionUID = -3359486469483278961L;
	public InterpreterException(Throwable cause) {
		super(cause);
	}
	public InterpreterException(String message) {
		super(message);
	}		
}
