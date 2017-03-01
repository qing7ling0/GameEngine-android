package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.drawing.PointF;
import com.qq.engine.scene.NodeProperty;

public class ParabolaTo extends IntervalAction {

	/** 最终坐标 */
	protected PointF endPosition;
	
	/** 开始坐标 */
	protected PointF startPosition;
	
	/** 当前移动位移 */
	protected PointF durDis;
	
	protected Parabola para;
	
	public static ParabolaTo create(float dt, PointF point)
	{
		ParabolaTo mt = new ParabolaTo(dt, point);
		
		return mt;
	}
	
	public ParabolaTo(float dt, PointF point)
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
		para = new Parabola(startPosition, endPosition, 1, 1);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
		float x = startPosition.x+durDis.x*dt;
		float y = para.getCurrentPoint(x);
		
		target.setPosition(x, y);
//		Debug.i(this, "  update  =dt" + dt);
	}

	@Override
	public ParabolaTo getCopy() {
		// TODO Auto-generated method stub
		return new ParabolaTo(duraction, endPosition);
	}

}
