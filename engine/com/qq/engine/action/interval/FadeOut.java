package com.qq.engine.action.interval;


public class FadeOut extends FadeTo {

	protected FadeOut(float t, int alpha) {
		super(t, alpha);
		// TODO Auto-generated constructor stub
	}

	public static FadeOut create(float t)
	{
		return new FadeOut(t, 0);
	}

	@Override
	public FadeOut getCopy() {
		// TODO Auto-generated method stub
		return new FadeOut(duraction, endAlpha);
	}
	
	@Override
	public FadeIn reverse() {
		// TODO Auto-generated method stub
		return FadeIn.create(duraction);
	}

}
