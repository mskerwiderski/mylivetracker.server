<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<style type="text/css" title="tableStyle"> 
table.display tr.even {
	background-color: #F2F2F2;
}
table.display tr.odd {
	background-color: #FFFFFF;
}
table.display tr.odd.active {
	background-color: #E0F8E0;
}
table.display tr.even.active {
	background-color: #EFFBEF;
}
table.display tr.odd.emergency {
	background-color: #F8E0E0;
}
table.display tr.even.emergency {
	background-color: #FBEFEF;
}
</style>

<script type="text/javascript">
	function calcTableAndMapHeight(offset) {
		var wnd = $(document).height();
		mlt_log("window-height: " + wnd);
		mlt_log("offset: " + offset);
		return wnd-offset;	
	}
	
	function refreshTrackOverview(selectedTracksView) {
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = "RefreshTrackOverview";
		if (selectedTracksView != null) {
			document.forms["tracksOverviewForm"].elements["selectedTracksView"].value = selectedTracksView;
		}
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();
	}
	function editTrack(actionExecutor, selectedTrackId) {	
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = actionExecutor;		
		document.forms["tracksOverviewForm"].elements["selectedTrackId"].value = selectedTrackId;
		var trackName =
			document.forms["tracksOverviewForm"].elements["trackNames[" + selectedTrackId + "]"].value;
		var trackActivityStatus =
			document.forms["tracksOverviewForm"].elements["trackActivityStati[" + selectedTrackId + "]"].value;
		var trackReleaseStatus =
			document.forms["tracksOverviewForm"].elements["trackReleaseStati[" + selectedTrackId + "]"].value;
		document.forms["tracksOverviewForm"].elements["selectedTrackName"].value = trackName;
		document.forms["tracksOverviewForm"].elements["selectedTrackActivityStatus"].value = trackActivityStatus;
		document.forms["tracksOverviewForm"].elements["selectedTrackReleaseStatus"].value = trackReleaseStatus;
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();		
	}
	function renameTrack(selectedTrackId) {		
		var keyCode=event.keyCode;
	  	if(keyCode==13) {
	  		editTrack("RenameTrack", selectedTrackId);
	  	}
	}
	function createTrack() {
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = "CreateTrack";		
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();		 
	}
	function startTracking(actionExecutor, selectedTrackId) {		
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = actionExecutor;
		document.forms["tracksOverviewForm"].elements["selectedTrackId"].value = selectedTrackId;
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();		 
	}				
	function switchLiveTrackingOnOff() {
		if (document.forms["tracksOverviewForm"].elements["selectedLiveTrackingOpt"].value == "true") {
			$("#divLiveTrackingOptsPositions").show();
			$("#divLiveTrackingOptsInterval").show();
			$("#divLiveTrackingOptsZoom").show();
			$("#divLiveTrackingOptsLabelPositions").show();
			$("#divLiveTrackingOptsLabelInterval").show();
			$("#divLiveTrackingOptsLabelZoom").show();
		} else {
			$("#divLiveTrackingOptsPositions").hide();	
			$("#divLiveTrackingOptsInterval").hide();
			$("#divLiveTrackingOptsZoom").hide();
			$("#divLiveTrackingOptsLabelPositions").hide();
			$("#divLiveTrackingOptsLabelInterval").hide();
			$("#divLiveTrackingOptsLabelZoom").hide();
		}
	}	
	function versionInfo(trackId, trackName) {
		var textTempl = '<spring:message code="overview.version.info.confirm.text" />';
		var text = textTempl.replace(/\{0\}/g, "<c:out value='${tracksOverviewCmd.versionStr}'/>");
		text = text.replace(/\{1\}/g, "<c:out value='${tracksOverviewCmd.forumLink}'/>");
		$confirmVersionInfoDlg.html(text);	
		$confirmVersionInfoDlg.dialog('open');
	}
	var $confirmVersionInfoDlg;
	function removeTrack(trackId, trackName) {
		var textTempl = '<spring:message code="overview.track.remove.confirm.text" />';
		var text = textTempl.replace(/\{0\}/g, trackName);			
		$confirmRemoveTrackDlg.html(text);	
		$confirmRemoveTrackDlg.dialog('option', 'trackId', trackId);
		$confirmRemoveTrackDlg.dialog('open');
	}
	var $confirmRemoveTrackDlg;
	function resetTrack(trackId, trackName) {
		var textTempl = '<spring:message code="overview.track.reset.confirm.text" />';
		var text = textTempl.replace(/\{0\}/g, trackName);			
		$confirmResetTrackDlg.html(text);	
		$confirmResetTrackDlg.dialog('option', 'trackId', trackId);
		$confirmResetTrackDlg.dialog('open');
	}
	var $confirmResetTrackDlg;
	 
	$(document).ready(function() {
		switchLiveTrackingOnOff();
		
		$confirmVersionInfoDlg = $('#versionInfoDialog')
		.dialog({
			modal: true,				
			autoOpen: false,
			closeOnEscape: true,
			title: '<spring:message code="overview.version.info.confirm.header" />',
			buttons: {
				'<spring:message code="overview.version.info.confirm.ok" />': function() {
					$(this).dialog('close');
				}
			}					
		});
		
		$confirmRemoveTrackDlg = $('#removeTrackDialog')
		.dialog({
			modal: true,				
			autoOpen: false,
			closeOnEscape: true,
			title: '<spring:message code="overview.track.remove.confirm.header" />',
			buttons: {
				'<spring:message code="overview.track.remove.confirm.no" />': function() {
					$(this).dialog('close');
				},
				'<spring:message code="overview.track.remove.confirm.yes" />': function() {
					editTrack('RemoveTrack',
						$(this).dialog('option', 'trackId'));
					$(this).dialog('close');
				}										
			}					
		});				

		$confirmResetTrackDlg = $('#resetTrackDialog')
		.dialog({
			modal: true,				
			autoOpen: false,
			closeOnEscape: true,
			title: '<spring:message code="overview.track.reset.confirm.header" />',
			buttons: {
				'<spring:message code="overview.track.reset.confirm.no" />': function() {
					$(this).dialog('close');
				},
				'<spring:message code="overview.track.reset.confirm.yes" />': function() {
					editTrack('ResetTrack',
						$(this).dialog('option', 'trackId'));
					$(this).dialog('close');
				}										
			}					
		});	
		
		<c:if test="${tracksOverviewCmd.showVersionInfo}">
			versionInfo();
		</c:if> 
	});
	
	var mlt_getOnlyActiveTracks = false;
	var mlt_versionId = null;
	
	function mlt_refreshTracksOverview() {
   		var senderId = document.forms["tracksOverviewForm"].elements["selectedSenderFilter"].value;
   		var datePeriod = document.forms["tracksOverviewForm"].elements["selectedDatePeriodFilter"].value;
   		var searchStr = document.forms["tracksOverviewForm"].elements["selectedSearchStrFilter"].value;
		$.getJSON('tracks_list.do', {
			versionId:mlt_versionId,
			senderId:senderId,
			datePeriod:datePeriod,
			searchStr:searchStr,
			onlyActive:mlt_getOnlyActiveTracks},
			mlt_processData
		);
		$("#refreshIcon").attr("src", "img/led/arrow_refresh.png");
   	}
	
	function mlt_processData(mlt_data) {
		if (mlt_data == null) return;
		if ((mlt_versionId != null) && (mlt_versionId == mlt_data.jsonCommons.versionId)) {
			return;
		}
		mlt_versionId = mlt_data.jsonCommons.versionId;
		mlt_renderTracksOverview(mlt_data);
	}
	
	$(document).ready(
		function mlt_processRequest() {
			mlt_refreshTracksOverview();
	   	}
    );
</script> 

<script>
var autoRefreshTimer = null;
var showRedBullotSecs = null;
function startAutoRefresh() {
	autoRefreshTimer = document.forms["tracksOverviewForm"].elements["selectedTracksOverviewOptRefresh"].value;
	if (autoRefreshTimer > 1) {
		showRedBullotSecs = 2;
	} else {
		showRedBullotSecs = 1;
	}
	autoRefresh();
}
function autoRefresh() {			
	if (autoRefreshTimer == 0) {
		mlt_refreshTracksOverview();
		startAutoRefresh();
	} else if (autoRefreshTimer == -1) { 
		setTimeout("autoRefresh()", 1000);
	} else {
		if (autoRefreshTimer == showRedBullotSecs) {
			$("#refreshIcon").attr("src", "img/glossbasic/bullet_red.png");
		}
		autoRefreshTimer--;
		setTimeout("autoRefresh()", 1000);
	}
}
window.onload=startAutoRefresh;
</script>

<div id="versionInfoDialog" style="display: none">
</div>

<div id="removeTrackDialog" style="display: none">
</div>

<div id="resetTrackDialog" style="display: none">
</div>

<form:form id="tracksOverviewForm" name="tracksOverviewForm" 
	commandName="tracksOverviewCmd" acceptCharset="UTF-8">
	<form:hidden path="actionExecutor"/>
	<form:hidden path="selectedTracksView"/>
	<form:hidden path="selectedTrackId"/>	
	<form:hidden path="selectedTrackName"/>
	<form:hidden path="selectedTrackActivityStatus"/>
	<form:hidden path="selectedTrackReleaseStatus"/>
<div id="divTracksOverviewHeader">	
<table> 
	<tr style="padding: 0px;border-spacing: 0px;">
		<security:authorize ifAnyGranted="Admin,User">
		<c:if test="${!empty tracksOverviewCmd.senderEntriesForCreateTrack}">
		<td style="padding: 0px;border-spacing: 0px;width:150px;white-space: nowrap;background-color: #f2f2f2;">
			<table style="padding: 0px;border-spacing: 0px;">
				<tr>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
						border:none;border-right:solid;border-right-width:1px;
						text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
						&nbsp;<spring:message code="overview.label.create.track.line1" />&nbsp;&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
						&nbsp;&nbsp;<spring:message code="overview.label.create.track.sender" />&nbsp;&nbsp;
					</td>
					<td rowspan="2" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;">			
						<div class="mlt-button">
							&nbsp;<a href="#" onclick="javascript:createTrack();">
								<spring:message code='overview.create.track.button' />
							</a>&nbsp;														
						</div>	
					</td>					
				</tr>
				<tr>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
						border:none;border-right:solid;border-right-width:1px;
						text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
						&nbsp;<spring:message code="overview.label.create.track.line2" />&nbsp;&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;">
						&nbsp;<form:select									 
							path="selectedSenderForCreateTrack" 				 
							multiple="false"
							cssStyle="border-style: groove; border-width: 2px;"
							onchange="javascript:refreshTrackOverview(null);"
							>
							<c:forEach var="senderEntry" 
								items="${tracksOverviewCmd.senderEntriesForCreateTrack}"
								varStatus="status">
			    				<form:option value="${senderEntry.value}"  
			    					label="${senderEntry.label}"/>
			  				</c:forEach>
						</form:select>
					</td>
				</tr>
			</table>							
		</td>
		</c:if>
		</security:authorize>
		<td style="padding: 0px;border-spacing: 0px;height:35px;white-space: nowrap;width:100%;background-color: #f2f2f2;">
			&nbsp;		
		</td>
		<td style="padding: 0px;border-spacing: 0px;background-color: #e0f8e0;">
		<table style="padding: 0px;border-spacing: 0px;">
		<tr>
			<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
				border:none;border-right:solid;border-right-width:1px;
				text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
				&nbsp;<spring:message code="overview.label.livetracking.line1" />&nbsp;&nbsp;
			</td>
			<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
				&nbsp;&nbsp;<spring:message code="overview.livetracking" />&nbsp;&nbsp;
			</td>
			<td id="divLiveTrackingOptsLabelPositions" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
				&nbsp;&nbsp;<spring:message code="overview.livetracking.keep.positions" />&nbsp;&nbsp;
			</td>
			<td id="divLiveTrackingOptsLabelInterval" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
				&nbsp;&nbsp;<spring:message code="overview.livetracking.updateinterval" />&nbsp;&nbsp;
			</td>
			<td id="divLiveTrackingOptsLabelZoom" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
				&nbsp;&nbsp;<spring:message code="overview.livetracking.flytomode" />&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
				border:none;border-right:solid;border-right-width:1px;
				text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
				&nbsp;<spring:message code="overview.label.livetracking.line2" />&nbsp;&nbsp;
			</td>
			<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;">
				<c:set var="selectedLiveTrackingOpt">${tracksOverviewCmd.selectedLiveTrackingOpt}</c:set>
				&nbsp;<select id="selectedLiveTrackingOpt" name="selectedLiveTrackingOpt" 
					style="border-style: groove; border-width: 2px;"
					onchange="javascript:refreshTrackOverview(null);">
					<c:forEach var="liveTrackingOpt" items="${tracksOverviewCmd.liveTrackingOpts}">
	 						<c:choose>
	   						<c:when test="${liveTrackingOpt.value == selectedLiveTrackingOpt}">
						    	<option value="${liveTrackingOpt.value}" selected>
						        	<spring:message code="${liveTrackingOpt.label}" />
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${liveTrackingOpt.value}" >
						        	<spring:message code="${liveTrackingOpt.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
				</select>&nbsp;			
			</td>
			<td id="divLiveTrackingOptsPositions" style="padding: 0px;border-spacing: 0px; white-space: nowrap;border:none;">
				<c:set var="selectedLiveTrackingOptKeepRecentPos">${tracksOverviewCmd.selectedLiveTrackingOptKeepRecentPos}</c:set>		
				&nbsp;<select id="selectedLiveTrackingOptKeepRecentPos" name="selectedLiveTrackingOptKeepRecentPos"
					style="border-style: groove; border-width: 2px;" onchange="javascript:refreshTrackOverview(null);"> 
					<c:forEach var="optKeepRecentPosition" items="${tracksOverviewCmd.liveTrackingOptsKeepRecentPos}">
	 						<c:choose>
	   						<c:when test="${optKeepRecentPosition.value == selectedLiveTrackingOptKeepRecentPos}">
						    	<option value="${optKeepRecentPosition.value}" selected>
						        	<spring:message code="${optKeepRecentPosition.label}" />
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${optKeepRecentPosition.value}" >
						        	<spring:message code="${optKeepRecentPosition.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
				</select>				
			</td>
			<td id="divLiveTrackingOptsInterval" style="padding: 0px;border-spacing: 0px; white-space: nowrap;border:none;">	
				<c:set var="selectedLiveTrackingOptUpdateInterval">${tracksOverviewCmd.selectedLiveTrackingOptUpdateInterval}</c:set>	
				&nbsp;<select id="selectedLiveTrackingOptUpdateInterval" name="selectedLiveTrackingOptUpdateInterval"
					style="border-style: groove; border-width: 2px;" onchange="javascript:refreshTrackOverview(null);"> 
					<c:forEach var="optUpdateInterval" items="${tracksOverviewCmd.liveTrackingOptsUpdateInterval}">
	 						<c:choose>
	   						<c:when test="${optUpdateInterval.value == selectedLiveTrackingOptUpdateInterval}">
						    	<option value="${optUpdateInterval.value}" selected>
						        	<spring:message code="${optUpdateInterval.label}" />
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${optUpdateInterval.value}" >
						        	<spring:message code="${optUpdateInterval.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
				</select>		
			</td>
			<td id="divLiveTrackingOptsZoom" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;">				
				<c:set var="selectedLiveTrackingOptFlyToMode">${tracksOverviewCmd.selectedLiveTrackingOptFlyToMode}</c:set>
				&nbsp;<select id="selectedLiveTrackingOptFlyToMode" name="selectedLiveTrackingOptFlyToMode" 
					style="border-style: groove; border-width: 2px;" onchange="javascript:refreshTrackOverview(null);"> 
					<c:forEach var="optFlyToMode" items="${tracksOverviewCmd.liveTrackingOptsFlyToMode}">
	 						<c:choose>
	   						<c:when test="${optFlyToMode.value == selectedLiveTrackingOptFlyToMode}">
						    	<option value="${optFlyToMode.value}" selected>
						        	<spring:message code="${optFlyToMode.label}" />
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${optFlyToMode.value}" >
						        	<spring:message code="${optFlyToMode.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
				</select>&nbsp;			
			</td>	
		</tr>
		</table>
		</td>
	</tr>
</table>
<table>
	<tr style="padding: 0px;border-spacing: 0px;">
		<td style="padding: 0px;border-spacing: 0px;text-align: right;background-color: #f2f2f2;">
			<table style="padding: 0px;border-spacing: 0px;">
				<tr style="padding: 0px;border-spacing: 0px;">
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
						border:none;border-right:solid;border-right-width:1px;
						text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
						&nbsp;<spring:message code="overview.label.filter.line1" />&nbsp;&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
						&nbsp;&nbsp;<spring:message code="overview.label.filter.sender" />&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
						&nbsp;<spring:message code="overview.label.filter.timeperiod" />&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
						&nbsp;<spring:message code="overview.label.filter.trackname" />&nbsp;
					</td>
				</tr>	
				<tr style="padding: 0px;border-spacing: 0px;">
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;
						border:none;border-right:solid;border-right-width:1px;
						text-align: left;font-weight: bold;font-size: small;font-variant: small-caps;">
						&nbsp;<spring:message code="overview.label.filter.line2" />&nbsp;&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;">
						&nbsp;<form:select									 
							path="selectedSenderFilter" 				 
							multiple="false"
							cssStyle="border-style: groove; border-width: 2px;"
							onchange="javascript:refreshTrackOverview(null);"
							>
							<c:forEach var="senderEntry" 
								items="${tracksOverviewCmd.senderEntriesForFilter}"
								varStatus="status">
			    				<form:option value="${senderEntry.value}"  
			    					label="${senderEntry.label}"/>
			  				</c:forEach>
						</form:select>&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;">	
						<c:set var="selectedDatePeriodFilter">${tracksOverviewCmd.selectedDatePeriodFilter}</c:set>
						<select id="selectedDatePeriodFilter" name="selectedDatePeriodFilter"
							style="border-style: groove; border-width: 2px;" 
							onchange="javascript:refreshTrackOverview(null);" > 
							<c:forEach var="optDatePeriod" items="${tracksOverviewCmd.tracksOverviewOptsDatePeriod}">
			 						<c:choose>
			   						<c:when test="${optDatePeriod.value == selectedDatePeriodFilter}">
								    	<option value="${optDatePeriod.value}" selected>
								        	<spring:message code="${optDatePeriod.label}" />
								      	</option>
								    </c:when>
							    	<c:otherwise>
							      		<option value="${optDatePeriod.value}" >
								        	<spring:message code="${optDatePeriod.label}" />
								      	</option>
								    </c:otherwise>
								</c:choose>
							</c:forEach>
						</select>&nbsp;
					</td>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;">
						<spring:message code="overview.track.table.title.track.name.search" var="titleTrackNameSearch" />
						<form:input cssStyle="margin-top:-1px;margin-left:3px;width:150px; text-align:left; border-style: groove; border-width: 2px;"
							onkeypress="javascript:checkSearchEnterPressed();"
							path="selectedSearchStrFilter"
							title="${titleTrackNameSearch}" />&nbsp;
						<img src="img/led/cross.png" onclick="javascript:resetSearchStr();" 
							style="border: none;margin-bottom: -3px;"/>&nbsp;	
						<script>
							document.getElementById("selectedSearchStrFilter").focus();
							function resetSearchStr() {
								document.getElementById("selectedSearchStrFilter").value = "";
								refreshTrackOverview(null);
							}
							function checkSearchEnterPressed(e) {
							  	var keyCode=(e)? e.which :event.keyCode;
							  	if(keyCode==13) {
							  		refreshTrackOverview(null);
							  	}
							}
						</script>	
					</td>
				</tr>
			</table>
		</td>			
		<td style="padding: 0px;border-spacing: 0px;height:35px;white-space: nowrap;width:100%;background-color: #f2f2f2;">
			&nbsp;		
		</td> 
		<td style="padding: 0px;border-spacing: 0px;text-align: right;background-color: #f2f2f2;">
			<table style="padding: 0px;border-spacing: 0px;">
				<tr style="padding: 0px;border-spacing: 0px;">
					<td rowspan="2" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;text-align: center;">	
						<div class="mlt-button">
							<c:choose>
								<c:when test="${tracksOverviewCmd.selectedTracksView eq 'Table'}">
									&nbsp;<a href="#" onclick="javascript:refreshTrackOverview('Map');">					
										<img src="img/led/map.png" style="border: none;" />
									</a>&nbsp;
								</c:when>
								<c:otherwise>
									&nbsp;<a href="#" onclick="javascript:refreshTrackOverview('Table');">					
										<img src="img/led/table.png" style="border: none; "/>
									</a>&nbsp;
								</c:otherwise>
							</c:choose>
						</div>						
					</td>	
					<c:choose>
						<c:when test="${tracksOverviewCmd.selectedTracksView eq 'Map'}">
							<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
								&nbsp;<spring:message code="overview.map.flytomode" />&nbsp;
							</td>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;font-size: x-small;">
						&nbsp;<spring:message code="overview.refresh.interval" />&nbsp;
					</td>
					<td rowspan="2" style="padding: 0px;border-spacing: 0px;white-space: nowrap;border:none;text-align: center;">	
						<div class="mlt-button">
							&nbsp;<a href="#" onclick="javascript:mlt_refreshTracksOverview();" >					
								<img id="refreshIcon" src="img/led/arrow_refresh.png" style="border: none; margin-left: 0px;"/>
							</a>&nbsp;														
						</div>						
					</td>
				</tr>
				<tr style="padding: 0px;border-spacing: 0px;">
					<c:choose>
						<c:when test="${tracksOverviewCmd.selectedTracksView eq 'Map'}">
							<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;">
								<c:set var="selectedTracksOverviewOptFlyToMode">${tracksOverviewCmd.selectedTracksOverviewOptFlyToMode}</c:set>
								<select id="selectedTracksOverviewOptFlyToMode" name="selectedTracksOverviewOptFlyToMode" 
									style="border-style: groove; border-width: 2px;" 
									onchange="javascript:refreshTrackOverview(null);"> 
									<c:forEach var="optMapFlyToMode" items="${tracksOverviewCmd.tracksOverviewOptsFlyToMode}">
					 						<c:choose>
					   						<c:when test="${optMapFlyToMode.value == selectedTracksOverviewOptFlyToMode}">
										    	<option value="${optMapFlyToMode.value}" selected>
										        	<spring:message code="${optMapFlyToMode.label}" />
										      	</option>
										    </c:when>
									    	<c:otherwise>
									      		<option value="${optMapFlyToMode.value}" >
										        	<spring:message code="${optMapFlyToMode.label}" />
										      	</option>
										    </c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
					<td style="padding: 0px;border-spacing: 0px;white-space: nowrap;border: none;text-align: left;">
						<c:set var="selectedTracksOverviewOptRefresh">${tracksOverviewCmd.selectedTracksOverviewOptRefresh}</c:set>
						<select id="selectedTracksOverviewOptRefresh" name="selectedTracksOverviewOptRefresh"
							style="border-style: groove; border-width: 2px;" 
							onchange="javascript:refreshTrackOverview(null);" > 
							<c:forEach var="optRefresh" items="${tracksOverviewCmd.tracksOverviewOptsRefresh}">
			 						<c:choose>
			   						<c:when test="${optRefresh.value == selectedTracksOverviewOptRefresh}">
								    	<option value="${optRefresh.value}" selected>
								        	<spring:message code="${optRefresh.label}" />
								      	</option>
								    </c:when>
							    	<c:otherwise>
							      		<option value="${optRefresh.value}" >
								        	<spring:message code="${optRefresh.label}" />
								      	</option>
								    </c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>		
<div id="divTracks" >
	<c:choose>
		<c:when test="${tracksOverviewCmd.selectedTracksView eq 'Table'}">
			<tiles:insertAttribute name="tracks-as-table" />
		</c:when>
		<c:otherwise>
			<tiles:insertAttribute name="tracks-as-map" />
		</c:otherwise>
	</c:choose>
</div>
</form:form>
