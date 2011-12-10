package de.msk.mylivetracker.service.geocoding.yahoo.response;

import java.io.Serializable;

/**
 * ResultSet.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ResultSet implements Serializable {

	private static final long serialVersionUID = -371619738629450233L;
	
	private String version;
	private int Error;
	private String errorMessage;
	private Results[] Results;
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the error
	 */
	public int getError() {
		return Error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(int error) {
		Error = error;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the results
	 */
	public Results[] getResults() {
		return Results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(Results[] results) {
		Results = results;
	}	
}
