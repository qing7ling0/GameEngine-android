package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;

public class EaseSineInOut extends EaseAction {

    public static EaseSineInOut action(IntervalAction action) {
        return new EaseSineInOut(action);
    }

    protected EaseSineInOut(IntervalAction action) {
        super(action);
    }

	@Override
	public EaseSineInOut getCopy() {
		return new EaseSineInOut(other.getCopy());
	}

    @Override
    public void update(float t) {
        other.update(-0.5f * ((float)Math.cos(Math.PI * t) - 1));
    }

    @Override
    public IntervalAction reverse() {
        return new EaseSineInOut(other.reverse());
    }

}
