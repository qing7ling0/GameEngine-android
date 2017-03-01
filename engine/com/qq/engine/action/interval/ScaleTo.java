package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class ScaleTo extends IntervalAction {

	protected float scaleX;
	protected float scaley;
	protected float endSx;
	protected float endSy;
	protected float durSax;
	protected float durSay;
	
	protected ScaleTo(float t, float scalex, float scaley) {
		super(t);
		// TODO Auto-generated constructor stub
		this.endSx = scalex;
		this.endSy = scaley;
	}

	public static ScaleTo create(float t, float scalex, float scaley)
	{
		return new ScaleTo(t, scalex, scaley);
	}
	
	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		
		scaleX = node.getScaleX();
		scaley = node.getScaleY();
		
		durSax = endSx - scaleX;
		durSay = endSy - scaley;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		target.setScale(scaleX+durSax*dt, scaley+durSay*dt);
	}

	@Override
	public ScaleTo getCopy() {
		// TODO Auto-generated method stub
		return new ScaleTo(duraction, endSx, endSy);
	}

}
