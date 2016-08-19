
var mlt_infoTableHdrFontColor = "#FFFFFF";
var mlt_infoTableHdrColor4HomePosition = "#529647";
var mlt_infoTableHdrColor4StartPosition = "#a8a8a8";
var mlt_infoTableHdrColor4FirstOfLastRecentPositions = "a8a8a8";
var mlt_infoTableHdrColor4RecentPosition = "#529647";
var mlt_infoTableHdrColor4Message = "#477c94";
var mlt_infoTableHdrColor4EmergencySignalActivated = "#f5454d";
var mlt_infoTableHdrColor4EmergencySignalDeactivated = "#c4bc20";
var mlt_color4Recent = "#ee7b7b";
var mlt_color4NotRecent = "#ebebeb";

var mlt_HOME_POS = "homePos";
var mlt_HOME_POS_zIndexOffset = "0";
var mlt_START_POS = "startPos";
var mlt_START_POS_zIndexOffset = "50";
var mlt_RECENT_POS = "recentPos";
var mlt_RECENT_POS_zIndexOffset = "500";
var mlt_MESSAGE_POS = "messagePos";
var mlt_MESSAGE_POS_zIndexOffset = "250";
var mlt_EMERGENCY_SIGNAL_ACTIVATED_POS = "emergencySignalActivatedPos";
var mlt_EMERGENCY_SIGNAL_ACTIVATED_POS_zIndexOffset = "1000";
var mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS = "emergencySignalDeactivatedPos";
var mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS_zIndexOffset = "950";

function mlt_initMap(anker, mapsUsedStr, defMapId, defCenter, defZoom, supportedLayerNames) {
   	var supportedLayers = [
   	    new L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'), // OpenStreetMap.Mapnik
   	    new L.tileLayer('http://{s}.tiles.wmflabs.org/bw-mapnik/{z}/{x}/{y}.png'), // OpenStreetMap.BlackAndWhite
   	    new L.tileLayer('http://{s}.tile.openstreetmap.de/tiles/osmde/{z}/{x}/{y}.png'), // OpenStreetMap.DE
   	    new L.tileLayer('http://{s}.tile.opentopomap.org/{z}/{x}/{y}.png'), // OpenTopoMap 
   	    new L.tileLayer('http://{s}.tile.thunderforest.com/cycle/{z}/{x}/{y}.png'), // Thunderforest.OpenCycleMap
   	    new L.tileLayer('http://{s}.tile.thunderforest.com/transport/{z}/{x}/{y}.png'), // Thunderforest.Transport
   	    new L.tileLayer('http://{s}.tile.thunderforest.com/transport-dark/{z}/{x}/{y}.png'), // Thunderforest.TransportDark
   	    new L.tileLayer('http://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png'), // Thunderforest.Landscape
   	    new L.tileLayer('http://{s}.tile.thunderforest.com/outdoors/{z}/{x}/{y}.png'), // Thunderforest.Outdoors
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldStreetMap
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/Specialty/DeLorme_World_Base_Map/MapServer/tile/{z}/{y}/{x}'), // Esri.DeLorme
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldTopoMap
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldImagery
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Terrain_Base/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldTerrain
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Shaded_Relief/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldShadedRelief
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldPhysical
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/Ocean_Basemap/MapServer/tile/{z}/{y}/{x}'), // Esri.OceanBaseMap
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/{z}/{y}/{x}'), // Esri.NatGeoWorldMap
   	    new L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}'), // Esri.WorldGrayCanvas
   	    new L.tileLayer('http://tile.mtbmap.cz/mtbmap_tiles/{z}/{x}/{y}.png') // MtbMap
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
		attributionControl: true,
		zoomControl: false,
	});
	var defaultLayer = supportedLayers[defMapId];
	map.addLayer(defaultLayer);
	map.addControl(new L.Control.Layers(baseLayers,'',{collapsed: true, position:'topleft'}));
	var fullscreenAndSymbols = new L.Control.FullscreenAndSymbols({position:'topright'});
    map.addControl(fullscreenAndSymbols);
    map.on('symbolsOn', function(){
    	mlt_markersVisible = true;
      	mlt_putMarkersToLayer();
    });
    map.on('symbolsOff', function(){
    	mlt_markersVisible = false;
      	mlt_removeMarkersFromLayer();
    });
    map.on('enterFullscreen', function(){
    	if ($("#divFooter").length > 0) {
    		$(divFooter).hide();
    	}
    });
    map.on('exitFullscreen', function(){
    	if ($("#divFooter").length > 0) {
    		$(divFooter).show();	
    	}
    	$(window).resize();
    });
	return map;
}

function mlt_createCircleMarker(lat, lon, posType, isRecent, popup) {
	var ringColor = mlt_infoTableHdrColor4RecentPosition;
	var zIndexOffset = mlt_RECENT_POS_zIndexOffset;
	var fillColor = isRecent ? mlt_color4Recent : mlt_color4NotRecent;
	if (posType == mlt_HOME_POS) {
		ringColor = mlt_infoTableHdrColor4HomePosition;
		zIndexOffset = mlt_HOME_POS_zIndexOffset;
	} else if (posType == mlt_START_POS) {
		ringColor = mlt_infoTableHdrColor4StartPosition;
		zIndexOffset = mlt_START_POS_zIndexOffset;
	} else if (posType == mlt_RECENT_POS) {
		ringColor = mlt_infoTableHdrColor4RecentPosition;
		zIndexOffset = mlt_RECENT_POS_zIndexOffset;
	} else if (posType == mlt_MESSAGE_POS) {
		ringColor = mlt_infoTableHdrColor4Message;
		zIndexOffset = mlt_MESSAGE_POS_zIndexOffset;
	} else if (posType == mlt_EMERGENCY_SIGNAL_ACTIVATED_POS) {
		ringColor = mlt_infoTableHdrColor4EmergencySignalActivated;
		zIndexOffset = mlt_EMERGENCY_SIGNAL_ACTIVATED_POS_zIndexOffset;
	} else if (posType == mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS) {
		ringColor = mlt_infoTableHdrColor4EmergencySignalDeactivated;
		zIndexOffset = mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS_zIndexOffset;
	}
	if (isRecent) {
		zIndexOffset = Math.max(zIndexOffset, mlt_RECENT_POS_zIndexOffset);
	}
	var circleMarker = new L.CircleMarker(
		[lat, lon], {
			stroke: true,
		 	 color: ringColor,
			 weight: 2, // set outer ring size.
			 opacity: 0.8,
			 fill: true,
			 fillColor: fillColor,
			 fillOpacity: 0.8,
			 zIndexOffset: zIndexOffset,
			 //dashArray: "5, 5"
		 }
	);
	circleMarker.setRadius(4); // set overall size.
	if (popup != null) {
		circleMarker.bindPopup(popup);
	}
	return circleMarker;
}

function mlt_addCircleMarkerToMap(map, lat, lon, posType, isRecent, popup) {
	var circleMarker = mlt_createCircleMarker(lat, lon, posType, isRecent, popup);
	map.addLayer(circleMarker);
	return circleMarker;
}

function mlt_addCircleMarkerToMarkersAndMap(markers, map, lat, lon, posType, isRecent, popup) {
	var circleMarker = mlt_addCircleMarkerToMap(map, lat, lon, posType, isRecent, popup);
	markers.push(circleMarker);
	return circleMarker;
}