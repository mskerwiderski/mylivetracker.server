package de.msk.mylivetracker.service.sms;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * ISmsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface ISmsService {

	public static final class SendingSmsResult {
		private boolean success;
		private String code;
		protected SendingSmsResult(boolean success, String code) {
			this.success = success;
			this.code = code;
		}
		public boolean isSuccess() {
			return success;
		}
		public String getCode() {
			return code;
		}
	}
	
	public SendingSmsResult sendTestSms(UserWithoutRoleVo user);
	
	public SendingSmsResult sendEmergencyActivatedSms(
		UserWithoutRoleVo user, DataReceivedVo dataReceived);	
	
	public SendingSmsResult sendEmergencyDeactivatedSms(
		UserWithoutRoleVo user, DataReceivedVo dataReceived);
}
