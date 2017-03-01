package com.qq.engine.net.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import com.qq.engine.net.Encoder;
import com.qq.engine.net.Packet;
import com.qq.engine.utils.Debug;

public class WriteThread implements Runnable {	
	private SocketConn sc;
	private Socket socket;
	private DataOutputStream dos;
	protected String ip;
	/**等待发送的包*/
	private Vector<Packet> outQueue;	
	protected long connID;
	
	protected int priority = Thread.NORM_PRIORITY;
	
	/**当前角色id*/
	private static int heroID;
	
	/** Ping包 */
	Packet ping;
	final static int	PING_TIME_MAX	= 15*1000;
	
	private boolean over;
	
//	protected boolean run ;
	
	public WriteThread(SocketConn sc, Socket socket, DataOutputStream dos, Vector<Packet> outQueue, boolean sendHead)
	{
		this.sc = sc;
		this.connID = sc.connID;
		this.socket = socket;
		this.dos = dos;
		this.outQueue = outQueue;
		this.over = false;
		sendHead(sendHead);
		
		Thread thread = new Thread(this);
		thread.setPriority(priority);
		thread.start();
		Debug.v("WriteThread.WriteThread");
	}
	

	public void sendHead(boolean sendHead)
	{
		
	}

	/**生成发送包
	 * @return
	 */
	protected Packet createSend() {
		Packet toSend = null;
		if(outQueue.size() > 0) {
			toSend = (Packet) outQueue.elementAt(0);
			outQueue.removeElementAt(0);
		}
		return toSend;
	}

	/**
	 * 实现run接口 线程运行，负责维护网络连接
	 */
	public void run() {
		while (!over) {
			if (socket.isConnected() && !socket.isClosed() && !socket.isOutputShutdown())
			{
				try {
					Packet toSend = createSend();
					if(toSend != null) {
	//					Debug.i("write Thread will send ----0x"+Integer.toHexString(toSend.getType()) );
						send(toSend);
						try{
							Thread.sleep(1);
						}
						catch(Exception e){
							Debug.initError(e, false);
							Debug.e("WriteThread. ....send Packet .... error");}
					}
					else
					{
						sendPing();
						synchronized(outQueue)
						{
							try{
								outQueue.wait(5000);
							}
							catch(Exception e){
								Debug.initError(e, false);
								Debug.e("WriteThread. ....outQueue...wait error");
							}
						}
					}
					Debug.v("WriteThread.toSend..." + toSend);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Debug.initError(e, false);
					sc.close(true, connID);
					break;
				}
			}
		}
		Debug.e("WriteThread.exit...");
	}

	public String getIp() {
		return ip;
	}

	public static int getHeroID() {
		return heroID;
	}

	public static void setHeroID(int heroID) {
		WriteThread.heroID = heroID;
	}

	private final void send(Packet outPacket) throws IOException{
		
//		Debug.i("SocketConn.send:sendPacket aa pid = 0x" + Integer.toHexString(outPacket.getType()));
//		Debug.desc2 = "try send p = " + Integer.toHexString(outPacket.getType());
		dos.write(Encoder.packet2Bytes(outPacket));
		dos.flush();
//		Debug.i("SocketConn.send:sendPacket bb pid = 0x" + Integer.toHexString(outPacket.getType()) + ","+outPacket.getLength());
//		Debug.desc2 = "send p = " + Integer.toHexString(outPacket.getType());
	}
	private void sendPing() throws IOException
	{
		long lt = System.currentTimeMillis() - sc.lastPongTime;
		if(lt > PING_TIME_MAX)
		{
			createPingPacket();
			send(ping);
			Debug.d("WriteThread.sendPing");
		}
		else
		{
			Debug.d("WriteThread  sendPing false");
		}
	}
	private void createPingPacket() {
		ping = new Packet();
		ping.setType((short) 0x00f2);
		ping.setId(WriteThread.getHeroID());
		ping.setOption((byte) 0);
	}
	
	public void over()
	{
		over = true;
	}
}
