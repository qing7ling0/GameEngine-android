package com.qq.engine.events;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.qq.engine.drawing.PointF;
import com.qq.engine.events.Event.EventType;
import com.qq.engine.scene.Node;
import com.qq.engine.utils.Debug;
import com.qq.engine.utils.collections.ConcNodeCachingLinkedQueue;
import com.qq.engine.view.Screen;

public class EventsDispatcher {

    private final ConcNodeCachingLinkedQueue<Event> eventQueue = new ConcNodeCachingLinkedQueue<Event>();
    
    
	private static EventsDispatcher instance = new EventsDispatcher();
    
	private boolean dispatchEvents;
	
	public EventsDispatcher()
	{
		dispatchEvents = true;
	}
	
	public static EventsDispatcher getInstance()
	{
		return instance;
	}
	    
	public boolean getDispatchEvents() {
	    return dispatchEvents;
	}
	
	public void setDispatchEvents(boolean dispatchEvents) {
	    this.dispatchEvents = dispatchEvents;
	}
	
	/**
	 * 创建触摸的事件数据
	 * @param me
	 * @return
	 */
	public static Event createTouchEvent(MotionEvent me)
	{
		Event ev = new Event();
//		PointF point = new PointF(me.getX(), me.getY());
		PointF point = new PointF(Screen.screenConvertToGameX(me.getX()), Screen.screenConvertToGameY(me.getY()));

		int action = me.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		String pKey = me.getPointerId(0)+"touch";
		
		ev.setSinglePoint(point);
		ev.setSinglePointKey(pKey);
		boolean ok = false;
		switch (actionCode)
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			ok = true;
			ev.setEventType(EventType.touchDown);
			break;
		case MotionEvent.ACTION_MOVE:
			ok = true;
			ev.setEventType(EventType.touchMoved);
			break;
		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_POINTER_UP:
			ok = true;
			ev.setEventType(EventType.touchUp);
			break;
		}
		if (ok) return ev;
		else return null;
	}
	
	
	/**
	 * 创建按键事件数据
	 * @param ke
	 * @return
	 */
	public static Event createKeyEvent(KeyEvent ke)
	{
		Event ev = new Event();
		ev.setKeyCode(ke.getKeyCode());
		switch (ke.getAction())
		{
		case KeyEvent.ACTION_DOWN:
			ev.setEventType(EventType.keyDown);
			break;
		case KeyEvent.ACTION_UP:
			ev.setEventType(EventType.keyUp);
			break;
		}
		
		return ev;
	}

	public void queueEvent(MotionEvent event) {
    	if(dispatchEvents)
    	{
    		if(event.getAction() ==  MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP)
    		{
//    			Debug.e(this, " queueMotionEvent  touchEnded");
    		}
	    	// copy event for queue
	    	MotionEvent eventForQueue  = MotionEvent.obtain(event);

	    	Event ev = createTouchEvent(eventForQueue);
	    	if (ev != null) eventQueue.push(ev);
    	}
    }
	
	public boolean queueEvent(KeyEvent event) {
		// copy event for queue
		
		KeyEvent eventForQueue = new KeyEvent(event);

    	eventQueue.push(createKeyEvent(eventForQueue));
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		return false;
	}
	
	public void update(Node node)
	{
		Event event = null;
		while( (event = eventQueue.poll()) != null)
    	{
			if (node != null)
			{
				node.handle(event);
				if (event.getEventType() == EventType.touchUp) break;
			}
    	}
	}
}
