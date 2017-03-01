package com.qq.engine.drawing;

public class RectangleF {
	public float x;
	public float y;
	public float width;
	public float height;
	
	public RectangleF()
	{
		
	}
	
	public RectangleF (float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public RectangleF (RectangleF rect)
	{
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
	}
	
	public RectangleF (Rectangle rect)
	{
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.w;
		this.height = rect.h;
	}
	
	/**
	 * 检测某一点是不是在矩形内
	 * @param point
	 * @param rect
	 * @return
	 */
	public static boolean isIn(PointF point, RectangleF rect)
	{
		return isIn(new RectangleF(point.x, point.y, 0, 0), rect);
	}
	public static boolean isIn(float x, float y, RectangleF rect)
	{
		return isIn(new PointF(x, y), rect);
	}
	public static boolean isIn(float px, float py, float x, float y, float w, float h)
	{
		return isIn(new PointF(px, py), new RectangleF(x, y, w, h));
	}

	public static boolean isIn(RectangleF from, RectangleF to) {	
		
		return rectangle2DCollision(from.x, from.y, from.width, from.height, to.x, to.y, to.width, to.height);
	}
	
	/**
	 * 检测碰撞
	 * 
	 * @param ax
	 *            a矩形左上角x坐标
	 * @param ay
	 *            a矩形左上角x坐标
	 * @param aw
	 *            a矩形宽度
	 * @param ah
	 *            b矩形高度
	 * @param bx
	 *            a矩形左上角x坐标
	 * @param by
	 *            a矩形左上角x坐标
	 * @param bw
	 *            b矩形高度
	 * @param bh
	 *            b矩形高度
	 * @return boolean

	 */
	public static boolean rectangle2DCollision(float ax, float ay, float aw, float ah, float bx,
			float by, float bw, float bh) {	
		if(ax+aw <= bx) return false;
		if(ax >= bx+bw) return false;
		if(ay+ah <= by) return false;
		if(ay >= by+bh) return false;
		return true;
	}
	
	public static RectangleF intersection(RectangleF source, RectangleF target)
	{
		return intersection(source.x, source.y, source.width, source.height, target.x, target.y, target.width, target.height);
	}

	/**
	 * 
	 * 获取2个矩形的交集
	 * 
	 * @param sx
	 * @param sy
	 * @param sw
	 * @param sh
	 * @param tx
	 * @param ty
	 * @param tw
	 * @param th
	 * @return
	 */
	public static RectangleF intersection(float sx, float sy, float sw, float sh, float tx, float ty, float tw, float th)
	{
		if (rectangle2DCollision(sx, sy, sw, sh, tx, ty, tw, th))
		{
			float x = sx > tx ? sx : tx;
			float y = sy > ty ? sy : ty;
			float w = sx+sw > tx+tw ? tx+tw : sx+sw;
			float h = sy+sh > ty+th ? ty+th : sy+sh;
			
			return new RectangleF(x, y, w-x, h-y);
		}
		
		return new RectangleF(0, 0, 0, 0);
	}
}
