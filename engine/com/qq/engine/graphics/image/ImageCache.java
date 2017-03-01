package com.qq.engine.graphics.image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import loader.GLImageLoaderManager;
import loader.glloader.GLResourceLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qq.engine.GameDriver;
import com.qq.engine.drawing.SizeF;
import com.qq.engine.opengl.loader.ColorLoader;

public class ImageCache {

	private static ImageCache instance = new ImageCache();
	
	public static Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

	public ImageCache() {
	}

	public Image get(String key) {
		return null;
	}

	public static Image createImage(GLImageLoaderManager loaderManager, GLResourceLoader loader) {

		return createImage(loaderManager, loader, 1);
	}

	public static Image createImage(GLImageLoaderManager loaderManager, GLResourceLoader loader, float scale) {

		Image gi = Image.create(loaderManager, loader, scale);
		if (gi != null && !gi.createSuc())
		{
			gi = null;
		}

		return gi;
	}

	public static Image createImage(GLImageLoaderManager loaderManager, String key, int width, int height) {
		return ImageCache.createImage(loaderManager, new ColorLoader(key, width, height));
	}

	public static ImagePacker createImagePacker(GLImageLoaderManager loaderManager, GLResourceLoader loader, InputStream packerIs)
	{
		ImagePacker ip = ImagePacker.create(loaderManager, loader, packerIs);
		if (!ip.getPackerData().createOk) return null;
		return ip;
	}

	public static Bitmap createBitmap(InputStream stream) throws IOException {
		return createBitmap(stream, BITMAP_CONFIG);
	}
	
	public static Bitmap createBitmap(InputStream stream, Bitmap.Config config) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = config;
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		return BitmapFactory.decodeStream(stream, null, opt);
	}
	
	public static Bitmap createBitmap(int resId, Bitmap.Config config) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = config;
		opt.inDensity = density;

		return BitmapFactory.decodeResource(
				GameDriver.ANDROID_ACTIVITY.getResources(), resId, opt);
	}
	
	private static native ByteBuffer load (long[] nativeData, byte[] buffer, int offset, int len, int requestedFormat);

	public static SizeF readBitmapSize(InputStream stream) throws IOException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(stream, null, opt);
			
		return SizeF.create(opt.outWidth, opt.outHeight);
	}

	public static int density;

//	public static Image createImage(Image image, RectangleF clip) {
//		ImageRegion ir = new ImageRegion();
//		ir.frameRect = new Rectangle();
//		ir.frameRect.x = (int) clip.x;
//		ir.frameRect.y = (int) clip.y;
//		ir.frameRect.w = (int) clip.width;
//		ir.frameRect.h = (int) clip.height;
//		
//		Image im = Image.create(image, ir);
//
//		return im;
//	}
	
	public static Bitmap createBitmap(int width, int height)
	{
		return Bitmap.createBitmap(width, height, BITMAP_CONFIG);
	}
	
	public void paintImageLog()
	{
//		for(int i=0; i<)
	}
}
