package de.msk.mylivetracker.service.geocoding.google;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;

/**
 * GoogleGeocodingService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class GoogleGeocodingService extends AbstractGeocodingService {

	private IApplicationService applicationService;
		
	private static final String GOOGLE_URL_TEMPLATE = 
		"http://maps.google.com/maps/geo?key=$key&q=$pos&sensor=$sensor&output=$output&hl=$language";
	
	private static final String PARAM_KEY = "$key";
	private static final String PARAM_POS = "$pos";
	private static final String PARAM_SENSOR = "$sensor";
	private static final String PARAM_OUTPUT = "$output";	
	private static final String PARAM_LANGUAGE = "$language";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getAddressOfPositionAux(de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos, java.lang.String)
	 */
	@Override
	public String getAddressOfPositionAux(LatLonPos position, String languageCode) {
		if (position == null) {
			throw new IllegalArgumentException("position must not be null.");
		}
		if (!this.applicationService.getParameterValueAsBoolean(Parameter.GeocodingEnabled)) {
			return "Geocoding Service is switched off!";
		}
		if ((position.getLatitude() == null) || (position.getLongitude() == null)) {
			throw new IllegalArgumentException("latitude and/or longitude must not be null.");
		}
		return getGoogleGeocoderResult(position.toString(), languageCode);							
	}	

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getPositionOfAddressAux(java.lang.String, java.lang.String)
	 */
	@Override
	public LatLonPos getPositionOfAddressAux(String address, String languageCode) {
		if (StringUtils.isEmpty(address)) {
			throw new IllegalArgumentException("pos must not be null.");
		}
		if (!this.applicationService.getParameterValueAsBoolean(Parameter.GeocodingEnabled)) {
			return null;
		}
		LatLonPos res = null;
		String resp = getGoogleGeocoderResult(address, languageCode);
		String[] respArr = StringUtils.split(resp, ',');
		if ((respArr != null) && (respArr.length == 2)) {
			try {
				res = new LatLonPos(
					Double.valueOf(respArr[0]),
					Double.valueOf(respArr[1]));
			} catch (Exception e) {
				e.printStackTrace();
				res = null;
			}
		}
		return res;
	}
	
	private String getGoogleGeocoderResult(String pos, String languageCode) {
		if (StringUtils.isEmpty(pos)) {
			throw new IllegalArgumentException("pos must not be null.");
		} 
		String res = null;
		String urlStr = GOOGLE_URL_TEMPLATE;
		urlStr = StringUtils.replace(urlStr, PARAM_KEY, 
			applicationService.getGoogleApiKey());
		urlStr = StringUtils.replace(urlStr, PARAM_POS, StringEscapeUtils.escapeHtml(pos));
		urlStr = StringUtils.replace(urlStr, PARAM_SENSOR, "true");
		urlStr = StringUtils.replace(urlStr, PARAM_OUTPUT, "csv");
		urlStr = StringUtils.replace(urlStr, PARAM_LANGUAGE, languageCode);
		
		try {
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
			String[] respArr = StringUtils.split(resp, ',');
			if ((respArr.length >=3) && StringUtils.equals(respArr[0], "200")) {
				String cutStr = respArr[0] + "," + respArr[1] + ",";
				resp = StringUtils.remove(resp, cutStr);
				resp = StringUtils.remove(resp, '"');
				resp = StringUtils.trimToNull(resp);
				res = resp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		}
		
		return res;			
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}	
}
