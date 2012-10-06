package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.processor.CutConnectionException;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.PreProcessResultDsc;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.RemoteInfo;
import de.msk.mylivetracker.web.uploader.processor.ServerInfo;
import de.msk.mylivetracker.web.uploader.processor.UploadProcessor;

/**
 * TcpSocketProcessor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TcpSocketProcessor extends UploadProcessor implements Runnable {
 
	private static final Log log = LogFactory.getLog(TcpSocketProcessor.class);
	
	private SocketProcessorConfig socketProcessorConfig;	
	private Socket socket;
	
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
		
	/**
	 * @param serverName
	 * @param processorType
	 * @param supportedServices
	 * @param dataPacketCreator
	 * @param dataInterpreter
	 * @param socketProcessorConfig
	 * @param socket
	 */
	public TcpSocketProcessor(String serverName, 
		ProcessorType processorType,		
		Services supportedServices,
		DataPacketCreator dataPacketCreator,
		SocketProcessorConfig socketProcessorConfig,
		Socket socket) {
		super(serverName, 
			processorType, 			
			supportedServices, 
			dataPacketCreator,
			true);
		this.socketProcessorConfig = socketProcessorConfig;
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.info(this.getProcessorInfo() + ": thread started.");		
		
		while ((socket != null) && !socket.isClosed()) {
			this.process();
		}
		
		log.info(this.getProcessorInfo() + ": thread end reached.");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getRemoteInfo()
	 */
	@Override
	public RemoteInfo getRemoteInfo() {
		return new RemoteInfo(
			this.socket.getInetAddress().getHostAddress(),
			this.socket.getInetAddress().getCanonicalHostName(),				
			String.valueOf(this.socket.getPort())
		);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getServerInfo()
	 */
	@Override
	public ServerInfo getServerInfo() {
		return new ServerInfo(
			this.getServerName(),
			String.valueOf(this.socket.getLocalPort())); 		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.UploadProcessor#postProcess(de.msk.mylivetracker.web.uploader.processor.UploadProcessor.ProcessResult, boolean)
	 */
	@Override
	public void postProcess(ProcessResult result, boolean cutConnection) {
		try {
			String response = (result != null) ? ((result.getResponseStr() != null) ? 
				result.getResponseStr() : result.getResultMsg()) : null;
			if (response != null) {
				writer.write(response);
				writer.flush();			
			}
			log.debug("response: " + response);
		} catch (IOException e) {
			log.info(e.toString());
		}
		if (cutConnection) {
			this.closeSocket();
		}
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#preProcess()
	 */
	@Override
	public PreProcessResultDsc preProcess() {
		PreProcessResultDsc preProcessResultDsc = null;
		try {
			// set timeout.
			this.socket.setSoTimeout(
				this.socketProcessorConfig.getSocketReadTimeoutInMSecs());

			// create reader if not exists.
			if (reader == null) {				
				reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
				log.info(this.getProcessorInfo() + ": new reader created.");
			}
			
			// create writer if not exists.
			if (writer == null) {
				writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
				log.info(this.getProcessorInfo() + ": new writer created.");
			}
			
			preProcessResultDsc =
				this.readDataStr(reader, this.socketProcessorConfig.isDoReadLine());
		} catch (Exception e) {
			preProcessResultDsc = 
				new PreProcessResultDsc(
					PreProcessResultDsc.ConnectionStatus.TcpError);
		}
		
		return preProcessResultDsc;
	}
	
	private PreProcessResultDsc readDataStr(BufferedReader reader, boolean readLine) {
		String dataStr = "";		
		PreProcessResultDsc.ConnectionStatus connectionStatus = 
			PreProcessResultDsc.ConnectionStatus.Running;
		boolean doRead = true;
		long startMSecs = (new DateTime()).getAsMSecs();
		while (doRead) {					
			try {
				if (readLine) {
					dataStr = reader.readLine();					
				} else {					
					int ch = reader.read();
					if (ch != -1) {						
						dataStr += (char)ch;					
					} else {
						log.debug(this.getProcessorInfo() + ": cut connection exception.");
						throw new CutConnectionException();														
					}
				}
			} catch (SocketTimeoutException e) {
				long checkMSecs = (new DateTime()).getAsMSecs();
				if ((checkMSecs - startMSecs) > this.socketProcessorConfig.getConnectionTimeoutInMSecs()) {
					connectionStatus = PreProcessResultDsc.ConnectionStatus.Timeout;
				}						
				doRead = connectionStatus.isOk() &&	StringUtils.isEmpty(dataStr);				
			} catch (Exception e) {
				connectionStatus = PreProcessResultDsc.ConnectionStatus.Lost;
				doRead = false;
				log.debug(this.getProcessorInfo() + ": connection lost.");
			}			
		}				
		
		log.debug(this.getProcessorInfo() + 
			": received dataStr: '" + (StringUtils.isEmpty(dataStr) ? "<empty>" : dataStr) +
			"', status: " + connectionStatus.toString());
				
		return new PreProcessResultDsc(
			connectionStatus,
			createDataStrCtxArray( 
				this.socketProcessorConfig.isSupportsMultipleRecordsPerReception(),
				dataStr));
	}
	
	public static DataStrCtx[] createDataStrCtxArray(
		boolean supportsMultipleRecordsPerReception, String dataStr) {
		DataStrCtx[] res = new DataStrCtx[0];
		if (!StringUtils.isEmpty(dataStr)) {
			if (supportsMultipleRecordsPerReception) {
				dataStr = StringUtils.replace(dataStr, "\r\n", "\n");
				dataStr = StringUtils.replace(dataStr, "\r", "\n");
				String[] dataStrArr = StringUtils.split(dataStr, "\n");
				res = new DataStrCtx[dataStrArr.length];
				for (int i=0; i < dataStrArr.length; i++) {
					res[i] = new DataStrCtx(dataStrArr[i]);
				}
			} else {
				res = new DataStrCtx[1];
				dataStr = StringUtils.chomp(dataStr);
				res[0] = new DataStrCtx(dataStr);
			}
		}
		return res;
	}
	
	private void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			log.info(this.getProcessorInfo() + 
				": exception while closing socket: " + 
				e.getMessage());
		} finally {
			this.socket = null;
			log.info(this.getProcessorInfo() + ": socket closed.");
		}		
	}		
}
