package com.qq.engine.action;

import java.lang.reflect.Method;

import com.qq.engine.utils.Debug;

public class Timer {

    private Object target;
    private String selector;
    private Method invocation;
    
    /** interval in seconds */
    private float interval;
    private float time;
    
    private IUpdateCallback callback;
    
    private boolean over;

    public String getSelector() {
    	return selector;
    }
    
    public static Timer create(Object targ, String s) {
        return create(targ, s, 0);
    }

    public static Timer create(Object t, String s, float seconds) {
    	Timer timer = new Timer();
    	timer.init(t, s, seconds);
    	
    	return timer;
    }
    
    public Timer() {
    }
    
    public static Timer create(IUpdateCallback c, float seconds) {
    	Timer timer = new Timer();
    	timer.init(c, seconds);
    	
    	return timer;
    }
    
    protected void init(IUpdateCallback c, float seconds)
    {
        callback = c;

        interval = seconds;
        time = -1;
    }
    
    protected void init(Object t, String s, float seconds)
    {
    	
        target = t;
        selector = s;

        interval = seconds;
        time = -1;

        try {
            Class<?> cls = target.getClass();
            invocation = cls.getMethod(s, Float.TYPE);
        } catch (NoSuchMethodException e) {
    		e.printStackTrace();
    	}
    }
    
    public void setInterval(float i) {
        interval = i;
    }

    public float getInterval() {
        return interval;
    }

    public void update(float dt) {
    	if (!over)
    	{
	        if (time == -1) {
	            time = 0;
	        } else {
	            time += dt;
	        }
	        if (time >= interval) {
	        	if(callback != null) {
	        		callback.updateCallback(time);
	        	} else {
	                try {
	                    invocation.invoke(target, time);
	                } catch (Exception e) {
	                    e.printStackTrace();
						Debug.initError(e, false);
	                }        		
	        	}
	            time = 0;
	        }
    	}
    }

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
    
}
