<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<script src="<c:url value='/js/map.commons.js'/>"></script>

<script type="text/javascript"> 
	mlt_getOnlyActiveTracks = false;	 
	var mlt_tableTracks = null;
			
	function mlt_renderTracksOverview(mlt_data) {
		if (mlt_tableTracks != null) {
			mlt_tableTracks.fnClearTable();
		}
		mlt_renderTrackTable(mlt_data.jsonCommons);
		for (var i=0; i < mlt_data.tracks.length; i++) {
			mlt_renderTrackTableRow(i, mlt_data.tracks[i], i==mlt_data.tracks.length-1);
   		}			
	}
	
	function mlt_renderTrackTable(commons) {
		var msg = '<spring:message code="overview.track.table.info" />';
		if (commons.maxCountOfRecordsExceeded) {
			msg = '<spring:message code="overview.track.table.info.toomuch" />';
			msg = msg.replace(/\{0\}/g, commons.countFoundTracks);
			msg = msg.replace(/\{1\}/g, commons.countDisplayedTracks);
		}
		var sInfoTxt = commons.statusUpdated + "&nbsp;&#8226;&nbsp;" + msg;
		var sInfoEmptyTxt = commons.statusUpdated + "&nbsp;&#8226;&nbsp;" + "<spring:message code="overview.track.table.info.empty" />";
		
		mlt_tableTracks = $('#tableTracks').dataTable( {
			"aoColumns": [
					{ "bSortable": false, "bSearchable": false },
					{ "bSortable": false, "bSearchable": false },
					{ "bSortable": false, "bSearchable": false },
					{ "bSortable": false, "bSearchable": false }						
				],
			"bDestroy": true,	
			"bFilter" : false,	
			"bSort" : false,	
			"bAutoWidth": false,	
			"bJQueryUI": true,
			"sScrollY": "75%",							
			"bPaginate": false,		
			"oLanguage": {
				"sSearch": "&nbsp;<spring:message code="overview.track.table.search" />&nbsp;",
				"sEmptyTable": "&nbsp;<spring:message code="overview.track.table.empty" />&nbsp;",
				"sInfo": "&nbsp;" + sInfoTxt + "&nbsp;",
				"sInfoEmpty": "&nbsp;" + sInfoEmptyTxt + "&nbsp;",
				"sInfoFiltered": "",
				"sZeroRecords": "&nbsp;<spring:message code="overview.track.table.zero.records" />&nbsp;"					
			}
		});
	}
	
	function mlt_renderTrackTableRow(idx, track, refresh) {
		var row = $('#trackTableRow').clone(true);
		var className = (track.active ? "active" : "");
		row.attr("class", className)
		$("#googleMapsButton", row).attr("onclick", "javascript:startTracking('RedirectToTrackAsMapCtrl', '" + track.trackId + "');");
		$("#removeTrackButton", row).attr("onclick", "javascript:removeTrack('" + track.trackId + "','" + track.name + "');");
		$("#googleEarthButton", row).attr("onclick", "javascript:startTracking('RedirectToTrackAsGoogleEarthCtrl', '" + track.trackId + "');");
		//$("#", row).attr("onclick", "");
		$("#resetTrackButton", row).attr("onclick", "javascript:resetTrack('" + track.trackId + "','" + track.name + "');");
		$("#kmlExportButton", row).attr("onclick", "javascript:startTracking('RedirectToTrackAsKmlFileCtrl', '" + track.trackId + "');");
		$("#gpxExportButton", row).attr("onclick", "javascript:startTracking('RedirectToTrackAsGpxFileCtrl', '" + track.trackId + "');");
		if (!track.canBeActivated) {
			$("#resetTrackDiv", row).remove();
		}
		$("#senderNameAbbr", row).html("&nbsp;" + track.senderName + "&nbsp;");
		$("#senderInfo", row).attr("title", "Sender Id: " + track.senderId);
		if (track.phoneNumber) {
			$("#phoneNumber", row).html("&nbsp;" + track.phoneNumber + "&nbsp;");
		} else {
			$("#phoneNumber", row).html("");
		}
		if (!track.active) {
			$("#trackIsActive", row).html("&nbsp;&nbsp;");
		}
		$("#trackNames", row).attr("value", track.nameNotAbbr);
		if (!track.editable) {
			$("#trackNames", row).attr("readonly", "readonly");
		}
		$("#trackNames", row).attr("onkeypress", "javascript:renameTrack('" + track.trackId + "');");
		$("#trackNames", row).attr("name", "trackNames["+ track.trackId + "]");
		$("#trackNames", row).attr("id", "trackNames"+ track.trackId);
		if (!track.recEmSi) {
			$("#recentEmergencySigalIsActive", row).html("&nbsp;&nbsp;");
		}
		if (!track.canBeActivated) {
			$("#trackActivityStati", row).attr("disabled", "disabled");
		}
		$("#trackActivityStati", row).attr("onchange", "javascript:editTrack('UpdateTrackActivityStatus','" + track.trackId + "');");
		$("#trackActivityStati", row).attr("name", "trackActivityStati[" + track.trackId+ "]");
		$("#trackActivityStati", row).attr("id", "trackActivityStati" + track.trackId);
		$("#optActivityStatus_" + track.active, row).attr("selected", "true");
		if (!track.editable) {
			$("#trackReleaseStati", row).attr("disabled", "disabled");
		}
		$("#trackReleaseStati", row).attr("onchange", "javascript:editTrack('UpdateTrackReleaseStatus','" + track.trackId + "');");
		$("#trackReleaseStati", row).attr("name", "trackReleaseStati[" + track.trackId+ "]");
		$("#trackReleaseStati", row).attr("id", "trackReleaseStati" + track.trackId);
		$("#optReleaseStatus_" + track.released, row).attr("selected", "true");
		$("#timestampTrackCreated", row).html("&nbsp;" + track.created + "&nbsp;");
		$("#timestampLastHeartbeatSent", row).html("&nbsp;" + track.lastHeartbeatSent + "&nbsp;");
		$("#timestampFirstPositionReceived", row).html("&nbsp;" + track.firstPosRcvd + "&nbsp;");
		$("#timestampTrackUpdated", row).html("&nbsp;" + track.updated + "&nbsp;");
		$("#cntPositions", row).html("&nbsp;" + track.cntPos + "&nbsp;");
		if (track.cntPos == 0) {
			$("#cntPositions", row).css("color", "grey");
		} else if (track.recPos) {
			$("#timestampRecPosition", row).html("&nbsp;" + track.recPosUpd + "&nbsp;");
			$("#infoRecPosition", row).html("&nbsp;" + track.recPos.infoPosition + "&nbsp;");
		}
		$("#cntMobNwCells", row).html("&nbsp;" + track.cntMobNwCells + "&nbsp;");
		if (track.cntMobNwCells == 0) {
			$("#cntMobNwCells", row).css("color", "grey");
		} else if (track.recMobNwCell) {
			$("#timestampRecMobNwCell", row).html("&nbsp;" + track.recMobNwCell.rcvd + "&nbsp;");
			$("#infoRecMobNwCell", row).html("&nbsp;" + track.recMobNwCell.cells + "&nbsp;");
		}
		$("#cntMessages", row).html("&nbsp;" + track.cntMsg + "&nbsp;");
		if (track.cntMsg == 0) {
			$("#cntMessages", row).css("color", "grey");
		} else if (track.recMsg) {
			$("#cntMessages", row).css("color", mlt_infoTableHdrColor4Message);
			$("#timestampRecMessage", row).html("&nbsp;" + track.recMsg.rcvd + "&nbsp;");
			$("#timestampRecMessage", row).css("color", mlt_infoTableHdrColor4Message);
			$("#infoRecMessage", row).html("&nbsp;" + track.recMsg.msg + "&nbsp;");
			$("#infoRecMessage", row).css("color", mlt_infoTableHdrColor4Message);
		}
		
		$("#cntCardiacFunctions", row).html("&nbsp;" + track.cntCardiacFunctions + "&nbsp;");
		if (track.cntCardiacFunctions == 0) {
			$("#cntCardiacFunctions", row).css("color", "grey");
		} else if (track.recCardiacFunction) {
			$("#timestampRecCardiacFunction", row).html("&nbsp;" + track.recCardiacFunction.rcvd + "&nbsp;");
			$("#infoRecCardiacFunction", row).html("&nbsp;" + track.recCardiacFunction.info + "&nbsp;");
		}
			
		if (!track.recEmSi) {
			$("#rowEmergencySignal", row).remove();
		} else {
			$("#cntEmergencySignals", row).html("&nbsp;" + track.cntEmSi + "&nbsp;");
			if (track.cntEmSi == 0) {
				$("#cntEmergencySignals", row).css("color", "grey");
			} else if (track.recEmSi) {
				var color = (track.recEmSi.active ? 
					mlt_infoTableHdrColor4EmergencySignalActivated : 
					mlt_infoTableHdrColor4EmergencySignalDeactivated)
				$("#cntEmergencySignals", row).css("color", color);
				$("#timestampRecEmergencySignal", row).html("&nbsp;" + track.recEmSi.rcvd + "&nbsp;");
				$("#timestampRecEmergencySignal", row).css("color", color);
				var msg = (track.recEmSi.active ? 
					"<spring:message code='overview.emergency.signal.true.received' />" :
					"<spring:message code='overview.emergency.signal.false.received' />");
				if (track.recEmSi.hasMsg) {
					msg += ":&nbsp" + track.recEmSi.msg; 
				}
				$("#recEmergencySignalMessage", row).html("&nbsp;" + msg + "&nbsp;");
				$("#recEmergencySignalMessage", row).css("color", color);
				
				var sms = "";
				if (track.recEmSi.smssent) {
					sms = "<spring:message code='sms.transport.success' />";
				} else {
					<c:choose>
						<c:when test="${!currentUser.emergency.smsUnlocked}" >
							sms = "<spring:message code='sms.transport.not.active' />"
						</c:when>
						<c:when test="${!currentUser.emergency.smsEnabled}" >
							sms = "<spring:message code='sms.transport.not.active' />";
						</c:when>
						<c:otherwise>
							sms = "<spring:message code='sms.transport.failed' />";
						</c:otherwise>
					</c:choose>
				}
				$("#recEmergencySignalSmsStatus", row).html("&nbsp;" + sms + "&nbsp;");
				$("#recEmergencySignalSmsStatus", row).css("color", color);
			}
		}
	
		mlt_tableTracks.fnAddTr(row[0], refresh);
	}		
</script> 
		<div style="visibility: hidden;height:0px;"> 		
		<table>
			<tr id="trackTableRow" class="trackRow">
				<td  class="trackRowCol">
					<table>
						<tr>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<div class="mlt-button">		
									<a id="googleMapsButton" href="#" title="<spring:message code="overview.track.table.title.map" />" 
										onclick="alert('call startTracking()');"
										style="width:30px">								
										<img src="img/led/map.png" style="border: none; margin-left: -7px;"/>
									</a>
								</div> 
							</td>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<security:authorize ifAnyGranted="Admin,User">
								<div class="mlt-button">
									<a id="removeTrackButton" href="#" title="<spring:message code="overview.track.table.title.remove" />" 
										onclick="alert('call removeTrack()');"
										style="width:30px">
										<img src="img/led/cross.png" style="border: none; margin-left: -7px;"/>												
									</a>
								</div>
								</security:authorize>	
							</td>	
						</tr>
						<tr>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<div class="mlt-button">		
									<a id="googleEarthButton" href="#"  title="<spring:message code="overview.track.table.title.earth" />"
										onclick="alert('call startTracking()');"
										style="width:30px">
										<img src="img/led/world.png" style="border: none; margin-left: -7px;"/>
									</a>															 
								</div>
							</td>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<div id="resetTrackDiv" class="mlt-button">
									<a id="resetTrackButton" href="#" title="<spring:message code="overview.track.table.title.reset" />" 
										onclick="alert('call resetTrack()');"
										style="width:30px;">
										<img src="img/led/arrow_undo.png" style="border: none; margin-left: -7px;"/>
									</a>
								</div>	
							</td>
						</tr>
						<tr>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<div class="mlt-button">		
									<a id="kmlExportButton" href="#"  title="<spring:message code="overview.track.table.title.export.kml" />"
										onclick="alert('call startTracking()');"
										style="width:30px">
										<font style="color:rgb(115,154,58);font-size: xx-small;margin-left: -7px;font-weight: bold;">kml</font>
									</a>															 
								</div>
							</td>
							<td style="text-align: center; border: none; white-space: nowrap;">
								<div class="mlt-button">		
									<a id="gpxExportButton" href="#"  title="<spring:message code="overview.track.table.title.export.gpx" />"
										onclick="alert('call startTracking()');"
										style="width:30px">
										<font style="color:rgb(115,154,58);font-size: xx-small;margin-left: -7px;font-weight: bold;">gpx</font>
									</a>															 
								</div>	
							</td>
						</tr>
					</table>
				</td>
				<td class="trackRowCol">
					<table style="border-spacing: 0px;">
						<tr>
							<td style="width:20px;height:20px;white-space: nowrap; border: none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.sender" />" 
									src="img/led/mobile_phone.png" style="border: none;" />&nbsp;
							</td>				
							<td id="senderInfo" title="Sender Id: SenderId"
								style="white-space: nowrap; border: none;">
								<table style="border-spacing: 0px;">
									<tr><td id="senderNameAbbr" style="white-space: nowrap; border: none;">&nbsp;senderNameAbbr&nbsp;</td></tr>
									<tr><td id="phoneNumber" style="white-space: nowrap; border: none;">&nbsp;phoneNumber&nbsp;</td></tr>
								</table>
							</td>									
						</tr>								
						<tr>
							<td id="trackIsActive" style="width:20px;height:20px;white-space: nowrap; border: none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.active" />" 
									src="img/glossbasic/bullet_red.png" style="border: none;" />&nbsp;
							</td>
							<td style="white-space: nowrap; border: none;">
								<spring:message code="overview.track.table.title.track.name" var="titleTrackName" />
								<input
									id="trackNames"
									name="trackNames"												
									title="<spring:message code='overview.track.table.title.track.name' />"	
									value="trackName"			
									onkeypress="alert('call renameTrack()');"									
									style="width:95%;border-style: groove; border-width: 2px;vertical-align: middle;"							 
								 />
							</td>
						</tr>
						<tr>								
							<td id="recentEmergencySigalIsActive" style="width:20px;height:20px;white-space: nowrap; border: none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" title="<spring:message code="overview.track.table.title.emergency" />" 
									src="img/nuvola/messagebox_warning.png" style="border: none;" />&nbsp;
							</td>
							<td style="border: none; white-space: nowrap;">
								<select id='trackActivityStati' 
									name='trackActivityStati'
									title="<spring:message code="overview.track.table.title.status" />" 
									onchange="alert('call editTrack()');"
									style="width:90px; border-style: groove; border-width: 2px;"
								> 
									<c:forEach var="optActivityStatus" items="${tracksOverviewCmd.trackOptsActivityStatus}">											
			 				      		<option id="optActivityStatus_${optActivityStatus.value}" value="${optActivityStatus.value}" >										      		
								        	<spring:message code="${optActivityStatus.label}" />
								      	</option>
									</c:forEach>
								</select> 																	
								<select id='trackReleaseStati' 
									name='trackReleaseStati'
									title="<spring:message code="overview.track.table.title.release" />" 
									onchange="alert('call editTrack()');"
									style="width:90px; border-style: groove; border-width: 2px;"
								> 
									<c:forEach var="optReleaseStatus" items="${tracksOverviewCmd.trackOptsReleaseStatus}">											
							      		<option id="optReleaseStatus_${optReleaseStatus.value}" value="${optReleaseStatus.value}" >										      		
								        	<spring:message code="${optReleaseStatus.label}" />
								      	</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</table>						
				</td>
				<td class="trackRowCol">
					<table style="border-spacing: 0px;">															
						<tr>
							<td style="width:20px;height:20px;white-space: nowrap; border-top: none; border-left: none; border-bottom: none;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
									title="<spring:message code="overview.track.table.title.timestamp.track.created" />" 
									src="img/led/asterisk_orange.png" style="border: none;" />&nbsp;
							</td>																																										
							<td id="timestampTrackCreated" style="text-align:center; white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>								
						<tr>
							<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.track.connected" />" 
								src="img/led/connect.png" style="border: none;" />&nbsp;
							</td>																											
							<td id="timestampLastHeartbeatSent" style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.first.position.received" />" 
								src="img/led/arrow_right.png" style="border: none;" />&nbsp;
							</td>																		
							<td id="timestampFirstPositionReceived" style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>								
						<tr>
							<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;" 
								title="<spring:message code="overview.track.table.title.timestamp.track.updated" />"
								src="img/led/arrow_refresh.png" style="border: none;" />&nbsp;
							</td>																											
							<td id="timestampTrackUpdated" style="text-align:center; white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>								
					</table>
				</td>
				<td class="trackRowCol">
					<table style="border-spacing: 0px;">
						<tr>
							<td style="width:20px;height:20px;white-space: nowrap; border-top: none; border-left: none; border-bottom: none;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.current.position" />" 
								src="img/farmfresh/satellite_dish.png" style="border: none;" />&nbsp;
							</td>
							<td id="cntPositions" style="width:40px;text-align: right;white-space: nowrap; border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="timestampRecPosition" style="width:140px;text-align:center;white-space: nowrap;border-top: none; border-bottom: none;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="infoRecPosition" style="text-align:left;white-space: nowrap; border-top: none; border-right: none; border-bottom: none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.current.mobnwcell" />" 
								src="img/crystalclear/signal_4.png" style="border: none;" />&nbsp;
							</td>
							<td id="cntMobNwCells" style="text-align: right; white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>   
							<td id="timestampRecMobNwCell" style="text-align:center; white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="infoRecMobNwCell" style="text-align:left;white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td style="height:20px;white-space: nowrap; border-left: none; border-bottom: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.current.message" />" 
								src="img/led/email.png" style="border: none;" />&nbsp;
							</td>									
							<td id="cntMessages" style="text-align: right; white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="timestampRecMessage" style="text-align:center;white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>											
							<td id="infoRecMessage" style="text-align:left;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>								
						<tr id="rowCardiacFunction">
							<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.current.cardiacfunction" />" 
								src="img/led/heart_empty.png" style="border: none;" />&nbsp;
							</td>
							<td id="cntCardiacFunctions" style="text-align: right;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="timestampRecCardiacFunction" style="text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="infoRecCardiacFunction" style="white-space: nowrap; border-right: none; border-bottom: none; border-top-style: dotted;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
						</tr>
						<tr id="rowEmergencySignal">		
							<td style="height:20px;white-space: nowrap;  border-bottom: none; border-left: none; border-top-style: dotted;border-right:none;">
								&nbsp;<img style="height:16px;width:16px;vertical-align: middle;"
								title="<spring:message code="overview.track.table.title.timestamp.current.emergencysignal" />" 
								src="img/nuvola/messagebox_warning.png" style="border: none;" />&nbsp;
							</td>									
							<td id="cntEmergencySignals" style="text-align: right;white-space: nowrap; border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="timestampRecEmergencySignal" style="text-align:center;white-space: nowrap;border-bottom: none; border-top-style: dotted;border-right:none;border-left-style: dotted;">
								&nbsp;&nbsp;
							</td>
							<td id="infoRecEmergencySignal" style="white-space: nowrap; border-bottom: none; border-right: none; border-top-style: dotted;border-left-style: dotted;">
								<table>
									<tr><td id="recEmergencySignalMessage" style="border:none;"></td></tr>
									<tr><td id="recEmergencySignalSmsStatus" style="border:none;"></td></tr>
								</table>									
							</td>	
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</div>
		
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
		</table> 				
		