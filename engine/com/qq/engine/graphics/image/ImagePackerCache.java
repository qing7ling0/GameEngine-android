package com.qq.engine.graphics.image;

import java.util.Hashtable;

public class ImagePackerCache {

	private static ImagePackerCache instance = new ImagePackerCache();
	private static Hashtable<String, ImagePackerData> texsCache = new Hashtable<String, ImagePackerData>();
	
	public static ImagePackerCache getInstance()
	{
		return instance;
	}
	
	protected ImagePackerCache() {
		// TODO Auto-generated constructor stub
	}

	public void add(ImagePackerData imagePackerData)
	{
		texsCache.put(imagePackerData.key, imagePackerData);
	}
	
	public ImagePackerData getImagePackerDataByKey(String key)
	{
		return texsCache.get(key);
	}
	
	public void remove(ImagePackerData imagePackerData)
	{
		texsCache.remove(imagePackerData.key);
	}
}
