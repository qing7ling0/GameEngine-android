package com.qq.engine.view;

import java.lang.reflect.Field;

import android.view.ViewParent;
import android.widget.Scroller;

import com.qq.engine.GameDriver;
import com.qq.engine.utils.Debug;

/**
 * @author wuqingqing
 *
 */
public class EditTextChangeCheck {

	public static boolean changed = true;
	private static EditTextChangeCheck instance;
	
	private ViewParent par = null;
	private Scroller ms;
	private Class<?> cl;
	private Field f;
	
	public static EditTextChangeCheck getInstance()
	{
		if (instance == null)
		{
			instance = new EditTextChangeCheck();
		}
		return instance;
	}
	
	
	public boolean checkChange()
	{
		if (par == null)
		{
			
			par = GameDriver.getGameRootView();
		}
		try
		{
			if (ms == null)
			{
				int apiLevel = android.os.Build.VERSION.SDK_INT;
				// Android SDK 4.0
				if (apiLevel < 14)
				{
					cl = Class.forName("android.view.ViewRoot");
				}
				else
				{
					cl = Class.forName("android.view.ViewRootImpl");
				}
				f = cl.getDeclaredField("mScroller");
				f.setAccessible(true);
				ms = (Scroller) f.get(par);
			}
			else
			{
				if (ms.getFinalY() != ms.getCurrY())
				{
					requestLayout();
					return true;
				}
				else
				{
					if (changed)
					{
						requestLayout();
						changed = false;
						return true;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Debug.initError(e, false);
		}
		
		return false;
	}
	
	private void requestLayout()
	{
		GameDriver.runOnUIThread( new Runnable(){
			public void run()
			{
				GameDriver.ANDROID_ACTIVITY.getWindow().getDecorView().requestLayout();
			}
		});
	}

}
