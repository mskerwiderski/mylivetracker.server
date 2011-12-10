package de.msk.mylivetracker.domain.statistics;

import java.util.TimeZone;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * ServiceCallCountVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ServiceCallCountVo extends AbstractStatisticVo {

	private static final long serialVersionUID = 8792703983984620395L;

	private String service;
	private String date;
	private int callCount;
	
	public static String getDayStr() {
		DateTime dateTime = new DateTime();
		return dateTime.getAsStr(
			TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC), 
			DateTime.STD_DATE_FORMAT);		
	}
	
	public static ServiceCallCountVo createInstance(String service, int callCount) {
		ServiceCallCountVo serviceCallCount = new ServiceCallCountVo();
		serviceCallCount.service = service;
		serviceCallCount.date = getDayStr();
		serviceCallCount.callCount = callCount;
		return serviceCallCount;
	}
		
	public ServiceCallCountVo() {
	}
	
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}	
	/**
	 * @return the callCount
	 */
	public int getCallCount() {
		return callCount;
	}
	/**
	 * @param callCount the callCount to set
	 */
	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}	
}
