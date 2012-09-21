package de.msk.mylivetracker.service;

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
		ServerAddress, // server address (dns or ip).
		ApplicationBaseUrl, // application base url.
		ApplicationName, // application name.		
		ApplicationPort, // application port.
		ApplicationRealm, // application realm (used for user password encryption).
		EvaluationModeEnabled, // if yes: only one registered user is allowed.
		GeocodingEnabled, // if no: geocoding is system wide disabled.
		LogLifeTimeInMSecs, // lifetime of log records in milliseconds.
		RunDemoAfterStartup, // if yes: demo tracks are started after application startup.
		SmsCreatorDePassword, // password of sms service smscreator.de.
		SmsCreatorDeUsername, // username of sms service smscreator.de.
		TrackLifeTimeInMSecs, // lifetime of track records in milliseconds.
		YahooApiKey, // yahoo api key (needed for geocoder).		
		CloudmadeApiKey, // cloudmade api key (needed for cloudmade api).
		RunCleanTasksAfterStartup, // if yes: clean tasks are started after application startup.
	};
				
	public void reloadParameters();
	
	public String getServerAddress();
	public String getApplicationBaseUrl();
	public Integer getApplicationPort();
	
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