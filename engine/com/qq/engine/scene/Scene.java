package com.qq.engine.scene;

import com.qq.engine.graphics.Graphics;
import com.qq.engine.view.Screen;

public class Scene extends Node {

	
	public static Scene create()
	{
		Scene scene = new Scene();
		scene.init();
		
		return scene;
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		isAnchorPointForPosition = true;
		setContentSize(Screen.GAME_W, Screen.GAME_H);
		setAnchor(Graphics.HCENTER | Graphics.VCENTER);
	}
	
}
