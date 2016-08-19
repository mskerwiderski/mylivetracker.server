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
		return "v1.5.1";
	}
	
	public static String getAppId() {
		return "MyLiveTracker";
	}
	
	public static String getAppIdAndVersion() {
		return getAppId() + " " + getVersion();
	}
	
	public static String getPortalLink() {
		return "http://portal.mylivetracker.de";
	}
	
	public static String getForumLink() {
		return "http://forum.mylivetracker.info";
	}
}
