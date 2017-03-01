package com.qq.engine.action.ease;

import com.qq.engine.action.IntervalAction;


/** Ease Elastic abstract class
 @since v0.8.2
 */
public abstract class EaseElastic extends EaseAction {
	/** period of the wave in radians. default is 0.3 */
	protected float period_;

	/** Initializes the action with the inner action 
	 * 	and the period in radians (default is 0.3) */
    protected EaseElastic(IntervalAction action, float period) {
        super(action);
        period_ = period;
    }
    
    protected EaseElastic(IntervalAction action) {
    	this(action, 0.3f);
    }

    @Override
    public abstract EaseAction getCopy();

    @Override
    public abstract IntervalAction reverse();
}
