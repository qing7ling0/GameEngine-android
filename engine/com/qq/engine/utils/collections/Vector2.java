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

package com.qq.engine.utils.collections;

import java.io.Serializable;

import com.qq.engine.opengl.glutils.Matrix3;
import com.qq.engine.utils.MathUtils;
import com.qq.engine.utils.NumberUtils;

public class Vector2 implements Serializable {
	private static final long serialVersionUID = 913902788239530931L;

	public final static Vector2 tmp = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();

	public final static Vector2 X = new Vector2(1, 0);
	public final static Vector2 Y = new Vector2(0, 1);
	public final static Vector2 Zero = new Vector2(0, 0);

	public float x;
	public float y;

	public Vector2 () {
	}

	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 (Vector2 v) {
		set(v);
	}

	public Vector2 cpy () {
		return new Vector2(this);
	}

	public float len () {
		return (float)Math.sqrt(x * x + y * y);
	}

	public float len2 () {
		return x * x + y * y;
	}

	public Vector2 set (Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	public Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2 sub (Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public Vector2 nor () {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}

	public Vector2 add (Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public Vector2 add (float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public float dot (Vector2 v) {
		return x * v.x + y * v.y;
	}

	public Vector2 mul (float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector2 mul (float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public float dst (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	public float dst (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	public float dst2 (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}

	public float dst2 (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return x_d * x_d + y_d * y_d;
	}

	public String toString () {
		return "[" + x + ":" + y + "]";
	}

	public Vector2 sub (float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector2 tmp () {
		return tmp.set(this);
	}

	public Vector2 mul (Matrix3 mat) {
		float x = this.x * mat.val[0] + this.y * mat.val[3] + mat.val[6];
		float y = this.x * mat.val[1] + this.y * mat.val[4] + mat.val[7];
		this.x = x;
		this.y = y;
		return this;
	}

	public float crs (Vector2 v) {
		return this.x * v.y - this.y * v.x;
	}

	public float crs (float x, float y) {
		return this.x * y - this.y * x;
	}

	public float angle () {
		float angle = (float)Math.atan2(y, x) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		return angle;
	}

	public Vector2 rotate (float degrees) {
		float rad = degrees * MathUtils.degreesToRadians;
		float cos = (float)Math.cos(rad);
		float sin = (float)Math.sin(rad);

		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;

		return this;
	}

	public Vector2 lerp (Vector2 target, float alpha) {
		Vector2 r = this.mul(1.0f - alpha);
		r.add(target.tmp().mul(alpha));
		return r;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + NumberUtils.floatToIntBits(x);
		result = prime * result + NumberUtils.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2 other = (Vector2) obj;
		if (NumberUtils.floatToIntBits(x) != NumberUtils.floatToIntBits(other.x))
			return false;
		if (NumberUtils.floatToIntBits(y) != NumberUtils.floatToIntBits(other.y))
			return false;
		return true;
	}

	/**
	 * Compares this vector with the other vector, using the supplied
	 * epsilon for fuzzy equality testing.
	 * @param obj
	 * @param epsilon
	 * @return whether the vectors are the same.
	 */
	public boolean epsilonEquals(Vector2 obj, float epsilon) {
		if(obj == null) return false;
		if(Math.abs(obj.x - x) > epsilon) return false;
		if(Math.abs(obj.y - y) > epsilon) return false;
		return true;
	}
}
