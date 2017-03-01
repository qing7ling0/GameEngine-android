package com.qq.engine.net.handler;

import com.qq.engine.net.ConnPool;
import com.qq.engine.net.Packet;


public abstract class Handler {
	private int protocolType;
	
	public Handler(int protocolType)
	{
		this.protocolType = protocolType;
	}
	public abstract void parse(Packet in);
	public void send(Packet out)
	{
		int ot = out.getType() & 0xff;
		out.setType(ot | (protocolType<<8));
		ConnPool.send(out); 
	}
	public int getProtocolType() {
		return protocolType;
	}
}
