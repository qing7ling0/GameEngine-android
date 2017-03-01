package com.qq.engine.drawing;

public class Size {
	public int width;
	public int height;
	
	public static Size create(int width, int height)
	{
		return new Size(width, height);
	}
	public Size(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
