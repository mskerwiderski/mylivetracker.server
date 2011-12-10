package de.msk.mylivetracker.domain.track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.sender.SenderVo;

/**
 * TrackVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackVo implements Serializable {

	private static final long serialVersionUID = -3644780753652644779L;
	
	public static final Integer MAX_COUNT_OF_POSITIONS_PER_TRACK = 5000;
	
	private String trackId;
	private String trackClientId; // for sync
	private Integer versionMajor;
	private String name;
	private boolean active;
	private boolean released;
	private String senderId;
	private String senderName;
	private String phoneNumber;
	private String userId;
	
	private TrackTimestampsVo timestamps;
	
	private Integer countPositions;
	private List<PositionVo> positions = new ArrayList<PositionVo>();
	private PositionVo startPosition;
	private PositionVo recentPosition;
	private Integer countMobNwCells;
	private List<MobNwCellVo> mobNwCells = new ArrayList<MobNwCellVo>();
	private MobNwCellVo recentMobNwCell;
	private Integer countMessages;
	private List<MessageVo> messages = new ArrayList<MessageVo>();
	private MessageVo recentMessage;
	private Integer countSenderStates;
	private List<SenderStateVo> senderStates = new ArrayList<SenderStateVo>();
	private SenderStateVo recentSenderState;
	private Integer countCardiacFunctions;
	private List<CardiacFunctionVo> cardiacFunctions = new ArrayList<CardiacFunctionVo>();
	private CardiacFunctionVo recentCardiacFunction;
	private Integer countEmergencySignals;
	private List<EmergencySignalVo> emergencySignals = new ArrayList<EmergencySignalVo>();
	private EmergencySignalVo recentEmergencySignal;
	private Double distanceInMtr;
	private Double clientStartMileageInMtr;
	private Double clientMileageInMtr;
	private Double speedInMtrPerHour;
	private Long clientRuntimeWithoutPausesInMSecs;
	private Long clientRuntimeWithPausesInMSecs;

	public static TrackVo createInstance(String trackClientId, String name, SenderVo sender, boolean released) {		
		return createInstance(
			UUID.randomUUID().toString(), 
			trackClientId,
			name, sender.getUserId(), 
			sender.getSenderId(),
			sender.getName(),
			0, released, new DateTime());
	}
	
	public static TrackVo createInstance(String trackId, String trackClientId, String name,
		String userId, String senderId, String senderName, Integer versionMajor,
		boolean released, DateTime trackCreated) {
		TrackVo track = new TrackVo();									
		track.setTrackId(trackId);
		track.setTrackClientId(trackClientId);
		track.setVersionMajor(versionMajor);
		track.setName(name);			
		track.setTimestamps(new TrackTimestampsVo(trackCreated));		
		track.setActive(true);
		track.setReleased(released);
		track.setSenderId(senderId);
		track.setSenderName(senderName);
		track.setUserId(userId);
		track.setCountPositions(0);
		track.setCountMobNwCells(0);
		track.setCountMessages(0);
		track.setCountSenderStates(0);
		track.setCountCardiacFunctions(0);
		track.setCountEmergencySignals(0);
		track.setDistanceInMtr(0d);
		track.setSpeedInMtrPerHour(0d);
		return track;	
	}
	
	public static TrackVo reset(TrackVo track) {	
		return createInstance(
			track.getTrackId(),
			track.getTrackClientId(),
			track.getName(), 
			track.getUserId(), 
			track.getSenderId(),
			track.getSenderName(),
			track.getVersionMajor() + 1, track.isReleased(), 
			track.getTimestamps().getTrackCreated());				
	}	
	
	public boolean maxCountOfPositionsReached() {
		return this.getCountPositions() >= MAX_COUNT_OF_POSITIONS_PER_TRACK;
	}
	
	/**
	 * @return the startPosition
	 */
	public PositionVo getStartPosition() {
		return startPosition;
	}

	/**
	 * @param startPosition the startPosition to set
	 */
	public void setStartPosition(PositionVo startPosition) {
		this.startPosition = startPosition;
	}

	public PositionVo getEldestPosition() {
		PositionVo startPosition = null;
		if ((positions != null) && !positions.isEmpty()) {
			startPosition = positions.get(positions.size()-1);	
		}
		return startPosition;
	}
		
	/**
	 * @return the trackId
	 */
	public String getTrackId() {
		return trackId;
	}

	/**
	 * @param trackId the trackId to set
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	/**
	 * @return the trackClientId
	 */
	public String getTrackClientId() {
		return trackClientId;
	}

	/**
	 * @param trackClientId the trackClientId to set
	 */
	public void setTrackClientId(String trackClientId) {
		this.trackClientId = trackClientId;
	}

	/**
	 * @return the versionMajor
	 */
	public Integer getVersionMajor() {
		return versionMajor;
	}

	/**
	 * @param versionMajor the versionMajor to set
	 */
	public void setVersionMajor(Integer versionMajor) {
		this.versionMajor = versionMajor;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the timestamps
	 */
	public TrackTimestampsVo getTimestamps() {
		return timestamps;
	}

	/**
	 * @param timestamps the timestamps to set
	 */
	public void setTimestamps(TrackTimestampsVo timestamps) {
		this.timestamps = timestamps;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the released
	 */
	public boolean isReleased() {
		return released;
	}

	/**
	 * @param released the released to set
	 */
	public void setReleased(boolean released) {
		this.released = released;
	}	

	/**
	 * @param countPositions the countPositions to set
	 */
	public void setCountPositions(Integer countPositions) {
		this.countPositions = countPositions;
	}	
	/**
	 * @return the countPositions
	 */
	public Integer getCountPositions() {
		return countPositions;
	}	
	
	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<PositionVo> positions) {
		this.positions = positions;			
	}
	/**
	 * @return the positions
	 */
	public List<PositionVo> getPositions() {
		return positions;
	}			
	
	/**
	 * @return the countMobNwCells
	 */
	public Integer getCountMobNwCells() {
		return countMobNwCells;
	}

	/**
	 * @param countMobNwCells the countMobNwCells to set
	 */
	public void setCountMobNwCells(Integer countMobNwCells) {
		this.countMobNwCells = countMobNwCells;
	}

	/**
	 * @return the mobNwCells
	 */
	public List<MobNwCellVo> getMobNwCells() {
		return mobNwCells;
	}

	/**
	 * @param mobNwCells the mobNwCells to set
	 */
	public void setMobNwCells(List<MobNwCellVo> mobNwCells) {
		this.mobNwCells = mobNwCells;		
	}

	/**
	 * @return the countMessages
	 */
	public Integer getCountMessages() {
		return countMessages;
	}

	/**
	 * @param countMessages the countMessages to set
	 */
	public void setCountMessages(Integer countMessages) {
		this.countMessages = countMessages;
	}

	/**
	 * @return the messages
	 */
	public List<MessageVo> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<MessageVo> messages) {
		this.messages = messages;		
	}	

	/**
	 * @return the recentPosition
	 */
	public PositionVo getRecentPosition() {		
		return recentPosition;
	}

	/**
	 * @param recentPosition the recentPosition to set
	 */
	public void setRecentPosition(PositionVo recentPosition) {
		this.recentPosition = recentPosition;
	}	

	/**
	 * @return the recentMobNwCell
	 */
	public MobNwCellVo getRecentMobNwCell() {
		return recentMobNwCell;
	}

	/**
	 * @param recentMobNwCell the recentMobNwCell to set
	 */
	public void setRecentMobNwCell(MobNwCellVo recentMobNwCell) {
		this.recentMobNwCell = recentMobNwCell;		
	}

	/**
	 * @return the recentMessage
	 */
	public MessageVo getRecentMessage() {		
		return recentMessage;
	}

	/**
	 * @param recentMessage the recentMessage to set
	 */
	public void setRecentMessage(MessageVo recentMessage) {
		this.recentMessage = recentMessage;
	}

	/**
	 * @return the countSenderStates
	 */
	public Integer getCountSenderStates() {
		return countSenderStates;
	}

	/**
	 * @param countSenderStates the countSenderStates to set
	 */
	public void setCountSenderStates(Integer countSenderStates) {
		this.countSenderStates = countSenderStates;
	}

	/**
	 * @return the senderStates
	 */
	public List<SenderStateVo> getSenderStates() {
		return senderStates;
	}

	/**
	 * @param senderStates the senderStates to set
	 */
	public void setSenderStates(List<SenderStateVo> senderStates) {
		this.senderStates = senderStates;		
	}

	/**
	 * @return the recentSenderState
	 */
	public SenderStateVo getRecentSenderState() {
		return recentSenderState;
	}

	/**
	 * @param recentSenderState the recentSenderState to set
	 */
	public void setRecentSenderState(SenderStateVo recentSenderState) {
		this.recentSenderState = recentSenderState;
	}

	/**
	 * @return the countCardiacFunctions
	 */
	public Integer getCountCardiacFunctions() {
		return countCardiacFunctions;
	}

	/**
	 * @param countCardiacFunctions the countCardiacFunctions to set
	 */
	public void setCountCardiacFunctions(Integer countCardiacFunctions) {
		this.countCardiacFunctions = countCardiacFunctions;
	}

	/**
	 * @return the cardiacFunctions
	 */
	public List<CardiacFunctionVo> getCardiacFunctions() {
		return cardiacFunctions;
	}

	/**
	 * @param cardiacFunctions the cardiacFunctions to set
	 */
	public void setCardiacFunctions(List<CardiacFunctionVo> cardiacFunctions) {
		this.cardiacFunctions = cardiacFunctions;
	}

	/**
	 * @return the recentCardiacFunction
	 */
	public CardiacFunctionVo getRecentCardiacFunction() {
		return recentCardiacFunction;
	}

	/**
	 * @param recentCardiacFunction the recentCardiacFunction to set
	 */
	public void setRecentCardiacFunction(CardiacFunctionVo recentCardiacFunction) {
		this.recentCardiacFunction = recentCardiacFunction;
	}

	/**
	 * @return the countEmergencySignals
	 */
	public Integer getCountEmergencySignals() {
		return countEmergencySignals;
	}

	/**
	 * @param countEmergencySignal the countEmergencySignals to set
	 */
	public void setCountEmergencySignals(Integer countEmergencySignals) {
		this.countEmergencySignals = countEmergencySignals;
	}

	/**
	 * @return the emergencySignals
	 */
	public List<EmergencySignalVo> getEmergencySignals() {
		return emergencySignals;
	}

	/**
	 * @param emergencySignals the emergencySignals to set
	 */
	public void setEmergencySignals(List<EmergencySignalVo> emergencySignals) {
		this.emergencySignals = emergencySignals;
	}

	/**
	 * @return the recentEmergencySignal
	 */
	public EmergencySignalVo getRecentEmergencySignal() {
		return recentEmergencySignal;
	}

	/**
	 * @param recentEmergencySignal the recentEmergencySignal to set
	 */
	public void setRecentEmergencySignal(EmergencySignalVo recentEmergencySignal) {
		this.recentEmergencySignal = recentEmergencySignal;
	}

	public boolean getEmergencySignalActive() {
		return (this.recentEmergencySignal != null) &&
			(this.recentEmergencySignal.getActive());
	}
	
	/**
	 * @return the distanceInMtr
	 */
	public Double getDistanceInMtr() {
		if (this.distanceInMtr == null) {
			this.distanceInMtr = 0d;
		}
		return distanceInMtr;
	}

	public void addDistanceInMtr(Double distanceInMtr) {
		if (this.distanceInMtr == null) {
			this.distanceInMtr = 0d;
		}
		this.distanceInMtr += distanceInMtr;
	}
	
	/**
	 * @param distanceInMtr the distanceInMtr to set
	 */
	public void setDistanceInMtr(Double distanceInMtr) {
		this.distanceInMtr = distanceInMtr;
	}
	
	/**
	 * @return the speedInMtrPerHour
	 */
	public Double getSpeedInMtrPerHour() {
		return speedInMtrPerHour;
	}

	/**
	 * @param speedInMtrPerHour the speedInMtrPerHour to set
	 */
	public void setSpeedInMtrPerHour(Double speedInMtrPerHour) {
		this.speedInMtrPerHour = speedInMtrPerHour;
	}

	/**
	 * @return the clientStartMileageInMtr
	 */
	public Double getClientStartMileageInMtr() {
		return clientStartMileageInMtr;
	}

	/**
	 * @param clientStartMileageInMtr the clientStartMileageInMtr to set
	 */
	public void setClientStartMileageInMtr(Double clientStartMileageInMtr) {
		this.clientStartMileageInMtr = clientStartMileageInMtr;
	}

	/**
	 * @return the clientMileageInMtr
	 */
	public Double getClientMileageInMtr() {
		return clientMileageInMtr;
	}

	/**
	 * @param clientMileageInMtr the clientMileageInMtr to set
	 */
	public void setClientMileageInMtr(Double clientMileageInMtr) {
		this.clientMileageInMtr = clientMileageInMtr;
	}

	/**
	 * @return the clientRuntimeWithoutPausesInMSecs
	 */
	public Long getClientRuntimeWithoutPausesInMSecs() {
		return clientRuntimeWithoutPausesInMSecs;
	}

	/**
	 * @param clientRuntimeWithoutPausesInMSecs the clientRuntimeWithoutPausesInMSecs to set
	 */
	public void setClientRuntimeWithoutPausesInMSecs(
			Long clientRuntimeWithoutPausesInMSecs) {
		this.clientRuntimeWithoutPausesInMSecs = clientRuntimeWithoutPausesInMSecs;
	}

	/**
	 * @return the clientRuntimeWithPausesInMSecs
	 */
	public Long getClientRuntimeWithPausesInMSecs() {
		return clientRuntimeWithPausesInMSecs;
	}

	/**
	 * @param clientRuntimeWithPausesInMSecs the clientRuntimeWithPausesInMSecs to set
	 */
	public void setClientRuntimeWithPausesInMSecs(
			Long clientRuntimeWithPausesInMSecs) {
		this.clientRuntimeWithPausesInMSecs = clientRuntimeWithPausesInMSecs;
	}

	private static final double MSECS_PER_HOUR = 3600000d;
	
	/**
	 * @return the avgSpeedInMtrPerHour
	 */
	public Double getAvgSpeedInMtrPerHourWithPauses() {
		Double avgSpeedInMtrPerHour = 0d;
		double timeInHrs = this.getTimestamps().getTrackRuntimeInMSecs() / MSECS_PER_HOUR;
		if (timeInHrs != 0) {
			avgSpeedInMtrPerHour = this.getDistanceInMtr() * (1d / timeInHrs);
		} 
		return avgSpeedInMtrPerHour;
	}
	
	/**
	 * @return the avgSpeedInMtrPerHour
	 */
	public Double getAvgSpeedInMtrPerHourWithoutPauses() {
		if (getClientRuntimeWithoutPausesInMSecs() == null) return null;
		Double avgSpeedInMtrPerHour = 0d;
		double timeInHrs = this.getClientRuntimeWithoutPausesInMSecs() / MSECS_PER_HOUR;
		if (timeInHrs != 0) {
			avgSpeedInMtrPerHour = this.getDistanceInMtr() * (1d / timeInHrs);
		} 
		return avgSpeedInMtrPerHour;
	}
	
	/**
	 * @return the posSpeedInMtrPerHour
	 */
	public Double getPosAltitudeInMtr() {
		Double posAltitudeInMtr = 0d;
		if (this.recentPosition != null) {
			posAltitudeInMtr = this.recentPosition.getAltitudeInMtr();			
		}
		return posAltitudeInMtr;
	}
}
