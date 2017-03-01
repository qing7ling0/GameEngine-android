package com.qq.engine.net.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

import com.qq.engine.net.Packet;
import com.qq.engine.utils.Debug;

public class SocketConn implements Runnable{
	public WriteThread writeThread;
	public ReadThread readThread;	
	private Socket socket;
	private boolean netTimeout;
	protected long connID;
	private boolean checkPong;
	protected long lastPongTime;
	
	/** 打开连接失败 */
	protected boolean openConnectFailure;
	
	/**等待发送的包*/
	private Vector<Packet> outQueue = new Vector<Packet>(3, 5);	
	
	private final static String SOCKET = "socket://";

	public SocketConn()
	{
		
	}
	
	public void close()
	{
		close(false, this.connID);
	}
	public void close(boolean timeout)
	{
		close(timeout, this.connID);
	}
	void close(boolean timeout, long connID)
	{
		Debug.d("SocketConn.close");
		synchronized(this.outQueue)
		{
			try {
				if(socket != null)
				{
					if(timeout)
					{
						this.setNetTimeout(true);
					}
					socket.close();
					Debug.d("SocketConn.close");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				Debug.initError(e, false);
			}
			finally
			{
				checkPong = false;
				if (writeThread != null) writeThread.over();
				if (readThread != null) readThread.over();
				writeThread = null;
				readThread = null;
				socket = null;
				connectIP = "";
			}
			this.outQueue.removeAllElements();
		}
	}

	private String openIP;
	private String connectIP;
	private boolean sendHead;
	public void openDirect(String connectIP, boolean sendHead) {
		this.openIP = connectIP+"";
		this.sendHead = sendHead;
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void run()
	{
		Debug.v("SocketConn.open = " + openIP);
		this.connID = System.currentTimeMillis();
		try {
			String conIp = openIP;
			// 建立连接
//			if(!conIp.substring(0, 9).equals(SOCKET)){
			// 建立连接
				conIp = SOCKET+openIP;
//			}
			int colonDex = conIp.indexOf(":",SOCKET.length());
    		String host = conIp.substring(SOCKET.length(), colonDex);
    		String port = conIp.substring(colonDex+1, conIp.length());
    		int portInt = Integer.parseInt(port);
    		openConnectFailure = false;
			socket = new Socket();
			socket.setSoTimeout(30*1000);
//			socket.setSoLinger(true, 0);
//			socket.setReuseAddress(true);
			Debug.v("SocketConn.run: try to connect "+conIp, "  host=", host, " port=", port);
			SocketAddress sa = new InetSocketAddress(host, portInt);
			socket.connect(sa, 30*1000);
			// 打开数据输出流
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			this.lastPongTime = System.currentTimeMillis();
			this.checkPong = true;
			this.connectIP = openIP+"";
			this.writeThread = new WriteThread(this, socket, dos, this.outQueue, sendHead);
			this.readThread = new ReadThread(this, socket, dis);
			Debug.v("SocketConn.run: create readthread "+socket.getInetAddress(), ",", socket.getPort());
		}
		catch (Exception e) {
			e.printStackTrace();
			Debug.initError(e, false);
			this.openConnectFailure = true;
			this.close(true, this.connID);
		}
	}
	
	public void checkTimeout()
	{
		if(checkPong)
		{
			if(System.currentTimeMillis() - lastPongTime > 60*1000)
			{
				Debug.e("SocketConn.网络超时  长时间没有Ping包");
				this.close(true, this.connID);
			}
		}
	}
	
	public void setNetTimeout(boolean netTimeout) {
		this.netTimeout = netTimeout;
		Debug.w("SocketConn.net timeout");
	}
	
	public void parsePacket()
	{
		if(readThread!=null)
			this.readThread.parsePacket();
	}
	
	public boolean isNetTimeout() {
		return netTimeout;
	}
	
	public void handleClientRequest(Packet out) {
		this.lastPongTime = System.currentTimeMillis();
		synchronized(outQueue)
		{
			outQueue.addElement(out);
			outQueue.notify();
		}
	}

	public boolean isOK()
	{
		return this.socket != null;
	}

	public boolean isOpenConnectFailure() {
		return openConnectFailure;
	}
	
	/** 判断是否已经打开了此链接 */
	public boolean checkOpened(String ip)
	{
		return connectIP!=null && connectIP.equals(ip);
	}
}
