package com.qq.engine.action.interval;

import com.qq.engine.drawing.PointF;
import com.qq.engine.utils.Debug;

public class Parabola {
	
	private PointF topPointF;
	private float a;
	
	public Parabola(PointF start, PointF end, float anger, float g)
	{
		topPointF = PointF.zeroPoint();
		
		calcTopPoint(start, end, 90, 5);
	}
	
	public float getCurrentPoint(float x)
	{
		float y = a * (x - topPointF.x) * (x - topPointF.x) + topPointF.y;
		Debug.e("getCurrentPoint  x = " + x + "  y=" + y);
		
		return y;
	}

	public void calcTopPoint(PointF start, PointF end, float anger, float g)
	{
		float x1 = start.x;
		float y1 = start.y;
		float x2 = end.x;
		float y2 = end.y;
		
		double degrees = Math.toRadians(anger);
		float a = 0.003f;
		
		if (start.x != end.x)
		{
			float x = ( ((y2-y1) / a) + x1*x1 - x2*x2 ) / (2 * (x1 - x2));
			
			float y = y1 - a * (x1 - x) * (x1 - x);
			
			topPointF.x = x;
			topPointF.y = y;
			
			
			this.a = a;
			
			Debug.e("Parabola  x = " + x + "  y=" + y);
		}
		
	}

}
