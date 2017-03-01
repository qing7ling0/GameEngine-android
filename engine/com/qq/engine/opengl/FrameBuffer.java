package com.qq.engine.opengl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

import loader.glloader.GLResourceLoader;

import com.qq.engine.graphics.Graphics;
import com.qq.engine.graphics.image.Image;
import com.qq.engine.graphics.image.ImageCache;
import com.qq.engine.opengl.loader.ColorLoader;
import com.qq.engine.utils.Utils;
import com.qq.engine.view.Screen;

/***
 * 暂时不用
 * @author wuqingqing
 *
 */
public class FrameBuffer {

	private int width;
	private int height;
	private int powWidth;
	private int powHeight;
	private int viewportWidth;
	private int viewportHeight;
	public int framebufferid;
	public int renderbufferid;
	public GL10 gl;
	public GL11ExtensionPack gl11ep;

	private Image img;

	public static boolean isSupports;
	public FrameBufferMartix fbMartix;
	
	public FrameBuffer(GL10 gl, int width, int height, int viewportWidth,
			int viewportHeight) {
		this.width = width;
		this.height = height;
		this.powWidth = Utils.pow2(width);
		this.powHeight = Utils.pow2(height);
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.gl = gl;
		this.fbMartix = new FrameBufferMartix();
		calcScale();

		if (isSupports) {
			build(gl);
		}
	}

	public FrameBuffer(GL10 gl, int width, int height) {
		this(gl, width, height, width, height);
	}

	private void calcScale() {
		float sx = width * 1f / Screen.SCREEN_W;
		float sy = height * 1f / Screen.SCREEN_H;

		
		if (sx < 1 && sy < 1) {
//			screenScale = sx > sy ? sx : sy;
			fbMartix.clipScaleX= fbMartix.clipScaleY = 1;
			fbMartix.drawScale = Screen.SCALE;
			fbMartix.glOrthofWidth = Screen.SCREEN_W ;
			fbMartix.glOrthofHeight = Screen.SCALE_H;
			
		} else {
			float screenScale = sx > sy ? sx : sy;
			fbMartix.drawScale = 1;
			fbMartix.clipScaleX = fbMartix.clipScaleY = screenScale;
			fbMartix.glOrthofWidth = Screen.SCREEN_W * screenScale;
			fbMartix.glOrthofHeight = Screen.SCALE_H * screenScale;
		}
	}

	private void build(GL10 gl) {
		GLResourceLoader loader = new ColorLoader("FrameBuffer:"
				+ this.hashCode(), powWidth, powHeight);
		img = ImageCache.createImage(null, loader);

		gl11ep = (GL11ExtensionPack) gl;
		int[] framebuffers = new int[1];
		gl11ep.glGenFramebuffersOES(1, framebuffers, 0);
		framebufferid = framebuffers[0];
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				framebufferid);

		int[] renderbuffers = new int[1];
		gl11ep.glGenRenderbuffersOES(1, renderbuffers, 0);
		renderbufferid = renderbuffers[0];

		gl11ep.glBindRenderbufferOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
				renderbufferid);
		gl11ep.glRenderbufferStorageOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
				GL11ExtensionPack.GL_STENCIL_INDEX8_OES, powWidth, powHeight);
		gl11ep.glFramebufferRenderbufferOES(
				GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				GL11ExtensionPack.GL_STENCIL_ATTACHMENT_OES,
				GL11ExtensionPack.GL_RENDERBUFFER_OES, renderbufferid);
		gl11ep.glFramebufferTexture2DOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
				GL11ExtensionPack.GL_COLOR_ATTACHMENT0_OES, GL10.GL_TEXTURE_2D,
				img.getTexture().getTextureId(), 0);
		int status = gl11ep
				.glCheckFramebufferStatusOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES);
		if (status != GL11ExtensionPack.GL_FRAMEBUFFER_COMPLETE_OES) {
			isSupports = false;
			this.recycle();

		}
		
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
	}

	public static void check(GL10 gl) {
//		isSupports = checkIfContextSupportsFrameBufferObject(gl);
		isSupports = false;
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(img, x, y, 0, 0, width, height);
	}

	public static boolean checkIfContextSupportsFrameBufferObject(GL10 gl) {
		return checkIfContextSupportsExtension(gl, "GL_OES_framebuffer_object");
	}

	/**
	 * This is not the fastest way to check for an extension, but fine if we are
	 * only checking for a few extensions each time a context is created.
	 * 
	 * @param gl
	 * @param extension
	 * @return true if the extension is present in the current context.
	 */
	public static boolean checkIfContextSupportsExtension(GL10 gl,
			String extension) {
		String extensions = " " + gl.glGetString(GL10.GL_EXTENSIONS) + " ";
		return extensions.indexOf(" " + extension + " ") >= 0;
	}

	public void begin() {
		gl.glViewport(0, 0, Screen.SCREEN_W, Screen.SCREEN_H);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		 gl.glOrthof(0, fbMartix.glOrthofWidth, 0, fbMartix.glOrthofHeight, -1, 1);

//		gl.glOrthof(0, Screen.SCREEN_W, 0, Screen.SCREEN_H, -1, 1);
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, framebufferid);
	}

	public void end() {
		// gl.glViewport(0, 0, Screen.SCREEN_W, Screen.SCREEN_H);
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glLoadIdentity();
		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glOrthof(0, Screen.SCREEN_W, Screen.SCREEN_H, 0, -1, 1);
//		 gl.glTranslatef(0, Screen.SCREEN_H, 0);
		// gl.glScalef(1, -1, 1);
		gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, 0);
	}

	public void recycle() {
		if (img != null) {
			img.recycle();
			img = null;
		}

		if (renderbufferid != 0) {
			int[] renderbuffers = new int[1];
			renderbuffers[0] = renderbufferid;
			gl11ep.glDeleteRenderbuffersOES(1, renderbuffers, 0);
		}

		if (framebufferid != 0) {
			int[] framebuffers = new int[1];
			framebuffers[0] = framebufferid;
			gl11ep.glDeleteFramebuffersOES(1, framebuffers, 0);
		}
	}
}
