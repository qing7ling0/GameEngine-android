package com.qq.engine.graphics;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class GFont {
	public static final byte SIZE_SMALL              = 20;
	public static final byte SIZE_MEDIUM             = 25;
	public static final byte SIZE_LARGE              = 27;
	public static final byte SIZE_LARGE_2            = 30;
	public static final byte SIZE_LARGEST            = 40;
	
	private static Paint paint;
    private byte size;
    private boolean isBold;
    private int color;
    private short alpha;

    protected GFont(int size, int color)
    {
    	this.size = (byte) size;
    	this.color = color;
    	this.isBold = true;
    	if (paint == null)
    	{
	    	paint = new Paint();
    	}
    }
    
	public static GFont create(int size)
	{
	    return create(size, 0x0);
	}
    
	public static GFont create(GFont font)
	{
		GFont gf = new GFont(font.size, font.getColor());
		gf.setBold(font.isBold);
		gf.setAlpha(font.alpha);
		
	    return gf;
	}
    
	public static GFont create(int size, int color)
	{
	    return new GFont(size, color);
	}

	public static GFont getDefaultFont()
	{
	    return create(SIZE_MEDIUM);
	}
	public static GFont smallFont()
	{
	    return create(SIZE_SMALL);
	}
	public static GFont mediumFont()
	{
	    return create(SIZE_MEDIUM);
	}
	public static GFont largeFont()
	{
	    return create(SIZE_LARGE);
	}
	public static GFont largerFont()
	{
	    return create(SIZE_LARGEST);
	}
	
	public int charWidth( char[] ch, int offset, int length )
	{
		paint.setFakeBoldText(isBold);
		paint.setTextSize(size);
	    return (int) Math.ceil( paint.measureText( ch, offset, length ) )+0;
	}
	
	public int charWidth( char ch )
	{
	    return this.charWidth( new char[]{ ch }, 0, 1 );
	}
	
	public int lineHeight()
	{
		paint.setFakeBoldText(isBold);
		paint.setTextSize(size);
		FontMetrics fm = paint.getFontMetrics();  
	    return (int) Math.ceil(fm.descent - fm.ascent)+2;
	}
	
	public int getSize()
	{
	    return this.size;
	}
	
	public void setSize(int size)
	{
		this.size = (byte) size;
	}
	
	public int stringWidth( String str )
	{
		paint.setFakeBoldText(isBold);
		paint.setTextSize(size);
	    return (int) Math.ceil(paint.measureText(str));
	}
	
	public int substringWidth( String str, int offset, int len )
	{
		paint.setFakeBoldText(isBold);
		paint.setTextSize(size);
	    return (int) Math.ceil(paint.measureText( str, offset, len )) + 0;
	}

	public boolean isBold() {
		return isBold;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getAlpha() {
		return alpha;
	}

	/** 0-255 */
	public void setAlpha(int alpha) {
		this.alpha = (short) alpha;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (this == o) return true;
		if (o instanceof GFont)
		{
			GFont font = (GFont)o;
			return font.size==size && font.isBold == isBold && font.color==color && font.alpha==alpha;
		}
		return false;
	}
	
	public String getKey()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("size=").append(size);
		sb.append("isBold=").append(isBold);
		sb.append("color").append(color);
		
		return sb.toString();
	}
}
