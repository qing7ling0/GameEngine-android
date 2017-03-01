package com.qq.engine.utils.collections;

import java.util.Hashtable;



public class VectorHash {
	public static Integer NULL_OBJ = new Integer(1);
	public Hashtable	hash;
	public ArrayList	vkeys;
	public int maxCount;
		
	public VectorHash(int maxCount)
	{
		this.maxCount = maxCount;
		hash 	= new Hashtable(maxCount);
		vkeys	= new ArrayList(maxCount,3);
	}
	
	public void put(String key, Object obj)
	{
		if(obj == null)
		{
			obj = NULL_OBJ;
		}
		hash.put(key, obj);
		vkeys.addElement(key);
		if(vkeys.size() > maxCount)
		{
			remove(0);
		}
	}
	
	public void remove(int index)
	{
		Object key = vkeys.elementAt(index);
		vkeys.removeElementAt(index);
		hash.remove(key);
	}
	
	public void remove(String key)
	{
		vkeys.remove(key);
		hash.remove(key);
	}
	
	public Object getkey(int index){
		return vkeys.elementAt(index);
	}
	
	public Object getByIndex(int index)
	{
		Object key = vkeys.elementAt(index);
		Object obj = hash.get(key);
		if(obj == NULL_OBJ)
		{
			return null;
		}
		return obj;
	}
	
	public Object getByKey(String key)
	{
		Object obj = hash.get(key);
		if(obj == NULL_OBJ)
		{
			return null;
		}else{
		}
		return obj;
	}
	
	public boolean containsKey(String key)
	{
		return hash.containsKey(key);
	}
	
	public void removeAll()
	{
		vkeys.removeAllElements();
		hash.clear();
	}
	
	public int size()
	{
		return vkeys.size();
	}
}
