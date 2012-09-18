package de.msk.mylivetracker.service.geocoding.cloudmade.response;

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
	
	private int found;
	private Feature[] features;

	public int getFound() {
		return found;
	}

	public void setFound(int found) {
		this.found = found;
	}
	
	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeocodingResponse [found=" + found + "]";
	}
}
