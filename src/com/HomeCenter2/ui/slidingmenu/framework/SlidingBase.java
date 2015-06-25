package com.HomeCenter2.ui.slidingmenu.framework;

import com.HomeCenter2.HomeCenterUIEngine;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SlidingBase {
	
	private static final String TAG = "SlidingBase";
	
	private LinearLayout mKeepContentView;
	private FrameLayout mContentView, mMenuPane, mContentPane;	
	private SlidingPaneLayout mSlidingPaneLayout;
	private View mShadow;
	Context mContext = null;
	
    private View mOverlayView = null;
    private View mOverlayContent = null;

	public SlidingBase(Context context) {
		mContext = context;
	}

	public void setMenu(int id) {
		ActionBarActivity activity = (ActionBarActivity) mContext;
		if (activity != null) {
			setContent(activity.getLayoutInflater().inflate(id, null));
		}
	}

	public void setMenu(View view) {
		mMenuPane.addView(view);
	}

	public void setMenu(Fragment fragment) {
		ActionBarActivity activity = (ActionBarActivity) mContext;
		if (activity != null) {
			activity.getSupportFragmentManager()
					.beginTransaction()
					.replace(mMenuPane.getId(), fragment,
							HomeCenterUIEngine.MENU_SCREEN).commit();
		}
	}

	public FrameLayout getMenuPane() {
		return mMenuPane;
	}

	public void setMenuPane(FrameLayout mMenuPane) {
		this.mMenuPane = mMenuPane;
	}

	public void setContent(int id) {
		ActionBarActivity activity = (ActionBarActivity) mContext;
		if (activity != null) {
			setContent(activity.getLayoutInflater().inflate(id, null));
		}
	}

	public void setContent(View view) {
		mContentView.removeAllViews();
		mContentView.addView(view);
		
        mOverlayContent = null;
        mOverlayView = null; 
	}

	public void setContent(Fragment fragment) {
		ActionBarActivity activity = (ActionBarActivity) mContext;
		if (activity != null) {
			activity.getSupportFragmentManager()
					.beginTransaction()
					.replace(mContentView.getId(), fragment,
							HomeCenterUIEngine.MAIN_SCREEN).commit();
		}
	}

	public FrameLayout getContent() {
		return mContentView;
	}

	public void setContent(FrameLayout mContentView) {
		this.mContentView = mContentView;
	}

	public FrameLayout getContentPane() {
		return mContentPane;
	}

	public void setContentPane(FrameLayout mContentPane) {
		this.mContentPane = mContentPane;
	}

	public LinearLayout getKeepContentView() {
		return mKeepContentView;
	}

	public void setKeepContentView(LinearLayout mKeepContentView) {
		this.mKeepContentView = mKeepContentView;
	}

	//using
	public void showContent(){
		/*if(mSlidingLayout.isDrawerOpen(mMenuPane)){
			mSlidingLayout.closeDrawer(mMenuPane);
			
		}*/
		if (mSlidingPaneLayout.isSlideable()) {
			if (mSlidingPaneLayout.isOpen()) {
				mSlidingPaneLayout.closePane();
			}
		} else {
			Log.d(TAG, "showcontent not scroll");
			SlidingBaseActivity activity = (SlidingBaseActivity) mContext;
			activity.panelChangedNotSlide();
		}

	}
	
	//1 screen using
	public void showContent(boolean animation){
		showContent(); //need animation
	}
	
	//1 screen using
	public void showMenu(){
		/*if(!mSlidingLayout.isDrawerOpen(mMenuPane)){
			mSlidingLayout.openDrawer(mMenuPane);
		}		*/
		if(mSlidingPaneLayout.isOpen()){
			mSlidingPaneLayout.openPane();			
		}
	}
	
	//alwary using 
	public void showMenu(boolean animation){
		showMenu();
	}
	
	public boolean isMenuShowing(){
		//return mSlidingLayout.isDrawerOpen(mMenuPane);
		return mSlidingPaneLayout.isOpen();
	}
	
	public void clickHomeButton(){
		/*if(mSlidingLayout.isDrawerOpen(mMenuPane)){
			mSlidingLayout.closeDrawer(mMenuPane);			
		}else{
			mSlidingLayout.openDrawer(mMenuPane);
		}*/
		if(mSlidingPaneLayout.isOpen()){
			mSlidingPaneLayout.closePane();
		}else{
			mSlidingPaneLayout.openPane();
		}
	}

	public SlidingPaneLayout getSlidingPaneLayout() {
		return mSlidingPaneLayout;
	}

	public void setSlidingPaneLayout(SlidingPaneLayout mSlidingPaneLayout) {
		this.mSlidingPaneLayout = mSlidingPaneLayout;
	}

	//Help overlay


	public void setOverlayView(View v) {
        if (mOverlayView != null)
        {
        	mContentPane.removeView(mOverlayView);
            mOverlayContent = null;
            mOverlayView = null;
        }
        mOverlayView = v;
        mContentPane.addView(mOverlayView);	    
    }
	
	public void removeOverlayView()
	{
	    if (mOverlayView != null)
        {
	    	mContentPane.removeView(mOverlayView);
        }
	    mOverlayContent = null;
        mOverlayView = null;
	}
	
	public void setOverlayContent(View v)
	{
	    mOverlayContent = v;
	}
	
	public void setOverlayContentView(View view)
	{
	    setOverlayView(view);
	}

	public View getShadow() {
		return mShadow;
	}

	public void setShadow(View mShadow) {
		this.mShadow = mShadow;
	}

}
