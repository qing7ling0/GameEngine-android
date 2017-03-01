package com.qq.engine.action;

import java.util.ArrayList;
import java.util.HashMap;

import com.qq.engine.scene.Node;

public class ActionManger implements IUpdate {
 
	
	private ArrayList<Node> actionList;
	
	private static ActionManger instance;
	
	public static ActionManger getInstance()
	{
		if (instance == null)
		{
			instance = new ActionManger();
		}
		
		return instance;
	}
	
	public ActionManger()
	{
		Scheduler.getInstance().add(this, 0);
		actionList = new ArrayList<Node>();
	}
	
	public void add(Node action)
	{
		actionList.add(action);
	}

	public boolean remove(Node action)
	{
		return actionList.remove(action);
	}

	public void removeAll()
	{
		actionList.clear();
	}

	@Override
	public void update(float dt) {
		
		for(int i=0; i<actionList.size(); i++)
		{
			actionList.get(i).actionsUpdate(dt);
		}
	}
}
