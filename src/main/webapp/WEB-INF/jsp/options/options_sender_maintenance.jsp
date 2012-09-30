<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<div id="dlgRemoveSender" title="<spring:message code="sendermaintenance.remove.confirm.title" />">
</div>
	
<script>
var $dlgRemoveSender;

$(document).ready(function(e) {		
	$dlgRemoveSender = $('#dlgRemoveSender').dialog({
		modal: true,				
		autoOpen: false,
		closeOnEscape: true,		
		buttons: {
			'<spring:message code="sendermaintenance.remove.confirm.no" />': function() {
				$(this).dialog('close');
			},
			'<spring:message code="sendermaintenance.remove.confirm.yes" />': function() {
				actionExecute("RemoveSender");
				$(this).dialog('close');
			}														
		},
		open: function(event, ui) {			
			$(this).html("<spring:message code="sendermaintenance.remove.confirm.text" arguments="${optionsCmd.selectedSenderId}" />");			 
		}					
	});
	setSenderSymbolImage(null);
});

function setSenderFields() { 
	actionExecute('SetSenderFields');
}

function updateSender() {			
	actionExecute('UpdateSender');
}

function addSender() {
	actionExecute('AddSender');
}

function removeSender() {
	$dlgRemoveSender.dialog('open');
}

</script>

<table>	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="sendermaintenance.documentation" />&nbsp;
		</td>
		<td style="width:30%;">
			&nbsp;<a href="doc/MyLiveTracker.pdf" target="_blank"><b><i>MyLiveTracker Dokumentation (im PDF Format)</i></b></a>&nbsp;												
		</td>					
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="sendermaintenance.senders.select" 
				arguments="${fn:length(optionsCmd.senderEntries)-1},${optionsCmd.senderLimit}" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selSenderId">${optionsCmd.selectedSenderId}</c:set>
			&nbsp;<select id="selectedSenderId" name="selectedSenderId"
				onchange="javascript:setSenderFields();" 
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="senderEntry" items="${optionsCmd.senderEntries}">
  						<c:choose>
    						<c:when test="${senderEntry.sender.senderId eq selSenderId}">
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
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="selectedSenderId" />&nbsp;
		</td>
	</tr>
	<tr>
		<c:set var="senderIdStyle" value="" />
		<c:if test="${!empty selSenderId}" >
			<c:set var="senderIdStyle" value="background:gray;color:white;" />
		</c:if>
		
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.senderid" />&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				cssStyle="${senderIdStyle}"
				path="senderDetails.senderId"	
				size="30"
				readonly="${!empty selSenderId}"
			/>&nbsp;
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="senderDetails.senderId" />&nbsp;
		</td>
	</tr>
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.sendertype" />&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				cssStyle="background:gray;color:white;"
				path="senderDetails.senderType"	
				size="30"
				readonly="true"
			/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.name" />&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				path="senderDetails.name"	
				size="30"
			/>&nbsp;
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="senderDetails.name" />&nbsp;
		</td>
	</tr>
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.timezone" />&nbsp;</td>
		<td>
			<c:set var="selSenderTimeZone">${optionsCmd.senderDetails.timeZone}</c:set>
			&nbsp;<select id="senderDetails.timeZone" name="senderDetails.timeZone" 
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="commonsOptsTimeZone" items="${optionsCmd.commonsOptsTimeZone}">
  						<c:choose>
    						<c:when test="${commonsOptsTimeZone.value == selSenderTimeZone}">
						    	<option value="${commonsOptsTimeZone.value}" selected>
						        	<spring:message code="${commonsOptsTimeZone.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
					      		<option value="${commonsOptsTimeZone.value}" >
						        	<spring:message code="${commonsOptsTimeZone.label}" />
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
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.switches" 
			arguments="${optionsCmd.supportedSenderSwitches}" argumentSeparator=";"/>&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				path="senderDetails.switches"	
				size="60"
			/>&nbsp;
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="senderDetails.switches" />&nbsp;
		</td>
	</tr>
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.active" />&nbsp;</td>
		<td>
			<c:set var="selSenderIsActive">${optionsCmd.senderDetails.active}</c:set>
			&nbsp;<select id="senderDetails.active" name="senderDetails.active" 
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="commonsOptYesNo" items="${optionsCmd.commonsOptsYesNo}">
  						<c:choose>
    						<c:when test="${commonsOptYesNo.value == selSenderIsActive}">
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
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.auth.required" />&nbsp;</td>
		<td>
			<c:set var="selSenderIsAuthRequired">${optionsCmd.senderDetails.authRequired}</c:set>
			&nbsp;<select id="senderDetails.authRequired" name="senderDetails.authRequired" 
				class="text ui-widget-content ui-corner-all" > 
				<c:forEach var="commonsOptYesNo" items="${optionsCmd.commonsOptsYesNo}">
 						<c:choose>
   						<c:when test="${commonsOptYesNo.value == selSenderIsAuthRequired}">
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
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.auth.username" />&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				path="senderDetails.authUsername"	
				size="30"
			/>&nbsp;
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="senderDetails.authUsername" />&nbsp;
		</td>
	</tr>
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.auth.plain.password" />&nbsp;</td>
		<td>
			&nbsp;<form:input cssClass="text ui-widget-content ui-corner-all"
				path="senderDetails.authPlainPassword"	
				size="30"
			/>&nbsp;
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="senderDetails.authPlainPassword" />&nbsp;
		</td>
	</tr>				
	<tr>
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.redirect.to" />&nbsp;</td>
		<td>
			<c:set var="selRedirectTo">${optionsCmd.senderDetails.redirectTo}</c:set>
			&nbsp;<select id="senderDetails.redirectTo" name="senderDetails.redirectTo"
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="redirectEntry" items="${optionsCmd.redirectEntries}">
  						<c:choose>
    						<c:when test="${redirectEntry.sender.senderId eq selRedirectTo}">
						    	<option value="${redirectEntry.sender.senderId}" selected>
						        	<c:out value="${redirectEntry.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
						      	<option value="${redirectEntry.sender.senderId}" >
						        	<c:out value="${redirectEntry.label}" />
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
		<td>&nbsp;<spring:message code="sendermaintenance.sender.details.symbol" />&nbsp;</td>
		<td>
			<table><tr>
			<td style="border:none;width:50%;">
			<script>
				function setSenderSymbolImage(symbol) {
					if (symbol == null) {
						symbol = document.forms['optionsForm'].elements['senderDetails.symbol'].value;
					}
					var imgUrl = "<c:url value='img/map/'/>" + symbol + ".png";
					document.forms['optionsForm'].elements['senderSymbolImg'].src=imgUrl;
				}
			</script>
			<c:set var="selSymbol">${optionsCmd.senderDetails.symbol}</c:set>
			<select id="senderDetails.symbol" name="senderDetails.symbol"
			 	onchange="javascript:setSenderSymbolImage(this.value);"
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="symbolEntry" items="${optionsCmd.symbolEntries}">
  						<c:choose>
    						<c:when test="${symbolEntry.symbol eq selSymbol}">
						    	<option value="${symbolEntry.symbol.id}" selected>
						        	<c:out value="${symbolEntry.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
						      	<option value="${symbolEntry.symbol.id}">
						        	<c:out value="${symbolEntry.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
			</select></td>
			<td style="border:none;width:50%;"><img id="senderSymbolImg"/></td>
			</tr></table>
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${!empty selSenderId}">
					&nbsp;<spring:message code="sendermaintenance.update.remove" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;<spring:message code="sendermaintenance.add" />&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<td style="white-space: nowrap;">
			<div class="mlt-button">
				<c:choose>
					<c:when test="${!empty selSenderId}">
						&nbsp;<a href="#" onclick="javascript:updateSender();">
							<spring:message code="sendermaintenance.update.button" />
						</a>&nbsp;<a href="#" onclick="javascript:removeSender();">
							<spring:message code="sendermaintenance.remove.button" />
						</a>&nbsp;	
					</c:when>
					<c:otherwise>
						&nbsp;<a href="#" onclick="javascript:addSender();">
							<spring:message code="sendermaintenance.add.button" />
						</a>&nbsp;						
					</c:otherwise>
				</c:choose>
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 2}">
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
