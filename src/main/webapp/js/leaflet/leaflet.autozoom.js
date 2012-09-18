var controlAutoZoomOn = true;
L.Control.AutoZoom = L.Control.extend({
	options: {
		position: 'topleft'
	},
	
	onAdd: function (map) {
		var containerClass = 'leaflet-control-zoom', className, container;
		if(map.zoomControl) {
			container = map.zoomControl._container;
			className = '-autozoom last';
		} else {
			container = L.DomUtil.create('div', containerClass);
			className = '-autozoom';
		}
		this._createButton('Auto Zoom', containerClass + className, container, this.toogleAutoZoom, map);
		return container;
	},
	
	_createButton: function (title, className, container, fn, context) {
		var link = L.DomUtil.create('a', className, container);
		link.href = '#';
		link.title = title;
		L.DomEvent
			.addListener(link, 'click', L.DomEvent.stopPropagation)
			.addListener(link, 'click', L.DomEvent.preventDefault)
			.addListener(link, 'click', fn, context);
		return link;
	},
	
	toogleAutoZoom: function () {
		controlAutoZoomOn = !controlAutoZoomOn;
		alert("toggle auto zoom: " + controlAutoZoomOn);
	},
});

