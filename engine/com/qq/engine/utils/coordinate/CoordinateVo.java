package com.qq.engine.utils.coordinate;

import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.SizeF;

public class CoordinateVo {

	public float scaleX;
	public float scaleY;
	public float lastScaleX;
	public float lastScaleY;
	public SizeF size;
	
	/**  */
	public PointF originPoint;
	public PointF position;
	
	public CoordinateVo()
	{
		originPoint = new PointF();
		position = new PointF();
		size = new SizeF();
		lastScaleX = 1;
		lastScaleY = 1;
	}

	public CoordinateVo(float scaleX, float scaleY, PointF originPoint, PointF position, SizeF size) {
		this();
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.originPoint = new PointF(originPoint);
		this.position = new PointF(position);
		this.size.width = size.width;
		this.size.height = size.height;
		
	}
	
	public void translateCoordinate()
	{
		position.x -= Math.abs(lastScaleX) * originPoint.x*(scaleX-1);
		position.y -= Math.abs(lastScaleY) * originPoint.y*(scaleY-1);
		
		size.width *= Math.abs(scaleX);
		size.height *= Math.abs(scaleY);
		
		if (scaleX < 0)
		{
			position.x -= size.width;
		}
		if (scaleY < 0)
		{
			position.y -= size.height;
		}
		
	}
}
