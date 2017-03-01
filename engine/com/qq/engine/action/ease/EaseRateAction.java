package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


/**
 * Base class for Easing actions with rate parameters
 */
public class EaseRateAction extends EaseAction {
	/** rate value for the actions */
	float rate;
	
	/** Initializes the action with the inner action and the rate parameter */
    protected EaseRateAction(IntervalAction action, float aRate) {
        super(action);
        rate = aRate;
    }

    @Override
    public EaseRateAction getCopy() {
        return new EaseRateAction(other.getCopy(), rate);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseRateAction(other.reverse(), 1 / rate);
    }
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		float ra = this.rate;
	}

}
