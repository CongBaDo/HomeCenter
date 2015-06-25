package com.HomeCenter2.ui.slidingmenu.framework;
public interface OnPageScrolledCompleteListener {
		/**
		 * Called after the sliding menu has finished moving and the content page is fully visible.
		 * 
		 * Not called if the menu did not scroll.
		 */
		public void onViewScrolledComplete(boolean isShow);
	}
