package com.qq.engine.opengl.glutils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.qq.engine.graphics.Graphics;

public class VertexArray {
	final FloatBuffer buffer;
	final ByteBuffer byteBuffer;
	boolean isBound = false;
	public VertexType vertexType;
	
	/** 数据最大值 */
	private int maxNumVer;

	public VertexArray (int maxNumVer, VertexType vertexType) {
		this.vertexType = vertexType;
		this.maxNumVer = maxNumVer;
		
		byteBuffer = ByteBuffer.allocateDirect(this.vertexType.vertexSize*maxNumVer);
		byteBuffer.order(ByteOrder.nativeOrder());
		buffer = byteBuffer.asFloatBuffer();
		buffer.flip();
		byteBuffer.flip();
	}

	public void dispose () {
	}

	public FloatBuffer getBuffer () {
		return buffer;
	}

	public int getNumVertices () {
		return buffer.limit() * 4;
	}

	public int getNumMaxVertices () {
		return byteBuffer.capacity();
	}

	public void setVertices (float[] vertices, int offset, int count) {
		buffer.clear();
		buffer.put(vertices);
		buffer.position(0);
	}

	public void bind () {
		GL10 gl = Graphics.gl10;
//		byteBuffer.limit(buffer.limit() * 4);
		switch (vertexType.vertexType) {
		case VertexType.VERTEX_VERTICES:
			buffer.position(0);
			gl.glVertexPointer(vertexType.numComponents, GL10.GL_FLOAT, 0, buffer);
			break;

		case VertexType.VERTEX_COORDINATES:
			buffer.position(0);
			gl.glTexCoordPointer(vertexType.numComponents, GL10.GL_FLOAT, 0, buffer);
			break;
			
		case VertexType.VERTEX_COLOR:
			buffer.position(0);
			gl.glColorPointer(vertexType.numComponents, GL10.GL_FLOAT, 0, buffer);
			break;

		default:
			// throw new GdxRuntimeException("unkown vertex attribute type: " + attribute.usage);
		}
		isBound = true;
	}

	public void unbind () {
		buffer.position(0);
		isBound = false;
	}
	
}
