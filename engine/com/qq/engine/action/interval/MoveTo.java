package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.drawing.PointF;
import com.qq.engine.scene.NodeProperty;

public class MoveTo extends IntervalAction {

	/** 最终坐标 */
	protected PointF endPosition;
	
	/** 开始坐标 */
	protected PointF startPosition;
	
	/** 当前移动位移 */
	protected PointF durDis;
	
	public static MoveTo create(float dt, PointF point)
	{
		MoveTo mt = new MoveTo(dt, point);
		
		return mt;
	}
	
	public MoveTo(float dt, PointF point)
	{
		super(dt);
		endPosition = PointF.create(point);
		startPosition = PointF.zeroPoint();
		durDis = PointF.zeroPoint();
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		startPosition = PointF.create(target.getPosition());
		durDis.set(endPosition.x-startPosition.x, endPosition.y-startPosition.y);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		target.setPosition(startPosition.x+durDis.x*dt, startPosition.y+durDis.y*dt);
//		Debug.i(this, "  update  =dt" + dt);
	}

	@Override
	public MoveTo getCopy() {
		// TODO Auto-generated method stub
		return new MoveTo(duraction, endPosition);
	}

}
