package de.msk.mylivetracker.service.geocoding;

import de.msk.mylivetracker.domain.statistics.ServiceCallCountVo;
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
	
	private String name;
	private String nameFailed = this.name + " (failed)";
	private int requestLimit;
	private IStatisticsService statisticsService;
	
	public static class LatLonPos {
		private Double latitude;
		private Double longitude;
		public LatLonPos(Double latitude, Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
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
		
	public String getAddressOfPosition(LatLonPos position, String languageCode) {
		String res = "";
		ServiceCallCountVo serviceCallCount = 
			this.statisticsService.getServiceCallCount(name);
		if (this.requestLimit > serviceCallCount.getCallCount()) {
			try {
				res = this.getAddressOfPositionAux(position, languageCode);
			} catch (Exception e) {
				res = "";
				this.statisticsService.logServiceCallCount(nameFailed);
			} finally {
				this.statisticsService.logServiceCallCount(name);
			}
		}				
		return res;
	}
	
	public LatLonPos getPositionOfAddress(String address, String languageCode) {
		LatLonPos res = null;
		ServiceCallCountVo serviceCallCount = 
			this.statisticsService.getServiceCallCount(name);
		if (this.requestLimit > serviceCallCount.getCallCount()) {
			try {
				res = this.getPositionOfAddressAux(address, languageCode);
			} catch (Exception e) {
				res = null;
				this.statisticsService.logServiceCallCount(nameFailed);
			} finally {
				this.statisticsService.logServiceCallCount(name);
			}
		}		
		return res;
	}
	
	public abstract String getAddressOfPositionAux(LatLonPos position, String languageCode);
	public abstract LatLonPos getPositionOfAddressAux(String address, String languageCode);

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
