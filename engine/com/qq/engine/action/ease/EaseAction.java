package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class EaseAction extends IntervalAction {
	public static final float M_PI_X_2 = (float) (Math.PI * 2.0f);

	protected IntervalAction other;
	protected float rate;

	public EaseAction(IntervalAction action) {
		// TODO Auto-generated constructor stub
		super(action.getDuraction());
		this.other = action;
	}
	
	public EaseAction(IntervalAction action, float rate) {
		// TODO Auto-generated constructor stub
		super(action.getDuraction());
		this.other = action;
		this.rate = rate;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		other.start(node);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		other.stop();
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		float ra = this.rate;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}
}
