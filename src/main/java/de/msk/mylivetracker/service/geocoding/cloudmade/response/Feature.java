package de.msk.mylivetracker.service.geocoding.cloudmade.response;

/**
 * Feature.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-08-20
 * 
 */
public class Feature {

	private Centroid centroid;
	private Properties properties;
	private Location location;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Centroid getCentroid() {
		return centroid;
	}

	public void setCentroid(Centroid centroid) {
		this.centroid = centroid;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
