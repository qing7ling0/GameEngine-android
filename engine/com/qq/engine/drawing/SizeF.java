package com.qq.engine.drawing;

public class SizeF {
	public float width;
	public float height;
	
	public SizeF()
	{
		
	}
	
	public SizeF(float width, float height)
	{
		this.width = width;
		this.height = height;
	}
	
	public static SizeF create(SizeF size)
	{
		return new SizeF(size.width, size.height);
	}
	
	public static SizeF create(float width, float height)
	{
		return new SizeF(width, height);
	}
	
	public void set(SizeF size)
	{
		this.width = size.width;
		this.height = size.height;
	}
}
