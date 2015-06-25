package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.PInfo;
import com.HomeCenter2.R;

public class MyApplicationssAdapter extends BaseAdapter {

	private static final String TAG = "MyDevicesAdapter";
	ArrayList<PInfo> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	

	public MyApplicationssAdapter(Context context, ArrayList<PInfo> items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (mItems == null)
			return 0;
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		if (mItems == null || position == -1)
			return null;
		return mItems.get(position);
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

		PInfo item = mItems.get(position);
		if (item == null) {
			return view;
		}		
		viewHolder.imgIcon.setImageDrawable(item.getIcon());
		viewHolder.txtTitle.setText(item.getAppname());		
		return view;
	}

	private View newView(int position) {
		View view = mInflater.inflate(R.layout.my_app_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtContent =(TextView) view.findViewById(R.id.txtContent);
		viewHolder.txtContent.setVisibility(View.GONE);
		
		viewHolder.cbChoose = (CheckedTextView) view.findViewById(R.id.img_choose_type);
		
		view.setTag(viewHolder);
		return view;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
		TextView txtContent;
		CheckedTextView cbChoose;
	}

	public void onChangeData(ArrayList<PInfo> items) {
		if (mItems != null) {
			mItems.clear();
		}
		mItems = items;
		notifyDataSetChanged();
	}

}
