package com.qq.engine.scene;

import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.RectangleF;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.image.Image;
import com.qq.engine.utils.Debug;

public class Sprite extends Node {

	protected Image drawImage;
	protected Image image;
	protected RectangleF rect;
	
	/** x轴显示比例  负值为从右开始*/
	protected float perx;
	/** y轴显示比例  负值为从下开始*/
	protected float pery;

	private float[] vertices;
	private float[] texcoords;
	
	/**
	 * 精灵绘制区域是否发生变化
	 * */
	private boolean clipChange;
	
	/**
	 * 是否是绘制变灰
	 */
	private boolean drawGray;
	
	public static Sprite create (Image image)
	{
		return create(image, new RectangleF(0, 0, image.getWidth(), image.getHeight()));
	}
	
	public static Sprite create (SpriteFrame spriteFrame)
	{
		return create(spriteFrame.image, spriteFrame.rect);
	}
	
	public static Sprite create(Image image, RectangleF rect)
	{
		Sprite sp = new Sprite();
		sp.init(image, rect);
		return sp;
	}
	
	protected void init(Image image, RectangleF rect)
	{
		this.init();
		this.setAnchor(Graphics.HCENTER | Graphics.VCENTER);
		this.initWithImage(image, rect);
	}
	
	public void initWithImage(Image image, RectangleF rect)
	{
		this.image = image;
		if (image == null) 
			Debug.e("Sprite initWithImage error Image ==null" + (parent != null?parent:""));
		this.rect = rect;
		this.drawGray = false;
		this.perx = 1;
		this.pery = 1;
		this.clipChange = true;
		this.setContentSize(rect.width, rect.height);
	}
	
	public void initWithImage(Image image)
	{
		initWithImage(image, new RectangleF(0, 0, image.getWidth(), image.getHeight()));
	}

	@Override
	public void draw(Graphics g) {
		if (image == null)
		{
			return;
		}
		if (clipChange)
		{
			clipChange = false;
			if (vertices == null)
			{
				vertices = new float[8];
				texcoords = new float[8];
			}
			updateTexcoords(drawImage);
		}
		drawImage.drawInSpriteBatch(g, vertices, texcoords, g.color);
	}
	
	@Override
	public void visit(Graphics g) {
		// TODO Auto-generated method stub

		if (visible && image != null)
		{
			drawImage = image;
			if (isGray())
			{
				drawImage = image.getGrayImage();
				drawImage.checkLoadDone();
				if (!drawImage.isLoadDone())
				{
					drawImage = image;
					if (drawGray)
					{
						drawGray = false;
					}
				}
				else
				{
					if (!drawGray)
					{
						clipChange = true;
						drawGray = true;
					}
				}
			}
			else
			{
				// 在灰色图片和正常图片切换时，需要重置绘制数据
				if (drawGray)
				{
					clipChange = true;
					drawGray = false;
				}
			}
		}
		super.visit(g);
	}

	@Override
	protected void onMatrixChange() {
		// TODO Auto-generated method stub
		super.onMatrixChange();
		clipChange = true;
	}

	protected void updateTexcoords(Image img)
	{
		final RectangleF drawRect = new RectangleF();
		final PointF imgPacketOff = PointF.zeroPoint();
		float x =0, y = 0, w = 0, h = 0, sx = 0, sy = 0;
		
		rect.width = width;
		rect.height = height;
		if (perx < 0)
		{
			x = rect.x + (1+perx)*rect.width;
			w = -1*perx*rect.width;
			sx = width - w;
		}
		else
		{
			x = rect.x;
			w = perx*rect.width;
		}
		if (pery < 0)
		{
			y = rect.y + (1+pery)*rect.height;
			h = -1*pery*rect.height;
			sy = height - h;
		}
		else
		{
			y = rect.y;
			h = pery*rect.height;
		}

		drawRect.x = x;
		drawRect.y = y;
		drawRect.width = w;
		drawRect.height = h;
		
		imgPacketOff.x = sx;
		imgPacketOff.y = sy;
		
		img.getDrawTexcoords(texcoords, drawRect, imgPacketOff);
		updateVertices(img, 0, 0, drawRect.width, drawRect.height, imgPacketOff.x, imgPacketOff.y);
		
	}
	
	protected void updateVertices(Image img, float sx, float sy, float width, float height, float offx, float offy)
	{
		img.getDrawVertices(vertices, sx, sy, width, height, offx, offy, worldMatrix);
	}

	public void setPerXY(float perx, float pery)
	{
		this.perx = perx;
		this.pery = pery;
		this.clipChange = true;
	}

	public float getPerx() {
		return perx;
	}

	public void setPerx(float perx) {
		this.perx = perx;
		this.clipChange = true;
	}

	public float getPery() {
		return pery;
	}

	public void setPery(float pery) {
		this.pery = pery;
		this.clipChange = true;
	}
	
}
