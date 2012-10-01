package de.msk.mylivetracker.domain.statistics;

import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * ServiceCallVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-01
 * 
 */
public class ServiceCallVo extends AbstractStatisticVo {

	private static final long serialVersionUID = 8792703983984620395L;

	private String service;
	private String date;
	private String url;
	private String result;
	
	public ServiceCallVo() {
	}
	
	public static final String getDateStr(DateTime ...dateTimes) {
		DateTime dateTime = new DateTime();
		if (dateTimes.length > 0) {
			dateTime = dateTimes[0];
		}
		return dateTime.getAsStr(
			TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC), 
			DateTime.INTERNAL_DATE_FMT);
	}
	
	public ServiceCallVo(String service, String url, String result) {
		this.service = (StringUtils.isEmpty(service) ? UNKNOWN : service);
		this.date = getDateStr(this.getLogTimestamp());
		this.url = (StringUtils.isEmpty(url) ? EMPTY : url);
		this.result = (StringUtils.isEmpty(result) ? EMPTY_STRING : result);
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
