package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract.StatusUpdates;
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
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.KeyDoorLock;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.StatusRelationship;
import com.HomeCenter2.ui.ScheduleImageView;

public class SensorAdapter extends BaseAdapter implements OnClickListener {

	private static final String TAG = "SensorAdapter";
	List<Sensor> mItems = null;
	StatusRelationship mSR;
	Context mContext = null;
	LayoutInflater mInflater;

	SensorAdapter mAdapter = null;

	public interface ClickSensorCallback {
		public void clickPosCallback(int pos);
	}

	private ClickSensorCallback callback;

	public SensorAdapter(Context context, List<Sensor> sensor,
			StatusRelationship sr) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mItems = sensor;
		mSR = sr;
		mAdapter = this;
	}

	public void setClickCallback(ClickSensorCallback callback) {
		Log.e(TAG, "setClickCallback");
		this.callback = callback;
	}

	public void setClickPos(int pos) {
		switch (pos) {
		case configManager.TEMPERATURE_1:
			mSR.setTemp1(!mSR.isTemp1());
			break;
		case configManager.TEMPERATURE_2:
			mSR.setTemp2(!mSR.isTemp2());
			break;
		case configManager.LIGHT_1:
			mSR.setLight1(!mSR.isLight1());
			break;
		case configManager.LIGHT_2:
			mSR.setLight2(!mSR.isLight2());
			break;
		case configManager.DOOR_STATUS_1:
			mSR.setDoor1(!mSR.isDoor1());
			break;
		case configManager.DOOR_STATUS_2:
			mSR.setDoor2(!mSR.isDoor2());
			break;
		case configManager.DOOR_STATUS_3:
			mSR.setDoor3(!mSR.isDoor3());
			break;
		case configManager.DOOR_STATUS_4:
			mSR.setDoor4(!mSR.isDoor4());
			break;
		case configManager.MOTION_1:
			mSR.setMotion1(!mSR.isMotion1());
			break;
		case configManager.MOTION_2:
			mSR.setMotion2(!mSR.isMotion2());
			break;
		case configManager.MOTION_3:
			mSR.setMotion3(!mSR.isMotion3());
			break;
		case configManager.MOTION_4:
			mSR.setMotion4(!mSR.isMotion4());
			break;
		case configManager.SMOKE_1:
			mSR.setSmoke1(!mSR.isSmoke1());
			break;
		case configManager.SMOKE_2:
			mSR.setSmoke2(!mSR.isSmoke2());
			break;
		default:
			break;
		}

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
		Sensor item = mItems.get(position);
		if (item == null) {
			return view;
		}

		refreshActiveButton(view, viewHolder.imgIcon, position);
		viewHolder.imgIcon.setImageResource(item.getIcon());
		viewHolder.txtTitle.setText(item.getName());
		
		final int pos = position;

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setClickPos(pos);
				callback.clickPosCallback(pos);				
				notifyDataSetChanged();
			}
		});
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
		// Switch deviceOn;

	}

	public void onChangeData(ArrayList<Sensor> items) {
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

	public void refreshActiveButton(View view, ImageView button, int pos) {
		boolean isActive = true;
		switch (pos) {
		case configManager.TEMPERATURE_1:
			isActive = mSR.isTemp1();
			break;
		case configManager.TEMPERATURE_2:
			isActive = mSR.isTemp2();
			break;
		case configManager.LIGHT_1:
			isActive = mSR.isLight1();
			break;
		case configManager.LIGHT_2:
			isActive = mSR.isLight2();
			break;
		case configManager.DOOR_STATUS_1:
			isActive = mSR.isDoor1();
			break;
		case configManager.DOOR_STATUS_2:
			isActive = mSR.isDoor2();
			break;
		case configManager.DOOR_STATUS_3:
			isActive = mSR.isDoor3();
			break;
		case configManager.DOOR_STATUS_4:
			isActive = mSR.isDoor4();
			break;
		case configManager.MOTION_1:
			isActive = mSR.isMotion1();
			break;
		case configManager.MOTION_2:
			isActive = mSR.isMotion2();
			break;
		case configManager.MOTION_3:
			isActive = mSR.isMotion3();
			break;
		case configManager.MOTION_4:
			isActive = mSR.isMotion4();
			break;
		case configManager.SMOKE_1:
			isActive = mSR.isSmoke1();
			break;
		case configManager.SMOKE_2:
			isActive = mSR.isSmoke2();
			break;
		default:
			break;
		}

		if (isActive == true) {
			view.setBackgroundResource(R.drawable.boder_key_selected);			
		} else {
			view.setBackgroundResource(R.drawable.boder_key_normal);			
		}
	}
}
