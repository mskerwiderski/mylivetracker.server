package de.msk.mylivetracker.service.application;

/**
 * IApplicationService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IApplicationService {
	
	public enum Parameter {
		ApplicationBaseUrl, // application base url.
		ApplicationName, // application name.		
		ApplicationPort, // application port.
		ApplicationRealm, // application realm (used for user password encryption).
		AutoLoginTicketForDemoGuest, // auto-login ticket for guests of demo.
		ClientTcpPorts, // open tcp ports for clients.
		ClientUdpPorts, // open udp ports for clients.
		EvaluationModeEnabled, // if yes: only one registered user is allowed.
		GeocodingEnabled, // if no: geocoding is system wide disabled.
		RunCleanTasksAfterStartup, // if yes: clean tasks are started after application startup.
		RunDemoAfterStartup, // if yes: demo tracks are started after application startup.
		ServerAddress, // server address (dns or ip).
		SmsCreatorDePassword, // password of sms service smscreator.de.
		SmsCreatorDeUsername, // username of sms service smscreator.de.
		SupportEmail, // email address for contact MyLiveTracker support.
		TrackLifeTimeInMSecs, // lifetime of track records in milliseconds.
	};
				
	public void reloadParameters();
	
	public String getServerAddress();
	public String getApplicationBaseUrl();
	public Integer getApplicationPort();
	
	public boolean isTcpPortUsed(int port);
	public boolean isUdpPortUsed(int port);
	
	/**
	 * get parameter value as string.
	 * @param parameter - the parameter.
	 * @return Returns parameter value.
	 */
	public String getParameterValueAsString(Parameter parameter);		
	
	/**
	 * get parameter value as long.
	 * @param parameter - the parameter.
	 * @return Returns parameter value.
	 */
	public Long getParameterValueAsLong(Parameter parameter);
	
	/**
	 * get parameter value as boolean.
	 * @param parameter - the parameter.
	 * @return Returns parameter value.
	 */
	public Boolean getParameterValueAsBoolean(Parameter parameter);
}
