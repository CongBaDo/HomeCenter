package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.HomeCenter2.fragment.RoomManageFragment;
import com.HomeCenter2.house.Room;

/**
 * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
 * could be a large number of items in the ViewPager and we don't want to retain them all in
 * memory at once but create/destroy them on the fly.
 */
public class RoomManagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = "RoomManagerAdapter";
    private final int mSize;
    private List<Room> rooms;
    
    public RoomManagerAdapter(Context ctx, FragmentManager fm, List<Room> rooms) {
        super(fm);
        this.rooms = rooms;
        mSize = rooms.size();
    }
    
    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
    	Log.d(TAG, "getItem postition "+position);
    		return RoomManageFragment.newInstance(position);
    }
    
    @Override
    public int getItemPosition(Object object) {
    	Log.i(TAG, "BAFjhdsjf ");
    	  return POSITION_NONE;
	 }
    
    @Override
    public void destroyItem(View collection, int position, Object o) {
        Log.d(TAG, "destroying view at position " + position);
        View view = (View) o;
        ((ViewPager) collection).removeView(view);
        view = null;
    }
    
    
    
    
    @Override
    public void finishUpdate(ViewGroup container) {
    	super.finishUpdate(container);
    }
    
    @Override
    public float getPageWidth(int position) {
    	// TODO Auto-generated method stub
    		return 1.0f;
    }
}
