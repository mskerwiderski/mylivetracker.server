package de.msk.mylivetracker.web.frontend.login;

import java.util.List;

import de.msk.mylivetracker.web.util.TwitterUtils.TwitterMessage;

/**
 * LoginCmd.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-05
 * 
 */
public class LoginCmd {

	private String loginResultMessage;
	private String autoLoginUrlForDemoGuest;
	private List<TwitterMessage> twitterMessages;

	public String getLoginResultMessage() {
		return loginResultMessage;
	}

	public void setLoginResultMessage(String loginResultMessage) {
		this.loginResultMessage = loginResultMessage;
	}

	public String getAutoLoginUrlForDemoGuest() {
		return autoLoginUrlForDemoGuest;
	}

	public void setAutoLoginUrlForDemoGuest(String autoLoginUrlForDemoGuest) {
		this.autoLoginUrlForDemoGuest = autoLoginUrlForDemoGuest;
	}

	public List<TwitterMessage> getTwitterMessages() {
		return twitterMessages;
	}

	public void setTwitterMessages(List<TwitterMessage> twitterMessages) {
		this.twitterMessages = twitterMessages;
	}

	@Override
	public String toString() {
		return "LoginCmd [loginResultMessage=" + loginResultMessage
			+ ", autoLoginUrlForDemoGuest=" + autoLoginUrlForDemoGuest
			+ ", twitterMessages=" + twitterMessages + "]";
	}
}
