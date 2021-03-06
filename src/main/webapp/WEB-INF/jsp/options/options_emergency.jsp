<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<script type="text/javascript">
	function saveAllEmergency() {
		actionExecute('SaveAllEmergency');
	}

	function saveAndSendTestSms() {
		actionExecute('SaveAndSendTestSms');
	}

	function resetAllEmergency() {
		actionExecute('ResetAllEmergency');
	}
	
</script>

<c:set var="smsCount"><spring:message code="emergency.sms.count.zero" /></c:set>
<c:set var="smsLast"><spring:message code="emergency.sms.not.yet.sent" /></c:set>
<c:if test="${optionsCmd.userEmergency.smsSentCount gt 0}">
	<c:set var="smsCount">${optionsCmd.userEmergency.smsSentCount}</c:set>
</c:if>
<c:if test="${! empty optionsCmd.userEmergency.smsLastSent}">
	<c:set var="smsLast"><mlt:dateTime dateTime="${optionsCmd.userEmergency.smsLastSent}" /></c:set>
</c:if>

<table>
	<tr>
		<td colspan="3">
			&nbsp;<spring:message code="emergency.sms.headerline" />&nbsp;
		</td>
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.info" />&nbsp;
		</td> 
		<td style="width:30%;">
			&nbsp;<form:checkbox path="userEmergency.smsEnabled"
			cssClass="text ui-widget-content ui-corner-all" />&nbsp;
			<spring:message code="emergency.sms.enabled" />&nbsp;<br>
			&nbsp;<spring:message code="emergency.sms.count" 
				arguments="${smsCount}"/>&nbsp;<br>
			&nbsp;<spring:message code="emergency.sms.last" 
				arguments="${smsLast}"/>&nbsp;<br>			
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsEnabled" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.service.provider" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selDefSmsServiceProvider">${optionsCmd.userEmergency.smsServiceProvider}</c:set>
			&nbsp;<select id="userEmergency.smsServiceProvider" name="userEmergency.smsServiceProvider"
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="smsServiceProviders" items="${optionsCmd.smsServiceProviders}">
  						<c:choose>
    						<c:when test="${smsServiceProviders.value eq selDefSmsServiceProvider}">
						    	<option value="${smsServiceProviders.value}" selected>
						        	<spring:message code="${smsServiceProviders.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
						      	<option value="${smsServiceProviders.value}" >
						        	<spring:message code="${smsServiceProviders.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
			</select>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsServiceProvider" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.service.username" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userEmergency.smsServiceUsername"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsServiceUsername" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.service.password" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userEmergency.smsServicePassword"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsServicePassword" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.service.params" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userEmergency.smsServiceParams"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsServiceParams" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.sender" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userEmergency.smsSender"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsSender" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="emergency.sms.recipient" />&nbsp;
		</td>
		<td>
			<form:textarea cssClass="text ui-widget-content ui-corner-all"
				style="margin-left: 4px;width:98%" rows="3"
				path="userEmergency.smsRecipient"/>
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsRecipient" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="emergency.sms.message.template" />&nbsp;
		</td>
		<td>
			<form:textarea cssClass="text ui-widget-content ui-corner-all"
				style="margin-left: 4px;width:98%" rows="4"
				path="userEmergency.smsMessageTemplate"/>
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsMessageTemplate" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.sms.test" />&nbsp;
		</td>
		<td style="width:30%;white-space: nowrap;">
			<div class="mlt-button" >
				&nbsp;<a href="#" onclick="javascript:saveAndSendTestSms();" >
					<spring:message code='emergency.sms.test.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="emergency.save.reset" />&nbsp;
		</td>
		<td style="width:30%;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:saveAllEmergency();">
					<spring:message code='emergency.save.button' />
				</a>												
				<a href="#" onclick="javascript:resetAllEmergency();">
					<spring:message code='emergency.reset.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 5}">
				<td class="ui-state-highlight">
					&nbsp;<c:out escapeXml="false" value="${optionsCmd.infoMessage}" />&nbsp;						
				</td>		
			</c:when>
			<c:otherwise>
				<td>&nbsp;</td>
			</c:otherwise>
		</c:choose>								
	</tr>
</table>	
