package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseInOut extends EaseRateAction {

    public static EaseInOut create(IntervalAction action, float rate) {
        return new EaseInOut(action, rate);
    }

    protected EaseInOut(IntervalAction action, float rate) {
        super(action, rate);
    }

    @Override
    public void update(float t) {
        int sign = 1;
        int r = (int) rate;
        if (r % 2 == 0)
            sign = -1;

        t *= 2;
        if (t < 1)
            other.update(0.5f * (float) Math.pow(t, rate));
        else
            other.update(sign * 0.5f * ((float) Math.pow(t - 2, rate) + sign * 2));
    }


    // InOut and OutIn are symmetrical
    @Override
	public IntervalAction reverse()  {
		return new EaseInOut(other.reverse(), rate);
	}

}
