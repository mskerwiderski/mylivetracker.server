<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page import="de.msk.mylivetracker.web.util.WebUtils" %>

<link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet-0.4.4.css'/>" />
<!--[if lte IE 8]>
     <link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet.ie-0.4.4.css'/>" />
<![endif]-->
<link rel="stylesheet" href="<c:url value='/style/map-and-controls.css'/>" />
<script type="text/javascript" src="<c:url value='/js/leaflet/leaflet-0.4.4.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.fullscreen-and-symbols.controls.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.providers-0.0.1.js'/>"></script>
<script src="<c:url value='/js/map.commons.js'/>"></script>

<style>
.modal {
    display:    none;
    position:   fixed;
    z-index:    1000;
    top:        0;
    left:       0;
    height:     100%;
    width:      100%;
    background: rgba( 255, 255, 255, .8 ) 
                url(./img/others/loader.gif) 
                50% 50% 
                no-repeat;
}
body.loading {
    overflow: hidden;   
}
body.loading .modal {
    display: block;
}
</style>

<div style="font-size: small;background-color: "></div>
<script type="text/javascript">
	var loadingWindow = true;
	function showLoadingWindow() {
		$('body').addClass("loading"); 
	}
	function hideLoadingWindow() {
		if (!loadingWindow) return; 
		$('body').removeClass("loading");
		loadingWindow = false;
	}
	function mlt_infoTable(title, titleColor, timestamp, location, message) {
		var res = "<table>";
		res += "<tr><td style='color:" + mlt_infoTableHdrFontColor + ";background-color:" + titleColor + "' colspan='2'>&nbsp;<b>" + title + "</b>&nbsp;</td></tr>";
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
		if (message != null) {
			res += "<tr style='font-size: x-small;'>";
			res += "<td>&nbsp;<b><spring:message code='track.map.info.window.info.message' /></b>&nbsp;</td>";
			res += "<td>&nbsp;" + message + "&nbsp;</td>";
			res += "</tr>";
		}
		res += "</table>";
		return res;
	}
	
	function mlt_PosAndInfo(latlng, rcvd, infoTitle) {		
		this.latlng = latlng;	  	
	  	this.rcvd = rcvd;	  		 
	  	this.infoTitle = infoTitle;
	}
	mlt_PosAndInfo.prototype.info = function(title, titleColor) {
		var timestamp = "<spring:message code='track.map.info.window.info.timestamp.null' />";
		if (this.rcvd != null) {
			timestamp = this.rcvd;
		} 
		return mlt_infoTable(title, titleColor, timestamp, this.infoTitle, null); 
	}
	mlt_PosAndInfo.prototype.infoWOTimestamp = function(title, titleColor) {
		return mlt_infoTable(title, titleColor, null, this.infoTitle, null); 
	}
	
	function mlt_createMsgMarkerId(idStr) {
		return "msg-icon-" + idStr;
	}
	function mlt_createMsgCircleMarkerId(idStr) {
		return "msg-cmark-" + idStr;
	}
	function mlt_createEmSiMarkerId(idStr) {
		return "emsi-icon-" + idStr;
	}
	function mlt_createEmSiCircleMarkerId(idStr) {
		return "emsi-cmark-" + idStr;
	}
	
	//current request id. 
	var mlt_reqId = "<c:out value='${reqId}'/>";

	// the map object.
	var mlt_map = null;
	var mlt_DEFAULT_CENTER = new L.LatLng(48.1391265, 11.5801863);
	var mlt_DEFAULT_ZOOM = 3;
	
	// json data object.
	var mlt_data = null;
	
	// possible values of mlt_comeFrom:
	// o null  --> last view was not a home location nor a track.
	// o home  --> last view was a home location.
	// o track --> last view was a track.
	var mlt_lastView = null;

	// last track id and version (is != null if mlt_lastView = "track").
	var mlt_lastTrackId = null;
	var mlt_lastTrackVersionMajor = null;
	
	// current positions, bounds and markers.
	var mlt_positions = null;
	var mlt_posAndInfo = null;
	var mlt_bounds = null;
	var mlt_bounds_empty = true;
	var mlt_markers = null;	
	var mlt_markersVisible = true;
	var mlt_circleMarkers = null;
	
	var mlt_startMarker = null;
	var mlt_recentMarker = null;
	var mlt_homeMarker = null;
	var mlt_startCircleMarker = null;
	var mlt_recentCircleMarker = null;
	var mlt_homeCircleMarker = null;
	
	var mlt_lastMessageCircleMarkerId = null;
	var mlt_lastMessageCircleMarker = null;
	var mlt_lastEmergencySignalCircleMarkerId = null;
	var mlt_lastEmergencySignalCircleMarker = null;
	
	// the polyline.
	var mlt_polyline = null;

	// update view state.
	function mlt_updateViewState() {		
		if (mlt_data.track != null) {
			mlt_lastView = "track";
			mlt_lastTrackId = mlt_data.track.trackId;
			mlt_lastTrackVersionMajor = mlt_data.track.versionMajor;
		} else if (mlt_data.user.hasHome) {
			mlt_lastView = "home";			
			mlt_lastTrackId = null;
			mlt_lastTrackVersionMajor = null;
		} else {
			mlt_lastView = null;
			mlt_lastTrackId = null;
			mlt_lastTrackVersionMajor = null;
		}
	}
	
	function mlt_removeMarkersFromLayer() {
		if (mlt_markers != null) {
			for (var i in mlt_markers) {
				mlt_map.removeLayer(mlt_markers[i]);
			}
		}
		if (mlt_startMarker != null) {
			mlt_map.removeLayer(mlt_startMarker);
		}
		if (mlt_recentMarker != null) {
			mlt_map.removeLayer(mlt_recentMarker);
		}
		if (mlt_homeMarker != null) {
			mlt_map.removeLayer(mlt_homeMarker);
		}
	}
	function mlt_removeCircleMarkersFromLayer() {
		if (mlt_circleMarkers != null) {
			for (var i in mlt_markers) {
				mlt_map.removeLayer(mlt_markers[i]);
			}
		}
	}
	function mlt_putMarkersToLayer() {
		if (!mlt_markersVisible) return;
		for (var i in mlt_markers) {
			mlt_map.addLayer(mlt_markers[i]);
		}
		if (mlt_startMarker != null) {
			mlt_map.addLayer(mlt_startMarker);
		}
		if (mlt_recentMarker != null) {
			mlt_map.addLayer(mlt_recentMarker);
		}
		if (mlt_homeMarker != null) {
			mlt_map.addLayer(mlt_homeMarker);
		}
	}
	function mlt_putCircleMarkersToLayer() {
		for (var i in mlt_circleMarkers) {
			mlt_map.addLayer(mlt_circleMarkers[i]);
		}
	}
	function mlt_clearAllLayers() {
		if (mlt_polyline != null) {
			mlt_map.removeLayer(mlt_polyline);
		}
		if (mlt_startMarker != null) {
			mlt_map.removeLayer(mlt_startMarker);
		}
		if (mlt_startCircleMarker != null) {
			mlt_map.removeLayer(mlt_startCircleMarker);
		}
		if (mlt_recentMarker != null) {
			mlt_map.removeLayer(mlt_recentMarker);
		}
		if (mlt_recentCircleMarker != null) {
			mlt_map.removeLayer(mlt_recentCircleMarker);
		}
		if (mlt_homeMarker != null) {
			mlt_map.removeLayer(mlt_homeMarker);
		}
		if (mlt_homeCircleMarker != null) {
			mlt_map.removeLayer(mlt_homeCircleMarker);
		}
		mlt_removeMarkersFromLayer();
		mlt_removeCircleMarkersFromLayer();
	}
	
	function mlt_drawPolylineAndMarkers(reset) {
		mlt_clearAllLayers();
		if ((mlt_positions != null) && (mlt_positions.length > 0)) {
	   		mlt_polyline = 
	   			L.polyline(mlt_positions, {
	   				color: "#" + mlt_data.user.routeColor,
	   				weight: mlt_data.user.routeWidth,
	   				opacity: 0.7,
	   			}).addTo(mlt_map);
	   	} else {
		   	mlt_polyline = null;
	   	}
		
	   	if (mlt_data.track != null) {
	   		var cntPos = mlt_positions.length;
   			// add startMarker only if there is minimum 1 position.
   			// exception: there is only one position and this position has a message or a emergency signal.	
   			if (cntPos > 1) {
   				var displayTrackComplete = mlt_data.track.cntPos == cntPos;
   				var info = null;
   				if (displayTrackComplete) {
   					info = mlt_posAndInfo[0].info(
   	   					"<spring:message code='track.map.info.window.title.startPosition' />",
   	   					mlt_infoTableHdrColor4StartPosition);
   					mlt_startMarker =
   		   				mlt_createMarkerWithInfoWindow(
   		    	   			mlt_startIcon, 
   		    	   			mlt_positions[0].lat, 
   		    	   			mlt_positions[0].lng,
   		    	   			info);
   				} else {
   					info = mlt_posAndInfo[0].info(
 	   	   				"<spring:message code='track.map.info.window.title.firstOfLastRecentPositions' />",
 	   	   				mlt_infoTableHdrColor4FirstOfLastRecentPositions);
   					mlt_startMarker =
   		   				mlt_createMarkerWithInfoWindow(
   		    	   			mlt_firstIcon, 
   		    	   			mlt_positions[0].lat, 
   		    	   			mlt_positions[0].lng,
   		    	   			info);
   				}
		   		if (mlt_markersVisible) {
		   			mlt_map.addLayer(mlt_startMarker);
		   		}
		   		mlt_startCircleMarker = mlt_addCircleMarkerToMap(
		   			mlt_map,
	   				mlt_positions[0].lat, 
	   				mlt_positions[0].lng,
	   				mlt_START_POS,
	   				false,
	   				info);
	   		}
		   	if ((cntPos > 0) && 
		   		!mlt_data.track.currPosHasEmSi && 
		   		!mlt_data.track.currPosHasMsg) {
		   		// add recentMarker only if there are minimum 2 positions and
	   			// the recent position has no message and no emergency signal.
		   		mlt_createRecentIcon(mlt_data.track.senderSymbolId);
		   		var info = mlt_posAndInfo[cntPos-1].info(
   	   				"<spring:message code='track.map.info.window.title.currentPosition' />",
   	   				mlt_infoTableHdrColor4RecentPosition);
		   		mlt_recentMarker =
		   			mlt_createMarkerWithInfoWindow(
	    	   			mlt_recentIcon, 
	    	   			mlt_positions[cntPos-1].lat, 
	    	   			mlt_positions[cntPos-1].lng,
	    	   			info);
		   		if (mlt_markersVisible) {
		   			mlt_map.addLayer(mlt_recentMarker);
		   		}
		   		mlt_recentCircleMarker = mlt_addCircleMarkerToMap(
		   			mlt_map,
	   				mlt_positions[cntPos-1].lat, 
	   				mlt_positions[cntPos-1].lng,
	   				mlt_RECENT_POS, 
	   				true,
	   				info);
		   	} 
	   	}
	   	if ((mlt_data.track == null) && mlt_data.user.hasHome) {
	   		mlt_clearAllLayers();
	   		var info = mlt_posAndInfo[0].infoWOTimestamp(
   	   			"<spring:message code='track.map.info.window.title.homePosition' />",
   	   			mlt_infoTableHdrColor4HomePosition);
	   		mlt_homeMarker = 
	   			mlt_createMarkerWithInfoWindow(
	    	   		mlt_homeIcon, 
	    	   		mlt_positions[0].lat, 
	    	   		mlt_positions[0].lng,	    	   		
	    	   		info);
	   		if (mlt_markersVisible) {
		   		mlt_map.addLayer(mlt_homeMarker);
	   		}
		   	mlt_homeCircleMarker = mlt_addCircleMarkerToMap(
		   		mlt_map,
   				mlt_positions[0].lat, 
   				mlt_positions[0].lng,
   				mlt_HOME_POS, 
   				false,
   				info);
	   	}
	   	mlt_putMarkersToLayer();
	   	mlt_putCircleMarkersToLayer();
   	}
	
	function mlt_flyToView() {
		var center = mlt_DEFAULT_CENTER;
		var zoom = mlt_DEFAULT_ZOOM;
		if (!mlt_bounds_empty) {
			center = mlt_bounds.getCenter();
	 		zoom = mlt_map.getBoundsZoom(mlt_bounds, false);
	 		if (zoom > 0) { zoom--; }
		}
	 	mlt_map.setView(center, zoom);
	}
	
	function mlt_flyToCurrentPosition(calcZoom) {
		if (mlt_positions.length == 0) {
			return;
		}
		var center = mlt_positions[mlt_positions.length-1];
		var zoom = mlt_map.getZoom();
		if (calcZoom) {
			zoom = mlt_DEFAULT_ZOOM;
			if (!mlt_bounds_empty) {
				zoom = mlt_map.getBoundsZoom(mlt_bounds);
				if (zoom > 0) { zoom--; }
			}
		}
		mlt_map.setView(center, zoom);
	}
	
	// fit map or not.
	function mlt_fitMapOrNot(fitMap, isHome) {	
		var flyToMode = "<c:out value='${flyToMode}' />";
		if (fitMap) {
			if (flyToMode != "FlyToCurrentPosition") {
				mlt_flyToView();
			} else {
				mlt_flyToCurrentPosition(true);
			}
			return;
		}	
		if (isHome) {
			mlt_flyToCurrentPosition(false);
			return;
		}
		if (flyToMode == "None") {
			return;
		}
		if (flyToMode == "FlyToView") {
			mlt_flyToView();
			return;
		}		
		if (flyToMode == "FlyToCurrentPosition") {
			mlt_flyToCurrentPosition(false);
			return;
		}
	}
	
	function mlt_resetPositionsBoundsAndMarkers() {
 		mlt_positions = [];
 		mlt_posAndInfo = [];
		mlt_bounds = new L.LatLngBounds();
		mlt_bounds_empty = true;
		mlt_markers = new Array();
		mlt_circleMarkers = new Array();
 	}

 	function mlt_UpdateStatus(fitMap, reset, isHome) {		
		this.fitMap = fitMap;	  	
		this.reset = reset;	  		  		  	
		this.isHome = isHome;
	}
 	
	// update positions, bounds and markers of current track, home location or 'nothing'.
	// returns mlt_UpdateStatus. 
	function mlt_updatePositionsBoundsAndMarkers() {
		var fitMap = false;
		var reset = false;
		var isHome = false;
		if ((mlt_lastView == null) || (0 < <c:out value='${keepRecPos}'/>)) {
			mlt_resetPositionsBoundsAndMarkers();			
			fitMap = true && (0 == <c:out value='${keepRecPos}'/>);
			reset = true;								
		}
		if (mlt_data.track != null) {		   	
		   	if ((mlt_data.track.trackId != mlt_lastTrackId) || 
				(mlt_data.track.versionMajor != mlt_lastTrackVersionMajor)) {
		   		mlt_resetPositionsBoundsAndMarkers();	   			
		   		fitMap = true;
		   		reset = true;		   		
		   	}	
		   	if (mlt_data.track.positions != null) {			   	
			   	for (var i=0; i < mlt_data.track.positions.length; i++) {
				   	var pos = new L.LatLng(
	   					mlt_data.track.positions[i].lat,
	   					mlt_data.track.positions[i].lon);	   			
		   			mlt_positions.push(pos);
		   			mlt_posAndInfo.push(new mlt_PosAndInfo(
	   					pos, 
	   					mlt_data.track.positions[i].rcvd, 
	   					mlt_data.track.positions[i].infoTitle));
		   			mlt_bounds.extend(pos);		
		   			mlt_bounds_empty = false;
	   	   		}		   	   	
		   	}
		   	if ((mlt_data.track.positions != null) &&
		   		(mlt_data.track.positions.length > 0)) {	
		   		// if there are new positions,
		   		// existing lastMessageCircleMarker must be set to 'not recent'.
		   		if (mlt_lastMessageCircleMarkerId != null) {
		   			mlt_circleMarkers[mlt_lastMessageCircleMarkerId] =
		   				mlt_lastMessageCircleMarker;
		   			mlt_lastMessageCircleMarkerId = null;
			   		mlt_lastMessageCircleMarker = null;
		   		}
		   		// if there are new positions,
		   		// existing lastEmergencySignalCircleMarker must be set to 'not recent'.
		   		if (mlt_lastEmergencySignalCircleMarkerId != null) {
		   			mlt_circleMarkers[mlt_lastEmergencySignalCircleMarkerId] =
		   				mlt_lastEmergencySignalCircleMarker;
		   			mlt_lastEmergencySignalCircleMarkerId = null;
		   			mlt_lastEmergencySignalCircleMarker = null;
		   		}
	   		}	   	
		   	if (mlt_data.track.messages != null) {
  	   			for (var i=0; i < mlt_data.track.messages.length; i++) {
  	   				var skipMsg = false;
  	   				if ((i == (mlt_data.track.messages.length-1)) &&
  	   					mlt_data.track.currPosHasEmSi && 
  			   			mlt_data.track.currPosHasMsg) {
  	   					skipMsg = true;
  	   				}
 	   				if (!skipMsg && mlt_data.track.messages[i].hasPos) {
 	   					var isRecent = 
 	   						(i == (mlt_data.track.messages.length-1)) &&
 	     			   		mlt_data.track.currPosHasMsg;
 	   					var info = mlt_infoTable("<spring:message code='track.map.info.window.title.message' />", 
	    	   				mlt_infoTableHdrColor4Message,	
  	   						mlt_data.track.messages[i].rcvd, 
  	   						mlt_data.track.messages[i].infoTitle,
  	   						mlt_data.track.messages[i].msg);
	  	   				mlt_markers[mlt_createMsgMarkerId(mlt_data.track.messages[i].id)] = 
	  	  	   				mlt_createMarkerWithInfoWindow(
	  	    	   				mlt_messageIcon, 
	  	    	   				mlt_data.track.messages[i].lat, 
	  	    	   				mlt_data.track.messages[i].lon,
	  	    	   				info);
	  	   				mlt_circleMarkers[mlt_createMsgCircleMarkerId(mlt_data.track.messages[i].id)] =
	  	   					mlt_createCircleMarker(
		  	   					mlt_data.track.messages[i].lat, 
		  	   					mlt_data.track.messages[i].lon,
		  	   					mlt_MESSAGE_POS, isRecent, info);
	  	   				if (isRecent) {
	  	   					mlt_lastMessageCircleMarkerId = 
	  	   						mlt_createMsgCircleMarkerId(mlt_data.track.messages[i].id); 
	  	   					mlt_lastMessageCircleMarker = mlt_createCircleMarker(
		  	   					mlt_data.track.messages[i].lat, 
		  	   					mlt_data.track.messages[i].lon,
		  	   					mlt_MESSAGE_POS, false);
	  	   				}
 	   				}
  	   			}
	   		} 
		   	if (mlt_data.track.emergencySignals != null) {
  	   			for (var i=0; i < mlt_data.track.emergencySignals.length; i++) {
  	   				if (mlt_data.track.emergencySignals[i].hasPos) {
  	   					var isRecent = 
	   						(i == (mlt_data.track.emergencySignals.length-1)) &&
	     			   		mlt_data.track.currPosHasEmSi;
  	   					var info = mlt_infoTable(
    	   					(mlt_data.track.emergencySignals[i].active ?	
    	   						"<spring:message code='track.map.info.window.title.emergencySignalActivated' />" :
    	   						"<spring:message code='track.map.info.window.title.emergencySignalDeactivated' />"), 
    	   					(mlt_data.track.emergencySignals[i].active ? 
   	   							mlt_infoTableHdrColor4EmergencySignalActivated :
   	   							mlt_infoTableHdrColor4EmergencySignalDeactivated),	
   	   						mlt_data.track.emergencySignals[i].rcvd, 
   	   						mlt_data.track.emergencySignals[i].infoTitle,
   	   						mlt_data.track.emergencySignals[i].msg);
  	  	   				mlt_markers[mlt_createEmSiMarkerId(mlt_data.track.emergencySignals[i].id)] = 
  	  	   					mlt_createMarkerWithInfoWindow(
	  	   						(mlt_data.track.emergencySignals[i].active ?
	    	   						mlt_emergencyActivatedIcon :
	    	   						mlt_emergencyDeactivatedIcon), 
	  	    	   				mlt_data.track.emergencySignals[i].lat, 
	  	    	   				mlt_data.track.emergencySignals[i].lon,
	  	    	   				info);
  	  	   				var posType = mlt_data.track.emergencySignals[i].active ?
  	  	   					mlt_EMERGENCY_SIGNAL_ACTIVATED_POS : mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS; 
  	  	   					mlt_circleMarkers[mlt_createEmSiCircleMarkerId(mlt_data.track.emergencySignals[i].id)] =
  	  	   					mlt_createCircleMarker(
		  	   					mlt_data.track.emergencySignals[i].lat, 
		  	   					mlt_data.track.emergencySignals[i].lon,
		  	   					posType,
		  	   					isRecent,
		  	   					info);
	  	  	   			if (isRecent) {
	  	   					mlt_lastEmergencySignalCircleMarkerId = 
	  	   						mlt_createEmSiCircleMarkerId(mlt_data.track.emergencySignals[i].id);
	  	   					var posType = mlt_data.track.emergencySignals[i].active ?
	  	  	   					mlt_EMERGENCY_SIGNAL_ACTIVATED_POS : mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS; 
	  	   					mlt_lastEmergencySignalCircleMarker = mlt_createCircleMarker(
		  	   					mlt_data.track.emergencySignals[i].lat, 
		  	   					mlt_data.track.emergencySignals[i].lon,
		  	   					posType, false);
	  	   				}
  	   				}
  	   			}
	   		}	   			   		
		} else if (mlt_data.user.hasHome) {
			mlt_resetPositionsBoundsAndMarkers();
			var pos = new L.LatLng(
				mlt_data.user.homeLat,
				mlt_data.user.homeLon);
			mlt_positions.push(pos);
			mlt_posAndInfo.push(new mlt_PosAndInfo(
 				pos, null, mlt_data.user.homeAddrTitle));
			mlt_bounds.extend(new L.LatLng(
				mlt_data.user.homeLat,
				mlt_data.user.homeLon));		
			mlt_bounds_empty = false;
			fitMap = (mlt_lastView != "home");
			reset = true;
			isHome = true;
		} else {
			mlt_resetPositionsBoundsAndMarkers();
		}
		return new mlt_UpdateStatus(fitMap, reset, isHome);
	}

	// update view.
   	function mlt_updateView() {
   	   	var updateStatus = mlt_updatePositionsBoundsAndMarkers();
   	   	mlt_fitMapOrNot(updateStatus.fitMap, updateStatus.isHome);
   	   	mlt_drawPolylineAndMarkers(updateStatus.reset);
   	   	mlt_updateInfoDisplay();
		mlt_updateViewState();
   	}
   	
   	// process response callback.
   	function mlt_processResponse(data) {   	   	   		
   	   	if (!data.jsonCommons.reqRejected) {
   	   	   	mlt_data = data;
   	   		mlt_updateView();
            mlt_reqId = mlt_data.jsonCommons.reqId;
        }
   	 	hideLoadingWindow();
   	}
   	
   	function mlt_refreshTracksOverview() {
   		if (mlt_map == null) {
   			var mlt_supportedLayerNames = [
             	<c:forEach var="supportedMap" items="${supportedMaps}">
                 	"<spring:message code='${supportedMap.label}' />",
             	</c:forEach>
            ];
   			mlt_map = mlt_initMap("map_canvas",
   				"<c:out value='${mapsUsedStr}' />",
   				"<c:out value='${defMapId}' />",
   				mlt_DEFAULT_CENTER, mlt_DEFAULT_ZOOM,
   				mlt_supportedLayerNames);
   		}   			
   		if (mlt_reqId != null) {   	   	   		
   	   		var reqType = "json";   			
  			var userId = "<c:out value='${userId}'/>";
			var ticketId = "<c:out value='${ticketId}'/>";
			var senderId = "<c:out value='${senderId}'/>";
			var trackId = "<c:out value='${trackId}'/>";			
  			<c:choose>	
				<c:when test="${live}">				
					var keepRecPos = "<c:out value='${keepRecPos}'/>"; 
  				</c:when>
				<c:otherwise>
					var keepRecPos = "0";
				</c:otherwise>
			</c:choose>					
			var cntPosDrawed = 0;
			if (mlt_positions != null) {
				cntPosDrawed = mlt_positions.length;
			}
			var reqIdSnd = mlt_reqId;
			mlt_reqId = null;
			$.getJSON('track_as_map.sec', {
				reqId:reqIdSnd,reqType:reqType,userId:userId,ticketId:ticketId,
	  			senderId:senderId,trackId:trackId,keepRecPos:keepRecPos,
	  			cntPosDrawed:cntPosDrawed,recTrackId:mlt_lastTrackId},
	  			mlt_processResponse
	  		);
	   	}	
   		$("#loadDataMarker").hide();
   	}
   	
    $(document).ready(      		   
   		function mlt_processRequest() {
   			showLoadingWindow();
   			mlt_refreshTracksOverview();
   		}   	   		   	   	   		
    ); 	

    var autoRefreshTimer = null;
    function startAutoRefresh() {
    	autoRefreshTimer = <c:out value="${intervalSecs}"/>;
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
    <c:if test="${live}">
    	window.onload=startAutoRefresh;
   	</c:if>	
</script>
	
<body>
	<div id="divTrack" style="height:98%;">
	<table
		<c:choose>
			<c:when test="${fullscreen}">
			style="width:100%;height:100%" 			
			</c:when>
			<c:otherwise>
			style="width:<c:out value="${width}"/>px;height:<c:out value="${height}"/>px;"
			</c:otherwise>
		</c:choose>
	>
	<c:if test='${showTrackInfo}' >	
		<tr style="height:10px;">			
			<td style="border:none;">
				<div id="divActiveTrack" >
				<table style="font-size: small;">
					<tr>
						<td id="labelSenderName"  
							style="font-weight: bold;width: 10%;white-space: nowrap;">
							&nbsp;<spring:message code='track.status.labelSenderName' />&nbsp;&nbsp;
							<img id="loadDataMarker" src="img/glossbasic/bullet_red.png" style="margin-top:-16px;margin-bottom:-12px;border: none;vertical-align: middle;"/>
						</td>
						<td id="valueSenderName"  colspan="2"
							style="white-space: nowrap;width: 20%"></td>						
						<td id="labelTrackName"
							style="font-weight: bold;width: 10%;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelTrackName' />&nbsp;
						</td>
						<td id="valueTrackName" colspan="2"   
							style="white-space: nowrap;width: 20%;color:#529647; font-weight: bold"></td>
						<td id="labelMessage"  
							style="font-weight: bold;width: 10%;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelMessage' />&nbsp;
						</td>
						<td id="valueMessage" colspan="4"
							style="white-space: nowrap;width: 30%;color:#477c94;font-weight: bold"></td>											
					</tr>
					<tr>
						<td id="labelPosition" 
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelPosition' />&nbsp;
						</td> 
						<td id="valuePosition" colspan="5" 
							style="white-space: nowrap;width:50%;"></td>
						<td id="labelMobNwCell"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelMobNwCell' />&nbsp;
						</td>
						<td id="valueMobNwCell" colspan="4" 
							style="white-space: nowrap;width:30%;"></td>			
					</tr>					
					<tr>
						<td id="labelStarted"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelStarted' />&nbsp;
						</td>	
						<td id="valueStarted" colspan="2"  
							style="white-space: nowrap;width:20%;"></td>						
						<td id="labelDistance"  
							style="font-weight: bold;width:10%;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelDistance' />&nbsp;
						</td>
						<td id="valueDistance"  
							style="white-space: nowrap;width:10%;"></td>		
						<td id="labelCurrSpeed"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelCurrSpeed' />&nbsp;
						</td>
						<td id="valueCurrSpeed"  
							style="white-space: nowrap;width:10%;"></td>
						<td id="labelRuntime"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelRuntime' />&nbsp;
						</td>	
						<td colspan="3" id="valueRuntime"  
							style="white-space: nowrap;width:20%;"></td>							
					</tr>
					<tr>
						<td id="labelLastUpdated"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelLastUpdated' />&nbsp;
						</td>	
						<td id="valueLastUpdated" colspan="2"  
							style="white-space: nowrap;width:20%;"></td>						
						<td id="labelCurrAltitude"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelCurrAltitude' />&nbsp;
						</td>
						<td id="valueCurrAltitude"  
							style="white-space: nowrap;width:10%;"></td>							
						<td id="labelAvgSpeed"  
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelAvgSpeed' />&nbsp;
						</td>
						<td id="valueAvgSpeed"  
							style="white-space: nowrap"></td>
						<td id="labelCardiacFunction" 
							style="font-weight: bold;width:10%;text-align:left;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelCardiacFunction' />&nbsp;
						</td>				
						<td id="valueCardiacFunction" style="width:22%;text-align:left;white-space: nowrap" colspan="3">							
						</td>					
					</tr>
				</table>								
				</div>
				<div id="divHomeLocation">
				<table style="font-size: small;">	
					<tr>
						<td id="labelHomeAddress"  
							style="font-weight: bold;width: 35px;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelHomeLocation' />&nbsp;
						</td>						
						<td id="valueHomeAddress"  
							style="white-space: nowrap;"></td>
						<td style="width:100%;white-space: nowrap">&nbsp;</td>							
					</tr>
				</table>
				</div>
			</td>
		</tr>
	</c:if>	
		<tr>
			<td>
				<div id="map_canvas" style="width:100%;height:100%"></div>					
			</td>
		</tr>
	<c:if test='${showTrackInfo}' >	
		<tr style="height:10px;">
			<td style="border:none;">
				<table>
					<tr>			
						<td id="updateStatusPos" 
							style="font-size: xx-small;text-align: left;vertical-align: middle;white-space: nowrap">							
						</td>
						<td id="updateStatusDevice" 
							style="width:100%;font-size: xx-small;text-align: left;vertical-align: middle;white-space: nowrap">							
						</td>
						<td id="updateStatusTimestamp" 
							style="font-size: xx-small;text-align: right;vertical-align: middle;white-space: nowrap">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</c:if>		
	</table>
    </div>
    <div class="modal"><!-- Place at bottom of page --></div>
</body>

<script type="text/javascript" >		
	var mlt_startIcon = null;
	var mlt_firstIcon = null;
	var mlt_recentIcon = null;
	var mlt_messageIcon = null;
	var mlt_homeIcon = null;
	var mlt_emergencyActivatedIcon = null;
	var mlt_emergencyDeactivatedIcon = null;	
			
	function mlt_createIcon(imageUrl, size) {
		return new L.icon({
		    iconUrl: imageUrl,
		    iconSize: size,
		    iconAnchor: [size.x/2, size.y+5],
		    popupAnchor: [1, -1 * (size.y+3)]
		});
	}
	
	function mlt_createRecentIcon(senderSymbolId) {
		var senderSymbolImageUrl = "<c:url value='img/map/'/>" + senderSymbolId + ".png";
		mlt_recentIcon = mlt_createIcon(senderSymbolImageUrl, new L.Point(32, 37));
	}
	
	function mlt_createIcons() {
		mlt_startIcon     = mlt_createIcon("<c:url value="img/map/start.png"/>", new L.Point(32, 37));
		mlt_firstIcon     = mlt_createIcon("<c:url value="img/map/star.png"/>", new L.Point(32, 37));
		mlt_messageIcon   = mlt_createIcon("<c:url value="img/map/message.png" />", new L.Point(32, 37));
		mlt_homeIcon      = mlt_createIcon("<c:url value="img/map/home.png" />", new L.Point(32, 37));
		mlt_emergencyActivatedIcon = mlt_createIcon("<c:url value="img/map/emergencyactivated.png" />", new L.Point(32, 37));
		mlt_emergencyDeactivatedIcon = mlt_createIcon("<c:url value="img/map/emergencydeactivated.png" />", new L.Point(32, 37));
	}
	mlt_createIcons();

	function mlt_createMarkerAux(icon, lat, lon) {  	
		mlt_createMarkerAux(icon, lat, lon, false);
   	}
   	
	function mlt_createMarkerAux(icon, lat, lon, clickable) {  	
   	   	var marker = new L.Marker(
	    	new L.LatLng(lat, lon), 
    		{ icon:icon, clickable:clickable, opacity:0.7 });	    
	    return marker; 
   	}
	
	function mlt_createMarkerWithInfoWindow(icon, lat, lon, message) {
		var marker = mlt_createMarkerAux(icon, lat, lon, true);
		marker.bindPopup(message);
		return marker; 	    
   	}   	

	function mlt_updateInfoDisplay() {
		var SPACE = "&nbsp;";   		
   		if (mlt_data.track != null) {   		   			
   			$("#divHomeLocation").hide();
			$("#divActiveTrack").show();
   	   		$("#valueSenderName").html(SPACE + mlt_data.track.senderName + SPACE);
			$("#valueTrackName").html(SPACE + mlt_data.track.name + SPACE);
			$("#valueStarted").html(SPACE + mlt_data.track.firstPosRcvd + SPACE);
			$("#valueLastUpdated").html(SPACE + mlt_data.track.recPosUpd + SPACE);
			if (mlt_data.track.runtimeWithPauses != null) {
				var runtimeStr = mlt_data.track.runtimeWithPauses;
				if (mlt_data.track.runtimeWithoutPauses != null) {
					runtimeStr += SPACE + "(" +
						mlt_data.track.runtimeWithoutPauses + ")";
				}
				$("#valueRuntime").html(SPACE + runtimeStr + SPACE);
			}
			if (mlt_data.track.recPos == null) {
				$("#valuePosition").html(SPACE + mlt_data.jsonCommons.notFound + SPACE);
			} else {				
				$("#valuePosition").html(SPACE + mlt_data.track.recPos.info + SPACE);											
			}
			if (mlt_data.track.recMobNwCell == null) {
				$("#valueMobNwCell").html(SPACE + mlt_data.jsonCommons.notFound + SPACE);
			} else {				
				$("#valueMobNwCell").html(SPACE + mlt_data.track.recMobNwCell.info + SPACE);
			}			
			if (mlt_data.track.recMsg == null) {
				$("#valueMessage").html(SPACE + mlt_data.jsonCommons.notFound + SPACE);
			} else {
				$("#valueMessage").html(SPACE + mlt_data.track.recMsg.msg + SPACE);
			}
			$("#valueDistance").html(SPACE + mlt_data.track.distance + SPACE);
			$("#valueCurrSpeed").html(SPACE + mlt_data.track.posSpeed + SPACE);			
			var avgSpeedStr = mlt_data.track.avgSpeedWithPauses;
			if (mlt_data.track.avgSpeedWithoutPauses != null) {
				avgSpeedStr += SPACE + "(" +
					mlt_data.track.avgSpeedWithoutPauses + ")";		
			}
			$("#valueAvgSpeed").html(SPACE + avgSpeedStr + SPACE);
			$("#valueCurrAltitude").html(SPACE + mlt_data.track.posAlt + SPACE);
			if (mlt_data.track.recCardiacFunction == null) {
				$("#valueCardiacFunction").html(SPACE + mlt_data.jsonCommons.notFound + SPACE);
			} else {
				$("#valueCardiacFunction").html(SPACE + mlt_data.track.recCardiacFunction.info + SPACE);
			}
			$("#updateStatusTimestamp").html(SPACE +
				"<spring:message code='track.status.labelStatusUpdated' />" +
				SPACE + mlt_data.jsonCommons.statusUpdated + SPACE);
			if (mlt_data.track.recSenderState == null) {
				$("#updateStatusDevice").html(SPACE + 
					"<spring:message code='track.status.labelSenderStatus' />:" +	
					SPACE + mlt_data.jsonCommons.notFound + SPACE);				
			} else {
				$("#updateStatusDevice").html(SPACE +
					"<spring:message code='track.status.labelSenderStatus' />:" +
					SPACE + mlt_data.track.recSenderState.info + SPACE);
			}
			$("#updateStatusPos").html(SPACE +
				"<spring:message code='track.status.loadedPositions' />:" + 
				SPACE + (mlt_positions != null ? mlt_positions.length : 0));							
   		} else {   	   		
   			$("#divActiveTrack").hide();
			$("#divHomeLocation").show();
			$("#updateStatusTimestamp").html(SPACE +
				"<spring:message code='track.status.labelStatusUpdated' />" +
				SPACE + mlt_data.jsonCommons.statusUpdated + SPACE);		
			$("#updateStatusPos").html(SPACE +
				"<spring:message code='track.status.loadedPositions' />:" + 
				SPACE + 1 + SPACE);			
			$("#valueHomeAddress").html(SPACE + mlt_data.user.homeAddr + SPACE);			
   		}   		   	   		
   	}
  
</script>
