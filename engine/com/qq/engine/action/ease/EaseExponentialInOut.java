package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseExponentialInOut extends EaseAction {

    public static EaseExponentialInOut create(IntervalAction action) {
        return new EaseExponentialInOut(action);
    }

    protected EaseExponentialInOut(IntervalAction action) {
        super(action);
    }

    @Override
    public void update(float t) {
    	t /= 0.5f;
        if (t < 1)
            t = 0.5f * (float) Math.pow(2, 10 * (t - 1));
        else
            t = 0.5f * (-(float) Math.pow(2, -10 * (t - 1) ) + 2);
        other.update(t);
    }

}
