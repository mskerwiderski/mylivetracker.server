package de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks;

import de.msk.mylivetracker.commons.util.password.PasswordEncoderForGpsGatePortal;
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

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String plainPassword) {
		return PasswordEncoderForGpsGatePortal.encode(plainPassword);
	}
}
