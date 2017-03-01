package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseSineIn extends EaseAction {

    public static EaseSineIn create(IntervalAction action) {
        return new EaseSineIn(action);
    }

    protected EaseSineIn(IntervalAction action) {
        super(action);
    }

	@Override
	public EaseSineIn getCopy() {
		return new EaseSineIn(other.getCopy());
	}

    @Override
    public void update(float t) {
        other.update(-1 * (float)Math.cos(t * (float) Math.PI / 2) + 1);
    }

    @Override
    public IntervalAction reverse() {
        return new EaseSineOut(other.reverse());
    }


}
