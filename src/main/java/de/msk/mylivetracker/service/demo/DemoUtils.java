package de.msk.mylivetracker.service.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;

/**
 * DemoUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoUtils {
	
	private static final Log log = LogFactory.getLog(DemoUtils.class);
	
	private static final String PARAM_STATUS = "status";
	private static final String PARAM_LONGITUDE = "lon";
	private static final String PARAM_LATITUDE = "lat";
	private static final String PARAM_LABEL = "label";
	private static final String PARAM_IMEI = "imei";
	private static final String PARAM_IN_CELL_ID = "incell";
	private static final String PARAM_IN_LOCAL_AREA_CODE = "inlac";
	private static final String PARAM_IN_NETWORK_NAME = "innwname";
	private static final String PARAM_IN_MOBILE_COUNTRY_CODE = "inmcc";
	private static final String PARAM_IN_MOBILE_NETWORK_CODE = "inmnc";
	private static final String PARAM_IN_MOBILE_MODE = "inmode";

	protected enum PositionStatus { 
		Accurate("A"), NotAccurate("V");
		
		private String mark;
		private PositionStatus(String mark) {
			this.mark = mark;
		}
		public String getMark() {
			return mark;
		}		
	};

	protected enum MobileNetworkProvider {
		GermanyO2("O2 - de", "262", "07"),
		GermanyTMobile("T-Mobile - de", "262", "01"),
		GermanyVodafone("Vodafone - de", "262", "02"),
		SwissSwisscomMobile("Swisscom - ch", "228", "01"),
		AustriaTMobile("T-Mobile - a", "232", "03");
		
		private String name;
		private String mcc;
		private String mnc;
		
		private MobileNetworkProvider(String name, String mcc, String mnc) {
			this.name = name;
			this.mcc = mcc;
			this.mnc = mnc;
		}

		public String getName() {
			return name;
		}

		public String getMcc() {
			return mcc;
		}

		public String getMnc() {
			return mnc;
		}				
	};
	
	protected enum MobileMode {
		Gsm(2), Umts(6);
		
		private Integer mode;
		private MobileMode(Integer mode) {
			this.mode = mode;
		}
		public Integer getMode() {
			return mode;
		}
	};

	protected static String sendCreateTrack(
		String senderId, String urlPrefix, String trackName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_IMEI, senderId);
		params.put(PARAM_LABEL, 
			DeviceActionExecutor.CreateTrack.getCode() + " " + trackName);
		return sendAux(urlPrefix, params);
	}
	
	protected static String sendCloseActiveTrack(
		String senderId, String urlPrefix) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_IMEI, senderId);
		params.put(PARAM_LABEL, DeviceActionExecutor.CloseTrack.getCode());
		return sendAux(urlPrefix, params);
	}

	protected static String sendMessage(
		String senderId, String urlPrefix, String message) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_IMEI, senderId);
		params.put(PARAM_LABEL, message);
		return sendAux(urlPrefix, params);
	}
	
	protected static String sendPositionAndMessage(
		String senderId, String urlPrefix,
		double lat, double lon, PositionStatus status,
		boolean mobNwCellReceived,
		Integer cellId, Integer localAreaCode,
		MobileNetworkProvider mobileNetworkProvider,
		MobileMode mobileModus,
		String message) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAM_IMEI, senderId);
		params.put(PARAM_LATITUDE, String.valueOf(lat));
		params.put(PARAM_LONGITUDE, String.valueOf(lon));
		params.put(PARAM_STATUS, status.getMark());
		if (mobNwCellReceived) {
			params.put(PARAM_IN_CELL_ID, cellId.toString());
			params.put(PARAM_IN_LOCAL_AREA_CODE, localAreaCode.toString());
			params.put(PARAM_IN_NETWORK_NAME, mobileNetworkProvider.getName());
			params.put(PARAM_IN_MOBILE_COUNTRY_CODE, mobileNetworkProvider.getMcc());
			params.put(PARAM_IN_MOBILE_NETWORK_CODE, mobileNetworkProvider.getMnc());
			params.put(PARAM_IN_MOBILE_MODE, mobileModus.getMode().toString());
		}
		if (!StringUtils.isEmpty(message)) {
			params.put(PARAM_LABEL, message);
		}
		
		return sendAux(urlPrefix, params);		
	}

	private static String sendAux(String urlPrefix, Map<String, String> params) {
		String res = null;
		try {
			String url = urlPrefix;
			for (String param : params.keySet()) {
				url += param + "=" + params.get(param) + "&";
			}
			url = StringUtils.left(url, url.length()-1);
			WebConversation wc = new WebConversation();
			log.debug("send url: " + url);
			WebResponse resp = wc.getResponse(url);
			res = resp.getText();
		} catch (Exception e) {
			e.printStackTrace();
			res = "send url failed: " + e.getMessage();
		}
		return res;
	}	


}
