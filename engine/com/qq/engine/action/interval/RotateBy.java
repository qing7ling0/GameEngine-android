package com.qq.engine.action.interval;

import com.qq.engine.scene.NodeProperty;

public class RotateBy extends RotateTo {

	public static RotateBy create(float t, float rotate)
	{
		return new RotateBy(t, rotate);
	}
	
	protected RotateBy(float t, float rotate) {
		super(t, rotate);
		this.durRotate = rotate;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
//		endRotate += node.getRotation();
		float temp = durRotate;
		super.start(node);
		durRotate = temp;
	}

	@Override
	public RotateBy getCopy() {
		// TODO Auto-generated method stub
		return new RotateBy(duraction, durRotate);
	}

	@Override
	public RotateBy reverse() {
		// TODO Auto-generated method stub
		return new RotateBy(duraction, -durRotate);
	}


}
