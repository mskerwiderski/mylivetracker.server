package de.msk.mylivetracker.service.geocoding.nominatim;

/**
 * ReverseGeocoderResponse.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-23
 * 
 */
public class ReverseGeocoderResponse {
	private Address address;

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReverseGeocoderResponse [address=" + address + "]";
	}
}
