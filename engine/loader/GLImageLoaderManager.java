package loader;

import java.util.ArrayList;

import com.qq.engine.action.IUpdate;
import com.qq.engine.action.Scheduler;
import com.qq.engine.graphics.image.Image;

public class GLImageLoaderManager implements IUpdate {

	private ArrayList<Image> loaders;
	
	private int loadCount;
	private int doneCount;
	
	public GLImageLoaderManager() {
		// TODO Auto-generated constructor stub
		loaders = new ArrayList<Image>();
		doneCount = 0;
		Scheduler.getInstance().add(this);
	}
	
	public void add(Image image)
	{
		loaders.add(image);
		loadCount ++;
	}
	
	public float getLoadProgress()
	{
		if (allLoadDone()) return 1;
		if (loadCount > 0)
		{
			int pro = doneCount*100/loadCount;
			return pro/100f;
		}
		return 1;
	}
	
	public void loadDone()
	{
		doneCount++;
	}
	
	public boolean allLoadDone()
	{
		for(Image img : loaders)
		{
			if (!img.isLoadDone())
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void remove(Image load)
	{
		loaders.remove(load);
		loadCount--;
	}
	
	public void recycle()
	{
		Scheduler.getInstance().remove(this);
		if (loaders != null)
		{
			for(int i=0; i<loaders.size(); i++)
			{
				loaders.get(i).recycle();
			}
			loaders.clear();
			loaders = null;
		}
		loadCount = 0;
		doneCount = 0;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		for(int i=0; i<loaders.size(); i++)
		{
			loaders.get(i).checkLoadDone();
		}
	}

}
