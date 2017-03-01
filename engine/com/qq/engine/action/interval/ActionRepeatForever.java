package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class ActionRepeatForever extends IntervalAction {

	private IntervalAction reAction;
	
	public static ActionRepeatForever create(IntervalAction reAction) {
		// TODO Auto-generated constructor stub
		return new ActionRepeatForever(reAction);
	}

	protected ActionRepeatForever(IntervalAction reAction) {
		super(0);
		this.reAction = reAction;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		this.reAction.start(node);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		this.reAction.stop();
	}

	@Override
	public void step(float dt) {
		// TODO Auto-generated method stub
		this.reAction.step(dt);
		if (this.reAction.isDone())
		{
			float ot = this.reAction.getCurTime() - this.reAction.getDuraction();
			this.reAction.start(target);
			this.reAction.step(ot);
		}
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ActionRepeatForever getCopy() {
		// TODO Auto-generated method stub
		return new ActionRepeatForever(reAction);
	}

	@Override
	public ActionRepeatForever reverse() {
		// TODO Auto-generated method stub
		return new ActionRepeatForever(reAction.reverse());
	}

}
