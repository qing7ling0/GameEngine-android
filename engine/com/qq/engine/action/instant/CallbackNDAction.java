package com.qq.engine.action.instant;

import com.qq.engine.scene.Node;
import com.qq.engine.utils.Debug;

public class CallbackNDAction extends CallbackNAction {

	protected Object[] data;
	
	public static CallbackNDAction create(Object target, String selector, Object... data)
	{
		Class<?>[] cls = new Class<?>[data.length];
		for(int i=0; i<cls.length; i++)
		{
			cls[i] = data[i].getClass();
		}
		
		return new CallbackNDAction(target, selector, cls, data);
	}
	
	protected CallbackNDAction(Object target, String selector, Class<?>[] classes, Object... data) {
		super(target, selector, classes);
		this.data = data;
	}
	
	@Override
	protected void callback() {
		try {
        	try {
                Class<?> cls = targetCallback.getClass();
                invocation = cls.getMethod(selector, partypes);
        	} catch (NoSuchMethodException e) {
        		e.printStackTrace();
        	}
			invocation.invoke(targetCallback, data);
		} catch (Exception e) {
			Debug.initError(e, false);
		}
	}
	

    public CallbackNDAction copy() {
        return new CallbackNDAction(targetCallback, selector, partypes, data);
    }
}
