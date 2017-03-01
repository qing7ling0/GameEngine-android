package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.drawing.Color;
import com.qq.engine.scene.NodeProperty;

public class ColorTo extends IntervalAction {

	protected Color endColor;
	
	/** 开始坐标 */
	protected Color startColor;
	
	/** 当前移动位移 */
	protected float dupR;
	protected float dupG;
	protected float dupB;
	protected float dupA;
	
	protected Color curColor;
	
	public static ColorTo create(float dt, Color color)
	{
		ColorTo mt = new ColorTo(dt, color);
		
		return mt;
	}
	
	public ColorTo(float dt, Color color)
	{
		super(dt);
		endColor = new Color(color);
		startColor = new Color();
		curColor = new Color();
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		startColor = new Color(target.getColor());
		dupR = (endColor.r - startColor.r);
		dupG = (endColor.g - startColor.g);
		dupB = (endColor.b - startColor.b);
		dupA = (endColor.a - startColor.a);
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		curColor.r = startColor.r + dupR*dt;
		curColor.g = startColor.g + dupG*dt;
		curColor.b = startColor.b + dupB*dt;
		curColor.a = startColor.a + dupA*dt;
		target.setColor(curColor);
	}

	@Override
	public ColorTo getCopy() {
		// TODO Auto-generated method stub
		return new ColorTo(duraction, endColor);
	}

}
