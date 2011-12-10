package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * MobNwCellVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class MobNwCellVo implements Serializable {
	
	private static final long serialVersionUID = -3933507509032389084L;
	
	private String mobNwCellId = UUID.randomUUID().toString();
	private TrackVo track;
	private PositionVo position;	
	private Integer cellId;
	private Integer localAreaCode;
	private String networkName;
	private String mobileCountryCode;
	private String mobileNetworkCode;
	private Integer mobileMode;
	private DateTime timeReceived;
	
	public MobNwCellVo() {
	}

	public MobNwCellVo(
			Integer cellId, Integer localAreaCode, String networkName,
			String mobileCountryCode, String mobileNetworkCode,
			Integer mobileMode, DateTime timeReceived) {		
		this.cellId = cellId;
		this.localAreaCode = localAreaCode;
		this.networkName = networkName;
		this.mobileCountryCode = mobileCountryCode;
		this.mobileNetworkCode = mobileNetworkCode;
		this.mobileMode = mobileMode;
		this.timeReceived = timeReceived;
	}

	public boolean isValid() {
		return 
			!StringUtils.isEmpty(this.mobNwCellId) &&
			(this.timeReceived != null) &&
			(this.mobileCountryCode != null) &&
			(this.mobileNetworkCode != null) &&
			(this.localAreaCode != null) &&
			(this.cellId != null);
	}
	
	/**
	 * @return the mobNwCellId
	 */
	public String getMobNwCellId() {
		return mobNwCellId;
	}

	/**
	 * @param mobNwCellId the mobNwCellId to set
	 */
	public void setMobNwCellId(String mobNwCellId) {
		this.mobNwCellId = mobNwCellId;
	}	

	/**
	 * @return the track
	 */
	public TrackVo getTrack() {
		return track;
	}

	/**
	 * @param track the track to set
	 */
	public void setTrack(TrackVo track) {
		this.track = track;
	}

	/**
	 * @return the position
	 */
	public PositionVo getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(PositionVo position) {
		this.position = position;
	}

	/**
	 * @return the cellId
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the localAreaCode
	 */
	public Integer getLocalAreaCode() {
		return localAreaCode;
	}

	/**
	 * @param localAreaCode the localAreaCode to set
	 */
	public void setLocalAreaCode(Integer localAreaCode) {
		this.localAreaCode = localAreaCode;
	}

	/**
	 * @return the networkName
	 */
	public String getNetworkName() {
		return networkName;
	}

	/**
	 * @param networkName the networkName to set
	 */
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
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
	 * @return the mobileMode
	 */
	public Integer getMobileMode() {
		return mobileMode;
	}

	/**
	 * @param mobileMode the mobileMode to set
	 */
	public void setMobileMode(Integer mobileMode) {
		this.mobileMode = mobileMode;
	}

	/**
	 * @return the timeReceived
	 */
	public DateTime getTimeReceived() {
		return timeReceived;
	}

	/**
	 * @param timeReceived the timeReceived to set
	 */
	public void setTimeReceived(DateTime timeReceived) {
		this.timeReceived = timeReceived;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MobNwCellVo [timeReceived=" + timeReceived
				+ ", mobileCountryCode=" + mobileCountryCode
				+ ", mobileNetworkCode=" + mobileNetworkCode + ", networkName="
				+ networkName + ", localAreaCode=" + localAreaCode
				+ ", cellId=" + cellId + ", mobileMode=" + mobileMode + "]";
	}
}
