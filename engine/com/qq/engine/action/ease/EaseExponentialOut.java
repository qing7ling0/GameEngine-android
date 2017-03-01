package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseExponentialOut extends EaseAction {

    public static EaseExponentialOut create(IntervalAction action) {
        return new EaseExponentialOut(action);
    }

    protected EaseExponentialOut(IntervalAction action) {
        super(action);
    }

	@Override
	public EaseExponentialOut getCopy() {
		return new EaseExponentialOut(other.getCopy());
	}

    @Override
    public void update(float t) {
        other.update((t == 1) ? 1 : ((float) (-Math.pow(2, -10 * t / 1) + 1)));
    }

    @Override
    public IntervalAction reverse() {
        return new EaseExponentialIn(other.reverse());
    }

}
