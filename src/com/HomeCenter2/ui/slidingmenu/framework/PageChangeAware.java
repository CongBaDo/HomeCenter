package com.HomeCenter2.ui.slidingmenu.framework;

/**
 * Implemented by pages (mFragments) that want notifications when
 * they are selected or deselected (i.e. another page
 * is selected) in the sliding menu framework.
 * 
 * @author mmartikainen
 *
 */
public interface PageChangeAware {
	/**
	 * Notification that this page has been selected for display.
	 * It may not yet be available for the user to 
	 * interact with.
	 * 
	 * onScreenSlidingCompleted() will be called 
	 * once the page is fully visible (i.e. the sliding
	 * menu animation has completed). 
	 */
	void onPageSelected();
	
	/**
	 * Notification that this page will no longer be the selected page,
	 * because another page is being selected.
	 */
	void onPageDeselected();
	
	/**
	 * Notification that this page is selected and the sliding menu animation
	 * has completed.
	 */
	void onScreenSlidingCompleted();
	
	/**
	 * Notification that the sliding menu has been opened.
	 */
	void onMenuOpened();
	
	/**
	 * Notification that the sliding menu has been closed.
	 */
	void onMenuClosed();
}
