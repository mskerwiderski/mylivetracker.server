package de.msk.mylivetracker.dao;

import java.util.List;

import de.msk.mylivetracker.domain.sender.SenderVo;

/**
 * ISenderDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 001 18.11.2011 updateSenderType added.
 * 000 initial 2011-08-11
 * 
 */
public interface ISenderDao {
	
	/**
	 * get sender.
	 * @param senderId - the sender id.
	 * @return Returns sender value object if found, otherwise <code>null</code>.
	 */
	public SenderVo getSender(String senderId);
	
	/**
	 * store (save or update sender).
	 * @param sender - the sender.
	 */
	public void storeSender(SenderVo sender);
	
	/**
	 * update sender type.
	 * @param senderId - the sender id.
	 * @param senderType - the sender type.
	 */
	public void updateSenderType(String senderId, String senderType);
	
	/**
	 * delete sender.
	 * @param senderId - the sender id.
	 */
	public void deleteSender(String senderId);
	
	/**
	 * delete all senders of user.
	 * @param userId - the user id.
	 */
	public void deleteSendersOfUser(String userId);
	
	/**
	 * get senders of specified user id.
	 * @param userId - user id.
	 * @return Returns the list of senders of user id.
	 */
	public List<SenderVo> getSenders(String userId);

	/**
	 * get sender count of specified user id.
	 * @param userId
	 * @return
	 */
	public Integer getSenderCount(String userId);
}
