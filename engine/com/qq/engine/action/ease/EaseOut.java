package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;

/**
 * 变速运动   rate<1 加速  rate > 1 减速
 * @author wuqingqing
 *
 */
public class EaseOut extends EaseAction {

	public static EaseOut create(IntervalAction action, float rate)
	{
		EaseOut ei = new EaseOut(action, rate);
		
		return ei;
	}
	
	public EaseOut (IntervalAction action, float rate)
	{
		super(action, rate);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
        other.update((float) Math.pow(dt, 1 / rate));
	}

}
