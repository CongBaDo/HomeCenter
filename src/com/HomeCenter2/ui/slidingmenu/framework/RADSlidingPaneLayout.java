package com.HomeCenter2.ui.slidingmenu.framework;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RADSlidingPaneLayout extends SlidingPaneLayout{
	public RADSlidingPaneLayout(Context context, AttributeSet attrs){
		 super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
	    if (!this.isOpen() && event.getX() > (getWidth() / 5)) {
	            return false;
	        }
	    return super.onInterceptTouchEvent(event);
	}
}
