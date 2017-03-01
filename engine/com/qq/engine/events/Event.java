package com.qq.engine.events;

import com.qq.engine.drawing.PointF;
import com.qq.engine.scene.Node;
import com.qq.engine.utils.Debug;


public class Event {
	public static final byte KEY_BACK = android.view.KeyEvent.KEYCODE_BACK;

	static public enum EventType {
		touchDown, touchUp, touchMoved, keyDown, keyUp
	}

	private PointF singlePoint;
	private String singlePointKey;
	
	private PointF[] mulPoints;
	
	private int keyCode;
	
	private boolean handled;
	
	private boolean stop;
	
	private boolean cancelled;
	
	private EventType eventType;

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public PointF getSinglePoint() {
		return singlePoint;
	}

	public void setSinglePoint(PointF singlePoint) {
		this.singlePoint = singlePoint;
	}

	public PointF[] getMulPoints() {
		return mulPoints;
	}

	public String getSinglePointKey() {
		return singlePointKey;
	}

	public void setSinglePointKey(String singlePointKey) {
		this.singlePointKey = singlePointKey;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public void setMulPoints(PointF[] mulPoints) {
		this.mulPoints = mulPoints;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void handled(Node node)
	{
		if (!cancelled)
		{
			if (node.isKeyEnabled() && !handled)
			{
				if (eventType == EventType.keyDown)
				{
					cancelled = node.onKeyDown(keyCode);
				}
				else if (eventType == EventType.keyUp)
				{
					cancelled = node.onKeyUp(keyCode);
				}
				handled = cancelled;
			}
			
			if (node.isTouchEnabled())
			{
//				Debug.i(this, " handle node=" + node);
				
				if (eventType == EventType.touchDown)
				{
					handled = node.onTouchBegan((int)singlePoint.x, (int)singlePoint.y);
					if (handled)
					{
						node.addTouchPointKey(singlePointKey);
						if (node.isSwallowsTouches()) cancelled = true;
					}
				}
				else
				{
					if (node.hasTouchPointKey(singlePointKey))
					{
						if (eventType == EventType.touchMoved && !cancelled)
						{
							node.onTouchMoved((int)singlePoint.x, (int)singlePoint.y);
						}
						else if (eventType == EventType.touchUp)
						{
							if (cancelled)
							{
								node.onTouchInterrupted();
							}
							else
							{
								node.onTouchEnded((int)singlePoint.x, (int)singlePoint.y);
							}
							node.removeTouchPointKey(singlePointKey);
						}
					}
				}
				
			}
		}
		
	}
	
}
