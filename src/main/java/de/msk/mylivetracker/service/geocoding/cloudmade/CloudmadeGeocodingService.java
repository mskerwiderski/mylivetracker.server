package de.msk.mylivetracker.service.geocoding.cloudmade;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.cloudmade.response.Centroid;
import de.msk.mylivetracker.service.geocoding.cloudmade.response.Feature;
import de.msk.mylivetracker.service.geocoding.cloudmade.response.GeocodingResponse;

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

	public static void main(String[] argv) {
		CloudmadeGeocodingService s = new CloudmadeGeocodingService();
		LatLonPos pos = new LatLonPos(48.10478,11.616025);
		GeocodingResponse rsp = s.getCloudmadeGeocoderResult(pos, "de");
		System.out.println(rsp.toString());
		for (int i=0; i < rsp.getFeatures().length; i++) {
			Feature f = rsp.getFeatures()[i];
			String street = f.getProperties().getAddr_street();
			System.out.println(street);
		}
	}
	
	private IApplicationService applicationService;
	
	private static final String CLOUDMADE_URL_TEMPLATE = 
		"http://geocoding.cloudmade.com/$apikey/geocoding/v2/find.js?query=$query";
		
	private static final String CLOUDMADE_URL_TEMPLATE_REVERSE = 
		"http://geocoding.cloudmade.com/$apikey/geocoding/v2/find.js?around=$pos&object_type=address&distance=5000&return_location=true&results=1";
	
	private static final String PARAM_APIKEY = "$apikey";
	private static final String PARAM_QUERY = "$query";
	private static final String PARAM_POS = "$pos";
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getAddressOfPositionAux(de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos, java.lang.String)
	 */
	@Override
	public String getAddressOfPositionAux(LatLonPos position, String languageCode) {
		GeocodingResponse response = this.getCloudmadeGeocoderResult(position, languageCode);
		String address = null;
		if (response.getFeatures() != null) {
			Feature feature = response.getFeatures()[0];
			
			String country = "";
			String county = "";
			String city = "";
			String street = "";
			String housenumber = "";
			
			if (feature.getProperties() != null) {
				country = feature.getProperties().getAddr_country();
				city = feature.getProperties().getAddr_city();
				street = feature.getProperties().getAddr_street();
				housenumber = feature.getProperties().getAddr_housenumber();
			}
			
			if (feature.getLocation() != null) {
				if (StringUtils.isEmpty(country)) {
					country = feature.getLocation().getCountry();
				}
				if (StringUtils.isEmpty(county)) {
					county = feature.getLocation().getCounty();
				}
				if (StringUtils.isEmpty(city)) {
					city = feature.getLocation().getCity();
				}
				if (StringUtils.isEmpty(street)) {
					street = feature.getLocation().getRoad();
					housenumber = "";
				}
			}
			
			address =
				(StringUtils.isEmpty(street) ? "" : street) +
				(StringUtils.isEmpty(housenumber) ? "" : " " + housenumber) +
				(StringUtils.isEmpty(city) ? "" : ", " + city) +
				(StringUtils.isEmpty(county) ? "" : ", " + county) +
				(StringUtils.isEmpty(country) ? "" : ", " + country);
		}
		return address;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.geocoding.AbstractGeocodingService#getPositionOfAddressAux(java.lang.String, java.lang.String)
	 */
	@Override
	public LatLonPos getPositionOfAddressAux(String address, String languageCode) {
		GeocodingResponse response = this.getCloudmadeGeocoderResult(address, languageCode);
		LatLonPos position = null;
		if (response.getFeatures() != null) {
			Feature feature = response.getFeatures()[0];
			if (feature != null) {
				Centroid centroid = feature.getCentroid();
				if (centroid != null) {
					double[] coord = centroid.getCoordinates();
					if ((coord != null) && (coord.length == 2)) {
						position = new LatLonPos(coord[0], coord[1]);
					}
				}
			}
		}
		return position;
	}

	private GeocodingResponse getCloudmadeGeocoderResult(String query, String languageCode) {
		if (query == null) {
			throw new IllegalArgumentException("query must not be null.");
		} 
		GeocodingResponse res = null;
		String urlStr = CLOUDMADE_URL_TEMPLATE;
		urlStr = StringUtils.replace(urlStr, PARAM_APIKEY, 
			applicationService.getParameterValueAsString(Parameter.CloudmadeApiKey));
		//urlStr = StringUtils.replace(urlStr, PARAM_APIKEY, "7e59ccb56f7d4a1687e67f1ec3e83626");
		urlStr = StringUtils.replace(urlStr, PARAM_QUERY, query);
		
		try {
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
			resp = StringUtils.replace(resp, "addr:", "addr_");
			Gson gson = new Gson();
			res = gson.fromJson(resp, GeocodingResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		} 
		
		return res;			
	}
	
	private GeocodingResponse getCloudmadeGeocoderResult(LatLonPos position, String languageCode) {
		if (position == null) {
			throw new IllegalArgumentException("position must not be null.");
		} 
		GeocodingResponse res = null;
		String urlStr = CLOUDMADE_URL_TEMPLATE_REVERSE;
		urlStr = StringUtils.replace(urlStr, PARAM_APIKEY, 
			applicationService.getParameterValueAsString(Parameter.CloudmadeApiKey));
		//urlStr = StringUtils.replace(urlStr, PARAM_APIKEY, "7e59ccb56f7d4a1687e67f1ec3e83626");
		urlStr = StringUtils.replace(urlStr, PARAM_POS, position.toString());
		
		try {
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
			resp = StringUtils.replace(resp, "addr:", "addr_");
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
