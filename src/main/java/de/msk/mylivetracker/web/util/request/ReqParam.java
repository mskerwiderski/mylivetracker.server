package de.msk.mylivetracker.web.util.request;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * ReqParam.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ReqParam<T> {	
	
	private String name;
	private Class<T> type;
	private boolean compress;
	
	private static final Set<Class<?>> SUPPORTED_TYPES = new HashSet<Class<?>>(); 
	
	static {
		SUPPORTED_TYPES.add(String.class);
		SUPPORTED_TYPES.add(Integer.class);
		SUPPORTED_TYPES.add(Long.class);
		SUPPORTED_TYPES.add(Double.class);
		SUPPORTED_TYPES.add(Boolean.class);			
	}	
	
	/**
	 * constructor.
	 * @param name - the param name (must not be empty).
	 */
	public ReqParam(String name, Class<T> type) {
		this.init(name, type, false);
	}

	/**
	 * constructor.
	 * @param name - the param name (must not be empty).
	 */
	public ReqParam(String name, Class<T> type, boolean compress) {
		this.init(name, type, compress);
	}
	
	private void init(String name, Class<T> type, boolean compress) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("name must not be empty.");
		}
		if (type == null) {
			throw new IllegalArgumentException("type must not be null.");
		}
		if (!SUPPORTED_TYPES.contains(type)) {
			throw new IllegalArgumentException(
				"type '" + type.getName() + "' is not supported.");
		}
		this.name = name;
		this.type = type;
		this.compress = compress;
	}
	
	public boolean valueExists(HttpServletRequest request) {
		return (this.getValueFromReq(request, null) != null);
	}

	public T getValueFromSess(HttpServletRequest request) {
		return getValueFromSess(request, null);
	}
	
	@SuppressWarnings("unchecked")
	public T getValueFromSess(HttpServletRequest request, T defValue) {
		return (T)request.getSession().getAttribute(this.name);
	}
	
	public T getValueFromReq(HttpServletRequest request) {
		return this.getValueFromReq(request, null);
	}
	
	@SuppressWarnings("unchecked")
	public T getValueFromReq(HttpServletRequest request, T defValue) {
		T value = null;
		if (request == null) {
			throw new IllegalArgumentException("request must not be null.");
		}
		String valueStr = request.getParameter(this.name);
		if (!StringUtils.isEmpty(valueStr) && this.compress) {
			try {
				valueStr = unzipStrFromBytes(Base64.decode(valueStr));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (StringUtils.isEmpty(valueStr)) {
			value = defValue;
		} else if (this.type.equals(String.class)) {
			value = (T)valueStr;
		} else if (this.type.equals(Integer.class)) {
			if (NumberUtils.isNumber(valueStr)) {
				value = (T)NumberUtils.createInteger(valueStr);				
			} else {
				value = defValue;
			}
		} else if (this.type.equals(Long.class)) {
			if (NumberUtils.isNumber(valueStr)) {
				value = (T)NumberUtils.createLong(valueStr);				
			} else {
				value = defValue;
			}
		} else if (this.type.equals(Double.class)) {
			if (NumberUtils.isNumber(valueStr)) {
				value = (T)NumberUtils.createDouble(valueStr);				
			} else {
				value = defValue;
			}
		} else if (this.type.equals(Boolean.class)) {			
			value = (T)BooleanUtils.toBooleanObject(valueStr);
			if (value == null) {
				value = defValue;
			}
		}
		return value;
	}	
		
	private static String unzipStrFromBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		BufferedInputStream bufis = new BufferedInputStream(
				new GZIPInputStream(bis));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = bufis.read(buf)) > 0) {
			bos.write(buf, 0, len);
		}
		String retval = bos.toString();
		bis.close();
		bufis.close();
		bos.close();
		return retval;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @return the compress
	 */
	public boolean isCompress() {
		return compress;
	}		
}
