package com.qq.engine.drawing;

public class Rectangle {
	public int x,y,w,h;
	
	public Rectangle()
	{}
	
	public Rectangle(float x, float y, float w, float h)
	{
		this((int)x, (int)y, (int)w, (int)h);
	}
	
	public Rectangle(RectangleF rectf)
	{
		this(rectf.x, rectf.y, rectf.width, rectf.height);
	}
	
	public Rectangle(int x,int y,int w,int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean collision(int x1, int y1, int w1, int h1)
	{
		return collision(x1,y1,w1,h1,  x,y,w,h);
	}
	
	public boolean collision(Rectangle rect)
	{
		return collision(rect.x,rect.y,rect.w,rect.h);
	}
	
	public int getBottom()
	{
		return y+h;
	}
	
	public int getRight()
	{
		return x+w;
	}
	
	public static boolean collision(Rectangle desc,Rectangle tar){
		return collision(desc.x,desc.y,desc.w,desc.h,tar.x,tar.y,tar.w,tar.h);
	}
	
	/**
	 * kalter 检测碰撞
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
	 * @return
	 * 
	 *  if(rect1.right <= rect2.left) return FALSE;
		if(rect1.left >= rect2.right ) return FALSE;

		if(rect1.bottom <= rect2.top ) return FALSE;
		if(rect1.top >= rect2.bottom ) return FALSE;

		return TRUE;

	 */
	public static boolean collision(float x1, float y1, float w1, float h1, float x2,
			float y2, float w2, float h2) {	
		if(x1+w1 <= x2) return false;
		if(x1 >= x2+w2) return false;
		if(y1+h1 <= y2) return false;
		if(y1 >= y2+h2) return false;
		
		return true;
	}
	
	public static boolean isIn(int x,int y, Rectangle rect)
	{
		return isIn(x,y,rect.x,rect.y,rect.w,rect.h);
	}
	
	public static boolean isIn(int x,int y, int[] rect)
	{
		return isIn(x,y,rect[0],rect[1],rect[2],rect[3]);
	}

	public static boolean isIn(Rectangle from, Rectangle to)
	{
		return collision(from.x, from.y, from.w, from.h, to.x, to.y, to.w, to.h);
	}
	public static boolean isIn(int x,int y, int rx, int ry, int rw, int rh)
	{
		if(x < rx || y < ry || x > rx+rw || y > ry+rh)
		{
			return false;
		}
		return true;
	}
	
	public static Rectangle merge(Rectangle r1, Rectangle r2)
	{
		int x1 = r1.x,y1=r1.y,x2 = r1.x+r1.w, y2 = r1.y+r1.h;
		if(r2.x < x1)	x1 = r2.x;
		if(r2.y < y1)	y1 = r2.y;
		if(r2.x+r2.w > x2)	x2 = r2.x+r2.w;
		if(r2.y+r2.h > y2)	y2 = r2.y+r2.h;
		return new Rectangle(x1,y1,x2-x1,y2-y1);
	}
	
	
}
