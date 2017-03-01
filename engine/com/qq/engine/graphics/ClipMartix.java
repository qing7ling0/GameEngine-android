package com.qq.engine.graphics;

import com.qq.engine.drawing.Point;
import com.qq.engine.view.Screen;

/**
 * 区域剪切
 * @author wuqingqing
 *
 */
public class ClipMartix {

	public static float clipScaleX = Screen.SCALE;
	public static float clipScaleY = Screen.SCALE;
	public static Point clipTranslatePoint = Point.create(0, 0);
	public static boolean clipTrans180 = true;
	
}
