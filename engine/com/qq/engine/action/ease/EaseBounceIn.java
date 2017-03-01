package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBounceIn extends EaseBounce {

    public static EaseBounceIn create(IntervalAction action) {
        return new EaseBounceIn(action);
    }

    protected EaseBounceIn(IntervalAction action) {
        super(action);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseBounceIn(other.getCopy());
    }

    @Override
    public void update(float t) {
        float newT = 1 - bounceTime(1 - t);
        other.update(newT);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseBounceOut(other.reverse());
    }

}
