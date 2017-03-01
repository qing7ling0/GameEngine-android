package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBounceInOut extends EaseBounce {

    public static EaseBounceInOut create(IntervalAction action) {
        return new EaseBounceInOut(action);
    }

    protected EaseBounceInOut(IntervalAction action) {
        super(action);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseBounceInOut(other.getCopy());
    }

    @Override
    public void update(float t) {
        float newT = 0;
        if (t < 0.5) {
            t = t * 2;
            newT = (1 - bounceTime(1 - t)) * 0.5f;
        } else
            newT = bounceTime(t * 2 - 1) * 0.5f + 0.5f;

        other.update(newT);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseBounceInOut(other.reverse());
    }

}
