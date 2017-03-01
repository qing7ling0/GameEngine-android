package com.qq.engine.action.interval;

import com.qq.engine.scene.Node;


public class FadeIn extends FadeTo {

	public static FadeIn create(float t)
	{
		return new FadeIn(t, 255);
	}
	
	protected FadeIn(float t, int alpha) {
		super(t, alpha);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FadeIn getCopy() {
		// TODO Auto-generated method stub
		return new FadeIn(duraction, endAlpha);
	}

	@Override
	public FadeOut reverse() {
		// TODO Auto-generated method stub
		return FadeOut.create(duraction);
	}

}
