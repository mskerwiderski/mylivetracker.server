/*
 * L.Control.ZoomFS - default Leaflet.Zoom control with an added fullscreen button
 * built to work with Leaflet version 0.4
 * https://github.com/elidupuis/leaflet.zoomfs
 */
L.Control.ZoomFS = L.Control.Zoom.extend({
	includes: L.Mixin.Events,
	onAdd: function (map) {
		var className = 'leaflet-control-zoom';
		this._container = L.DomUtil.create('div', className);

		this._map = map;
		this._isFullscreen = false;
		this._isAutoZoomOn = true;
		this._createButton('Auto Zoom', 'leaflet-control-autozoom-on', this._container, this.autozoom, this);
		this._createButton('Full Screen', 'leaflet-control-fullscreen', this._container, this.fullscreen, this);
		// needs to be updated on next Leaflet release according to https://github.com/CloudMade/Leaflet/commit/0bdc48a86442d2287dc3a1085f956e60c2af4975#L1L6
		this._createButton('Zoom in', className + '-in', this._container, map.zoomIn, map);
		this._createButton('Zoom out', className + '-out', this._container, map.zoomOut, map);

		return this._container;
	},
	autozoom: function() {
	    // call appropriate internal function
		if (this._isAutoZoomOn) {
			this._autoZoomOff();
		} else {
			this._autoZoomOn();
		};
	},
	_autoZoomOff: function() {
		//console.log("btn:" + this._container.childNodes[0].className);
		var autoZoomButton = this._container.childNodes[0];
		L.DomUtil.removeClass(autoZoomButton, "leaflet-control-autozoom-on");
		L.DomUtil.addClass(autoZoomButton, 'leaflet-control-autozoom-off');
		this._isAutoZoomOn = false;
		// fire fullscreen event on map
		this._map.fire('autoZoomOff');
	},
	_autoZoomOn: function(){
		//console.log("btn:" + this._container.childNodes[0].className);
		var autoZoomButton = this._container.childNodes[0];
		L.DomUtil.removeClass(autoZoomButton, "leaflet-control-autozoom-off");
		L.DomUtil.addClass(autoZoomButton, 'leaflet-control-autozoom-on');
		this._isAutoZoomOn = true;
	    // fire fullscreen event
		this._map.fire('autoZoomOn');
	 },	
	 fullscreen: function() {
		 // call appropriate internal function
		 if (!this._isFullscreen) {
			 this._enterFullScreen();
		 } else{
			 this._exitFullScreen();
		 };
		 // force internal resize
		 this._map.invalidateSize();
	 },
	 _enterFullScreen: function() {
		 var container = this._map._container;
		 // apply our fullscreen settings
		 container.style.position = 'fixed';
		 container.style.left = 0;
		 container.style.top = 0;
		 container.style.width = '100%';
		 container.style.height = '100%';
		 // store state
		 L.DomUtil.addClass(container, 'leaflet-fullscreen');
		 this._isFullscreen = true;
		 // add ESC listener
		 L.DomEvent.addListener(document, 'keyup', this._onKeyUp, this);
		 // fire fullscreen event on map
		 this._map.fire('enterFullscreen');
	 },
	 _exitFullScreen: function() {
		 var container = this._map._container;
		 // update state
		 L.DomUtil.removeClass(container, 'leaflet-fullscreen');
		 this._isFullscreen = false;
		 // remove fullscreen style; make sure we're still position relative for Leaflet core.
		 container.removeAttribute('style');
		 // re-apply position:relative; if user does not have it.
		 var position = L.DomUtil.getStyle(container, 'position');
		 if (position !== 'absolute' && position !== 'relative') {
			 container.style.position = 'relative';
		 }
		 // remove ESC listener
		 L.DomEvent.removeListener(document, 'keyup', this._onKeyUp);
		 // fire fullscreen event
		 this._map.fire('exitFullscreen');
	 },
	 _onKeyUp: function(e) {
		 if (!e) var e = window.event;
		 if (e.keyCode === 27 && this._isFullscreen === true) {
			 this._exitFullScreen();
		 }
	 }
});