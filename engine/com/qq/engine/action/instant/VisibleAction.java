package com.qq.engine.action.instant;

import com.qq.engine.scene.NodeProperty;

public class VisibleAction extends InstantAction {

	protected boolean visible;
	
	public static VisibleAction create(boolean visible)
	{
		return new VisibleAction(visible);
	}
	
	protected VisibleAction(boolean visible) {
		// TODO Auto-generated constructor stub
		super();
		this.visible = visible;
	}

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		target.setVisible(visible);
	}

	@Override
	public VisibleAction getCopy() {
		// TODO Auto-generated method stub
		return new VisibleAction(visible);
	}

	@Override
	public VisibleAction reverse() {
		// TODO Auto-generated method stub
		return new VisibleAction(!visible);
	}

}
