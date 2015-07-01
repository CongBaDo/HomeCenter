package com.HomeCenter2.ui.slidingmenu.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.RegisterService;

public abstract class RADialerMainScreenAbstract extends RADialerScreenAbstract
		implements OnPageScrolledCompleteListener {

	protected FrameLayoutRADialer mFrameLayoutHeader;
	LinearLayout mLinearLayout = null;

	protected abstract View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	/**
	 * This method will be invoked when the screen page on the right scrolling
	 * completed. This is where the inherit child-screen should be override to
	 * load view's content to avoid UI hang and jerky sliding.
	 */
	public abstract void onScreenSlidingCompleted();

	public RADialerMainScreenAbstract(Class<?> clas, int title, String tag,
			SlidingBaseActivity context) {
		super(clas, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterService service = RegisterService.getService();
		if(service!= null){
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			uiEngine.addPageScrolledRObserver(this);
		}
	}

	@Override
	public void onCreated() {
		super.onCreated();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);

		mFrameLayoutHeader = new FrameLayoutRADialer(mContext);
		mFrameLayoutHeader.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLinearLayout.addView(mFrameLayoutHeader);

		View content = onCreateContentView(inflater, container,
				savedInstanceState);
		if (content != null) {
			mLinearLayout.addView(content);
		}

		super.onCreateView(inflater, container, savedInstanceState);
		return mLinearLayout;
	}

	protected void setFragmentActionBar(Fragment actionBar) {
		if (actionBar == null)
			return;
		if (mFrameLayoutHeader != null) {
			// In the case of device which has been re-launched from background
			// such as receiving call from background
			// the state of fragment can not be restored, therefore, change from
			// commit() to commitAllowingStateLoss()
			// to avoid
			// "java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState"
			getFragmentManager().beginTransaction()
					.add(mFrameLayoutHeader.getId(), actionBar)
					.commitAllowingStateLoss();
		}
	}

	/**
	 * This is primary function for main screen
	 */
	public void onClickHome() {
		mContext.clickHomeMenu();
	}

	public boolean backToPreviousScreen() {
		return mContext.switchToPreviousContentView();
		// return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			HomeCenter2Activity activity = (HomeCenter2Activity) getActivity();
			if (activity != null) {
				// return activity.switchToPreviousContentView();
			}
		}

		return false;
	}
	

	@Override
	public void onDestroy() {
		
		RegisterService service = RegisterService.getService();
		if(service!= null){
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			uiEngine.removePageScrolledObserver(this);
		}
		super.onDestroy();
	}

	
}
