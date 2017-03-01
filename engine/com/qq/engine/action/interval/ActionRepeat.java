package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.Node;
import com.qq.engine.scene.NodeProperty;

public class ActionRepeat extends IntervalAction {

	protected int times;
	protected IntervalAction reAction;
	protected int curTimes;
	
	public static ActionRepeat create(IntervalAction reAction, int times)
	{
		return new ActionRepeat(reAction, times);
	}
	
	protected ActionRepeat(IntervalAction reAction, int times) {
		super(reAction.getDuraction()*times);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		reAction.start(node);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		reAction.stop();
	}

	@Override
	public void update(float per) {
		// TODO Auto-generated method stub
		super.update(per);
		per *= times;
		if (per >= curTimes+1)
		{
			reAction.update(1);
			curTimes++;
			if (curTimes < times)
			{
				reAction.stop();
				reAction.start(target);
				reAction.update(per-curTimes);
			}
		}
		else
		{
			per -= curTimes;
			reAction.update(per);
		}
	}

	@Override
	public ActionRepeat getCopy() {
		// TODO Auto-generated method stub
		return new ActionRepeat(reAction, times);
	}

}
