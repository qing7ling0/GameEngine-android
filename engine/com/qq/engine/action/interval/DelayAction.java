package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;

public class DelayAction extends IntervalAction {

	public static DelayAction create(float dt)
	{
		return new DelayAction(dt);
	}
	
	protected DelayAction(float dt) {
		// TODO Auto-generated constructor stub
		super(dt);
	}

	@Override
	public DelayAction getCopy() {
		return new DelayAction(duraction);
	}

    @Override
    public void update(float t) {
    }

    @Override
    public DelayAction reverse() {
        return new DelayAction(duraction);
    }
}
