package de.msk.mylivetracker.web.util.request;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * ReqParamValue.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ReqParamValue<T> {

	private static final String EQUAL = "=";
	private static final URLCodec CODEC_UTF_8 = new URLCodec("UTF-8");
	
	private ReqParam<T> param;
	private T value;
	
	/**
	 * constructor.
	 * @param param
	 * @param value
	 */
	public ReqParamValue(ReqParam<T> param, T value) {
		this.param = param;
		this.value = value;
	}
	
	/**
	 * constructor.
	 * @param param
	 */
	public ReqParamValue(ReqParam<T> param) {
		this.param = param;
		this.value = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(this.param)
			.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		ReqParamValue<T> rpv = (ReqParamValue<T>)obj;
		return new EqualsBuilder()
        	.append(this.param, rpv.param)
	        .isEquals();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "";
		if (value != null) {
			String valueStr = value.toString();
			try {
				if (!this.param.isCompress()) {
					
				} else {
					byte[] valueBytes = zipStrToBytes(valueStr);
					valueStr = Base64.encode(valueBytes);
				}
				valueStr = CODEC_UTF_8.encode(valueStr);
			} catch (Exception e) {
				throw new RuntimeException(e);			
			}
			if (!StringUtils.isEmpty(valueStr)) {
				res = this.param.getName() + EQUAL + valueStr;
			}
		}		
		return res;
	}
	
	private static byte[] zipStrToBytes(String input) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedOutputStream bufos = new BufferedOutputStream(
			new GZIPOutputStream(bos));
		bufos.write(input.getBytes());
		bufos.close();
		byte[] retval = bos.toByteArray();
		bos.close();
		return retval;
	}	
	
	/**
	 * @return the param
	 */
	public ReqParam<T> getParam() {
		return param;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}		
}
