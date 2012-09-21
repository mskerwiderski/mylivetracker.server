package de.msk.mylivetracker.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.domain.sender.SenderVo;

/**
 * SenderDao.
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
public class SenderDao extends SqlMapClientDaoSupport implements ISenderDao {
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ISenderDao#getSender(java.lang.String)
	 */
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public SenderVo getSender(String senderId) {
		SenderVo senderVo = (SenderVo)
			this.getSqlMapClientTemplate().queryForObject(
			"SenderVo.getSenderBySenderId", senderId);
		return senderVo;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ISenderDao#storeSender(de.msk.mylivetracker.domain.SenderVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void storeSender(SenderVo sender) {
		this.getSqlMapClientTemplate().
			insert("SenderVo.storeSender", sender);		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateSenderType(String senderId, String senderType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("senderId", senderId);
		params.put("senderType", senderType);
		this.getSqlMapClientTemplate().
			update("SenderVo.updateSenderType", params);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ISenderDao#removeSender(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeSender(String senderId) {
		this.getSqlMapClientTemplate().
			delete("SenderVo.removeSender", senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ISenderDao#getSenders(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public List<SenderVo> getSenders(String userId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		List<SenderVo> senders = (List<SenderVo>)
			this.getSqlMapClientTemplate().queryForList(
				"SenderVo.getSendersByUserId", params);
		return senders;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ISenderDao#getSenderCount(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public Integer getSenderCount(String userId) {
		return (Integer)this.getSqlMapClientTemplate().
			queryForObject("SenderVo.getSenderCount", userId);
	}

}
