package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


public class EaseSineOut extends EaseAction {

    public static EaseSineOut create(IntervalAction action) {
        return new EaseSineOut(action);
    }

    protected EaseSineOut(IntervalAction action) {
        super(action);
    }

	@Override
	public EaseSineOut getCopy() {
		return new EaseSineOut(other.getCopy());
	}

    @Override
    public void update(float t) {
        other.update((float)Math.sin(t * (float) Math.PI / 2));
    }

    @Override
    public IntervalAction reverse() {
        return new EaseSineIn(other.reverse());
    }

}
