package de.msk.mylivetracker.service.sender;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.dao.ISenderDao;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;

/**
 * SenderService.
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
public class SenderService implements ISenderService {

	private Cache senderCache;
	private ISenderDao senderDao;
			
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#getAuthorizedSender(de.msk.mylivetracker.web.positionreceiver.SenderFromRequestVo)
	 */
	public SenderVo getAuthorizedSender(SenderFromRequestVo senderFromRequest) {
		SenderVo senderInRepo = null; 
			
		if ((senderFromRequest != null) && (!StringUtils.isEmpty(senderFromRequest.getSenderId()))) {
			senderInRepo = this.getSender(senderFromRequest.getSenderId());
		}
		
		if ((senderInRepo != null) && !senderInRepo.isActive()) {
			senderInRepo = null;
		}
		
		if (senderInRepo != null) {					
			if (senderInRepo.isAuthRequired()) {
				IPasswordEncoder passwordEncoder = senderFromRequest.getPasswordEncoder();
				String senderInRepoPassword = senderInRepo.getAuthPlainPassword();
				if (passwordEncoder != null) {
					senderInRepoPassword = 
						passwordEncoder.encode(senderInRepoPassword);
				}
				if (StringUtils.isEmpty(senderInRepo.getAuthUsername()) || 
					StringUtils.isEmpty(senderInRepoPassword) ||
					StringUtils.isEmpty(senderFromRequest.getAuthUsername()) || 
					StringUtils.isEmpty(senderFromRequest.getAuthPlainPassword()) ||
					!senderInRepo.getAuthUsername().equals(senderFromRequest.getAuthUsername()) ||
					!senderInRepoPassword.equals(senderFromRequest.getAuthPlainPassword())) {
					senderInRepo = null;
				}
			}
		}
				
		return senderInRepo;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#storeSender(de.msk.mylivetracker.domain.SenderVo)
	 */
	@Override
	public void storeSender(SenderVo sender) {
		senderCache.remove(sender.getSenderId());
		senderDao.storeSender(sender);
	}

	@Override
	public void updateSenderType(String senderId, String senderType) {
		SenderVo sender = this.getSender(senderId);
		if (sender == null) return;
		if (!StringUtils.equals(sender.getSenderType(), senderType)) {
			senderCache.remove(sender.getSenderId());
			sender.setSenderType(senderType);			
			senderDao.updateSenderType(sender.getSenderId(), sender.getSenderType());
		}
	}

	@Override
	public void deleteSender(String senderId) {
		senderCache.remove(senderId);
		senderDao.deleteSender(senderId);
	}

	@Override
	public void deleteSendersOfUser(String userId) {
		@SuppressWarnings("unchecked")
		List<String> senderIds = (List<String>)this.senderCache.getKeys();
		for (String senderId : senderIds) {
			SenderVo sender = (SenderVo)this.senderCache.get(senderId).getObjectValue();
			if (StringUtils.equals(sender.getUserId(), userId)) {
				this.senderCache.remove(senderId);
			}
		}
		this.senderDao.deleteSendersOfUser(userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#senderExists(java.lang.String)
	 */
	@Override
	public boolean senderExists(String senderId) {
		boolean res = false;
		if (StringUtils.isEmpty(senderId)) return res;
		
		res = senderCache.isKeyInCache(senderId);
		
		if (!res) {
			SenderVo sender = senderDao.getSender(senderId);
			if (sender != null) {
				senderCache.put(new Element(senderId, sender));
				res = true;
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#getSender(java.lang.String)
	 */
	public SenderVo getSender(String senderId) {
		if (StringUtils.isEmpty(senderId)) return null;
		SenderVo sender = null;
		if (senderCache.isKeyInCache(senderId)) {
			sender = (SenderVo)senderCache.get(senderId).getObjectValue();
		} else {
			sender = senderDao.getSender(senderId);
			if (sender != null) {
				senderCache.put(new Element(senderId, sender));
			}
		}
		return sender;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#getSenders(java.lang.String)
	 */
	public List<SenderVo> getSenders(String userId) {
		return senderDao.getSenders(userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISenderService#getSenderCount(java.lang.String)
	 */
	@Override
	public Integer getSenderCount(String userId) {
		return senderDao.getSenderCount(userId);
	}

	/**
	 * @return the senderCache
	 */
	public Cache getSenderCache() {
		return senderCache;
	}

	/**
	 * @param senderCache the senderCache to set
	 */
	public void setSenderCache(Cache senderCache) {
		this.senderCache = senderCache;
	}

	/**
	 * @return the senderDao
	 */
	public ISenderDao getSenderDao() {
		return senderDao;
	}

	/**
	 * @param senderDao the senderDao to set
	 */
	public void setSenderDao(ISenderDao senderDao) {
		this.senderDao = senderDao;
	}	
}
