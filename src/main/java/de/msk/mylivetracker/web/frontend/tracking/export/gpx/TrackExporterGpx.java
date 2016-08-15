package de.msk.mylivetracker.web.frontend.tracking.export.gpx;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import de.msk.mylivetracker.Global;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.commons.util.datetime.DateTime.DateTimeStruct;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.web.frontend.tracking.export.ITrackExporter;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.GpxType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.LinkType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.MetadataType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.ObjectFactory;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.PersonType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.TrkType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.TrksegType;
import de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated.WptType;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * TrackExporterGpx.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackExporterGpx implements ITrackExporter<Object> {

	private JAXBContext jaxbContext;
	
	private static XMLGregorianCalendar getUtcTimeStr(Calendar calendar, DateTime dateTime) {
		DateTimeStruct struct = 
			dateTime.getDateTimeStruct(TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC));
		return XMLGregorianCalendarImpl.
			createDateTime(
				struct.year, 
				struct.month,
				struct.day,
				struct.hour,
				struct.minute,
				struct.second,
				struct.millisecond, 
				DatatypeConstants.FIELD_UNDEFINED);		
	}
	
	@Override
	public ByteArrayOutputStream convert(HttpServletRequest request,
		TrackVo track, Object addDsc) throws Exception {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC));
		
		XMLGregorianCalendar timestamp =  getUtcTimeStr(calendar, new DateTime());
		
		ObjectFactory of = new ObjectFactory();
		GpxType gpx = of.createGpxType();		
		gpx.setVersion("1.1");
		gpx.setCreator(Global.getAppIdAndVersion());
		
		MetadataType meta = of.createMetadataType();
		meta.setTime(timestamp);

		PersonType person = of.createPersonType();
		person.setName(Global.getAppId());
		
		LinkType link = of.createLinkType();
		link.setHref(Global.getPortalLink());
		link.setText(Global.getAppId());
		person.setLink(link);
		
		meta.setAuthor(person);
		
		gpx.setMetadata(meta);
		
		TrkType gpxTrack = of.createTrkType();
		gpxTrack.setName(
			FmtUtils.getStrAsCompatibleFilename(
				track.getName(), track.getTrackId()));			
		gpxTrack.setCmt("UserId: " + track.getUserId() + 
			", SenderId: " + track.getSenderId() + 
			", gpx created: " + timestamp.toXMLFormat());
		
		TrksegType trackseg = of.createTrksegType();							
		for (PositionVo pos : track.getPositions())  {
			WptType wp = of.createWptType();			
			wp.setTime(getUtcTimeStr(calendar, pos.getTimeReceived()));				
			wp.setLat(new BigDecimal(pos.getLatitudeInDecimal().toString()));
			wp.setLon(new BigDecimal(pos.getLongitudeInDecimal().toString()));
			if (pos.getAltitudeInMtr() != null) {
				wp.setEle(new BigDecimal(pos.getAltitudeInMtr().toString()));
			}
			trackseg.getTrkpt().add(wp);			
		}
				
		gpxTrack.getTrkseg().add(trackseg);
		gpx.getTrk().add(gpxTrack);
		
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		try {
			JAXBElement<GpxType> gpxRoot =
                of.createGpx(gpx);            
            javax.xml.bind.Marshaller m = this.jaxbContext.createMarshaller();            
		    m.marshal(gpxRoot, ostr);
        } catch (JAXBException e){
        	e.printStackTrace();
        }
		return ostr;
	}

	/**
	 * @return the jaxbContext
	 */
	public JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	/**
	 * @param jaxbContext the jaxbContext to set
	 */
	public void setJaxbContext(JAXBContext jaxbContext) {
		this.jaxbContext = jaxbContext;
	}	
}
