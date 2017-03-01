package com.qq.engine.opengl;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import loader.glloader.GLAsyncResourceLoaderHelper;
import loader.glloader.GLResource;
import loader.glloader.GLResourceLoader;
import loader.glloader.GLResourceManage;
import loader.glloader.LoaderData;

import android.opengl.GLUtils;

import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.utils.Debug;
import com.qq.engine.utils.Utils;

public class Texture2D implements GLResource {

	private static IntBuffer buffer;
	private int textureId;
	private int mWidth;
	private int mHeight;
	private int powWidth;
	private int powHeight;
	private boolean antialias;
	public int refCount;
	public String key;
	
	private GLResourceLoader loader;
	private int[] pixels;
	private boolean createSuc;
	
	private boolean loadFinish;
	
	/** 打包图片是，设置的缩放参数 */
	public float scale;
//	private long time;
	
	public Texture2D(GLResourceLoader loader, float scale)
	{
//		time = System.currentTimeMillis();
		this.loader = loader;
		this.scale = scale;
		if (buffer == null) buffer = IntBuffer.allocate(1);
		this.textureId = 0;
		this.readSize();
		if (createSuc)
		{
			this.initLoader();
			this.retain();
			GLResourceManage.getInstance().addLoad(this);
			TextureCache.add(this.loader.key, this);
		}
	}
	
	private void readSize()
	{
		SizeF size = loader.readTextureSize();
		mWidth = (int) (size.width / scale);
		mHeight = (int) (size.height / scale);

		powWidth = Utils.pow2((int)size.width);
		powHeight = Utils.pow2((int)size.height);
		
		createSuc = mWidth>0 && mHeight>0;
		if (!createSuc)
		{
			Debug.e(this.getClass().getSimpleName(), " createSuc fail  key=", loader.key);
		}
	}
	
	private void initLoader()
	{
		if (createSuc)
		{
			loader.setResource(this);
			textureId = 0;
			key = loader.key;
			if (loader.isAsynLoader)
			{
				loadFinish = false;
				GLAsyncResourceLoaderHelper.getInstance().addLoad(loader);
			}
			else
			{
				loader.loadRes();
				loader.loadTex(Graphics.gl10);
				loader.clean();
				loadFinish = true;
			}
		}
	}

	@Override
	public void reLoad(GL10 gl) {
		// TODO Auto-generated method stub
		initLoader();
		
		Debug.i(this.getClass().getSimpleName(), "  reLoad-----  key=", key);
	}

	@Override
	public void load(GL10 gl) {
		// TODO Auto-generated method stub
//		long time = System.currentTimeMillis();
		if (createSuc)
		{
			LoaderData ld = loader.loaderData;
			if (ld.bm != null)
			{
				buffer.position(0);
				// 创建纹理
				gl.glGenTextures(1, buffer);
				textureId = buffer.get(0);
				
				gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);
				// 设置需要使用的纹理
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
//				gl.glCopyTexImage2D(target, level, GL10.GL_COMPRESSED_TEXTURE_FORMATS, x, y, width, height, border)
//				gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, ld.bm, 0);
			
				loadFinish();
			}
		}
//		Debug.e("Texture2d " + key + "  load time = " + (System.currentTimeMillis() - time));
//		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, mWidth, mHeight, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, texData);
	}
	
	protected void loadFinish()
	{
		setAntiAliasTexParameters();
		buffer.position(0);
		loadFinish = true;
	}
	
	public boolean bind()
	{
		if (textureId == 0) return false;
		Graphics.gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		return true;
	}

	public int width()
	{
		return mWidth;
	}
	
	public int height()
	{
		return mHeight;
	}
	
	public int pow2Width()
	{
		return powWidth;
	}
	
	public int pow2Height()
	{
		return powHeight;
	}
	
	public boolean isAntialias()
	{
		return antialias;
	}
	
	public void dispose () {
		if (textureId == 0) return;
		buffer.put(0, textureId);
		buffer.position(0);
		Graphics.gl10.glDeleteTextures(1, buffer);
		textureId = 0;
	}

	public void setTexParameters(int[] texParams)
	{
		Graphics.gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		Graphics.gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, texParams[0]);
		Graphics.gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, texParams[1]);
		Graphics.gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, texParams[2]);
		Graphics.gl10.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, texParams[3]);
	}
	
	public void setAliasTexParameters()
	{
		antialias = false;
		int[] texParams = new int[]{ 
			GL10.GL_NEAREST, 
			GL10.GL_NEAREST, 
			GL10.GL_CLAMP_TO_EDGE, 
			GL10.GL_CLAMP_TO_EDGE };
		setTexParameters(texParams);
	}
	
	public void setAntiAliasTexParameters()
	{
		antialias = true;
		int[] texParams = new int[]{ 
			GL10.GL_LINEAR, 
			GL10.GL_LINEAR, 
			GL10.GL_CLAMP_TO_EDGE, 
			GL10.GL_CLAMP_TO_EDGE };
		setTexParameters(texParams);
	}
	
	public int getPixel(int x, int y)
	{
		getPixels();
		int index = y * mWidth + x;
		if (pixels != null && index < pixels.length)
		{
			return pixels[index];
		}
		return 0;
	}

	public int[] getPixels() {
		if (pixels==null)
		{
			LoaderData ld = loader.createLoaderData();
			if (ld.bm != null)
			{
				pixels = new int[mWidth * mHeight];
				ld.bm.getPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);
				ld.recycle();
				ld = null;
			}
		}
		
		return pixels;
	}
	
	public LoaderData getBitmap()
	{
		if (loader == null) return null;
		LoaderData ld = null;
		synchronized (loader) {
			Debug.d(this.getClass().getSimpleName(), "  LoaderData getBitmap");
			ld = loader.createLoaderData();
		}
		return ld;
	}
	
	public void recyclePixels()
	{
		pixels = null;
	}
	
	public void retain()
	{
		refCount ++;
	}

	public String getDebugInfo()
	{
		return " key=" + key + " refCount=" + refCount;
	}
	
	public float getMemory()
	{
		return (int)(powHeight*powWidth*4*10000000f/(1024*1024))/10000000f;
	}

	public void printLog()
	{
		Debug.d(this.getClass().getSimpleName(), " key=" + key + " refCount=" + refCount + " width="+ mWidth + "  height=" + mHeight + " Memory=" + getMemory() + "M");
	}

	public void recycle()
	{
		this.refCount--;
		if (this.refCount < 1)
		{
			this.pixels = null;
//			Debug.i(this.getClass().getSimpleName(), " recycle  key=", key);
			TextureCache.remove(this);
			GLResourceManage.getInstance().removeRes(this);
			GLAsyncResourceLoaderHelper.getInstance().removeLoad(loader);
			
			loader.setResource(null);
			loader = null;
			this.dispose();
		}
	}

	public int getTextureId() {
		return textureId;
	}

	public boolean isCreateSuc() {
		return createSuc;
	}
	
	public static float[] getVertices(float cx, float cy, float cw, float ch) {
		float x2 = cx + cw;
		float y2 = cy + ch;
		float[] vertices = new float[8];
		vertices[0] = cx;
		vertices[1] = cy;
		vertices[2] = cx;
		vertices[3] = y2;
		vertices[4] = x2;
		vertices[5] = y2;
		vertices[6] = x2;
		vertices[7] = cy;
		return vertices;
	}

	public static float[] getTexcoords(float cx, float cy, float cw, float ch, float tw, float th) {
		
		float x = cx / tw;
		float y = cy / th;
		float x2 = (cx + cw) / tw;
		float y2 = (cy + ch) / th;
		
		float[] texcoords = new float[8];
		texcoords[0] = x;
		texcoords[1] = y;
		texcoords[2] = x;
		texcoords[3] = y2;
		texcoords[4] = x2;
		texcoords[5] = y2;
		texcoords[6] = x2;
		texcoords[7] = y;
		return texcoords;
	}

	public boolean isLoadFinish() {
		return loadFinish;
	}

	public void setLoadFinish(boolean loadFinish) {
		this.loadFinish = loadFinish;
	}
}
