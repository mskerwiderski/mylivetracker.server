package de.msk.mylivetracker.service.geocoding.cloudmade;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.yahoo.response.GeocodingResponse;

/**
 * CloudmadeGeocodingService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class CloudmadeGeocodingService extends AbstractGeocodingService {

	private IApplicationService applicationService;
	
	private static final String CLOUDMADE_URL_TEMPLATE = 
		"http://geocoding.cloudmade.com/$apikey/geocoding/v2/find.js?around=$pos&object_type=address&distance=50";
	
	private static final String PARAM_APIKEY = "$apikey";
	private static final String PARAM_POS = "$pos";
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getAddressOfPositionAux(de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos, java.lang.String)
	 */
	@Override
	public String getAddressOfPositionAux(LatLonPos position,
			String languageCode) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getPositionOfAddressAux(java.lang.String, java.lang.String)
	 */
	@Override
	public LatLonPos getPositionOfAddressAux(String address, String languageCode) {
		return null;
	}

	@SuppressWarnings("unused")
	private GeocodingResponse getCloudmadeGeocoderResult(LatLonPos position, String languageCode) {
		if (position == null) {
			throw new IllegalArgumentException("position must not be null.");
		} 
		GeocodingResponse res = null;
		String urlStr = CLOUDMADE_URL_TEMPLATE;
		urlStr = StringUtils.replace(urlStr, PARAM_APIKEY, 
			applicationService.getParameterValueAsString(Parameter.CloudmadeApiKey));
		urlStr = StringUtils.replace(urlStr, PARAM_POS, position.toString());
		
		try {
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
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
