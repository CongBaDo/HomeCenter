package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.MyImageView;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;

public class MyParameterRoomAdapter extends BaseAdapter implements
		OnClickListener, onCheckChangedListener  {

	private static final String TAG = "MyParameterRoomAdapter";
	List<Room> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	
	public MyParameterRoomAdapter(Context context, List<Room> items) {
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
		viewHolder.deviceOn.setTag(position);

		Room item = mItems.get(position);
		if (item == null) {
			return view;
		}
		
		if(position< configManager.MAX_ROOM_IN_AREA){
			viewHolder.deviceOn.setVisibility(View.VISIBLE);
			viewHolder.txtOther.setVisibility(View.VISIBLE);
			viewHolder.txtLockId.setVisibility(View.VISIBLE);
			boolean isTurnAll = isTurnOnAll(item);
			viewHolder.deviceOn.setOnCheckChangedListener(null);
			viewHolder.deviceOn.setChecked(isTurnAll);
			viewHolder.deviceOn.setOnCheckChangedListener(this);			
			viewHolder.txtLockId.setText(item.getLockId());
			
		}else{
			viewHolder.deviceOn.setVisibility(View.INVISIBLE);
			viewHolder.txtOther.setVisibility(View.GONE);
			viewHolder.txtLockId.setVisibility(View.INVISIBLE);
		}
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getName());
		viewHolder.txtId.setText(item.getIdSever());
		return view;
	}

	private View newView(int position) {
		View view = mInflater.inflate(R.layout.parameter_config_room_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtTitle.setSelected(true);
		viewHolder.txtId =  (TextView) view.findViewById(R.id.txtRoomId);
		viewHolder.txtLockId = (TextView) view.findViewById(R.id.txtLockId);
		viewHolder.txtOther = (TextView) view.findViewById(R.id.txtOther);
		
		viewHolder.deviceOn = (ScheduleImageView) view.findViewById(R.id.imgOn);
		viewHolder.deviceOn.setOnClickListener(this);
		viewHolder.deviceOn.setSrcCheched(R.drawable.ic_turn_on);
		viewHolder.deviceOn.setSrcNonChecked(R.drawable.ic_turn_off);
		viewHolder.deviceOn.setOnCheckChangedListener(this);

		view.setTag(viewHolder);
		return view;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
		TextView txtId;
		TextView txtOther;
		TextView txtLockId;		
		ScheduleImageView deviceOn;		
	}

	public void onChangeData(ArrayList<Room> items) {
		if (mItems != null) {
			mItems.clear();
		}
		mItems = items;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		Room device = mItems.get(position);
		switch (v.getId()) {
		case R.id.imgOn:
			ScheduleImageView viewItem= (ScheduleImageView) v;
			viewItem.toggle();
			break;
		default:
			break;
		}
	}

	private void onOffRoom(Room room, boolean isChecked) {	
		Log.d(TAG, "onOffDevice: checked: " + isChecked);

		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, room.getId());
		bundle.putInt(configManager.DEVICE_ID, 0);
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);

		Log.e(TAG, "onCheckedChanged::room id: " + room.getId() + " , isOn: "
				+ isChecked);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what= HCRequest.REQUEST_SET_DEVICE_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}
	

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		int position = Integer.parseInt(view.getTag().toString());
		Room device = mItems.get(position);
		if(device!=null){
		onOffRoom(device, isChecked);
		}
	}

	
	private boolean isTurnOnAll(Room room){
		List<Device> devices = room.getDevices();		
		Device item = null;
		int size = devices.size();
		boolean isTurnOnAll = true;
		for (int i = 0; i < size; i++) {
			item = devices.get(i);
			if (item instanceof LampRoot) {
				LampRoot itemTemp = (LampRoot) item;
				if (!itemTemp.isState()) {
					isTurnOnAll = false;
				}
				
			} else if (item instanceof RollerShutter) {
				RollerShutter itemTemp = (RollerShutter) item;
				if (itemTemp.getRoller() > 0) {
					isTurnOnAll = false;
				}
				
			} else if (item instanceof DoorLock) {
				DoorLock itemTemp = (DoorLock) item;
				if (itemTemp.isState()) {
					isTurnOnAll = false;
				}				
			}
		}
		return isTurnOnAll;
	}

}
