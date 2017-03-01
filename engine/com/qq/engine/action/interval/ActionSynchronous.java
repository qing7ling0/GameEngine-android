package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

/**
 * 多个action 同步执行
 * @author wuqingqing
 *
 */
public class ActionSynchronous extends IntervalAction {

	private IntervalAction[] actions;


	public static ActionSynchronous create(IntervalAction... actions) {
		return new ActionSynchronous(actions);
	}
	
	protected ActionSynchronous(IntervalAction... actions) {
		super(0);
		this.actions = actions;
		float dur = 0;
		for(int i=0; i<actions.length; i++)
		{
			if (actions[i] != null)
			{
				if (actions[i].getDuraction() > dur)
				{
					dur = this.actions[i].getDuraction();
				}
			}
		}
		
		this.duraction = dur;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		for(int i=0, size=actions.length; i<size; i++)
		{
			actions[i].stop();
		}
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		for(int i=0, size=actions.length; i<size; i++)
		{
			actions[i].start(node);
		}
	}

	@Override
	public void update(float per) {
		// TODO Auto-generated method stub
		for(int i=0, size=actions.length; i<size; i++)
		{
			actions[i].update(per);
		}
	}

	@Override
	public ActionSynchronous getCopy()
	{
		IntervalAction[] actionCopys = new IntervalAction[actions.length];
		for(int i=0, size=actions.length; i<size; i++)
		{
			actionCopys[i] = actions[i].getCopy();
		}
		
		
		return new ActionSynchronous(actionCopys);
	}
}
