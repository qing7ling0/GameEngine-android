package com.qq.engine.graphics;

public class GraphicsDriver {
	
	private static GraphicsDriver instance;
	
	public static GraphicsDriver getInstance()
	{
		if (instance != null)
		{
			instance = new GraphicsDriver();
		}
		return instance;
	}

	public Graphics g;
	
}
