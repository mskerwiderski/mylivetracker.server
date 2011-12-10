<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<script type="text/javascript">

	function saveAllMasterData() {
		actionExecute('SaveAllMasterData');
	}

	function resetAllMasterData() {
		document.forms["optionsForm"].elements["userMasterData.password"].value = "";
		document.forms["optionsForm"].elements["retypedPassword"].value = "";
		actionExecute('ResetAllMasterData');
	}
	
</script>

<table>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="masterdata.userid" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<input class="text ui-widget-content ui-corner-all" 				
				value="<c:out value='${currentUser.userId}' />"	
				size="30" readonly="readonly"
			/>&nbsp;												
		</td>					
		<td>&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="masterdata.last.name" />&nbsp;
		</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				path="userMasterData.lastName"	
				size="30"
			/>&nbsp;										
		</td>	
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userMasterData.lastName" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="masterdata.first.name" />&nbsp;
		</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userMasterData.firstName"	
				size="30"
			/>&nbsp;					
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userMasterData.firstName" />&nbsp;
		</td>						
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="masterdata.email.address" />&nbsp;
		</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all" 
				path="userMasterData.emailAddress"	
				size="30"
			/>&nbsp;											
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userMasterData.emailAddress" />&nbsp;	
		</td>			
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="masterdata.new.password" />&nbsp;
		</td>
		<td>
			&nbsp;<form:password cssClass="text ui-widget-content ui-corner-all" 
				path="userMasterData.password"	
				size="30"
			/>&nbsp;												
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userMasterData.password" />&nbsp;	
		</td>				
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="masterdata.retype.new.password" />&nbsp;
		</td>
		<td>
			&nbsp;<form:password cssClass="text ui-widget-content ui-corner-all" 
				path="retypedPassword"	
				size="30" 
			/>&nbsp;												
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="retypedPassword" />&nbsp;	
		</td>					
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="masterdata.save.reset" />&nbsp;
		</td>
		<td style="width:30%;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:saveAllMasterData();">
					<spring:message code='masterdata.save.button' />
				</a>												
				<a href="#" onclick="javascript:resetAllMasterData();">
					<spring:message code='masterdata.reset.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 0}">
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
