package com.qq.engine.opengl;

import javax.microedition.khronos.opengles.GL10;

import com.qq.engine.drawing.Color;
import com.qq.engine.graphics.Graphics;
import com.qq.engine.opengl.glutils.Mesh;
import com.qq.engine.opengl.glutils.VertexType;


public class SpriteBatch {

	private Mesh mesh;
	private int size = 500;
	
	private Texture2D lastTexture = null;

	private int idx = 0;
	private int idx2 = 0;
	private int idx3 = 0;
	private final float[] vertices;
	private final float[] texCoords;
	private final float[] colorPoints;

	private boolean drawing = false; 

	private boolean blendingDisabled = false;
	private int blendSrcFunc = GL10.GL_SRC_ALPHA;
	private int blendDstFunc = GL10.GL_ONE_MINUS_SRC_ALPHA;

	public int renderCalls = 0;

	public int totalRenderCalls = 0;

	public int maxSpritesInBatch = 0;
	
	private boolean open;
	
	public SpriteBatch()
	{
		mesh = new Mesh(size*4, size*6, new VertexType(2, VertexType.VERTEX_VERTICES), new VertexType(2, VertexType.VERTEX_COORDINATES), new VertexType(4, VertexType.VERTEX_COLOR));
		
		int len = size * 6;
		short[] indices = new short[len];
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		this.mesh.setIndices(indices);
		
		vertices = new float[size * 8];
		texCoords = new float[size * 8];
		colorPoints = new float[size * 16];
		
		open = true;
	}

	public void begin (GL10 gl) {
		renderCalls = 0;

//		gl.glEnable(GL10.GL_TEXTURE_2D);
		idx = 0;
		idx2 = 0;
		idx3 = 0;
		lastTexture = null;
		drawing = true;
	}

	/** Finishes off rendering. Enables depth writes, disables blending and texturing. Must always be called after a call to
	 * {@link #begin()} */
	public void end (GL10 gl) {
		if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before end.");
		if (idx > 0) renderMesh();
		lastTexture = null;
		idx = 0;
		idx2 = 0;
		idx3 = 0;
		drawing = false;

		if (isBlendingEnabled()) gl.glDisable(GL10.GL_BLEND);

//		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void render()
	{
		renderMesh();
	}


	/** Draws a rectangle with the bottom left corner at x,y having the width and height of the texture.
	 * @param texture the Texture
	 * @param x the x-coordinate in screen space
	 * @param y the y-coordinate in screen space */
	public void draw (Texture2D texture, float x, float y) {
		
		draw(texture, x, y, 0, 0, texture.width(), texture.height());
	}

	/** Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height. */
	public void draw (Texture2D texture, float x, float y, float clipx, float clipy, float width, float height) {
		if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
		
		float[] texVers = Texture2D.getVertices(x, y, width, height);

		float[] coords = Texture2D.getTexcoords(clipx, clipy, width, height, texture.pow2Width(), texture.pow2Height());
		
		draw(texture, texVers, coords, new Color(1, 1, 1, 1));
	}

	public void draw(Texture2D texture, float[] vers, float[] coords, Color color)
	{
		if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		if (texture != lastTexture) {
			switchTexture(texture);
		} else if (idx == vertices.length) //
			renderMesh();

		final float[] ver = vers;
		final float[] coord = coords;
		
		final float[] ver2 = vertices;
		final float[] coord2 = texCoords;
		final float[] colPs = colorPoints;
		final Color cor = color;
		
		for(int i=0; i<ver.length; i++)
		{
			ver2[idx++] = ver[i];
		}

		for(int i=0; i<coord.length; i++)
		{
			coord2[idx2++] = coord[i];
		}
		
		for(int i=0; i<4; i++)
		{
			colPs[idx3++] = cor.r;
			colPs[idx3++] = cor.g;
			colPs[idx3++] = cor.b;
			colPs[idx3++] = cor.a;
		}
		
	}
	
	private void renderMesh () {
		if (idx == 0) return;
		GL10 gl = Graphics.gl10;
		renderCalls++;
		totalRenderCalls++;
		final int spritesInBatch = idx / 8;
		if (spritesInBatch > maxSpritesInBatch) maxSpritesInBatch = spritesInBatch;

		boolean bindok = lastTexture.bind();
		if (bindok)
		{
			mesh.setVertices(vertices, 0, idx, 0);
			mesh.setVertices(texCoords, 0, idx2, 1);
			mesh.setVertices(colorPoints, 0, idx3, 2);
			mesh.getIndicesBuffer().position(0);
			mesh.getIndicesBuffer().limit(spritesInBatch * 6);
	
			if (blendingDisabled) {
				gl.glDisable(GL10.GL_BLEND);
			} else {
				gl.glEnable(GL10.GL_BLEND);
				gl.glBlendFunc(blendSrcFunc, blendDstFunc);
			}
	
			mesh.render(GL10.GL_TRIANGLES, 0, spritesInBatch * 6);
		}

		idx = 0;
		idx2 = 0;
		idx3 = 0;
	}

	/** Disables blending for drawing sprites. */
	public void disableBlending () {
		renderMesh();
		blendingDisabled = true;
	}

	/** Enables blending for sprites */
	public void enableBlending () {
		renderMesh();
		blendingDisabled = false;
	}

	/** Sets the blending function to be used when rendering sprites.
	 * 
	 * @param srcFunc the source function, e.g. GL11.GL_SRC_ALPHA
	 * @param dstFunc the destination function, e.g. GL11.GL_ONE_MINUS_SRC_ALPHA */
	public void glBlendFunc (int srcFunc, int dstFunc) {
		renderMesh();
		blendSrcFunc = srcFunc;
		blendDstFunc = dstFunc;
	}

	/** Disposes all resources associated with this SpriteBatch */
	public void dispose () {
		mesh.dispose();
	}

	private void switchTexture (Texture2D texture) {
		renderMesh();
		lastTexture = texture;
	}


	/** @return whether blending for sprites is enabled */
	public boolean isBlendingEnabled () {
		return !blendingDisabled;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
