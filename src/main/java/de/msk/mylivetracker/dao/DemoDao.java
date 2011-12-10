package de.msk.mylivetracker.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.demo.DemoPositionVo;
import de.msk.mylivetracker.domain.demo.DemoTrackVo;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * DemoDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoDao extends SqlMapClientDaoSupport implements IDemoDao {

	private static final String SQL_INSERT_DEMO_TRACK_RECORD = "DemoTrackVo.insertDemoTrackRecord";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IDemoDao#getDemoTrack(java.lang.String)
	 */
	@Override
	public DemoTrackVo getDemoTrack(String trackId) {
		DemoTrackVo demoTrack = (DemoTrackVo)
			this.getSqlMapClientTemplate().queryForObject(
				"DemoTrackVo.getDemoTrackByTrackId", trackId);
		return demoTrack;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IDemoDao#insertTrackAsDemoTrack(de.msk.mylivetracker.domain.track.TrackVo)
	 */
	@Override
	public int insertTrackAsDemoTrack(TrackVo track) {
		int count = 0;
		if (track == null) return count;
		
		List<PositionVo> positions = track.getPositions();
				
		Map<String, MessageVo> messages = new HashMap<String, MessageVo>();
		for (MessageVo message : track.getMessages()) {
			messages.put(message.getPosition().getPositionId(), message);
		}
		
		Map<String, SenderStateVo> senderStates = new HashMap<String, SenderStateVo>();
		for (SenderStateVo senderState : track.getSenderStates()) {
			senderStates.put(senderState.getPosition().getPositionId(), senderState);
		}
		
		Map<String, MobNwCellVo> mobNwCells = new HashMap<String, MobNwCellVo>();
		for (MobNwCellVo mobNwCell : track.getMobNwCells()) {
			mobNwCells.put(mobNwCell.getPosition().getPositionId(), mobNwCell);
		}
		
		Map<String, CardiacFunctionVo> cardiacFunctions = new HashMap<String, CardiacFunctionVo>();
		for (CardiacFunctionVo cardiacFunction : track.getCardiacFunctions()) {
			cardiacFunctions.put(cardiacFunction.getPosition().getPositionId(), cardiacFunction);
		}
		
		long started = track.getTimestamps().getFirstPositionReceived().getAsMSecs();
		for (PositionVo pos : positions) {
			long offset = (pos.getTimeReceived().getAsMSecs() - started) / 1000;
			DemoPositionVo rec = new DemoPositionVo(track.getTrackId(), track.getName());
			rec.setOffsetInSeconds(offset);
			// position.
			rec.setLatitudeInDecimal(pos.getLatitudeInDecimal());
			rec.setLongitudeInDecimal(pos.getLongitudeInDecimal());
			rec.setAltitudeInMtr(pos.getAltitudeInMtr());
			rec.setAddress(pos.getAddress());
			// message.
			MessageVo message = messages.get(pos.getPositionId());
			if (message != null) {
				rec.setMessage(message.getContent());
			}
			// sender state.
			SenderStateVo senderState = senderStates.get(pos.getPositionId());
			if (senderState != null) {
				rec.setSenderState(senderState.getState());
			}
			// mobile network cell.
			MobNwCellVo mobNwCell = mobNwCells.get(pos.getPositionId());
			if (mobNwCell != null) {
				rec.setMobileCountryCode(mobNwCell.getMobileCountryCode());
				rec.setMobileNetworkName(mobNwCell.getNetworkName());
				rec.setMobileNetworkCode(mobNwCell.getMobileNetworkCode());
				rec.setMobileLocalAreaCode(mobNwCell.getLocalAreaCode());
				rec.setMobileCellId(mobNwCell.getCellId());
			}
			// cardiacFunction.
			CardiacFunctionVo cardiacFunction = cardiacFunctions.get(pos.getPositionId());
			if (cardiacFunction != null) {
				rec.setHeartrateInBpm(cardiacFunction.getHeartrateInBpm());
				rec.setHeartrateMinInBpm(cardiacFunction.getHeartrateMinInBpm());
				rec.setHeartrateMaxInBpm(cardiacFunction.getHeartrateMaxInBpm());
				rec.setHeartrateAvgInBpm(cardiacFunction.getHeartrateAvgInBpm());
			}
			this.getSqlMapClientTemplate().insert(SQL_INSERT_DEMO_TRACK_RECORD, rec);
			count++;
		}
		return count;
	}	
}
