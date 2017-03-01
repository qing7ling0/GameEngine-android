package com.qq.engine.opengl.glutils;


/**
 * GL 数组
 * @author wuqingqing
 *
 */
public class VertexType {

	/** 纹理绘制区域数组 */
	public final static byte VERTEX_COORDINATES = 1;

	/** 纹理位置坐标数组 */
	public final static byte VERTEX_VERTICES = 2;
	
	/** 纹理颜色数组 */
	public final static byte VERTEX_COLOR = 3;
	
	public byte vertexType;
	public int numComponents;
	public int vertexSize;
	
	public VertexType(int vertexNum, byte vertexType) {
		this.vertexType = vertexType;
		this.numComponents = vertexNum;
		this.vertexSize = 4 * this.numComponents;
	}

}
