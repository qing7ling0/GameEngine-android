package com.qq.engine.action.interval;

import com.qq.engine.action.IntervalAction;
import com.qq.engine.drawing.BezierConfig;
import com.qq.engine.drawing.PointF;
import com.qq.engine.scene.NodeProperty;

public class BezierTo extends IntervalAction {
	final BezierConfig originalconfig;
    protected BezierConfig config;
    protected PointF startPosition;
	
    public static BezierTo create(float t, BezierConfig c) {
        return new BezierTo(t, c);
    }

    protected BezierTo(float t, BezierConfig c) {
        super(t);
        this.config = c;
        originalconfig = new BezierConfig();
        originalconfig.controlPoint_1 = PointF.create(c.controlPoint_1.x, c.controlPoint_1.y);
        originalconfig.controlPoint_2 = PointF.create(c.controlPoint_2.x, c.controlPoint_2.y);
        originalconfig.endPosition = PointF.create(c.endPosition.x, c.endPosition.y);
    }

    @Override
    public void update(float per) {
        float xa = startPosition.x;
        float xb = config.controlPoint_1.x;
        float xc = config.controlPoint_2.x;
        float xd = config.endPosition.x;

        float ya = startPosition.y;
        float yb = config.controlPoint_1.y;
        float yc = config.controlPoint_2.y;
        float yd = config.endPosition.y;

        float x = BezierConfig.bezierAt(xa, xb, xc, xd, per);
        float y = BezierConfig.bezierAt(ya, yb, yc, yd, per);
        
        target.setPosition(PointF.create(x, y));
    }

    @Override
    public void start(NodeProperty aTarget) {
        super.start(aTarget);
        startPosition = PointF.create(aTarget.getPosition());

//        config.controlPoint_1 = PointF.sub(originalconfig.controlPoint_1, startPosition);
//        config.controlPoint_2 = PointF.sub(originalconfig.controlPoint_2, startPosition);
//        config.endPosition = PointF.sub(originalconfig.endPosition, startPosition);
//        config.controlPoint_1 = PointF.sub(originalconfig.controlPoint_1, startPosition);
//        config.controlPoint_2 = PointF.sub(originalconfig.controlPoint_2, startPosition);
//        config.endPosition = PointF.sub(originalconfig.endPosition, startPosition);
    }

    @Override
    public BezierTo getCopy() {
        return new BezierTo(duraction, config);
    }
    
    @Override
    public BezierTo reverse() {
        // TODO: reverse it's not working as expected
    	BezierConfig r = new BezierConfig();
        r.endPosition = PointF.negative(config.endPosition);
        r.controlPoint_1 = PointF.add(config.controlPoint_2, PointF.negative(config.endPosition));
        r.controlPoint_2 = PointF.add(config.controlPoint_1, PointF.negative(config.endPosition));

        return new BezierTo(duraction, r);
    }
}

