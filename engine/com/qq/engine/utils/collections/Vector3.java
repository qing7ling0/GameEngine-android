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

import com.qq.engine.opengl.glutils.Matrix4;
import com.qq.engine.utils.NumberUtils;


public class Vector3 implements Serializable {
	private static final long serialVersionUID = 3840054589595372522L;

	/** the x-component of this vector **/
	public float x;
	/** the x-component of this vector **/
	public float y;
	/** the x-component of this vector **/
	public float z;

	/** Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp = new Vector3();
	/** Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp2 = new Vector3();
	/** Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp3 = new Vector3();

	public final static Vector3 X = new Vector3(1, 0, 0);
	public final static Vector3 Y = new Vector3(0, 1, 0);
	public final static Vector3 Z = new Vector3(0, 0, 1);
	public final static Vector3 Zero = new Vector3(0, 0, 0);

	public Vector3 () {
	}

	public Vector3 (float x, float y, float z) {
		this.set(x, y, z);
	}

	public Vector3 (Vector3 vector) {
		this.set(vector);
	}

	public Vector3 (float[] values) {
		this.set(values[0], values[1], values[2]);
	}

	public Vector3 set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 set (Vector3 vector) {
		return this.set(vector.x, vector.y, vector.z);
	}

	public Vector3 set (float[] values) {
		return this.set(values[0], values[1], values[2]);
	}

	public Vector3 cpy () {
		return new Vector3(this);
	}

	public Vector3 tmp () {
		return tmp.set(this);
	}

	public Vector3 tmp2 () {
		return tmp2.set(this);
	}

	Vector3 tmp3 () {
		return tmp3.set(this);
	}

	public Vector3 add (Vector3 vector) {
		return this.add(vector.x, vector.y, vector.z);
	}

	public Vector3 add (float x, float y, float z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}

	public Vector3 add (float values) {
		return this.set(this.x + values, this.y + values, this.z + values);
	}

	public Vector3 sub (Vector3 a_vec) {
		return this.sub(a_vec.x, a_vec.y, a_vec.z);
	}

	public Vector3 sub (float x, float y, float z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}

	public Vector3 sub (float value) {
		return this.set(this.x - value, this.y - value, this.z - value);
	}

	public Vector3 mul (float value) {
		return this.set(this.x * value, this.y * value, this.z * value);
	}

	public Vector3 div (float value) {
		float d = 1 / value;
		return this.set(this.x * d, this.y * d, this.z * d);
	}

	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	public float len2 () {
		return x * x + y * y + z * z;
	}

	public boolean idt (Vector3 vector) {
		return x == vector.x && y == vector.y && z == vector.z;
	}

	public float dst (Vector3 vector) {
		float a = vector.x - x;
		float b = vector.y - y;
		float c = vector.z - z;

		a *= a;
		b *= b;
		c *= c;

		return (float)Math.sqrt(a + b + c);
	}

	public Vector3 nor () {
		float len = this.len();
		if (len == 0) {
			return this;
		} else {
			return this.div(len);
		}
	}

	public float dot (Vector3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public Vector3 crs (Vector3 vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}

	public Vector3 crs (float x, float y, float z) {
		return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
	}

	public Vector3 mul (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03], x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13], x * l_mat[Matrix4.M20] + y
			* l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]);
	}

	public Vector3 prj (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		float l_w = x * l_mat[Matrix4.M30] + y * l_mat[Matrix4.M31] + z * l_mat[Matrix4.M32] + l_mat[Matrix4.M33];
		return this.set((x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03]) / l_w, (x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13])
			/ l_w, (x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]) / l_w);
	}

	public Vector3 rot (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02], x * l_mat[Matrix4.M10] + y
			* l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12], x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22]);
	}

	public boolean isUnit () {
		return this.len() == 1;
	}

	public boolean isZero () {
		return x == 0 && y == 0 && z == 0;
	}

	public Vector3 lerp (Vector3 target, float alpha) {
		Vector3 r = this.mul(1.0f - alpha);
		r.add(target.tmp().mul(alpha));
		return r;
	}

	public Vector3 slerp (Vector3 target, float alpha) {
		float dot = dot(target);
		if (dot > 0.99995 || dot < 0.9995) {
			this.add(target.tmp().sub(this).mul(alpha));
			this.nor();
			return this;
		}

		if (dot > 1) dot = 1;
		if (dot < -1) dot = -1;

		float theta0 = (float)Math.acos(dot);
		float theta = theta0 * alpha;
		Vector3 v2 = target.tmp().sub(x * dot, y * dot, z * dot);
		v2.nor();
		return this.mul((float)Math.cos(theta)).add(v2.mul((float)Math.sin(theta))).nor();
	}

	/** {@inheritDoc} */
	public String toString () {
		return x + "," + y + "," + z;
	}

	public float dot (float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	public float dst2 (Vector3 point) {

		float a = point.x - x;
		float b = point.y - y;
		float c = point.z - z;

		a *= a;
		b *= b;
		c *= c;

		return a + b + c;
	}

	public float dst2 (float x, float y, float z) {
		float a = x - this.x;
		float b = y - this.y;
		float c = z - this.z;

		a *= a;
		b *= b;
		c *= c;

		return a + b + c;
	}

	public float dst (float x, float y, float z) {
		return (float)Math.sqrt(dst2(x, y, z));
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + NumberUtils.floatToIntBits(x);
		result = prime * result + NumberUtils.floatToIntBits(y);
		result = prime * result + NumberUtils.floatToIntBits(z);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3 other = (Vector3)obj;
		if (NumberUtils.floatToIntBits(x) != NumberUtils.floatToIntBits(other.x)) return false;
		if (NumberUtils.floatToIntBits(y) != NumberUtils.floatToIntBits(other.y)) return false;
		if (NumberUtils.floatToIntBits(z) != NumberUtils.floatToIntBits(other.z)) return false;
		return true;
	}

	/**
	 * Compares this vector with the other vector, using the supplied
	 * epsilon for fuzzy equality testing.
	 * @param obj
	 * @param epsilon
	 * @return whether the vectors are the same.
	 */
	public boolean epsilonEquals(Vector3 obj, float epsilon) {
		if(obj == null) return false;
		if(Math.abs(obj.x - x) > epsilon) return false;
		if(Math.abs(obj.y - y) > epsilon) return false;
		if(Math.abs(obj.z - z) > epsilon) return false;
		return true;
	}
	
	/**
	 * Compares this vector with the other vector, using the supplied
	 * epsilon for fuzzy equality testing.
	 * @return whether the vectors are the same.
	 */
	public boolean epsilonEquals(float x, float y, float z, float epsilon) {
		if(Math.abs(x - this.x) > epsilon) return false;
		if(Math.abs(y - this.y) > epsilon) return false;
		if(Math.abs(z - this.z) > epsilon) return false;
		return true;
	}
	
	/** Scales the vector components by the given scalars.
	 * 
	 * @param scalarX
	 * @param scalarY
	 * @param scalarZ */
	public Vector3 scale (float scalarX, float scalarY, float scalarZ) {
		x *= scalarX;
		y *= scalarY;
		z *= scalarZ;
		return this;
	}
}
