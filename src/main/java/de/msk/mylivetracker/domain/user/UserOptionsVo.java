package de.msk.mylivetracker.domain.user;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.util.PwdUtils;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * UserOptionsVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserOptionsVo implements Cloneable, Serializable {
	
	private static final long serialVersionUID = -8532173448835158813L;
	
	private Boolean optionsSaved;
	private String timeZone;
	private String language;	
	private String googleLanguage;
	private String scaleUnit;
	private String defTrackName;
	private Boolean defTrackReleaseStatus;
	private Integer trackAutoClose;
	private Boolean guestAccEnabled;
	private String guestAccPassword;
	private Boolean guestAccClosedTrEnabled;
	private Boolean guestAccPrivTrEnabled;
	private Boolean recTrAccEnabled;
	private String recTrAccCode;
	private Boolean recTrAccPrivTrEnabled;
	private String trackRouteColor;
	private Integer trackRouteWidth;
	private Boolean geocodingEnabled;
	private String homeLocAddress;
	private Double homeLocLatitude;
	private Double homeLocLongitude;
	
	public static final String CODE_DEF_TRACK_NAME = 
		"options.track.defname.default";
	
	public void setDefaultValues(MessageSource messageSource) {	
		optionsSaved = true;
		timeZone = DateTime.TIME_ZONE_UTC;
		language = UsersLocaleResolver.DEFAULT_LOCALE.getLanguage();
		googleLanguage = UsersLocaleResolver.DEFAULT_LOCALE.getLanguage();
		scaleUnit = UsersLocaleResolver.DEFAULT_LOCALE.getLanguage();
		defTrackName = messageSource.getMessage(
			CODE_DEF_TRACK_NAME, null, UsersLocaleResolver.DEFAULT_LOCALE);
		defTrackReleaseStatus = false;
		trackAutoClose = 0;
		
		guestAccEnabled = false;
		guestAccPassword = PwdUtils.getPlainPassword();
		guestAccClosedTrEnabled = false;
		guestAccPrivTrEnabled = false;
		
		recTrAccEnabled = false;
		recTrAccCode = UUID.randomUUID().toString();
		recTrAccPrivTrEnabled = false;
		
		trackRouteColor = "FF3300";
		trackRouteWidth = 3;	
		
		geocodingEnabled = false;
		
		homeLocAddress = "Munich, Germany";
		homeLocLatitude = 48.1391265d;
		homeLocLongitude = 11.5801863d;
	}
		
	public UserOptionsVo copy() {
		UserOptionsVo options = null;
		try {
			options = (UserOptionsVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return options;
	}

	/**
	 * @return the optionsSaved
	 */
	public Boolean getOptionsSaved() {
		return optionsSaved;
	}

	/**
	 * @param optionsSaved the optionsSaved to set
	 */
	public void setOptionsSaved(Boolean optionsSaved) {
		this.optionsSaved = optionsSaved;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the googleLanguage
	 */
	public String getGoogleLanguage() {
		return googleLanguage;
	}

	/**
	 * @param googleLanguage the googleLanguage to set
	 */
	public void setGoogleLanguage(String googleLanguage) {
		this.googleLanguage = googleLanguage;
	}

	/**
	 * @return the scaleUnit
	 */
	public String getScaleUnit() {
		return scaleUnit;
	}

	/**
	 * @param scaleUnit the scaleUnit to set
	 */
	public void setScaleUnit(String scaleUnit) {
		this.scaleUnit = scaleUnit;
	}

	/**
	 * @return the defTrackName
	 */
	public String getDefTrackName() {
		return defTrackName;
	}

	/**
	 * @param defTrackName the defTrackName to set
	 */
	public void setDefTrackName(String defTrackName) {
		this.defTrackName = defTrackName;
	}

	/**
	 * @return the defTrackReleaseStatus
	 */
	public Boolean getDefTrackReleaseStatus() {
		return defTrackReleaseStatus;
	}

	/**
	 * @param defTrackReleaseStatus the defTrackReleaseStatus to set
	 */
	public void setDefTrackReleaseStatus(Boolean defTrackReleaseStatus) {
		this.defTrackReleaseStatus = defTrackReleaseStatus;
	}

	/**
	 * @return the trackAutoClose
	 */
	public Integer getTrackAutoClose() {
		return trackAutoClose;
	}

	/**
	 * @param trackAutoClose the trackAutoClose to set
	 */
	public void setTrackAutoClose(Integer trackAutoClose) {
		this.trackAutoClose = trackAutoClose;
	}

	/**
	 * @return the guestAccEnabled
	 */
	public Boolean getGuestAccEnabled() {
		return guestAccEnabled;
	}

	/**
	 * @param guestAccEnabled the guestAccEnabled to set
	 */
	public void setGuestAccEnabled(Boolean guestAccEnabled) {
		this.guestAccEnabled = guestAccEnabled;
	}

	/**
	 * @return the guestAccPassword
	 */
	public String getGuestAccPassword() {
		return guestAccPassword;
	}

	/**
	 * @param guestAccPassword the guestAccPassword to set
	 */
	public void setGuestAccPassword(String guestAccPassword) {
		this.guestAccPassword = guestAccPassword;
	}

	/**
	 * @return the guestAccClosedTrEnabled
	 */
	public Boolean getGuestAccClosedTrEnabled() {
		return guestAccClosedTrEnabled;
	}

	/**
	 * @param guestAccClosedTrEnabled the guestAccClosedTrEnabled to set
	 */
	public void setGuestAccClosedTrEnabled(Boolean guestAccClosedTrEnabled) {
		this.guestAccClosedTrEnabled = guestAccClosedTrEnabled;
	}

	/**
	 * @return the guestAccPrivTrEnabled
	 */
	public Boolean getGuestAccPrivTrEnabled() {
		return guestAccPrivTrEnabled;
	}

	/**
	 * @param guestAccPrivTrEnabled the guestAccPrivTrEnabled to set
	 */
	public void setGuestAccPrivTrEnabled(Boolean guestAccPrivTrEnabled) {
		this.guestAccPrivTrEnabled = guestAccPrivTrEnabled;
	}

	/**
	 * @return the recTrAccEnabled
	 */
	public Boolean getRecTrAccEnabled() {
		return recTrAccEnabled;
	}

	/**
	 * @param recTrAccEnabled the recTrAccEnabled to set
	 */
	public void setRecTrAccEnabled(Boolean recTrAccEnabled) {
		this.recTrAccEnabled = recTrAccEnabled;
	}

	/**
	 * @return the recTrAccCode
	 */
	public String getRecTrAccCode() {
		return recTrAccCode;
	}

	/**
	 * @param recTrAccCode the recTrAccCode to set
	 */
	public void setRecTrAccCode(String recTrAccCode) {
		this.recTrAccCode = recTrAccCode;
	}

	/**
	 * @return the recTrAccPrivTrEnabled
	 */
	public Boolean getRecTrAccPrivTrEnabled() {
		return recTrAccPrivTrEnabled;
	}

	/**
	 * @param recTrAccPrivTrEnabled the recTrAccPrivTrEnabled to set
	 */
	public void setRecTrAccPrivTrEnabled(Boolean recTrAccPrivTrEnabled) {
		this.recTrAccPrivTrEnabled = recTrAccPrivTrEnabled;
	}

	/**
	 * @return the trackRouteColor
	 */
	public String getTrackRouteColor() {
		return trackRouteColor;
	}

	/**
	 * @param trackRouteColor the trackRouteColor to set
	 */
	public void setTrackRouteColor(String trackRouteColor) {
		this.trackRouteColor = trackRouteColor;
	}

	/**
	 * @return the trackRouteWidth
	 */
	public Integer getTrackRouteWidth() {
		return trackRouteWidth;
	}

	/**
	 * @param trackRouteWidth the trackRouteWidth to set
	 */
	public void setTrackRouteWidth(Integer trackRouteWidth) {
		this.trackRouteWidth = trackRouteWidth;
	}

	/**
	 * @return the geocodingEnabled
	 */
	public Boolean getGeocodingEnabled() {
		return geocodingEnabled;
	}

	/**
	 * @param geocodingEnabled the geocodingEnabled to set
	 */
	public void setGeocodingEnabled(Boolean geocodingEnabled) {
		this.geocodingEnabled = geocodingEnabled;
	}

	/**
	 * @return the homeLocAddress
	 */
	public String getHomeLocAddress() {
		return homeLocAddress;
	}

	/**
	 * @param homeLocAddress the homeLocAddress to set
	 */
	public void setHomeLocAddress(String homeLocAddress) {
		this.homeLocAddress = homeLocAddress;
	}

	/**
	 * @return the homeLocLatitude
	 */
	public Double getHomeLocLatitude() {
		return homeLocLatitude;
	}

	/**
	 * @param homeLocLatitude the homeLocLatitude to set
	 */
	public void setHomeLocLatitude(Double homeLocLatitude) {
		this.homeLocLatitude = homeLocLatitude;
	}

	/**
	 * @return the homeLocLongitude
	 */
	public Double getHomeLocLongitude() {
		return homeLocLongitude;
	}

	/**
	 * @param homeLocLongitude the homeLocLongitude to set
	 */
	public void setHomeLocLongitude(Double homeLocLongitude) {
		this.homeLocLongitude = homeLocLongitude;
	}
}
