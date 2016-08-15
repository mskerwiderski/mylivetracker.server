package de.msk.mylivetracker.service.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;

import de.cetix.SendSMS.Send;
import de.cetix.SendSMS.SendLocator;
import de.cetix.SendSMS.SendSoap;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.sms.ISmsService.SendingSmsResult;

public class SmsServiceSpecific {

	private static final String SERVICE_PROVIDER_SMSCREATORDE = "smscreator.de";
	private static final String SERVICE_PROVIDER_SMSTRADEDE = "smstrade.de";
	private static final String RESULT_CODE_UNKNOWN = "<unknown>";
	private static final String TRANSPORT_TYPE = "19";
	private static final String BACKMAIL = "backmail:";	
	private static final String NUMBER_STRIP_CHARS = "()/-";
	
	public static SendingSmsResult sendSms(UserWithoutRoleVo user, String recipient, String smsText, 
		long minPauseInMSecsForUserBetweenTwoSmsSent) {
		SendingSmsResult result = null;
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		if (StringUtils.isEmpty(smsText)) {
			throw new IllegalArgumentException("smsText must not be empty.");
		}
		if (StringUtils.isEmpty(user.getEmergency().getSmsServiceProvider())) {
			throw new IllegalArgumentException("smsServiceProvider must not be empty.");
		}
		if (!checkMinPause(user, minPauseInMSecsForUserBetweenTwoSmsSent)) {
			result = new SendingSmsResult(false, "checkMinPause failed");
		} else {
			String sender = StringUtils.replaceChars(
				user.getEmergency().getSmsSender(), NUMBER_STRIP_CHARS, null);
			recipient = StringUtils.replaceChars(recipient, NUMBER_STRIP_CHARS, null);
			ByteBuffer textByteBuffer = Charset.forName("ISO-8859-1").encode(smsText);
			CharBuffer textCharBuffer = Charset.forName("ISO-8859-1").decode(textByteBuffer);
			smsText = textCharBuffer.toString();
			if (StringUtils.equals(SERVICE_PROVIDER_SMSCREATORDE, 
				user.getEmergency().getSmsServiceProvider())) {
				result = sendSmsViaSmsCreatorDe(user, sender, recipient, smsText);
			} else if (StringUtils.equals(SERVICE_PROVIDER_SMSTRADEDE, 
				user.getEmergency().getSmsServiceProvider())) {
				result = sendSmsViaSmsTradeDe(user, sender, recipient, smsText);
			} else {
				throw new IllegalArgumentException("smsServiceProvider '" + user.getEmergency().getSmsServiceProvider() + "' is unknown.");
			}
		}
		return result;
	}
	
	private static SendingSmsResult sendSmsViaSmsCreatorDe(UserWithoutRoleVo user, String sender, String recipient, String smsText) {
		String resultCode = RESULT_CODE_UNKNOWN;
		try {
			Send service = new SendLocator();
			SendSoap sendSoap = service.getSendSoap();
			resultCode = sendSoap.sendText(
				user.getEmergency().getSmsServiceUsername(), 
				user.getEmergency().getSmsServicePassword(), 
				BACKMAIL + user.getMasterData().getEmailAddress(), 
				sender, 
				recipient,
				smsText, 
				TRANSPORT_TYPE,	 
				"");
		} catch (Exception e) {
			resultCode = e.getMessage();
		} 
		return new SendingSmsResult(StringUtils.startsWith(resultCode, "OK"), resultCode);
	}
	
	private static SendingSmsResult sendSmsViaSmsTradeDe(UserWithoutRoleVo user, String sender, String recipient, String smsText) {
		String resultCode = RESULT_CODE_UNKNOWN;
		try {
			String urlString = "http://gateway.smstrade.de?message=" + smsText + 
				"&to=" + recipient + "&from=" + sender + 
				"&key=" + user.getEmergency().getSmsServicePassword() +
				"&" + user.getEmergency().getSmsServiceParams(); 
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(
	        	new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null);
	        in.close();
	        if (StringUtils.equals(inputLine, "100")) {
	        	resultCode = "OK 100";
	        }
		} catch (Exception e) {
			resultCode = e.getMessage();
		} 
		return new SendingSmsResult(StringUtils.startsWith(resultCode, "OK"), resultCode);
	}
	
	private static boolean checkMinPause(UserWithoutRoleVo user,
		long minPauseInMSecsForUserBetweenTwoSmsSent) {
		if (user.getEmergency().getSmsLastSent() == null) {
			return true;
		}
		return (
			((new DateTime()).getAsMSecs() -
			user.getEmergency().getSmsLastSent().getAsMSecs()) > 
			minPauseInMSecsForUserBetweenTwoSmsSent);
	}
}
