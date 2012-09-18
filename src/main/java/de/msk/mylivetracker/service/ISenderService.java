package de.msk.mylivetracker.service;

import java.util.List;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;

/**
 * ISenderService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 002 08.09.2012 senderExists added.
 * 001 18.11.2011 updateSenderType added.
 * 000 initial 2011-08-11
 * 
 */
public interface ISenderService {

	/**
	 * check whether sender exists or not.
	 * @param senderId - the sender id.
	 * @return Returns <code>true</code> if sender exists, otherwise <code>false</code>.
	 */
	public boolean senderExists(String senderId);
	
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
	 * remove sender.
	 * @param senderId - the sender id.
	 */
	public void removeSender(String senderId);
	
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
	
	/**
	 * authorize and return complete sender value object.
	 * @param senderFromRequest - the sender from request.
	 * @return Returns <code>true</code> if sender was successfully authorized, 
	 * otherwise <code>false</code>.
	 */
	SenderVo getAuthorizedSender(SenderFromRequestVo senderFromRequest);
}
