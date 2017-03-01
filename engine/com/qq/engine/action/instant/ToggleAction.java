package com.qq.engine.action.instant;

import com.qq.engine.scene.NodeProperty;

public class ToggleAction extends InstantAction {

    public static ToggleAction create() {
        return new ToggleAction();
    }

	@Override
	public void start(NodeProperty node) {
		// TODO Auto-generated method stub
		super.start(node);
		target.setVisible(!target.isVisible());
	}

	@Override
	public ToggleAction getCopy() {
		// TODO Auto-generated method stub
		return new ToggleAction();
	}
	
}
