package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class FadeTo extends IntervalAction {

	protected int endAlpha;
	protected int durAlpha;
	protected int alpha;
	
	public static FadeTo create(float t, int alpha)
	{
		return new FadeTo(t, alpha);
	}
	
	protected FadeTo(float t, int alpha) {
		super(t);
		this.endAlpha = alpha;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		alpha = node.getAlpha();
		durAlpha = endAlpha - alpha;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
		
		target.setAlpha(alpha+(int)(durAlpha*dt));
	}

	@Override
	public FadeTo getCopy()
	{
		return new FadeTo(duraction, endAlpha);
	}
}
