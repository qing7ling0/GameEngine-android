package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBackOut extends EaseAction {

    public static EaseBackOut create(IntervalAction action) {
        return new EaseBackOut(action);
    }

    protected EaseBackOut(IntervalAction action) {
        super(action);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseBackOut(other.getCopy());
    }

    @Override
    public void update(float t) {
        float overshoot = 1.70158f;

        t = t - 1;
        other.update(t * t * ((overshoot + 1) * t + overshoot) + 1);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseBackIn(other.reverse());
    }

}
