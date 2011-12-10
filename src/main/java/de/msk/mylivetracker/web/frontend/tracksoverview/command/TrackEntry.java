package de.msk.mylivetracker.web.frontend.tracksoverview.command;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * TrackEntry.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackEntry implements Serializable {
		
	private static final long serialVersionUID = 3055143092178331447L;
	
	private SenderVo sender;
	private String senderNameAbbreviated;
	private TrackVo track;
	private String trackNameAbbreviated;
	private boolean editable = false;
	private boolean canBeActivated = false;
	
	private String distance;
	private String currSpeed;
	private String avgSpeedWithPauses;
	private String avgSpeedWithoutPauses;
	
	private String recPosStr;
	private String recMessageStr;
	private String recMobNwCellStr;	
	private String recCardiacFunctionStr;
	
	/**
	 * constructor.
	 * @param applicationService
	 * @param user
	 * @param track
	 */
	public TrackEntry(
		HttpServletRequest request,	
		IApplicationService applicationService,
		UserWithRoleVo user,
		SenderVo sender,
		TrackVo track) {
		this.sender = sender;
		this.track = track;
				
		this.senderNameAbbreviated = FmtUtils.getSenderLabel(
			sender, false, track.getSenderName());			
		this.trackNameAbbreviated = 
			StringUtils.abbreviate(track.getName(), 30);
				
		this.editable =  
			((user.getRole().equals(UserWithRoleVo.UserRole.Admin) ||
			user.getRole().equals(UserWithRoleVo.UserRole.User)));	
		this.canBeActivated = this.editable && (sender != null);
		
		this.distance = FmtUtils.getDistanceAsStr( 
			track.getDistanceInMtr(), UsersLocaleResolver.getScaleUnit(user), true);
		this.avgSpeedWithPauses = FmtUtils.getSpeedAsStr( 
			track.getAvgSpeedInMtrPerHourWithPauses(), UsersLocaleResolver.getScaleUnit(user));
		this.avgSpeedWithoutPauses = FmtUtils.getSpeedAsStr( 
			track.getAvgSpeedInMtrPerHourWithoutPauses(), UsersLocaleResolver.getScaleUnit(user));
		this.currSpeed = FmtUtils.getSpeedAsStr(
			track.getSpeedInMtrPerHour(), UsersLocaleResolver.getScaleUnit(user));
		this.recMessageStr = StringUtils.abbreviate(
			FmtUtils.getMessageAsStr(track.getRecentMessage(), false), 30);
		this.recPosStr = FmtUtils.getPositionAsStr(
			track.getRecentPosition(), UsersLocaleResolver.getScaleUnit(user), true, false, false); 		
		this.recMobNwCellStr = 
			FmtUtils.getMobNwCellAsStr(track.getRecentMobNwCell(), true);
		this.recCardiacFunctionStr = 
			FmtUtils.getCardiacFunctionAsStr(track.getRecentCardiacFunction());
	}
	
	/**
	 * @return the sender
	 */
	public SenderVo getSender() {
		return sender;
	}

	/**
	 * @return the track
	 */
	public TrackVo getTrack() {
		return track;
	}		

	/**
	 * @return the senderNameAbbreviated
	 */
	public String getSenderNameAbbreviated() {
		return senderNameAbbreviated;
	}

	/**
	 * @return the trackNameAbbreviated
	 */
	public String getTrackNameAbbreviated() {
		return trackNameAbbreviated;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @return the canBeActivated
	 */
	public boolean isCanBeActivated() {
		return canBeActivated;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @return the currSpeed
	 */
	public String getCurrSpeed() {
		return currSpeed;
	}	

	/**
	 * @return the avgSpeedWithPauses
	 */
	public String getAvgSpeedWithPauses() {
		return avgSpeedWithPauses;
	}

	/**
	 * @return the avgSpeedWithoutPauses
	 */
	public String getAvgSpeedWithoutPauses() {
		return avgSpeedWithoutPauses;
	}

	/**
	 * @return the recPosStr
	 */
	public String getRecPosStr() {
		return recPosStr;
	}

	/**
	 * @return the recMessageStr
	 */
	public String getRecMessageStr() {
		return recMessageStr;
	}

	/**
	 * @return the recMobNwCellStr
	 */
	public String getRecMobNwCellStr() {
		return recMobNwCellStr;
	}

	/**
	 * @return the recCardiacFunctionStr
	 */
	public String getRecCardiacFunctionStr() {
		return recCardiacFunctionStr;
	}	
}
