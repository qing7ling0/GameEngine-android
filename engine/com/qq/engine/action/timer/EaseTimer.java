package com.qq.engine.action.timer;

public class EaseTimer extends IntervalTimer {

	protected float rate;
	
	public EaseTimer(float t) {
		super(t);
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

}
