package de.msk.mylivetracker.domain.sender;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * SenderSymbolVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-02
 * 
 */
public enum SenderSymbolVo {
	Phone("sender.symbol.phone"),
	PersonMale("sender.symbol.person.male"),
	PersonFemale("sender.symbol.person.female"),
	Family("sender.symbol.family"),
	Runner("sender.symbol.runner"),
	Bicycle("sender.symbol.bicycle"),
	Motorcycle("sender.symbol.motorcycle"),
	Car("sender.symbol.car"),
	Van("sender.symbol.van"),
	Train("sender.symbol.train"),
	Bus("sender.symbol.bus"),
	Truck("sender.symbol.truck"),
	Pet("sender.symbol.pet");
	
	private String id;
	private String msgCode;
	
	private static Map<String, SenderSymbolVo> INSTANCES =
		new HashMap<String, SenderSymbolVo>();
	
	static {
		for (SenderSymbolVo symbol : SenderSymbolVo.values()) {
			INSTANCES.put(symbol.getId(), symbol);
		}
	}
	
	public static SenderSymbolVo getById(String id) {
		return INSTANCES.get(id);
	}
	
	private SenderSymbolVo(String msgCode) {
		this.id = StringUtils.lowerCase(this.name());
		this.msgCode = msgCode;
	}
	public String getId() {
		return id;
	}
	public String getMsgCode() {
		return msgCode;
	}
}