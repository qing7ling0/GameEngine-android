package com.qq.engine.scene;

import com.qq.engine.drawing.RectangleF;
import com.qq.engine.graphics.GFont;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.text.TextStrokeStyle;
import com.qq.engine.graphics.text.TextTexture;
import com.qq.engine.graphics.text.TextTextureCache;
import com.qq.engine.utils.Debug;
import com.qq.engine.utils.Utils;

public class TextLable extends Node {

	private GFont font;
	private String text;
	
	private int strokeColor;
	
	/** 是否描边 */
	private boolean isStroke;
	
	/** 是否是异步加载资源 */
	private boolean isAsyn;
	
	private Sprite sp;
	private TextTexture textTex;
	
	public static TextLable create(String text)
	{
		return create(text, GFont.getDefaultFont());
	}

	public static TextLable create(String text, int color)
	{
		TextLable tn = new TextLable();
		tn.init(text, GFont.create(GFont.SIZE_MEDIUM, color), false);
		return tn;
	}

	public static TextLable create(String text, GFont font)
	{
		TextLable tn = new TextLable();
		tn.init(text, font, false);
		return tn;
	}
	public static TextLable create(String text, GFont font, boolean isAsyn)
	{
		TextLable tn = new TextLable();
		tn.init(text, font, isAsyn);
		return tn;
	}
	
	public void init(String text, GFont font, boolean isAsyn)
	{
		super.init();
		this.setAnchorPoint(0.5f, 0.5f);
		this.font = font;
		this.font.setAlpha(alpha);
		this.setText(text);
		this.isStroke = false;
		this.strokeColor = 0x0;
		this.isAsyn = isAsyn;
		this.setBold(true);
	}

	public void setTextSize(int size)
	{
		font.setSize(size);
		this.setContentSize(Utils.getStringWidth(text, font), font.lineHeight());
	}
	
	public void setTextColor(int color)
	{
		font.setColor(color);
	}

	public int getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	public boolean isStroke() {
		return isStroke;
	}

	public void setStroke(boolean isStroke) {
		this.isStroke = isStroke;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		super.setAlpha(alpha);
		if (font != null) font.setAlpha(alpha);
	}

	@Override
	public void visit(Graphics g) {
		// TODO Auto-generated method stub
		
		TextTexture tex = null;
		if (isStroke)
		{
			tex = TextTextureCache.getInstance().addText(font, text, strokeColor, TextStrokeStyle.STROKE_RB, isAsyn);
		}
		else
		{
			tex = TextTextureCache.getInstance().addText(font, text, isAsyn);
		}
		
		if (textTex != tex && tex != null || sp == null)
		{
			textTex = tex;
			if (sp == null)
			{
				if (textTex.img == null) Debug.e(this.getClass().getSimpleName(), "  create sprite text=", text);
				sp = Sprite.create(textTex.img);
				sp.setAnchor(Graphics.LEFT|Graphics.TOP);
				addChild(sp);
			}
			else
			{
				sp.initWithImage(textTex.img, new RectangleF(0, 0, textTex.img.getWidth(), textTex.img.getHeight()));
			}
		}
		super.visit(g);
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();
		this.sp = null;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		this.setContentSize(font.stringWidth(text), font.lineHeight());
	}

	/**
	 * @return the font
	 */
	public GFont getFont() {
		return font;
	}
	
	public void setBold(boolean bold)
	{
		if (font != null)
		{
			font.setBold(bold);
		}
	}

	public boolean isAsyn() {
		return isAsyn;
	}

	public void setAsyn(boolean isAsyn) {
		this.isAsyn = isAsyn;
	}
	
}
