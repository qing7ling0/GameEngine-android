package loader.glloader;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.utils.Debug;
import com.qq.engine.utils.Utils;

public abstract class GLResourceLoader {

	public String key;
	
	public GLResource resource;
	
	public LoaderData loaderData;
	
	public Bitmap.Config rgbaConfig; 
	
	/** 是否是异步步加载 */
	public boolean isAsynLoader;
	
	public GLResourceLoader() {
		// TODO Auto-generated constructor stub
		key = "";
		isAsynLoader = true;
		rgbaConfig = Bitmap.Config.ARGB_8888;
		init();
	}

	protected void init() {};
	
	public void loadRes()
	{
		this.loaderData = createLoaderData();
	}
	
	public void loadTex(GL10 gl)
	{
		if (this.resource != null)
			this.resource.load(gl);
	}
	
	public InputStream createSource() { return null; };
	
	public SizeF readTextureSize()
	{
		InputStream is = createSource();
		SizeF size = SizeF.create(0, 0);
		if (is != null)
		{
			try {
				size = ImageCache.readBitmapSize(is);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Debug.initError(e, false);
			}
		}
		
		return size;
	}
	
	public LoaderData createLoaderData()
	{
		Bitmap bm = createBitmap();
		bm = pow2Bitmap(bm);
		
		return LoaderData.create(bm);
	}
	
	protected Bitmap createBitmap()
	{
		Bitmap bm = null;
		InputStream is = createSource();
		if (is != null)
		{
			try {
				bm = ImageCache.createBitmap(is, rgbaConfig);
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debug.printImagesLog();
				Debug.e(this.getClass().getName(), " createBitmap OOM  key=" + key);
				Debug.initError(e, false);
				e.printStackTrace();
			}
		}
		
		return bm;
	}
	
	protected Bitmap pow2Bitmap(Bitmap bm)
	{
		if (bm != null)
		{
			int width = bm.getWidth();
			int height = bm.getHeight();
			int pow2Width = Utils.pow2(width);
			int pow2Height = Utils.pow2(height);
			
			if (width != pow2Width || height != pow2Height)
			{
				Bitmap bit = ImageCache.createBitmap(pow2Width, pow2Height);
				Canvas cav = new Canvas(bit);
				cav.drawBitmap(bm, 0, 0, null);
				bm.recycle();
				bm = bit;
			}
		}
		
		return bm;
	}
	
	public GLResource getResource() {
		return resource;
	}

	public void setResource(GLResource resource) {
		this.resource = resource;
	}

	public void clean()
	{
		if (loaderData != null)
		{
			loaderData.recycle();
			loaderData = null;
		}
	}
	
}
