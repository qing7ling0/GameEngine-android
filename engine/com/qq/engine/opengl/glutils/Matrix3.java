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

import java.io.Serializable;

import com.qq.engine.drawing.PointF;
import com.qq.engine.utils.MathUtils;
import com.qq.engine.utils.collections.Vector3;


/** A 3x3 column major matrix for 2D transforms.
 * 
 * @author mzechner */
public class Matrix3 implements Serializable {
	
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;

	public static final int TOP             = 1<<1;
    public static final int BOTTOM          = 1<<2;
    public static final int LEFT            = 1<<3;
    public static final int RIGHT           = 1<<4;
    public static final int VCENTER         = 1<<5;
    public static final int HCENTER         = 1<<6;
	
	private static final long serialVersionUID = 7907569533774959788L;
	private final static float DEGREE_TO_RAD = (float)Math.PI / 180;
	public static final int M00 = 0;
	public static final int M10 = 1;
	public static final int M20 = 2;
	public static final int M01 = 3;
	public static final int M11 = 4;
	public static final int M21 = 5;
	public static final int M02 = 6;
	public static final int M12 = 7;
	public static final int M22 = 8;
	public float[] val = new float[9];
	private float[] tmp = new float[9];

	public Matrix3 () {
		idt();
	}

	public Matrix3 (Matrix3 matrix) {
		set(matrix);
	}

	/** Sets this matrix to the identity matrix
	 * @return this matrix */
	public void idt () {
		this.val[0] = 1;
		this.val[1] = 0;
		this.val[2] = 0;

		this.val[3] = 0;
		this.val[4] = 1;
		this.val[5] = 0;

		this.val[6] = 0;
		this.val[7] = 0;
		this.val[8] = 1;
	}

	/** Multiplies this matrix with the other matrix in the order this * m.
	 * @return this matrix */
	public void mul (Matrix3 m) {
		
		mul(val, m.val);
	}

	/** Sets this matrix to a rotation matrix that will rotate any vector in counter clockwise order around the z-axis.
	 * @param degrees the angle in degrees.
	 * @return this matrix */
	public Matrix3 setToRotation (float degrees) {
		float angle = DEGREE_TO_RAD * degrees;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);

		this.val[0] = cos;
		this.val[1] = sin;
		this.val[2] = 0;

		this.val[3] = -sin;
		this.val[4] = cos;
		this.val[5] = 0;

		this.val[6] = 0;
		this.val[7] = 0;
		this.val[8] = 1;

		return this;
	}

	/** Sets this matrix to a translation matrix.
	 * @param x the translation in x
	 * @param y the translation in y
	 * @return this matrix */
	public Matrix3 setToTranslation (float x, float y) {
		this.val[0] = 1;
		this.val[1] = 0;
		this.val[2] = 0;

		this.val[3] = 0;
		this.val[4] = 1;
		this.val[5] = 0;

		this.val[6] = x;
		this.val[7] = y;
		this.val[8] = 1;

		return this;
	}

	/** Sets this matrix to a scaling matrix
	 * 
	 * @param scaleX the scale in x
	 * @param scaleY the scale in y
	 * @return this matrix */
	public Matrix3 setToScaling (float scaleX, float scaleY) {
		this.val[0] = scaleX;
		this.val[1] = 0;
		this.val[2] = 0;

		this.val[3] = 0;
		this.val[4] = scaleY;
		this.val[5] = 0;

		this.val[6] = 0;
		this.val[7] = 0;
		this.val[8] = 1;

		return this;
	}

	public String toString () {
		String[] values = {Float.toString(val[M00]), Float.toString(val[M01]), Float.toString(val[M02]), Float.toString(val[M10]),
			Float.toString(val[M11]), Float.toString(val[M12]), Float.toString(val[M20]), Float.toString(val[M21]),
			Float.toString(val[M22])};
		int max1 = Math.max(values[0].length(), Math.max(values[3].length(), values[6].length()));
		int max2 = Math.max(values[1].length(), Math.max(values[4].length(), values[7].length()));
		int max3 = Math.max(values[2].length(), Math.max(values[5].length(), values[8].length()));
		return String.format("%" + max1 + "s, %" + max2 + "s, %" + max3 + "s\n%" + max1 + "s, %" + max2 + "s, %" + max3 + "s\n%"
			+ max1 + "s, %" + max2 + "s, %" + max3 + "s", (Object[])values);
	}

	/** @return the determinant of this matrix */
	public float det () {
		return val[0] * val[4] * val[8] + val[3] * val[7] * val[2] + val[6] * val[1] * val[5] - val[0] * val[7] * val[5] - val[3]
			* val[1] * val[8] - val[6] * val[4] * val[2];
	}

	/** Inverts this matrix given that the determinant is != 0
	 * @return this matrix */
	public Matrix3 inv () {
		float det = det();
		if (det == 0) throw new RuntimeException("Can't invert a singular matrix");

		float inv_det = 1.0f / det;

		tmp[0] = val[4] * val[8] - val[5] * val[7];
		tmp[1] = val[2] * val[7] - val[1] * val[8];
		tmp[2] = val[1] * val[5] - val[2] * val[4];
		tmp[3] = val[5] * val[6] - val[3] * val[8];
		tmp[4] = val[0] * val[8] - val[2] * val[6];
		tmp[5] = val[2] * val[3] - val[0] * val[5];
		tmp[6] = val[3] * val[7] - val[4] * val[6];
		tmp[7] = val[1] * val[6] - val[0] * val[7];
		tmp[8] = val[0] * val[4] - val[1] * val[3];

		val[0] = inv_det * tmp[0];
		val[1] = inv_det * tmp[1];
		val[2] = inv_det * tmp[2];
		val[3] = inv_det * tmp[3];
		val[4] = inv_det * tmp[4];
		val[5] = inv_det * tmp[5];
		val[6] = inv_det * tmp[6];
		val[7] = inv_det * tmp[7];
		val[8] = inv_det * tmp[8];

		return this;
	}

	public void set (Matrix3 mat) {

		float[] va = mat.val;
		float[] val = this.val;
		val[0] = va[0];
		val[1] = va[1];
		val[2] = va[2];
		val[3] = va[3];
		val[4] = va[4];
		val[5] = va[5];
		val[6] = va[6];
		val[7] = va[7];
		val[8] = va[8];
	}

	public void set (Matrix4 mat) {
		val[0] = mat.val[0];
		val[1] = mat.val[1];
		val[2] = mat.val[2];
		val[3] = mat.val[4];
		val[4] = mat.val[5];
		val[5] = mat.val[6];
		val[6] = mat.val[8];
		val[7] = mat.val[9];
		val[8] = mat.val[10];
	}

	/** Adds a translational component to the matrix in the 3rd column. The other columns are untouched.
	 * @param vector The translation vector
	 * @return This matrix for chaining */
	public Matrix3 trn (Vector3 vector) {
		val[6] += vector.x;
		val[7] += vector.y;
		return this;
	}

	/** Adds a translational component to the matrix in the 3rd column. The other columns are untouched.
	 * @param x The x-component of the translation vector
	 * @param y The y-component of the translation vector
	 * @return This matrix for chaining */
	public Matrix3 trn (float x, float y) {
		val[6] += x;
		val[7] += y;
		return this;
	}

	/** Postmultiplies this matrix by a translation matrix. Postmultiplication is also used by OpenGL ES'
	 * glTranslate/glRotate/glScale
	 * @param x
	 * @param y
	 * @return this matrix for chaining */
	public void translate (float x, float y) {
		tmp[0] = 1;
		tmp[1] = 0;
		tmp[2] = 0;

		tmp[3] = 0;
		tmp[4] = 1;
		tmp[5] = 0;

		tmp[6] = x;
		tmp[7] = y;
		tmp[8] = 1;
		mul(val, tmp);
	}
	
	public void transform(int transfrom, float width, float height, float anchorx, float anchory)
	{
		float w = width;
		float h = height;
		switch (transfrom)
		{
		case TRANS_ROT90: 
		case TRANS_ROT270:
		case TRANS_MIRROR_ROT90:
		case TRANS_MIRROR_ROT270:
			w = height;
			h = width;
			break;
		}
		if (anchorx != 0 || anchory != 0) translate(- w*anchorx, - h*anchory);
		
		switch(transfrom)
		{
		case TRANS_MIRROR:
			translate(w, 0);
			scale(-1, 1);
			break;
		case TRANS_MIRROR_ROT90:
			translate(w, 0);
			rotate(90);
			translate(h, 0);
			scale(-1, 1);
			break;
		case TRANS_MIRROR_ROT180:
			translate(w, h);
			rotate(180);
			translate(w, 0);
			scale(-1, 1);
			break;
		case TRANS_MIRROR_ROT270:
			translate(0, h);
			rotate(270);
			translate(h, 0);
			scale(-1, 1);
			break;
		case TRANS_ROT90:
			translate(w, 0);
			rotate(90);
			break;
		case TRANS_ROT180:
			translate(w, h);
			rotate(180);
			break;
		case TRANS_ROT270:
			translate(0, h);
			rotate(270);
			break;
		}
	}

	/** Postmultiplies this matrix with a (counter-clockwise) rotation matrix. Postmultiplication is also used by OpenGL ES'
	 * glTranslate/glRotate/glScale
	 * @param angle the angle in degrees
	 * @return this matrix for chaining */
	public void rotate (float angle) {
		if (angle == 0) return;
		angle = DEGREE_TO_RAD * angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);

		tmp[0] = cos;
		tmp[1] = sin;
		tmp[2] = 0;

		tmp[3] = -sin;
		tmp[4] = cos;
		tmp[5] = 0;

		tmp[6] = 0;
		tmp[7] = 0;
		tmp[8] = 1;
		mul(val, tmp);
	}
	
	public void rot(float angle)
	{
		angle = DEGREE_TO_RAD * angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);

		tmp[M00] *= cos;
		tmp[M10] *= sin;

		tmp[M01] *= cos;
		tmp[M11] *= sin;
	}

	/** Postmultiplies this matrix with a scale matrix. Postmultiplication is also used by OpenGL ES' glTranslate/glRotate/glScale.
	 * @param scaleX
	 * @param scaleY
	 * @return this matrix for chaining */
	public void scale (float scaleX, float scaleY) {
		tmp[0] = scaleX;
		tmp[1] = 0;
		tmp[2] = 0;

		tmp[3] = 0;
		tmp[4] = scaleY;
		tmp[5] = 0;

		tmp[6] = 0;
		tmp[7] = 0;
		tmp[8] = 1;
		mul(val, tmp);
	}
	
	public void skew (float skewX, float skewY) {

		tmp[0] = 1;
		tmp[1] = skewX;
		tmp[2] = 0;

		tmp[3] = skewY;
		tmp[4] = 1;
		tmp[5] = 0;

		tmp[6] = 0;
		tmp[7] = 0;
		tmp[8] = 1;
		mul(val, tmp);
	}

	public float[] getValues () {
		return val;
	}

	public void scl (Vector3 scale) {
		val[M00] *= scale.x;
		val[M11] *= scale.y;
	}

	public void scl (float scale) {
		val[M00] *= scale;
		val[M11] *= scale;
	}
	
	public void scl (float scalex, float scaley) {
		val[M00] *= scalex;
		val[M11] *= scaley;
	}

	public void transpose () {
		float v00 = val[M00];
		float v01 = val[M10];
		float v02 = val[M20];
		float v10 = val[M01];
		float v11 = val[M11];
		float v12 = val[M21];
		float v20 = val[M02];
		float v21 = val[M12];
		float v22 = val[M22];
		val[M00] = v00;
		val[M01] = v01;
		val[M02] = v02;
		val[M10] = v10;
		val[M11] = v11;
		val[M12] = v12;
		val[M20] = v20;
		val[M21] = v21;
		val[M22] = v22;
	}

	private static void mul (float[] mata, float[] matb) {
		final float[] ma = mata;
		final float[] mb = matb;
		
		final float v00 = ma[0] * mb[0] + ma[3] * mb[1] + ma[6] * mb[2];
		final float v01 = ma[0] * mb[3] + ma[3] * mb[4] + ma[6] * mb[5];
		final float v02 = ma[0] * mb[6] + ma[3] * mb[7] + ma[6] * mb[8];

		final float v10 = ma[1] * mb[0] + ma[4] * mb[1] + ma[7] * mb[2];
		final float v11 = ma[1] * mb[3] + ma[4] * mb[4] + ma[7] * mb[5];
		final float v12 = ma[1] * mb[6] + ma[4] * mb[7] + ma[7] * mb[8];

		final float v20 = ma[2] * mb[0] + ma[5] * mb[1] + ma[8] * mb[2];
		final float v21 = ma[2] * mb[3] + ma[5] * mb[4] + ma[8] * mb[5];
		final float v22 = ma[2] * mb[6] + ma[5] * mb[7] + ma[8] * mb[8];

		ma[0] = v00;
		ma[1] = v10;
		ma[2] = v20;
		ma[3] = v01;
		ma[4] = v11;
		ma[5] = v21;
		ma[6] = v02;
		ma[7] = v12;
		ma[8] = v22;
	}
	
	
	public void matrixPoint(PointF point)
	{
		PointF p = point;
		
		float x = p.x;
		float y = p.y;
		
		final float[] va = val;

		x = p.x * va[M00] + p.y * va[M01] + va[M02];
		y = p.x * va[M10] + p.y * va[M11] + va[M12];
		
		p.x = x;
		p.y = y;
	}
}
