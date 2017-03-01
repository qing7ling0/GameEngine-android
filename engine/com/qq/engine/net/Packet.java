package com.qq.engine.net;


/**
 * 消息包
 * @author wuqingqing
 */
public class Packet extends StreamIO {
	/** 协议号*/
	protected int type = -1;
	/** 实体ID 一般就是Hero id*/
	protected int id = 0;	
	/** 由具体协议来定义*/
	protected byte option;	
	/**	 备用预留返回码*/
	protected byte callbackID;
	
	public Packet(int type)
	{
		setType(type);
	}
	
	public Packet(){}
	
	public byte getOption() {
		return option;
	}

	public void setOption(byte option) {
		this.option = option;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getCallbackID() {
		return callbackID;
	}

	public void setCallbackID(byte callbackID) {
		this.callbackID = callbackID;
	}
}
