package com.qq.engine.action.interval;

import com.qq.engine.scene.NodeProperty;

public class FadeBy extends FadeTo {

	
	public static FadeBy create(float t, int alpha)
	{
		return new FadeBy(t, alpha);
	}
	
	protected FadeBy(float t, int alpha) {
		super(t, alpha);
		durAlpha = alpha;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		int temp = durAlpha;
		super.start(node);
		durAlpha = temp;
	}

	@Override
	public FadeBy getCopy() {
		// TODO Auto-generated method stub
		return new FadeBy(duraction, durAlpha);
	}

	@Override
	public FadeBy reverse() {
		// TODO Auto-generated method stub
		return new FadeBy(duraction, -durAlpha);
	}


}
