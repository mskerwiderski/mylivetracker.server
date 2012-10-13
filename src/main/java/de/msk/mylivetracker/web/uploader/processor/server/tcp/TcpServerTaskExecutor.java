package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class TcpServerTaskExecutor extends ThreadPoolTaskExecutor {

	private static final long serialVersionUID = 9037305024116796763L;

	private static final Log log = LogFactory.getLog(TcpServerTaskExecutor.class);
	
	public void executeProcessor(TcpSocketProcessor processor) {
		log.info("executeProcessor: " + processor.getName()); 
		super.execute(processor);
		log.info("active count = " + this.getActiveCount());
	}
	
}
