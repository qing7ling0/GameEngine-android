package com.qq.engine.action.interval;

import com.qq.engine.scene.NodeProperty;

public class SkewBy extends SkewTo {

	protected SkewBy(float t, float skewX, float skewY) {
		super(t, skewX, skewY);
		durSkx = skewX;
		durSky = skewY;
	}

	public static SkewBy create(float t, float skewX, float skewY)
	{
		return new SkewBy(t, skewX, skewY);
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		float tempx = durSkx;
		float tempy = durSky;
		super.start(node);
		durSkx = tempx;
		durSky = tempy;
	}

	@Override
	public SkewBy getCopy() {
		// TODO Auto-generated method stub
		return new SkewBy(duraction, durSkx, durSky);
	}

	@Override
	public SkewBy reverse() {
		// TODO Auto-generated method stub
		return new SkewBy(duraction, -durSkx, -durSky);
	}

}
