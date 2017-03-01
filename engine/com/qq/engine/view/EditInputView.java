package com.qq.engine.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class EditInputView extends FrameLayout {

	public EditInputView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setFocusableInTouchMode(true);
	}

	public EditInputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EditInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean requestChildRectangleOnScreen(View child,
			Rect rectangle, boolean immediate) {
		EditTextChangeCheck.changed = true;
//		Debug.i("MyFrameLayout  requestChildRectangle");
		return super.requestChildRectangleOnScreen(child, rectangle, immediate);
	}

}
