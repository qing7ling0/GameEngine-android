package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.scene.NodeProperty;

public class RotateTo extends IntervalAction {

	protected float endRotate;
	protected float startRotate;
	protected float durRotate;
	
	public static RotateTo create(float t, float rotate)
	{
		return new RotateTo(t, rotate);
	}
	
	protected RotateTo(float t, float rotate) {
		super(t);
		// TODO Auto-generated constructor stub
		this.endRotate = rotate;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		startRotate = node.getRotation();
		durRotate = endRotate - startRotate;
	}

	@Override
	public void update(float per) {
		// TODO Auto-generated method stub
		super.update(per);
		target.setRotation(startRotate +per*durRotate);
	}

	@Override
	public RotateTo getCopy() {
		// TODO Auto-generated method stub
		return new RotateTo(duraction, endRotate);
	}

}
