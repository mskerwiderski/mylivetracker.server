package de.msk.mylivetracker.web.frontend.tracking.export.kml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LookAt;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.domain.TicketVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ITicketService;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc;
import de.msk.mylivetracker.web.frontend.tracking.export.ITrackExporter;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.WebUtils;
import de.msk.mylivetracker.web.util.request.ReqParamValues;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * TrackExporterKml.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackExporterKml implements ITrackExporter<UserAndRoleDsc> {
	
	private static final Log log = LogFactory.getLog(TrackExporterKml.class);
	                            
	private boolean onlyStaticExport;
	private IApplicationService applicationService;
	private ITicketService ticketService;
	
	private static final String STYLE_MARKER_MESSAGE = "message-marker-style";
	private static final String STYLE_MARKER_STARTPOS = "startpos-marker-style";
	private static final String STYLE_MARKER_RECPOS = "recpos-marker-style";
	private static final String STYLE_ROUTE = "route-style";	
		
	private String getUpdateUrl(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track) {		
		String applicationBaseUrl = 
			this.applicationService.getApplicationBaseUrl();
		
		String exportUrl = 
			ReqUrlStr.create(applicationBaseUrl)
			.addUrlPath(UrlUtils.URL_TRACK_AS_GOOGLE_EARTH_CTRL)
			.addParamValues(
				ReqParamValues.create()
				.add(AbstractTrackingCtrl.PARAM_REQ_TYPE, 
					AbstractTrackingCtrl.RequestType.binary.toString())
				.add(AbstractTrackingCtrl.PARAM_USER_ID, userAndRoleDsc.user.getUserId())
				.add(AbstractTrackingCtrl.PARAM_TICKET_ID, this.ticketService.createTicket(
					TicketVo.Type.UrlTicket,
					userAndRoleDsc.user.getUserId(),	
					userAndRoleDsc.role))
				.add(AbstractTrackingCtrl.PARAM_TRACK_ID, track.getTrackId())	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_LIVE, false)	
				.add(request, AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS)
				.add(request, AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE))
			.toString();
					 					
		log.debug("exportUrl='" + exportUrl + "'");		
		return exportUrl;
	}
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.export.ITrackExporter#convert(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.domain.track.TrackVo, java.lang.Object)
	 */
	@Override
	public ByteArrayOutputStream convert(HttpServletRequest request,
		TrackVo track, UserAndRoleDsc userAndRoleDsc) throws Exception {
		String applicationBaseUrl = 
			this.applicationService.getApplicationBaseUrl();
		
		final Kml kml = KmlFactory.createKml();
		
		UserOptionsVo options = userAndRoleDsc.user.getOptions();
		
		String routeColor = htmlColorToGoogleColor(options.getTrackRouteColor());
		Integer routeWidth = options.getTrackRouteWidth();
				
		String description = "";
		
		Boolean live = 
			AbstractTrackingCtrl.PARAM_TRACKING_LIVE.getValueFromReq(request);
		if (this.onlyStaticExport) {
			live = false;
		}
		Integer keepRecPos = 
			AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS.getValueFromReq(request, 0);
		Integer updateInterval = 
			AbstractTrackingCtrl.PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS.getValueFromReq(request);
		Integer flyToMode =
			AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE.getValueFromReq(request);
		
		if (!live) {
			description += WebUtils.getMessage(request, 
				"kml.track.positions.nonlive", track.getCountPositions());			
		} else {
			if (keepRecPos.intValue() == 0) {
				description += WebUtils.getMessage(request, 
					"kml.track.positions.live.all", 
					updateInterval);
			} else {
				description += WebUtils.getMessage(request, 
					"kml.track.positions.live.recent", 
					keepRecPos, updateInterval);
			}			
		}
			
		final Document document = kml.createAndSetDocument()
			.withName(FmtUtils.getStrAsCompatibleFilename(
				track.getName(), 
				track.getTrackId()))
			.withDescription(description)
			.withOpen(true);
			
		this.createAndSetIconStyle(applicationBaseUrl, 
			document, STYLE_MARKER_MESSAGE, "message.png");
		this.createAndSetIconStyle(applicationBaseUrl, 
			document, STYLE_MARKER_STARTPOS, "start.png");
		this.createAndSetIconStyle(applicationBaseUrl, 
			document, STYLE_MARKER_RECPOS, track.getSenderSymbolId() + ".png");
						
		if (live) {
			final NetworkLink networklink = document.createAndAddNetworkLink()
				.withName(track.getName() + " - " + 
					WebUtils.getMessage(request, "kml.livetracking"))				
				.withRefreshVisibility(true);
			boolean flyToView = false;
			if (flyToMode == StatusParamsVo.TrackingFlyToMode.FlyToView.ordinal()) {
				flyToView = true;
			}
			networklink.setFlyToView(flyToView);	
			
			networklink.createAndSetLink()
				.withHref(this.getUpdateUrl(request, userAndRoleDsc, track))
				.withRefreshMode(RefreshMode.ON_INTERVAL)
				.withRefreshInterval(Double.valueOf(updateInterval));
			networklink.setRefreshVisibility(true);
			
			PositionVo recentPosition = track.getRecentPosition();
			if (!flyToView && (track.getRecentPosition() != null)) {
				LookAt lookAt = networklink.createAndSetLookAt();
				lookAt.setLatitude(recentPosition.getLatitudeInDecimal());
				lookAt.setLongitude(recentPosition.getLongitudeInDecimal());
				if (recentPosition.getAltitudeInMtr() != null) {
					lookAt.setAltitude(recentPosition.getAltitudeInMtr());
					lookAt.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
				}				
				lookAt.setRange(500d); // 500 meters above position.
				lookAt.setTilt(0d); // look directly from above.				
			}
		} else {		
			addRoute(request, track, document, routeColor, routeWidth);
			UserWithoutRoleVo user = userAndRoleDsc.user;
			
			if ((keepRecPos.intValue() == 0) && 
				!track.getPositions().isEmpty()){
				String startPosTimeRcvdStr = DateTimeUtils.getDateTimeStr4UserRep(
					user, track.getStartPosition().getTimeReceived());
				addPositionMessage(STYLE_MARKER_STARTPOS,
					WebUtils.getMessage(request, "kml.position.start"), 
					WebUtils.getMessage(request, "kml.position.start.received") + " " + 
						startPosTimeRcvdStr + ".", 
					track.getStartPosition(), document);
			} else if (track.getPositions().size() > 1) {
				String eldestPosTimeRcvdStr = DateTimeUtils.getDateTimeStr4UserRep(
					user, track.getEldestPosition().getTimeReceived());
				addPositionMessage(STYLE_MARKER_RECPOS,
					WebUtils.getMessage(request, "kml.position.eldest"), 
					WebUtils.getMessage(request, "kml.position.eldest.received") + " " +
						eldestPosTimeRcvdStr + ".", 
					track.getEldestPosition(), document);
			}
			
			for (MessageVo message : track.getMessages()) {
				if (message.getPosition() != null) {
					String messageTimeRcvdStr = DateTimeUtils.getDateTimeStr4UserRep(
						user, message.getTimeReceived());
					addPositionMessage(STYLE_MARKER_MESSAGE,
						message.getContent(), 
						WebUtils.getMessage(request, "kml.message.received") + " " +
						messageTimeRcvdStr + ".", 
						message.getPosition(), document);
				}
			}					
			
			if (!track.getPositions().isEmpty()) {
				PositionVo currPosition = track.getRecentPosition();
				String curPosTimeRcvdStr = DateTimeUtils.getDateTimeStr4UserRep(
					user, currPosition.getTimeReceived());
				String currPosDsc = 
					WebUtils.getMessage(request, "kml.position.recent.received") + 
					" " + curPosTimeRcvdStr;
				String address = currPosition.getAddress();
				if (!StringUtils.isEmpty(address)) {
					currPosDsc += ", " +
						WebUtils.getMessage(request, "kml.address.at", "", address);
				}
				currPosDsc += ".";
				
				addPositionMessage(STYLE_MARKER_RECPOS,
					WebUtils.getMessage(request, "kml.position.recent"), 
					currPosDsc,	currPosition, document);
			}
		}
		
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		kml.marshal(ostr);
		
		if (!this.onlyStaticExport) { 
			ostr = compressToKmz(
				FmtUtils.getStrAsCompatibleFilename(
					track.getName(), track.getTrackId()), ostr);
		}
	
		return ostr;
	}	
		
	private void createAndSetIconStyle(
		String applicationBaseUrl, Document document, 
		String styleId, String iconName) {
		String iconUrl = ReqUrlStr.create(
			applicationBaseUrl, "img", "map", iconName).toString();
		Style style = document.createAndAddStyle().withId(styleId);		
		style.createAndSetIconStyle()
			.withColorMode(ColorMode.NORMAL)			
			.withScale(1.0d).createAndSetIcon()
			.withHref(iconUrl);	
	}
	
	private void addPositionMessage(String styleName,
		String messageName, String messageDescription,
		PositionVo position, Document document) {
		
		Placemark placemark = document.createAndAddPlacemark()
			.withName(messageName)
			.withDescription(messageDescription)
			.withVisibility(true);
		placemark.setStyleUrl("#" + styleName);
		
		Point point = placemark.createAndSetPoint();
		point.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
		
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.add(position2Coordinate(position));			
		point.setCoordinates(coordinates);
	}		
	
	private void addRoute(HttpServletRequest request, 
		TrackVo track, Document document, 
		String trackRouteColor, Integer trackRouteWidth) {	
		final Style style = document.createAndAddStyle().withId(STYLE_ROUTE);
		
		style.createAndSetLineStyle()
			.withColorMode(ColorMode.NORMAL)
			.withColor(trackRouteColor)
			.withWidth(Double.valueOf(trackRouteWidth));
		
		Placemark placemark = document.createAndAddPlacemark()
		   .withName(WebUtils.getMessage(request, "kml.route"))		   
		   .withVisibility(true);
		placemark.setStyleUrl("#" + STYLE_ROUTE);
				
		LineString lineString = placemark.createAndSetLineString();
		lineString.setTessellate(true);
		lineString.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);		
		
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (PositionVo pos : track.getPositions()) {			
			coordinates.add(position2Coordinate(pos));
		}
		lineString.setCoordinates(coordinates);						
	}	
	
	private static String htmlColorToGoogleColor(String htmlColor) {
		String googleColor = "ff";
		googleColor += StringUtils.substring(htmlColor, 4, 6);
		googleColor += StringUtils.substring(htmlColor, 2, 4);
		googleColor += StringUtils.substring(htmlColor, 0, 2);
		return StringUtils.lowerCase(googleColor);
	}
	
	private Coordinate position2Coordinate(PositionVo position) {
		Coordinate coordinate = null;
		if (position.getAltitudeInMtr() != null) {
			coordinate = new Coordinate(
				position.getLongitudeInDecimal(), 
				position.getLatitudeInDecimal(),
				position.getAltitudeInMtr());
		} else {
			coordinate = new Coordinate(
				position.getLongitudeInDecimal(), 
				position.getLatitudeInDecimal());
		}
		return coordinate;
	}
	
	private static ByteArrayOutputStream compressToKmz(
		String trackname, ByteArrayOutputStream ostr) throws IOException {  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		
		ZipOutputStream zipfile = new ZipOutputStream(bos);  
		zipfile.setLevel(9);
		
		ZipEntry zipentry = new ZipEntry(trackname + ".kml");  			
		zipfile.putNextEntry(zipentry);  
		zipfile.write(ostr.toByteArray());  		 
		zipfile.close();  
		
		bos.close();
		
		return bos;  
	}

	/**
	 * @return the onlyStaticExport
	 */
	public boolean isOnlyStaticExport() {
		return onlyStaticExport;
	}

	/**
	 * @param onlyStaticExport the onlyStaticExport to set
	 */
	public void setOnlyStaticExport(boolean onlyStaticExport) {
		this.onlyStaticExport = onlyStaticExport;
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the ticketService
	 */
	public ITicketService getTicketService() {
		return ticketService;
	}

	/**
	 * @param ticketService the ticketService to set
	 */
	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}	
}
