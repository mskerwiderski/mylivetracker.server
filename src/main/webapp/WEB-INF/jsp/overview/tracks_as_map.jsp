<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet-0.7.7.css'/>" />
<link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet.iconlabel.css'/>" />
<link rel="stylesheet" href="<c:url value='/style/map-and-controls.css'/>" />
<script type="text/javascript" src="<c:url value='/js/leaflet/leaflet-0.7.7.min.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.iconlabel.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.fullscreen-and-symbols.controls.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.providers-1.1.14.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.gpx-24.06.2016.js'/>"></script>
<script src="<c:url value='/js/map.commons.js'/>"></script>

<script type="text/javascript">
	mlt_getOnlyActiveTracks = true;
	
	//the map object.
	var mlt_map = null;
	var mlt_DEFAULT_CENTER = new L.LatLng(48.1391265, 11.5801863);
	var mlt_DEFAULT_ZOOM = 3;

	var mlt_markers = null;
	var mlt_markersVisible = true;
	var mlt_circleMarkers = null;
	var mlt_bounds = null;
	var mlt_bounds_empty = true;
	var mlt_current_sender_pos = null;
	var mlt_adaptZoomLevelIsFirstCall = true;
	
	//process response callback.
	function mlt_renderTracksOverview(mlt_data) {
		mlt_resetBoundsAndMarkers();
		for (var i=0; i < mlt_data.tracks.length; i++) {
			if (mlt_data.tracks[i].cntPos > 0) {
				var recEmSiIsActive = false;
				if (mlt_data.tracks[i].cntEmSi > 0) {
					recEmSiIsActive = mlt_data.tracks[i].recEmSi.active;
				}
				var info = mlt_infoTable(
					mlt_data.tracks[i].trackId, 
					mlt_data.tracks[i].name, 
					mlt_data.tracks[i].recPos.rcvd, 
					mlt_data.tracks[i].recPos.infoPosition, 
					mlt_data.tracks[i].cntMsg > 0, 
					mlt_data.tracks[i].cntEmSi > 0, 
					recEmSiIsActive);
				var marker = mlt_createAndSetSenderMarker(
					mlt_data.tracks[i].senderName,
					mlt_data.tracks[i].senderSymbolId,
					mlt_data.tracks[i].recPos.lat,
					mlt_data.tracks[i].recPos.lon,
					mlt_data.tracks[i].recPos.infoPosition,
					mlt_data.tracks[i].recPos.rcvd,
					mlt_data.tracks[i].trackId,
					mlt_data.tracks[i].name,
					mlt_data.tracks[i].cntMsg > 0,
					mlt_data.tracks[i].cntEmSi > 0,
					recEmSiIsActive,
					info);
				mlt_markers.push(marker);
				if (mlt_markersVisible) {
					mlt_map.addLayer(marker);	
				}
				mlt_addCircleMarkerToMarkersAndMap(
					mlt_circleMarkers, mlt_map,
					mlt_data.tracks[i].recPos.lat, 
					mlt_data.tracks[i].recPos.lon,
					mlt_RECENT_POS, true,
					info);
				var pos = new L.LatLng(
   					mlt_data.tracks[i].recPos.lat,
   					mlt_data.tracks[i].recPos.lon);
				mlt_bounds.extend(pos);		
	   			mlt_bounds_empty = false;
	   			if (i == 0) {
	   				mlt_current_sender_pos = pos;
	   			}
			}	
   		}
		var infoTxt = '<spring:message code="overview.track.map.no.active.tracks.found" />';
		if (mlt_data.tracks.length == 1) {
			infoTxt = '<spring:message code="overview.track.map.one.track.found" />';
		} else if (mlt_data.tracks.length > 1) {
			infoTxt = '<spring:message code="overview.track.map.tracks.found" />';
			infoTxt = infoTxt.replace(/\{0\}/g, mlt_data.tracks.length);
		}
		infoTxt = "&nbsp;" + mlt_data.jsonCommons.statusUpdated + "&nbsp;&#8226;&nbsp;" + infoTxt;
		$("#infoTxt").html(infoTxt);	
		mlt_adaptZoomLevel();
    }        
	function mlt_removeMarkersFromLayer() {
		if (mlt_markers != null) {
			for (var i=0; i < mlt_markers.length; i++) {
			   	mlt_map.removeLayer(mlt_markers[i]);				   	
		   	}
		}
	}
	function mlt_removeCircleMarkersFromLayer() {
		if (mlt_circleMarkers != null) {
			for (var i=0; i < mlt_circleMarkers.length; i++) {
			   	mlt_map.removeLayer(mlt_circleMarkers[i]);				   	
		   	}
		}
	}
	function mlt_putMarkersToLayer() {
		if (mlt_markers != null) {
			for (var i=0; i < mlt_markers.length; i++) {
			   	mlt_map.addLayer(mlt_markers[i]);				   	
		   	}
		}
	}
	function mlt_resetBoundsAndMarkers() {
		mlt_removeMarkersFromLayer();
		mlt_removeCircleMarkersFromLayer();
 		mlt_bounds = new L.LatLngBounds();
 		mlt_current_sender_pos = null;
		mlt_bounds_empty = true;
		mlt_markers = [];
		mlt_circleMarkers = [];
 	}
	
	function mlt_adaptZoomLevel() {
		var flyToMode = "<c:out value='${tracksOverviewCmd.selectedTracksOverviewOptFlyToMode}' />";
		if (flyToMode == "None") { 
			// zoom mode is off.
			if (mlt_adaptZoomLevelIsFirstCall) {
				mlt_zoomModeAllSenders();
			}
		} else if (flyToMode == "FlyToView") {
			// zoom mode is 'all senders'.
			mlt_zoomModeAllSenders();
		} else if (flyToMode == "FlyToCurrentSender") {
			// zoom mode is 'current sender'.
			mlt_zoomModeCurrentSender();
		} else {
			// unknown zoom mode.
			mlt_zoomModeAllSenders();
		}
		
		mlt_adaptZoomLevelIsFirstCall = false;
	}
	
	function mlt_zoomModeAllSenders() {
		var center = mlt_DEFAULT_CENTER;
		var zoom = mlt_DEFAULT_ZOOM;
		if (!mlt_bounds_empty) {
			center = mlt_bounds.getCenter();
	 		zoom = mlt_map.getBoundsZoom(mlt_bounds, false);
	 		if (zoom > 0) { zoom--; }
		}
	 	mlt_map.setView(center, zoom);
	}
	
	function mlt_zoomModeCurrentSender() {
		var center = mlt_DEFAULT_CENTER;
		var zoom = mlt_DEFAULT_ZOOM;
		if (mlt_current_sender_pos != null) {
			var bounds = new L.LatLngBounds();
			bounds.extend(mlt_current_sender_pos);
			center = bounds.getCenter();
	 		zoom = mlt_map.getBoundsZoom(bounds, false);
	 		if (zoom > 0) { zoom--; }
		}
	 	mlt_map.setView(center, zoom);
	}
</script> 
<table style="width:100%;">
	<tr>
		<td>
			<div id="divMapCanvas"></div>
		</td>
	</tr>
	<tr style="height:10px;">
		<td style="border:none;">
			<table>
				<tr>			
					<td id="infoTxt" 
						style="font-size: xx-small;text-align: left;vertical-align: middle;white-space: nowrap">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<script>
	$(document).ready(
		function initMap() {
			$(window).resize(function() {
				$(divMapCanvas).css("height", calcTableAndMapHeight(192));
				mlt_map.invalidateSize(true);
			});
			var mlt_supportedLayerNames = [
           		<c:forEach var="supportedMap" items="${tracksOverviewCmd.supportedMaps}">
           	    	"<spring:message code='${supportedMap.label}' />",
           		</c:forEach>
           	];
           	mlt_map = mlt_initMap("divMapCanvas",
           		"<c:out value='${tracksOverviewCmd.mapsUsedStr}' />",
           		"<c:out value='${tracksOverviewCmd.defMapId}' />",
           		mlt_DEFAULT_CENTER, mlt_DEFAULT_ZOOM,
           		mlt_supportedLayerNames);
           	<c:forEach var="routeUsed" items="${tracksOverviewCmd.routesUsedArr}">
				new L.GPX('<c:out value="${routeUsed}"/>', {async: true}).on('loaded', function(e) {
			  		mlt_map.fitBounds(e.target.getBounds());
				}).addTo(mlt_map);
			</c:forEach>
           	$(window).resize();
	   	}
    );
	
	function mlt_createAndSetSenderMarker(name, symbolId, lat, lon, posStr, rcvd, 
		trackId, trackName, hasMsg, hasEmSi, recEmSiIsActive, info) {
		var SenderIcon = L.Icon.Label.extend({
			options: {
		    	iconUrl: "<c:url value='img/map/'/>" + symbolId + ".png",
		    	iconSize: new L.Point(32, 37),
		    	iconAnchor: new L.Point(-16, -79),
		    	labelAnchor: new L.Point(16, -76),
		    	wrapperAnchor: new L.Point(0, -37),
		    	popupAnchor: new L.Point(1, -40),
			}
		});
		var senderNameColor = mlt_infoTableHdrColor4RecentPosition;
		if (hasEmSi) {
			if (recEmSiIsActive) {
				senderNameColor = mlt_infoTableHdrColor4EmergencySignalActivated;
			} else {
				senderNameColor = mlt_infoTableHdrColor4EmergencySignalDeactivated;
			}
		} else if (hasMsg) {
			senderNameColor = mlt_infoTableHdrColor4Message;
		}
		name = "<b style='color:"+ senderNameColor + ";'>" + name + "</b>";
	   	var marker = new L.Marker(
    	new L.LatLng(lat, lon), 
			{ icon: new SenderIcon({ labelText: name }), 
    		clickable:true, opacity: 0.8, });
	   	
	   	marker.bindPopup(info);
    	return marker; 
	}
	function mlt_infoTable(trackId, trackName, timestamp, location, hasMsg, hasEmSi, recEmSiIsActive) {
		var titleColor = mlt_infoTableHdrColor4RecentPosition;
		if (hasEmSi) {
			if (recEmSiIsActive) {
				titleColor = mlt_infoTableHdrColor4EmergencySignalActivated;
			} else {
				titleColor = mlt_infoTableHdrColor4EmergencySignalDeactivated;
			}
		} else if (hasMsg) {
			titleColor = mlt_infoTableHdrColor4Message;
		}
		var href = "javascript:startTracking('RedirectToTrackAsMapCtrl', '" + trackId + "');";
		var anker = "<a href=\"#\" onclick=\"" + href + "\"><img src='img/led/map.png' style='border: none;'/></a>";
		var res = "<table><tr><td colspan='2'><table><tr>";
		res += "<td style='border: none;width:16px;'>" + anker + "</td>";
		res += "<td style='color:#FFFFFF;background-color:" + titleColor + ";white-space: nowrap;'>&nbsp;<b>" + trackName + "</b>&nbsp;</td>";
		res += "</tr></table></td></tr>";
		if (timestamp != null) {
			res += "<tr style='font-size: x-small;'>";
			res += "<td>&nbsp;<b><spring:message code='track.map.info.window.info.timestamp' /></b>&nbsp;</td>";
			res += "<td>&nbsp;" + timestamp + "&nbsp;</td>";
			res += "</tr>";
		}
		if (location != null) {
			res += "<tr style='font-size: x-small;'>";
			res += "<td>&nbsp;<b><spring:message code='track.map.info.window.info.location' /></b>&nbsp;</td>";
			res += "<td>&nbsp;" + location + "&nbsp;</td>";
			res += "</tr>";
		}
		res += "</table>";
		return res;
	}
</script>