
var mlt_infoTableHdrFontColor = "#FFFFFF";
var mlt_infoTableHdrColor4HomePosition = "#529647";
var mlt_infoTableHdrColor4StartPosition = "#529647";
var mlt_infoTableHdrColor4RecentPosition = "#529647";
var mlt_infoTableHdrColor4Message = "#477c94";
var mlt_infoTableHdrColor4EmergencySignalActivated = "#f5454d";
var mlt_infoTableHdrColor4EmergencySignalDeactivated = "#c4bc20";
var mlt_color4Recent = "#ee7b7b";
var mlt_color4NotRecent = "#ebebeb";

var mlt_HOME_POS = "homePos";
var mlt_START_POS = "startPos";
var mlt_RECENT_POS = "recentPos";
var mlt_MESSAGE_POS = "messagePos";
var mlt_EMERGENCY_SIGNAL_ACTIVATED_POS = "emergencySignalActivatedPos";
var mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS = "emergencySignalDeactivatedPos";

function mlt_createCircleMarker(lat, lon, posType, isRecent) {
	var ringColor = mlt_infoTableHdrColor4RecentPosition;
	var fillColor = isRecent ? mlt_color4Recent : mlt_color4NotRecent;
	if (posType == mlt_HOME_POS) {
		ringColor = mlt_infoTableHdrColor4HomePosition;
	} else if (posType == mlt_START_POS) {
		ringColor = mlt_infoTableHdrColor4StartPosition;
	} else if (posType == mlt_RECENT_POS) {
		ringColor = mlt_infoTableHdrColor4RecentPosition;
	} else if (posType == mlt_MESSAGE_POS) {
		ringColor = mlt_infoTableHdrColor4Message;
	} else if (posType == mlt_EMERGENCY_SIGNAL_ACTIVATED_POS) {
		ringColor = mlt_infoTableHdrColor4EmergencySignalActivated;
	} else if (posType == mlt_EMERGENCY_SIGNAL_DEACTIVATED_POS) {
		ringColor = mlt_infoTableHdrColor4EmergencySignalDeactivated;
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
			 //dashArray: "5, 5"
		 }
	);
	circleMarker.setRadius(4); // set overall size.
	return circleMarker;
}

function mlt_addCircleMarkerToMap(map, lat, lon, posType, isRecent) {
	var circleMarker = mlt_createCircleMarker(lat, lon, posType, isRecent);
	map.addLayer(circleMarker);
	return circleMarker;
}

function mlt_addCircleMarkerToMarkersAndMap(markers, map, lat, lon, posType, isRecent) {
	var circleMarker = mlt_addCircleMarkerToMap(map, lat, lon, posType, isRecent);
	markers.push(circleMarker);
	return circleMarker;
}