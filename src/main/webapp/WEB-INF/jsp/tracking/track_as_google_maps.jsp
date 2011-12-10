<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page import="de.msk.mylivetracker.web.util.WebUtils" %>

<%
	String timestampPattern = WebUtils.getMessage(request, "track.gmaps.timestamp.pattern");
	request.setAttribute("timestampPattern", timestampPattern);
%>

<script src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=<c:out value="${googleApiKey}"/>'
    type="text/javascript" ></script>    
<script type="text/javascript">			
	
	function mlt_LatLngRcvd(latlng, addr, rcvd, rec) {		
		this.latlng = latlng;	  	
		this.addr = addr;
	  	this.rcvd = rcvd;	  		 
	  	this.rec = rec;
	}
	mlt_LatLngRcvd.prototype.info = function() {		
		return this.addr + 
			", <spring:message code='track.gmaps.received' />: " + this.rcvd +
			", <spring:message code='track.gmaps.recorded' />: " + this.rec;
	}
	function mlt_TimestampMarker(idx, marker) {		
		this.idx = idx;	  	
		this.marker = marker;
	}		
	
	function mlt_distanceInMtr(posFrom, posTo) {
		var earthRadiusInMtr = 6371000.0;
		var deltaLatitude = (posTo.lat() - posFrom.lat()) * Math.PI / 180;
		var deltaLongitude = (posTo.lng() - posFrom.lng()) * Math.PI / 180;
		var a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) + 
		Math.cos(posFrom.lat() * Math.PI / 180) * 
		Math.cos(posTo.lat() * Math.PI / 180) * 
		Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		var d = earthRadiusInMtr * c;
		return d;
	}
		
	//current request id. 
	var mlt_reqId = "<c:out value='${reqId}'/>";

	// the map object.
	var mlt_map = null;
	var mlt_DEFAULT_CENTER = new GLatLng(48.1391265, 11.5801863);
	var mlt_DEFAULT_ZOOM = 3;
	
	// json data object.
	var mlt_data = null;
	
	// possible values of mlt_comeFrom:
	// o null  --> last view was not a home location nor a track.
	// o home  --> last view was a home location.
	// o track --> last view was a track.
	var mlt_lastView = null;

	var mlt_trackIsHidden = false;
	
	// last track id and version (is != null if mlt_lastView = "track").
	var mlt_lastTrackId = null;
	var mlt_lastTrackVersionMajor = null;

	// last home position.
	var mlt_lastHomePositionLat = null;
	var mlt_lastHomePositionLon = null;
		
	// current positions, bounds and markers.
	var mlt_positions = null;
	var mlt_bounds = null;
	var mlt_timestamps = null;
	var mlt_timestampMarkers = null;
	var mlt_markers = null;	
	var mlt_startMarker = null;
	var mlt_recentMarker = null;
	
	// the polyline.
	var mlt_polyline = null;
	var mlt_polylineEncoder = null;
   	
	// update view state.
	function mlt_updateViewState() {		
		if (mlt_data.track != null) {
			mlt_lastView = "track";
			mlt_lastTrackId = mlt_data.track.trackId;
			mlt_lastTrackVersionMajor = mlt_data.track.versionMajor;
			mlt_lastHomePositionLat = null;
			mlt_lastHomePositionLon = null;
		} else if (mlt_data.user.hasHome) {
			mlt_lastView = "home";			
			mlt_lastTrackId = null;
			mlt_lastTrackVersionMajor = null;
			mlt_lastHomePositionLat = mlt_data.user.homeLat;
			mlt_lastHomePositionLon = mlt_data.user.homeLon;
		} else {
			mlt_lastView = null;
			mlt_lastTrackId = null;
			mlt_lastTrackVersionMajor = null;
			mlt_lastHomePositionLat = null;
			mlt_lastHomePositionLon = null;
		}
	}
	   	 	
 	// update polyline
	function mlt_drawPolylineAndMarkers(reset) {
		if (reset == true) {
			mlt_map.clearOverlays();
		} else {
			if (mlt_polyline != null) {
				mlt_map.removeOverlay(mlt_polyline);
			}
			if (mlt_startMarker != null) {
				mlt_map.removeOverlay(mlt_startMarker);
			}
			if (mlt_recentMarker != null) {
				mlt_map.removeOverlay(mlt_recentMarker);
			}
			if (mlt_markers != null) {
				for (var i=0; i < mlt_markers.length; i++) {
				   	mlt_map.removeOverlay(mlt_markers[i]);				   	
			   	}
			}			
		}
		if ((mlt_positions != null) && (mlt_positions.length > 200)) {
	   		if (mlt_polylineEncoder == null) {
	   			mlt_polylineEncoder = new PolylineEncoder();
	   		}
	   		mlt_polyline = 
	   			mlt_polylineEncoder.dpEncodeToGPolyline(
   					mlt_positions,
			   		"#" + mlt_data.user.routeColor, 
			   		mlt_data.user.routeWidth, 0.7, { clickable:true, geodesic:true });
	   	} else if ((mlt_positions != null) && (mlt_positions.length > 0)) {
	   		mlt_polyline = new GPolyline(
   				mlt_positions, 
		   	   	"#" + mlt_data.user.routeColor, 
		   	 	mlt_data.user.routeWidth, 0.7, { clickable:true, geodesic:true });
	   	} else {
		   	mlt_polyline = null;
	   	}
		if (mlt_polyline != null) {	   					         
			GEvent.addListener(mlt_polyline, "click", function(latlng) {
				if (mlt_timestamps.length <= 0) return;
				if (mlt_trackIsHidden) return;
				
				var min = mlt_distanceInMtr(mlt_timestamps[0].latlng, latlng);
				var idx = 0;
				
				for (var i=1; i < mlt_timestamps.length; i++) {
					var dist = mlt_distanceInMtr(mlt_timestamps[i].latlng, latlng);
					if (dist < min) {
						min = dist;
						idx = i;
					}
				}
				var marker = (mlt_timestampMarkers[idx] == null ? 
					null : mlt_timestampMarkers[idx].marker);			 
				if (marker == null) {								
					marker =			
						mlt_createTimestampMarker(
							idx,
							mlt_timestampIcon, 
		    	   			mlt_timestamps[idx].latlng.lat(), 
		    	   			mlt_timestamps[idx].latlng.lng(),
		    	   			mlt_timestamps[idx].info()  	   			
		    	   		);	   					
					mlt_map.addOverlay(marker);
					mlt_timestampMarkers[idx] = new mlt_TimestampMarker(idx, marker);
				} 							    		
			});  		            
			mlt_map.addOverlay(mlt_polyline);
			if (mlt_trackIsHidden) {
				mlt_polyline.hide();
			}
		}	   
		
	   	if (mlt_data.track != null) {
	   		var cntPos = mlt_positions.length;
	   		if (cntPos > 0) {
	   			mlt_startMarker =
		   			mlt_createMarkerAux(
	    	   			mlt_startIcon, 
	    	   			mlt_positions[0].lat(), 
	    	   			mlt_positions[0].lng());
		   		mlt_map.addOverlay(mlt_startMarker);
		   		if (mlt_trackIsHidden) {
					mlt_startMarker.hide();
				}
		   	}
		   	if (cntPos > 1) {
		   		mlt_recentMarker =
		   			mlt_createMarkerAux(
	    	   			mlt_recentIcon, 
	    	   			mlt_positions[cntPos-1].lat(), 
	    	   			mlt_positions[cntPos-1].lng());
		   		mlt_map.addOverlay(mlt_recentMarker);
		   		if (mlt_trackIsHidden) {
		   			mlt_recentMarker.hide();
				}
		   	}
	   	}
	   	if ((mlt_data.track == null) && mlt_data.user.hasHome) {
	   		mlt_map.clearOverlays();
		   	mlt_map.addOverlay(
	   		   	mlt_createMarkerWithInfoWindow(
	    	   		mlt_homeIcon, 
	    	   		mlt_data.user.homeLat, 
	    	   		mlt_data.user.homeLon,	    	   		
	    	   		mlt_data.user.homeAddr));
	   	}
	   			   	
	   	for (var i=0; i < mlt_markers.length; i++) {
	   		mlt_map.addOverlay(mlt_markers[i]);
	   		if (mlt_trackIsHidden) {
	   			mlt_markers[i].hide();
			}
	   	}		     		   			   			   		   			   	
   	}

	function mlt_flyToView() {
		var center = mlt_DEFAULT_CENTER;
		var zoom = mlt_DEFAULT_ZOOM;
		if (!mlt_bounds.isEmpty()) {
			center = mlt_bounds.getCenter();
	 		zoom = mlt_map.getBoundsZoomLevel(mlt_bounds);
		}
	 	mlt_map.setCenter(center, zoom);
	}

	function mlt_flyToCurrentPosition(calcZoom) {
		if (mlt_positions.length == 0) {
			return;
		}
		var center = mlt_positions[mlt_positions.length-1];
		var zoom = null;
		if (calcZoom) {
			zoom = mlt_DEFAULT_ZOOM;
			if (!mlt_bounds.isEmpty()) {
				zoom = mlt_map.getBoundsZoomLevel(mlt_bounds);
			}
		}
		mlt_map.setCenter(center, zoom);
	}
	
 	// fit map or not.
	function mlt_fitMapOrNot(fitMap) {	
		var flyToMode = <c:out value='${flyToMode}' />;
		if (fitMap) {
			if (flyToMode != 2) {
				mlt_flyToView();
			} else {
				mlt_flyToCurrentPosition(true);
			}
			return;
		}	
		if (flyToMode == 0) {
			return;
		}
		if (flyToMode == 1) {
			mlt_flyToView();
			return;
		}		
		if (flyToMode == 2) {
			mlt_flyToCurrentPosition(false);
			return;
		}
	}

 	function resetPositionsBoundsAndMarkers() {
 		mlt_positions = [];
		mlt_bounds = new GLatLngBounds();
		mlt_timestamps = [];
		mlt_timestampMarkers = new Object();
		mlt_markers = [];
 	}

 	function mlt_UpdateStatus(fitMap, reset) {		
		this.fitMap = fitMap;	  	
		this.reset = reset;	  		  		  	
	}
	
	// update positions, bounds and markers of current track, home location or 'nothing'.
	// returns mlt_UpdateStatus. 
	function mlt_updatePositionsBoundsAndMarkers() {
		var fitMap = false;
		var reset = false;
		if ((mlt_lastView == null) || (0 < <c:out value='${keepRecPos}'/>)) {
			resetPositionsBoundsAndMarkers();			
			fitMap = true && (0 == <c:out value='${keepRecPos}'/>);
			reset = true;								
		}
		if (mlt_data.track != null) {		   	
		   	if ((mlt_data.track.trackId != mlt_lastTrackId) || 
				(mlt_data.track.versionMajor != mlt_lastTrackVersionMajor)) {
		   		resetPositionsBoundsAndMarkers();	   			
		   		fitMap = true;
		   		reset = true;		   		
		   	}	
		   	if (mlt_data.track.positions != null) {			   	
			   	for (var i=mlt_data.track.positions.length-1; i >= 0 ; i--) {
				   	var pos = new GLatLng(
	   					mlt_data.track.positions[i].lat,
	   					mlt_data.track.positions[i].lon);	   			
		   			mlt_positions.push(pos);
		   			mlt_bounds.extend(pos);		   			
		   			var ts = new mlt_LatLngRcvd(
				   		pos,
				   		mlt_data.track.positions[i].infoTitle,
	   					mlt_data.track.positions[i].rcvd,
	   					mlt_data.track.positions[i].rec
				   	);  	  	
				   	mlt_timestamps.push(ts);				   		
		   	   	}		   	   	
		   	}
		   	if (mlt_data.track.messages != null) {
  	   			for (var i=0; i < mlt_data.track.messages.length; i++) {
  	   				mlt_markers.push( 
  	  	   				mlt_createMarkerWithInfoWindow(
  	    	   				mlt_messageIcon, 
  	    	   				mlt_data.track.messages[i].lat, 
  	    	   				mlt_data.track.messages[i].lon,
  	    	   				mlt_data.track.messages[i].msg));	    	   			   	   			  	  	   			      	   		
  	   			}
	   		} 
		   	if (mlt_data.track.emergencySignals != null) {
  	   			for (var i=0; i < mlt_data.track.emergencySignals.length; i++) {
  	  	   			if (mlt_data.track.emergencySignals[i].hasMsg) {
  	  	   				mlt_markers.push( 
  	  	   					mlt_createMarkerWithInfoWindow(
	  	   						(mlt_data.track.emergencySignals[i].active ?
	    	   						mlt_emergencyActivatedIcon :
	    	   						mlt_emergencyDeactivatedIcon), 
	  	    	   				mlt_data.track.emergencySignals[i].lat, 
	  	    	   				mlt_data.track.emergencySignals[i].lon,
	  	    	   				mlt_data.track.emergencySignals[i].msg));
  	  	   			} else {
	  	   				mlt_markers.push( 
	  	   					mlt_createMarkerAux(
	  	   						(mlt_data.track.emergencySignals[i].active ?
	    	   						mlt_emergencyActivatedIcon :
	    	   						mlt_emergencyDeactivatedIcon), 
	  	    	   				mlt_data.track.emergencySignals[i].lat, 
	  	    	   				mlt_data.track.emergencySignals[i].lon));
  	  	   			}	    	   			   	   			  	  	   			      	   		
  	   			}
	   		}	   			   		
		} else if (mlt_data.user.hasHome) {
			if ((mlt_lastHomePositionLat != mlt_data.user.homeLat) ||
				(mlt_lastHomePositionLon != mlt_data.user.homeLon)) {
				resetPositionsBoundsAndMarkers();
				mlt_positions.push(new GLatLng(
					mlt_data.user.homeLat,
					mlt_data.user.homeLon));
				mlt_bounds.extend(new GLatLng(
					mlt_data.user.homeLat,
					mlt_data.user.homeLon));				
				fitMap = true;
				reset = true;
			}			
		} else {
			resetPositionsBoundsAndMarkers();
		}
		return new mlt_UpdateStatus(fitMap, reset);
	}
   	
 	// update view.
   	function mlt_updateView() {
   	   	var updateStatus = mlt_updatePositionsBoundsAndMarkers();
   	   	mlt_fitMapOrNot(updateStatus.fitMap);
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
   	}
   	var mlt_showHideTrackControl = null;
   	function ShowHideTrackControl() {
  		this.labelShowTrack = "<spring:message code='track.status.labelShowTrack' />";
 		this.labelHideTrack = "<spring:message code='track.status.labelHideTrack' />";
 		this.labelCurrent = document.createTextNode(this.labelHideTrack);
 		this.showHideDiv = document.createElement("div"); 
 		this.showHideDiv.appendChild(this.labelCurrent);
 		// following does not work with ie:
 		//var style = document.createAttribute("style");
 		//style.nodeValue = "white-space:nowrap;";
 		//this.showHideDiv.setAttributeNode(style);
 		this.showHideDiv.style.nodeValue = "white-space:nowrap;";
 	}
   	ShowHideTrackControl.prototype = new GControl();
   	ShowHideTrackControl.prototype.initialize = function(map) {
 		var container = document.createElement("div");
   		this.setButtonStyle_(this.showHideDiv);
   		container.appendChild(this.showHideDiv);
   		GEvent.addDomListener(this.showHideDiv, "click", function() {      			
   			var labelNew = document.createTextNode(mlt_showHideTrackControl.labelShowTrack);
   			if (mlt_trackIsHidden) {
   				labelNew = document.createTextNode(mlt_showHideTrackControl.labelHideTrack);
   			}
   			mlt_showHideTrackControl.showHideDiv.replaceChild(labelNew, mlt_showHideTrackControl.labelCurrent);
   			mlt_showHideTrackControl.labelCurrent = labelNew;
   			if (mlt_trackIsHidden) {
				mlt_trackIsHidden = false;				
				if (mlt_polyline != null) {
					mlt_polyline.show();   
					mlt_startMarker.show();
					mlt_recentMarker.show();
					for (var i=0; i < mlt_markers.length; i++) {
			   			mlt_markers[i].show();						
				   	}
					for (var i=0; i < mlt_timestamps.length; i++) {
						if (mlt_timestampMarkers[i] != null) {
							mlt_timestampMarkers[i].marker.show();
						}
				   	}					
				}								   			
   			} else {
   				mlt_trackIsHidden = true;
   				if (mlt_polyline != null) {
   					mlt_polyline.hide();
   					mlt_startMarker.hide();
					mlt_recentMarker.hide();
					for (var i=0; i < mlt_markers.length; i++) {
			   			mlt_markers[i].hide();						
				   	}
					for (var i=0; i < mlt_timestamps.length; i++) {
						if (mlt_timestampMarkers[i] != null) {
					 		mlt_timestampMarkers[i].marker.hide();
						}
				   	}
   				}   				
   			}
   		});
   		map.getContainer().appendChild(container);
   		return container;
 	}
   	ShowHideTrackControl.prototype.getDefaultPosition = function() {
   		return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(8, 30));
 	}
 	ShowHideTrackControl.prototype.setButtonStyle_ = function(button) {
 	 	button.style.color = "#0000cc";
		button.style.backgroundColor = "white";
		button.style.font = "small Arial";
		button.style.border = "1px solid black";
		button.style.padding = "2px";
		button.style.marginBottom = "3px";
		button.style.textAlign = "center";
		button.style.width = "8em";
		button.style.cursor = "pointer";
 	}
 	
 	function mlt_initMap() {
   		if (GBrowserIsCompatible()) {    				        
			var copyrightOSM = new GCopyrightCollection(
				"<a href=\"http://www.openstreetmap.org/\">OpenStreetMap</a>");
			copyrightOSM.addCopyright(
		    	new GCopyright(1, 
		    		new GLatLngBounds(new GLatLng(-90,-180), 
		    	    new GLatLng(90,180)), 0, " "));
		    var tilesMapnik = new GTileLayer(
	    		copyrightOSM, 1, 17, 
		    	{tileUrlTemplate: 'http://tile.openstreetmap.org/{Z}/{X}/{Y}.png'});	    
		    var mapMapnik = new GMapType(
		    	[tilesMapnik],     
		    	G_NORMAL_MAP.getProjection(), 
		    	"OSM");	    	    
		    mlt_map = new GMap2(
		    	document.getElementById("map_canvas"), 
		  		{ mapTypes: [mapMapnik] });	  
		    mlt_showHideTrackControl = new ShowHideTrackControl();
		    mlt_map.addControl(mlt_showHideTrackControl);
		    mlt_map.setCenter(mlt_DEFAULT_CENTER, mlt_DEFAULT_ZOOM);		
		    mlt_map.setUIToDefault();
		}
	}	      	

    $(document).ready(      		    				    			
   		function mlt_processRequest() {
   	   		if (mlt_map == null) {
   	   			mlt_initMap(); 
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
				$.getJSON('track_as_google_maps.sec', 
	   				{reqId:reqIdSnd,reqType:reqType,userId:userId,ticketId:ticketId,
	   					senderId:senderId,trackId:trackId,keepRecPos:keepRecPos,
	   					cntPosDrawed:cntPosDrawed,recTrackId:mlt_lastTrackId},
	   				mlt_processResponse
	   			);   			 	
   	   		}				   			
   			<c:if test="${live}">
   				window.setTimeout(mlt_processRequest, <c:out value="${intervalSecs}"/> * 1000);
   			</c:if>
   	   	}   		
    );	 
</script>

<body onunload="GUnload()">	
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
							&nbsp;<spring:message code='track.status.labelSenderName' />&nbsp;
						</td>
						<td id="valueSenderName"  colspan="2"
							style="white-space: nowrap;width: 20%"></td>						
						<td id="labelTrackName"
							style="font-weight: bold;width: 10%;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelTrackName' />&nbsp;									
						</td>
						<td id="valueTrackName" colspan="2"   
							style="white-space: nowrap;width: 20%;color: green; font-weight: bold"></td>
						<td id="labelMessage"  
							style="font-weight: bold;width: 10%;white-space: nowrap">
							&nbsp;<spring:message code='track.status.labelMessage' />&nbsp;
						</td>
						<td id="valueMessage" colspan="4"
							style="white-space: nowrap;width: 30%;color:red;font-weight: bold"></td>											
					</tr>
					<tr>
						<td id="labelPosition"
							style="font-weight: bold;white-space: nowrap;width:10%;">
							&nbsp;<spring:message code='track.status.labelPosition' />&nbsp;
						</td> 
						<td id="valuePosition" colspan="5" 
							style="white-space: nowrap;width:50%;">																 									
						</td>						
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
</body>

<script type="text/javascript" >		
	var mlt_startIcon = null;
	var mlt_recentIcon = null;
	var mlt_messageIcon = null;
	var mlt_homeIcon = null;
	var mlt_timestampIcon = null;
	var mlt_emergencyActivatedIcon = null;
	var mlt_emergencyDeactivatedIcon = null;	
			
	function mlt_createIcon(size, image, shadow) {
		var iconVar = new GIcon(G_DEFAULT_ICON);   		
        iconVar.iconAnchor = new GPoint(9, size.height);                           
	 	iconVar.iconSize = size;
		iconVar.infoWindowAnchor = new GPoint(9, 2);
		iconVar.image = image;
		iconVar.shadowSize = new GSize(37, size.height);
		iconVar.shadow = shadow;
		return iconVar;	
	}	
	
	function mlt_createIcons() {
		mlt_startIcon     = mlt_createIcon(new GSize(20, 34), "<c:url value="img/dd-start.png"/>", "<c:url value="img/shadow50.png"   />");
		mlt_recentIcon    = mlt_createIcon(new GSize(39, 34), "<c:url value="img/arrow.png"/>", "<c:url value="img/arrowshadow.png"/>");
		mlt_messageIcon   = mlt_createIcon(new GSize(20, 34), "<c:url value="img/blue_MarkerM.png" />", "<c:url value="img/shadow50.png"   />");
		mlt_homeIcon      = mlt_createIcon(new GSize(20, 34), "<c:url value="img/darkgreen_MarkerH.png" />", "<c:url value="img/shadow50.png"   />");
		mlt_timestampIcon = mlt_createIcon(new GSize(12, 24), "<c:url value="img/mm_20_gray.png" />", null);
		mlt_emergencyActivatedIcon = mlt_createIcon(new GSize(20, 34), "<c:url value="img/red_MarkerS.png" />", "<c:url value="img/shadow50.png"   />");
		mlt_emergencyDeactivatedIcon = mlt_createIcon(new GSize(20, 34), "<c:url value="img/yellow_MarkerS.png" />", "<c:url value="img/shadow50.png"   />");		
	}
	
	mlt_createIcons();

	function mlt_createMarkerAux(icon, lat, lon) {  	
		mlt_createMarkerAux(icon, lat, lon, false);
   	}
   	
	function mlt_createMarkerAux(icon, lat, lon, clickable) {  	
   	   	var marker = new GMarker(
	    	new GLatLng(lat, lon), 
    		{ icon:icon, clickable:clickable });	    
	    return marker; 
   	}

	function mlt_createMarkerWithInfoWindow(icon, lat, lon, message) {   	   	
		var marker = mlt_createMarkerAux(icon, lat, lon, true);
	    var windowOptions = { maxWidth:80 };
	    GEvent.addListener(marker, "click", function() {	    	
	    	marker.openInfoWindowHtml(message, windowOptions);
	    });	    		    
	    return marker; 	    
   	}   	

	function mlt_createTimestampMarker(timstampMarkerIdx,
		icon, lat, lon, message) {   	   	
		var marker = mlt_createMarkerAux(icon, lat, lon, true);
	    var windowOptions = { maxWidth:80 };
	    GEvent.addListener(marker, "mouseover", function() {	    	
	    	marker.openInfoWindowHtml(message, windowOptions);
	    });	 
	    GEvent.addListener(marker, "mouseout", function() {	    	
	    	marker.closeInfoWindow();
	    });	   	
    	GEvent.addListener(marker, "dblclick", function() {
    		mlt_map.removeOverlay(marker);
    		mlt_timestampMarkers[timstampMarkerIdx] = null;    		
    	});
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
				SPACE + (mlt_positions != null ? mlt_positions.length : 0) + 
				SPACE + "(enc: " + (mlt_polyline != null ? mlt_polyline.getVertexCount() : 0 ) + ")");							
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