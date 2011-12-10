package de.msk.mylivetracker.web.uploader.processor.server.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.web.uploader.processor.IDataCtx;

/**
 * HttpServletRequestCtx.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpServletRequestCtx implements IDataCtx {

	private HttpServletRequest request;
	private Map<String, String> parameterMap = new HashMap<String, String>();
	private String queryStr = "";
	
	/**
	 * @param request
	 */
	public HttpServletRequestCtx(HttpServletRequest request) {
		this.request = request;
		@SuppressWarnings("rawtypes")
		Enumeration names = 
			this.request.getParameterNames();
		while (names.hasMoreElements())	{
			String key = (String)names.nextElement();
			String value = 
				this.request.getParameter(key);
			parameterMap.put(key, value);
			queryStr += key + "=" + value;
			if (names.hasMoreElements()) {
				queryStr += "&";
			}
		}	
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IDataCtx#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return parameterMap.isEmpty();
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}
	
	/**
	 * @return the parameterMap
	 */
	public Map<String, String> getParameterMap() {
		return parameterMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return request.getRequestURL().toString() +
			"?" + queryStr;
	}
}
