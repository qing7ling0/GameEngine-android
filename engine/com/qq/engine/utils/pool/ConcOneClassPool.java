package com.qq.engine.utils.pool;

import com.qq.engine.utils.collections.ConcNodeCachingStack;

public abstract class ConcOneClassPool<T> {
	private ConcNodeCachingStack<T> objs;

	public ConcOneClassPool() {
		objs = new ConcNodeCachingStack<T>();
	}

	protected abstract T allocate();
	
	public T get() {
		T ret = objs.pop();
		if(null == ret) {
			ret = allocate();
		}

		return ret;
	}
	
	public void free(T obj) {
		objs.push(obj);
	}
}
