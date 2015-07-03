package com.HomeCenter2.ui.mainS;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogConfigDevice;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.DialogRoomMenu;
import com.HomeCenter2.ui.RoomMenuListener;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.adapter.MyDevicesAdapter;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.mainscreen.DetailDeviceScreen;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyDevicesScreen extends RADialerMainScreenAbstract implements
		OnItemLongClickListener, XMLListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, StatusListener, RoomMenuListener,
		View.OnClickListener, onCheckChangedListener {

	private static final String TAG = "MyDevicesScreen";
	ListView mLVDevices;
	MyDevicesAdapter mDeviceAdapter;
	ArrayList<Device> items = null;
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

	public MyDevicesScreen(int title, String tag, SlidingBaseActivity context) {
		super(MyDevicesScreen.class, title, tag, context);
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
		View view = (View) inflater.inflate(R.layout.my_device_screen,
				container, false);

		mLVDevices = (ListView) view.findViewById(R.id.lvDevice);
		mDeviceAdapter = new MyDevicesAdapter(mContext, this, null);
		mLVDevices.setAdapter(mDeviceAdapter);
		mLVDevices.setOnItemClickListener(this);
		mLVDevices.setOnItemLongClickListener(this);

		rltRoom = (RelativeLayout) view.findViewById(R.id.rltDeviceItem);
		rltRoom.setVisibility(View.VISIBLE);

		mImgRoomIcon = (ImageView) view.findViewById(R.id.imgIcon);

		mImgRoomOn = (ScheduleImageView) view.findViewById(R.id.imgOn);
		mImgRoomOn.setOnClickListener(this);
		mImgRoomOn.setSrcCheched(R.drawable.btn_on_icon);
		mImgRoomOn.setSrcNonChecked(R.drawable.btn_on_icon);
		mImgRoomOn.setOnCheckChangedListener(this);

		mImgRoomOff = (ScheduleImageView) view.findViewById(R.id.imgOff);
		mImgRoomOff.setOnClickListener(this);
		mImgRoomOff.setSrcCheched(R.drawable.btn_off_icon);
		mImgRoomOff.setSrcNonChecked(R.drawable.btn_off_icon);
		mImgRoomOff.setOnCheckChangedListener(this);

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
		inflater.inflate(R.menu.my_device_menu, menu);
		mMenu = menu;
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
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		});
		mTxtRoomName.setText(room.getName());
		mImgRoomIcon.setImageResource(room.getIcon());

		List<Device> devices = room.getDevices();
		if (items == null) {
			items = new ArrayList<Device>();
		} else {
			items.clear();
			items = new ArrayList<Device>();
		}
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
				items.add(item);
			} else if (item instanceof RollerShutter) {
				RollerShutter itemTemp = (RollerShutter) item;
				if (itemTemp.getRoller() > 0) {
					isTurnOnAll = false;
				}
				items.add(item);
			} else if (item instanceof DoorLock) {
				DoorLock itemTemp = (DoorLock) item;
				if (itemTemp.isState()) {
					isTurnOnAll = false;
				}
				items.add(item);
			}
		}
		int first = mLVDevices.getFirstVisiblePosition();
		mDeviceAdapter.onChangeData(items);
		mDeviceAdapter.notifyDataSetChanged();
		mLVDevices.setSelection(first);

		if (isTurnOnAll) {
			mImgRoomOn.setOnCheckChangedListener(null);
			mImgRoomOn.setChecked(isTurnOnAll);
			mImgRoomOn.setOnCheckChangedListener(this);

			mImgRoomOff.setOnCheckChangedListener(null);
			mImgRoomOff.setChecked(!isTurnOnAll);
			mImgRoomOff.setOnCheckChangedListener(this);
		} else {
			mImgRoomOn.setOnCheckChangedListener(null);
			mImgRoomOn.setChecked(!isTurnOnAll);
			mImgRoomOn.setOnCheckChangedListener(this);

			mImgRoomOff.setOnCheckChangedListener(null);
			mImgRoomOff.setChecked(isTurnOnAll);
			mImgRoomOff.setOnCheckChangedListener(this);
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
		Device device = (Device) mDeviceAdapter.getItem(position);
		if (device instanceof DoorLock || device instanceof RollerShutter) {
		} else {
			showDetailDevice(device);
		}
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
		/*
		 * Device device = (Device) mDeviceAdapter.getItem(position); if (device
		 * instanceof DoorLock || device instanceof RollerShutter) { } else {
		 * showDetailDevice(device); }
		 */
	}

	private void showDetailDevice(Device device) {
		Bundle bundle = new Bundle();

		bundle.putSerializable(configManager.DEVICE_BUNDLE, device);
		bundle.putBoolean(configManager.IS_DEVICE_BUNDLE, true);

		DetailDeviceScreen fragment = DetailDeviceScreen
				.initializeDetailDeviceScreen(bundle, -1,
						(SlidingBaseActivity) mContext);
		HomeCenter2Activity activity = (HomeCenter2Activity) mContext;
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);
	}

	@Override
	public void changeStatusDevice() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "changeStatusDevice");
				if (isClicked) {
					isClicked = false;
				} else {
					refreshList();
				}

			}
		});
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {

	}

	@Override
	public void onRoomSelected(int selected) {
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
		switch (v.getId()) {
		case R.id.imgOn:
			setOnOffAll(true);
			((ScheduleImageView) v).toggle();
			break;
		case R.id.imgOff:
			setOnOffAll(false);
			((ScheduleImageView) v).toggle();
			break;
		case R.id.txtTitle:
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_ROOM_MENU);
			break;
		}
	}

	private void onOffRoom(Room room, boolean isChecked) {
		Log.d(TAG, "onOffRoom: roomId: " + room.getId() + ", checked: "
				+ isChecked);

		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, room.getId());
		bundle.putInt(configManager.DEVICE_ID, 0);
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_DEVICE_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
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
		case R.id.imgOn:
			onOffRoom(mRoomCurrent, true);
			break;
		case R.id.imgOff:
			onOffRoom(mRoomCurrent, false);
			break;

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
			Device device = items.get(position - 1);
			if (device instanceof LampRoot) {
				LampRoot lamp = (LampRoot) device;
				onOffLampDevice(lamp, true, position - 1);
			}
			break;

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

			Device deviceOff = items.get(position - 10 - 1);
			if (deviceOff instanceof LampRoot) {
				LampRoot lamp = (LampRoot) deviceOff;
				onOffLampDevice(lamp, false, position - 10 - 1);
			}
			break;
		case 0:
			onOffRoom(mRoomCurrent, true);
			break;
		case 21:
			onOffRoom(mRoomCurrent, false);
		default:

			break;
		}
	}

	public void setOnOffAll(boolean isOn) {
		setClicked(true);
		List<Device> devices = mRoomCurrent.getDevices();
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

}