package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MapsUsedVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-09-14 initial 
 * 
 */
public class MapsUsedVo implements Cloneable, Serializable {

	private static final long serialVersionUID = 4241899850562024489L;

	private static final int MAPS_USED_SIZE = 32;
	
	
	
	private static final String SEP = ",";
	private static final int DEF_MAP_ID_DEFAULT = 0;
	
	private boolean[] mapsUsed;
	private int defMapId = DEF_MAP_ID_DEFAULT;
	
//	public int getMapsUsedSize() {
//		return MAPS_USED_SIZE;
//	}
	
	public MapsUsedVo(String mapsUsedStr) {
		if (StringUtils.isEmpty(mapsUsedStr)) {
			throw new IllegalArgumentException("mapsUsedStr must not be empty");
		} 
		String[] items = StringUtils.split(mapsUsedStr, SEP);
		if ((items == null) || (items.length != 2)) {
			throw new IllegalArgumentException("invalid mapUsedStr=" + mapsUsedStr);			
		}
		try {
			this.defMapId = Integer.valueOf(items[0]);
		} catch (Exception e) {
			this.defMapId = 0;
		}
		if ((this.defMapId < 0) || (this.defMapId >= MAPS_USED_SIZE)) {
			throw new IllegalArgumentException("invalid defMapId=" + this.defMapId);
		}
		mapsUsed = new boolean[MAPS_USED_SIZE];
		for (int mapId=0; mapId < items[1].length(); mapId++) {
			mapsUsed[mapId] = BooleanUtils.toBoolean(String.valueOf(items[1].charAt(mapId)), "1", "0");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new MapsUsedVo(this.toString());
	}

	public MapsUsedVo copy() {
		MapsUsedVo mapsUsed = null;
		try {
			mapsUsed = (MapsUsedVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return mapsUsed;
	}
	
	public void setMapStatus(int mapId, boolean status) {
		if ((mapId < 0) || (mapId >= MAPS_USED_SIZE)) {
			throw new IllegalArgumentException("invalid mapId=" + mapId);
		}
		mapsUsed[mapId] = status;
	}
	
	public boolean getMapStatus(int mapId) {
		if ((mapId < 0) || (mapId >= MAPS_USED_SIZE)) {
			throw new IllegalArgumentException("invalid mapId=" + mapId);
		}
		return mapsUsed[mapId];
	}

	public boolean isDefMapIdValid() {
		return this.getMapStatus(defMapId);
	}
	
	public int getDefMapId() {
		return defMapId;
	}

	public void setDefMapId(int defMapId) {
		if ((defMapId < 0) || (defMapId >= MAPS_USED_SIZE)) {
			throw new IllegalArgumentException("invalid defMapId=" + defMapId);
		}
		this.defMapId = defMapId;
	}

	public boolean[] getMapsUsed() {
		return mapsUsed;
	}

	public String getMapsUsedStr() {
		StringBuffer buf = new StringBuffer(MAPS_USED_SIZE);
		for (int mapId=0; mapId < MAPS_USED_SIZE; mapId++) {
			buf.append(BooleanUtils.toInteger(mapsUsed[mapId], 1, 0, 0));
		}
		return buf.toString();
	}
	
	public void setMapsUsed(boolean[] mapsUsed) {
		this.mapsUsed = mapsUsed;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(MAPS_USED_SIZE * 2);
		buf.append(this.defMapId).append(",");
		buf.append(this.getMapsUsedStr());
		return buf.toString();
	}
}
