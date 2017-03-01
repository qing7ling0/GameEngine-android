package com.qq.engine.action.interval;

import com.qq.engine.scene.NodeProperty;

public class ScaleBy extends ScaleTo {

	protected ScaleBy(float t, float scalex, float scaley) {
		super(t, scalex, scaley);
		durSax = scalex;
		durSay = scaley;
	}

	public static ScaleBy create(float t, float scalex, float scaley)
	{
		return new ScaleBy(t, scalex, scaley);
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		float tempx = durSax;
		float tempy = durSay;
		super.start(node);
		durSax = tempx;
		durSay = tempy;
	}

	@Override
	public ScaleBy getCopy() {
		// TODO Auto-generated method stub
		return new ScaleBy(duraction, durSax, durSay);
	}

	@Override
	public ScaleBy reverse() {
		// TODO Auto-generated method stub
		return new ScaleBy(duraction, -durSax, -durSay);
	}
	
}
