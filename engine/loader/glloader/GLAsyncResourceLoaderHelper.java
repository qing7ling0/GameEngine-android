package loader.glloader;


import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.opengles.GL10;

import com.qq.engine.utils.Debug;

public class GLAsyncResourceLoaderHelper implements Runnable {
	
	private boolean isRunning;
	private ConcurrentLinkedQueue<GLResourceLoader>  loaderResList;
	private ConcurrentLinkedQueue<GLResourceLoader>  loaderTexList;
	private Thread thread;
	private boolean pause;
	private boolean canLoad;
	
	private GLResourceLoader currentLoader;

	public GLAsyncResourceLoaderHelper() {
		loaderResList = new ConcurrentLinkedQueue<GLResourceLoader>();
		loaderTexList = new ConcurrentLinkedQueue<GLResourceLoader>();
		thread = new Thread(this);
		start();
	}
    
    private static GLAsyncResourceLoaderHelper instance = new GLAsyncResourceLoaderHelper();

    public static GLAsyncResourceLoaderHelper getInstance() {
        return instance;
    }

    public void addLoad(GLResourceLoader data) {
		synchronized (loaderResList) {
			loaderResList.add(data);
		}
	}
    
    public void removeLoad(GLResourceLoader data)
    {
		synchronized (loaderResList) {
			loaderResList.remove(data);
		}
    }
	
	public void start()
	{
		if (!isRunning)
		{
			isRunning = true;
			thread.start();
		}
	}

	public void stop()
	{
		isRunning = false;
		clean();
	}
	
	public void pause()
	{
		pause = true;
	}
	
	public void resume()
	{
		pause = false;
	}
	
	public boolean isEmpty()
	{
		int count = 0;
		synchronized (loaderResList) {
			count = loaderResList.size();
		}
		synchronized (loaderTexList) {
			count += loaderTexList.size();
		}
		return count==0;
	}

	public int getLoaderResCount()
	{
		int count = 0;
		synchronized (loaderResList) {
			count = loaderResList.size();
		}
		
		return count;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning)
		{
			int size = 0;
			synchronized (loaderTexList) {
				size = loaderTexList.size();
			}
			if (!pause && canLoad && size < 2)
			{
				loadRes();

				if (currentLoader != null) {
					currentLoader.loadRes();
					synchronized (loaderTexList) {
						loaderTexList.add(currentLoader);
						currentLoader = null;
					}
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Debug.initError(e, false);
			}
		}
	}
	
	public void loadRes()
	{
		synchronized (loaderResList) {
			if(loaderResList.size() > 0 && currentLoader == null) {
				currentLoader  = loaderResList.poll();
			}
		}
	}
	
	public void loadTexture(GL10 gl)
	{
		synchronized (loaderTexList) {
			if (loaderTexList.size() > 0)
			{
				GLResourceLoader data  = loaderTexList.poll();
				data.loadTex(gl);
				data.clean();
			}
		}
	}
	
	public void clean()
	{
		synchronized (loaderResList) {
			loaderResList.clear();
		}
		
		synchronized (loaderTexList) {
			while(loaderTexList.size() > 0)
			{
				GLResourceLoader data  = loaderTexList.poll();
				data.clean();
			}
			loaderTexList.clear();
		}
	}

	public boolean isCanLoad() {
		return canLoad;
	}

	public void setCanLoad(boolean canLoad) {
		this.canLoad = canLoad;
	}

}
