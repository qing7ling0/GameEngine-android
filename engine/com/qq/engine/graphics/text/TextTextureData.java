package com.qq.engine.graphics.text;

public class TextTextureData {

	public int size;
	
	public int color;
	
	public boolean isBold;
	
	public String text;
	/** 描边颜色 */
	public int strokeColor;
	/** 描边方式 */
	public byte textStrokeStyle;
	
	public String key;

	public static TextTextureData create(TextTextureData data)
	{
		TextTextureData texData = new TextTextureData();
		texData.init(data.text, data.size, data.color, data.isBold, data.strokeColor, data.textStrokeStyle);
		
		return texData;
	}
	
	public void init(String text, int size, int color, boolean isBold, int strokeColor, byte textStrokeStyle)
	{
		this.text = text;
		this.size = size;
		this.color = color;
		this.isBold = isBold;
		this.text = text;
		this.strokeColor = strokeColor;
		this.textStrokeStyle = textStrokeStyle;
		
		StringBuffer sb = new StringBuffer();
		sb.append("t=").append(text).
		append("|s=").append(size).
		append("|c=").append(color).
		append("|b=").append(isBold).
		append("|sc=").append(strokeColor).
		append("|ss=").append(textStrokeStyle);
		this.key = sb.toString();
	}
	
	public String getKey()
	{
		return key;
	}
	
}
