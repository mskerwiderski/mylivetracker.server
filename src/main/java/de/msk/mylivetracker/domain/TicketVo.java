package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

/**
 * TicketVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TicketVo implements Cloneable, Serializable {

	private static final long serialVersionUID = -1086284514268972886L;
	
	private static final String TICKET_ID_PREFIX = "T_";
	
	public enum Type {
		UrlTicket,
		JsonRequestId
	}
	
	private Type ticketType;
	private String ticketId;
	private String userId;
	private UserRole authorizedRole;
	private DateTime created;
	private DateTime lastUsed;
	private boolean neverUsed;
		
	/**
	 * private constructor. 
	 */
	private TicketVo() {
	}

	public static TicketVo create(Type ticketType, String userId, UserRole authorizedRole) {
		TicketVo ticket = new TicketVo();
		ticket.ticketType = ticketType;
		ticket.ticketId = TICKET_ID_PREFIX + UUID.randomUUID().toString();
		ticket.userId = userId;
		ticket.authorizedRole = authorizedRole;
		ticket.created = new DateTime();
		ticket.lastUsed = null;
		ticket.neverUsed = true;		
		return ticket;
	}
	
	public TicketVo copy() {
		TicketVo ticket = null;
		try {
			ticket = (TicketVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return ticket;
	}
	
	public void markAsUsed() {
		this.lastUsed = new DateTime();
		this.neverUsed = false;
	}	

	/**
	 * @return the ticketType
	 */
	public Type getTicketType() {
		return ticketType;
	}

	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}
	/**
	 * @return the authorizedRole
	 */
	public UserRole getAuthorizedRole() {
		return authorizedRole;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the created
	 */
	public DateTime getCreated() {
		return created;
	}

	/**
	 * @return the lastUsed
	 */
	public DateTime getLastUsed() {
		return lastUsed;
	}

	/**
	 * @return the neverUsed
	 */
	public boolean isNeverUsed() {
		return neverUsed;
	}	
}
