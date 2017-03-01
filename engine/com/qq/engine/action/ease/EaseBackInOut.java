package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBackInOut extends EaseAction {

	private static float overshoot = 1.70158f * 1.525f;
	
	public static EaseBackInOut create(IntervalAction action, float rate)
	{
		EaseBackInOut ebi = new EaseBackInOut(action, rate);
		
		return ebi;
	}
	
	protected EaseBackInOut(IntervalAction action, float rate) {
		super(action, rate);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
        dt = dt * 2;
        if (dt < 1) {
            other.update((dt * dt * ((overshoot + 1) * dt - overshoot)) / 2);
        } else {
            dt = dt - 2;
            other.update((dt * dt * ((overshoot + 1) * dt + overshoot)) / 2 + 1);
        }
	}


}
