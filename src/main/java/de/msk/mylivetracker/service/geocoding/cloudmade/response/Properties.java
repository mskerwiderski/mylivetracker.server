package de.msk.mylivetracker.service.geocoding.cloudmade.response;

/**
 * Properties.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class Properties {
	private String osm_id;
	private String addr_country;
	private String addr_city;
	private String addr_street;
	private String addr_housenumber;
	public String getOsm_id() {
		return osm_id;
	}
	public void setOsm_id(String osm_id) {
		this.osm_id = osm_id;
	}
	public String getAddr_country() {
		return addr_country;
	}
	public void setAddr_country(String addr_country) {
		this.addr_country = addr_country;
	}
	public String getAddr_city() {
		return addr_city;
	}
	public void setAddr_city(String addr_city) {
		this.addr_city = addr_city;
	}
	public String getAddr_street() {
		return addr_street;
	}
	public void setAddr_street(String addr_street) {
		this.addr_street = addr_street;
	}
	public String getAddr_housenumber() {
		return addr_housenumber;
	}
	public void setAddr_housenumber(String addr_housenumber) {
		this.addr_housenumber = addr_housenumber;
	}
}
