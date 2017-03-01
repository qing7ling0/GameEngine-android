package loader.glloader;

import android.graphics.Bitmap;

public abstract class GLResourceLoaderData {

	public String key;
	public Bitmap.Config rgbaConfig; 
	
	/** 是否是异步步加载 */
	public boolean isAsynLoader;
	
	public abstract GLResourceLoader createGLResourceLoader();
}
