package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseElasticOut extends EaseElastic {

    public static EaseElasticOut create(IntervalAction action) {
        return new EaseElasticOut(action, 0.3f);
    }

    public static EaseElasticOut action(IntervalAction action, float period) {
        return new EaseElasticOut(action, period);
    }

    protected EaseElasticOut(IntervalAction action, float period) {
        super(action, period);
    }

    @Override
    public EaseAction getCopy() {
        return new EaseElasticOut(other.getCopy(), period_);
    }

    @Override
    public void update(float t) {
        float newT = 0;
        if (t == 0 || t == 1) {
            newT = t;
        } else {
            float s = period_ / 4;
            newT = (float) (Math.pow(2, -10 * t) * Math.sin((t - s) * M_PI_X_2  / period_) + 1);
        }
        other.update(newT);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseElasticIn(other.reverse(), period_);
    }

}
