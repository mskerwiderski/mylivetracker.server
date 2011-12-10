package de.msk.mylivetracker.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import de.msk.mylivetracker.domain.TicketVo;
import de.msk.mylivetracker.domain.TicketVo.Type;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

/**
 * TicketService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TicketService implements ITicketService {

	private Cache ticketCache;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITicketService#createTicket(de.msk.mylivetracker.domain.TicketVo.Type, java.lang.String, de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole)
	 */
	@Override
	public String createTicket(Type ticketType, String userId, UserRole role) {
		TicketVo ticket = TicketVo.create(ticketType, userId, role);
		ticketCache.put(new Element(ticket.getTicketId(), ticket));
		return ticket.getTicketId();
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITicketService#checkAndDiscardTicket(java.lang.String)
	 */
	@Override
	public boolean checkAndDiscardTicket(String ticketId) {
		boolean res = this.ticketCache.isKeyInCache(ticketId);
		if (res) {
			this.ticketCache.remove(ticketId);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITicketService#useTicket(java.lang.String)
	 */
	@Override
	public TicketVo useTicket(String ticketId) {
		TicketVo ticket = null;
		TicketVo originalTicket = null;
		if (this.ticketCache.isKeyInCache(ticketId)) {
			originalTicket = (TicketVo)this.ticketCache.get(ticketId).getValue();
			ticket = originalTicket.copy();
			originalTicket.markAsUsed();
		}
		return ticket;
	}

	/**
	 * @return the ticketCache
	 */
	public Cache getTicketCache() {
		return ticketCache;
	}

	/**
	 * @param ticketCache the ticketCache to set
	 */
	public void setTicketCache(Cache ticketCache) {
		this.ticketCache = ticketCache;
	}	
}
