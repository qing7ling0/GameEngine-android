package com.qq.engine.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import xyj.resource.ResLoader;

import android.util.Log;

import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.graphics.text.TextTextureCache;
import com.qq.engine.log.LogOut;
import com.qq.engine.opengl.TextureCache;


public class Debug {
	private final static byte E = 0;//error
	private final static byte W = 1;//warning
	private final static byte I = 2;//info
	private final static byte D = 3;//debug
	private final static byte V = 4;//verbose
	private static byte level = I;
	private final static char[] LEVELS = {'E','W','I','D', 'V'};
	private final static String TAG = "ME";
	
	/**正式包需要把这个标志设为FALSE*/
	public final static boolean BETA = false;
	
	/**可在按不同的键之后触发*/
	public static boolean SCREEN_PRINT = false;
	/**控制台输出，其实是截获Debug的输出到屏幕上*/
	public static boolean CONSOLE_ENABLE = false;
	/**输出Bitmaps相关的信息*/
	public static boolean BITMAPS_PRINT = false;
	
//	public static MultiText ERROR;
	
	public static void nextLevel()
	{
		level = (byte)(2+((level-2+1)%3));
		Debug.i("Debug.nextLevel:level = " + LEVELS[level]);
	}
	
	public static void i(String s)
	{
		Log.i(TAG, s);
	}
	
	public static void i(Object... s)
	{
		StringBuffer sb = new StringBuffer();
		for(Object ts:s)
		{
			sb.append(ts);
		}
		Log.i(TAG, sb.toString());
	}
	
	public static void w(String s)
	{
		Log.w(TAG, s);
		LogOut.getInstance().addLog(s);
	}
	
	public static void w(Object... s)
	{
		StringBuffer sb = new StringBuffer();
		for(Object ts:s)
		{
			sb.append(ts);
		}
		Log.w(TAG, sb.toString());
		LogOut.getInstance().addLog(sb.toString());
	}
	
	public static void e(String s)
	{
		Log.e(TAG, s);
		LogOut.getInstance().addLog(s);
	}
	
	public static void e(Object... s)
	{
		StringBuffer sb = new StringBuffer();
		for(Object ts:s)
		{
			sb.append(ts);
		}
		Log.e(TAG, sb.toString());
		LogOut.getInstance().addLog(sb.toString());
	}
	
	public static void d(String s)
	{
		Log.d(TAG, s);
	}
	
	public static void d(Object... s)
	{
		StringBuffer sb = new StringBuffer();
		for(Object ts:s)
		{
			sb.append(ts);
		}
		Log.d(TAG, sb.toString());
	}
	
	public static void v(String s)
	{
		Log.v(TAG, s);
	}
	
	public static void v(Object... s)
	{
		StringBuffer sb = new StringBuffer();
		for(Object ts:s)
		{
			sb.append(ts);
		}
		Log.v(TAG, sb.toString());
	}
	
	public static byte getLevel() {
		return level;
	}

	
	public static void printImagesLog()
	{
		TextureCache.getInstance().printLog();
	}
	
	public static void initError(Exception e)
	{
		initError(e, true);
	}
	
	public static void initError(Exception e, boolean save)
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		LogOut.getInstance().addLog(e.getClass().getSimpleName()+"----------------- Exception Begin");
		LogOut.getInstance().addLog(sw.getBuffer().toString());
		LogOut.getInstance().addLog(e.getClass().getSimpleName()+"----------------- Exception End");
		
		if (save) LogOut.getInstance().save(ResLoader.PATH_SDCARD);
	}
	
	public static void colse()
	{
		LogOut.getInstance().save(ResLoader.PATH_SDCARD, true);
	}
}
