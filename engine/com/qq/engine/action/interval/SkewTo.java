package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class SkewTo extends IntervalAction {

	protected float skewX;
	protected float skewY;
	protected float endSx;
	protected float endSy;
	protected float durSkx;
	protected float durSky;
	
	protected SkewTo(float t, float skewX, float skewY) {
		super(t);
		// TODO Auto-generated constructor stub
		this.endSx = skewX;
		this.endSy = skewY;
	}

	public static SkewTo create(float t, float skewX, float skewY)
	{
		return new SkewTo(t, skewX, skewY);
	}
	
	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		
		skewX = node.getSkewX();
		skewY = node.getSkewY();
		
		durSkx = endSx - skewX;
		durSky = endSy - skewY;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		target.setSkew(skewX+durSkx*dt, skewY+durSky*dt);
	}

	@Override
	public SkewTo getCopy() {
		// TODO Auto-generated method stub
		return new SkewTo(duraction, endSx, endSy);
	}

}
