package loader.glloader;

import android.graphics.Bitmap;

public class LoaderData {
	
	public Bitmap bm;

	public static LoaderData create(Bitmap bitmap) {
		// TODO Auto-generated constructor stub
		return new LoaderData(bitmap);
	}
	
	public LoaderData(Bitmap bitmap)
	{
		this.bm = bitmap;
	}
	
	public void recycle()
	{
		if (bm != null)
		{
			bm.recycle();
			bm = null;
		}
	}

}
