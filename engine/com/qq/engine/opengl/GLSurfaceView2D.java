package com.qq.engine.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLSurfaceView2D extends GLSurfaceView {

	public GLSurfaceView2D(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setFocusable(true);
		this.setEGLConfigChooser(5, 6, 5, 0, 16, 4);
	}
}
