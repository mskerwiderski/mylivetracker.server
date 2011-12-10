<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page import="de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl" %>

<script type="text/javascript">

	function saveAllStatusPage() {
		actionExecute('SaveAllStatusPage');
	}

	function resetAllStatusPage() {
		actionExecute('ResetAllStatusPage');
	}

	function setStatusPageStyleToDefault() {
		actionExecute('SetStatusPageStyleToDefault');
	}
			
</script>

<table>
	<tr>
		<td colspan="3" style="font-weight: bold">
			&nbsp;<spring:message code="statuspage.description" />&nbsp;
		</td>
	</tr>
	<c:if test="${!optionsCmd.userOptions.recTrAccEnabled}">
		<tr>
			<td colspan="3" style="font-weight: bold; color: red">
				&nbsp;<spring:message code="statuspage.access.disabled" />&nbsp;
			</td>	
		</tr>
	</c:if>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.sender.id" />&nbsp;			
		</td>
		<td style="width:30%;" >
			<c:set var="selStatusPageSenderId">${optionsCmd.userStatusPage.senderId}</c:set>
			&nbsp;<select id="userStatusPage.senderId" name="userStatusPage.senderId"
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="senderEntry" items="${optionsCmd.senderEntries}">
  						<c:choose>
    						<c:when test="${senderEntry.sender.senderId eq selStatusPageSenderId}">
						    	<option value="${senderEntry.sender.senderId}" selected>
						        	<c:out value="${senderEntry.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
						      	<option value="${senderEntry.sender.senderId}" >
						        	<c:out value="${senderEntry.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
			</select>&nbsp;												
		</td>					
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.tracking.live" />&nbsp;
		</td>
		<td style="width:30%;" >
			<c:set var="selStatusPageTrackingLive">
				${optionsCmd.userStatusPage.trackingLive}
			</c:set>
			&nbsp;<select id="userStatusPage.trackingLive" 
				name="userStatusPage.trackingLive" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="commonsOptOnOff" 
					items="${optionsCmd.commonsOptsOnOff}">
 					<c:choose>
   						<c:when test="${commonsOptOnOff.value == selStatusPageTrackingLive}">
					    	<option value="${commonsOptOnOff.value}" selected>
					        	<spring:message code="${commonsOptOnOff.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${commonsOptOnOff.value}" >
					        	<spring:message code="${commonsOptOnOff.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;											
		</td>					
		<td>&nbsp;
		</td>
	</tr>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.tracking.keep.recent.positions" />&nbsp;
		</td>
		<td style="width:30%;" >
			<c:set var="selStatusPageTrackingKeepRecentPositions">
				${optionsCmd.userStatusPage.trackingKeepRecentPositions}
			</c:set>
			&nbsp;<select id="userStatusPage.trackingKeepRecentPositions" 
				name="userStatusPage.trackingKeepRecentPositions" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="stPgTrOptsKeepRecentOpts" 
					items="${optionsCmd.stPgTrOptsKeepRecentPos}">
 					<c:choose>
   						<c:when test="${stPgTrOptsKeepRecentOpts.value == selStatusPageTrackingKeepRecentPositions}">
					    	<option value="${stPgTrOptsKeepRecentOpts.value}" selected>
					        	<spring:message code="${stPgTrOptsKeepRecentOpts.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${stPgTrOptsKeepRecentOpts.value}" >
					        	<spring:message code="${stPgTrOptsKeepRecentOpts.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;											
		</td>					
		<td>&nbsp;
		</td>
	</tr>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.tracking.update.interval" />&nbsp;
		</td>
		<td style="width:30%;" >
			<c:set var="selStatusPageTrackingUpdateIntervalInSecs">
				${optionsCmd.userStatusPage.trackingUpdateIntervalInSecs}
			</c:set>
			&nbsp;<select id="userStatusPage.trackingUpdateIntervalInSecs" 
				name="userStatusPage.trackingUpdateIntervalInSecs" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="stPgTrOptsUpdateInterval" 
					items="${optionsCmd.stPgTrOptsUpdateInterval}">
 					<c:choose>
   						<c:when test="${stPgTrOptsUpdateInterval.value == selStatusPageTrackingUpdateIntervalInSecs}">
					    	<option value="${stPgTrOptsUpdateInterval.value}" selected>
					        	<spring:message code="${stPgTrOptsUpdateInterval.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${stPgTrOptsUpdateInterval.value}" >
					        	<spring:message code="${stPgTrOptsUpdateInterval.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;											
		</td>					
		<td>&nbsp;
		</td>
	</tr>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.tracking.fly.to.mode" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selStatusPageTrackingFlyToMode">
				${optionsCmd.userStatusPage.trackingFlyToMode}
			</c:set>
			&nbsp;<select id="userStatusPage.trackingFlyToMode" 
				name="userStatusPage.trackingFlyToMode" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="stPgTrOptsFlyToMode" 
					items="${optionsCmd.stPgTrOptsFlyToMode}">
 					<c:choose>
   						<c:when test="${stPgTrOptsFlyToMode.value == selStatusPageTrackingFlyToMode}">
					    	<option value="${stPgTrOptsFlyToMode.value}" selected>
					        	<spring:message code="${stPgTrOptsFlyToMode.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${stPgTrOptsFlyToMode.value}" >
					        	<spring:message code="${stPgTrOptsFlyToMode.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;											
		</td>					
		<td>&nbsp;
		</td>
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="statuspage.window.size" />&nbsp;			
		</td>
		<td>
			<table><tr>
			<td colspan="2" style="border:none;">	
				<c:set var="selStatusPageFullScreen">
					${optionsCmd.userStatusPage.fullScreen}
				</c:set>
				&nbsp;<spring:message code="statuspage.window.size.fullscreen" />&nbsp;
					<select id="userStatusPage.fullScreen" 
					name="userStatusPage.fullScreen" 
					class="text ui-widget-content ui-corner-all"> 
					<c:forEach var="commonsOptYesNo" 
						items="${optionsCmd.commonsOptsYesNo}">
	 					<c:choose>
	   						<c:when test="${commonsOptYesNo.value == selStatusPageFullScreen}">
						    	<option value="${commonsOptYesNo.value}" selected>
						        	<spring:message code="${commonsOptYesNo.label}" />
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${commonsOptYesNo.value}" >
						        	<spring:message code="${commonsOptYesNo.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
				</select>&nbsp;	
			</td>
			<td style="border:none;width:100%">&nbsp;</td>
			</tr>
			<tr>
			<td style="border:none;vertical-align: left;white-space: nowrap;">
				&nbsp;<spring:message code="statuspage.window.size.width" />&nbsp;
				<form:input 
					cssClass="text ui-widget-content ui-corner-all"
					path="userStatusPage.windowWidth"
					size="5"
				/>&nbsp;<spring:message code="statuspage.window.size.pixel" />
			</td>
			<td style="border:none;vertical-align: left;white-space: nowrap;">
				*&nbsp;<spring:message code="statuspage.window.size.height" />&nbsp;
				<form:input cssClass="text ui-widget-content ui-corner-all"
					path="userStatusPage.windowHeight"	
					size="5"
				/>&nbsp;<spring:message code="statuspage.window.size.pixel" />
			</td>
			<td style="border:none;width:100%">&nbsp;</td>			
			</tr></table>										
		</td>	
		
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userStatusPage.windowWidth" />
			<form:errors cssClass="ui-state-error"  
				path="userStatusPage.windowHeight" />&nbsp;	
		</td>			
	</tr>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="statuspage.show.track.info" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selStatusPageShowTrackInfo">
				${optionsCmd.userStatusPage.showTrackInfo}
			</c:set>
			&nbsp;<select id="userStatusPage.showTrackInfo" 
				name="userStatusPage.showTrackInfo" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="commonsOptYesNo" 
					items="${optionsCmd.commonsOptsYesNo}">
 					<c:choose>
   						<c:when test="${commonsOptYesNo.value == selStatusPageShowTrackInfo}">
					    	<option value="${commonsOptYesNo.value}" selected>
					        	<spring:message code="${commonsOptYesNo.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${commonsOptYesNo.value}" >
					        	<spring:message code="${commonsOptYesNo.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;											
		</td>					
		<td>&nbsp;
		</td>
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="statuspage.css.style" />&nbsp;
		</td>
		<td>
			<form:textarea cssClass="text ui-widget-content ui-corner-all"
				path="userStatusPage.cssStyle"	
				rows="4" cssStyle="margin-left: 4px;width:98%"
			/>										
		</td>	
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userStatusPage.cssStyle" />&nbsp;	
		</td>			
	</tr>			
	<tr>
		<td>
			&nbsp;<spring:message code="statuspage.save.reset" />&nbsp;
		</td>
		<td style="white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:saveAllStatusPage();">
					<spring:message code='statuspage.save.button' />
				</a>												
				<a href="#" onclick="javascript:resetAllStatusPage();">
					<spring:message code='statuspage.reset.button' />
				</a>
				<a href="#" onclick="javascript:setStatusPageStyleToDefault();">
					<spring:message code='statuspage.setdefstyle.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 3}">
				<td class="ui-state-highlight">
					&nbsp;<c:out escapeXml="false" value="${optionsCmd.infoMessage}" />&nbsp;						
				</td>		
			</c:when>
			<c:otherwise>
				<td>&nbsp;</td>
			</c:otherwise>
		</c:choose>								
	</tr>		
	<tr>
		<td>
			&nbsp;<spring:message code='statuspage.iframe.code.status.info' />&nbsp;		
		</td>
		<td>
			&nbsp;<spring:message code='statuspage.iframe.code.google.maps' />&nbsp;
		</td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td>
			<textarea rows="4" 
				style="font-family:Courier;margin-left: 4px;width:98%"
			><c:out value="${optionsCmd.iframeTrackAsStatusInfo}" /></textarea>			
		</td>
		<td>
			<textarea rows="4" 
				style="font-family:Courier;margin-left: 4px;width:98%"
			><c:out value="${optionsCmd.iframeTrackAsGoogleMaps}" /></textarea>
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code='statuspage.url.status.info' />&nbsp;		
		</td>
		<td>
			&nbsp;<spring:message code='statuspage.url.google.maps' />&nbsp;
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<spring:message code='statuspage.url.tipp' />&nbsp;		
		</td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td>
			<textarea rows="2" 
				style="font-family:Courier;margin-left: 4px;width:98%"
			><c:out value="${optionsCmd.userStatusPage.linkTrackAsStatusInfo}" /></textarea>			
		</td>
		<td>
			<textarea rows="2" 
				style="font-family:Courier;margin-left: 4px;width:98%"
			><c:out value="${optionsCmd.userStatusPage.linkTrackAsGoogleMaps}" /></textarea>
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<div style="height:33px;margin-left:4px;margin-top:4px;" class="mlt-button">
				<a href="<c:out value="${optionsCmd.userStatusPage.linkTrackAsStatusInfo}" />"
					target="_blank"><spring:message code='statuspage.preview.status.info' /></a>
			</div>				
		</td>
		<td> 
			<div style="height:33px;margin-left:4px;margin-top:4px;" class="mlt-button">
				<a href="<c:out value="${optionsCmd.userStatusPage.linkTrackAsGoogleMaps}" />"
					target="_blank"><spring:message code='statuspage.preview.google.maps' /></a>
			</div>		
		</td>
		<td>
			&nbsp;
		</td>
	</tr>		
</table>	
