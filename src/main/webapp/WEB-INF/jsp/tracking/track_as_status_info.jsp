<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page import="de.msk.mylivetracker.web.util.WebUtils" %>

<c:if test="${!empty css}">
	<style type="text/css">
		<c:out value="${css}"/>	
	</style>
</c:if>

<script type="text/javascript" >
	function setValues(data) {
		var SPACE = "&nbsp;";
		$("#valueUserName").html(SPACE + data.user.name);
		if (data.track != null) {							
			$("#divHomeLocation").hide();
			$("#divActiveTrack").show();
			$("#valueSenderName").html(SPACE + data.track.senderName + SPACE);
			$("#valueTrackName").html(SPACE + data.track.name + SPACE);
			$("#valueLastUpdated").html(SPACE + data.track.updated + SPACE);
			
			if (data.valuePositionGoogleMapsUrl == null) {
				$("#divPositionWithUrl").hide();
				$("#divPositionWithoutUrl").show();				
				if (data.track.recPos == null) {
					$("#divPositionWithoutUrl").html(SPACE + data.jsonCommons.notFound + SPACE);
				} else {					
					$("#divPositionWithoutUrl").html(SPACE + data.track.recPos.info + SPACE);					
				} 
			} else {
				$("#divPositionWithoutUrl").hide();
				$("#divPositionWithUrl").show();								
				$("#valuePositionGoogleMapsUrl").html(data.track.recPos.info);				
				$("#valuePositionGoogleMapsUrl").attr('href', data.valuePositionGoogleMapsUrl);				
			}															
			if (data.track.recMobNwCell == null) {
				$("#valueMobNwCell").html(SPACE + data.jsonCommons.notFound + SPACE);
			} else {				
				$("#valueMobNwCell").html(SPACE + data.track.recMobNwCell.info + SPACE);
			}			
			if (data.track.recMsg == null) {
				$("#valueMessage").html(SPACE + data.jsonCommons.notFound + SPACE);
			} else {
				$("#valueMessage").html(SPACE + data.track.recMsg.msg + SPACE);
			}
			if (data.track.recCardiacFunction == null) {
				$("#valueCardiacFunction").html(SPACE + data.jsonCommons.notFound + SPACE);
			} else {
				$("#valueCardiacFunction").html(SPACE + data.track.recCardiacFunction.info + SPACE);
			}
			if (data.track.recSenderState == null) {
				$("#updateStatusDevice").html(SPACE + 
					"<spring:message code='track.status.labelSenderStatus' />:" +	
					SPACE + data.jsonCommons.notFound + SPACE);				
			} else {
				$("#updateStatusDevice").html(SPACE +
					"<spring:message code='track.status.labelSenderStatus' />:" +
					SPACE + data.track.recSenderState.info + SPACE);
			}
			$("#valueStatusAsGoogleMaps").attr('href', data.valueStatusAsGoogleMaps);			
		} else {			
			$("#divActiveTrack").hide();
			$("#divHomeLocation").show();
			if (!data.user.hasHome) {
				$("#divHomeLocationWithUrl").hide();
				$("#divHomeLocationWithoutUrl").show();
				$("#divHomeLocationWithoutUrl").html(SPACE + data.user.homeAddr + SPACE);				
			} else {
				$("#divHomeLocationWithoutUrl").hide();
				$("#divHomeLocationWithUrl").show();
				$("#valueHomeGoogleMapsUrl").html(data.user.homeAddr);
				$("#valueHomeGoogleMapsUrl").attr('href', data.valueHomeGoogleMapsUrl);
			}																			
		}	
		$("#updateStatus").html(SPACE +
			"<spring:message code='track.status.labelStatusUpdated' />" + 
			SPACE + data.jsonCommons.statusUpdated + SPACE);									
	}
	
	$(document).ready(			
		function update() {								
			var reqType = "json";
			var userId = "<c:out value='${userId}'/>";
			var ticketId = "<c:out value='${ticketId}'/>";
			var senderId = "<c:out value='${senderId}'/>";		
			var trackId = "<c:out value='${trackId}'/>";		
			$.getJSON('track_as_status_info.sec', 
				{reqType:reqType,userId:userId,ticketId:ticketId,senderId:senderId,trackId:trackId},
				setValues
			);
			<c:if test="${live}"> 		
				window.setTimeout(update, <c:out value="${intervalSecs}" /> * 1000);
			</c:if>
		}
	);			     					
</script>

<table
		<c:choose>
			<c:when test="${!empty width}"> 
			style="width:<c:out value="${width}"/>px;height:<c:out value="${height}"/>px;"
			</c:when>
			<c:otherwise>
			style="width:100%;height:100%;"
			</c:otherwise>
		</c:choose>
		class="track-status-content"
>
<tr style="vertical-align: top;height:5px;"><td>
	<table class="track-status-content" >
		<tr>
			<td style="border:none;height:5px;">
				<div id="valueUserName" class="track-status-username">
				</div>
			</td>
		</tr>	
	</table>
</td></tr>
<tr style="vertical-align: top">
	<td style="border:none;">
	<table id="divHomeLocation" class="track-status-content">
		<tr>
			<td style="height:5px;" >
				<div id="labelNoActiveTrack" class="track-status-noactivetrack">
					&nbsp;<spring:message code='track.status.labelNoActiveTrack' />&nbsp;
				</div>						
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelHomeLocation" class="track-status-values">
					&nbsp;<spring:message code='track.status.labelHomeLocation' />&nbsp;
				</div>						
			</td>
		</tr>		
		<tr>	
			<td style="height:5px" >
				<div id="divHomeLocationWithUrl" style="margin-left: 4px;" class="track-status-values">
					<a id="valueHomeGoogleMapsUrl" target="_blank" href="">																
					</a>			
				</div>
				<div id="divHomeLocationWithoutUrl" class="track-status-values">
				</div>
			</td>
		</tr>
	</table>	
	<table id="divActiveTrack" class="track-status-content">		
		<tr>
			<td style="height:5px;" >
				<div id="labelSenderName" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelSenderName' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueSenderName" class="track-status-values">
				</div>
			</td>
		</tr>		
		<tr>
			<td style="height:5px" >
				<div id="labelTrackName" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelTrackName' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueTrackName" class="track-status-trackname">
				</div>
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelLastUpdated" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelLastUpdated' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueLastUpdated" class="track-status-values">
				</div>
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelPosition" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelPosition' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="divPositionWithUrl" style="margin-left: 4px;" class="track-status-values">
					<a id="valuePositionGoogleMapsUrl" target="_blank" href="" class="track-status-values">					
					</a>			
				</div>
				<div id="divPositionWithoutUrl" class="track-status-values">
				</div>
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelMobNwCell" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelMobNwCell' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueMobNwCell" class="track-status-values">
				</div>
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelMessage" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelMessage' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueMessage" class="track-status-values">
				</div>
			</td>
		</tr>
		<tr>
			<td style="height:5px" >
				<div id="labelCardiacFunction" class="track-status-labels">
					&nbsp;<spring:message code='track.status.labelCardiacFunction' />&nbsp;
				</div>						
			</td>
			<td>
				<div id="valueCardiacFunction" class="track-status-values">
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div style="margin-left: 4px;" class="track-status-labels">
					<a id="valueStatusAsGoogleMaps" target="_blank" href="" class="track-status-labels">					
						<spring:message code="track.status.labelStatusAsMap" />					
					</a>			
				</div>
			</td>
		</tr>			
	</table>
<div>
<table class="track-status-content">
	<tr>
		<td colspan="2">
			<div id="updateStatusDevice" class="track-status-footer">
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="updateStatus" class="track-status-footer">													
			</div>
		</td>
	</tr>
</table>
</div>
</td></tr>
</table>