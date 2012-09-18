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
			<c:choose>
				<c:when test="${optionsCmd.userEmergency.smsUnlocked}">
					&nbsp;<spring:message code="emergency.sms.unlocked" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;<spring:message code="emergency.sms.not.unlocked" />&nbsp;
				</c:otherwise>			
			</c:choose>
		</td>
	</tr>
	<c:if test="${optionsCmd.userEmergency.smsUnlocked}">
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
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userEmergency.smsRecipient"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userEmergency.smsRecipient" />&nbsp;	
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
	</c:if>	
</table>	
