package de.msk.mylivetracker.service;

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

	public static class SmsServiceException extends Exception {

		private static final long serialVersionUID = 6109116070619730864L;

		public SmsServiceException() {
		}

		/**
		 * @param message
		 * @param cause
		 */
		public SmsServiceException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public SmsServiceException(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		public SmsServiceException(Throwable cause) {
			super(cause);
		}
	}
	
	public void sendTestSms(UserWithoutRoleVo user) throws SmsServiceException;
	
	public void sendEmergencyActivatedSms(
		UserWithoutRoleVo user, DataReceivedVo dataReceived)
		throws SmsServiceException;	
	
	public void sendEmergencyDeactivatedSms(
		UserWithoutRoleVo user, DataReceivedVo dataReceived)
		throws SmsServiceException;
}
