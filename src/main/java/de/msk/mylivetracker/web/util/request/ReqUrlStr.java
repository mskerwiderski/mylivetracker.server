package de.msk.mylivetracker.web.util.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * ReqUrlStr.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ReqUrlStr {
	private static final String SLASH = "/";
	private static final String BACKSLASH = "\\";
	private static final String QUESTIONMARK = "?";
	
	private String baseUrl = "";
	List<String> pathes = new ArrayList<String>();
	private ReqParamValues reqParamValues = ReqParamValues.create();		
	
	/**
	 * @param baseUrl
	 */
	private ReqUrlStr(String baseUrl) {
		if (!StringUtils.isEmpty(baseUrl)) {
			baseUrl = StringUtils.chomp(baseUrl, SLASH);
			baseUrl = StringUtils.chomp(baseUrl, BACKSLASH);
			baseUrl = StringUtils.chomp(baseUrl, QUESTIONMARK);
			this.baseUrl = baseUrl;
		}		
	}
	
	public static ReqUrlStr create(String baseUrl, String... pathes) {
		if (StringUtils.isEmpty(baseUrl)) {
			throw new IllegalArgumentException("baseUrl must not be null.");
		}
		ReqUrlStr reqUrlStr = new ReqUrlStr(baseUrl);
		reqUrlStr.addUrlPathes(pathes);
		return reqUrlStr;
	}
	
	public ReqUrlStr addUrlPath(String path) {
		if (!StringUtils.isEmpty(path)) {
			path = StringUtils.remove(path, SLASH);
			path = StringUtils.remove(path, BACKSLASH);
			pathes.add(path);
		}
		return this;
	}

	public ReqUrlStr addUrlPathes(String... pathes) {
		for (String path : pathes) {
			addUrlPath(path);
		}
		return this;
	}
	
	public <T> ReqUrlStr addParamValue(ReqParam<T> param, T value) {
		this.addParamValue(new ReqParamValue<T>(param, value));
		return this;
	}
	
	public ReqUrlStr addParamValue(ReqParamValue<?> paramValue) {
		this.reqParamValues.add(paramValue);
		return this;
	}
	
	public ReqUrlStr addParamValues(ReqParamValues paramValues) {
		this.reqParamValues.add(paramValues);
		return this;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = baseUrl;
		for (String path : pathes) {
			if (!StringUtils.isEmpty(path)) {
				res += SLASH + path;
			}
		}		
		String paramValuesStr = this.reqParamValues.toString();
		if (!StringUtils.isEmpty(paramValuesStr)) {
			res += QUESTIONMARK + paramValuesStr;
		}		
		return res;
	}	
}
