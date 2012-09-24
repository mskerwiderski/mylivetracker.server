package de.msk.mylivetracker.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.ClientInfoVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.ITrackService.DeleteTrackResult;
import de.msk.mylivetracker.service.ITrackService.TrackListResult;
import de.msk.mylivetracker.util.GpsUtils;

/**
 * TrackDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackDao extends SqlMapClientDaoSupport implements ITrackDao {

	private static final Log log = LogFactory.getLog(TrackDao.class);	
	
	// selects
	private static final String SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_MIN_TRACK = "TrackVo.getActiveTrackOfSenderAsMinTrack";
	private static final String SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_RECENT_TRACK = "TrackVo.getActiveTrackOfSenderAsRecentTrack";
	private static final String SQL_GET_TRACKS_FOR_GUEST_AS_RECENT_TRACK = "TrackVo.getTracksForGuestAsRecentTrack";
	private static final String SQL_GET_TRACKS_FOR_USER_AS_RECENT_TRACK = "TrackVo.getTracksForUserAsRecentTrack";
	private static final String SQL_GET_TRACKS_OVERALL_AS_RECENT_TRACK = "TrackVo.getTracksOverallAsRecentTrack";
	private static final String SQL_GET_TRACKS_COUNT_FOR_GUEST_AS_RECENT_TRACK = "TrackVo.getTracksCountForGuestAsRecentTrack";
	private static final String SQL_GET_TRACKS_COUNT_FOR_USER_AS_RECENT_TRACK = "TrackVo.getTracksCountForUserAsRecentTrack";
	private static final String SQL_GET_TRACKS_COUNT_OVERALL_AS_RECENT_TRACK = "TrackVo.getTracksCountOverallAsRecentTrack";
	private static final String SQL_GET_TRACK_BY_ID_AS_MIN_TRACK = "TrackVo.getTrackByIdAsMinTrack";
	private static final String SQL_GET_POSITIONS_OF_TRACK = "PositionVo.getPositionsOfTrack";
	private static final String SQL_GET_MESSAGES_OF_TRACK = "MessageVo.getMessagesOfTrack";
	private static final String SQL_GET_EMERGENCY_SIGNALS_OF_TRACK = "EmergencySignalVo.getEmergencySignalsOfTrack";
	private static final String SQL_GET_MOB_NW_CELLS_OF_TRACK = "MobNwCellVo.getMobNwCellsOfTrack";
	private static final String SQL_GET_CARDIAC_FUNCTIONS_OF_TRACK = "CardiacFunctionVo.getCardiacFunctionsOfTrack";
	private static final String SQL_GET_SENDER_STATES_OF_TRACK = "SenderStateVo.getSenderStatesOfTrack";
	private static final String SQL_GET_TRACK_BY_ID_AS_RECENT_TRACK = "TrackVo.getTrackByIdAsRecentTrack";
	private static final String SQL_GET_RECENT_TRACK_AS_MIN_TRACK = "TrackVo.getRecentTrackAsMinTrack";
	private static final String SQL_GET_RECENT_TRACK_AS_RECENT_TRACK = "TrackVo.getRecentTrackAsRecentTrack";
	private static final String SQL_GET_CLOSED_TRACK_IDS_BY_USER_ID = "TrackVo.getClosedTrackIdsByUserId";
	private static final String SQL_GET_OLD_TRACK_IDS_BY_TIMESTAMP = "TrackVo.getOldTrackIdsByTimestamp";
	private static final String SQL_GET_ONE_TRACK_ID_WITH_REMOVE_FLAG = "TrackVo.getOneTrackIdWithRemoveFlag";
		
	// update
	private static final String SQL_RENAME_TRACK = "TrackVo.renameTrack";
	private static final String SQL_PRIVATIZE_TRACK = "TrackVo.privatizeTrack";
	private static final String SQL_PUBLISH_TRACK = "TrackVo.publishTrack";
	private static final String SQL_CLOSE_ACTIVE_TRACK = "TrackVo.closeActiveTrack";	
	private static final String SQL_UPDATE_TRACK = "TrackVo.updateTrack";
	private static final String SQL_UPDATE_TRACK_HEARTBEAT = "TrackVo.updateTrackHeartbeat";	
	private static final String SQL_OPEN_TRACK = "TrackVo.openTrack";
	private static final String SQL_CLOSE_TRACK = "TrackVo.closeTrack";
	
	// insert
	private static final String SQL_INSERT_TRACK = "TrackVo.insertTrack";	
	private static final String SQL_STORE_EMERGENCY_SIGNAL = "EmergencySignalVo.storeEmergencySignal";
	private static final String SQL_STORE_SENDER_STATE = "SenderStateVo.storeSenderState";
	private static final String SQL_STORE_CARDIAC_FUNCTION = "CardiacFunctionVo.storeCardiacFunction";
	private static final String SQL_STORE_MESSAGE = "MessageVo.storeMessage";
	private static final String SQL_STORE_MOB_NW_CELL = "MobNwCellVo.storeMobNwCell";
	private static final String SQL_STORE_POSITION = "PositionVo.storePosition";
	
	// remove
	private static final String SQL_MARK_TRACK_FOR_REMOVING = "TrackVo.markTrackForRemoving";
	private static final String SQL_REMOVE_TRACK = "TrackVo.removeTrack";	
	private static final String SQL_REMOVE_POSITION = "TrackVo.removePosition";
	private static final String SQL_REMOVE_MESSAGE = "TrackVo.removeMessage";
	private static final String SQL_REMOVE_MOB_NW_CELL = "TrackVo.removeMobNwCell";	
	private static final String SQL_REMOVE_SENDER_STATE= "TrackVo.removeSenderState";
	private static final String SQL_REMOVE_CARDIAC_FUNCTION= "TrackVo.removeCardiacFunction";
	private static final String SQL_REMOVE_EMERGENCY_SIGNAL= "TrackVo.removeEmergencySignal";
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getTracksAsRecent(de.msk.mylivetracker.domain.track.TrackFilterVo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackListResult getTracksAsRecent(TrackFilterVo trackFilter) {
		Integer rawTracksCount = 0;
		List<TrackVo> rawTracks = new ArrayList<TrackVo>();		
		if (trackFilter.getUserRole() == null) {
			rawTracksCount = (Integer)this.getSqlMapClientTemplate().queryForObject(
				SQL_GET_TRACKS_COUNT_OVERALL_AS_RECENT_TRACK, trackFilter);	
			rawTracks = this.getSqlMapClientTemplate().queryForList(
				SQL_GET_TRACKS_OVERALL_AS_RECENT_TRACK, trackFilter);
		} else if (trackFilter.getUserRole().equals(UserWithRoleVo.UserRole.User) ||
			trackFilter.getUserRole().equals(UserWithRoleVo.UserRole.Admin)) {
			rawTracksCount = (Integer)this.getSqlMapClientTemplate().queryForObject(
				SQL_GET_TRACKS_COUNT_FOR_USER_AS_RECENT_TRACK, trackFilter);	
			rawTracks = this.getSqlMapClientTemplate().queryForList(
				SQL_GET_TRACKS_FOR_USER_AS_RECENT_TRACK, trackFilter);
		} else {
			rawTracksCount = (Integer)this.getSqlMapClientTemplate().queryForObject(
				SQL_GET_TRACKS_COUNT_FOR_GUEST_AS_RECENT_TRACK, trackFilter);
			rawTracks = this.getSqlMapClientTemplate().queryForList(
				SQL_GET_TRACKS_FOR_GUEST_AS_RECENT_TRACK, trackFilter);
		}		
		return new TrackListResult(rawTracksCount, rawTracks, 
			rawTracksCount > trackFilter.getMaxCountOfRecords());
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getTrackAsDetailed(java.lang.String, java.lang.Integer, boolean, boolean, boolean, boolean, boolean)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getTrackAsDetailed(String trackId, Integer cntCutPositions,
		boolean messagesIncl, boolean emergencySignalsIncl,
		boolean cardiacFunctionsIncl, boolean mobNwCellsIncl,
		boolean senderStatesIncl) {
		if (trackId == null) {
			throw new IllegalArgumentException("trackId must not be null.");
		}		
		if (cntCutPositions == null) {
			cntCutPositions = 0;
		}
		TrackVo track = this.getTrackAsRecent(trackId);
		if (track == null) {
			throw new IllegalArgumentException("track must not be null.");
		}
		
		if (track.getCountPositions() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("trackId", trackId);
			Integer startAt = 0;
			if (cntCutPositions > 0) {
				startAt = track.getCountPositions() - cntCutPositions;
			} else if (cntCutPositions < 0) {
				startAt = -1 * cntCutPositions;
			}
			Integer fetchCount = track.getCountPositions() - startAt;
			
			params.put("startAt", startAt);
			params.put("fetchCount", fetchCount);
			
			log.debug("countPos=" + track.getCountPositions());
			log.debug("cntCutPositions=" + cntCutPositions);
			log.debug("startAt=" + startAt);
			log.debug("fetchCount=" + fetchCount);
			
			@SuppressWarnings("unchecked")
			List<PositionVo> positions = (List<PositionVo>)this.getSqlMapClientTemplate().
				queryForList(SQL_GET_POSITIONS_OF_TRACK, params);	
			if ((positions != null) && !positions.isEmpty()) {
				track.setPositions(positions);
			}
			
			if ((positions != null) && !positions.isEmpty()) {
				params = new HashMap<String, Object>();
				params.put("trackId", trackId);
				params.put("minSequenceId", positions.get(0).getSequenceId());
			
				if (messagesIncl && track.getCountMessages() > 0) {
					@SuppressWarnings("unchecked")
					List<MessageVo> messages = (List<MessageVo>)this.getSqlMapClientTemplate().
						queryForList(SQL_GET_MESSAGES_OF_TRACK, params);	
					if (messages != null) {
						track.setMessages(messages);
					}
				}
				
				if (emergencySignalsIncl && track.getCountEmergencySignals() > 0) {
					@SuppressWarnings("unchecked")
					List<EmergencySignalVo> emergencySignals = (List<EmergencySignalVo>)this.getSqlMapClientTemplate().
						queryForList(SQL_GET_EMERGENCY_SIGNALS_OF_TRACK, params);	
					if (emergencySignals != null) {
						track.setEmergencySignals(emergencySignals);
					}
				}
				
				if (mobNwCellsIncl && track.getCountMobNwCells() > 0) {
					@SuppressWarnings("unchecked")
					List<MobNwCellVo> mobNwCells = (List<MobNwCellVo>)this.getSqlMapClientTemplate().
						queryForList(SQL_GET_MOB_NW_CELLS_OF_TRACK, params);	
					if (mobNwCells != null) {
						track.setMobNwCells(mobNwCells);
					}
				}
				
				if (cardiacFunctionsIncl && track.getCountCardiacFunctions() > 0) {
					@SuppressWarnings("unchecked")
					List<CardiacFunctionVo> cardiacFunctions = (List<CardiacFunctionVo>)this.getSqlMapClientTemplate().
						queryForList(SQL_GET_CARDIAC_FUNCTIONS_OF_TRACK, params);	
					if (cardiacFunctions != null) {
						track.setCardiacFunctions(cardiacFunctions);
					}
				}
				
				if (senderStatesIncl && track.getCountSenderStates() > 0) {
					@SuppressWarnings("unchecked")
					List<SenderStateVo> senderStates = (List<SenderStateVo>)this.getSqlMapClientTemplate().
						queryForList(SQL_GET_SENDER_STATES_OF_TRACK, params);	
					if (senderStates != null) {
						track.setSenderStates(senderStates);
					}
				}
			}
		}
		
		log.debug("positions loaded=" + ((track.getPositions() != null) ? track.getPositions().size() : "0"));
		log.debug("messages loaded=" + ((track.getMessages() != null) ? track.getMessages().size() : "0"));
		log.debug("emergencySignals loaded=" + ((track.getEmergencySignals() != null) ? track.getEmergencySignals().size() : "0"));
		
		return track;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getTrackAsRecent(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getTrackAsRecent(String trackId) {
		if (trackId == null) {
			throw new IllegalArgumentException("trackId must not be null.");
		}		
		// get track by trackId.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("trackId", trackId);
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_TRACK_BY_ID_AS_RECENT_TRACK, params);	
		
		return track;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getTrackAsMin(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getTrackAsMin(String trackId) {
		if (trackId == null) {
			throw new IllegalArgumentException("trackId must not be null.");
		}		
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_TRACK_BY_ID_AS_MIN_TRACK, trackId);	
		
		return track;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getRecentTrackAsMin(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getRecentTrackAsMin(UserWithoutRoleVo user, String senderId) {
		TrackVo track = null;
		
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", user.getUserId());		
		params.put("senderId", senderId);
		params.put("onlyPublic", !user.getOptions().getRecTrAccPrivTrEnabled());
		
		track = (TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_RECENT_TRACK_AS_MIN_TRACK, params);
				
		return track;
	}

	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getRecentTrackAsRecent(UserWithoutRoleVo user, String senderId) {
		TrackVo track = null;
		
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", user.getUserId());		
		params.put("senderId", senderId);
		params.put("onlyPublic", !user.getOptions().getRecTrAccPrivTrEnabled());
		
		track = (TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_RECENT_TRACK_AS_RECENT_TRACK, params);
				
		return track;
	}
	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#storePositionAndMessage(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.SenderVo, de.msk.mylivetracker.domain.PositionVo, de.msk.mylivetracker.domain.MobNwCellVo, de.msk.mylivetracker.domain.MessageVo, de.msk.mylivetracker.domain.SenderStateVo, de.msk.mylivetracker.domain.CardiacFunctionVo, de.msk.mylivetracker.domain.EmergencySignalVo, de.msk.mylivetracker.domain.ClientInfoVo, de.msk.mylivetracker.domain.user.UserOptionsVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void storePositionAndMessage(UserWithoutRoleVo user,
		SenderVo sender, PositionVo position, MobNwCellVo mobNwCell,
		MessageVo message, SenderStateVo senderState,
		CardiacFunctionVo cardiacFunction,
		EmergencySignalVo emergencySignal, ClientInfoVo clientInfo,
		UserOptionsVo options) {
		if ((position == null) && 
			(mobNwCell == null) && 
			(message == null) &&
			(senderState == null) &&
			(cardiacFunction == null) &&
			(emergencySignal == null) &&
			(clientInfo == null)) {
			throw new IllegalArgumentException("nothing to store.");			
		}
		
		// get active track of sender.
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate()
			.queryForObject(
				SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_RECENT_TRACK, 
				sender.getSenderId());		
		
		String trackName = null;		
		String trackClientId = null;
		if (clientInfo.isValid()) {
			trackName = clientInfo.getTrackName();		
			trackClientId = clientInfo.getTrackId();			
		}
		
		if ((track == null) || 
			(!StringUtils.isEmpty(trackName) && 
			!StringUtils.equals(trackName, track.getName())) ||
			(!StringUtils.isEmpty(trackClientId) && 
			!StringUtils.equals(trackClientId, track.getTrackClientId())) ||
			trackIsExpired(user, track, options.getTrackAutoClose())) {			
			if (StringUtils.isEmpty(trackName)) {
				trackName = options.getDefTrackName();
			}
			track = this.auxCreateTrack(sender, track, trackClientId, 
				trackName, options.getDefTrackReleaseStatus());
		}
		
		if ((track != null) && (track.maxCountOfPositionsReached())) {
			track = this.auxCreateTrack(sender, track, trackClientId, 
				track.getName(), track.isReleased());
		}
		
		if (clientInfo.isValid()) {
			// set start mileage only 
			// o if received mileage is not null and
			// o if received mileage is greater or equal than zero and
			// o if start mileage is null and
			// o if count positions is null
			if ((clientInfo.getMileageInMtr() != null) &&
				(clientInfo.getMileageInMtr() >= 0d) &&
				(track.getClientStartMileageInMtr() == null) &&	
				(track.getCountPositions() == 0)) {
				track.setClientStartMileageInMtr(clientInfo.getMileageInMtr());
			}
			
			// if received mileage is invalid, set current mileage to null.
			if ((clientInfo.getMileageInMtr() == null) || 
				(clientInfo.getMileageInMtr() < 0d) ||
				((track.getClientMileageInMtr() != null) &&
				 (clientInfo.getMileageInMtr() < track.getClientMileageInMtr()))) {
				track.setClientMileageInMtr(null);
			} else {
				track.setClientMileageInMtr(clientInfo.getMileageInMtr());
			}
			
			track.setPhoneNumber(clientInfo.getPhoneNumber());
			track.setClientRuntimeWithoutPausesInMSecs(clientInfo.getRuntimeWithoutPausesInMSecs());
			track.setClientRuntimeWithPausesInMSecs(clientInfo.getRuntimeWithPausesInMSecs());
			track.getTimestamps().setLastHeartbeatSent(clientInfo.getTimeReceived());
		}
		
		// store position if exists.
		if (position.isValid()) {
			
			// update track distance and speed.
			PositionVo lastPosition = track.getRecentPosition();
			
			if (lastPosition != null) {
				Double distanceBetweenLast2PositionsInMtr = 
					GpsUtils.distanceInMtr(lastPosition, position);
				
				if ((track.getClientStartMileageInMtr() != null) && 
					(track.getClientMileageInMtr() != null)) {
					// valid start mileage and current mileage.
					track.setDistanceInMtr(
						track.getClientMileageInMtr() - 
						track.getClientStartMileageInMtr());
				} else {
					if (distanceBetweenLast2PositionsInMtr != null) {
						track.addDistanceInMtr(distanceBetweenLast2PositionsInMtr);
					}
				}
				if (distanceBetweenLast2PositionsInMtr != null) {
					Double timeBetweenLast2Positions =
						new Double(
							position.getTimeReceived().getAsMSecs() - 
							lastPosition.getTimeReceived().getAsMSecs());
					Double speedInMtrPerHour = distanceBetweenLast2PositionsInMtr / 
						(((double)timeBetweenLast2Positions) / 1000d / 3600d);
					if (speedInMtrPerHour.isInfinite() || speedInMtrPerHour.isNaN()) {
						track.setSpeedInMtrPerHour(0.0d);
					} else {
						track.setSpeedInMtrPerHour(speedInMtrPerHour);
					}
				} else {
					track.setSpeedInMtrPerHour(0.0d);
				}
			}
					
			position.setTrack(track);					
			this.getSqlMapClientTemplate().insert(SQL_STORE_POSITION, position);
			if (track.getStartPosition() == null) {
				track.setStartPosition(position);
			}
			track.setRecentPosition(position);
			track.setCountPositions(track.getCountPositions() + 1);			
		}
		
		// store mobile network position if exists.
		if (mobNwCell.isValid()) {
			mobNwCell.setTrack(track);
			mobNwCell.setPosition(position.isValid() ? position : null);
			this.getSqlMapClientTemplate().insert(SQL_STORE_MOB_NW_CELL, mobNwCell);
			track.setRecentMobNwCell(mobNwCell);
			track.setCountMobNwCells(track.getCountMobNwCells() + 1);			
		}
		
		// store message if exists.
		if (message.isValid()) {
			message.setTrack(track);
			message.setPosition(position.isValid() ? position : null);
			this.getSqlMapClientTemplate().insert(SQL_STORE_MESSAGE, message);
			track.setRecentMessage(message);
			track.setCountMessages(track.getCountMessages() + 1);			
		}

		// store senderState if exists.		
		if (senderState.isValid()) {
			senderState.setTrack(track);
			senderState.setPosition(position.isValid() ? position : null);
			this.getSqlMapClientTemplate().insert(SQL_STORE_SENDER_STATE, senderState);
			track.setRecentSenderState(senderState);
			track.setCountSenderStates(track.getCountSenderStates() + 1);			
		}

		// store cardiacFunction if exists.		
		if (cardiacFunction.isValid()) {
			cardiacFunction.setTrack(track);
			cardiacFunction.setPosition(position.isValid() ? position : null);
			this.getSqlMapClientTemplate().insert(SQL_STORE_CARDIAC_FUNCTION, cardiacFunction);
			track.setRecentCardiacFunction(cardiacFunction);
			track.setCountCardiacFunctions(track.getCountCardiacFunctions() + 1);			
		}
		
		// store emergency signal if exists.		
		if (emergencySignal.isValid()) {
			emergencySignal.setTrack(track);
			emergencySignal.setPosition(position.isValid() ? position : null);
			this.getSqlMapClientTemplate().insert(SQL_STORE_EMERGENCY_SIGNAL, emergencySignal);
			track.setRecentEmergencySignal(emergencySignal);
			track.setCountEmergencySignals(track.getCountEmergencySignals() + 1);			
		}

		// update track.
		track.getTimestamps().update(position);
		
		this.getSqlMapClientTemplate().update(SQL_UPDATE_TRACK, track);						
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getActiveTrackAsMin(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getActiveTrackAsMin(String senderId) {
		return this.getActiveTrackAsMinAux(senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#getActiveTrackAsRecent(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public TrackVo getActiveTrackAsRecent(String senderId) {
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate()
		.queryForObject(
			SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_RECENT_TRACK, 
			senderId);									
		return track;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#createTrack(de.msk.mylivetracker.domain.SenderVo, java.lang.String, boolean)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public TrackVo createTrack(SenderVo sender, 
		String trackName, boolean trackReleased) {
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate()
			.queryForObject(SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_MIN_TRACK, 
			sender.getSenderId());
		track = auxCreateTrack(sender, track, null, 
			trackName, trackReleased);			
		return track;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#updateTrackHeartbeat(java.util.Date, de.msk.mylivetracker.domain.SenderVo, java.lang.String, boolean)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateTrackHeartbeat(DateTime heartbeat, SenderVo sender,
		String trackName, boolean trackReleased) {
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate()
			.queryForObject(
				SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_RECENT_TRACK, 
				sender.getSenderId());		
		if (track == null) {
			track = this.auxCreateTrack(sender, track, null, trackName, trackReleased);
		}
		track.getTimestamps().setLastHeartbeatSent(heartbeat);
		this.getSqlMapClientTemplate().update(SQL_UPDATE_TRACK_HEARTBEAT, track);	
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#openTrack(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void openTrack(String trackId) {
		TrackVo trackToOpen = 
			(TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_TRACK_BY_ID_AS_MIN_TRACK, trackId);
		TrackVo currentActiveTrackOfSender =
			(TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_MIN_TRACK, trackToOpen.getSenderId());
		
		if (currentActiveTrackOfSender != null) {
			this.getSqlMapClientTemplate().update(SQL_CLOSE_TRACK, 
				createTrackParams(currentActiveTrackOfSender.getTrackId(), null));			
		}
		
		this.getSqlMapClientTemplate().update(SQL_OPEN_TRACK, 
			createTrackParams(trackId, null));
		log.debug("track with trackId " + trackId + " successfully opened.");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#closeTrack(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void closeTrack(String trackId) {	
		this.getSqlMapClientTemplate().update(SQL_CLOSE_TRACK, 
			createTrackParams(trackId, null));
		log.debug("track with trackId " + trackId + " successfully closed.");
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#closeActiveTrack(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void closeActiveTrack(String senderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("senderId", senderId);
		params.put("trackUpdated", new DateTime());
		this.getSqlMapClientTemplate().update(
			SQL_CLOSE_ACTIVE_TRACK,	params);
		log.debug("active track of sender " + senderId + " successfully closed.");	
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#resetTrack(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void resetTrack(String trackId) {
		this.resetTrackAux(trackId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#resetActiveTrack(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void resetActiveTrack(String senderId) {
		TrackVo track = this.getActiveTrackAsMinAux(senderId);
		this.resetTrack(track.getTrackId());	
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#removeTrack(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeTrack(String trackId) {
		this.markTrackForRemoving(trackId);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#removeClosedTracks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeClosedTracks(String userId) {
		List<String> trackIds = (List<String>)
			this.getSqlMapClientTemplate().queryForList(
				SQL_GET_CLOSED_TRACK_IDS_BY_USER_ID, userId);	
		for (String trackId : trackIds) {
			this.markTrackForRemoving(trackId);
		}	
		log.debug(trackIds.size() + " tracks removed of userId " + userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#removeOldTracks(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeOldTracks(long olderThanInMSecs) {
		DateTime timestamp = 
			new DateTime(new DateTime().getAsMSecs() - olderThanInMSecs);
		
		List<String> trackIds = (List<String>)
		this.getSqlMapClientTemplate().queryForList(
			SQL_GET_OLD_TRACK_IDS_BY_TIMESTAMP, timestamp);	
		for (String trackId : trackIds) {
			log.debug("remove track with trackId = '" + trackId + "'");
			this.markTrackForRemoving(trackId);
		}	
		log.debug(trackIds.size() + " tracks removed which are older than " + olderThanInMSecs + " msecs.");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#removeTracksWithRemoveFlag()
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public DeleteTrackResult deleteOneRemovedTrack() {
		int cnt = 0;
		String trackId = (String)
			this.getSqlMapClientTemplate().queryForObject(
				SQL_GET_ONE_TRACK_ID_WITH_REMOVE_FLAG);
		if (!StringUtils.isEmpty(trackId)) {
			cnt = this.deleteTrackAux(trackId);
		}
		DeleteTrackResult res = new DeleteTrackResult(trackId, cnt);
		log.debug(res.toString());
		return res;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#renameTrack(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void renameTrack(String trackId, String trackName) {
		int cnt = this.getSqlMapClientTemplate().update(SQL_RENAME_TRACK,
			createTrackParams(trackId, trackName));
		if (cnt != 1) {
			throw new RuntimeException(
				"renameTrack for track with trackId " + trackId + "failed, updated records: " + cnt);
		}
		log.debug("track with trackId " + trackId + 
			" successfully renamed to '" + trackName + "'");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#privatizeTrack(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void privatizeTrack(String trackId) {
		this.getSqlMapClientTemplate().update(
			SQL_PRIVATIZE_TRACK, createTrackParams(trackId, null));
		log.debug("track with trackId " + trackId + " successfully privatized.");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.ITrackDao#publishTrack(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void publishTrack(String trackId) {
		this.getSqlMapClientTemplate().update(
			SQL_PUBLISH_TRACK, createTrackParams(trackId, null));
		log.debug("track with trackId " + trackId + " successfully published.");
	}	
	
	/*
	 * ************************************************************************
	 * private methods which are not transactional.
	 * ************************************************************************
	 */
	
	private TrackVo auxCreateTrack(SenderVo sender, TrackVo currActiveTrack,
		String trackClientId, String trackName, 
		boolean newTrackReleased) {
						
		if (currActiveTrack != null) {
			this.getSqlMapClientTemplate().update(SQL_CLOSE_TRACK, 
				createTrackParams(currActiveTrack.getTrackId(), null));
			log.debug("current active track with trackid '" + 
				currActiveTrack.getTrackId() + "' closed.");
		}
	
		TrackVo newTrack = TrackVo.createInstance(
			trackClientId, trackName, sender, true, newTrackReleased);
			
		this.getSqlMapClientTemplate().insert(SQL_INSERT_TRACK, newTrack);
		
		log.debug("a new track has been started, " +
			"track '" +	newTrack.getName() + "'" +
			" with trackid '" + newTrack.getTrackId() + "'" +
			" for sender '" + newTrack.getSenderId() + "'");
		
		return newTrack;
	}

	private TrackVo getActiveTrackAsMinAux(String senderId) {
		TrackVo track = (TrackVo)this.getSqlMapClientTemplate()
			.queryForObject(
				SQL_GET_ACTIVE_TRACK_OF_SENDER_AS_MIN_TRACK, 
				senderId);									
		return track;
	}
	
	private void resetTrackAux(String trackId) {
		TrackVo track = 
			(TrackVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_GET_TRACK_BY_ID_AS_MIN_TRACK, trackId);		
		this.markTrackForRemoving(trackId); 
		track = TrackVo.reset(track);
		this.getSqlMapClientTemplate().insert(SQL_INSERT_TRACK, track);
		log.debug("track with trackId '" + trackId + "' successfully resetted.");
	}
	
	private void markTrackForRemoving(String trackId) {
		this.getSqlMapClientTemplate().update(SQL_MARK_TRACK_FOR_REMOVING, trackId);
	}
	
	private int deleteTrackAux(String trackId) {
		int cnt = 0;
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_TRACK, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_POSITION, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_MESSAGE, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_MOB_NW_CELL, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_SENDER_STATE, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_CARDIAC_FUNCTION, trackId);
		cnt += this.getSqlMapClientTemplate().delete(SQL_REMOVE_EMERGENCY_SIGNAL, trackId);
		return cnt;
	}
	
	private static Map<String, Object> createTrackParams(String trackId, String trackName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("trackId", trackId);
		if (!StringUtils.isEmpty(trackName)) {
			params.put("trackName", trackName);
		}
		params.put("trackUpdated", new DateTime());
		return params;
	}
	
	private boolean trackIsExpired(UserWithoutRoleVo user,
		TrackVo track, Integer trackAutoClose) {

		boolean res = false;
		if (trackAutoClose != 0) {
			if (trackAutoClose == -1) {
				res = false;
				String now = (new DateTime()).getAsStr(
					TimeZone.getTimeZone(user.getOptions().getTimeZone()), 
					DateTime.STD_DATE_FORMAT);
				String updated = track.getTimestamps().getTrackUpdated().getAsStr(
					TimeZone.getTimeZone(user.getOptions().getTimeZone()), 
					DateTime.STD_DATE_FORMAT);	
				res = !StringUtils.equals(now, updated);
			} else {
				DateTime now = new DateTime();
				if (now.getAsMSecs() -
					track.getTimestamps().getTrackUpdated().getAsMSecs() >= 
					trackAutoClose * 60 * 60 * 1000) {
					res = true;
				}
			}
		}
		return res;
	}
}
