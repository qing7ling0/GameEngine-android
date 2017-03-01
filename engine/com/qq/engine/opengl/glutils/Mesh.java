package com.qq.engine.opengl.glutils;

import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.qq.engine.graphics.Graphics;
import com.qq.engine.utils.Debug;

public class Mesh {

	protected final VertexArray[] vertexArrays;
	protected final IndexArray indices;
	protected boolean autoBind = true;
	protected final boolean isVertexArray;

	public Mesh(int maxVertices, int maxIndices, VertexType... vertexTypes) {
		this.vertexArrays = new VertexArray[vertexTypes.length];
		for (int i = 0; i < this.vertexArrays.length; i++) {
			vertexArrays[i] = new VertexArray(maxVertices, vertexTypes[i]);
		}
		indices = new IndexArray(maxIndices);
		isVertexArray = true;
	}

	public void setVertices(float[] vertices, int verIndex) {
		setVertices(vertices, 0, vertices.length, verIndex);
	}

	public void setVertices(float[] vertices, int offset, int count,
			int verIndex) {
		if (verIndex > this.vertexArrays.length) {
			Debug.e("Mash setVertices error index > vertexArrays.length index="
					+ verIndex);
			return;
		}
		this.vertexArrays[verIndex].setVertices(vertices, offset, count);
	}

	public void getVertices(float[] vertices, int verIndex) {
		if (verIndex > this.vertexArrays.length) {
			Debug.e("Mash getVertices error index > vertexArrays.length index="
					+ verIndex);
			return;
		}
		VertexArray va = vertexArrays[verIndex];
		if (vertices.length < va.getNumVertices() / 4) {
			Debug.e("Mash getVertices error not enough room in vertices array has "
					+ vertices.length
					+ " floats, needs "
					+ va.getNumVertices()
					/ 4);
			return;
		}
		int pos = va.getBuffer().position();
		va.getBuffer().position(0);
		va.getBuffer().get(vertices, 0, va.getNumVertices() / 4);
		va.getBuffer().position(pos);
	}

	public void setIndices(short[] indices) {
		this.indices.setIndices(indices, 0, indices.length);
	}

	public void setIndices(short[] indices, int offset, int count) {
		this.indices.setIndices(indices, offset, count);
	}

	public void getIndices(short[] indices) {
		if (indices.length < getNumIndices())
			throw new IllegalArgumentException(
					"not enough room in indices array, has " + indices.length
							+ " floats, needs " + getNumIndices());
		int pos = this.indices.getBuffer().position();
		this.indices.getBuffer().position(0);
		this.indices.getBuffer().get(indices, 0, getNumIndices());
		this.indices.getBuffer().position(pos);
	}

	public int getNumIndices() {
		return indices.getNumIndices();
	}

	public int getNumVertices(int verIndex) {
		if (verIndex > this.vertexArrays.length) {
			Debug.e("Mash getNumVertices error index > vertexArrays.length index="
					+ verIndex);
			return 0;
		}
		return vertexArrays[verIndex].getNumVertices();
	}

	public int getMaxVertices(int verIndex) {
		if (verIndex > this.vertexArrays.length) {
			Debug.e("Mash getMaxVertices error index > vertexArrays.length index="
					+ verIndex);
			return 0;
		}
		return vertexArrays[verIndex].getNumMaxVertices();
	}

	public int getMaxIndices() {
		return indices.getNumMaxIndices();
	}

	public int getVertexSize(int verIndex) {
		if (verIndex > this.vertexArrays.length) {
			Debug.e("Mash getVertexSize error index > vertexArrays.length index="
					+ verIndex);
			return 0;
		}
		return vertexArrays[verIndex].vertexType.vertexSize;
	}

	public void setAutoBind(boolean autoBind) {
		this.autoBind = autoBind;
	}

	public void bind() {
		for (int i = 0; i < vertexArrays.length; i++) {
			vertexArrays[i].bind();
		}
	}

	public void unbind() {
		for (int i = 0; i < vertexArrays.length; i++) {
			vertexArrays[i].unbind();
		}
	}

	public void render(int primitiveType) {
		render(primitiveType, 0, getNumIndices());
	}

	public void render(int primitiveType, int offset, int count) {
		if (count == 0)
			return;

		long time = System.currentTimeMillis();
		GL10 gl = Graphics.gl10;

		if (autoBind)
			bind();

		if (indices.getNumIndices() > 0) {
			ShortBuffer buffer = indices.getBuffer();
			int oldPosition = buffer.position();
			int oldLimit = buffer.limit();
			buffer.position(offset);
			buffer.limit(offset + count);
			gl.glDrawElements(primitiveType, count, GL10.GL_UNSIGNED_SHORT, buffer);
			buffer.position(oldPosition);
			buffer.limit(oldLimit);
			buffer = null;
		} else {
			gl.glDrawArrays(primitiveType, offset, count);
		}

		if (autoBind)
			unbind();
		// Debug.e("Mash render time=========",
		// System.currentTimeMillis()-time);
	}

	public void scale(float scaleX, float scaleY, float scaleZ) {

		int index = 0;
		VertexArray va = null;
		for (int i = 0; i < vertexArrays.length; i++) {
			if (vertexArrays[i].vertexType.vertexType == VertexType.VERTEX_VERTICES) {
				va = vertexArrays[i];
				index = i;
				break;
			}
		}
		if (va != null) {
			int numComponents = va.vertexType.numComponents;
			int numVertices = va.getNumVertices();
			int vertexSize = va.vertexType.vertexSize / 4;

			float[] vertices = new float[numVertices * vertexSize];
			getVertices(vertices, index);

			int idx = 0;
			switch (numComponents) {
			case 1:
				for (int i = 0; i < numVertices; i++) {
					vertices[idx] *= scaleX;
					idx += vertexSize;
				}
				break;
			case 2:
				for (int i = 0; i < numVertices; i++) {
					vertices[idx] *= scaleX;
					vertices[idx + 1] *= scaleY;
					idx += vertexSize;
				}
				break;
			case 3:
				for (int i = 0; i < numVertices; i++) {
					vertices[idx] *= scaleX;
					vertices[idx + 1] *= scaleY;
					vertices[idx + 2] *= scaleZ;
					idx += vertexSize;
				}
				break;
			}

			setVertices(vertices, index);
		}
	}

	public ShortBuffer getIndicesBuffer() {
		return indices.getBuffer();
	}

	public void dispose() {

	}
}
