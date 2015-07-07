package com.HomeCenter2.ui.mainS;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Control;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogConfigDevice;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.DialogRoomMenu;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.adapter.MyDevicesAdapter;
import com.HomeCenter2.ui.adapter.RoomManagerAdapter;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class RoomManagerScreenFragment extends RADialerMainScreenAbstract implements
		OnItemLongClickListener, XMLListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, StatusListener,
		View.OnClickListener, onCheckChangedListener {

	private static final String TAG = "RoomManagerScreenFragment";
	ListView mLVDevices;
	MyDevicesAdapter mDeviceAdapter;
	HomeCenterUIEngine mUiEngine = null;
	private boolean isClicked = false;

	House mHouse = null;
	RoomMenuAdapter mRoomDropDownAdapter;
	protected Handler mHandler;
	LayoutInflater mInflater;
	private Device mDeviceFocused = null;

	ScheduleImageView mImgRoomOn, mImgRoomOff;
	TextView mTxtRoomName;
	ImageView mImgRoomIcon;
	Room mRoomCurrent;
	View mView;
	RelativeLayout rltRoom;
	Menu mMenu;
	public boolean mIsSpeaking = false;
	private ViewPager roomPager;
	private RoomManagerAdapter roomAdapter;

	public RoomManagerScreenFragment(int title, String tag, SlidingBaseActivity context) {
		super(RoomManagerScreenFragment.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		View view = (View) inflater.inflate(R.layout.my_room_manager_screen,
				container, false);
		
		Log.e(TAG, "onCreateContentView");
		
		initUI(view);
		initData();
		
		return view;
	}
	
	private void initUI(View view){
		roomPager = (ViewPager)view.findViewById(R.id.room_pager);
		roomPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initData(){
		roomAdapter = new RoomManagerAdapter(getActivity(), getFragmentManager(), mHouse.getRooms() );
		roomPager.setAdapter(roomAdapter);
	}
	
	public void updateRoomIndex(int index){
		roomPager.setCurrentItem(index);
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
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				return true;
			case R.id.speak_Menu:
				mIsSpeaking = true;
				resetMenu();
				mContext.controlSpeech();
				return true;

			case R.id.stop_speak_Menu:
				mIsSpeaking = false;
				resetMenu();
				return true;
			}
		}
		return false;
	}

	private void resetMenu() {
		if (mMenu.size() >= 2) {
			MenuItem itemSpeak = mMenu.findItem(R.id.speak_Menu);
			MenuItem itemStop = mMenu.findItem(R.id.stop_speak_Menu);
			if (mIsSpeaking) {
				itemSpeak.setVisible(false);
				itemStop.setVisible(true);
			} else {
				itemSpeak.setVisible(true);
				itemStop.setVisible(false);
			}
		}
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

		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		/*
		 * mDeviceFocused = (Device) mDeviceAdapter.getItem(position);
		 * DialogFragmentWrapper.showDialog(getFragmentManager(), this,
		 * configManager.DIALOG_CHANGE_NAME);
		 */
		return true;
	}

	@Override
	public void savedXML() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
	}

	@Override
	public void onPageSelected() {
		Log.d(TAG, "onPageSelected");

	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtTitle:
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_ROOM_MENU);
			break;
		}
	}

	public void saveConfig(Bundle bundle) {
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_DEVICE_ADDRESS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		switch (view.getId()) {

		}
	}

	int position = 0;

	public void onOffBySpeak(int position) {
		Toast.makeText(mContext, "Speak : " + position, Toast.LENGTH_SHORT)
				.show();
		switch (position) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		default:

			break;
		}
	}

	public void setOnOffAll(boolean isOn) {
		setClicked(true);
		List<Control> devices = mRoomCurrent.getControls();
		ArrayList<Device> items = new ArrayList<Device>();
		Device item = null;
		int size = devices.size();
		boolean isTurnOnAll = true;
		for (int i = 0; i < size; i++) {
			item = devices.get(i);
			if (item instanceof LampRoot) {
				LampRoot itemTemp = (LampRoot) item;
				itemTemp.setState(isOn);
				items.add(itemTemp);
			} else if (item instanceof RollerShutter) {
				RollerShutter itemTemp = (RollerShutter) item;
				itemTemp.setActiveTemp(isOn);
				items.add(itemTemp);
			} else if (item instanceof DoorLock) {
				DoorLock itemTemp = (DoorLock) item;
				itemTemp.setState(isOn);
				items.add(itemTemp);
			}
		}
		int first = mLVDevices.getFirstVisiblePosition();
		mDeviceAdapter.onChangeData(items);
		mDeviceAdapter.notifyDataSetChanged();
		mLVDevices.setSelection(first);
	}

	// //////////////////////Speak///////////////////////////
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

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeStatusDevice() {
		// TODO Auto-generated method stub
		
	}

}