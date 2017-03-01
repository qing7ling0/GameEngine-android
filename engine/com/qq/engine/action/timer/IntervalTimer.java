package com.qq.engine.action.timer;

public class IntervalTimer {

public final static float MIN_TIME_UINT = 0.000000001F;
	
	/** 完成 Action 所需时间 */
	protected float duraction;
	
	/** 当前运行时间 */
	protected float curTime;
	
	/** 当前进度 */
	protected float per;
	

	public static IntervalTimer create(float t)
	{
		IntervalTimer ta = new IntervalTimer(t);

		return ta;
	}    
	
    protected IntervalTimer(float t) {
    	duraction = t;
    }
	
	public void start() {
		// TODO Auto-generated method stub
		curTime = MIN_TIME_UINT;
		if (duraction == 0) duraction = MIN_TIME_UINT;
	}

	public void stop() {
		// TODO Auto-generated method stub
		curTime = duraction;
	}

	public void update(float dt) {
		// TODO Auto-generated method stub
		curTime += dt;
		per = Math.min(1, curTime/duraction);
	}
	
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
	
	public float getPer()
	{
		return per;
	}
}
