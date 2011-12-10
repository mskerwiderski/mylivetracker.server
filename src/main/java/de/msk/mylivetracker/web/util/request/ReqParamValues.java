package de.msk.mylivetracker.web.util.request;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * ReqParamValues.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ReqParamValues {
	
	protected static final String PARAM_APPENDER = "&";
	
	private Set<ReqParamValue<?>> paramValues =
		new HashSet<ReqParamValue<?>>();
		
	/**
	 * private constructor.
	 */
	private ReqParamValues() {
	}

	public static ReqParamValues create() {
		return new ReqParamValues();
	}
	
	public <T> ReqParamValues add(HttpServletRequest request, ReqParam<T> param) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null.");
		}
		this.add(param, param.getValueFromReq(request));
		return this;
	}
	
	public <T> ReqParamValues add(ReqParamValue<T> paramValue) {
		if (paramValue != null) {
			if (paramValues.contains(paramValue)) {
				paramValues.remove(paramValue);
			}
			if (!paramValues.add(paramValue)) {
				throw new RuntimeException("comsistency check: adding param value '" + paramValue.toString() + "' failed.");
			}
		}
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> ReqParamValues add(ReqParam<T> param, T value) {
		if (param != null) {
			this.add(new ReqParamValue(param, value));
		}
		return this;
	}
	
	public <T> ReqParamValues add(ReqParamValues reqParamValues) {
		for (ReqParamValue<?> paramValue : reqParamValues.paramValues) {
			this.add(paramValue);
		}
		return this;
	}

//	public Map<String, Object> toMap() {
//		Map<String, Object> res = new HashMap<String, Object>();
//		for (ReqParamValue<?> paramValue : paramValues) {			
//			String paramValueStr = paramValue.toString();
//			if (!StringUtils.isEmpty(paramValueStr)) {
//				res.put(paramValue.getParam().getName(), 
//					paramValue.getValue());						
//			}
//		}
//		return res;
//	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "";
		for (ReqParamValue<?> paramValue : paramValues) {			
			String paramValueStr = paramValue.toString();
			if (!StringUtils.isEmpty(paramValueStr)) {
				res += StringUtils.isEmpty(res) ? 
					"" : PARAM_APPENDER;
				res += paramValueStr;						
			}
		}
		return res;
	}		
}
