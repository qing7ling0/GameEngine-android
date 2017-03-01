package com.qq.engine.action.interval;

import com.qq.engine.drawing.Color;
import com.qq.engine.scene.NodeProperty;

public class ColorBy extends ColorTo {

	
	public ColorBy(float dt, float dr, float dg, float db, float da) {
		super(dt, new Color());
		dupR = dr;
		dupG = dg;
		dupB = db;
		dupA = da;
	}
	
	public static ColorBy create(float time, float dr, float dg, float db, float da)
	{
		return new ColorBy(time, dr, dg, db, da);
	}
	
	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		float tempr = dupR;
		float tempg = dupG;
		float tempb = dupB;
		float tempa = dupA;
		super.start(node);
		dupR = tempr;
		dupG = tempg;
		dupB = tempb;
		dupA = tempa;
	}
	
	@Override
	public ColorBy getCopy() {
		// TODO Auto-generated method stub
		return new ColorBy(duraction, dupR, dupG, dupB, dupA);
	}
	
	@Override
	public ColorBy reverse() {
		// TODO Auto-generated method stub
		return new ColorBy(duraction, -dupR, -dupG, -dupB, -dupA);
	}
}