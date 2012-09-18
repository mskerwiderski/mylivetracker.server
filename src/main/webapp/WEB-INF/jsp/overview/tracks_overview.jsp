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
	function refreshTrackOverview(tracksView) {
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = "RefreshTrackOverview";
		if (tracksView != null) {
			document.forms["tracksOverviewForm"].elements["tracksView"].value = tracksView;
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
			$("#divLiveTrackingOpts").show();				
		} else {
			$("#divLiveTrackingOpts").hide();			
		}
	}	
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
	});
	
	var mlt_getOnlyActiveTracks = false;
	
	function mlt_refreshTracksOverview() {
   		var senderId = document.forms["tracksOverviewForm"].elements["selectedSenderFilter"].value;
   		var dateFrom = document.forms["tracksOverviewForm"].elements["selectedDateFromFilter"].value;
   		var dateTo = document.forms["tracksOverviewForm"].elements["selectedDateToFilter"].value;
   		var searchStr = document.forms["tracksOverviewForm"].elements["selectedSearchStrFilter"].value;
		$.getJSON('tracks_list.do', {
			senderId:senderId,
			dateFrom:dateFrom,
			dateTo:dateTo,
			searchStr:searchStr,
			onlyActive:mlt_getOnlyActiveTracks},
			mlt_renderTracksOverview
		);
		$("#loadDataMarker").hide();
   	}
	
	$(document).ready(
		function mlt_processRequest() {
			mlt_refreshTracksOverview();
	   	}
    );
</script> 

<script>
var autoRefreshTimer = null;
function startAutoRefresh() {
	autoRefreshTimer = document.forms["tracksOverviewForm"].elements["selectedTracksOverviewOptsRefresh"].value;
	autoRefresh();
}
function autoRefresh() {			
	if (autoRefreshTimer == 0) {
		mlt_refreshTracksOverview();
		startAutoRefresh();
	} else if (autoRefreshTimer == -1) { 
		setTimeout("autoRefresh()", 1000);
	} else {
		if (autoRefreshTimer == 2) {
			$("#loadDataMarker").show();
		}
		autoRefreshTimer--;
		setTimeout("autoRefresh()", 1000);
	}
}
window.onload=startAutoRefresh;
</script>

<div id="removeTrackDialog" style="display: none">
</div>

<div id="resetTrackDialog" style="display: none">
</div>

<form:form id="tracksOverviewForm" name="tracksOverviewForm" 
	commandName="tracksOverviewCmd">

	<form:hidden path="actionExecutor"/>
	<form:hidden path="tracksView"/>
	<form:hidden path="selectedTrackId"/>	
	<form:hidden path="selectedTrackName"/>
	<form:hidden path="selectedTrackActivityStatus"/>
	<form:hidden path="selectedTrackReleaseStatus"/>
	
<table> 
	<tr>
		<security:authorize ifAnyGranted="Admin,User">
		<c:if test="${!empty tracksOverviewCmd.senderEntries}">
		<td style="width:150px;white-space: nowrap;">
			<table>
			<tr><td style="white-space: nowrap;border:none;">
			<form:select									 
				path="selectedSenderEntryIdx" 				 
				multiple="false"
				cssStyle="border-style: groove; border-width: 2px;"
				>
				<c:forEach var="senderEntry" 
					items="${tracksOverviewCmd.senderEntries}"
					varStatus="status">
    				<form:option value="${status.index}"  
    					label="${senderEntry.label}"/>
  				</c:forEach>
			</form:select>
			</td>
			<td style="white-space: nowrap;border:none;">			
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:createTrack();">
					<spring:message code='overview.create.track.button' />
				</a>&nbsp;														
			</div>	
			</td></tr>
			</table>							
		</td>
		</c:if>
		</security:authorize>
		<td>
		<table>
		<tr>
		<td style="white-space: nowrap;border:none;">
			&nbsp;<spring:message code="overview.livetracking" />&nbsp;
			<c:set var="selectedLiveTrackingOpt">${tracksOverviewCmd.selectedLiveTrackingOpt}</c:set>
			<select id="selectedLiveTrackingOpt" name="selectedLiveTrackingOpt" 
				style="border-style: groove; border-width: 2px;"
				onchange="javascript:switchLiveTrackingOnOff();"> 
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
		<td id="divLiveTrackingOpts" style="width:100%; white-space: nowrap;border:none;">
			&nbsp;<spring:message code="overview.livetracking.keep.positions" />&nbsp;		
			<c:set var="selectedLiveTrackingOptKeepRecentPos">${tracksOverviewCmd.selectedLiveTrackingOptKeepRecentPos}</c:set>		
			<select id="selectedLiveTrackingOptKeepRecentPos" name="selectedLiveTrackingOptKeepRecentPos"
				style="border-style: groove; border-width: 2px;" > 
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
			&nbsp;<spring:message code="overview.livetracking.updateinterval" />&nbsp;
			<c:set var="selectedLiveTrackingOptUpdateInterval">${tracksOverviewCmd.selectedLiveTrackingOptUpdateInterval}</c:set>	
			<select id="selectedLiveTrackingOptUpdateInterval" name="selectedLiveTrackingOptUpdateInterval"
				style="border-style: groove; border-width: 2px;" > 
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
			&nbsp;<spring:message code="overview.livetracking.flytomode" />&nbsp;
			<c:set var="selectedLiveTrackingOptFlyToMode">${tracksOverviewCmd.selectedLiveTrackingOptFlyToMode}</c:set>
			<select id="selectedLiveTrackingOptFlyToMode" name="selectedLiveTrackingOptFlyToMode" 
				style="border-style: groove; border-width: 2px;" > 
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
	</tr>
</table>
<table>
	<tr>	
		<td style="height:35px;white-space: nowrap;width:100%;">
			<table><tr><td style="border:none;">
			<c:set var="selectedSenderFilter">${tracksOverviewCmd.selectedSenderFilter}</c:set>		
			<select id="selectedSenderFilter" name="selectedSenderFilter" 
				onchange="javascript:refreshTrackOverview(null);"
				style="border-style: groove; border-width: 2px;" > 
					<c:forEach var="optSenderFilter" items="${tracksOverviewCmd.senderFilterOptions}">											
					    	<c:choose>
	   						<c:when test="${optSenderFilter.value eq selectedSenderFilter}">
						    	<option value="${optSenderFilter.value}" selected>
						        	<spring:message 
						        		code="${optSenderFilter.label}" 
						        		text="${optSenderFilter.label}" 
						        	/>
						      	</option>
						    </c:when>
					    	<c:otherwise>
					      		<option value="${optSenderFilter.value}" >										      		
						        	<spring:message 
						        		code="${optSenderFilter.label}" 
						        		text="${optSenderFilter.label}" 
						        	/>
						      	</option>
						    </c:otherwise>
						</c:choose>									
					</c:forEach>
				</select>	
			&nbsp;<spring:message code="overview.label.period.from" />&nbsp;						
			<form:input cssStyle="width:85px; text-align:center; border-style: groove; border-width: 2px;" 
				onchange="javascript:refreshTrackOverview(null);"
				path="selectedDateFromFilter"/>
			&nbsp;<spring:message code="overview.label.period.to" />&nbsp;
			<form:input cssStyle="width:85px; text-align:center; border-style: groove; border-width: 2px;"
				onchange="javascript:refreshTrackOverview(null);"
				path="selectedDateToFilter"/>								
			<script>			 					
				$.datepicker.setDefaults(<spring:message code="datepicker.i18n" />);									
				$('#selectedDateFromFilter').datepicker({ dateFormat: 'dd.mm.yy' });
				$('#selectedDateToFilter').datepicker({ dateFormat: 'dd.mm.yy' });				
			</script>						
			&nbsp;<spring:message code="overview.label.track.name.search" />&nbsp;
			<spring:message code="overview.track.table.title.track.name.search" var="titleTrackNameSearch" />
			<form:input cssStyle="width:200px; text-align:left; border-style: groove; border-width: 2px;"
				onkeypress="javascript:checkSearchEnterPressed();"
				path="selectedSearchStrFilter"
				title="${titleTrackNameSearch}" />
			<img src="img/remove.png" onclick="javascript:resetSearchStr();" 
				style="border: none;margin-bottom: -3px;"/>	
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
			</td></tr></table>			
		</td> 
		<td style="text-align: right;">
		<table><tr style="text-align: right;">
		<td style="white-space: nowrap;border:none;text-align: right;">	
			<div class="mlt-button">
				<c:choose>
					<c:when test="${tracksOverviewCmd.tracksView eq 'Table'}">
						&nbsp;<a href="#" onclick="javascript:refreshTrackOverview('Map');">					
							<img src="img/tracks_overview_symbols/map.png" style="border: none;"/>
						</a>&nbsp;
					</c:when>
					<c:otherwise>
						&nbsp;<a href="#" onclick="javascript:refreshTrackOverview('Table');">					
							<img src="img/tracks_overview_symbols/table.png" style="border: none;"/>
						</a>&nbsp;
					</c:otherwise>
				</c:choose>
			</div>						
		</td>		
		<td style="white-space: nowrap;border:none;text-align: right;">
			&nbsp;<spring:message code="overview.refresh.interval" />&nbsp;
			<c:set var="selectedTracksOverviewOptsRefresh">${tracksOverviewCmd.selectedTracksOverviewOptsRefresh}</c:set>	
			<select id="selectedTracksOverviewOptsRefresh" name="selectedTracksOverviewOptsRefresh"
				style="border-style: groove; border-width: 2px;" 
				onchange="javascript:refreshTrackOverview(null);" > 
				<c:forEach var="optRefresh" items="${tracksOverviewCmd.tracksOverviewOptsRefresh}">
 						<c:choose>
   						<c:when test="${optRefresh.value == selectedTracksOverviewOptsRefresh}">
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
			</select>&nbsp;
		</td>
		<td style="white-space: nowrap;border:none;text-align: center;">	
			<img id="loadDataMarker" src="img/tracks_overview_symbols/bullet_red.png" style="margin-left:-10px;margin-right:-7px;border: none;vertical-align: middle;"/>
		</td>
		<td style="white-space: nowrap;border:none;text-align: right;">	
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:mlt_refreshTracksOverview();">					
					<img id="refreshIcon" src="img/tracks_overview_symbols/refresh.png" style="border: none;"/>
				</a>&nbsp;														
			</div>						
		</td>		
		</tr></table>
		</td>							
	</tr>		
</table>
<hr/>	
	<div id="divTracks" >
		<c:choose>
			<c:when test="${tracksOverviewCmd.tracksView eq 'Table'}">
				<tiles:insertAttribute name="tracks-as-table" />
			</c:when>
			<c:otherwise>
				<tiles:insertAttribute name="tracks-as-map" />
			</c:otherwise>
		</c:choose>
	</div>
</form:form>
