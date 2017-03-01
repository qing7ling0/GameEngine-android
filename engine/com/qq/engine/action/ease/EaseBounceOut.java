package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseBounceOut extends EaseBounce {

    public static EaseBounceOut create(IntervalAction action) {
        return new EaseBounceOut(action);
    }

    protected EaseBounceOut(IntervalAction action) {
        super(action);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseBounceOut(other.getCopy());
    }

    @Override
    public void update(float t) {
        float newT = bounceTime(t);
        other.update(newT);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseBounceIn(other.reverse());
    }

}
