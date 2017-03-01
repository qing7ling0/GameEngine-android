package com.qq.engine.action.instant;

import com.qq.engine.scene.Node;
import com.qq.engine.utils.Debug;

public class CallbackNAction extends CallbackAction {

    protected Class<?>[] partypes;
    
	public static CallbackNAction create(Object target, String selector)
	{
		return new CallbackNAction(target, selector);
	}

    protected CallbackNAction(Object target, String selector) {
		this(target, selector, new Class[] { Node.class });
	}
    
    protected CallbackNAction(Object target, String selector, Class<?>[] p) {
		super(target, selector);
		partypes = p;
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
            invocation.invoke(targetCallback, target);
        } catch (Exception e) {
			Debug.initError(e, false);
        }
    }

    public CallbackNAction copy() {
        return new CallbackNAction(targetCallback, selector);
    }
}
