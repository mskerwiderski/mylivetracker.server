package de.msk.mylivetracker.web.uploader.interpreter.afischer.aftrack.android;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.md5.MD5;
import de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder;

/**
 * PasswordEncoder.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-11-02 initial.
 * 
 */
public class PasswordEncoder implements IPasswordEncoder {

	public PasswordEncoder() {
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String plainPassword) {
		String encodedPassword = null;
		if (!StringUtils.isEmpty(plainPassword)) {			
			try {
				MD5 md5 = new MD5();
				md5.Update(plainPassword, null);
				encodedPassword = md5.asHex();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return encodedPassword;
	}
}
