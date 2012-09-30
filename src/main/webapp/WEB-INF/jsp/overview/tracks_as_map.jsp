<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet-0.4.4.css'/>" />
<!--[if lte IE 8]>
     <link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet.ie-0.4.4.css'/>" />
<![endif]-->
<link rel="stylesheet" href="<c:url value='/js/leaflet/leaflet.iconlabel.css'/>" />

<script type="text/javascript" src="<c:url value='/js/leaflet/leaflet-0.4.4.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.iconlabel.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.fullscreen.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.autozoom.js'/>"></script>
<script src="<c:url value='/js/leaflet/leaflet.providers-0.0.1.js'/>"></script>
<script src="<c:url value='/js/map.commons.js'/>"></script>

<style type="text/css">
	#map { width: 700px; height: 433px; }
	.leaflet-control-zoom-fullscreen { background-image: url(<c:url value='/js/leaflet/images/icon-fullscreen.png'/>); }
	.leaflet-control-zoom-fullscreen.last { margin-top: 5px }
	/* on selector per rule as explained here : http://www.sitepoint.com/html5-full-screen-api/ */
	#map:-webkit-full-screen { width: 100% !important; height: 100% !important; }
	#map:-moz-full-screen { width: 100% !important; height: 100% !important; }
	#map:full-screen { width: 100% !important; height: 100% !important; }
	.leaflet-control-zoom-autozoom { background-image: url(<c:url value='/js/leaflet/images/icon-fullscreen.png'/>); }
	.leaflet-control-zoom-autozoom.last { margin-top: 5px }
</style>

<script type="text/javascript">
	mlt_getOnlyActiveTracks = true;
	
	//the map object.
	var mlt_map = null;
	var mlt_DEFAULT_CENTER = new L.LatLng(48.1391265, 11.5801863);
	var mlt_DEFAULT_ZOOM = 3;

	var mlt_markers = null;
	var mlt_bounds = null;
	var mlt_bounds_empty = true;
	
	//process response callback.
	function mlt_renderTracksOverview(mlt_data) {
		mlt_resetBoundsAndMarkers();
		for (var i=0; i < mlt_data.tracks.length; i++) {
			if (mlt_data.tracks[i].cntPos > 0) {
				var recEmSiIsActive = false;
				if (mlt_data.tracks[i].cntEmSi > 0) {
					recEmSiIsActive = mlt_data.tracks[i].recEmSi.active;
				}
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
					recEmSiIsActive);
				mlt_markers.push(marker);
				mlt_map.addLayer(marker);
				mlt_addCircleMarkerToMarkersAndMap(
					mlt_markers, mlt_map,
					mlt_data.tracks[i].recPos.lat, 
					mlt_data.tracks[i].recPos.lon,
					mlt_RECENT_POS, true);
				var pos = new L.LatLng(
   					mlt_data.tracks[i].recPos.lat,
   					mlt_data.tracks[i].recPos.lon);
				mlt_bounds.extend(pos);		
	   			mlt_bounds_empty = false;
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
	
	function mlt_resetBoundsAndMarkers() {
		if (mlt_markers != null) {
			for (var i=0; i < mlt_markers.length; i++) {
			   	mlt_map.removeLayer(mlt_markers[i]);				   	
		   	}
		}
 		mlt_bounds = new L.LatLngBounds();
		mlt_bounds_empty = true;
		mlt_markers = [];
 	}
	
	function mlt_adaptZoomLevel() {
		var center = mlt_DEFAULT_CENTER;
		var zoom = mlt_DEFAULT_ZOOM;
		if (!mlt_bounds_empty) {
			center = mlt_bounds.getCenter();
	 		zoom = mlt_map.getBoundsZoom(mlt_bounds, false);
	 		if (zoom > 0) { zoom--; }
		}
	 	mlt_map.setView(center, zoom);
	}
</script> 
<table id="map_complete" style="width:100%;height:80%;">
	<tr>
		<td>
			<div id="map_canvas" style="width:100%;height:100%;"></div>
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
	function mlt_initMap(anker, mapsUsedStr, defMapId, defCenter, defZoom) {
		var supportedLayerNames = [
			"<spring:message code='map.OpenStreetMap.Mapnik' />",
			"<spring:message code='map.OpenStreetMap.DE' />",
			"<spring:message code='map.OpenStreetMap.BlackAndWhite' />",
			"<spring:message code='map.Thunderforest.OpenCycleMap' />",
			"<spring:message code='map.Thunderforest.Transport' />",
			"<spring:message code='map.Thunderforest.Landscape' />",
			"<spring:message code='map.MapQuestOpen.OSM' />",
			"<spring:message code='map.MapQuestOpen.Aerial' />",
			"<spring:message code='map.MapBox.Simple' />",
			"<spring:message code='map.MapBox.Streets' />",
			"<spring:message code='map.MapBox.Light' />",
			"<spring:message code='map.MapBox.Lacquer' />",
			"<spring:message code='map.MapBox.Warden' />",
			"<spring:message code='map.Stamen.Toner' />",
			"<spring:message code='map.Stamen.Terrain' />",
			"<spring:message code='map.Stamen.Watercolor' />",
			"<spring:message code='map.Esri.WorldStreetMap' />",
			"<spring:message code='map.Esri.DeLorme' />",
			"<spring:message code='map.Esri.WorldTopoMap' />",
			"<spring:message code='map.Esri.WorldImagery' />",
			"<spring:message code='map.Esri.OceanBasemap' />",
			"<spring:message code='map.Esri.NatGeoWorldMap' />"
		];
		var supportedLayers = [
			new L.TileLayer.OpenStreetMap.Mapnik,
			new L.TileLayer.OpenStreetMap.DE,
			new L.TileLayer.OpenStreetMap.BlackAndWhite,
			new L.TileLayer.Thunderforest.OpenCycleMap,
			new L.TileLayer.Thunderforest.Transport,
			new L.TileLayer.Thunderforest.Landscape,
			new L.TileLayer.MapQuestOpen.OSM,
			new L.TileLayer.MapQuestOpen.Aerial,
			new L.TileLayer.MapBox.Simple,
			new L.TileLayer.MapBox.Streets,
			new L.TileLayer.MapBox.Light,
			new L.TileLayer.MapBox.Lacquer,
			new L.TileLayer.MapBox.Warden,
			new L.TileLayer.Stamen.Toner,
			new L.TileLayer.Stamen.Terrain,
			new L.TileLayer.Stamen.Watercolor,
			new L.TileLayer.Esri.WorldStreetMap,
			new L.TileLayer.Esri.DeLorme,
			new L.TileLayer.Esri.WorldTopoMap,
			new L.TileLayer.Esri.WorldImagery,
			new L.TileLayer.Esri.OceanBasemap,
			new L.TileLayer.Esri.NatGeoWorldMap
		];		
		var baseLayers = {};
		for (var idx=0; idx < mapsUsedStr.length; idx++) {
			if (mapsUsedStr[idx] == "1") {
				baseLayers[supportedLayerNames[idx]] = supportedLayers[idx];	
			}
		}
		var map = new L.Map(anker, {
			center: defCenter,
			zoom: defZoom,
			attributionControl: true
		});
		var fullscreen = new L.Control.FullScreen();
		map.addControl(fullscreen);
		var autozoom = new L.Control.AutoZoom();
		map.addControl(autozoom);
		var defaultLayer = supportedLayers[defMapId];
		map.addLayer(defaultLayer);
		map.addControl(new L.Control.Layers(baseLayers,'',{collapsed: true}));
		
		return map;
	}
	
	mlt_map = mlt_initMap("map_canvas",
		"<c:out value='${tracksOverviewCmd.mapsUsedStr}' />",
		"<c:out value='${tracksOverviewCmd.defMapId}' />",
		mlt_DEFAULT_CENTER, mlt_DEFAULT_ZOOM);
	
	function mlt_createAndSetSenderMarker(name, symbolId, lat, lon, posStr, rcvd, 
		trackId, trackName, hasMsg, hasEmSi, recEmSiIsActive) {
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
	   	
	   	marker.bindPopup(mlt_infoTable(trackId, trackName, rcvd, posStr, hasMsg, hasEmSi, recEmSiIsActive));
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