package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.md5.MD5;
import de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder;

/**
 * PasswordEncoderDeprecated.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PasswordEncoderDeprecated implements IPasswordEncoder {

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String plainPassword) {
		String encodedPassword = null;
		if (!StringUtils.isEmpty(plainPassword)) {
			MD5 md5 = new MD5();
		    try {
				md5.Update(plainPassword, null);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			encodedPassword = md5.asHex();
		}
		return encodedPassword;
	}
}
