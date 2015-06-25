package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

public class MyRemoteAdapter extends FragmentStatePagerAdapter {
	List<Fragment> mListFragments;	
	ActionBarActivity mContext;

	public MyRemoteAdapter(ActionBarActivity context,
			FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		mContext = context;
		mListFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mListFragments.get(position);
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
		return POSITION_NONE;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// do nothing here! no call to super.restoreState(arg0, arg1);
	}
}