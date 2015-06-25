package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
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
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.ui.MyImageView;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.mainS.MyDevicesScreen;

public class MyDevicesAdapter extends BaseAdapter implements OnClickListener,
		onCheckChangedListener {

	private static final String TAG = "MyDevicesAdapter";
	ArrayList<Device> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	MyDevicesAdapter mAdapter;
	MyDevicesScreen mScreen;

	public MyDevicesAdapter(Context context, MyDevicesScreen screen, ArrayList<Device> items) {
		super();
		mContext = context;
		mItems = items;
		mInflater = LayoutInflater.from(context);
		mAdapter = this;
		mScreen = screen;
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

		Device item = mItems.get(position);
		if (item == null) {
			return view;
		}
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getName());
		
		//viewHolder.txtTitle.setTextColor(199999);
		if (item instanceof LampRoot) {
			LampRoot itemTemp = (LampRoot) item;
			if (itemTemp.isState()) {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(true);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			} else {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(false);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			}
		} else if (item instanceof RollerShutter) {
			RollerShutter itemTemp = (RollerShutter) item;
			if (itemTemp.getRoller()>0) {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(true);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			} else {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(false);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			}
		} else if (item instanceof DoorLock) {
			DoorLock itemTemp = (DoorLock) item;
			if (itemTemp.isState()) {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(true);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			} else {
				viewHolder.deviceOn.setOnCheckChangedListener(null);
				viewHolder.deviceOn.setChecked(false);
				viewHolder.deviceOn.setOnCheckChangedListener(this);
			}
		}

		return view;
	}

	private View newView(int position) {
		View view =null;
		
		view = mInflater.inflate(R.layout.my_device_item, null);
				
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtTitle.setSelected(true);		

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
		ScheduleImageView deviceOn;
	}

	public void onChangeData(ArrayList<Device> items) {
		if (mItems != null) {
			mItems.clear();
		}
		mItems = items;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgOn:
			((ScheduleImageView) v).toggle();
			break;
		default:
			break;
		}
	}

	private void onOffLampDevice(LampRoot device, boolean isChecked,
			int position) {
		Log.d(TAG, "onOffLampDevice: position in adapter: " + position);
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoomId());
		bundle.putInt(configManager.DEVICE_ID, device.getId());
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_DEVICE_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	private void setModeDoorLock(Device device, boolean isChecked, int position) {
		Log.d(TAG, "setModeDoorLock: " + position);
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoomId());
		bundle.putString(
				configManager.MODE_STATUS,
				(isChecked ? configManager.MODE_OPEN : configManager.MODE_CLOSE));
		bundle.putInt(configManager.DEVICE_ID, 1);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		int position = Integer.parseInt(view.getTag().toString());
		Device device = mItems.get(position);
		mScreen.setClicked(true);
		if (isChecked) {
			if (device instanceof LampRoot) {
				LampRoot lamp = ((LampRoot) device); 
				lamp.setState(isChecked);
				onOffLampDevice(lamp, true, position + 1);			
			} else if (device instanceof DoorLock) {
				DoorLock door = (DoorLock) device;
				door.setState(isChecked);
				setModeDoorLock(device, true, position + 1);
			}
		} else {
			if (device instanceof LampRoot) {
				LampRoot lamp = ((LampRoot) device); 
				lamp.setState(isChecked);
				onOffLampDevice(lamp, false, position + 1);
			} else if (device instanceof DoorLock){//nganguyen
				DoorLock door = (DoorLock) device;
				door.setState(isChecked);
				setModeDoorLock(device, false, position + 1);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

}
