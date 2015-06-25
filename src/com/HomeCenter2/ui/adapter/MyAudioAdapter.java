package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.AudioHC;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.CustomSpinner;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.mainS.MyAudioSystemScreen;

public class MyAudioAdapter extends BaseAdapter implements
		View.OnClickListener, onCheckChangedListener {

	private static final String TAG = "MyDevicesAdapter";
	List<AudioHC> mItems = null;
	Context mContext = null;
	LayoutInflater mInflater;
	String[] mInput;
	ArrayAdapter<String> adp1;
	MyAudioAdapter mAdapter = null;
	MyAudioSystemScreen mScreen = null;

	public MyAudioAdapter(MyAudioSystemScreen screen,Context context, List<AudioHC> items) {
		super();
		mContext = context;
		mAdapter = this;
		mScreen = screen;
		mItems = items;
		mInflater = LayoutInflater.from(context);
		mInput = mContext.getResources().getStringArray(R.array.audioTemp);
		int size = mItems.size();
		mInput = new String[size];
		AudioHC item=  null;
		Room room = null;
		for(int i = 0; i< size ; i++){
			item = items.get(i);
			room = item.getRoom();
			mInput[i]= room.getName();
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

		AudioHC item = mItems.get(position);
		if (item == null) {
			return view;
		}
		if (view == null) {
			view = newView(item, position);

		}

		MyAudioItem viewHolder = (MyAudioItem) view.getTag();

		int pos = getSelectionByValue(item.getInput());
		if (pos < 0) {
			item.setState(false);
		} else {
			item.setState(true);
			viewHolder.spinInput.setSelection(pos);
		}
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.POSITON_KEY, position);		
		viewHolder.spinInput.setTag(position);

		viewHolder.imgIcon.setImageResource(item.getIcon());
		Room room = item.getRoom();
		viewHolder.txtTitle.setText(room.getName());

		viewHolder.deviceOn.setTag(viewHolder.spinInput);
		
		viewHolder.deviceOn.setOnCheckChangedListener(null);
		viewHolder.deviceOn.setChecked(item.isState());
		viewHolder.deviceOn.setOnCheckChangedListener(this);
		return view;
	}

	private View newView(AudioHC item, int position) {

		View view = mInflater.inflate(R.layout.my_audio_item, null);
		MyAudioItem viewHolder = new MyAudioItem();
		viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
		viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		viewHolder.txtTitle.setSelected(true);
		viewHolder.deviceOn = (ScheduleImageView) view.findViewById(R.id.imgOn);
		viewHolder.deviceOn.setOnClickListener(this);
		viewHolder.deviceOn.setSrcCheched(R.drawable.ic_turn_on);
		viewHolder.deviceOn.setSrcNonChecked(R.drawable.ic_turn_off);
		viewHolder.deviceOn.setOnCheckChangedListener(this);

		viewHolder.spinInput = (CustomSpinner) view.findViewById(R.id.spnRoom);
	/*
		final Spinner spinner = viewHolder.spinInput;
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				int position = Integer.valueOf(spinner.getTag().toString());
				AudioHC audio = mItems.get(position);				
				onOffAudio(audio, audio.isState(), spinner.getSelectedItemPosition());
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});*/
		// viewHolder.spinInput.setTag(item);
		adp1 = new ArrayAdapter<String>(mContext,
				R.layout.textview_spinner, mInput);
//		adp1 = new ArrayAdapter<String>(mContext,
//				android.R.layout.simple_list_item_1, mInput);
		viewHolder.spinInput.setAdapter(adp1);

		view.setTag(viewHolder);

		return view;
	}

	public void onChangeData(List<AudioHC> items) {
		if (mItems != null) {
			mItems.clear();
		}
		mItems = items;
		notifyDataSetChanged();
	}

	private int getSelectionByValue(int value) {
		int size = mInput.length;
		if (value <= 0 || size <= 0)
			return -1;
		return value - 1;
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

	private void onOffAudio(AudioHC device, boolean isChecked, int position) {
		Log.d(TAG, "onOffAudio: position in adapter: " + position);
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoom().getId());
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);
		if (isChecked) {
			bundle.putInt(configManager.DEVICE_ID, position + 1);
		} else {
			bundle.putInt(configManager.DEVICE_ID, 0);
		}
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_AUDIO;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {		
		Spinner spinner = (Spinner) view.getTag();
		int position = Integer.valueOf(spinner.getTag().toString());
		AudioHC audio = mItems.get(position);		
		audio.setState(isChecked);				
		Log.d(TAG, "onCheckChanged: audio: " + position);
		onOffAudio(audio, isChecked, spinner.getSelectedItemPosition());
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if(uiEngine!= null && mScreen!= null){
			int[] array = mScreen.getAudioArray();			
			if(array!= null){
				Log.d(TAG, "onCheckChanged: audio: " + position  + "size: " + array.length);
				array[position]= (isChecked ? 1:0);
			}
			uiEngine.notifyAudioObserver(array);
		}
	}

}
