package de.msk.mylivetracker.web.uploader.interpreter.sarmini;

import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;

/**
 * DeviceSpecific.
 * 
 * @author michael skerwiderski, (c)2016
 * 
 * @version 000
 * 
 * history
 * 000 initial 2016-06-22
 * 
 */
public class DeviceSpecific implements IDeviceSpecific {

	public enum RequestType {
		Login,
		Position
	}
	
	private RequestType requestType;

	/**
	 * @param requestType
	 */
	public DeviceSpecific(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the requestType
	 */
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}	
}
