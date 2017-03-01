package com.qq.engine.graphics.text;

import com.qq.engine.graphics.image.Image;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.utils.Debug;

public class TextTexture {

	public TextLoader loader;
	public Image img;
	
	/** 最近一次被使用的时间 */
	public long lastRefTime;
	
	public static TextTexture create(TextLoader loader)
	{
		TextTexture text = new TextTexture();
		text.init(loader);
		
		return text;
	}
	
	private void init(TextLoader loader)
	{
		this.loader = loader;
		
		img = ImageCache.createImage(null, loader);
	}

	public void updateCreateTime()
	{
		lastRefTime = System.currentTimeMillis();
	}
	
	public void recycle()
	{
		img.recycle();
		img = null;
	}
	
	public void printSelfLog()
	{
		Debug.d(this, " text=" + loader.texData.text);
	}
}
