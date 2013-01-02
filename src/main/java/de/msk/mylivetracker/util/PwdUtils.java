package de.msk.mylivetracker.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;

/**
 * PwdUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PwdUtils {

	public static final String USERNAME = "msk";
	public static final String PASSWORD = "msk1968";
	public static final String REALM = "SKERWIDERSKI";
	
	private static final URLCodec CODEC_UTF_8 = new URLCodec("UTF-8");
	
	public static String forURL(String aURLFragment){
	     String result = null;
	     try {
	       result = URLEncoder.encode(aURLFragment, "UTF-8");
	     }
	     catch (UnsupportedEncodingException ex){
	       throw new RuntimeException("UTF-8 not supported", ex);
	     }
	     return result;
	   }

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(PwdUtils.getPlainPassword());
		System.out.println(UUID.randomUUID().toString());
		String hashedPassword = DigestUtils.md5Hex(
			USERNAME + ":" + REALM + ":" + PASSWORD);
		System.out.println("hashed password: " + hashedPassword);
		System.out.println(forURL("#123-"));
		System.out.println(CODEC_UTF_8.encode("#123-"));
	}
	
	public static final int PWD_LENGTH = 8;
	protected static java.util.Random r = new java.util.Random();

	/*
	 * Set of characters that is valid. Must be printable, memorable, and "won't
	 * break HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands
	 * (i.e., not ' <', '>', '$', '!', ...). I, L and O are good to leave out,
	 * as are numeric zero and one.
	 */
	protected static char[] goodChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
		'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
		'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '@', };
				  
	/**
	 * Generate a Password object with a random password.
	 */
	private static String getNext(int len) {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < len; i++) {
	    	sb.append(goodChar[r.nextInt(goodChar.length)]);
	    }
	    return sb.toString();
	}

	public static String getPlainPassword() {
		return PwdUtils.getNext(PWD_LENGTH);					
	}  

	public static String getPlainPassword(int len) {
		return PwdUtils.getNext(len);					
	}
}
