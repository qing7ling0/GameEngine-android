package com.qq.engine.utils.coordinate;

public class CoordinateConvertUtil {

	/** 未考虑镜像和旋转 */
	public static CoordinateVo convert(CoordinateVo from, CoordinateVo to)
	{
		to.lastScaleX = from.lastScaleX * from.scaleX;
		to.lastScaleY = from.lastScaleY * from.scaleY;
		to.position.x = from.position.x + to.position.x*to.lastScaleX + to.lastScaleX*to.originPoint.x*(1-to.scaleX);
		to.position.y = from.position.y + to.position.y*to.lastScaleY + to.lastScaleY*to.originPoint.y*(1-to.scaleY);
		return to;
	}
	
}
