/*
 * msk - based on:
 * o L.Control.ZoomFS - https://github.com/elidupuis/leaflet.zoomfs
 * 
 * added:
 * o appends automatically to zoom control if exists.
 * o added symbols control.
 * 
 */
L.Control.FullscreenAndSymbols = L.Control.Zoom.extend({
	includes: L.Mixin.Events,
	onAdd: function (map) {
		var zoomName = 'leaflet-control-zoom',
			barName = 'leaflet-bar',
			partName = barName + '-part',
			container = L.DomUtil.create('div', zoomName + ' ' + barName);

		this._map = map;
		this._isFullscreen = false;

		this._zoomFullScreenButton = this._createButton('','Full Screen',
			'leaflet-control-fullscreen ' +
			partName + ' ' +
			partName + '-top',
			container, this.fullscreen, this);

		this._zoomInButton = this._createButton('+', 'Zoom in',
			zoomName + '-in ' +
			partName + ' ',
			container, this._zoomIn,  this);

		this._zoomOutButton = this._createButton('-', 'Zoom out',
			zoomName + '-out ' +
			partName + ' ',
			container, this._zoomOut, this);

		this._zoomSymbolsButton = this._createButton('', 'Symbols',
			'leaflet-control-symbolson ' +
			partName + ' ' +
			partName + '-bottom',
			container, this.symbols, this);

		map.on('zoomend zoomlevelschange', this._updateDisabled, this);
		this._isSymbolsOn = true;
		
		return container;

	},
	symbols: function() {
		if (this._isSymbolsOn) {
			this._symbolsOff();
		} else {
			this._symbolsOn();
		};
	},
	_symbolsOff: function() {
		var symbolsButton = this._container.childNodes[3];
		L.DomUtil.removeClass(symbolsButton, "leaflet-control-symbolson");
		L.DomUtil.addClass(symbolsButton, 'leaflet-control-symbolsoff');
		this._isSymbolsOn = false;
		this._map.fire('symbolsOff');
	},
	_symbolsOn: function(){
		var symbolsButton = this._container.childNodes[3];
		//alert(symbolsButton);
		L.DomUtil.removeClass(symbolsButton, "leaflet-control-symbolsoff");
		L.DomUtil.addClass(symbolsButton, 'leaflet-control-symbolson');
		this._isSymbolsOn = true;
		this._map.fire('symbolsOn');
	},	
	fullscreen: function() {
		// call appropriate internal function
		if (!this._isFullscreen) {
			this._enterFullScreen();
		} else {
			this._exitFullScreen();
		}

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
		if (!e) e = window.event;
		if (e.keyCode === 27 && this._isFullscreen === true) {
			this._exitFullScreen();
		}
	}
});