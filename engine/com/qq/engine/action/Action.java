package com.qq.engine.action;

import com.qq.engine.scene.NodeProperty;

public abstract class Action {

	public static int TAG_NULL = -1;
	protected NodeProperty target;
	protected IActionCallback  callback;
	protected int tag;
	
	public boolean destroy;
	public boolean start;
	
	public Action()
	{
		tag = TAG_NULL;
	}

	public void start(NodeProperty node)
	{
		target = node;
		start = true;
	}
	
	public abstract void stop();
	
	public abstract void step(float dt);
	
	public abstract void update(float per);
	
	public boolean isDone()
	{
		return true;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public IActionCallback getCallback() {
		return callback;
	}

	public void setCallback(IActionCallback callback) {
		this.callback = callback;
	}
	
	public Action getCopy()
	{
		return null;
	}
	
	public Action reverse()
	{
		return null;
	}
	
	public void destroy()
	{
		destroy = true;
	}
}
