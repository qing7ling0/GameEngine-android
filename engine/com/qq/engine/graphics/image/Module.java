package com.qq.engine.graphics.image;

import com.qq.engine.drawing.RectangleF;

public class Module {

	public short		x;
	public short		y;
	public short		transRotate;
	public float		scaleX;
	public float		scaleY;
	public short		angle;
	public RectangleF	clip;

	/**
	 * 转换moduleRotate值
	 * 
	 * */
	public void translateRotate()
	{
//		switch(transRotate)
//		{
//		case Graphics.TRANS_MIRROR:
//			scaleX *= -1;
////			x -= clip.width;
//			break;
//		case Graphics.TRANS_MIRROR_ROT90:
//			scaleX *= -1;
//			angle += 90;
////			x -= clip.width;
////			y -= clip.height;
//			break;
//		case Graphics.TRANS_MIRROR_ROT180:
//			scaleX *= -1;
//			angle += 180;
//			y -= clip.height;
//			break;
//		case Graphics.TRANS_MIRROR_ROT270:
//			scaleX *= -1;
//			angle += 270;
//			break;
//		case Graphics.TRANS_ROT90:
//			angle += 90;
//			y -= clip.height;
//			break;
//		case Graphics.TRANS_ROT180:
//			angle += 180;
//			x -= clip.width;
//			y -= clip.height;
//			break;
//		case Graphics.TRANS_ROT270:
//			angle += 270;
//			x -= clip.width;
//			break;
//		}
	}
}
