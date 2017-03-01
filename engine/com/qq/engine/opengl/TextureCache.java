package com.qq.engine.opengl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.qq.engine.graphics.text.TextTexture;
import com.qq.engine.graphics.text.TextTextureData;
import com.qq.engine.utils.Debug;

public class TextureCache {
	private static HashMap<String, Texture2D> texsCache = new HashMap<String, Texture2D>();
	private static TextureCache instance = new TextureCache();

	public static TextureCache getInstance()
	{
		return instance;
	}
	
	public Texture2D get(String key) {
		if (texsCache.containsKey(key)) {
			Texture2D tex = texsCache.get(key);
			return tex;
		}
		return null;
	}
	
	public static void remove(Texture2D tex) {
		if (texsCache != null) {
			texsCache.remove(tex.key);
		}
	}
	
	public static void add(String key, Texture2D tex)
	{
		texsCache.put(key, tex);
	}
	
	public void printLog()
	{
		Iterator<Map.Entry<String, Texture2D>> it = texsCache.entrySet().iterator();
		
		float me = 0;
		
	    while (it.hasNext())
		{
			Map.Entry<String, Texture2D> entry = it.next();
			
			Texture2D tex = (Texture2D) entry.getValue();
			me += tex.getMemory();
		
			tex.printLog();
		}
	    
	    Debug.e(this.getClass().getSimpleName(), "  tex total memory=", me);
	}
}
