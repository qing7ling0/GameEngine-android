package com.qq.engine.graphics;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 把代码放到游戏线程中运行
 * 
 * @author wuqingqing
 *
 */

public class GGamePerform {
	
    private static GGamePerform instance = new GGamePerform();

    public static GGamePerform getInstance() {
        return instance;
    }
    
    public interface GPerformTask {
    	void perform();
    }
    
	
	private ConcurrentLinkedQueue<GPerformTask> taskQueue;

	public GGamePerform() {
		taskQueue = new ConcurrentLinkedQueue<GPerformTask>();
	}

    public void perform(GPerformTask task) {
		taskQueue.add(task);
	}


	public void update() {
		if(taskQueue.size() > 0) {
	
			GPerformTask task;
			while((task = taskQueue.poll()) != null) {
				task.perform();
			}
		}
	}
}
