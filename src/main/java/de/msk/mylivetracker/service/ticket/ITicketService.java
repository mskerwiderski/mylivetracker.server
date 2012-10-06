package de.msk.mylivetracker.service.ticket;

import de.msk.mylivetracker.domain.TicketVo;
import de.msk.mylivetracker.domain.TicketVo.Type;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

/**
 * ITicketService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface ITicketService {
	public TicketVo useTicket(String ticketId);
	public boolean checkAndDiscardTicket(String ticketId);
	public String createTicket(Type ticketType, String userId, UserRole role);	
}
