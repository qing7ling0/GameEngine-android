package com.qq.engine;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.qq.engine.events.EventsDispatcher;
import com.qq.engine.graphics.GGamePerform;
import com.qq.engine.graphics.GGamePerform.GPerformTask;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.scene.Scene;
import com.qq.engine.utils.Debug;

public abstract class GameLauncher {
	
	private static boolean launched;
	private Activity activity;
	private static GameLauncher instance;
	private int width;
	private int height;
	
	public GameLauncher(final Activity activity, int width, int height)
	{
		instance = this;
		
		this.activity = activity;
		this.width = width;
		this.height = height;
		GameDriver.createInstance(this);
	}
	
	public abstract void init();
	
	/**
	 * @return the instance
	 */
	public static GameLauncher getInstance() {
		return instance;
	}

	public void launch()
	{
		this.launchScene();
	}

	public void onPause()
	{
		GameDriver.getInstance().onPause();
	}
	
	public void onResume()
	{
		GameDriver.getInstance().onResume();
	}
	
	public void onDestroy()
	{
		GameDriver.getInstance().destroy();
	}
	
	public void exit()
	{
		GameDriver.ANDROID_ACTIVITY.finish();
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public abstract void launchScene();
	public abstract void quitGame();
	
	public abstract void runOnUIThread( final Runnable runnable );

	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			GGamePerform.getInstance().perform(new GPerformTask() {
				
				@Override
				public void perform() {
					// TODO Auto-generated method stub
					Debug.printImagesLog();
				}
			});
			
			return true;
		}
		
		return EventsDispatcher.getInstance().queueEvent(event);
	}
	
	public void drawFPS(Graphics g, int fps)
	{
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public abstract Scene createReloadScene();
	
}
