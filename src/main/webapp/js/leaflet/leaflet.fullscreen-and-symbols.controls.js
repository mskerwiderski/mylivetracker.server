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
		var containerClass = 'leaflet-control-zoom', 
		    classNameFullscreen,
		    classNameSymbols,
		    container;
		
		if (map.zoomControl) {
			container = map.zoomControl._container;
			classNameFullscreen = '-fullscreen last';
			classNameSymbols = '-symbols on';
		} else {
			container = L.DomUtil.create('div', containerClass);
			classNameFullscreen = '-fullscreen';
			classNameSymbols = '-symbols on';
		}
		
		this._isFullscreen = false;
		this._isSymbolsOn = true;
		this._symbolsButtonPosition = 3;
		this._createButton('Full Screen', containerClass + classNameFullscreen, container, this.fullscreen, this);
		this._createButton('Symbols', containerClass + classNameSymbols, container, this.symbols, this);
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
		var symbolsButton = this._container.childNodes[this._symbolsButtonPosition];
		L.DomUtil.removeClass(symbolsButton, "on");
		L.DomUtil.addClass(symbolsButton, 'off');
		this._isSymbolsOn = false;
		this._map.fire('symbolsOff');
	},
	_symbolsOn: function(){
		var symbolsButton = this._container.childNodes[this._symbolsButtonPosition];
		L.DomUtil.removeClass(symbolsButton, "off");
		L.DomUtil.addClass(symbolsButton, 'on');
		this._isSymbolsOn = true;
		this._map.fire('symbolsOn');
	},	
	fullscreen: function() {
		// call appropriate internal function
		if (!this._isFullscreen) {
			this._enterFullScreen();
		} else {
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