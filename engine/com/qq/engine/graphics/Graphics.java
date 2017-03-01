package com.qq.engine.graphics;


import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import com.qq.engine.drawing.Color;
import com.qq.engine.drawing.PointF;
import com.qq.engine.drawing.RectangleF;
import com.qq.engine.graphics.image.Image;
import com.qq.engine.graphics.text.TextTexture;
import com.qq.engine.graphics.text.TextTextureCache;
import com.qq.engine.opengl.SpriteBatch;
import com.qq.engine.opengl.glutils.IndexArray;
import com.qq.engine.opengl.glutils.Matrix3;
import com.qq.engine.opengl.glutils.VertexArray;
import com.qq.engine.opengl.glutils.VertexType;
import com.qq.engine.view.Screen;

public class Graphics
{
	public static final int TOP             = 1<<1;
    public static final int BOTTOM          = 1<<2;
    public static final int LEFT            = 1<<3;
    public static final int RIGHT           = 1<<4;
    public static final int VCENTER         = 1<<5;
    public static final int HCENTER         = 1<<6;
    
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    

    public static final byte BLEND_NORMAL = 1;
    public static final byte BLEND_ALPHA = 2;
    public static final byte BLEND_MASK = 3;
    public static final byte BLEND_CLIP = 4;
    public static final byte BLEND_VEIN = 5;

	public static GL10 gl10;
	private GFont font;

	private Stack<GFont> fontStack = new Stack<GFont>();
	private Stack<RectangleF> clipRectStack = new Stack<RectangleF>();
	private Stack<Matrix3> pushMat = new Stack<Matrix3>();
	private RectangleF clipRect;
	private int curAlpha = 0xff;
	private short[] alphaStacks = new short[1000];
	private int currentAlphaStackIndex;
	
	
	public Matrix3 matrix;
	private static PointF point;
	public static SpriteBatch batch;
	
	private static int blend;
	
	/** 颜色 RGBA */
	private static int[] colorMask = {0xff, 0xff, 0xff, 0xff};
	
	public Color color  = new Color(1, 1, 1, 1);
	
	private IndexArray indexArray;
	private VertexArray rectVertexArray;
	
	public Graphics()
	{
		font = GFont.getDefaultFont();
		font.setSize(30);
		
		indexArray = new IndexArray(6);
		indexArray.setIndices(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);
		rectVertexArray = new VertexArray(4, new VertexType(2, VertexType.VERTEX_VERTICES));
		
		matrix = new Matrix3();
		point = PointF.zeroPoint();
		
		batch = new SpriteBatch();
	}
    
	public void begin()
	{
//		translate(Screen.OFF_X, Screen.OFF_Y);
//		scale(Screen.SCALE, Screen.SCALE);
//		pushClip();
//		clipRect(0, 0, Screen.GAME_W, Screen.GAME_H);
	}
	
	public void end()
	{
//		popClip();
		matrix.idt();
	}

	public void resumeMatrix(Matrix3 mat)
	{
		this.matrix.set(mat);
	}
    
    public void drawLine( float x1, float y1, float x2, float y2, int color )
    {
    }
    
	// -----------------------------------------------------------
	// 区域剪切
    // #BEGIN
	// -----------------------------------------------------------
    
    public void pushClip()
    {
    	renderBatch();
    	if (clipRect != null)
    	{
    		clipRectStack.push(new RectangleF(clipRect));
    	}
    	else
    	{
    		glEnable(GL10.GL_SCISSOR_TEST);
    		clipRectStack.push(clipRect);
    	}
    }
    
    public void popClip()
    {
    	renderBatch();
    	clipRect = clipRectStack.pop();
    	if (clipRect != null)
    	{
    		setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
    	}
    	else
    	{
        	glDisable(GL10.GL_SCISSOR_TEST);
    	}
    }
    
    private void setClip(float x, float y, float w, float h)
    {
    	int cx = (int) x;
    	int cy = (int) y;
    	int cw = (int)w;
    	int ch = (int)h;

    	if (clipRect == null)
    	{
    		clipRect = new RectangleF(cx, cy, cw, ch);
    	}
    	else
    	{
    		RectangleF rect = RectangleF.intersection(clipRect.x, clipRect.y, clipRect.width, clipRect.height, cx, cy, cw, ch);
    		
    		clipRect.x = rect.x;
    		clipRect.y = rect.y;
    		clipRect.width = rect.width;
    		clipRect.height = rect.height;

    		cx = (int) clipRect.x;
    		cy = (int) clipRect.y;
    		cw = (int) clipRect.width;
    		ch = (int) clipRect.height;
    	}
    	
    	glScissor(cx, cy, cw, ch);
    }
    
    /**
     * 直接对画面进行裁剪
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void clipRect(float x, float y, float w, float h)
    {
    	int cx = (int) x;
    	int cy = Screen.GAME_H - (int)y - (int)h;
    	int cw = (int)w;
    	int ch = (int)h;
    	
    	cx = Screen.gameConvertToScreenX(cx);
    	cy = Screen.gameConvertToScreenY(cy);
    	cw = Screen.gameConvertToScreenWH(cw);
    	ch = Screen.gameConvertToScreenWH(ch);

    	setClip(cx, cy, cw, ch);
    }
    
    public void clipRect(RectangleF rect)
    {
        this.clipRect(rect.x, rect.y, rect.width, rect.height);
    }
    
	// -----------------------------------------------------------
	// 区域剪切
    // #END
	// -----------------------------------------------------------
    
	// -----------------------------------------------------------
	// Image
    // #BEGIN
	// -----------------------------------------------------------
	
	public void drawImage(Image img, float x, float y, int anchor)
	{
		drawImage(img, x, y, 0, 0, img.getWidth(), img.getHeight(), anchor);
	}

	public void drawImage(Image img, float x, float y)
	{
		drawImage(img, x, y, 0, 0, img.getWidth(), img.getHeight());
	}
	
	public void drawImage(Image img, float x, float y, float clipx, float clipy, float clipw, float cliph)
	{
		drawImage(img, x, y, clipx, clipy, clipw, cliph, Graphics.TOP | Graphics.LEFT);
	}
	
	public void drawImage(Image img, float x, float y, float clipx, float clipy, float clipw, float cliph, int anchor)
	{
		drawTransImage(img, x, y, clipx, clipy, clipw, cliph, anchor, TRANS_NONE);
	}
	
	public void drawTransImage(Image img, float x, float y, float clipx, float clipy, float clipw, float cliph, int anchor, int trans)
	{
		img.draw(this, x, y, clipx, clipy, clipw, cliph, anchor, trans);
	}
	
	public void transform(float width, float height, int trans, int anchor)
	{
		float anchorx = 0, anchory = 0;
		
		if ((anchor & HCENTER) != 0)
		{
			anchorx = 0.5f;
		}
		else if ((anchor & RIGHT) != 0)
		{
			anchorx = 1;
		}
		if ((anchor & VCENTER) != 0)
		{
			anchory = 0.5f;
		}
		else if ((anchor & BOTTOM) != 0)
		{
			anchory = 1;
		}
		
		matrix.transform(trans, width, height, anchorx, anchory);
	}
	
	public void drawFillImage(Image img, float rectX, float rectY, float rectW, float rectH, float clipX, float clipY, float clipW, float clipH)
	{
		if(rectW <= 0)	rectW = clipW;
		if(rectH <= 0)	rectH = clipH;
		
		for(int i = 0; i < rectW; i += clipW)
		{
			float cw = i+clipW > rectW ? rectW-i : clipW;
			for(int j = 0; j < rectH; j += clipH)
			{
				float ch = j+clipH > rectH ? rectH-j : clipH;
				drawImage(img, i+rectX, j+rectY, clipX, clipY, cw, ch);
			}
		}
	}

	// -----------------------------------------------------------
	// Image
    // #END
	// -----------------------------------------------------------

    public void fillArc( float x, float y, float width, float height, int startAngle, int arcAngle )
    {
    }
	
	public void drawAlphaRect(float x, float y, float w, float h, int alphaColor)
	{
	}
	
	public void drawRect(float x, float y, float w, float h, int color)
	{
	}

	
	public void drawRect(Graphics g, RectangleF rect, int color)
	{
		drawRect(rect.x, rect.y, rect.width, rect.height, color);
	}

	private float[] rectVertices = new float[8];
	public void fillRect(float x, float y, float w, float h, int r, int g, int b, int a, Matrix3 mat)
	{
		glDisable(GL10.GL_TEXTURE_2D);
		gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
		maskColor(r, g, b, a);
		
		getVertices(rectVertices, x, y, w, h, mat);
		rectVertexArray.setVertices(rectVertices, 0, 8);
		rectVertexArray.bind();
		indexArray.getBuffer().position(0);
		
		glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexArray.getBuffer());
		
		rectVertexArray.unbind();
		glEnable(GL10.GL_TEXTURE_2D);
		gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		resetMaskColor();
	}

	public void fillRect(float x, float y, float w, float h, int alpha, int color, Matrix3 mat)
	{
		fillRect(x, y, w, h, 0xFF & (color>>16), 0xFF & (color>>8), 0xFF & color, alpha, mat);
	}

	public void fillRect(float x, float y, float w, float h, int alpha, int color)
	{
		fillRect(x, y, w, h, 0xFF0000 & color, 0xFF00 & color, 0xFF & color, alpha, matrix);
	}
    
    public void fillRect( float x, float y, float width, float height, int color)
    {
    	fillRect(x, y, width, height, 0xff, color);
    }

	public void fillRect(RectangleF rect, int color)
	{
		fillRect(rect.x, rect.y, rect.width, rect.height, color);
	}
    
	// -----------------------------------------------------------
	// Matrix
    // #BEGIN
	// -----------------------------------------------------------
	public void push()
	{
		pushMat.push(new Matrix3(matrix));
	}
	
	public void pop()
	{
		matrix.set(pushMat.pop());
	}
	
	public void translate(float x, float y)
	{
		matrix.translate(x, y);
	}
	
	public void scale(float x, float y)
	{
		matrix.scale(x, y);
	}
	
	public void scale(float sx, float sy, float px, float py)
	{
		translate(px, py);
		scale(sx, sy);
		translate(-px, -py);
	}
	
	public void rotate(float degrees)
	{
		matrix.rotate(degrees);
	}
	
	public void rotate(float degrees, float x, float y)
	{
		translate(x, y);
		rotate(degrees);
		translate(-x, -y);
	}
	
	public void mulMatrix(Matrix3 mat)
	{
		this.matrix.mul(mat);
	}
	
	public void setMatrix(Matrix3 mat)
	{
		this.matrix.set(mat);
	}
	
	public void maskColor(int r, int g, int b, int a)
	{
		gl10.glColor4f(r*1.0f/colorMask[0], g*1.0f/colorMask[1], b*1.0f/colorMask[2], a*curAlpha*1.0f/(colorMask[3]*colorMask[3]));
	}
	
	public void resetMaskColor()
	{
		gl10.glColor4f(1, 1, 1, 1);
	}
	
	
	// -----------------------------------------------------------
	// Matrix
    // #END
	// -----------------------------------------------------------
	
	//------------------------------------------------------------
	// openggl api
    // #BEGIN
	// -----------------------------------------------------------

	public void glStencilFunc(int func, int ref, int mask)
	{
		gl10.glStencilFunc(func, ref, mask);
	}
	
	public void glStencilOp(int fail, int zfail, int zpass)
	{
		gl10.glStencilOp(fail, zfail, zpass);
	}
	
	public void glScissor( int x, int y, int width, int height )
	{
		gl10.glScissor(x, y, width, height);
	}

	public void glEnable(int cap)
	{
		gl10.glEnable(cap);
	}
	
	public void glClearStencil(int s)
	{
		gl10.glClearStencil(s);
	}
	
	public void glClear(int mask)
	{
		gl10.glClear(mask);
	}
	
	public void glAlphaFunc(int func, float ref)
	{
		gl10.glAlphaFunc(func, ref);
	}
	
	public void glDisable(int cap)
	{
		gl10.glDisable(cap);
	}

	public void glDrawElements(int mode, int count, int type, java.nio.Buffer indices)
	{
		gl10.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexArray.getBuffer());
	}
	//------------------------------------------------------------
	// opengl 
    // #END
	// -----------------------------------------------------------
	
	
	// -----------------------------------------------------------
	// String
    // #BEGIN
	// -----------------------------------------------------------

	public void setFont(GFont font)
	{
		this.font = font;
	}
	
	public void pushFont()
	{
		fontStack.add(font);
	}
	
	public void popFont()
	{
		setFont(fontStack.pop());
	}
	
	public void setTextBold(boolean bold)
	{
		this.font.setBold(bold);
	}
	
	public void setColor( int color )
    {
        this.font.setColor( color );
    }
	
	public void setTextSize(int size)
	{
		this.font.setSize(size);
	}
	
	public void setTextSizeMedium()
	{
		this.font.setSize(GFont.SIZE_MEDIUM);
	}
	
	public void setTextSizeSmall()
	{
		this.font.setSize(GFont.SIZE_SMALL);
	}
	
	public void setTextSizeLarge()
	{
		this.font.setSize(GFont.SIZE_LARGE);
	}
	
	public void setTextSizeLargest()
	{
		this.font.setSize(GFont.SIZE_LARGEST);
	}
    
    public void setAlphaColor(int color)
    {
    	this.setColor(color);
    	this.setAlpha(0xFF000000 & color);
    }
    public void setAlphaColor(int alpha, int color)
    {
    	this.setColor(color);
    	this.setAlpha(alpha);
    }
    
    public void pushAlpha()
    {
    	alphaStacks[currentAlphaStackIndex] = (short) curAlpha;
    	currentAlphaStackIndex++;
//    	alphaStack.push(curAlpha);
    }
    
    public void popAlpha()
    {
    	currentAlphaStackIndex--;
    	curAlpha = alphaStacks[currentAlphaStackIndex];
//    	curAlpha = alphaStack.pop();
    	
    }
    
    public void setAlpha(int alpha)
    {
    	curAlpha = (int) ((curAlpha*alpha) / 255f);
    }
	
	public void drawString(String string, float x, float y)
	{
		drawString(string, x, y, Graphics.LEFT|Graphics.TOP);
	}

	public void drawString(String string, float x, float y, int anchor)
	{
		drawString(string, x, y, anchor, false, false);
	}
	
	/**
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param anchor
	 * @param gray
	 * @param isAsyn 是否异步加载
	 */
	public void drawString(String string, float x, float y, int anchor, boolean gray, boolean isAsyn)
	{
		
		drawTextTexture(TextTextureCache.getInstance().addText(font, string, isAsyn), x, y, anchor, gray);
	}
	
	public void drawString(String string, float x, float y, int color, int anchor)
	{
		this.setColor(color);
		drawString(string, x, y, anchor);
	}
	
	public void drawString(String string, float x, float y, int color, int anchor, boolean gray, boolean isAsyn)
	{
		this.setColor(color);
		drawString(string, x, y, anchor, gray, isAsyn);
	}
	
	public void drawStrokeString(String string, float x, float y, int incolor, int outcolor, byte textStrokeStyle, int anchor)
	{
		drawStrokeString(string, x, y, incolor, outcolor, textStrokeStyle, anchor, false);
	}

	/**
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param incolor
	 * @param outcolor
	 * @param textStrokeStyle
	 * @param anchor
	 * @param gray
	 * @param isAnsy 是否异步加载
	 */
	public void drawStrokeString(String string, float x, float y, int incolor, int outcolor, byte textStrokeStyle, int anchor, boolean gray, boolean isAnsy)
	{
		this.setColor(incolor);
		drawTextTexture(TextTextureCache.getInstance().addText( font, string, outcolor, textStrokeStyle, isAnsy), x, y, anchor, gray);
	}
	
	public void drawStrokeString(String string, float x, float y, int incolor, int outcolor, byte textStrokeStyle, int anchor, boolean gray)
	{
		drawStrokeString(string, x, y, incolor, outcolor, textStrokeStyle, anchor, gray, false);
	}
	
	public void drawTextTexture(TextTexture text, float x, float y, int anchor, boolean gray)
	{
		Image drawImage = text.img;
		if (gray)
		{
			drawImage = text.img.getGrayImage();
			drawImage.checkLoadDone();
			if (!drawImage.isLoadDone())
			{
				drawImage = text.img;
			}
		}
		// alpha
		drawImage(drawImage, x, y, anchor);
	}

	// -----------------------------------------------------------
	// String
    // #END
	// -----------------------------------------------------------
	
	public static void glAlphaFunc(int blend)
	{
		if (Graphics.blend != blend)
		{
			switch (blend) {
			case BLEND_NORMAL:
				batch.glBlendFunc(
					GL10.GL_ONE, 
					GL10.GL_ONE_MINUS_SRC_ALPHA);
				break;
			case BLEND_ALPHA:
				batch.glBlendFunc(
					GL10.GL_SRC_ALPHA, 
					GL10.GL_ONE_MINUS_SRC_ALPHA);
				break;
			case BLEND_MASK:
				batch.glBlendFunc(
					GL10.GL_ZERO, 
					GL10.GL_SRC_ALPHA);
				break;
			case BLEND_CLIP:
				batch.glBlendFunc(
						GL10.GL_ZERO, 
					GL10.GL_ONE_MINUS_SRC_ALPHA);
				break;
			case BLEND_VEIN:
				batch.glBlendFunc(
					GL10.GL_DST_COLOR, 
					GL10.GL_ONE_MINUS_SRC_ALPHA);
				break;
			default:
				batch.glBlendFunc(
						GL10.GL_ONE, 
					GL10.GL_ONE_MINUS_SRC_ALPHA);
				break;
			}
			Graphics.blend = blend;
		}
		
		
	}
	
	public static void batchBegin()
	{
		if (batch != null) batch.begin(gl10);
	}
	
	public static void batchEnd()
	{
		if (batch != null) batch.end(gl10);
	}
	
	public static void renderBatch()
	{
		if (batch != null) batch.render();
	}

	public static void closeBatch()
	{
		if (batch != null) batch.setOpen(false);
	}
	
	public static void openBatch()
	{
		if (batch != null) batch.setOpen(true);
	}
	
	public static void getVertices(float[] vertices, float cx, float cy, float cw, float ch, Matrix3 matrix) {
		
		final float x, y, x1, y1, x2, y2, x3, y3;
		PointF point = Graphics.point;
		
		point.x = cx;
		point.y = cy;
		matrix.matrixPoint(point);
		
		x = point.x;
		y = point.y;
		
		point.x = cx;
		point.y = cy + ch;
		matrix.matrixPoint(point);
		
		x1 = point.x;
		y1 = point.y;

		point.x = cx + cw;
		point.y = cy + ch;
		matrix.matrixPoint(point);
		
		x2 = point.x;
		y2 = point.y;

		point.x = cx + cw;
		point.y = cy;
		matrix.matrixPoint(point);
		
		x3 = point.x;
		y3 = point.y;
		
		vertices[0] = x;
		vertices[1] = y;
		
		vertices[2] = x1;
		vertices[3] = y1;
		
		vertices[4] = x2;
		vertices[5] = y2;
		
		vertices[6] = x3;
		vertices[7] = y3;
	}

	public static void getTexcoords(float[] texcoords, float cx, float cy, float cw, float ch,
			float tw, float th) {

		final float x = cx / tw;
		final float y = cy / th;
		final float x2 = (cx + cw) / tw;
		final float y2 = (cy + ch) / th;
		
		texcoords[0] = x;
		texcoords[1] = y;
		
		texcoords[2] = x;
		texcoords[3] = y2;
		
		texcoords[4] = x2;
		texcoords[5] = y2;
		
		texcoords[6] = x2;
		texcoords[7] = y;
	}
}
