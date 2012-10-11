
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
		attributionControl: true,
		zoomControl: true,
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