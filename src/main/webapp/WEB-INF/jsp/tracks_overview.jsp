<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	function setSelectedTrack(selectedTrackEntryIdx, selectedTrackId) {
		document.forms["tracksOverviewForm"].elements["selectedTrackId"].value = selectedTrackId;
		document.forms["tracksOverviewForm"].elements["selectedTrackEntryIdx"].value = selectedTrackEntryIdx;
	}
	function refreshTrackOverview() {
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = "RefreshTrackOverview";
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();
	}
	function editTrack(actionExecutor, selectedTrackId, selectedTrackEntryIdx) {	
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = actionExecutor;		
		document.forms["tracksOverviewForm"].elements["selectedTrackId"].value = selectedTrackId;
		document.forms["tracksOverviewForm"].elements["selectedTrackEntryIdx"].value = selectedTrackEntryIdx;
		var trackName =
			document.forms["tracksOverviewForm"].elements["trackNames[" + selectedTrackEntryIdx + "]"].value;
		var trackActivityStatus =
			document.forms["tracksOverviewForm"].elements["trackActivityStati[" + selectedTrackEntryIdx + "]"].value;
		var trackReleaseStatus =
			document.forms["tracksOverviewForm"].elements["trackReleaseStati[" + selectedTrackEntryIdx + "]"].value;
		document.forms["tracksOverviewForm"].elements["selectedTrackName"].value = trackName;
		document.forms["tracksOverviewForm"].elements["selectedTrackActivityStatus"].value = trackActivityStatus;
		document.forms["tracksOverviewForm"].elements["selectedTrackReleaseStatus"].value = trackReleaseStatus;
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();		
	}
	function renameTrack(selectedTrackId, selectedTrackEntryIdx) {		
		var keyCode=event.keyCode;
	  	if(keyCode==13) {
	  		editTrack("RenameTrack", selectedTrackId, selectedTrackEntryIdx);
	  	}
	}
	function createTrack() {
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = "CreateTrack";		
		document.forms["tracksOverviewForm"].method = "post";
		document.forms["tracksOverviewForm"].submit();		 
	}
	function startTracking(actionExecutor, selectedTrackEntryIdx) {		
		document.forms["tracksOverviewForm"].elements["actionExecutor"].value = actionExecutor;
		document.forms["tracksOverviewForm"].elements["selectedTrackEntryIdx"].value = selectedTrackEntryIdx;
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
	function removeTrack(trackId, trackName, trackIdx) {
		var textTempl = '<spring:message code="overview.track.remove.confirm.text" />';
		var text = textTempl.replace(/\{0\}/g, trackName);			
		$confirmRemoveTrackDlg.html(text);	
		$confirmRemoveTrackDlg.dialog('option', 'trackId', trackId);
		$confirmRemoveTrackDlg.dialog('option', 'trackIdx', trackIdx);
		$confirmRemoveTrackDlg.dialog('open');
	}
	var $confirmRemoveTrackDlg;
	function resetTrack(trackId, trackName, trackIdx) {
		var textTempl = '<spring:message code="overview.track.reset.confirm.text" />';
		var text = textTempl.replace(/\{0\}/g, trackName);			
		$confirmResetTrackDlg.html(text);	
		$confirmResetTrackDlg.dialog('option', 'trackId', trackId);
		$confirmResetTrackDlg.dialog('option', 'trackIdx', trackIdx);
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
							$(this).dialog('option', 'trackId'), 
							$(this).dialog('option', 'trackIdx'));
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
						$(this).dialog('option', 'trackId'), 
						$(this).dialog('option', 'trackIdx'));
					$(this).dialog('close');
				}										
			}					
		});						
	});	
</script> 

<script>
var autoRefreshTimer = null;
function setAutoRefresh() {
	autoRefreshTimer = document.forms["tracksOverviewForm"].elements["selectedTracksOverviewOptsRefresh"].value;
	refreshTrackOverview();
}

function startAutoRefresh() {
	autoRefreshTimer = document.forms["tracksOverviewForm"].elements["selectedTracksOverviewOptsRefresh"].value;
	autoRefresh();
}

function autoRefresh() {			
	if (autoRefreshTimer == 1) {
		refreshTrackOverview();
	}
	else if (autoRefreshTimer == -1) { 
		setTimeout("autoRefresh()", 1000);
	} else {	
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
	<form:hidden path="selectedTrackId"/>	
	<form:hidden path="selectedTrackEntryIdx"/>
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
				onchange="javascript:refreshTrackOverview();"
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
				onchange="javascript:refreshTrackOverview();"
				path="selectedDateFromFilter"/>
			&nbsp;<spring:message code="overview.label.period.to" />&nbsp;
			<form:input cssStyle="width:85px; text-align:center; border-style: groove; border-width: 2px;"
				onchange="javascript:refreshTrackOverview();"
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
					refreshTrackOverview();
				}
				function checkSearchEnterPressed(e) {
				  	var keyCode=(e)? e.which :event.keyCode;
				  	if(keyCode==13) {
				  		refreshTrackOverview();
				  	}
				}
			</script>		
			</td></tr></table>			
		</td> 
		<td style="text-align: right;">
		<table><tr style="text-align: right;">		
		<td style="white-space: nowrap;border:none;text-align: right;">
			&nbsp;<spring:message code="overview.refresh.interval" />&nbsp;
			<c:set var="selectedTracksOverviewOptsRefresh">${tracksOverviewCmd.selectedTracksOverviewOptsRefresh}</c:set>	
			<select id="selectedTracksOverviewOptsRefresh" name="selectedTracksOverviewOptsRefresh"
				style="border-style: groove; border-width: 2px;" 
				onchange="javascript:setAutoRefresh();" > 
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
		<td style="white-space: nowrap;border:none;text-align: right;">	
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:refreshTrackOverview();">					
					<img src="img/arrow_refresh.png" style="border: none;"/>
				</a>&nbsp;														
			</div>						
		</td>		
		</tr></table>
		</td>							
	</tr>		
</table>
<hr/>	
	<script type="text/javascript"> 		
		$(document).ready(function() {
			var tableTracks = $('#tableTracks').dataTable( {
				"aoColumns": [
						{ "bSortable": false, "bSearchable": false },
						{ "bSortable": false, "bSearchable": false },
						{ "bSortable": false, "bSearchable": false },
						{ "bSortable": false, "bSearchable": false }						
					],
				"bFilter" : false,	
				"bSort" : false,	
				"bAutoWidth": false,	
				"bJQueryUI": true,
				"sScrollY": "75%",							
				"bPaginate": false,		
				"oLanguage": {
					"sSearch": "&nbsp;<spring:message code="overview.track.table.search" />&nbsp;",
					"sEmptyTable": "&nbsp;<spring:message code="overview.track.table.empty" />&nbsp;",
					"sInfo":
					<c:choose>
						<c:when test="${tracksOverviewCmd.maxCountOfRecordsExceeded}" >
						"&nbsp;<spring:message code="overview.track.table.info.toomuch" 
							arguments="${tracksOverviewCmd.countFoundTracks},${tracksOverviewCmd.countDisplayedTracks}"/>&nbsp;"
						</c:when>
						<c:otherwise>
						"&nbsp;<spring:message code="overview.track.table.info" />&nbsp;"
						</c:otherwise>
					</c:choose>	
					,
					"sInfoEmpty": "&nbsp;<spring:message code="overview.track.table.info.empty" />&nbsp;",
					"sInfoFiltered": "",
					"sZeroRecords": "&nbsp;<spring:message code="overview.track.table.zero.records" />&nbsp;"					
				}
			} );			
		});		
	</script> 
	<div id="divTracks" >
		<table style="border:0px;border-collapse:collapse; border-spacing:0px;" 
			class="display" id="tableTracks"> 
			<thead> 
				<tr>
					<th style="font-weight: bold;white-space: nowrap;">&nbsp;</th> 
					<th style="font-weight: bold;white-space: nowrap;">&nbsp;<spring:message code="overview.table.header.status" />&nbsp;</th>
					<th style="font-weight: bold;white-space: nowrap;">&nbsp;<spring:message code="overview.table.header.timestamps" />&nbsp;</th>
					<th style="font-weight: bold;white-space: nowrap;">&nbsp;<spring:message code="overview.table.header.current" />&nbsp;</th>														
				</tr> 				
			</thead>
			<tbody>				
				<c:forEach items="${tracksOverviewCmd.trackEntries}" var="trackEntry" varStatus="trackStatus" >			
					<c:choose>
						<c:when test="${trackEntry.track.active}">							
							<c:set var="class" value="active" />
						</c:when>
						<c:otherwise>	
							<c:set var="class" value="" />																						
						</c:otherwise>
					</c:choose>						
					<tr class="<c:out value='${class}' />">										
						<td>
							<table>
								<tr>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<div class="mlt-button">		
											<a id="googleMapsButton" href="#" title="<spring:message code="overview.track.table.title.maps" />" 
												onclick="javascript:startTracking('RedirectToTrackAsGoogleMapsCtrl', '<c:out value="${trackStatus.index}"  />');"
												style="width:30px">								
												<img src="img/map.png" style="border: none; margin-left: -5px;"/>
											</a>
										</div> 
									</td>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<security:authorize ifAnyGranted="Admin,User">
										<div class="mlt-button">
											<a id="removeTrackButton" href="#" title="<spring:message code="overview.track.table.title.remove" />" 
												onclick="javascript:removeTrack(
												'<c:out value="${trackEntry.track.trackId}"  />',
												'<c:out value="${trackEntry.track.name}"  />', 
												'<c:out value="${trackStatus.index}" />');"
												style="width:30px">
												<img src="img/cross.png" style="border: none; margin-left: -5px;"/>												
											</a>
										</div>
										</security:authorize>	
									</td>	
								</tr>
								<tr>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<div class="mlt-button">		
											<a id="googleEarthButton" href="#"  title="<spring:message code="overview.track.table.title.earth" />"
												onclick="javascript:startTracking('RedirectToTrackAsGoogleEarthCtrl', '<c:out value="${trackStatus.index}"  />');"
												style="width:30px">
												<img src="img/world.png" style="border: none; margin-left: -5px;"/>
											</a>															 
										</div>
									</td>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<c:if test="${trackEntry.canBeActivated}" >
										<div class="mlt-button">
											<a id="resetTrackButton" href="#" title="<spring:message code="overview.track.table.title.reset" />" 
												onclick="javascript:resetTrack(
												'<c:out value="${trackEntry.track.trackId}"  />',
												'<c:out value="${trackEntry.track.name}"  />', 
												'<c:out value="${trackStatus.index}" />');"
												style="width:30px;">
												<img src="img/arrow_undo.png" style="border: none; margin-left: -5px;"/>
											</a>
										</div>	
										</c:if>
									</td>
								</tr>
								<tr>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<div class="mlt-button">		
											<a id="kmlExportButton" href="#"  title="<spring:message code="overview.track.table.title.export.kml" />"
												onclick="javascript:startTracking('RedirectToTrackAsKmlFileCtrl', '<c:out value="${trackStatus.index}"  />');"
												style="width:30px">
												<font style="color:rgb(115,154,58);font-size: xx-small;margin-left: -5px;font-weight: bold;">kml</font>
											</a>															 
										</div>
									</td>
									<td style="text-align: center; border: none; white-space: nowrap;">
										<div class="mlt-button">		
											<a id="kmlExportButton" href="#"  title="<spring:message code="overview.track.table.title.export.gpx" />"
												onclick="javascript:startTracking('RedirectToTrackAsGpxFileCtrl', '<c:out value="${trackStatus.index}"  />');"
												style="width:30px">
												<font style="color:rgb(115,154,58);font-size: xx-small;margin-left: -5px;font-weight: bold;">gpx</font>
											</a>															 
										</div>	
									</td>
								</tr>
							</table>
						</td>
						<td>
							<table style="border-spacing: 0px;">
								<tr>
									<td style="width:20px;height:20px;white-space: nowrap; border: none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.sender" />" 
											src="img/mobile_phone.png" style="border: none;" />&nbsp;
									</td>																																										
									<td title="Sender Id: <c:out value="${trackEntry.track.senderId}" />"
										style="white-space: nowrap; border: none;">
										<table style="border-spacing: 0px;">
											<tr><td style="white-space: nowrap; border: none;">&nbsp;<c:out value="${trackEntry.senderNameAbbreviated}" />&nbsp;</td></tr>
											<c:if test="${!empty trackEntry.track.phoneNumber}">
												<tr><td style="white-space: nowrap; border: none;">&nbsp;<c:out value="${trackEntry.track.phoneNumber}" />&nbsp;</td></tr>
											</c:if>				
										</table>
									</td>									
								</tr>								
								<tr>
									<c:choose>
										<c:when test="${trackEntry.track.active}">
											<td style="width:20px;height:20px;white-space: nowrap; border: none;">
												&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.active" />" 
													src="img/active_track.png" style="border: none;" />&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="width:20px;height:20px;white-space: nowrap; border: none;">
												&nbsp;&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
									<td style="white-space: nowrap; border: none;">
										<spring:message code="overview.track.table.title.track.name" var="titleTrackName" />
										<form:input												
											title="${titleTrackName}"				
											readonly="${!trackEntry.editable}"											
											onkeypress="javascript:renameTrack('${trackEntry.track.trackId}','${trackStatus.index}');"									
											cssStyle="width:95%;border-style: groove; border-width: 2px;vertical-align: middle;"							 
											path="trackNames[${trackStatus.index}]" />
									</td>
								</tr>
								<tr>								
									<c:choose>
										<c:when test="${!empty trackEntry.track.recentEmergencySignal}">
											<td style="width:20px;height:20px;white-space: nowrap; border: none;">
												&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.emergency" />" 
													src="img/alert.png" style="border: none;" />&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="width:20px;height:20px;white-space: nowrap; border: none;">
												&nbsp;&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
									<td style="border: none; white-space: nowrap;">
										<select id='trackActivityStati<c:out value="${trackStatus.index}"/>' 
											name='trackActivityStati[<c:out value="${trackStatus.index}"/>]'
											title="<spring:message code="overview.track.table.title.status" />" 
											<c:if test="${!trackEntry.canBeActivated}">
												disabled="disabled"											
											</c:if> 
											onchange="javascript:editTrack('UpdateTrackActivityStatus','${trackEntry.track.trackId}','${trackStatus.index}');"
											style="width:90px; border-style: groove; border-width: 2px;"
										> 
											<c:forEach var="optActivityStatus" items="${tracksOverviewCmd.trackOptsActivityStatus}">											
							 					<c:choose>
							   						<c:when test="${optActivityStatus.value eq tracksOverviewCmd.trackActivityStati[trackStatus.index]}">
												    	<option value="${optActivityStatus.value}" selected>
												        	<spring:message code="${optActivityStatus.label}" />
												      	</option>
												    </c:when>
											    	<c:otherwise>
											      		<option value="${optActivityStatus.value}" >										      		
												        	<spring:message code="${optActivityStatus.label}" />
												      	</option>
												    </c:otherwise>
												</c:choose>
											</c:forEach>
										</select> 																	
										<select id='trackReleaseStati<c:out value="${trackStatus.index}"/>' 
											name='trackReleaseStati[<c:out value="${trackStatus.index}"/>]'
											title="<spring:message code="overview.track.table.title.release" />" 
											<c:if test="${!trackEntry.editable}">
												disabled="disabled"											
											</c:if>
											onchange="javascript:editTrack('UpdateTrackReleaseStatus','${trackEntry.track.trackId}','${trackStatus.index}');"
											style="width:90px; border-style: groove; border-width: 2px;"
										> 
											<c:forEach var="optReleaseStatus" items="${tracksOverviewCmd.trackOptsReleaseStatus}">											
							 					<c:choose>
							   						<c:when test="${optReleaseStatus.value eq tracksOverviewCmd.trackReleaseStati[trackStatus.index]}">
												    	<option value="${optReleaseStatus.value}" selected>
												        	<spring:message code="${optReleaseStatus.label}" />
												      	</option>
												    </c:when>
											    	<c:otherwise>
											      		<option value="${optReleaseStatus.value}" >										      		
												        	<spring:message code="${optReleaseStatus.label}" />
												      	</option>
												    </c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</td>
								</tr>
							</table>						
						</td>
						<td>
							<table style="border-spacing: 0px;">															
								<tr>
									<td style="width:20px;height:20px;white-space: nowrap; border-top: none; border-left: none; border-bottom: none;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
											title="<spring:message code="overview.track.table.title.timestamp.track.created" />" 
											src="img/asterisk_orange.png" style="border: none;" />&nbsp;
									</td>																																										
									<c:choose>
										<c:when test="${!empty trackEntry.track.timestamps.trackCreated}">
											<td style="text-align:center; white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.timestamps.trackCreated}" />&nbsp;
											</td>	
										</c:when>
										<c:otherwise>
											<td style="text-align:center; white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
										</c:otherwise>
									</c:choose>							
								</tr>								
								<tr>
									<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
										title="<spring:message code="overview.track.table.title.timestamp.track.connected" />" 
										src="img/connect.png" style="border: none;" />&nbsp;
									</td>																											
									<c:choose>
										<c:when test="${empty trackEntry.track.timestamps.lastHeartbeatSent}">
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.timestamps.lastHeartbeatSent}" />&nbsp;
											</td>	
										</c:otherwise>
									</c:choose>
								</tr>
								<tr>
									<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
										title="<spring:message code="overview.track.table.title.timestamp.first.position.received" />" 
										src="img/arrow_right.png" style="border: none;" />&nbsp;
									</td>																		
									<c:choose>
										<c:when test="${!empty trackEntry.track.timestamps.firstPositionReceived}">
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.timestamps.firstPositionReceived}" />&nbsp;
											</td>	
										</c:when>
										<c:otherwise>
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
								</tr>								
								<tr>
									<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" 
										title="<spring:message code="overview.track.table.title.timestamp.track.updated" />"
										src="img/arrow_refresh.png" style="border: none;" />&nbsp;
									</td>																											
									<c:choose>
										<c:when test="${!empty trackEntry.track.timestamps.trackUpdated}">
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.timestamps.trackUpdated}" />&nbsp;
											</td>	
										</c:when>
										<c:otherwise>
											<td style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
								</tr>								
							</table>
						</td>
						<td>
							<table style="border-spacing: 0px;">
								<tr>
									<td style="width:20px;height:20px;white-space: nowrap; border-top: none; border-left: none; border-bottom: none;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
										title="<spring:message code="overview.track.table.title.timestamp.current.position" />" 
										src="img/satellite.png" style="border: none;" />&nbsp;
									</td>
									<c:choose>
										<c:when test="${trackEntry.track.countPositions gt 0}">
											<td style="width:30px;text-align: right;white-space: nowrap; border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
											&nbsp;<c:out value="${trackEntry.track.countPositions}" />&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="width:30px;text-align: right; color:gray;white-space: nowrap; border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
											&nbsp;0&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty trackEntry.track.recentPosition.timeReceived}">
											<td style="width:150px;text-align:center;color:gray;white-space: nowrap;border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
											<td style="white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
												&nbsp;&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="width:150px;text-align:center; white-space: nowrap;border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.recentPosition.timeReceived}" />&nbsp;
											</td>
											<td style="white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
												&nbsp;<c:out value="${trackEntry.recPosStr}" />&nbsp;
											</td>	
										</c:otherwise>
									</c:choose>																																			
								</tr>
								<tr>
									<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
										title="<spring:message code="overview.track.table.title.timestamp.current.mobnwcell" />" 
										src="img/wireless_signal.png" style="border: none;" />&nbsp;
									</td>
									<c:choose>
										<c:when test="${trackEntry.track.countMobNwCells gt 0}">
											<td style="text-align: right; white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;<c:out value="${trackEntry.track.countMobNwCells}" />&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="text-align: right; color:gray;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;0&nbsp;
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty trackEntry.track.recentMobNwCell.timeReceived}">
											<td style="text-align:center;color:gray; white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
											<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
												&nbsp;&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.recentMobNwCell.timeReceived}" />&nbsp;
											</td>
											<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
												&nbsp;<c:out value="${trackEntry.recMobNwCellStr}" />&nbsp;
											</td>	
										</c:otherwise>
									</c:choose>									
								</tr>
								<tr>
									<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
										&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
										title="<spring:message code="overview.track.table.title.timestamp.current.message" />" 
										src="img/email.png" style="border: none;" />&nbsp;
									</td>									
									<c:choose>
										<c:when test="${trackEntry.track.countMessages gt 0}">
											<td style="text-align: right;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;<c:out value="${trackEntry.track.countMessages}" />&nbsp;
											</td>
										</c:when>
										<c:otherwise>
											<td style="text-align: right; color:gray;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;0&nbsp;
											</td>
										</c:otherwise>										
									</c:choose>
									<c:choose>
										<c:when test="${empty trackEntry.track.recentMessage.timeReceived}">
											<td style="text-align:center;color:gray;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;&nbsp;
											</td>
											<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
												&nbsp;&nbsp;
											</td>											
										</c:when>
										<c:otherwise>
											<td style="text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
											&nbsp;<mlt:dateTime dateTime="${trackEntry.track.recentMessage.timeReceived}" />&nbsp;
											</td>
											<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
												&nbsp;<c:out value="${trackEntry.recMessageStr}" />&nbsp;
											</td>	
										</c:otherwise>
									</c:choose>
								</tr>								
								<tr>
									<c:choose>
										<c:when test="${empty trackEntry.track.recentEmergencySignal}">
											<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
												&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
												title="<spring:message code="overview.track.table.title.timestamp.current.cardiacfunction" />" 
												src="img/heart_empty.png" style="border: none;" />&nbsp;
											</td>
											<c:choose>
												<c:when test="${trackEntry.track.countCardiacFunctions gt 0}">
													<td style="text-align: right;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;<c:out value="${trackEntry.track.countCardiacFunctions}" />&nbsp;
													</td>
												</c:when>
												<c:otherwise>
													<td style="text-align: right; color:gray;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;0&nbsp;
													</td>
												</c:otherwise>										
											</c:choose>
											<c:choose>
												<c:when test="${empty trackEntry.track.recentCardiacFunction.timeReceived}">
													<td style="text-align:center;color:gray;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
														&nbsp;&nbsp;
													</td>
													<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
														&nbsp;&nbsp;
													</td>											
												</c:when>
												<c:otherwise>
													<td style="text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;<mlt:dateTime dateTime="${trackEntry.track.recentCardiacFunction.timeReceived}" />&nbsp;
													</td>
													<td style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
														&nbsp;<c:out value="${trackEntry.recCardiacFunctionStr}" />&nbsp;
													</td>	
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>										
											<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
												&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
												title="<spring:message code="overview.track.table.title.timestamp.current.emergencysignal" />" 
												src="img/alert.png" style="border: none;" />&nbsp;
											</td>									
											<c:choose>
												<c:when test="${trackEntry.track.countEmergencySignals gt 0}">
													<td style="color:red;text-align: right;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;<c:out value="${trackEntry.track.countEmergencySignals}" />&nbsp;
													</td>
												</c:when>
												<c:otherwise>
													<td style="color:red;text-align: right; color:gray;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;0&nbsp;
													</td>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${empty trackEntry.track.recentEmergencySignal.timeReceived}">
													<td style="color:red;text-align:center;color:gray; white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
														&nbsp;&nbsp;
													</td>
													<td style="color:red;white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
														&nbsp;&nbsp;
													</td>
												</c:when>
												<c:otherwise>
													<td style="color:red;text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
													&nbsp;<mlt:dateTime dateTime="${trackEntry.track.recentEmergencySignal.timeReceived}" />&nbsp;
													</td>
													<td style="white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
														<table>
															<tr><td style="color:red;border:none;font-weight: bold;">
															<c:choose>	
																<c:when test="${trackEntry.track.recentEmergencySignal.active}">
																	&nbsp;<spring:message code="overview.emergency.signal.true.received" />
																	<c:if test="${!empty trackEntry.track.recentEmergencySignal.message}">
																		:&nbsp;<c:out value="${trackEntry.track.recentEmergencySignal.message}" />
																	</c:if>&nbsp;
																</c:when>
																<c:otherwise>
																	&nbsp;<spring:message code="overview.emergency.signal.false.received" />
																	<c:if test="${!empty trackEntry.track.recentEmergencySignal.message}">
																		:&nbsp;<c:out value="${trackEntry.track.recentEmergencySignal.message}" />
																	</c:if>&nbsp;
																</c:otherwise>									
															</c:choose>
															</td></tr>
															<tr><td style="color:red;border:none;">
															<c:choose>
																<c:when test="${trackEntry.track.recentEmergencySignal.smsSuccessfullySent}" >
																	&nbsp;<spring:message code="sms.transport.success" />&nbsp;
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${!currentUser.emergency.smsUnlocked}" >
																			&nbsp;<spring:message code="sms.transport.not.active" />&nbsp;
																		</c:when>
																		<c:when test="${!currentUser.emergency.smsEnabled}" >
																			&nbsp;<spring:message code="sms.transport.not.active" />&nbsp;
																		</c:when>
																		<c:otherwise>
																			&nbsp;<spring:message code="sms.transport.failed" />&nbsp;
																		</c:otherwise>
																	</c:choose>
																</c:otherwise>
															</c:choose>
															</td></tr>
														</table>
													</td>	
												</c:otherwise>
											</c:choose>			
										</c:otherwise>
									</c:choose>								
								</tr>
							</table>
						</td>																	
					</tr>									
				</c:forEach>				
			</tbody>				
		</table> 				
	</div>
</form:form>
