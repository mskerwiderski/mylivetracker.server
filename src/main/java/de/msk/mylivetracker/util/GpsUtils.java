package de.msk.mylivetracker.util;

import java.util.List;

import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * GpsUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class GpsUtils {

	public static Double distanceInMtr(TrackVo track) {
		Double dist = 0d;
		List<PositionVo> positions = track.getPositions();
		if (positions.size() > 1) {
			for (int i=1; i < positions.size(); i++) {
				dist += distanceInMtr(positions.get(i), positions.get(i-1));
			}
		}
		return dist;
	}
	
	public static Double distanceInMtr(PositionVo pos1, PositionVo pos2) {
		if ((pos1 == null) || (pos2 == null) || 
			(pos1.getLatitudeInDecimal() == null) || (pos1.getLongitudeInDecimal() == null) ||
			(pos2.getLatitudeInDecimal() == null) || (pos2.getLongitudeInDecimal() == null)) {
			return null;
		}
		Double earthRadiusInMtr = 6371000d;
		Double deltaLatitude = (pos2.getLatitudeInDecimal() - pos1.getLatitudeInDecimal()) * Math.PI / 180;
		Double deltaLongitude = (pos2.getLongitudeInDecimal() - pos1.getLongitudeInDecimal()) * Math.PI / 180;
		Double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) + Math.cos(pos1.getLatitudeInDecimal() * Math.PI / 180) * 
			Math.cos(pos2.getLatitudeInDecimal() * Math.PI / 180) * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = earthRadiusInMtr * c;
		return d;
	}
}
