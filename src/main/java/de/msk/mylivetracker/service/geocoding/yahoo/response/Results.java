package de.msk.mylivetracker.service.geocoding.yahoo.response;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Results.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class Results implements Serializable {

	private static final long serialVersionUID = 3481936836623463483L;
	
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String latitude;
	private String longitude;
	
	public String getAddressStr() {
		String res = "";
		if (!StringUtils.isEmpty(line1)) {
			res = line1;
		}
		if (!StringUtils.isEmpty(line2)) {
			if (!StringUtils.isEmpty(res)) {
				res += ", ";
			}
			res += line2;
		}
		if (!StringUtils.isEmpty(line3)) {
			if (!StringUtils.isEmpty(res)) {
				res += ", ";
			}
			res += line3;
		}
		if (!StringUtils.isEmpty(line4)) {
			if (!StringUtils.isEmpty(res)) {
				res += ", ";
			}
			res += line4;
		}
		return res;
	}
	
	/**
	 * @return the line1
	 */
	public String getLine1() {
		return line1;
	}
	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	/**
	 * @return the line2
	 */
	public String getLine2() {
		return line2;
	}
	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	/**
	 * @return the line3
	 */
	public String getLine3() {
		return line3;
	}
	/**
	 * @param line3 the line3 to set
	 */
	public void setLine3(String line3) {
		this.line3 = line3;
	}
	/**
	 * @return the line4
	 */
	public String getLine4() {
		return line4;
	}
	/**
	 * @param line4 the line4 to set
	 */
	public void setLine4(String line4) {
		this.line4 = line4;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}	
	
}
