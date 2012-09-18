<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<jsp:directive.page import="de.msk.mylivetracker.web.util.WebUtils" />
<% 
	String applicationBaseUrl = WebUtils.getApplicationBaseUrl(request);
	request.setAttribute("applicationBaseUrl", applicationBaseUrl);
%>

<script>	
	$(function() {
		$("#accordionId").val(<c:out value='${accordionId}' />);
		$("#accordion").accordion({ active: <c:out value='${accordionId}' /> });				 	
	});	
	function closeUploadProcessor(closePosProcessorSenderId, closePosProcessorThreadId) {
		$("#closePosProcessorSenderId").val(closePosProcessorSenderId);
		$("#closePosProcessorThreadId").val(closePosProcessorThreadId);
		execAction("closeUploadProcessor");
	}
	function execAction(action) {
		var activeIdx = $( "#accordion" ).accordion( "option", "active" );
		$("#accordionId").val(activeIdx);
		$("#action").val(action);
		document.forms["formAdmin"].submit();		
	}
</script>

<div id="divAdmin" style="height:98%;overflow:auto;">
<form id="formAdmin" name="formAdmin" action="admin.do" method="POST" >
<input type='hidden' id='accordionId' name='accordionId' value='0' >	
<input type='hidden' id='action' name='action' value='refreshPage' >
<input type='hidden' id='closePosProcessorSenderId' name='closePosProcessorSenderId' value='' >
<input type='hidden' id='closePosProcessorThreadId' name='closePosProcessorThreadId' value='' >

<table>
	<tr>
		<td style="width:100%">
			&nbsp;<c:out value="${result}"/>&nbsp;
		</td>
		<td style="text-align:right;white-space: nowrap;">
			<a href="mailto:support@mylivetracker.de?bcc=<c:out value='${emailAddresses}'/>">Email to users (bcc)</a>
		</td>
		<td style="text-align:right;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="performance/" >
					Performance Monitor
				</a>&nbsp;														
			</div>	
		</td>
		<td style="text-align:right;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a href="monitoring" >
					Server Monitor
				</a>&nbsp;														
			</div>			
		</td>
		<td style="text-align:right;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a onclick="javascript:execAction('refreshPage');" >
					Refresh
				</a>&nbsp;														
			</div>		
		</td>		
	</tr>	
</table>
<div id="accordion">
<h3><a href="#">Application parameters</a></h3>
<div>
<table>
	<tr>
		<th style="text-align: left">
			&nbsp;Application parameters&nbsp;
		</th>
		<th style="text-align:left;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a onclick="javascript:execAction('reloadApplicationParameters');" >
					Reload Application parameters
				</a>&nbsp;														
			</div>		
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Parameter Name&nbsp;						
		</th>
		<th style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Parameter Value&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<c:forEach items="${parameterMap}" var="parameter" >
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${parameter.key}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${parameter.value}"/>&nbsp;						
		</td>
		<td>
			&nbsp;&nbsp;							
		</td>
	</tr>
	</c:forEach>	
</table>
</div>
<h3><a href="#">User objects in cache</a></h3>
<div>
<table id="users">
	<tr>
		<th style="text-align: left;white-space: nowrap;" colspan="2">
			&nbsp;User objects in cache&nbsp;
		</th>
		<th style="text-align: left;white-space: nowrap;">
			&nbsp;Items in cache:&nbsp;<c:out value="${userCacheSize}"/>&nbsp;
		</th>
		<th style="text-align:left;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a onclick="javascript:execAction('clearUserCache');" >
					Clear user cache
				</a>&nbsp;														
			</div>		
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;User Id&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Name&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Login Count&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Last Login&nbsp;						
		</th>		
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<% try { %>
	<c:forEach items="${userMap}" var="user" >
	<c:if test="${!empty user.value.userId}">
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${user.value.userId}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${user.value.masterData.lastName}"/>,&nbsp;
			<c:out value="${user.value.masterData.firstName}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${user.value.loginCount}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty user.value.lastLogin}">
					&nbsp;<mlt:dateTime dateTime="${user.value.lastLogin}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet used&nbsp;												
				</c:otherwise>
			</c:choose>										
		</td>
		<td>
			&nbsp;&nbsp;							
		</td>
	</tr>
	</c:if>	
	</c:forEach>
  <% } catch (Exception e) { e.printStackTrace(); } %>	
</table>
</div>
<h3><a href="#">Ticket objects in cache</a></h3>
<div>
<table id="tickets">
	<tr>
		<th style="text-align: left;white-space: nowrap;" colspan="3">
			&nbsp;Ticket objects in cache&nbsp;
		</th>
		<th style="text-align: left;white-space: nowrap;">
			&nbsp;Items in cache:&nbsp;<c:out value="${ticketCacheSize}"/>&nbsp;
		</th>
		<th style="text-align:left;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a onclick="javascript:execAction('clearTicketCache');" >
					Clear ticket cache
				</a>&nbsp;														
			</div>		
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Ticket Type&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Ticket Id&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;User Id&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Created&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Last Used&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<% try { %>
	<c:forEach items="${ticketMap}" var="ticket" >
	<c:if test="${!empty ticket.value.ticketType}">
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${ticket.value.ticketType}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${ticket.key}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${ticket.value.userId}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<mlt:dateTime dateTime="${ticket.value.created}" />&nbsp;									
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty ticket.value.lastUsed}">
					&nbsp;<mlt:dateTime dateTime="${ticket.value.lastUsed}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet used&nbsp;												
				</c:otherwise>
			</c:choose>										
		</td>
		<td>
			&nbsp;&nbsp;							
		</td>
	</tr>
	</c:if>
	</c:forEach>
  <% } catch (Exception e) { e.printStackTrace(); } %>	
</table>
</div>
<h3><a href="#">Sender objects in cache</a></h3>
<div>
<table id="senders">
	<tr>
		<th style="text-align: left;white-space: nowrap;" colspan="2">
			&nbsp;Sender objects in cache&nbsp;
		</th>
		<th style="text-align: left;white-space: nowrap;">
			&nbsp;Items in cache:&nbsp;<c:out value="${senderCacheSize}"/>&nbsp;
		</th>
		<th style="text-align:left;white-space: nowrap;">	
			<div class="mlt-button">
				&nbsp;<a onclick="javascript:execAction('clearSenderCache');" >
					Clear sender cache
				</a>&nbsp;														
			</div>		
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;User Id&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Sender Id&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Name&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Time Zone&nbsp;						
		</th>
		<th style="width:100px;text-align:left;white-space: nowrap;">
			&nbsp;Switches&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<% try { %>
	<c:forEach items="${senderMap}" var="sender" >	
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${sender.value.userId}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${sender.value.senderId}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${sender.value.name}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${sender.value.timeZone}"/>&nbsp;						
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${sender.value.switches}" />&nbsp;									
		</td>
		<td>
			&nbsp;&nbsp;							
		</td>
	</tr>	
	</c:forEach>	
	<% } catch (Exception e) { e.printStackTrace(); } %>
</table>
</div>
<h3><a href="#">Current active tracks</a></h3>
<div>
<table>
	<tr>
		<th style="text-align: left" colspan="7">
			&nbsp;Current active tracks [found: <c:out value="${trackListResult.countFoundTracks}" />]&nbsp;
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;UserId&nbsp;
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;SenderId&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;TrackName&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;LastSignal&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;LastPosUpdate&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;PosCount&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;EmergencySignal&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>		
	<% try { %>
	<c:forEach items="${trackListResult.tracks}" var="track" >
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${track.userId}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${track.senderId}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<a target="_blank" href="<c:out value="${trackUrlMap[track.trackId]}"/>"><c:out value="${track.name}"/></a>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty track.timestamps.lastHeartbeatSent}">
					&nbsp;<mlt:dateTime dateTime="${track.timestamps.lastHeartbeatSent}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty track.timestamps.recentPositionUpdated}">
					&nbsp;<mlt:dateTime dateTime="${track.timestamps.recentPositionUpdated}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${track.countPositions}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty track.recentEmergencySignal}">
					&nbsp;<mlt:dateTime dateTime="${track.recentEmergencySignal.timeReceived}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td>
			&nbsp;							
		</td>
	</tr>
	</c:forEach>	
	<% } catch (Exception e) { e.printStackTrace(); } %>
</table>	
</div>
<h3><a href="#">Status of Upload Processors</a></h3>
<div>
<table>
	<tr>
		<th style="text-align: left" colspan="6">
			&nbsp;Status of Upload Processors&nbsp;
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;ServerName&nbsp;
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;ProcessorType&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;RemoteInfo&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;SenderId&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;DataInterpreter&nbsp;						
		</th>
		<th style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;LastDataReceived&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>	
	<tr>			
		<th colspan="3" style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;LastDataString&nbsp;						
		</th>
		<th colspan="3" style="text-align:left;width:200px;white-space: nowrap;">
			&nbsp;LastResultMsg&nbsp;						
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<c:forEach items="${uploadProcessors}" var="uploadProcessor" >
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${uploadProcessor.serverName}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${uploadProcessor.processorType}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${uploadProcessor.remoteInfo}"/>&nbsp;
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty uploadProcessor.senderFromRequest}">
					&nbsp;<c:out value="${uploadProcessor.senderFromRequest.senderId}"/>&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet identified&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty uploadProcessor.dataInterpreter}">
					&nbsp;<c:out value="${uploadProcessor.dataInterpreter}"/>&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet identified&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align:center;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty uploadProcessor.lastDataReceived}">
					&nbsp;<mlt:dateTime dateTime="${uploadProcessor.lastDataReceived}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>			
		</td>
		<td>
			&nbsp;							
		</td>
	</tr>
	<tr>	
		<td colspan="3" style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty uploadProcessor.lastDataString}">
					&nbsp;<c:out value="${uploadProcessor.lastDataString}"/>&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td colspan="3" style="text-align:left;white-space: nowrap;">
			<c:choose>
				<c:when test="${!empty uploadProcessor.lastProcessResult}">
					&nbsp;<c:out value="${uploadProcessor.lastProcessResult.resultMsg}"/>&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>
		</td>
		<td>
			&nbsp;							
		</td>
	</tr>	
	</c:forEach>	
</table>	
</div>
<h3><a href="#">Status of Position Processor Qeues</a></h3>
<div>
<table>
	<tr>
		<th style="text-align: left" colspan="13">
			&nbsp;Status of Position Processor Qeues&nbsp;
		</th>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<th style="text-align:center;width:200px;white-space: nowrap;">
			&nbsp;ThreadId&nbsp;<br>
			&nbsp;UserId&nbsp;						
		</th>
		<th style="text-align:center;width:200px;white-space: nowrap;">
			&nbsp;Sender&nbsp;						
		</th>
		<th style="text-align:center;width:100px;white-space: nowrap;">
			&nbsp;created&nbsp;<br>						
			&nbsp;updated&nbsp;
		</th>
		<th style="text-align:center;width:50px;white-space: nowrap;">
			&nbsp;PP&nbsp;						
		</th>
		<th style="text-align:center;width:50px;white-space: nowrap;">
			&nbsp;PS&nbsp;						
		</th>
		<th style="text-align:center;width:50px;white-space: nowrap;">
			&nbsp;PQ&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;Status&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;EO&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;RE&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;MRE&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;SD&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;SF&nbsp;						
		</th>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;&nbsp;				
		</th>		
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<c:forEach items="${storePosProcessors}" var="storePosProcessor" >
	<tr>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.id}"/>&nbsp;<br>
			&nbsp;<c:out value="${storePosProcessor.info.user.userId}"/>&nbsp;			
		</td>
		<td style="text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.sender.senderId}"/>&nbsp;<br>
			&nbsp;<c:out value="${storePosProcessor.info.sender.name}"/>&nbsp;			
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<mlt:dateTime dateTime="${storePosProcessor.info.started}" />&nbsp;<br>
			<c:choose>
				<c:when test="${!empty storePosProcessor.info.updated}">
					&nbsp;<mlt:dateTime dateTime="${storePosProcessor.info.updated}" />&nbsp;
				</c:when>
				<c:otherwise>
					&nbsp;not yet received&nbsp;												
				</c:otherwise>
			</c:choose>			
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.processedPositions}"/>&nbsp;
		</td>		
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.skippedPositions}"/>&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.positionsInQueue}"/>&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;
			<c:choose>
				<c:when test="${storePosProcessor.info.expired}">
					<font color="blue">EX</font>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${storePosProcessor.info.lastStatus}">
							<font color="green">OK</font>
						</c:when>	
						<c:otherwise>
							<font color="red">NOK</font>
						</c:otherwise>
					</c:choose>		
				</c:otherwise>				
			</c:choose>
			&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.errorCount}"/>&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.retryCount}"/>&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.maxNumberOfRetriesOccurred}"/>&nbsp;
		</td>	
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.sendEmergencySmsDone}"/>&nbsp;
		</td>
		<td style="text-align:center;white-space: nowrap;">
			&nbsp;<c:out value="${storePosProcessor.info.sendEmergencySmsFailed}"/>&nbsp;
		</td>
		<th style="width:50px;white-space: nowrap;">
			&nbsp;<div class="mlt-button">
				&nbsp;<a onclick="javascript:closeUploadProcessor('<c:out value="${storePosProcessor.info.sender.senderId}"/>', '<c:out value="${storePosProcessor.id}"/>');" >
					Close
				</a>&nbsp;														
			</div>&nbsp;						
		</th>	
		<td>
			&nbsp;							
		</td>
	</tr>
	<c:if test="${!empty storePosProcessor.info.lastErrorOccurred}">
		<tr>
			<td style="text-align:left;white-space: nowrap;">
				&nbsp;Last Error&nbsp;
			</td>
			<td style="text-align:center;white-space: nowrap;">
				&nbsp;<mlt:dateTime dateTime="${storePosProcessor.info.lastErrorOccurred}" />&nbsp;
			</td>
			<td colspan="11" style="text-align:left;white-space: nowrap;" title="<c:out value="${storePosProcessor.info.lastErrorMsg}"/>">
				&nbsp;<c:out value="${storePosProcessor.info.lastErrorMsgAbbr}"/>&nbsp;
			</td>
			<td>
				&nbsp;							
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty storePosProcessor.info.lastSmsErrorOccurred}">
		<tr>
			<td style="text-align:left;white-space: nowrap;">
				&nbsp;Last Sms Error&nbsp;
			</td>
			<td style="text-align:center;white-space: nowrap;">
				&nbsp;<mlt:dateTime dateTime="${storePosProcessor.info.lastSmsErrorOccurred}" />&nbsp;
			</td>
			<td colspan="11" style="text-align:left;white-space: nowrap;">
				&nbsp;<c:out value="${storePosProcessor.info.lastSmsErrorMsg}"/>&nbsp;
			</td>
			<td>
				&nbsp;							
			</td>
		</tr>
	</c:if>		
	</c:forEach>	
	<tr>
		<td colspan="13">
		&nbsp;<b>PP</b>=positions processed, 
			<b>PS</b>=positions skipped, 
			<b>PQ</b>=positions in queue,<br>
		&nbsp;<b>EO</b>=errors occurred,
			<b>RE</b>=retries executed,
			<b>MRE</b>=max retries executed,<br>
		&nbsp;<b>SD</b>=sms sent done,
			<b>SF</b>=sms sent failed
		&nbsp;
		</td>
		<td>
			&nbsp;&nbsp;							
		</td>
	</tr>	
</table>	
</div>
<h3><a href="#">Register new User</a></h3>
<div>
<table>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;User id&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='userId' name='userId' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Plain password&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='plainPassword' name='plainPassword' value='test2012' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Last name&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='lastName' name='lastName' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;First name&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='firstName' name='firstName' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Email address&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='emailAddress' name='emailAddress' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Language ('en' or 'de')&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='language' name='language' value='en' size="10">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td colspan="2" style="width:400px;text-align:left;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a onclick='javascript:execAction("registerUser");' >
					Register new User
				</a>&nbsp;														
			</div>					
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
</table>
</div>
<h3><a href="#">Create new demo track</a></h3>
<div>
<table>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Track id&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='trackId' name='trackId' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td colspan="2" style="width:400px;text-align:left;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a onclick='javascript:execAction("createDemoTrack");' >
					Create new demo track
				</a>&nbsp;														
			</div>					
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
</table>
</div>
<h3><a href="#">Start / Stop demo</a></h3>
<div>
<table>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Demo status&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<c:out value="${demoStatus}"/>&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td colspan="2" style="width:400px;text-align:left;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a onclick='javascript:execAction("startStopDemo");' >
					Start / Stop demo
				</a>&nbsp;														
			</div>					
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
</table>
</div>
</div>
</form>
</div>

