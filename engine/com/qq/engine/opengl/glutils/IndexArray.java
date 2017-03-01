/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.qq.engine.opengl.glutils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class IndexArray {
	private ShortBuffer buffer;
	private ByteBuffer byteBuffer;

	public IndexArray (int maxIndices) {
		byteBuffer = ByteBuffer.allocateDirect(maxIndices*2);
		byteBuffer.order(ByteOrder.nativeOrder());
		buffer = byteBuffer.asShortBuffer();
		buffer.flip();
		byteBuffer.flip();
	}

	public int getNumIndices () {
		return buffer.limit();
	}

	public int getNumMaxIndices () {
		return buffer.capacity();
	}

	public void setIndices (short[] indices, int offset, int count) {
		buffer.clear();
		buffer.put(indices, offset, count);
		buffer.flip();
		byteBuffer.position(0);
	}

	public ShortBuffer getBuffer () {
		return buffer;
	}
}