package com.qq.engine.graphics.image;


import loader.GLImageLoaderManager;
import loader.glloader.GLResourceLoader;
import loader.glloader.LoaderData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.qq.engine.drawing.Color;
import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.Rectangle;
import com.qq.engine.drawing.RectangleF;
import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.opengl.Texture2D;
import com.qq.engine.opengl.TextureCache;
import com.qq.engine.opengl.glutils.Matrix3;

public class Image {
	
	private static float[] vertices = new float[8];
	private static float[] texcoords = new float[8];

	private Texture2D texture;
	
	/** 加载完成 */
	private boolean loadDone;
	
	/** 实际图片的宽，图片打包前的宽 */
	private int mWidth;
	/** 实际图片的高，图片打包前的高 */
	private int mHeight;
	
	private int powWidth;
	private int powHeight;
	
	/** 剪切数据 */
	private Rectangle clipRect;
	/** 是否旋转 Graphics.TRANS_MIRROR_ROT90 */
	private boolean rotated;
	/** 打包后的偏移量 */
	private int offx;
	/** 打包后的偏移量 */
	private int offy;
	
	/** 混合类型 */
	private int blend;
	
	/** 是否置灰  */
	private Image grayImage;
	private GLResourceLoader grayImageLoader;
	
	private GLImageLoaderManager loaderManager;

	protected Image ()
	{
		this(null);
	}
	
	protected Image (GLImageLoaderManager loaderManager)
	{
		this.blend = Graphics.BLEND_NORMAL;
		this.loaderManager = loaderManager;
	}
	
	public static Image create(GLImageLoaderManager loaderManager, GLResourceLoader loader, float scale)
	{
		Image gi = new Image(loaderManager);
		gi.init(loader, scale);
		return gi;
	}

	public static Image create(GLImageLoaderManager loaderManager, Image img, ImageRegion region)
	{
		Image gi = new Image(loaderManager);
		gi.init(img, region);
		return gi;
	}
	
	/**
	 * 主要用于更加JOSN数据创建的Image来创建
	 * @param img
	 * @param region
	 */
	public void init(Image img, ImageRegion region)
	{
		this.clipRect = new Rectangle(region.frameRect.x+img.clipRect.x, region.frameRect.y+img.clipRect.y, region.frameRect.w, region.frameRect.h);
		this.rotated = region.rotated;
		this.texture = img.texture;
		this.mWidth = (int) (region.sourceSize.width / this.texture.scale);
		this.mHeight = (int) (region.sourceSize.height / this.texture.scale);
		this.powWidth = this.texture.pow2Width();
		this.powHeight = this.texture.pow2Height();
		this.offx = region.spriteSourceSize.x;
		this.offy = region.spriteSourceSize.y;
		this.texture.retain();
		if (this.loaderManager != null) this.loaderManager.add(this);
	}

	public void init(GLResourceLoader loader, float scale)
	{
		texture = TextureCache.getInstance().get(loader.key);
		if (texture == null)
		{
			this.texture = new Texture2D(loader, scale);
		}
		else
		{
			this.texture.retain();
		}
		if (!this.texture.isCreateSuc()) return;

		if (this.loaderManager != null) this.loaderManager.add(this);
		this.mWidth = texture.width();
		this.mHeight = texture.height();
		this.powWidth = texture.pow2Width();
		this.powHeight = texture.pow2Height();
		
		this.clipRect = new Rectangle(0, 0, mWidth, mHeight);
		this.rotated = false;
	}
	
	public byte[] readPixel(int transform)
	{
		byte[] source = new byte[texture.getPixels().length];
		for(int i=0; i<source.length; i++)
		{
			source[i] = (byte)(texture.getPixels()[i]==0?0:1);
		}
		int sw = rotated?clipRect.h:clipRect.w;
		int sh = rotated?clipRect.w:clipRect.h;
		
		byte[] pixes = transformPixels(source, texture.width(), clipRect.x, clipRect.y, sw, sh, rotated?Graphics.TRANS_ROT270:Graphics.TRANS_NONE);
		source = null;
		
		pixes = transformPixels(pixes, rotated?sh:sw, 0, 0, rotated?sh:sw, rotated?sw:sh, transform);
		
		return pixes;
	}
	
	public static byte[] getPixels(byte[] source, int stride, int x, int y, int w, int h)
	{
		if ((h+y-1)*w+x-1 >= source.length || x < 0 || y < 0 || w < 0 || h < 0) return null;

		byte[] pixes = new byte[w * h];
		for(int i=0; i<h; i++)
		{
			for(int j=0; j<w; j++)
			{
				pixes[i*w+j] = source[(i+y)*stride+(j+x)];
			}
		}
		
		return pixes;
	}
	
	protected void createGrayImageLoader()
	{
		if (grayImageLoader == null)
		{
			grayImageLoader = new GLResourceLoader() {
				
				Texture2D mTexture2d;
				
				@Override
				public void init() {
					// TODO Auto-generated method stub
					key = texture.key + " clipx=" + clipRect.x + " clipy=" + clipRect.y + " clipw=" + clipRect.w + " cliph=" + clipRect.h
							+ " rotated=" + rotated;
					isAsynLoader = true;
					mTexture2d = texture;
					mTexture2d.retain();
				}
				
				@Override
				public SizeF readTextureSize() {
					// TODO Auto-generated method stub
					return SizeF.create(clipRect.w, clipRect.h);
				}

				@Override
				public Bitmap createBitmap() {
					// TODO Auto-generated method stub
					if (mTexture2d == null) return null;
					LoaderData ld = mTexture2d.getBitmap();
					Bitmap bit = null;
					if (ld == null)
						return null;
					else
						 bit = ld.bm;
					if (bit == null) return null;
					
					int width = clipRect.w;
					int height = clipRect.h;
					
					Bitmap graybm = ImageCache.createBitmap(width, height);
					Paint paint = new Paint();
					ColorMatrix colorMatrix = new ColorMatrix();
			        colorMatrix.setSaturation(0);
			        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
			        paint.setColorFilter(colorMatrixFilter);
					Canvas cav = new Canvas(graybm);
					if (rotated)
					{
						cav.rotate(270);
						cav.translate(-height, 0);
					}
					cav.translate(-clipRect.x, -clipRect.y);
					cav.drawBitmap(bit, 0, 0, paint);
					
					bit.recycle();
					bit = null;
					
					return graybm;
				}

				@Override
				public void clean() {
					// TODO Auto-generated method stub
					super.clean();
					if (mTexture2d != null)
					{
						mTexture2d.recycle();
						mTexture2d = null;
					}
				}
				
			};
		}
	}
	
	protected void createGrayImage()
	{
		createGrayImageLoader();
		grayImage = ImageCache.createImage(null, grayImageLoader, texture.scale);
		if (grayImage != null)
		{
			grayImage.offx = offx;
			grayImage.offy = offy;
		}
	}
	
	public void checkLoadDone()
	{
		if (!loadDone)
		{
			if (texture != null && texture.isLoadFinish())
			{
				loadDone = true;
				if (loaderManager != null)
				{
					loaderManager.loadDone();
				}
			}
		}
	}
	
	public byte[] transformPixels(byte[] source, int stride,  int x, int y, int w, int h, int transform)
	{
		byte[] buf = getPixels(source, stride, x, y, w, h);
		
		int th;
		int tw;
		switch(transform)
		{
		case Graphics.TRANS_NONE:
		case Graphics.TRANS_MIRROR:
		case Graphics.TRANS_MIRROR_ROT180:
		case Graphics.TRANS_ROT180:
			tw = w;
			th = h;
			break;
		default:
			tw = h;
			th = w;
			break;
		}
		if (transform != 0) {
			byte[] trans = new byte[source.length];
			int sp = 0;
			for (int sy = 0; sy < h; sy++) {
				int tx;
				int ty;
				int td;

				switch (transform) {
				case Graphics.TRANS_ROT90:
					tx = tw - sy - 1;
					ty = 0;
					td = tw;
					break;
				case Graphics.TRANS_ROT180:
					tx = tw - 1;
					ty = th - sy - 1;
					td = -1;
					break;
				case Graphics.TRANS_ROT270:
					tx = sy;
					ty = th - 1;
					td = -tw;
					break;
				case Graphics.TRANS_MIRROR:
					tx = tw - 1;
					ty = sy;
					td = -1;
					break;
				case Graphics.TRANS_MIRROR_ROT90:
					tx = tw - sy - 1;
					ty = th - 1;
					td = -tw;
					break;
				case Graphics.TRANS_MIRROR_ROT180:
					tx = 0;
					ty = th - sy - 1;
					td = 1;
					break;
				case Graphics.TRANS_MIRROR_ROT270:
					tx = sy;
					ty = 0;
					td = tw;
					break;
				default:
					throw new RuntimeException("illegal transformation: "
							+ transform);
				}

				int tp = ty * tw + tx;
				for (int sx = 0; sx < w; sx++) {
					trans[tp] = buf[sp++];
					tp += td;
				}
			}
			buf = trans;
		}
		
		return buf;
	}
	
	public int getPixel(int x, int y)
	{
		if (x >= clipRect.w || y >= clipRect.h) return 0;
		return texture.getPixel(x, y);
	}
	
	private Matrix3 drawMatrix = new Matrix3();
	private PointF drawPoint = PointF.zeroPoint();
	private RectangleF drawRect = new RectangleF();
	
	public void draw(Graphics g, float x, float y, float clipx, float clipy, float clipw, float cliph, int anchor, int trans)
	{
		drawMatrix.set(g.matrix);

		if (trans != Graphics.TRANS_NONE)
		{
			float anchorx = 0, anchory = 0;
			
			if ((anchor & Graphics.HCENTER) != 0)
			{
				anchorx = 0.5f;
			}
			else if ((anchor & Graphics.RIGHT) != 0)
			{
				anchorx = 1;
			}
			if ((anchor & Graphics.VCENTER) != 0)
			{
				anchory = 0.5f;
			}
			else if ((anchor & Graphics.BOTTOM) != 0)
			{
				anchory = 1;
			}
			drawMatrix.transform(trans, clipw, cliph, anchorx, anchory);
		}
		else
		{
			if ((anchor & Graphics.HCENTER) != 0)
			{
				x -= clipw / 2;
			}
			else if ((anchor & Graphics.RIGHT) != 0)
			{
				x -= clipw;
			}
			if ((anchor & Graphics.VCENTER) != 0)
			{
				y -= cliph / 2;
			}
			else if ((anchor & Graphics.BOTTOM) != 0)
			{
				y -= cliph;
			}
		}
		
		drawMatrix.translate(x, y);
		
		drawPoint.x = 0;
		drawPoint.y = 0;
		
		drawRect.x = clipx;
		drawRect.y = clipy;
		drawRect.width = clipw;
		drawRect.height = cliph;
		
		getDrawTexcoords(texcoords, drawRect, drawPoint);
		getDrawVertices(vertices, 0, 0, drawRect.width, drawRect.height, drawPoint.x, drawPoint.y, drawMatrix);
		drawInSpriteBatch(g, vertices, texcoords, g.color);
	}
	
	public void drawInSpriteBatch(Graphics g, float[] vertices, float[] texcoords, Color color)
	{
		if (blend != Graphics.BLEND_NORMAL)
		{
			Graphics.glAlphaFunc(blend);
		}
		else
		{
			if (g.color.a < 1)
			{
				Graphics.glAlphaFunc(Graphics.BLEND_ALPHA);
			}
			else
			{
				Graphics.glAlphaFunc(blend);
			}
		}
		Graphics.batch.draw(texture, vertices, texcoords, color);
		if (blend != Graphics.BLEND_NORMAL)
		{
			Graphics.glAlphaFunc(Graphics.BLEND_NORMAL);
		}
	}
	
	public void getDrawTexcoords(float[] texcoords, RectangleF clip, PointF off)
	{
		float clipx, clipy, clipw, cliph;

		clipx = clip.x*texture.scale;
		clipy = clip.y*texture.scale;
		clipw = clip.width*texture.scale;
		cliph = clip.height*texture.scale;
		if (off.x != 0) off.x = off.x*texture.scale;
		if (off.y != 0) off.y = off.y*texture.scale;

		// 获取绘制区域和实际剪切区域的交集
		if (offx != 0)
		{
			clipx -= offx;
			if (clipx < 0) off.x = Math.max(off.x, -clipx);
		}
		if (offy != 0)
		{
			clipy -= offy;
			if (clipy < 0) off.y = Math.max(off.y, -clipy);
		}

		float rx = clipx;
		float ry = clipy;
		if (rx < 0) rx = 0;
		if (ry < 0) ry = 0;
		float rw = clipx+clipw;
		float rh = clipy+cliph;
		if (rw > clipRect.w) rw = clipRect.w;
		if (rh > clipRect.h) rh = clipRect.h;
		clipx = rx;
		clipy = ry;
		clipw = rw-rx;
		cliph = rh-ry;
		if (clipw < 0) clipw = 0;
		if (cliph < 0) cliph = 0;
		
		if (rotated)
		{
			final float cx2 = clipRect.h-clipy-cliph;
			final float cy2 = clipx;
			final float cw2 = cliph;
			final float ch2 = clipw;

			clipx = cx2;
			clipy = cy2;
			clipw = cw2;
			cliph = ch2;
		}
		
		clip.x = clipx;
		clip.y = clipy;
		clip.width = clipw;
		clip.height = cliph;
		
		Graphics.getTexcoords(texcoords, clipx+clipRect.x, clipy+clipRect.y, clipw, cliph, powWidth, powHeight);
	}
	
	public void getDrawVertices(float[] vertices, float sx, float sy, float sw, float sh, float offx, float offy, Matrix3 matrix)
	{
		Matrix3 mat = new Matrix3(matrix);

//		if (rotated)
//		{
//			float temp = sx;
//			sx = sy;
//			sy = temp;
//		}
//		if (sx!=0 || sy!=0) mat.translate(sx, sy);
		
		if (texture.scale != 1) mat.scale(1/texture.scale, 1/texture.scale);
		
		if (offx!=0 || offy!=0) mat.translate(offx, offy);
		
		if (rotated)
		{
			mat.transform(Graphics.TRANS_ROT270, sw, sh, 0, 0);
		}
		Graphics.getVertices(vertices, 0, 0, sw, sh, mat);
	}

	public Texture2D getTexture() {
		return texture;
	}

	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}

	public void setAliasTexParameters()
	{
		texture.setAliasTexParameters();
	}
	
	public void setAntiAliasTexParameters()
	{
		texture.setAntiAliasTexParameters();
	}
	
	public boolean isAntialias()
	{
		return texture.isAntialias();
	}
	
	public int getClipWidth()
	{
		return clipRect.w;
	}
	
	public int getClipHeight()
	{
		return clipRect.h;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}

	public int getPowWidth() {
		return powWidth;
	}

	public int getPowHeight() {
		return powHeight;
	}

	public int getBlend() {
		return blend;
	}

	public void setBlend(int blend) {
		this.blend = blend;
	}
	
	public void recycle()
	{
		if (texture != null)
		{
			texture.recycle();
//			texture = null;
		}
		if (grayImage != null)
		{
			grayImage.recycle();
			grayImage = null;
		}
	}
	
	public void recycleTexturePixels()
	{
		if (texture != null)
		{
			texture.recyclePixels();
		}
	}
	
	public Graphics getGraphics()
	{
		return new Graphics();
	}
	
	public void printLog()
	{
		this.texture.printLog();
	}
	
	public String getDebugInfo()
	{
		return texture.getDebugInfo();
	}
	
	public Image retain()
	{
		this.texture.retain();
		return this;
	}
	
	public boolean createSuc()
	{
		return texture != null && this.texture.isCreateSuc();
	}

	public Image getGrayImage() {
		if (grayImage == null)
		{
			createGrayImage();
		}
		return grayImage;
	}

	public boolean isLoadDone() {
		return loadDone;
	}

	public void setLoadDone(boolean loadDone) {
		this.loadDone = loadDone;
	}

	public GLImageLoaderManager getLoaderManager() {
		return loaderManager;
	}

	public void setLoaderManager(GLImageLoaderManager loaderManager) {
		this.loaderManager = loaderManager;
	}
	
}