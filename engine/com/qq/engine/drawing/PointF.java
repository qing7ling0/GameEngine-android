package com.qq.engine.drawing;

public class PointF {
	public float x;
	public float y;
	
	public PointF()
	{
		
	}
	
	public PointF(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public PointF(PointF point)
	{
		this(point.x, point.y);
	}
	
	public static PointF zeroPoint()
	{
		return new PointF(0, 0);
	}
	
	public static PointF create(PointF point)
	{
		return new PointF(point.x, point.y);
	}
	
	public static PointF create(Point point)
	{
		return new PointF(point.x, point.y);
	}
	
	public static PointF create(float x, float y)
	{
		return new PointF(x, y);
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static PointF negative(PointF p)
	{
		return PointF.create(-p.x, -p.y);
	}
	
	public static PointF sub(final PointF v1, final PointF v2) {
        return create(v1.x - v2.x, v1.y - v2.y);
    }
	
	public static PointF add(final PointF v1, final PointF v2) {
        return create(v1.x + v2.x, v1.y + v2.y);
    }
}
