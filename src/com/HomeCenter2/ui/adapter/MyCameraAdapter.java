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

import com.HomeCenter2.R;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.Temperature;

public class MyCameraAdapter extends BaseAdapter{

	private static final String TAG = "MyCameraAdapter";
	ArrayList<Device> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;

	public MyCameraAdapter(Context context, ArrayList<Device> items) {
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
		if (mItems == null)
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

		Device item = mItems.get(position);
		if (item == null) {
			return view;
		}		
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getName());
		if(item instanceof Temperature){
			Temperature temp= (Temperature) item;
			viewHolder.txtContent.setText(String.valueOf(temp.getTemperature()));
			viewHolder.txtContent.setVisibility(View.VISIBLE);
		}else{
			viewHolder.txtContent.setVisibility(View.GONE);
		}
		

		return view;
	}

	private View newView(int position) {
		View view = mInflater.inflate(R.layout.my_sensor_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtContent =(TextView) view.findViewById(R.id.txtContent);		
		view.setTag(viewHolder);
		return view;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
		TextView txtContent;		
	}

	public void onChangeData(ArrayList<Device> items) {
		if (mItems != null) {
			mItems.clear();
		}
		mItems = items;
		notifyDataSetChanged();
	}


}
