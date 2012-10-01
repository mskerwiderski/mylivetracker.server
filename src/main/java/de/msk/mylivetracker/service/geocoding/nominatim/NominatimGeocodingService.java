package de.msk.mylivetracker.service.geocoding.nominatim;

import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;

/**
 * NominatimGeocodingService.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-23
 * 
 */
public class NominatimGeocodingService extends AbstractGeocodingService {

	private static final Log log = LogFactory.getLog(NominatimGeocodingService.class);
	
	private static final URLCodec CODEC_UTF_8 = new URLCodec("UTF-8");
	
	public static void main(String[] argv) {
		NominatimGeocodingService service = new NominatimGeocodingService();
		LatLonPos position = new LatLonPos(48.10478,11.616025);
		String address = service.getAddressOfPosition(position, "de");
		System.out.println(address);
	}

// geocoder response: address --> position
//	[{
//		"place_id":"127507668",
//		"licence":"Data \u00a9 OpenStreetMap contributors, ODbL 1.0. http:\/\/www.openstreetmap.org\/copyright",
//		"osm_type":"way",
//		"osm_id":"145438164",
//		"boundingbox":[
//			"48.1049690246582","48.1051750183105",
//			"11.6161203384399","11.6167001724243"
//		],
//		"lat":"48.1050760994594",
//		"lon":"11.6164104586502",
//		"display_name":"33, Sandgrubenweg, Bezirksteil Perlach, Stadtbezirk 16 Ramersdorf - Perlach, 
//			M\u00fcnchen, Regierungsbezirk Oberbayern, Bayern, Deutschland",
//		"class":"place",
//		"type":"house"
//	}]

// reverse geocoder response: position --> address
//	{
//		"place_id":"127507668",
//		"licence":"Data \u00a9 OpenStreetMap contributors, ODbL 1.0. http:\/\/www.openstreetmap.org\/copyright",
//		"osm_type":"way",
//		"osm_id":"145438164",
//		"lat":"48.1050760994594",
//		"lon":"11.6164104586502",
//		"display_name":"33, Sandgrubenweg, Bezirksteil Perlach, Stadtbezirk 16 Ramersdorf - Perlach, M\u00fcnchen, Regierungsbezirk Oberbayern, Bayern, Deutschland",
//		"address":{
//			"house_number":"33",
//			"road":"Sandgrubenweg",
//			"suburb":"Bezirksteil Perlach",
//			"city_district":"Stadtbezirk 16 Ramersdorf - Perlach",
//			"city":"M\u00fcnchen",
//			"county":"M\u00fcnchen",
//			"state_district":"Regierungsbezirk Oberbayern",
//			"state":"Bayern",
//			"country":"Deutschland",
//			"country_code":"de"
//		}
//	}	
	private IApplicationService applicationService;
	
	private static final String NOMINATIM_URL_TEMPLATE = 
		"http://nominatim.openstreetmap.org/search?format=json&q=$query&accept-language=$lang&email=$email";
		
	private static final String NOMINATIM_URL_TEMPLATE_REVERSE = 
		"http://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lon&accept-language=$lang&email=$email";
	
	private static final String PARAM_EMAIL = "$email";
	private static final String PARAM_QUERY = "$query";
	private static final String PARAM_LAT = "$lat";
	private static final String PARAM_LON = "$lon";
	private static final String PARAM_LANGUAGE = "$lang";

	@Override
	public ServiceCallResult getAddressOfPositionAux(
		LatLonPos position, String languageCode) {
		if (position == null) {
			throw new IllegalArgumentException("position must not be null.");
		}
		if (StringUtils.isEmpty(languageCode)) {
			throw new IllegalArgumentException("languageCode must not be empty.");
		}
		ServiceCallResponse serviceCallResponse = this.callReverseGeocoder(position, languageCode);
		ReverseGeocoderResponse response = serviceCallResponse.getReverseGeocoderResponse();
		String address = null;
		if ((response != null) && (response.getAddress() != null)) {
			String country = response.getAddress().getCountry();
			String city = response.getAddress().getCity();
			String street = response.getAddress().getRoad();
			String housenumber = response.getAddress().getHouse_number();
		
			address =
				(StringUtils.isEmpty(street) ? "" : street) +
				(StringUtils.isEmpty(housenumber) ? "" : " " + housenumber) +
				(StringUtils.isEmpty(city) ? "" : ", " + city) +
				(StringUtils.isEmpty(country) ? "" : ", " + country);
		}
		return new ServiceCallResult(address, serviceCallResponse.getUrl());
	}

	@Override
	public ServiceCallResult getPositionOfAddressAux(
		Address address, String languageCode) {
		if (address == null) {
			throw new IllegalArgumentException("address must not be null.");
		}
		if (StringUtils.isEmpty(languageCode)) {
			throw new IllegalArgumentException("languageCode must not be empty.");
		}
		String query = "";
		try {
			if (!StringUtils.isEmpty(address.getHousenumber()) && 
				!StringUtils.isEmpty(address.getStreet())) {
				query += "+" + CODEC_UTF_8.encode(address.getHousenumber());
			}
			if (!StringUtils.isEmpty(address.getStreet())) {
				query += "+" + CODEC_UTF_8.encode(address.getStreet());
			}
			if (!StringUtils.isEmpty(address.getCity())) {
				if (!StringUtils.isEmpty(query)) {
					query += ",";
				}
				query += "+" + CODEC_UTF_8.encode(address.getCity());
			}
			if (!StringUtils.isEmpty(address.getCountry())) {
				if (!StringUtils.isEmpty(query)) {
					query += ",";
				}
				query += "+" + CODEC_UTF_8.encode(address.getCountry());
			}
		}
		catch (EncoderException e) {
			throw new IllegalArgumentException("address cannot be encoded to UTF-8: " + address.toString());
		}	
		ServiceCallResponse serviceCallResponse = this.callGeocoder(query, languageCode);
		GeocoderResponse response = serviceCallResponse.getGeocoderResponse();
		LatLonPos position = null;
		if ((response != null) && (response.getLat() != null) && (response.getLon() != null)) {
			position = new LatLonPos(response.getLat(), response.getLon());
		}
		return new ServiceCallResult(position, serviceCallResponse.getUrl());
	}

	private String getEncodedEmailAddress() {
		String emailAddress = null;
		try {
			emailAddress = this.applicationService.getParameterValueAsString(IApplicationService.Parameter.SupportEmail);
			emailAddress = CODEC_UTF_8.encode(emailAddress);
		} catch (EncoderException e1) {
			throw new RuntimeException("email cannot be encoded to UTF-8: " + emailAddress);
		}
		return emailAddress;
	}

	private static class ServiceCallResponse {
		private GeocoderResponse geocoderResponse = null;
		private ReverseGeocoderResponse reverseGeocoderResponse = null;
		private String url = null;
		public ServiceCallResponse(GeocoderResponse geocoderResponse, String url) {
			this.geocoderResponse = geocoderResponse;
			this.url = url;
		}
		public ServiceCallResponse(ReverseGeocoderResponse reverseGeocoderResponse, String url) {
			this.reverseGeocoderResponse = reverseGeocoderResponse;
			this.url = url;
		}
		public GeocoderResponse getGeocoderResponse() {
			return geocoderResponse;
		}
		public ReverseGeocoderResponse getReverseGeocoderResponse() {
			return reverseGeocoderResponse;
		}
		public String getUrl() {
			return url;
		}
	}
	
	private ServiceCallResponse callGeocoder(String query, String languageCode) {
		if (query == null) {
			throw new IllegalArgumentException("query must not be null.");
		} 
		if (StringUtils.isEmpty(languageCode)) {
			throw new IllegalArgumentException("languageCode must not be empty.");
		}
		log.debug("callGeocoder, query='" + query + "'");
		GeocoderResponse geocoderResponse = null;
		String urlStr = NOMINATIM_URL_TEMPLATE;
		urlStr = StringUtils.replace(urlStr, PARAM_EMAIL, getEncodedEmailAddress()); 
		urlStr = StringUtils.replace(urlStr, PARAM_LANGUAGE, languageCode);
		urlStr = StringUtils.replace(urlStr, PARAM_QUERY, query);
		try {
			log.debug("callGeocoder, url='" + urlStr + "'");
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
			log.debug("callGeocoder, response='" + resp + "'");
			Gson gson = new Gson();
			Type collectionType = 
				new TypeToken<Collection<GeocoderResponse>>(){}.getType();
			Collection<GeocoderResponse> collection = gson.fromJson(resp, collectionType);
			if ((collection != null) && !collection.isEmpty()) {
				geocoderResponse = collection.iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			geocoderResponse = null;
		} 
		return new ServiceCallResponse(geocoderResponse, urlStr);			
	}
	
	private ServiceCallResponse callReverseGeocoder(LatLonPos position, String languageCode) {
		if (position == null) {
			throw new IllegalArgumentException("position must not be null.");
		}
		if (StringUtils.isEmpty(languageCode)) {
			throw new IllegalArgumentException("languageCode must not be empty.");
		}
		log.debug("callReverseGeocoder, position='" + position + "'");
		ReverseGeocoderResponse reverseGeocoderResponse = null;
		String urlStr = NOMINATIM_URL_TEMPLATE_REVERSE;
		urlStr = StringUtils.replace(urlStr, PARAM_EMAIL, getEncodedEmailAddress());
		urlStr = StringUtils.replace(urlStr, PARAM_LANGUAGE, languageCode);
		urlStr = StringUtils.replace(urlStr, PARAM_LAT, String.valueOf(position.getLatitude()));
		urlStr = StringUtils.replace(urlStr, PARAM_LON, String.valueOf(position.getLongitude()));
		try {
			log.debug("callReverseGeocoder, url='" + urlStr + "'");
			WebConversation wc = new WebConversation();			
			WebResponse wr = wc.getResponse(urlStr);
			String resp = wr.getText();
			log.debug("callReverseGeocoder, response='" + resp + "'");
			Gson gson = new Gson();
			reverseGeocoderResponse = gson.fromJson(resp, ReverseGeocoderResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			reverseGeocoderResponse = null;
		} 
		return new ServiceCallResponse(reverseGeocoderResponse, urlStr);			
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
