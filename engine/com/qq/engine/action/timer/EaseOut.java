package com.qq.engine.action.timer;

/**
 * 变速运动   rate<1 加速  rate > 1 减速
 * @author wuqingqing
 *
 */
public class EaseOut extends EaseTimer {


	public EaseOut(float t, float rate) {
		super(t);
		// TODO Auto-generated constructor stub
		this.setRate(rate);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
        per = (float) Math.pow(per, 1 / rate);
	}
}
