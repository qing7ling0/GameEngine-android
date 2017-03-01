package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;
import com.qq.engine.utils.Debug;

public class ActionSequence extends IntervalAction {
	
	private IntervalAction[] actions;
	private int[] actionPers;
	private int actionIndex;
	
	/** 上一个action的per */
	private int lastPer;

	protected ActionSequence(IntervalAction... actions) {
		super(0);
		this.actions = actions;
		this.actionPers = new int[actions.length];
		
		for(int i=0; i<actions.length; i++)
		{
			duraction += actions[i].getDuraction();
		}
		
		for(int i=0; i<actionPers.length; i++)
		{
			float per = actions[i].getDuraction() / duraction;
			actionPers[i]  = (int) (per * 100000);
		}
	}

	public static ActionSequence create(IntervalAction... actions) {
		// TODO Auto-generated constructor stub
		return new ActionSequence(actions);
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		actionIndex = 0;
		lastPer = actionPers[actionIndex];
		actions[actionIndex].start(node);
	}

	@Override
	public void step(float dt) {
		// TODO Auto-generated method stub
		super.step(dt);
	}
	
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return super.isDone() && actionIndex >= actions.length;
	}

	@Override
	public void update(float per) {
		// TODO Auto-generated method stub
		super.update(per);
		if (actionIndex >= actions.length) return;
		
		// 这里放大100000倍是防止float计算误差
		int perInt = ((int)(per*100000)) - lastPer + actionPers[actionIndex];
		
		
		
		actions[actionIndex].update(Math.min(1, perInt*1f/actionPers[actionIndex]));

		while(actionIndex < actions.length && perInt >= actionPers[actionIndex])
		{
			perInt -= actionPers[actionIndex];
			nextAction(perInt);
		}
//		
//		if (per >= lastPer)
//		{
//			while(per >= lastPer)
//			{
//				per -= lastPer;
//				nextAction(per);
//			}
//		}
//		else
//		{
//			per = per  + actionPers[actionIndex] - lastPer;
//			actions[actionIndex].update(Math.min(1, per/actionPers[actionIndex]));
//		}
	}
	
	private void nextAction(int per)
	{
//		actions[actionIndex].update(1);
		if (actionIndex+1 < actions.length)
		{
			actions[actionIndex].stop();
			actionIndex++;
			actions[actionIndex].start(target);
			lastPer += actionPers[actionIndex];
			actions[actionIndex].update(Math.min(1, per*1f/actionPers[actionIndex]));
		}
		else
		{
			actionIndex++;
		}
	}

	@Override
	public ActionSequence getCopy() {
		// TODO Auto-generated method stub
		return new ActionSequence(actions);
	}

	@Override
	public ActionSequence reverse() {
		// TODO Auto-generated method stub
		
		int length = actions.length;
		
		IntervalAction[] acts = new IntervalAction[length];
		for(int i=0; i<length; i++)
		{
			acts[i] = actions[length-i-1].reverse();
		}
		
		return new ActionSequence(acts);
	}

}
