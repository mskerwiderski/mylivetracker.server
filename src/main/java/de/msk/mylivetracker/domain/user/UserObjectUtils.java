package de.msk.mylivetracker.domain.user;

import java.util.UUID;

import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.security.PasswordEncoder;
import de.msk.mylivetracker.util.PwdUtils;

/**
 * UserUtils.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-06
 * 
 */
public class UserObjectUtils {

	public static final String DEF_USER_OPTIONS_TRACK_NAME = "MyTrack";

	public static CreateUserWithRoleResult createUserWithRole(
		String userId, String realm, UserRole role, 
		String language, String timeZone,
		String lastName, String firstName,
		String emailAddress, int senderLimit) {
		UserWithRoleVo user = new UserWithRoleVo();
		user.setUserId(userId);
		user.setRole(role);
		user.setSenderLimit(3);
		user.setEnabled(true);
		user.setRealm(realm);
		String plainPassword = PwdUtils.getPlainPassword();
		String hashedPassword = 
			PasswordEncoder.encode(
			userId, realm, 
			plainPassword);
		user.setMasterData(createUserMasterData(
			lastName, firstName, emailAddress, hashedPassword));
		user.setOptions(createUserOptions(language, timeZone));
		user.setAutoLogin(createUserAutoLogin());
		user.setEmergency(createUserEmergency());
		user.setStatusPage(createUserStatusPage());
		return new CreateUserWithRoleResult(user, plainPassword);
	}
	
	public static final String DEF_MAPS_USED_STR = 
		"0," + // default map id.
		"11000000000000000000000000000000"; // 32 maps, first two maps are enabled.
	
	private static UserOptionsVo createUserOptions(
		String language, String timeZone) {
		UserOptionsVo userOptions = new UserOptionsVo();
		userOptions.setDefTrackName(DEF_USER_OPTIONS_TRACK_NAME);
		userOptions.setDefTrackReleaseStatus(false);
		userOptions.setLanguage(language);
		userOptions.setScaleUnit(language);
		userOptions.setTimeZone(timeZone);
		userOptions.setMapsUsed(new MapsUsedVo(DEF_MAPS_USED_STR));
		userOptions.setGeocoderLanguage(language);
		userOptions.setGeocoderMode(GeocoderMode.emergencySignalsAndMessages);
		userOptions.setTrackAutoClose(0);
		userOptions.setTrackRouteColor("FF3300");
		userOptions.setTrackRouteWidth(3);
		userOptions.setRecTrAccCode(UUID.randomUUID().toString());
		userOptions.setRecTrAccEnabled(false);
		userOptions.setRecTrAccPrivTrEnabled(false);
		userOptions.setGuestAccClosedTrEnabled(false);
		userOptions.setGuestAccEnabled(false);
		userOptions.setGuestAccPassword(PwdUtils.getPlainPassword());
		userOptions.setGuestAccPrivTrEnabled(false);
		userOptions.setHomeLocAddress("");		
		userOptions.setHomeLocLatitude(null);
		userOptions.setHomeLocLongitude(null);
		return userOptions;
	}
	
	private static UserMasterDataVo createUserMasterData(
		String lastName, String firstName,
		String emailAddress, String hashedPassword) {
		UserMasterDataVo userMasterData = new UserMasterDataVo();
		userMasterData.setLastName(lastName);
		userMasterData.setFirstName(firstName);
		userMasterData.setEmailAddress(emailAddress);
		userMasterData.setNewsletterEnabled(true);
		userMasterData.setPassword(hashedPassword);
		return userMasterData;
	}
	
	private static UserAutoLoginVo createUserAutoLogin() {
		UserAutoLoginVo userAutoLogin = new UserAutoLoginVo();
		userAutoLogin.setAutoLoginEnabledForUser(false);
		userAutoLogin.setAutoLoginEnabledForGuest(false);
		userAutoLogin.setAutoLoginTicketForUser(
			UserAutoLoginVo.createAutoLoginTicket());
		userAutoLogin.setAutoLoginTicketForGuest(
			UserAutoLoginVo.createAutoLoginTicket());
		return userAutoLogin;
	}

	private static UserEmergencyVo createUserEmergency() {
		UserEmergencyVo userEmergency = new UserEmergencyVo();
		userEmergency.setSmsUnlocked(false);
		userEmergency.setSmsEnabled(false);
		userEmergency.setSmsSender("");
		userEmergency.setSmsRecipient("");
		userEmergency.setSmsLastSent(null);
		userEmergency.setSmsSentCount(0);
		return userEmergency;
	}
	
	private static final String CRLF = "\r\n";
	public static final String DEF_USER_STATUS_PAGE_CSS_STYLE =
		".track-status-content { background: #d4d0c8; font-family: Verdana,Arial,sans-serif; font-variant: small-caps; font-size: x-small;}" + CRLF +
		".track-status-username { color: red; font-weight: bold; }" + CRLF + 
		".track-status-trackname { color: green; font-weight: bold; }" + CRLF +	
		".track-status-noactivetrack { color: red; font-weight: bold }" + CRLF +
		".track-status-labels { color: blue; font-size: small; }" + CRLF +
		".track-status-values { color: #125be2; font-size: small; }" + CRLF +
		".track-status-footer { color: white; font-size: 10px; font-variant: normal; }";
	
	private static UserStatusPageVo createUserStatusPage() {
		UserStatusPageVo userStatusPage = new UserStatusPageVo();
		userStatusPage.setCssStyle(DEF_USER_STATUS_PAGE_CSS_STYLE);
		userStatusPage.setSenderId(null);
		userStatusPage.setTrackingLive(true);
		userStatusPage.setTrackingUpdateIntervalInSecs(10);
		userStatusPage.setTrackingKeepRecentPositions(-1);
		userStatusPage.setTrackingFlyToMode(TrackingFlyToMode.FlyToView);
		userStatusPage.setShowTrackInfo(true);
		userStatusPage.setFullScreen(false);
		userStatusPage.setWindowHeight(600);
		userStatusPage.setWindowWidth(800);
		userStatusPage.setLinkTrackAsStatusInfo("");
		userStatusPage.setLinkTrackAsMap("");
		return userStatusPage;
	}
	
	public static class CreateUserWithRoleResult {
		private UserWithRoleVo user;
		private String plainPassword;
		public CreateUserWithRoleResult(UserWithRoleVo user,
			String plainPassword) {
			this.user = user;
			this.plainPassword = plainPassword;
		}
		public UserWithRoleVo getUser() {
			return user;
		}
		public String getPlainPassword() {
			return plainPassword;
		}
		@Override
		public String toString() {
			return "CreateUserWithRoleResult [user=" + user
				+ ", plainPassword=" + plainPassword + "]";
		}
	}
}
