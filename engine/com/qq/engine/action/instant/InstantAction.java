package com.qq.engine.action.instant;

import com.qq.engine.action.IntervalAction;

public class InstantAction extends IntervalAction {

	protected InstantAction() {
		super(0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void step(float dt) {
		// TODO Auto-generated method stub
		super.step(dt);
		update(1.0f);
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public InstantAction getCopy() {
		// TODO Auto-generated method stub
		return new InstantAction();
	}

	@Override
	public IntervalAction reverse() {
		// TODO Auto-generated method stub
		throw(new RuntimeException("Reverse action not implemented"));
	}


}
