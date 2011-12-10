package de.msk.mylivetracker.web.uploader.processor.server.udp;

import java.net.DatagramPacket;

import de.msk.mylivetracker.web.uploader.processor.IDataCtx;

/**
 * DatagramPacketCtx.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DatagramPacketCtx implements IDataCtx {

	private DatagramPacket packet;
	
	public DatagramPacketCtx(DatagramPacket packet) {
		this.packet = packet;		
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IDataCtx#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return (this.packet.getLength() == 0);
	}
	
	/**
	 * @return the dataStr
	 */
	public String getDataStr() {
		return new String(packet.getData(), 0, packet.getLength());
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDataStr();
	}	
}
