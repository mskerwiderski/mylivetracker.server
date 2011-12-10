package de.msk.mylivetracker.web.uploader.processor;

/**
 * CutConnectionException.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class CutConnectionException extends Exception {
	private static final long serialVersionUID = -6336183283453732829L;

	/**
	 * 
	 */
	public CutConnectionException() {
	}

	/**
	 * @param message
	 */
	public CutConnectionException(String message) {
		super(message);
	}		
}
