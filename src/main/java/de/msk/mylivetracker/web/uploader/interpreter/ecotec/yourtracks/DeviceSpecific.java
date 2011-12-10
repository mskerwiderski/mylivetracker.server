package de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks;

import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;

/**
 * DeviceSpecific.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DeviceSpecific implements IDeviceSpecific {

	public enum RequestType {
		Login,
		Version,
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
