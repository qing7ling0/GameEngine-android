package com.qq.engine.action.instant;

import java.lang.reflect.Method;

import com.qq.engine.scene.NodeProperty;
import com.qq.engine.utils.Debug;

public class CallbackAction extends InstantAction {
	protected Object targetCallback;
    protected String selector;

    protected Method invocation;
    
	public static CallbackAction create(Object target, String selector) {
		// TODO Auto-generated constructor stub
		return new CallbackAction(target, selector);
	}

	public CallbackAction (Object target, String selector) {
		super();
		this.targetCallback = target;
		this.selector = selector;
	}
	
	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		callback();
	}

	protected void callback()
	{
        try {
            try {
                Class<?> cls = targetCallback.getClass();
                invocation = cls.getMethod(selector);
        	} catch (NoSuchMethodException e) {
        		e.printStackTrace();
        	}
            invocation.invoke(targetCallback);
        } catch (Exception e) {
        	e.printStackTrace();
			Debug.initError(e, false);
        	Debug.e("CallbackAction callback " + this.selector);
        }
	}

    public CallbackAction copy() {
        return new CallbackAction(targetCallback, selector);
    }

}
