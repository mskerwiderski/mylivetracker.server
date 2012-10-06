package de.msk.mylivetracker.service.geocoding;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.domain.statistics.ServiceCallVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;

/**
 * AbstractGeocodingService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractGeocodingService {

	private static final Log log = LogFactory.getLog(AbstractGeocodingService.class);
	
	private static final String SERVICE_CALL_GET_ADDRESS_OF_POSITION_FAILED = "service call 'getAddressOfPosition(#p1, #p2)' FAILED";
	private static final String SERVICE_CALL_GET_POSITION_OF_ADDRESS_FAILED = "service call 'getPositionOfAddress(#p1, #p2)' FAILED";
	private static final String ERROR_MSG_PARAM1 = "#p1";
	private static final String ERROR_MSG_PARAM2 = "#p2";
	
	private String name;
	private int requestLimit;
	private IApplicationService applicationService;
	private IStatisticsService statisticsService;
	
	public static class LatLonPos {
		private Double latitude;
		private Double longitude;
		public LatLonPos(Double latitude, Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		public LatLonPos(String latitude, String longitude) {
			this.latitude = Double.valueOf(latitude);
			this.longitude = Double.valueOf(longitude);
		}
		public Double getLatitude() {
			return latitude;
		}
		public Double getLongitude() {
			return longitude;
		}
		@Override
		public String toString() {
			return latitude.toString() + "," + 
				longitude.toString();
		}				
	}
	
	public static class Address {
		String country;
		String city;
		String street;
		String housenumber;
		public Address(String country, String city, 
			String street, String housenumber) {
			this.country = country;
			this.city = city;
			this.street = street;
			this.housenumber = housenumber;
		}
		public String getCountry() {
			return country;
		}
		public String getCity() {
			return city;
		}
		public String getStreet() {
			return street;
		}
		public String getHousenumber() {
			return housenumber;
		}
		@Override
		public String toString() {
			return "Address [country=" + country + ", city=" + city
				+ ", street=" + street + ", housenumber=" + housenumber
				+ "]";
		}	
	}
	
	public String getAddressOfPosition(LatLonPos position, String languageCode) {
		String res = "";
		int serviceCallCount = 
			this.statisticsService.getServiceCallCountOfToday(name);
		if (this.requestLimit > serviceCallCount) {
			try {
				ServiceCallResult result = this.getAddressOfPositionAux(position, languageCode);
				if (result == null) {
					throw new RuntimeException("ServiceCallResult-object must not be null.");
				}
				res = result.getAddress();
				this.statisticsService.logServiceCall(new ServiceCallVo(
					this.name, result.getUrl(), res));
			} catch (Exception e) {
				log.fatal(e);
				res = "";
				String errorMsg = SERVICE_CALL_GET_ADDRESS_OF_POSITION_FAILED;
				errorMsg = StringUtils.replace(errorMsg, ERROR_MSG_PARAM1, position.toString());
				errorMsg = StringUtils.replace(errorMsg, ERROR_MSG_PARAM2, languageCode);
				this.statisticsService.logServiceCall(new ServiceCallVo(
					this.name, errorMsg, e.toString()));
			} 
		}				
		return res;
	}
	
	public LatLonPos getPositionOfAddress(Address address, String languageCode) {
		LatLonPos res = null;
		int serviceCallCount = 
			this.statisticsService.getServiceCallCountOfToday(name);
		if (this.requestLimit > serviceCallCount) {
			try {
				ServiceCallResult result = this.getPositionOfAddressAux(address, languageCode);
				if (result == null) {
					throw new RuntimeException("ServiceCallResult-object must not be null.");
				}
				res = result.getPosition();
				this.statisticsService.logServiceCall(new ServiceCallVo(
					this.name, result.getUrl(), res.toString()));
			} catch (Exception e) {
				log.fatal(e);
				res = null;
				String errorMsg = SERVICE_CALL_GET_POSITION_OF_ADDRESS_FAILED;
				errorMsg = StringUtils.replace(errorMsg, ERROR_MSG_PARAM1, address.toString());
				errorMsg = StringUtils.replace(errorMsg, ERROR_MSG_PARAM2, languageCode);
				this.statisticsService.logServiceCall(new ServiceCallVo(
					this.name, errorMsg, e.toString()));
			} 
		}		
		return res;
	}
	
	public static class ServiceCallResult {
		private String address;
		private LatLonPos position;
		private String url;
		public ServiceCallResult(String address, String url) {
			this.address = address;
			this.url = url;
		}
		public ServiceCallResult(LatLonPos position, String url) {
			this.position = position;
			this.url = url;
		}
		public String getAddress() {
			return address;
		}
		public LatLonPos getPosition() {
			return position;
		}
		public String getUrl() {
			return url;
		}
	}

	public abstract ServiceCallResult getAddressOfPositionAux(LatLonPos position, String languageCode);
	public abstract ServiceCallResult getPositionOfAddressAux(Address address, String languageCode);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the requestLimit
	 */
	public int getRequestLimit() {
		return requestLimit;
	}

	/**
	 * @param requestLimit the requestLimit to set
	 */
	public void setRequestLimit(int requestLimit) {
		this.requestLimit = requestLimit;
	}

	public IApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the statisticsService
	 */
	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}	
}
