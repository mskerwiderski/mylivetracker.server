
var mlt_infoTableHdrFontColor = "#FFFFFF";
var mlt_infoTableHdrColor4HomePosition = "#529647";
var mlt_infoTableHdrColor4StartPosition = "#529647";
var mlt_infoTableHdrColor4RecentPosition = "#529647";
var mlt_infoTableHdrColor4Message = "#477c94";
var mlt_infoTableHdrColor4EmergencySignalActivated = "#f34648";
var mlt_infoTableHdrColor4EmergencySignalDeactivated = "#c4bc20";
	
function mlt_createCircleMarker(lat, lon) {
	var circleMarker = new L.CircleMarker(
		[lat, lon], 
		{stroke: true,
	 	 color: '#529647',
		 weight: 2,
		 opacity: 0.8,
		 fill: true,
		 fillColor: '#f34648',
		 fillOpacity: 0.8}
	);
	circleMarker.setRadius(4);
	return circleMarker;
}

function mlt_addCircleMarkerToMap(map, lat, lon) {
	map.addLayer(mlt_createCircleMarker(lat, lon));
}

function mlt_addCircleMarkerToMarkersAndMap(markers, map, lat, lon) {
	var circleMarker = mlt_createCircleMarker(lat, lon);
	markers.push(circleMarker);
	map.addLayer(circleMarker);
}