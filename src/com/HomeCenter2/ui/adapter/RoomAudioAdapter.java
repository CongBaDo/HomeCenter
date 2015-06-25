package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenEntry;


public class RoomAudioAdapter extends BaseAdapter{
	List<Room> mItems=null;
	Context mContext= null;
	LayoutInflater mInflater;
	
	public RoomAudioAdapter(Context context, List<Room> items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);		
	} 
	
	@Override
	public int getCount() {
		if(mItems == null)
			return 0;
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		if(mItems == null)
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
		if(view == null){
			view= newView();			
		}
		Room item = mItems.get(position);
		if(item == null){
			return view;
		}		
		TextView tvRoom = (TextView) view;
		//tvRoom.setText(String.valueOf(item.getId()));
		tvRoom.setText(item.getName());
		
		tvRoom.setWidth(200);
		return view;
	}
	
	private View newView(){
		View view = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		return view;
	}
}
