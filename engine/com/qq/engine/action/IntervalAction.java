package com.qq.engine.action;

import com.qq.engine.scene.NodeProperty;

public class IntervalAction extends Action {

	public final static float MIN_TIME_UINT = 0.000000001F;
	
	/** 完成 Action 所需时间 */
	protected float duraction;
	
	/** 当前运行时间 */
	protected float curTime;
	

	public static IntervalAction create(float t)
	{
		IntervalAction ta = new IntervalAction(t);

		return ta;
	}    
	
    protected IntervalAction(float t) {
    	duraction = t;
    }
	
	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		curTime = MIN_TIME_UINT;
		if (duraction == 0) duraction = MIN_TIME_UINT;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		curTime = duraction;
		start = false;
	}

	@Override
	public void step(float dt) {
		// TODO Auto-generated method stub
		curTime += dt;
		update(Math.min(1, curTime/duraction));
	}

	@Override
	public void update(float per) {
		// TODO Auto-generated method stub

//		Debug.i(this, "  per=" + per);
	}
	
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return curTime >=  duraction;
	}

	public float getDuraction() {
		return duraction;
	}

	public void setDuraction(float duraction) {
		this.duraction = duraction;
	}

	public float getCurTime() {
		return curTime;
	}

	@Override
	public IntervalAction getCopy() {
		// TODO Auto-generated method stub
		return create(duraction);
	}

	@Override
	public IntervalAction reverse() {
		// TODO Auto-generated method stub
		throw(new RuntimeException("Reverse action not implemented"));
	}
	
	
}
