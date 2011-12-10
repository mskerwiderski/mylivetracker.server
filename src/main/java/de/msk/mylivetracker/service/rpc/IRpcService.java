package de.msk.mylivetracker.service.rpc;

import de.msk.mylivetracker.commons.rpc.LinkSenderRequest;
import de.msk.mylivetracker.commons.rpc.LinkSenderResponse;

/**
 * ISenderLinkService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-14
 * 
 */
public interface IRpcService {
	public String about();
	public LinkSenderResponse linkSender(LinkSenderRequest request);		
}
