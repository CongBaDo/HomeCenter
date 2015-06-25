package com.HomeCenter2.ui.slidingmenu.framework;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.HomeCenter2.R;

public abstract class RADialerMenuScreenAbstract extends RADialerScreenAbstract {

	public static int layoutXmlId = -1;
	
	protected abstract void setContentFromFragment();

	public RADialerMenuScreenAbstract(Class<?> clas,  String tag,SlidingBaseActivity context) {
		super(clas, -1,  tag, context);
	}

	public RADialerMenuScreenAbstract(Class<?> clas, int title,  String tag,
			SlidingBaseActivity context) {
		super(clas, title,tag, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		if (RADialerMenuScreenAbstract.layoutXmlId == R.layout.menu_screen_fragment1) {
			RADialerMenuScreenAbstract.layoutXmlId = R.layout.menu_screen_fragment2;
		} else {
			RADialerMenuScreenAbstract.layoutXmlId = R.layout.menu_screen_fragment1;
		}
		view = inflater.inflate(RADialerMenuScreenAbstract.layoutXmlId,
				container, false);
		this.setContentFromFragment();
		super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	protected void setFragmentActionBar(Fragment actionBar) {

		if (actionBar == null)
			return;

		if (RADialerMenuScreenAbstract.layoutXmlId == R.layout.menu_screen_fragment1) {
			getFragmentManager().beginTransaction()
					.replace(R.id.menu_header_fragment1, actionBar).commitAllowingStateLoss();
		} else {
			getFragmentManager().beginTransaction()
					.replace(R.id.menu_header_fragment2, actionBar).commitAllowingStateLoss();
		}
	}

	protected void setContentForScreen(Fragment contentMenu) {

		if (contentMenu == null)
			return;

		if (RADialerMenuScreenAbstract.layoutXmlId == R.layout.menu_screen_fragment1) {
			getFragmentManager().beginTransaction()
					.replace(R.id.menu_content_fragment1, contentMenu).commit();
		} else {
			getFragmentManager().beginTransaction()
					.replace(R.id.menu_content_fragment2, contentMenu).commit();
		}
	}

	/**
	 * This is primary function for menu screen
	 */

	public void showPopupMenu(View view, int item) {
		int popupWidth = 320;
		int popupHeight = item * 66;
		if (popupHeight > mHeigth)
			popupHeight = LayoutParams.MATCH_PARENT;
		int margin = 10;

		if (mPopup.isShowing())
			mPopup.dismiss();
		else {
			int OFFSET_X = mWidth - popupWidth - margin;
			int OFFSET_Y = 120;

			// Clear the default translucent background
			mPopup.setBackgroundDrawable(new BitmapDrawable());
			// Displaying the popup at the specified location, + offsets.
			mPopup.setContentView(view);
			mPopup.setWidth(popupWidth);
			mPopup.setHeight(popupHeight);
			mPopup.setFocusable(true);
			mPopup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
					OFFSET_Y);
		}
	}

	public void popBackStack() {
		getFragmentManager().popBackStack();
	}

}
