package com.HomeCenter2;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

import com.HomeCenter2.ui.slidingmenu.framework.ViewManager;

public class MainScreenPagerAdapter extends FragmentStatePagerAdapter {
	List<Fragment> mListFragments;
	ViewManager mViewManager;
	ActionBarActivity mContext;

	public MainScreenPagerAdapter(ActionBarActivity context, ViewManager vm,
			FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		mContext = context;
		mViewManager = vm;
		mListFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {		
		if(position>= 0 && position < mListFragments.size()){
			return mListFragments.get(position);	
		}
		return null;		
	}

	@Override
	public int getCount() {
		return mListFragments.size();
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {

		Fragment fragment = (Fragment) object;
		if (mViewManager.mNonPersistentViewList.contains(fragment)) {
			return POSITION_NONE;
		}
		return POSITION_UNCHANGED;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// do nothing here! no call to super.restoreState(arg0, arg1);
	}
}