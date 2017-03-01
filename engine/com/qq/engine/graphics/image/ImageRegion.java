package com.qq.engine.graphics.image;

import com.qq.engine.drawing.Rectangle;
import com.qq.engine.drawing.SizeF;

public class ImageRegion {
	public String filename;
	public Rectangle frameRect;
	public boolean rotated;
	public boolean trimmed;
	public Rectangle spriteSourceSize;
	public SizeF sourceSize;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Rectangle getFrameRect() {
		return frameRect;
	}
	public void setFrameRect(Rectangle frameRect) {
		this.frameRect = frameRect;
	}
	public boolean isRotated() {
		return rotated;
	}
	public void setRotated(boolean rotated) {
		this.rotated = rotated;
	}
	public boolean isTrimmed() {
		return trimmed;
	}
	public void setTrimmed(boolean trimmed) {
		this.trimmed = trimmed;
	}
	public Rectangle getSpriteSourceSize() {
		return spriteSourceSize;
	}
	public void setSpriteSourceSize(Rectangle spriteSourceSize) {
		this.spriteSourceSize = spriteSourceSize;
	}
	public SizeF getSourceSize() {
		return sourceSize;
	}
	public void setSourceSize(SizeF sourceSize) {
		this.sourceSize = sourceSize;
	}
	
}
