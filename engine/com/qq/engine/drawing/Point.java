package com.qq.engine.drawing;

public class Point {
	public int x;
	public int y;
	
	public Point()
	{
		
	}
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(Point point)
	{
		this(point.x, point.y);
	}
	
	public static Point zeroPoint()
	{
		return new Point(0, 0);
	}
	
	public static Point create(Point point)
	{
		return new Point(point);
	}

	public static Point create(int x, int y)
	{
		return new Point(x, y);
	}
	
	public static Point create(float x, float y)
	{
		return create((int)x, (int)y);
	}
	
	public static Point create(PointF point)
	{
		return create(point.x, point.y);
	}
	
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
