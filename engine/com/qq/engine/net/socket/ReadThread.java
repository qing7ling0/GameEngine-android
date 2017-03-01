package com.qq.engine.net.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import com.qq.engine.net.ConnPool;
import com.qq.engine.net.Encoder;
import com.qq.engine.net.Packet;
import com.qq.engine.utils.Debug;

public class ReadThread implements Runnable{
	private SocketConn sc;
	private Socket socket;
	private DataInputStream dis;
	protected long connID;
	protected boolean over;
		
	/**收到的包*/
	protected Vector<Packet> inQueue = new Vector<Packet>(3,5);
	
	public ReadThread(SocketConn sc, Socket socket, DataInputStream dis){
		this.sc = sc;
		this.connID = sc.connID;
		this.socket = socket;
		this.dis = dis;
		this.over = false;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void run(){
		while (!over) {
			if (socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown()) {
				try {
					if (dis != null) {
						if (dis.available() > 0) {
							Packet inPacket = new Packet();
							receivePacket(inPacket);
							Debug.d("ReadThread.run:receive p = " + Integer.toHexString(inPacket.getType()), Thread.currentThread().getId());
							if (inPacket.getType() != -1) {
								inQueue.addElement(inPacket);
							}
						}
						
						try {
							Thread.sleep(1);
						} catch (Exception e) {
							Debug.initError(e, false);
						}
					}
				} 
				catch (Exception exp) {
					exp.printStackTrace();
					Debug.initError(exp, false);
					sc.close(true, connID);
					break;
				}
				//end of catch
			}
		}
	}
	
	/**
	 * 保存数据包到InPacket
	 * 
	 * @param dis
	 */
	private void receivePacket(Packet in) throws Exception {
		byte[] head = new byte[12];
		readBytes(head, 12);
		
		in.setType(Encoder.readShort(0, head)&0xffff);
		int length = Encoder.readInt(2, head);			
		in.setId(Encoder.readInt(6, head));
		in.setOption(head[10]);
		in.setCallbackID(head[11]);

		in.setBody(new byte[length]);// 初始化消息体
		readBytes(in.getBody(), length);
	}
	
	private void readBytes(byte[] bytes, int readLength) throws IOException
	{
		for(int i = 0; i < readLength; )
		{
			int j = readLength-i;
			int readlength = dis.read(bytes, i, j);
			i += readlength;
			if(readlength > 0)
			{
				sc.lastPongTime = System.currentTimeMillis();
			}
		}//end for
	}//end function
	
	public void over(){
		over = true;
	}
	
	public void parsePacket()
	{
		if(inQueue.size() > 0)
		{
			Packet ip = (Packet)inQueue.elementAt(0);
			inQueue.removeElementAt(0);
			ConnPool.parsePacket(ip);
		}
	}
	
}
