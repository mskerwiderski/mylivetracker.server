package de.msk.mylivetracker.service.geocoding;

import de.msk.mylivetracker.domain.statistics.ServiceCallCountVo;
import de.msk.mylivetracker.service.IApplicationService;
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

	private static final String FAILED_APP = " (failed)";
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
		ServiceCallCountVo serviceCallCount = 
			this.statisticsService.getServiceCallCount(name);
		if (this.requestLimit > serviceCallCount.getCallCount()) {
			try {
				res = this.getAddressOfPositionAux(position, languageCode);
			} catch (Exception e) {
				res = "";
				this.statisticsService.logServiceCallCount(name + FAILED_APP);
			} finally {
				this.statisticsService.logServiceCallCount(name);
			}
		}				
		return res;
	}
	
	public LatLonPos getPositionOfAddress(Address address, String languageCode) {
		LatLonPos res = null;
		ServiceCallCountVo serviceCallCount = 
			this.statisticsService.getServiceCallCount(name);
		if (this.requestLimit > serviceCallCount.getCallCount()) {
			try {
				res = this.getPositionOfAddressAux(address, languageCode);
			} catch (Exception e) {
				res = null;
				this.statisticsService.logServiceCallCount(name + FAILED_APP);
			} finally {
				this.statisticsService.logServiceCallCount(name);
			}
		}		
		return res;
	}
	
	public abstract String getAddressOfPositionAux(LatLonPos position, String languageCode);
	public abstract LatLonPos getPositionOfAddressAux(Address address, String languageCode);

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
