package com.qq.engine.net;


import java.util.ArrayList;

import com.qq.engine.net.handler.Handler;
import com.qq.engine.net.socket.SocketConn;
import com.qq.engine.net.socket.WriteThread;

/**
 * @author wuqingqing
 * 负责维护网络连接池
 */
public class ConnPool {
	/**游戏专用*/
	private static SocketConn gameConn;
	private static ArrayList<Handler> handlerList = new ArrayList<Handler>();
	protected static ConnPool instance;
	
	public static ConnPool getInstance()
	{
		return instance;
	}

	public static void registerHandler(Handler handler)
	{
		handlerList.add(handler);
	}
	
	public static SocketConn getGameConn() {
		if(gameConn == null){
			gameConn = new SocketConn();
		}
		return gameConn;
	}
	
	public static void close()
	{
		ConnPool.getGameConn().close();
	}
	
	public static void parsePacket()
	{
		ConnPool.getGameConn().parsePacket();
		ConnPool.getGameConn().checkTimeout();
	}

	/**网络是否超时
	 * @return
	 */
	public static boolean isNetTimeout()
	{
		return getGameConn().isNetTimeout();
	}
	
	/**
	 * 是否打开连接失败
	 * @return
	 */
	public static boolean isOpenConnectFailure()
	{
		return getGameConn().isOpenConnectFailure();
	}
	
	/**
	 * @param timeout
	 */
	public static void setNetTimeout(boolean timeout)
	{
		gameConn.setNetTimeout(timeout);
	}
	
	/**
	 * 处理来自游戏模块的网络请求
	 * 
	 * @param out
	 */
	public static void send(Packet out) {
		// 提交网络线程发送
		out.setId(WriteThread.getHeroID());
		ConnPool.getGameConn().handleClientRequest(out);
	}

	/**处理单个包
	 * @param in
	 */
	public static void parsePacket(Packet in)
	{
		int t = in.getType();
		int bt = t>>8;
		Handler handler = null;
		
		for(int i=0; i<handlerList.size(); i++)
		{
			handler = handlerList.get(i);
			if (handler.getProtocolType() == bt)
			{
				handler.parse(in);
				break;
			}
		}
	}
	
	public void handTimeOut() {}
}

