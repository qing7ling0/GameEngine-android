package com.qq.engine.utils.collections;

public class ArrayList {
	protected Object[] array;
	protected int capacity;
	protected int increment;
	protected int size;
	
	public ArrayList()
	{
		this(3,1);
	}
	
	public ArrayList(int capacity, int increment)
	{
		this.capacity	= capacity;
		this.increment	= increment;
		array = new Object[capacity];
	}
	
	public int indexOf(Object obj)
	{
		for(int i = 0; i < size; i++)
		{
			if(array[i] == obj)
			{
				return i;
			}
		}
		return -1;
	}
	
	public Object elementAt(int index)
	{
		return array[index];
	}
	
	public void setElementAt(Object obj, int index)
	{
		array[index] = obj;
	}
	
	public void addElement(Object obj)
	{
		if(size == capacity)
		{
			ensureCapacity(size+1);
		}
		array[size++] = obj;
	}
	
	public void insertAt(int index, Object obj)
	{
		if(size == capacity)
		{
			ensureCapacity(size+1);
		}
		System.arraycopy(array, index, array, index+1, size - index);
		array[index] = obj;
		size++;
	}
	
	public void removeElementAt(int index)
	{
		if (index != --size)
		{
			System.arraycopy(array, index + 1, array, index, size - index);
		}
		array[size] = null;
	}
	
	public void remove(String obj)
	{
		for(int i = 0; i < size; i++)
		{
			if(array[i].equals(obj))
			{
				removeElementAt(i);
				break;
			}
		}
	}
	
	public void remove(Object obj)
	{
		for(int i = 0; i < size; i++)
		{
			if(array[i] == obj)
			{
				removeElementAt(i);
				break;
			}
		}
	}
	
	public int size()
	{
		return size;
	}
	
	public void removeAllElements()
	{
		if (size > 0)
		{
			// Allow for garbage collection.
			for(int i = 0; i < size; i++)
			{
				array[i] = null;
			}
			size = 0;
		}
	 }
	
	public void ensureCapacity(int minCapacity)
	{
		if(minCapacity > capacity)
		{
			capacity += increment;
			Object[] newData = new Object[capacity];
			System.arraycopy(array, 0, newData, 0, size);
			array = newData;
		}
	}
}
