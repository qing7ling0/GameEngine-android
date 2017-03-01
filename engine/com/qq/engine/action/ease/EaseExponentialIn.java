package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseExponentialIn extends EaseAction {

    public static EaseExponentialIn create(IntervalAction action) {
        return new EaseExponentialIn(action);
    }

    protected EaseExponentialIn(IntervalAction action) {
        super(action);
    }

	@Override
	public EaseExponentialIn getCopy() {
		return new EaseExponentialIn(other.getCopy());
	}

    @Override
    public void update(float t) {
        other.update((t == 0) ? 0 : (float) Math.pow(2, 10 * (t / 1 - 1)) - 1 * 0.001f);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseExponentialOut(other.reverse());
    }

}
