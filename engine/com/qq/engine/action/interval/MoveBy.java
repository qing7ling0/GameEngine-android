package com.qq.engine.action.interval;

import com.qq.engine.drawing.PointF;
import com.qq.engine.scene.NodeProperty;

public class MoveBy extends MoveTo {

	public MoveBy(float dt, PointF point) {
		super(dt, point);
		durDis.set(point.x, point.y);
	}

	public static MoveBy create(float time, PointF p)
	{
		return new MoveBy(time, p);
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		float tempx = durDis.x;
		float tempy = durDis.y;
		
		super.start(node);
		
		durDis.set(tempx, tempy);
	}

	@Override
	public MoveBy getCopy() {
		// TODO Auto-generated method stub
		return new MoveBy(duraction, durDis);
	}

	@Override
	public MoveBy reverse() {
		// TODO Auto-generated method stub
		return new MoveBy(duraction, PointF.negative(durDis));
	}
	
}
