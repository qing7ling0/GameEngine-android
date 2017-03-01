package com.qq.engine.opengl.loader;

import java.io.InputStream;

import loader.glloader.GLResourceLoader;
import loader.glloader.LoaderData;

import android.graphics.Bitmap;

import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.image.ImageCache;

public class ColorLoader extends GLResourceLoader {

	private int width;
	private int height;
	
	public ColorLoader(String key, int width, int height) {
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		this.key = key;
	}

	@Override
	public InputStream createSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SizeF readTextureSize() {
		// TODO Auto-generated method stub
		return SizeF.create(width, height);
	}

	@Override
	public Bitmap createBitmap() {
		// TODO Auto-generated method stub
		return ImageCache.createBitmap(width, height);
	}

}
