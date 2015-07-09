package com.HomeCenter2.ui.mainscreen;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.adapter.MyParameterRoomAdapter;
import com.HomeCenter2.ui.listener.LoginListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class ParameterConfigRoom extends RADialerMainScreenAbstract implements
		LoginListener, DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemLongClickListener, XMLListener, OnItemClickListener {

	public static final String TAG = "ParameterConfigRoom";

	public ParameterConfigRoom(int title, String tag,
			SlidingBaseActivity context) {
		super(ParameterConfigRoom.class, title, tag, context);
	}

	public static ParameterConfigRoom nInstance = null;
	List<Room> mRooms;
	Dialog mDialog = null;

	GridView mLVRooms;
	MyParameterRoomAdapter mRoomAdapter;

	public static ParameterConfigRoom initializeParameterConfigRoom(
			int titleId, String tag, SlidingBaseActivity mContext) {

		if (nInstance == null) {
			nInstance = new ParameterConfigRoom(titleId, tag, mContext);
		}
		return nInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.parameter_config_room, container,
				false);

		mLVRooms = (GridView) view.findViewById(R.id.lvRoom);
		mLVRooms.setOnItemLongClickListener(this);
		mLVRooms.setOnItemClickListener(this);
		mLVRooms.setNumColumns(3);

		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null) {
			return null;
		}
		uiEngine.addLoginObserver(this);
		uiEngine.addXMLObserver(this);

		refreshContentView();
		return view;

	}

	@Override
	public void onScreenSlidingCompleted() {
		// TODO Auto-generated method stub
		refreshContentView();
	}

	@Override
	public void loadHeader() {

	}

	@Override
	public void setContentMenu() {

	}

	private void refreshContentView() {

		RegisterService service = RegisterService.getService();
		if (service == null)
			return;
		HomeCenterUIEngine uiEngine = service.getUIEngine();
		if (uiEngine == null) {
			return;
		}

		mRooms = uiEngine.getAllRoom();

		int first = mLVRooms.getFirstVisiblePosition();

		ListAdapter ad = mLVRooms.getAdapter();
		if (ad != null) {
			mLVRooms.setAdapter(null);
			ad = null;
		}
		mRoomAdapter = new MyParameterRoomAdapter(mContext, mRooms);
		mLVRooms.setAdapter(mRoomAdapter);

		mLVRooms.setSelection(first);

	}

	@Override
	public void eventLogined(boolean isConnected) {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (ParameterConfigRoom.this.isVisible()) {
					refreshContentView();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null)
			return;
		uiEngine.removeLoginObserver(this);
		uiEngine.removeXMLObserver(this);

	}

	public void saveConfig() {

		Bundle bundle = new Bundle();

		StringBuffer strDeviceBuffer = new StringBuffer();
		String value = mRooms.get(9).getIdSever();
		if (TextUtils.isEmpty(value) == true) {
			strDeviceBuffer.append(configManager.ZERO);
		} else {
			strDeviceBuffer.append(value.trim());
		}
		strDeviceBuffer.append(configManager.COMMAS);
		for (int i = 0; i < configManager.MAX_ROOM_IN_AREA; i++) {
			value = mRooms.get(i).getIdSever();
			if (TextUtils.isEmpty(value) == true) {
				strDeviceBuffer.append(configManager.ZERO);
			} else {
				strDeviceBuffer.append(value.trim());
			}
			strDeviceBuffer.append(configManager.COMMAS);
		}
		value = mRooms.get(8).getIdSever();
		if (TextUtils.isEmpty(value) == true) {
			strDeviceBuffer.append(configManager.ZERO);
		} else {
			strDeviceBuffer.append(value.trim());
		}

		bundle.putString(configManager.SAVE_ROOMIDS, strDeviceBuffer.toString());

		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null) {
			return;
		}

		StringBuffer str = new StringBuffer();
		value = "";
		value = mRooms.get(9).getLockId();
		if (TextUtils.isEmpty(value) == true) {
			str.append(configManager.ZERO);
		} else {
			str.append(value.trim());
		}
		str.append(configManager.COMMAS);
		for (int i = 0; i < configManager.MAX_ROOM_IN_AREA; i++) {
			value = mRooms.get(i).getLockId();
			if (TextUtils.isEmpty(value) == true) {
				str.append(configManager.ZERO);
			} else {
				str.append(value.trim());
			}
			str.append(configManager.COMMAS);
		}

		value = mRooms.get(8).getLockId();
		if (TextUtils.isEmpty(value) == true) {
			str.append(configManager.ZERO);
		} else {
			str.append(value.trim());
		}

		bundle.putString(configManager.SAVE_LOCKIDS, str.toString());

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_ROOM_ADDRESS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);

	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {

		case configManager.DIALOG_CHANGE_ROOM_NAME:
			return showContentDeviceDialog();
		default:
			break;
		}
		return null;
	}

	public void showDialog(int action) {
		DialogFragmentWrapper.showDialog(getFragmentManager(), this, action);
	}

	/*
	 * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
	 * isChecked) { destroyTask(); Bundle bundle = new Bundle(); int roomId = 0;
	 * switch (buttonView.getId()) { case R.id.cbShowPassword: if (isChecked ==
	 * true) { mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT |
	 * InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); } else {
	 * mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT |
	 * InputType.TYPE_TEXT_VARIATION_PASSWORD); } return; case
	 * R.id.togbAllDeviceRoom01: roomId = 1; break; case
	 * R.id.togbAllDeviceRoom02: roomId = 2; break;
	 * 
	 * case R.id.togbAllDeviceRoom03: roomId = 3; break; case
	 * R.id.togbAllDeviceRoom04: roomId = 4; break;
	 * 
	 * case R.id.togbAllDeviceRoom05: roomId = 5; break; case
	 * R.id.togbAllDeviceRoom06: roomId = 6; break; case
	 * R.id.togbAllDeviceRoom07: roomId = 7; break;
	 * 
	 * case R.id.togbAllDeviceRoom08: roomId = 8; break;
	 * 
	 * default: break; } bundle.putInt("roomId", roomId);
	 * bundle.putString("devId", "00"); bundle.putBoolean("isSSOn", isChecked);
	 * Log.e(TAG, "onCheckedChanged::room id: " + roomId + " , isOn: " +
	 * isChecked); HCRequest hc = new HCRequest();
	 * hc.setRequestType(HCRequest.REQUEST_SET_DEVICE_STATUS); mNameRequestTask
	 * = new SocketRequestTask(this); if (mNameRequestTask != null) {
	 * mNameRequestTask.setRequest(hc); mNameRequestTask.setObject(bundle);
	 * mNameRequestTask.execute(hc); } }
	 */

	private Dialog showSetOnOffDeviceFail() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = new AlertDialog.Builder(mContext)
				.setTitle(R.string.warning)
				.setMessage(R.string.set_on_off_fail)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		return mDialog;
	}

	public void resultSetDevice(boolean isSetOnOffDevice) {
		if (isSetOnOffDevice == false) {
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_SET_ON_OFF_DEVICE_FAIL);
		} else {

		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		mSelected = position;
		showDialog(configManager.DIALOG_CHANGE_ROOM_NAME);
		return true;
	}

	LayoutInflater mInflater;
	EditText mRoomIdEdit, mLockIdEdit, mNameEdit;
	ImageView mRoomImage;
	int mSelected = 0;

	public Dialog showContentDeviceDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		Room roomSelected = (Room) mRoomAdapter.getItem(mSelected);
		View view = mInflater.inflate(R.layout.add_room_name_dialog, null);
		mRoomIdEdit = (EditText) view.findViewById(R.id.editRoomIdAddDevice);
		mRoomIdEdit.setText(String.valueOf(roomSelected.getIdSever()));

		mLockIdEdit = (EditText) view.findViewById(R.id.editLockIdAddDevice);
		mLockIdEdit.setText(roomSelected.getLockId());

		mNameEdit = (EditText) view.findViewById(R.id.editNameAddDevice);
		mNameEdit.setText(roomSelected.getName());

		mRoomImage = (ImageView) view.findViewById(R.id.imgRoom);
		mRoomImage.setImageResource(roomSelected.getIcon());

		mDialog = new AlertDialog.Builder(this.getActivity())
				.setTitle(R.string.room)
				.setView(view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = mNameEdit.getText().toString();
								String lid = mLockIdEdit.getText().toString();
								String rid = mRoomIdEdit.getText().toString();
								if (TextUtils.isEmpty(name) == true) {
									name = "No";
								}
								if (TextUtils.isEmpty(lid) == true) {
									lid = configManager.ZERO;
								}
								if (TextUtils.isEmpty(rid)) {
									rid = configManager.ZERO;
								}
								Room roomSelected = (Room) mRoomAdapter
										.getItem(mSelected);
								roomSelected.setName(name);
								roomSelected.setLockId(lid);
								roomSelected.setIdSever(rid);
								mRooms.set(mSelected, roomSelected);
								mRoomAdapter.notifyDataSetChanged();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mDialog.dismiss();
							}
						}).create();
		return mDialog;
	}

	public void save() {
		RegisterService service = RegisterService.getService();
		if (service == null)
			return;
		HomeCenterUIEngine uiEngine = service.getUIEngine();
		if (uiEngine == null) {
			return;
		}

		List<Room> rooms = uiEngine.getHouse().getRooms();
		int size = rooms.size();
		for (int i = 0; i < size; i++) {
			rooms.set(i, mRooms.get(i));
		}

		List<Object> objects = uiEngine.getHouse().getAllObjectInHome();
		if (objects != null && objects.size() > 0) {
			boolean result = XMLHelper.createDeviceFileXml(objects);
			if (result == true) {
				uiEngine.notifyXMLObserver();
			}
		}
	}

	@Override
	public void savedXML() {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Room room = (Room) mRoomAdapter.getItem(position);
		
		Log.i(TAG, "onItemClick "+room.getName());
		if (position == (configManager.MAX_ROOM_IN_AREA + 1)) {			
				showDetailGSM();
			
		} else if (position < configManager.MAX_ROOM_IN_AREA){
			DoorLock device = room.getDoorLock();
			if (device != null) {
				showDetailDoorLock(device);
			}
		}

	}

	private void showDetailDoorLock(Device device) {
		Bundle bundle = new Bundle();

		bundle.putSerializable(configManager.DEVICE_BUNDLE, device);
		DetailDoorLockScreen fragment = DetailDoorLockScreen
				.initializeDetailDeviceScreen(bundle, -1,
						(SlidingBaseActivity) mContext);
		HomeCenter2Activity activity = (HomeCenter2Activity) mContext;
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);
	}
	
	private void showDetailGSM() {		
		DetailGSM fragment = DetailGSM
				.initializeDetailGSM(null, -1,
						(SlidingBaseActivity) mContext);
		HomeCenter2Activity activity = (HomeCenter2Activity) mContext;
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);
	}

}
