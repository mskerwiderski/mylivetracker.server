package de.msk.mylivetracker.domain.demo;

import java.io.Serializable;

/**
 * DemoPositionVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoPositionVo implements Serializable {
		
	private static final long serialVersionUID = 7653232413676234005L;
	
	private String trackId;
	private String trackName;
	private Double latitudeInDecimal;
	private Double longitudeInDecimal;
	private Double altitudeInMtr;
	private String message;
	private String address;
	private String senderState;
	private String mobileCountryCode;
	private String mobileNetworkCode;
	private String mobileNetworkName;
	private Integer mobileCellId;
	private Integer mobileLocalAreaCode;
	private Integer heartrateInBpm;
	private Integer heartrateMinInBpm;
	private Integer heartrateMaxInBpm;
	private Integer heartrateAvgInBpm;	
	private Long offsetInSeconds;

	public DemoPositionVo() {
	}

	public DemoPositionVo(String trackId, String trackName) {
		this.trackId = trackId;
		this.trackName = trackName;
	}
	/**
	 * @return the trackId
	 */
	public String getTrackId() {
		return trackId;
	}
	/**
	 * @param trackId the trackId to set
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	/**
	 * @return the trackName
	 */
	public String getTrackName() {
		return trackName;
	}
	/**
	 * @param trackName the trackName to set
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	/**
	 * @return the latitudeInDecimal
	 */
	public Double getLatitudeInDecimal() {
		return latitudeInDecimal;
	}
	/**
	 * @param latitudeInDecimal the latitudeInDecimal to set
	 */
	public void setLatitudeInDecimal(Double latitudeInDecimal) {
		this.latitudeInDecimal = latitudeInDecimal;
	}
	/**
	 * @return the longitudeInDecimal
	 */
	public Double getLongitudeInDecimal() {
		return longitudeInDecimal;
	}
	/**
	 * @param longitudeInDecimal the longitudeInDecimal to set
	 */
	public void setLongitudeInDecimal(Double longitudeInDecimal) {
		this.longitudeInDecimal = longitudeInDecimal;
	}
	/**
	 * @return the altitudeInMtr
	 */
	public Double getAltitudeInMtr() {
		return altitudeInMtr;
	}
	/**
	 * @param altitudeInMtr the altitudeInMtr to set
	 */
	public void setAltitudeInMtr(Double altitudeInMtr) {
		this.altitudeInMtr = altitudeInMtr;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the senderState
	 */
	public String getSenderState() {
		return senderState;
	}
	/**
	 * @param senderState the senderState to set
	 */
	public void setSenderState(String senderState) {
		this.senderState = senderState;
	}
	/**
	 * @return the mobileCountryCode
	 */
	public String getMobileCountryCode() {
		return mobileCountryCode;
	}
	/**
	 * @param mobileCountryCode the mobileCountryCode to set
	 */
	public void setMobileCountryCode(String mobileCountryCode) {
		this.mobileCountryCode = mobileCountryCode;
	}
	/**
	 * @return the mobileNetworkCode
	 */
	public String getMobileNetworkCode() {
		return mobileNetworkCode;
	}
	/**
	 * @param mobileNetworkCode the mobileNetworkCode to set
	 */
	public void setMobileNetworkCode(String mobileNetworkCode) {
		this.mobileNetworkCode = mobileNetworkCode;
	}
	/**
	 * @return the mobileNetworkName
	 */
	public String getMobileNetworkName() {
		return mobileNetworkName;
	}
	/**
	 * @param mobileNetworkName the mobileNetworkName to set
	 */
	public void setMobileNetworkName(String mobileNetworkName) {
		this.mobileNetworkName = mobileNetworkName;
	}
	/**
	 * @return the mobileCellId
	 */
	public Integer getMobileCellId() {
		return mobileCellId;
	}
	/**
	 * @param mobileCellId the mobileCellId to set
	 */
	public void setMobileCellId(Integer mobileCellId) {
		this.mobileCellId = mobileCellId;
	}
	/**
	 * @return the mobileLocalAreaCode
	 */
	public Integer getMobileLocalAreaCode() {
		return mobileLocalAreaCode;
	}
	/**
	 * @param mobileLocalAreaCode the mobileLocalAreaCode to set
	 */
	public void setMobileLocalAreaCode(Integer mobileLocalAreaCode) {
		this.mobileLocalAreaCode = mobileLocalAreaCode;
	}
	/**
	 * @return the heartrateInBpm
	 */
	public Integer getHeartrateInBpm() {
		return heartrateInBpm;
	}
	/**
	 * @param heartrateInBpm the heartrateInBpm to set
	 */
	public void setHeartrateInBpm(Integer heartrateInBpm) {
		this.heartrateInBpm = heartrateInBpm;
	}
	/**
	 * @return the heartrateMinInBpm
	 */
	public Integer getHeartrateMinInBpm() {
		return heartrateMinInBpm;
	}
	/**
	 * @param heartrateMinInBpm the heartrateMinInBpm to set
	 */
	public void setHeartrateMinInBpm(Integer heartrateMinInBpm) {
		this.heartrateMinInBpm = heartrateMinInBpm;
	}
	/**
	 * @return the heartrateMaxInBpm
	 */
	public Integer getHeartrateMaxInBpm() {
		return heartrateMaxInBpm;
	}
	/**
	 * @param heartrateMaxInBpm the heartrateMaxInBpm to set
	 */
	public void setHeartrateMaxInBpm(Integer heartrateMaxInBpm) {
		this.heartrateMaxInBpm = heartrateMaxInBpm;
	}
	/**
	 * @return the heartrateAvgInBpm
	 */
	public Integer getHeartrateAvgInBpm() {
		return heartrateAvgInBpm;
	}
	/**
	 * @param heartrateAvgInBpm the heartrateAvgInBpm to set
	 */
	public void setHeartrateAvgInBpm(Integer heartrateAvgInBpm) {
		this.heartrateAvgInBpm = heartrateAvgInBpm;
	}
	/**
	 * @return the offsetInSeconds
	 */
	public Long getOffsetInSeconds() {
		return offsetInSeconds;
	}
	/**
	 * @param offsetInSeconds the offsetInSeconds to set
	 */
	public void setOffsetInSeconds(Long offsetInSeconds) {
		this.offsetInSeconds = offsetInSeconds;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DemoPositionVo [latitudeInDecimal=" + latitudeInDecimal
			+ ", longitudeInDecimal=" + longitudeInDecimal
			+ ", altitudeInMtr=" + altitudeInMtr + ", message=" + message
			+ ", address=" + address + ", senderState=" + senderState
			+ ", mobileCountryCode=" + mobileCountryCode
			+ ", mobileNetworkCode=" + mobileNetworkCode
			+ ", mobileNetworkName=" + mobileNetworkName
			+ ", mobileCellId=" + mobileCellId + ", mobileLocalAreaCode="
			+ mobileLocalAreaCode + ", heartrateInBpm=" + heartrateInBpm
			+ ", heartrateMinInBpm=" + heartrateMinInBpm
			+ ", heartrateMaxInBpm=" + heartrateMaxInBpm
			+ ", heartrateAvgInBpm=" + heartrateAvgInBpm
			+ ", offsetInSeconds=" + offsetInSeconds + "]";
	}	
}
