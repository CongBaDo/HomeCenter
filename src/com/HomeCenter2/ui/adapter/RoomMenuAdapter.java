package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.RoomMenuListener;


public class RoomMenuAdapter extends BaseAdapter{
	List<Room> mItems=null;
	Context mContext= null;
	LayoutInflater mInflater;
	private int mSelected = -1;
	private RoomMenuListener mListener;
	
	public RoomMenuAdapter(Context context, RoomMenuListener listener,  List<Room> items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);
		mSelected = -1;
		mListener = listener;
	} 
	
	@Override
	public int getCount() {
		if(mItems == null)
			return 0;
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		if(mItems == null || mItems.size() == 0)
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
		TextView tvRoom = (TextView) view.findViewById(android.R.id.text1);
		tvRoom.setHeight(80);
		//tvRoom.setText(String.valueOf(item.getId()));
		tvRoom.setText(item.getName());
		
		tvRoom.setWidth(200);
		return view;
	}
	
	private View newView(){
		View view = mInflater.inflate(android.R.layout.simple_list_item_single_choice, null);		
		return view;
	}

	public int getSelected() {
		return mSelected;
	}

	public void setSelected(int mSelected) {
		this.mSelected = mSelected;
		mListener.onRoomSelected(this.mSelected);
	}
	
	
}
