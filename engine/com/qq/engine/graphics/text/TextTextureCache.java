package com.qq.engine.graphics.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.qq.engine.graphics.GFont;

public class TextTextureCache {

	public static int GC_INTERVAL_MS = 5; // ms
	public static int GC_TIMEOUT = 10000; // ms
	private static float lastGCTick;
	private HashMap<String, TextTexture> textMap = new HashMap<String, TextTexture>();
	private TextLoader loader;
	private TextTextureData texData;

	private static TextTextureCache instance;

	public static TextTextureCache getInstance() {
		if (instance == null)
		{
			instance = new TextTextureCache();
		}

		return instance;
	}

	public TextTexture addText(GFont font, String text, boolean isAsyn)
	{
		return addText(font, text, 0xffffff, TextStrokeStyle.STROKE_NULL, isAsyn);
	}
	
	public TextTexture addText(GFont font, String text, int strokeColor, byte textStrokeStyle, boolean isAsyn)
	{
		texData = new TextTextureData();
		texData.init(text, font.getSize(), font.getColor(), font.isBold(), strokeColor, textStrokeStyle);
		
		
		TextTexture textTex = textMap.get(texData.key);

		if (textTex == null) {

			loader = new TextLoader(texData);
			loader.isAsynLoader = isAsyn;
			
			textTex = TextTexture.create(loader);
			textMap.put(texData.key, textTex);
		}
		textTex.updateCreateTime();
		
		return textTex;
	}

	public void update(float dt)
	{
		lastGCTick += dt;
		
		if (lastGCTick > GC_INTERVAL_MS) {
			lastGCTick = 0;
			long endTime = System.currentTimeMillis() - GC_TIMEOUT;
			
			Iterator<Map.Entry<String, TextTexture>> it = textMap.entrySet().iterator();
	        while (it.hasNext())
			{
				Map.Entry<String, TextTexture> entry = it.next();
				
				TextTexture tex = (TextTexture) entry.getValue();
				
				if (endTime > tex.lastRefTime)
				{
					tex.recycle();
//					textMap.remove(tex.loader.texData);
					it.remove();
				}
			}
		}
	}
	
	public void removeAll()
	{
		if (textMap != null)
		{
			Iterator<Map.Entry<String, TextTexture>> it = textMap.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, TextTexture> entry = it.next();
				TextTexture tex = (TextTexture) entry.getValue();
				tex.recycle();
				it.remove();
			}
		}
	}
	
	public void printLog()
	{
		Iterator<Map.Entry<String, TextTexture>> it = textMap.entrySet().iterator();
        while (it.hasNext())
		{
			Map.Entry<String, TextTexture> entry = it.next();
			
			TextTexture tex = (TextTexture) entry.getValue();
		
			tex.printSelfLog();
		}
	}
}
