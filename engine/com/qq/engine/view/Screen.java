package com.qq.engine.view;

import java.io.InputStream;

import android.util.DisplayMetrics;

import com.qq.engine.GameDriver;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.opengl.glutils.Matrix3;

public class Screen {
	
	public static int OFF_X = 0;
	public static int OFF_Y = 0;
	public static float SCALE = 1;

	/** 屏幕实际宽 */
	public static int SCREEN_W = 0;
	/** 屏幕实际高 */
	public static int SCREEN_H = 0;

	/** 游戏实际宽 */
	public static int GAME_W = 0;
	/** 游戏实际高 */
	public static int GAME_H = 0;
	
	/** 游戏实际宽的一半 */
	public static int HALF_SW = 0;
	/** 游戏实际高 的一半*/
	public static int HALF_SH = 0;

	/** 游戏屏幕缩放后的宽 */
	public static int SCALE_W = 0;
	/** 游戏屏幕缩放后的高 */
	public static int SCALE_H = 0;

	
	public static void initGame(int width, int height)
	{
		
		GAME_W = width;
		GAME_H = height;
	}
	
	public static void initScreen(int screenWidth, int screenHeight)
	{
//		initScreenSize();
		 SCREEN_W = screenWidth;
		 SCREEN_H = screenHeight;
		
		HALF_SW = GAME_W >> 1;
		HALF_SH = GAME_H >> 1;
		OFF_X = 0;
		OFF_Y = 0;
		
		adapterScreen();
	}
	
	private static void adapterScreen()
	{
		if(GAME_W*SCREEN_H < SCREEN_W*GAME_H)
		{
			SCALE = (SCREEN_H*1.0f)/GAME_H;
			SCALE_W = (int) (GAME_W*SCALE);
			SCALE_H = (int) (GAME_H*SCALE);
			
			OFF_X = ((int)(SCREEN_W-SCALE_W))>>1;
			OFF_Y = ((int)(SCREEN_H-SCALE_H))>>1;
		}
		else if(GAME_W*SCREEN_H == SCREEN_W*GAME_H)
		{
			SCALE = SCREEN_W*1.0f/GAME_W;
			SCALE_W = (int) (GAME_W*SCALE);
			SCALE_H = (int) (GAME_H*SCALE);
		}
		else
		{
			SCALE = (SCREEN_W*1.0f)/GAME_W;
			SCALE_W = (int) (GAME_W*SCALE);
			SCALE_H = (int) (GAME_H*SCALE);
			
			OFF_X = ((int)(SCREEN_W-SCALE_W))>>1;
			OFF_Y = ((int)(SCREEN_H-SCALE_H))>>1;
		}
	}
	
	public static void initScreenSize()
	{
		 DisplayMetrics dm = new DisplayMetrics();
		 GameDriver.ANDROID_ACTIVITY.getWindowManager().getDefaultDisplay().getMetrics(dm);
		 
		 SCREEN_W = dm.widthPixels;
		 SCREEN_H = dm.heightPixels;
	}
	
	public InputStream getResourceAsStream(String name)
	{
		return getClass().getResourceAsStream("/assets"+name);
	}
	
	/**
	 * 把游戏坐标X转换成屏幕实际坐标
	 * @param x
	 * @return
	 */
	public static int gameConvertToScreenX(int x)
	{
		return (int)(OFF_X+SCALE*x);
	}
	

	/**
	 * 把游戏坐标Y转换成屏幕实际坐标
	 * @param x
	 * @return
	 */
	public static int gameConvertToScreenY(int y)
	{
		return (int)(OFF_Y+SCALE*y);
	}
	

	/**
	 * 把游戏内的长度转换成屏幕实际长度
	 * @param x
	 * @return
	 */
	public static int gameConvertToScreenWH(int w)
	{
		return (int)(SCALE*w);
	}
	
	/**
	 * 把屏幕实际坐标X转换成游戏坐标
	 * @param x
	 * @return
	 */
	public static int screenConvertToGameX(float x)
	{
		return (int)((x-OFF_X)/SCALE);
	}
	

	/**
	 * 把屏幕实际坐标Y转换成游戏坐标
	 * @param x
	 * @return
	 */
	public static int screenConvertToGameY(float y)
	{
		return (int)((y-OFF_Y)/SCALE);
	}

	/**
	 * 把屏幕实际长度转换成游戏内的长度
	 * @param x
	 * @return
	 */
	public static int screenConvertToGameWH(float w)
	{
		return (int)(w/SCALE);
	}
}
