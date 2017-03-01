package com.qq.engine.opengl;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.opengles.GL10;

public class GLResourceHelper {
	
	private ConcurrentLinkedQueue<GLResource> taskQueue;
	private ArrayList<GLResource>  reloadList;

	public GLResourceHelper() {
		taskQueue = new ConcurrentLinkedQueue<GLResource>();
		reloadList = new ArrayList<GLResourceHelper.GLResource>();
	}

    public interface GLResource {
    	void load(GL10 gl);
    	void reLoad(GL10 gl);
    }
    
    private static GLResourceHelper instance = new GLResourceHelper();

    public static GLResourceHelper getInstance() {
        return instance;
    }

    public void addLoad(GLResource res) {
		reloadList.add(res);
	}
    
    public void removeRes(GLResource res)
    {
    	reloadList.remove(res);
    }

    public void reLoad(GL10 gl)
    {
    	for(int i=0; i<reloadList.size(); i++)
    	{
    		reloadList.get(i).reLoad(gl);
    	}
    }

	public void update(GL10 gl) {
		if(taskQueue.size() > 0) {
	
			GLResource task;
			if((task = taskQueue.poll()) != null) {
				task.load(gl);
			}
		}
	}

}
