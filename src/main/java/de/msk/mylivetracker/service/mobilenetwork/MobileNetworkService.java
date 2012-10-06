package de.msk.mylivetracker.service.mobilenetwork;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * MobileNetworkService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class MobileNetworkService implements IMobileNetworkService {

	private static final String[][] NETWORK_ARRAY = new String[][] {
		{ "262-01", "T-Mobile (de)" },
		{ "262-06", "T-Mobile (de)" },
		{ "262-02", "Vodafone (de)" },
		{ "262-04", "Vodafone (de)" },
		{ "262-09", "Vodafone (de)" },
		{ "262-03", "E-Plus (de)" },
		{ "262-05", "E-Plus (de)" },
		{ "262-77", "E-Plus (de)" },
		{ "262-07", "O2 (de)" },
		{ "262-08", "O2 (de)" },
		{ "262-11", "O2 (de)" },
		{ "262-10", "DB Telematik (de)" },
		{ "262-12", "Dolphin Telecom (de)" },
		{ "262-13", "mobilcom (de)" },
		{ "262-14", "Group3G (de)" },
		{ "262-15", "Airdata (de)" },
		{ "262-16", "vistream (de)" },
		{ "262-17", "ring Mobilfunk (de)" },
		{ "262-76", "Siemens (de)" },
		{ "232-01", "Mobilcom (at)" },
		{ "232-02", "Mobilcom (at)" },
		{ "232-09", "Mobilcom (at)" },
		{ "232-11", "Mobilcom (at)" },
		{ "232-03", "T-Mobile (at)" },
		{ "232-04", "T-Mobile (at)" },
		{ "232-07", "T-Mobile (at)" },
		{ "232-05", "Orange (at)" },
		{ "232-06", "Orange (at)" },
		{ "232-12", "Orange (at)" },
		{ "232-10", "Hutchison (at)" },
		{ "232-14", "Hutchison (at)" },		
	};
	
	private static Map<String, String> networkNames = new HashMap<String, String>();
	static {
		for (String[] network : NETWORK_ARRAY) {
			networkNames.put(network[0], network[1]);
		}
	}
	
	private static final String UNKNOWN = "unknown";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IMobileNetworkService#getNetworkName(java.lang.String, java.lang.String)
	 */
	@Override
	public String getNetworkName(String mcc, String mnc) {
		String key = mcc + "-" + StringUtils.leftPad(mnc, 2, '0');
		String name = UNKNOWN;
		if (networkNames.containsKey(key)) {
			name = networkNames.get(key);
		}
		return name;
	}

}
