package com.qq.engine.scene;

import java.io.InputStream;

import loader.glloader.GLResourceLoader;
import loader.glloader.LoaderData;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.image.Image;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.utils.Utils;

public class FPSLayer extends Node {

	private int fps;
	private Image img;
	private byte[] nums;
	
	public static FPSLayer create() {
		// TODO Auto-generated constructor stub
		FPSLayer fps = new FPSLayer();
		fps.init();
		
		return fps;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		nums = new byte[1];
		
		img = ImageCache.createImage(null, new GLResourceLoader() {
			
			@Override
			public InputStream createSource() {
				// TODO Auto-generated method stub
				key = "fps";
				return FPSLayer.class.getResourceAsStream("/assets/fps.png");
			}
			
//
//			@Override
//			public LoaderData createLoaderData() {
//				// TODO Auto-generated method stub
//				Bitmap bit = super.createLoaderData().bm;
//				Bitmap bit2 = ImageCache.createBitmap(256, 32);
//				Canvas cav = new Canvas(bit2);
//				cav.drawBitmap(bit, 0, 0, null);
//				bit.recycle();
//				bit = null;
//				
//				return LoaderData.create(bit2);
//			}
			
		});
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
		int length = Utils.getIntLength(fps);

		if (length > 2)
		{
			this.setContentSize(15*length, 23);
		}
		else
		{
			length = 2;
		}
		nums = new byte[length];

		int numCount = 0;
		while(true)
		{
			int n = fps%10;
			nums[numCount] = (byte)n;
			fps = fps/10;
			numCount++;
			if(fps == 0)
			{
				break;
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		super.draw(g);
		int sx = 0;
		for(int i=nums.length-1; i>-1; i--)
		{
			drawSingle(g, nums[i], sx, 0);
			sx += 15;
		}
	}

	private void drawSingle(Graphics g, int num, int x, int y)
	{
		g.drawImage(img, x, y, 15*num+0, 0, 15, 23);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
	}
	
	
}
