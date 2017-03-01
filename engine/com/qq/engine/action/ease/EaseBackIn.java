package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBackIn extends EaseAction {

    public static EaseBackIn create(IntervalAction action) {
        return new EaseBackIn(action);
    }

    protected EaseBackIn(IntervalAction action) {
        super(action);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseBackIn(other.getCopy());
    }

    @Override
    public void update(float t) {
        float overshoot = 1.70158f;
        other.update(t * t * ((overshoot + 1) * t - overshoot));
    }

    @Override
    public IntervalAction reverse() {
        return new EaseBackOut(other.reverse());
    }

}
