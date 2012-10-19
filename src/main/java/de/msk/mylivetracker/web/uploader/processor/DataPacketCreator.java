package de.msk.mylivetracker.web.uploader.processor;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter;

/**
 * DataPacketCreator.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DataPacketCreator {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(DataPacketCreator.class);
		
	private List<IDataInterpreter> dataInterpreters;
		
	private DataPacketCreator(List<IDataInterpreter> dataInterpreters) {
		this.dataInterpreters = dataInterpreters;
	}
	public DataPacket create(IDataCtx data, Services supportedServices, 
		IDataInterpreter interpreter, SenderFromRequestVo senderFromRequest,
		Map<String, Object> uploadProcessContext) {
		DataPacket dataPacket = null;
		if (interpreter != null) {
			dataPacket = interpreter.createDataPacket(
				supportedServices, senderFromRequest, data, uploadProcessContext);
		} else {
			for (int i=0; ((dataPacket == null) && (i < dataInterpreters.size())); i++) {
				dataPacket =
					dataInterpreters.get(i).createDataPacket(
						supportedServices, senderFromRequest, data, uploadProcessContext);								
			}
		}
		return dataPacket;
	}
	public List<IDataInterpreter> getDataInterpreters() {
		return dataInterpreters;
	}
}
