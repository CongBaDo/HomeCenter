package com.HomeCenter2.ui.mainS;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorStatus;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Light;
import com.HomeCenter2.house.Motion;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Smoke;
import com.HomeCenter2.house.Temperature;
import com.HomeCenter2.ui.DialogConfigDevice;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.DialogRoomMenu;
import com.HomeCenter2.ui.RoomMenuListener;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.MySensorsAdapter;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MySensorsScreen extends RADialerMainScreenAbstract implements
		OnItemLongClickListener, XMLListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, StatusListener, RoomMenuListener,
		View.OnClickListener {

	private static final String TAG = "MySensorsScreen";
	ListView mLVDevices;
	MySensorsAdapter mSensorAdapter;
	House mHouse = null;
	HomeCenterUIEngine mUiEngine = null;
	RoomMenuAdapter mRoomDropDownAdapter;
	protected Handler mHandler;
	LayoutInflater mInflater;
	private Device mDeviceFocused = null;
	Room mRoomCurrent;

	TextView mTxtRoomName;
	ImageView mImgRoomIcon;
	View mView;
	RelativeLayout rltRoom;

	public MySensorsScreen(int title, String tag, SlidingBaseActivity context) {
		super(MySensorsScreen.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); */
		mUiEngine = RegisterService.getHomeCenterUIEngine();
		if (mUiEngine == null) {
			((HomeCenter2Activity) mContext).doExit(mContext);
		}
		mUiEngine.addStatusObserver(this);
		mUiEngine.addXMLObserver(this);
		mHouse = mUiEngine.getHouse();

		mInflater = getLayoutInflater(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mUiEngine != null) {
			mUiEngine.removeStatusObserver(this);
			mUiEngine.removeXMLObserver(this);
		}

	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.my_device_screen,
				container, false);
		mLVDevices = (ListView) view.findViewById(R.id.lvDevice);
		mSensorAdapter = new MySensorsAdapter(mContext, null);
		mLVDevices.setAdapter(mSensorAdapter);
		mLVDevices.setOnItemLongClickListener(this);

		rltRoom = (RelativeLayout) view.findViewById(R.id.rltDeviceItem);
		rltRoom.setVisibility(View.VISIBLE);

		mImgRoomIcon = (ImageView) view.findViewById(R.id.imgIcon);
		ScheduleImageView imgRoomOn = (ScheduleImageView) view
				.findViewById(R.id.imgOn);
		imgRoomOn.setVisibility(View.GONE);
		
		ScheduleImageView imgRoomOff = (ScheduleImageView) view
				.findViewById(R.id.imgOff);
		imgRoomOff.setVisibility(View.GONE);

		mTxtRoomName = (TextView) view.findViewById(R.id.txtTitle);
		mTxtRoomName.setSelected(true);
		mTxtRoomName.setOnClickListener(this);

		mView = view.findViewById(R.id.viewItem);
		mView.setVisibility(View.VISIBLE);

		return view;
	}

	@Override
	public void onScreenSlidingCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setTitle(mTitleId);
		mActionBarV7.setDisplayShowTitleEnabled(true);

		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				break;
			}
		}

		return true;
	}

	private boolean refreshList() {
		if (mRoomDropDownAdapter == null)
			return false;
		mRoomCurrent = (Room) mRoomDropDownAdapter.getItem(mUiEngine
				.getRoomCurrentIndex());
		return refreshListByRoom(mRoomCurrent);
	}

	private boolean refreshListByRoom(Room room) {
		if (room == null) {
			return false;
		}
		mTxtRoomName.setText(room.getName());
		mImgRoomIcon.setImageResource(room.getIcon());
		List<Device> devices = room.getDevices();
		ArrayList<Device> items = new ArrayList<Device>();
		Device item = null;
		int size = devices.size();
		for (int i = 0; i < size; i++) {
			item = devices.get(i);
			if (item instanceof Temperature || item instanceof Light
					|| item instanceof Motion || item instanceof DoorStatus
					|| item instanceof Smoke) {
				items.add(item);
			}
		}		
		int first = mLVDevices.getFirstVisiblePosition();
		mSensorAdapter.onChangeData(items);
		mSensorAdapter.notifyDataSetChanged();
		mLVDevices.setSelection(first);
		//mLVDevices.setAdapter(mSensorAdapter);
		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		mDeviceFocused = (Device) mSensorAdapter.getItem(position);
		DialogFragmentWrapper.showDialog(getFragmentManager(), this,
				configManager.DIALOG_CHANGE_NAME);
		return false;
	}

	@Override
	public void savedXML() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "changeStatusDevice");
				refreshList();
			}
		});
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_CHANGE_NAME:
			return showChangeNameDialog();
		case configManager.DIALOG_ROOM_MENU:
			return DialogRoomMenu.showContentDeviceDialog(mContext, mInflater,
					mRoomDropDownAdapter);
		default:
			break;
		}
		return null;
	}

	private Dialog showChangeNameDialog() {
		Room room = (Room) mRoomDropDownAdapter.getItem(mRoomDropDownAdapter
				.getSelected());
		return DialogConfigDevice.showContentDeviceDialog(this, mContext,
				mInflater, room, mDeviceFocused);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void changeStatusDevice() {
		Log.d(TAG, "changeStatusDevice");
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				refreshList();

			}
		});
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {

	}

	@Override
	public void onRoomSelected(int selected) {
		Log.d(TAG, "changeStatusDevice");
		mContext.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				refreshList();
			}
		});

	}

	@Override
	public void onPageSelected() {
		Log.d(TAG, "onPageSelected");

		if (mRoomDropDownAdapter == null) {
			List<Room> rooms = null;
			if (mHouse != null) {
				rooms = mHouse.getRooms();
				int size = rooms.size();
				if (rooms != null && size > 0) {
					mRoomDropDownAdapter = new RoomMenuAdapter(mContext, this,
							rooms);
				}
			}
		}
		if (mRoomDropDownAdapter != null) {
			mRoomDropDownAdapter.setSelected(mUiEngine.getRoomCurrentIndex());
			refreshList();
		}

	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.txtTitle) {
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_ROOM_MENU);
		}

	}
}
