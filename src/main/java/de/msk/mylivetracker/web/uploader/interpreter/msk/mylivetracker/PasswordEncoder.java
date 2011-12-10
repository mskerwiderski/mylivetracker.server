package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.protocol.ProtocolUtils;
import de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder;

/**
 * PasswordEncoder.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PasswordEncoder implements IPasswordEncoder {

	private String deviceId;
	private String username;
	
	public PasswordEncoder(String deviceId, String username) {
		this.deviceId = deviceId;
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String plainPassword) {
		String encodedPassword = null;
		if (!StringUtils.isEmpty(plainPassword)) {			
			encodedPassword = ProtocolUtils.calcSeed(
				deviceId, username, plainPassword);
		}
		return encodedPassword;
	}
}
