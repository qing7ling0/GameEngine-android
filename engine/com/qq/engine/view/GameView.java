package com.qq.engine.view;

import com.qq.engine.events.EventsDispatcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class GameView extends FrameLayout {

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setFocusableInTouchMode(true);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		EventsDispatcher.getInstance().queueEvent(event);
		
		return true;
	}

}
