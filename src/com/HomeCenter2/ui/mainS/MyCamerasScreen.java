package com.HomeCenter2.ui.mainS;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.PInfo;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Camera;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.DialogRoomMenu;
import com.HomeCenter2.ui.PullToRefreshListView.OnRefreshListener;
import com.HomeCenter2.ui.RoomMenuListener;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.MyApplicationssAdapter;
import com.HomeCenter2.ui.adapter.MyCameraAdapter;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyCamerasScreen extends RADialerMainScreenAbstract implements
		OnRefreshListener, OnItemLongClickListener, XMLListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, StatusListener, RoomMenuListener, View.OnClickListener {

	private static final String TAG = "MyCamerasScreen";
	ListView mLVDevices;
	MyCameraAdapter mSensorAdapter;
	HomeCenterUIEngine mUiEngine = null;
	House mHouse = null;
	RoomMenuAdapter mRoomDropDownAdapter;
	protected Handler mHandler;
	LayoutInflater mInflater;
	private Device mDeviceFocused = null;
	PackageManager pm;

	public static final int APPLICATION_DIALOG = 0;
	public AlertDialog mDialog = null;
	ListView mAllAppsLV = null;
	EditText mNameEditView;
	MyApplicationssAdapter mAppsAdapter = null;

	TextView mTxtRoomName;
	ImageView mImgRoomIcon;

	public MyCamerasScreen(int title, String tag, SlidingBaseActivity context) {
		super(MyCamerasScreen.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); */
		RegisterService service = RegisterService.getService();
		if (service != null) {
			mUiEngine = service.getUIEngine();
		}
		if (mUiEngine == null) {
			return;
		}
		mHouse = mUiEngine.getHouse();
		mInflater = getLayoutInflater(savedInstanceState);
		mUiEngine.addStatusObserver(this);
		mUiEngine.addXMLObserver(this);
		pm = mContext.getPackageManager();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mRoomDropDownAdapter == null) {
			List<Room> rooms = null;
			if (mHouse != null) {
				rooms = mHouse.getRooms();
				int size = rooms.size();
				if (rooms != null && size > 0) {
					mRoomDropDownAdapter = new RoomMenuAdapter(mContext, this,
							rooms);
					mRoomDropDownAdapter.setSelected(0);
				}
			}
		}
		if (mRoomDropDownAdapter != null) {
			refreshListByPosition(mRoomDropDownAdapter.getSelected());
		}
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
		mSensorAdapter = new MyCameraAdapter(mContext, null);
		mLVDevices.setAdapter(mSensorAdapter);
		mLVDevices.setOnItemClickListener(this);
		mLVDevices.setOnItemLongClickListener(this);

		mImgRoomIcon = (ImageView) view.findViewById(R.id.imgIcon);		

		ScheduleImageView imgRoomOn = (ScheduleImageView) view.findViewById(R.id.imgOn);
		imgRoomOn.setVisibility(View.GONE);
		
		ScheduleImageView imgRoomOff = (ScheduleImageView) view.findViewById(R.id.imgOff);
		imgRoomOff.setVisibility(View.GONE);

		mTxtRoomName = (TextView) view.findViewById(R.id.txtTitle);
		mTxtRoomName.setSelected(true);
		mTxtRoomName.setOnClickListener(this);

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
				return true;
			}			
		}
		return false;
	}

	private boolean refreshListByPosition(int position) {
		if (mRoomDropDownAdapter == null)
			return false;
		Room room = (Room) mRoomDropDownAdapter.getItem(position);
		return refreshListByRoom(room);
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
			if (item instanceof Camera) {
				items.add(item);
			}
		}
		mSensorAdapter.onChangeData(items);
		mLVDevices.setAdapter(mSensorAdapter);
		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		mDeviceFocused = (Device) mSensorAdapter.getItem(position);
		DialogFragmentWrapper.showDialog(getFragmentManager(), this,
				APPLICATION_DIALOG);
		return true;
	}

	@Override
	public void savedXML() {
		Room room = (Room) mRoomDropDownAdapter.getItem(mRoomDropDownAdapter
				.getSelected());
		refreshListByRoom(room);
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case APPLICATION_DIALOG:
			Room room = (Room) mRoomDropDownAdapter
					.getItem(mRoomDropDownAdapter.getSelected());
			return showAllAppsDialog(mContext, mInflater, room);
		case configManager.DIALOG_ROOM_MENU:
			return DialogRoomMenu.showContentDeviceDialog(mContext, mInflater,
					mRoomDropDownAdapter);
		default:
			break;
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		mDeviceFocused = (Device) mSensorAdapter.getItem(position);
		Camera temp = (Camera) mDeviceFocused;
		PInfo appInfo = temp.getAppInfo();
		if (appInfo == null)
			return;
		Intent appStartIntent = pm
				.getLaunchIntentForPackage(appInfo.getPname());
		if (null != appStartIntent) {
			mContext.startActivity(appStartIntent);
		}
	}

	@Override
	public void changeStatusDevice() {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "changeStatusDevice");
				refreshListByPosition(mRoomDropDownAdapter.getSelected());
			}
		});

	}

	@Override
	public void onRefresh() {
		Log.d(TAG, "onRefresh");
		Room room = (Room) mRoomDropDownAdapter.getItem(mRoomDropDownAdapter
				.getSelected());
	}

	// ////////////////////Get All app

	private ArrayList<PInfo> getPackages() {
		ArrayList<PInfo> apps = getInstalledApps(false); /*
														 * false = no system
														 * packages
														 */
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
		}
		return apps;
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
		ArrayList<PInfo> res = new ArrayList<PInfo>();
		List<PackageInfo> packs = mContext.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((!getSysPackages) && (p.versionName == null)) {
				continue;
			}
			PInfo newInfo = new PInfo();
			newInfo.setAppname(p.applicationInfo.loadLabel(pm).toString());
			newInfo.setPname(p.packageName);
			newInfo.setVersionName(p.versionName);
			newInfo.setVersionCode(p.versionCode);
			newInfo.setIcon(p.applicationInfo.loadIcon(pm));
			res.add(newInfo);
		}
		return res;
	}
	
	int mPosition = -1;

	public Dialog showAllAppsDialog(Context context, LayoutInflater inflater,
			Room room) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		ArrayList<PInfo> appInfos = getPackages();
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.all_apps_listview, null);
		mAllAppsLV = (ListView) view.findViewById(R.id.lvApps);
		
		mAppsAdapter = new MyApplicationssAdapter(mContext, appInfos);
		mAllAppsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mAllAppsLV.setAdapter(mAppsAdapter);
		
		Camera camera = (Camera) mDeviceFocused;		
		int pos = getCurrentIndex(appInfos, camera.getAppInfo());
		mPosition = pos;
		mAllAppsLV.setItemChecked(pos, true);
		
		mNameEditView = (EditText) view.findViewById(R.id.editNameAddDevice);
		mNameEditView.setText(mDeviceFocused.getName());

		

		mAllAppsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mPosition = position;				
			}
		});		

		mDialog = new AlertDialog.Builder(mContext)
				.setTitle(R.string.device)
				.setView((View) view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Camera camera = (Camera) mDeviceFocused;
								PInfo appInfo = (PInfo) mAppsAdapter.getItem(mPosition);								
								camera.setAppInfo(appInfo);
								
								String name = mNameEditView.getText()
										.toString();
								if (!TextUtils.isEmpty(name)) {
									Log.d(TAG, "Save name camera:" + name);
									camera.setName(name);
									RegisterService service = RegisterService
											.getService();
									if (service != null) {
										HomeCenterUIEngine uiEngine = service
												.getUIEngine();
										if (uiEngine != null) {

											Room room = (Room) mRoomDropDownAdapter
													.getItem(mRoomDropDownAdapter
															.getSelected());
											uiEngine.saveRoom(room);
										}
									}
								}

								mDialog.dismiss();

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

	@Override
	public void onViewScrolledComplete(boolean isShow) {

	}

	@Override
	public void onRoomSelected(int selected) {
		if (mRoomDropDownAdapter == null)
			return;
		refreshListByPosition(mRoomDropDownAdapter.getSelected());
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub
		if (mRoomDropDownAdapter != null) {
			mRoomDropDownAdapter.setSelected(mUiEngine.getRoomCurrentIndex());
			refreshListByPosition(mRoomDropDownAdapter.getSelected());
		}
	}

	@Override
	public void onPageDeselected() {	

	}
	
	private int getCurrentIndex(ArrayList<PInfo> items, PInfo appInfo) {
		int size = items.size();
		String name;
		int index;
		if(size<=0 || appInfo == null)
		{
			index = -1;
			return index;
		}
		index = 0;		
		name = appInfo.getAppname();
		PInfo item= null;
		for(int i = 0; i< size; i++){
			item = items.get(i);			
			if(item != null && item.getAppname().equals(name)== true){
				index = i;
				break;
			}
		}		
		return index;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.txtTitle){
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_ROOM_MENU);			
		}
		
	}

}
