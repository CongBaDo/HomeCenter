package com.HomeCenter2.ui.adapter;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenEntry;

public class MainMenuAdapter extends BaseAdapter {
	ScreenEntry[] mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	private int mCurrentPosition;

	public MainMenuAdapter(Context context, ScreenEntry[] items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);
		if (items.length > 1) {
			mCurrentPosition = 1;
		} else {
			mCurrentPosition = -1;
		}
	}

	@Override
	public int getCount() {
		if (mItems == null)
			return 0;
		return mItems.length;
	}

	@Override
	public Object getItem(int position) {
		if (mItems == null)
			return null;
		return mItems[position];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = newView();
		}
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		ScreenEntry item = mItems[position];
		if (item == null) {
			return view;
		}
		viewHolder.txtTitle.setText(item.getTitleId());
		if (position == mCurrentPosition) {
			//view.setBackground(mContext.getResources().getDrawable(R.drawable.bg_black_and_translate));
			//view.setBackgroundColor(Color.BLACK);
			viewHolder.imgIcon.setImageResource(item.getIconIdSelected());
		} else {
			view.setBackgroundColor(Color.TRANSPARENT);			
			viewHolder.imgIcon.setImageResource(item.getIconId());			
		}
		return view;
	}

	private View newView() {
		View view = mInflater.inflate(R.layout.main_menu_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIconMenu);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitleMenu);
		view.setTag(viewHolder);
		return view;
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void updateCurrentPosition(int currentPosition) {
		mCurrentPosition = currentPosition;
		notifyDataSetChanged();
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

}