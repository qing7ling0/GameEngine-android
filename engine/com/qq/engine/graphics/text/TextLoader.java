package com.qq.engine.graphics.text;

import java.io.InputStream;

import loader.glloader.GLResourceLoader;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.qq.engine.drawing.SizeF;
import com.qq.engine.graphics.GFont;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.utils.Debug;
import com.qq.engine.utils.Utils;

public class TextLoader extends GLResourceLoader {

	public TextTextureData texData;
	
	private Paint paint;
	
	private GFont font;
	
	private int width;
	private int height;
	
	public TextLoader()
	{
	}
	
	public TextLoader(TextTextureData texData) {
		// TODO Auto-generated constructor stub
		this();
		this.texData = TextTextureData.create(texData);
		this.key = texData.getKey();
//		if (paint == null)
		{
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setTypeface(Typeface.DEFAULT);
		}
		
		font = GFont.create(texData.size, texData.color);
		font.setBold(texData.isBold);
		
		this.width = Math.max(2, (int) Utils.getStringWidth(texData.text, font));
		this.height = Math.max(2, font.lineHeight())+1;
		switch(texData.textStrokeStyle)
		{
		case TextStrokeStyle.STROKE_RB:
			this.width += 2;
			this.height += 2;
			break;
		case TextStrokeStyle.STROKE_TBLR:
			this.width += 4;
			this.height += 4;
			break;
		}
	}

	@Override
	public InputStream createSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SizeF readTextureSize() {
		return SizeF.create(width, height);
	}

	@Override
	public Bitmap createBitmap() {
		// TODO Auto-generated method stub

		Bitmap bm = ImageCache.createBitmap(width, height);
		Canvas cv = new Canvas(bm);
		
		int color = font.getColor();
		
//		Debug.d("TextLoader  text=" + texData.text,
//				" size=" + texData.size,
//				" color=", color,
//				" bold=", texData.isBold,
//				" stroke=", texData.textStrokeStyle,
//				" strokecolor=", texData.strokeColor );
		
		paint.setFakeBoldText(texData.isBold);
		paint.setTextSize(texData.size);
		float sy = -paint.getFontMetrics().top-1;
		
		switch(texData.textStrokeStyle)
		{
		case TextStrokeStyle.STROKE_RB:
			paint.setColor(0xff000000|texData.strokeColor);
			cv.drawText(texData.text, 1, sy, paint);
			cv.drawText(texData.text, 2, sy, paint);
			cv.drawText(texData.text, 0, sy+1, paint);
			cv.drawText(texData.text, 0, sy+2, paint);
			
			paint.setColor(0xff000000|color);
			cv.drawText(texData.text, 0, sy, paint);
			break;
		case TextStrokeStyle.STROKE_TBLR:
			paint.setColor(0xff000000|texData.strokeColor);
			cv.drawText(texData.text, -1, sy, paint);
			cv.drawText(texData.text, -2, sy, paint);
			cv.drawText(texData.text, 0, sy-1, paint);
			cv.drawText(texData.text, 0, sy-2, paint);
			cv.drawText(texData.text, 1, sy, paint);
			cv.drawText(texData.text, 2, sy, paint);
			cv.drawText(texData.text, 0, sy+1, paint);
			cv.drawText(texData.text, 0, sy+2, paint);
			
			paint.setColor(0xff000000|color);
			cv.drawText(texData.text, 0, sy, paint);
			break;
		case TextStrokeStyle.STROKE_NULL:
			paint.setColor(0xff000000|color);
			cv.drawText(texData.text, 0, sy, paint);
			break;
		}
		
		return bm;
	}
	
}
