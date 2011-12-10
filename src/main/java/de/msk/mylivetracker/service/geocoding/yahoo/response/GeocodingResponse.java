package de.msk.mylivetracker.service.geocoding.yahoo.response;

import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos;

/**
 * GeocodingResponse.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class GeocodingResponse {
	
	private ResultSet ResultSet;

	public String toAddressStr() {
		String res = "";
		if (ResultSet.getError() == 0) {
			res = ResultSet.getResults()[0].getAddressStr();			 
		} else {
			res = ResultSet.getErrorMessage();
		}
		return res;
	}
	
	public LatLonPos toLatLonPos() {
		LatLonPos res = null;
		if (ResultSet.getError() == 0) {
			res = new LatLonPos(
				Double.valueOf(ResultSet.getResults()[0].getLatitude()),
				Double.valueOf(ResultSet.getResults()[0].getLongitude()));			 
		} else {
			res = null;
		}
		return res;
	}
}
