package de.msk.mylivetracker.service.rpc;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.rpc.RegisterSenderRequest;
import de.msk.mylivetracker.commons.rpc.RegisterSenderResponse;
import de.msk.mylivetracker.commons.rpc.RpcResponse.ResultCode;
import de.msk.mylivetracker.domain.sender.SenderRadiusUnit;
import de.msk.mylivetracker.domain.sender.SenderSymbol;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.statusparams.IStatusParamsService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.util.PwdUtils;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.TcpServerConfig;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * SenderLinkService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-14
 * 
 */
public class JsonRpcService implements IRpcService {

	private IApplicationService applicationService;
	private IUserService userService;
	private ISenderService senderService;
	private ITrackService trackService;
	private TcpServerConfig tcpServerConfig;
	private IStatusParamsService statusParamsService;
	
	@Override
	public String about() {
		return "MyLiveTracker RPC Service v1.1.0";
	}

	@Override
	public RegisterSenderResponse registerSender(
		RegisterSenderRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}
		ResultCode resultCode = ResultCode.Ok;		
		String senderId = request.getSenderId();
		String senderName = request.getSenderName();
		String portalUserId = request.getPortalUserId();
		String portalPassword = request.getPortalPassword();
		String timeZoneId = request.getTimeZoneId();
		if (StringUtils.isEmpty(senderId) || 
			StringUtils.isEmpty(senderName) ||	
			StringUtils.isEmpty(portalUserId) || 
			StringUtils.isEmpty(portalPassword) ||
			StringUtils.isEmpty(timeZoneId)) {
			resultCode = ResultCode.InvalidInputParams;
		}
	
		UserWithoutRoleVo user = null;
		if (resultCode.isSuccess()) {
			// authorize user.
			boolean userAuthorized = false;
			user = this.userService.getUserWithoutRole(portalUserId);
			if ((user != null) && user.isEnabled()) {
				userAuthorized = StringUtils.equals(user.getMasterData().getPassword(), portalPassword);			
			}
			if (!userAuthorized) {
				resultCode = ResultCode.InvalidPortalUsernamePassword;
			}
		}
		
		SenderVo sender = null;
		if (resultCode.isSuccess()) {
			// get/check/create sender
			sender = this.senderService.getSender(senderId);
			if (sender != null) {
				// check if senderId belongs to userId.
				if (!sender.getUserId().equals(portalUserId)) {
					resultCode = ResultCode.SenderIdNotAllowed;
				}			
			} else {
				// check if sender count limit is reached.
				int senderCount = this.senderService.getSenderCount(portalUserId);
				if (user.getSenderLimit() <= senderCount) {
					resultCode = ResultCode.SenderCountLimitReached;
				}
			}
		}
		
		if (resultCode.isSuccess()) {
			sender = new SenderVo();
			sender.setActive(true);
			sender.setSenderId(senderId);
			sender.setName(senderName);
			sender.setUserId(portalUserId);
			sender.setTimeZone(timeZoneId);
			sender.setAuthRequired(true);
			sender.setAuthUsername(portalUserId);
			sender.setAuthPlainPassword(PwdUtils.getPlainPassword());			
			sender.setRedirectTo(null);
			sender.setSwitches(null);
			sender.setWithinRadiusCheckEnabled(false);
			sender.setRadius(0);
			sender.setRadiusUnit(UsersLocaleResolver.hasLocaleGerman(user) ? 
				SenderRadiusUnit.kilometer : SenderRadiusUnit.miles);
			sender.setSymbol(SenderSymbol.Phone);
			this.senderService.storeSender(sender);
		}
		
		RegisterSenderResponse response = null;
		
		if (resultCode.isSuccess()) {
			String serverAddress = 
				this.applicationService.getServerAddress();
			Integer serverPort = 
				this.applicationService.getParameterValueAsInteger(Parameter.ServerPortMltApp);
			response = new RegisterSenderResponse(request.getLocale(), resultCode,
				serverAddress, serverPort, 
				senderId, senderName, sender.getAuthUsername(), sender.getAuthPlainPassword(), 
				user.getOptions().getDefTrackName());
		} else {
			response = new RegisterSenderResponse(request.getLocale(), resultCode);			
		}
		
		return response;
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the senderService
	 */
	public ISenderService getSenderService() {
		return senderService;
	}
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}
	public ITrackService getTrackService() {
		return trackService;
	}
	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}
	public TcpServerConfig getTcpServerConfig() {
		return tcpServerConfig;
	}
	public void setTcpServerConfig(TcpServerConfig tcpServerConfig) {
		this.tcpServerConfig = tcpServerConfig;
	}
	public IStatusParamsService getStatusParamsService() {
		return statusParamsService;
	}
	public void setStatusParamsService(IStatusParamsService statusParamsService) {
		this.statusParamsService = statusParamsService;
	}	
}
