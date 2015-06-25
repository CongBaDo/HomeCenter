package com.HomeCenter2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.house.Device;

public class MyDeviceTypeAdapter extends BaseAdapter{

	private static final String TAG = "MyDeviceTypeAdapter";
	Device[] mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;

	public MyDeviceTypeAdapter(Context context, Device[] items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);
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
			view = newView(position);
		}
		ViewHolder viewHolder = (ViewHolder) view.getTag();		

		Device item = mItems[position];
		if (item == null) {
			return view;
		}		
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getName());		
		return view;
	}

	private View newView(int position) {
		View view = mInflater.inflate(R.layout.my_device_type_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.cbChoose = (CheckedTextView) view.findViewById(R.id.img_choose_type);
		
		view.setTag(viewHolder);
		return view;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
		CheckedTextView cbChoose;
		
	}
}
