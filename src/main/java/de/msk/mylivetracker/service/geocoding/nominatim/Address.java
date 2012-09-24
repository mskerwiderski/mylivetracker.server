package de.msk.mylivetracker.service.geocoding.nominatim;

/**
 * Address.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-23
 * 
 */
public class Address {
	String house_number;
	String road;
	String city;
	String country;
	/**
	 * @return the house_number
	 */
	public String getHouse_number() {
		return house_number;
	}
	/**
	 * @param house_number the house_number to set
	 */
	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}
	/**
	 * @return the road
	 */
	public String getRoad() {
		return road;
	}
	/**
	 * @param road the road to set
	 */
	public void setRoad(String road) {
		this.road = road;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Address [house_number=" + house_number + ", road=" + road
			+ ", city=" + city + ", country=" + country + "]";
	}
}
