package com.qq.engine.action;

import java.util.ArrayList;


public class Scheduler {
	private static Scheduler instance;
	
	private ArrayList<IUpdate> updateList = new ArrayList<IUpdate>();
	
	public static Scheduler getInstance()
	{
		if (instance == null)
		{
			instance = new Scheduler();
		}
		return instance;
	}
	
	public void update(float dt)
	{
		for(int i=0; i<updateList.size() ;i++)
		{
			IUpdate update = updateList.get(i);
			update.update(dt);
		}
	}
	
	public void add(IUpdate update)
	{
		if (updateList == null)
		{
			updateList = new ArrayList<IUpdate>();
		}
		updateList.add(update);
	}
	
	public void add(IUpdate update, int order)
	{
		if (updateList == null)
		{
			updateList = new ArrayList<IUpdate>();
		}
		updateList.add(order, update);
	}
	
	public void remove(IUpdate update)
	{
		if (updateList != null)
		{
			updateList.remove(update);
		}
	}
}
