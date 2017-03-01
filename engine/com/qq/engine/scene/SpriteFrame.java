package com.qq.engine.scene;

import com.qq.engine.drawing.RectangleF;
import com.qq.engine.graphics.image.Image;

public class SpriteFrame {
	public Image image;
	public RectangleF rect;
	
	public static SpriteFrame create(Image image, RectangleF rect)
	{
		SpriteFrame sf = new SpriteFrame();
		sf.image = image;
		sf.rect = rect;
		
		return sf;
	}
	
	public static SpriteFrame create(Image image)
	{
		SpriteFrame sf = new SpriteFrame();
		sf.image = image;
		sf.rect = new RectangleF(0, 0, image.getWidth(), image.getHeight());
		
		return sf;
	}
}
