package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Switch;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.KeyDoorLock;
import com.HomeCenter2.ui.DayLayout;
import com.HomeCenter2.ui.ScheduleImageView;

public class KeyDoorLockAdapter extends BaseAdapter implements OnClickListener {

	private static final String TAG = "KeyDoorLockAdapter";
	List<KeyDoorLock> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	DoorLock mDevice;
	KeyDoorLockAdapter mAdapter = null;
	
	public KeyDoorLockAdapter(Context context, DoorLock device) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mDevice = device;
		mItems = mDevice.getKeys();
		mAdapter = this;
		
		Log.e(TAG, "KeyDoorLockAdapter "+mItems.size());
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
		KeyDoorLock item = mItems.get(position);
		if (item == null) {
			return view;
		}
		Bundle bundle = (Bundle)viewHolder.imgIcon.getTag();
		bundle.putBoolean(configManager.ACTIVE_KEY, item.isState());
		bundle.putInt(configManager.POSITON_KEY, position);		
		
		refreshActiveButton(viewHolder.imgIcon, item.isState());
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getNameKey());
		return view;
	}

	private View newView(int position) {
		View view = mInflater.inflate(R.layout.key_doorlock_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtTitle.setSelected(true);
		Bundle bundle = new Bundle();
		bundle.putBoolean(configManager.ACTIVE_KEY, false);
		bundle.putInt(configManager.POSITON_KEY, position);
		viewHolder.imgIcon.setTag(bundle);
				
		view.setTag(viewHolder);
		return view;
	}

	private class ViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
		//Switch deviceOn;
		
	}

	public void onChangeData(ArrayList<KeyDoorLock> items) {
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

	private void setModeDoorLock(KeyDoorLock device, boolean isChecked,
			int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, mDevice.getRoomId());
		bundle.putString(configManager.MODE_STATUS,
				(isChecked ? configManager.MODE_SET_ID
						: configManager.MODE_DELETE_ID));
		bundle.putInt(configManager.DEVICE_ID, position);

		Log.e(TAG, "setModeDoorLock::room id: " + mDevice.getRoomId()
				+ " , devId: " + mDevice.getDeviceId() + " , isChecked: "
				+ isChecked + " positon: " + position);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}
	
	public void refreshActiveButton(ImageView button, boolean isActive){		
		Bundle bundle = (Bundle) button.getTag();
		bundle.putBoolean(configManager.ACTIVE_KEY, isActive);		
		
		button.setTag(bundle);		
		if(isActive == true){
			button.setBackgroundResource(R.drawable.key_doorlock_on);			
		}else{
			button.setBackgroundResource(R.drawable.key_doorlock_off);						
		}
	}
}

