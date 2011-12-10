package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * DeviceActionResult.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DeviceActionResult implements Serializable {

	private static final long serialVersionUID = -3166189474926486427L;
	
	private static final String RESULT_OK = "[OK]";
	private static final String RESULT_FAILED = "[FAILED]";
	private static final String VALUE_NOT_SET = "<not set>";
	
	public enum Param {
		DeviceActionName,
		SenderId,
		TrackId,
		TrackName,
		TrackIsReleased,
		DistanceInMtr,
		CountPositions,
		CurrentAddress,
		PositionId,
		MessageId,
		Message,
		MobNwCellId,
		AddInfo,
		ErrMessage
	};
	
	private Map<Param, String> paramValues = new HashMap<Param, String>();

	private Exception exception;
	
	private DeviceActionResult() {	
	}
	
	public static DeviceActionResult create(String deviceActionName) {
		if (StringUtils.isEmpty(deviceActionName)) {
			throw new IllegalArgumentException("deviceActionName must not be null.");
		}
		DeviceActionResult res = new DeviceActionResult();
		res.append(Param.DeviceActionName, deviceActionName);
		return res;
	}
	
	public static DeviceActionResult create(String deviceActionName, Exception exception) {
		if (StringUtils.isEmpty(deviceActionName)) {
			throw new IllegalArgumentException("deviceActionName must not be null.");
		}
		if (exception == null) {
			throw new RuntimeException("exception must not be null.");
		}
		DeviceActionResult res = new DeviceActionResult();
		res.append(Param.DeviceActionName, deviceActionName);
		res.append(Param.ErrMessage, exception.toString());
		res.exception = exception;
		return res;
	}
	
	public DeviceActionResult append(Param param, String value) {
		if (param == null) {
			throw new IllegalArgumentException("param must not be null.");
		}
		if (StringUtils.isEmpty(value)) {
			value = VALUE_NOT_SET;
		}
		paramValues.put(param, value);
		return this;
	}	
	
	public DeviceActionResult append(Param param, Integer value) {
		if (param == null) {
			throw new IllegalArgumentException("param must not be null.");
		}
		if (value == null) {
			paramValues.put(param, VALUE_NOT_SET);
		} else {
			paramValues.put(param, value.toString());
		}
		return this;
	}	
	
	public DeviceActionResult append(Param param, Boolean value) {
		if (param == null) {
			throw new IllegalArgumentException("param must not be null.");
		}		
		if (value == null) {
			paramValues.put(param, VALUE_NOT_SET);
		} else {
			paramValues.put(param, value.toString());
		}
		return this;
	}
	
	public DeviceActionResult append(Param param, Double value, String unit) {
		if (param == null) {
			throw new IllegalArgumentException("param must not be null.");
		}
		if (value == null) {
			paramValues.put(param, VALUE_NOT_SET);
		} else {
			DecimalFormat df = new DecimalFormat("#.##");
			String valueStr = df.format(value);
			paramValues.put(param, valueStr + unit);
		}
		return this;
	}
	
	public static DeviceActionResult createDeviceActionResult(String deviceActionResultStr) {
		DeviceActionResult res = new DeviceActionResult();
		deviceActionResultStr = StringUtils.remove(deviceActionResultStr, RESULT_OK);
		deviceActionResultStr = StringUtils.remove(deviceActionResultStr, RESULT_FAILED);
		String[] paramValuePairs = StringUtils.split(deviceActionResultStr, ';');
		for (String paramValuePairStr : paramValuePairs) {
			String[] paramValuePair = StringUtils.split(paramValuePairStr, ':');
			if (paramValuePair.length == 2) {
				Param param = Param.valueOf(paramValuePair[0]);
				res.getParamValues().put(param, paramValuePair[1]);
			}
		}
		return res;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public String getResultValue(Param param) {
		return paramValues.get(param);
	}

	public boolean hasException() {
		return (exception != null);
	}
		
	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		String errMessage = paramValues.get(Param.ErrMessage);
		buf.append(StringUtils.isEmpty(errMessage) ? RESULT_OK : RESULT_FAILED);
		for (Param param : Param.values()) {
			String value = paramValues.get(param);
			if (!StringUtils.isEmpty(value)) {
				buf.append(param.name()).append(":").append(value).append(";");
			}
		}
		return buf.toString();
	}
	
	/**
	 * @return the paramValues
	 */
	private Map<Param, String> getParamValues() {
		return paramValues;
	}
}
