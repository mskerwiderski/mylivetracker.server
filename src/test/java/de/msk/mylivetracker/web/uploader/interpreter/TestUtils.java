package de.msk.mylivetracker.web.uploader.interpreter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * TestUtils.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-11-24
 * 
 */
public class TestUtils {
	
	public static Map<String, String> getParameterValues(String httpUrl) {
		Map<String, String> parameterValues = new HashMap<String, String>();
		if (StringUtils.isEmpty(httpUrl)) return parameterValues;
		String parameterValuesStr = StringUtils.substringAfter(httpUrl, "?");
		String[] parameterValueStrArr = StringUtils.split(parameterValuesStr, "&");
		for (String parameterValueStr : parameterValueStrArr) {
			String[] parameterValuePair = StringUtils.split(parameterValueStr, "=");
			if (parameterValuePair.length == 2) {
				parameterValues.put(parameterValuePair[0], parameterValuePair[1]);
			}
		}
		return parameterValues;
	}
}
