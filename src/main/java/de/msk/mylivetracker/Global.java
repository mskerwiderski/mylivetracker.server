package de.msk.mylivetracker;

/**
 * Global.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history 000 initial 2011-08-11
 * 
 */
public class Global {

	/**
	 * Instantiates a new global.
	 */
	private Global() {

	}

	public static String getVersion() {
		return "v1.4.0 RC1";
	}
	
	public static String getAppId() {
		return "MyLiveTracker";
	}
	
	public static String getAppIdAndVersion() {
		return getAppId() + " " + getVersion();
	}
	
	public static String getLink() {
		return "http://www.mylivetracker.de";
	}
}
