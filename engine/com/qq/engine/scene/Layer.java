package com.qq.engine.scene;

import com.qq.engine.view.Screen;

public class Layer extends Node{
	
	public static Layer create()
	{
		Layer layer = new Layer();
		layer.init();
		
		return layer;
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentSize(Screen.GAME_W, Screen.GAME_H);
	}

}
