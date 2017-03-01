package com.qq.engine.scene;

import com.qq.engine.drawing.Color;
import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.SizeF;


public class NodeProperty {

	protected float px;
	protected float py;
	
	protected float width;
	protected float height;
	
	protected float scaleX;
	protected float scaleY;
	protected boolean visible;
	protected short alpha;
	protected float rotation;
	protected Color color;
	protected float skewX;
	protected float skewY;
	
	public NodeProperty()
	{
		visible = true;
		scaleX = 1;
		scaleY = 1;
		alpha = 255;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	public void setScaleX(float scalex) {
		this.scaleX = scalex;
	}
	public float getScaleY() {
		return scaleY;
	}
	public void setScaleY(float scaley) {
		this.scaleY = scaley;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public int getAlpha() {
		return alpha;
	}
	public void setAlpha(int alpha) {
		this.alpha = (short) alpha;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public Color getColor() {
		if (color == null) color = new Color(1, 1, 1, 1);
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public float getSkewX() {
		return skewX;
	}
	public void setSkewX(float skewX) {
		this.skewX = skewX;
	}
	public float getSkewY() {
		return skewY;
	}
	public void setSkewY(float skewY) {
		this.skewY = skewY;
	}


	public float getWidth() {
		return width;
	}


	public void setWidth(float width) {
		this.width = width;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getPositionX()
	{
		return px;
	}
	
	public float getPositionY()
	{
		return py;
	}

	public void setPosition(PointF position)
	{
		setPosition(position.x, position.y);
	}
	public void setPosition(float x, float y)
	{
		px = x;
		py = y;
	}
	public void setPositionX(float x)
	{
		px = x;
	}
	public void setPositionY(float y)
	{
		py = y;
	}

	public SizeF getSize() {
		return SizeF.create(width, height);
	}

	public void setSkew(float skewX, float skewY)
	{
		setSkewX(skewX);
		setSkewY(skewY);
	}

	public void setScale(float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}
	
	public PointF getPosition() {
		return PointF.create(px, py);
	}
}
