package de.msk.mylivetracker.service.geocoding.yahoo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.yahoo.response.GeocodingResponse;

/**
 * GeocodingService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class GeocodingService extends AbstractGeocodingService {
		
	private static final Log log = LogFactory.getLog(GeocodingService.class);
	
	private IApplicationService applicationService;
	
	private static final String PARAM_SRC_Q = "#SRC#";
	private static final String PARAM_LOCALE = "#LOC#";
	private static final String PARAM_APPID = "#APPID#";
	
	private static final String URL_GEOCODE_TEMPL = 
		"http://where.yahooapis.com/geocode?"
		+ "q=#SRC#&gflags=R&locale=#LOC#&flags=J&"
		+ "appid=#APPID#";
		
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
		return getYahooGeocoderResult(position.toString(), languageCode).toAddressStr();	
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
		return getYahooGeocoderResult(address, languageCode).toLatLonPos();
	}

	private GeocodingResponse getYahooGeocoderResult(String src, String languageCode) {
		if (StringUtils.isEmpty(src)) {
			throw new IllegalArgumentException("src must not be null.");
		} 
		GeocodingResponse res = null;
		String urlStr = URL_GEOCODE_TEMPL;
		urlStr = StringUtils.replace(urlStr, PARAM_SRC_Q, 
			StringEscapeUtils.escapeHtml(src));
		urlStr = StringUtils.replace(urlStr, PARAM_APPID, 
			applicationService.getParameterValueAsString(Parameter.YahooApiKey));
		String locale = languageCode + "_" + StringUtils.upperCase(languageCode);
		urlStr = StringUtils.replace(urlStr, PARAM_LOCALE, locale);
		
		try {
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = new String(wr.getText().getBytes(), wr.getCharacterSet());
			log.debug("charset: " + wr.getCharacterSet());
			log.debug("response: " + resp);
			Gson gson = new Gson();
			res = gson.fromJson(resp, GeocodingResponse.class);
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
