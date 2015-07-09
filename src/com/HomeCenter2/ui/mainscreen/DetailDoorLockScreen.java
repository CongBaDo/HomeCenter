package com.HomeCenter2.ui.mainscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.customview.HeaderFooterGridView;
import com.HomeCenter2.customview.SquareImageView;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.KeyDoorLock;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.adapter.KeyDoorLockAdapter;
import com.HomeCenter2.ui.listener.GetStatusKeyLockListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;
import com.HomeCenter2.utils.ImageProcessDialog;
import com.HomeCenter2.utils.ImageProcessDialog.ImageDialogListener;
import com.HomeCenter2.utils.ImageProcessDialog.ACTION;

public class DetailDoorLockScreen extends RADialerMainScreenAbstract implements
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, GetStatusKeyLockListener, OnItemLongClickListener, OnClickListener {

	private final int CHANGE_TYPE_DIALOG = 0;
	private LayoutInflater mInflater = null;

	public DetailDoorLockScreen(int title, String tag,
			SlidingBaseActivity context) {
		super(DetailDoorLockScreen.class, title, tag, context);
	}

	public static final String TAG = "TMT DetailDeviceScreen";
	public static Bundle mBundle;
	public static DetailDoorLockScreen m_instance = null;
	public DoorLock mDevice = null;
	public Room mRoom = null;

	ScrollView scrollView = null;

	HeaderFooterGridView mKeyLV;

	KeyDoorLockAdapter mKeyAdapter;

	Dialog mDialog = null;
	private View header;
	private SquareImageView imgThumbLeft, imgThumbRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(mContext);
		Log.d(TAG, "onCreateConentView");
		mDevice = (DoorLock) mBundle
				.getSerializable(configManager.DEVICE_BUNDLE);
		int roomId = mDevice.getRoomId();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			House mHouse = uiEngine.getHouse();
			if (mHouse != null) {
				mRoom = mHouse.getRoomsById(roomId);
			}
		}
		setHasOptionsMenu(true);
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			uiEngine.addKeyLockObserver(this);
		}

		View view = inflater.inflate(R.layout.detail_door_lock_screen,
				container, false);
		initUI(view, inflater);
		getDetailDoorLock();

		return view;
	}
	
	private void initUI(View view, LayoutInflater inflater){
		mKeyLV = (HeaderFooterGridView) view.findViewById(R.id.lvDoorLock);
		header = inflater.inflate(R.layout.header_room_detail, null);
		mKeyLV.addHeaderView(header);

		mKeyAdapter = new KeyDoorLockAdapter(mContext, mDevice);
		mKeyLV.setAdapter(mKeyAdapter);
		mKeyLV.setOnItemClickListener(this);
		mKeyLV.setNumColumns(5);
		mKeyLV.setOnItemLongClickListener(this);
		
		imgThumbLeft = (SquareImageView)header.findViewById(R.id.image_thumb_left);
		imgThumbRight = (SquareImageView)header.findViewById(R.id.image_thumb_right);
		
		imgThumbLeft.setOnClickListener(this);
		imgThumbRight.setOnClickListener(this);
	}

	@Override
	public void onScreenSlidingCompleted() {
	}

	@Override
	public void loadHeader() {

	}

	@Override
	public void setContentMenu() {

	}

	public static DetailDoorLockScreen initializeDetailDeviceScreen(
			Bundle bundle, int titleId, SlidingBaseActivity mContext) {
		mBundle = bundle;
		if (m_instance == null) {
			m_instance = new DetailDoorLockScreen(titleId,
					ScreenManager.DETAIL_DEVICE_TAG, mContext);
		}
		return m_instance;
	}

	private void getDetailDoorLock() {
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, mDevice.getRoomId());
		bundle.putInt(configManager.DEVICE_ID, mDevice.getId());

		Log.e(TAG, "getDetailDoorLock::room id: " + mDevice.getRoomId()
				+ " , devId: " + mDevice.getId());

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_GET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			uiEngine.removeKeyLockObserver(this);
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		if (mRoom != null) {
			mActionBarV7.setTitle(mRoom.getName());
		}
		mActionBarV7.setDisplayShowTitleEnabled(true);
		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		inflater.inflate(R.menu.my_detail_doorlock_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected:" + isVisible() + " , is: "
				+ (android.R.id.home == item.getItemId()));
		boolean isSelected = false;
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				backToPreviousScreen();
				isSelected = true;
				break;
			case R.id.remote_menu:
				setModeDoorLock(mDevice);
				break;
			}
		}
		return isSelected;
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub
		getDetailDoorLock();
		mActionBarV7.setIcon(mDevice.getIcon());

	}

	@Override
	public void onPageDeselected() {
		mActionBarV7.setIcon(R.drawable.ic_protection_totale);
	}

	private void refreshTextColor(TextView tv) {
		boolean isChecked = Boolean.parseBoolean(tv.getTag().toString());
		setTextColor(tv, isChecked);
	}

	private void setTextColor(TextView tv, Boolean isChecked) {
		if (isChecked == true) {
			isChecked = false;
			tv.setTypeface(Typeface.DEFAULT);
			tv.setTextColor(getResources().getColor(R.color.black));

		} else {
			isChecked = true;
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			tv.setTextColor(getResources().getColor(R.color.blue_light));

		}
		tv.setTag(isChecked);

	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*
		 * KeyDoorLock key = (KeyDoorLock) mKeyAdapter.getItem(position);
		 * if(key!= null){ key.setStatus(!key.isStatus());
		 * setDetailDevice(key,!key.isStatus()); }
		 * 
		 * mKeyAdapter.notifyDataSetChanged();
		 */
	}

	@Override
	public void recieveKeyLockStatus() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mKeyAdapter.notifyDataSetChanged();
			}
		});

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

	EditText mPasswordEdit;
	ImageView mKeyImage;

	int mPosition = 0;

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		mPosition = position;
		DialogFragmentWrapper.showDialog(getFragmentManager(), this,
				configManager.DIALOG_CHANGE_ROOM_NAME);
		return true;
	}

	public Dialog showContentDeviceDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		View view = mInflater.inflate(R.layout.add_lock_name_dialog, null);
		mPasswordEdit = (EditText) view.findViewById(R.id.editPasswordKey);
		mPasswordEdit.setText("");

		mKeyImage = (ImageView) view.findViewById(R.id.imgRoom);
		KeyDoorLock mKeyCurrent = (KeyDoorLock) mKeyAdapter.getItem(mPosition);
		mKeyImage.setImageResource(mKeyCurrent.getIcon());

		mDialog = new AlertDialog.Builder(this.getActivity())
				.setTitle(mKeyCurrent.getNameKey())
				.setView(view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String pw = mPasswordEdit.getText().toString();
								if (!TextUtils.isEmpty(pw)) {
									Bundle bundle = new Bundle();
									bundle.putString(
											configManager.PASSWORD_KEY, pw);
									bundle.putString(configManager.NAME, "tmt");
									setPasswordKey(bundle);
									mKeyAdapter.notifyDataSetChanged();
									// save();
									mDialog.dismiss();
								}

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

	public void saveConfig(Bundle bundle) {
		/*
		 * destroyTask(); HCRequest hc = new HCRequest();
		 * hc.setRequestType(HCRequest.REQUEST_SET_DEVICE_ADDRESS);
		 * mNameRequestTask = new SocketRequestTask(this); if (mNameRequestTask
		 * != null) { mNameRequestTask.setRequest(hc);
		 * mNameRequestTask.setObject(bundle); mNameRequestTask.execute(hc); }
		 */
	}

	private void setModeDoorLock(Device device) {
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoomId());
		bundle.putString(configManager.MODE_STATUS, configManager.MODE_LEARN_IR);
		bundle.putInt(configManager.DEVICE_ID, 1);

		Log.e(TAG, "getDetailDevice::room id: " + device.getRoomId()
				+ " , devId: " + device.getDeviceId());

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	private void setPasswordKey(Bundle bundle) {
		bundle.putInt(configManager.ROOM_ID, mDevice.getRoomId());
		int pos = mPosition + 1;
		bundle.putInt(configManager.DEVICE_ID, pos);
		Log.e(TAG, "getDetailDevice::room id: " + mDevice.getRoomId()
				+ " , devId: " + pos + ", pw:");

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_PASSWORD_KEY;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_thumb_left:
			showDialog();
			break;
			
		case R.id.image_thumb_right:
			showDialog();
			break;

		default:
			break;
		}
	}
	
	private void showDialog(){
		ImageProcessDialog dialog = new ImageProcessDialog(getActivity(), new ImageDialogListener() {
			
			@SuppressLint("NewApi") @Override
			public void shareCallback(ACTION shareType) {
				// TODO Auto-generated method stub
				if(shareType == ACTION.DELETE_IMAGE){
					
				}else if(shareType == ACTION.EDIT_DEVICE){
					
				}else if(shareType == ACTION.TAKE_PHOTO){
					
				}else if(shareType == ACTION.USE_LIBRARY){
					
				}
			}
			
			@Override
			public void dismissListener() {
				// TODO Auto-generated method stub
				
			}
		});
		dialog.showRadialDialog();
	}

}
