package de.msk.mylivetracker.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

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

	public static final String USERNAME = "demo";
	public static final String PASSWORD = "demo1968#+";
	public static final String REALM = "SKERWIDERSKI";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		System.out.println(UUID.randomUUID().toString());
		String hashedPassword = DigestUtils.md5Hex(
			USERNAME + ":" + REALM + ":" + PASSWORD);
		System.out.println("hashed password: " + hashedPassword);
	}
	
	public static final int MIN_LENGTH = 8;
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
	private static String getNext() {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < MIN_LENGTH; i++) {
	    	sb.append(goodChar[r.nextInt(goodChar.length)]);
	    }
	    return sb.toString();
	}

	/**
	 * set new password.
	 * @param user
	 * @param realm
	 */
	public static String getPlainPassword() {
		return PwdUtils.getNext();					
	}  

}
