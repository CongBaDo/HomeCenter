package com.HomeCenter2.ui.mainS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
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
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.DialogRoomMenu;
import com.HomeCenter2.ui.RoomMenuListener;
import com.HomeCenter2.ui.adapter.MyRemoteAdapter;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.mainscreen.RemoteAirConditionerScreen;
import com.HomeCenter2.ui.mainscreen.RemoteCameraScreen;
import com.HomeCenter2.ui.mainscreen.RemoteTVScreen;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyRemotesScreen extends RADialerMainScreenAbstract implements
		XMLListener, DialogFragmentWrapper.OnCreateDialogFragmentListener,
		StatusListener, TabHost.OnTabChangeListener,
		ViewPager.OnPageChangeListener, RoomMenuListener, View.OnClickListener {

	private static final String TAG = "TMT MyRemotesScreen";

	HomeCenterUIEngine mUiEngine = null;
	House mHouse = null;
	public RoomMenuAdapter mRoomDropDownAdapter;
	protected Handler mHandler;
	LayoutInflater mInflater;
	private TabHost mTabHost;
	private ViewPager mPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private MyRemoteAdapter mPagerAdapter;
	TextView mTxtRoomName;
	public RoomMenuListener mRoomMenuListener;

	Room mRoomCurrent = new Room();
	int mRemoteStatus = configManager.REMOTE_CONTROL;

	Menu mMenu = null;

	public MyRemotesScreen(int title, String tag, SlidingBaseActivity context) {
		super(MyRemotesScreen.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); */

		mUiEngine = RegisterService.getHomeCenterUIEngine();
		if (mUiEngine == null) {
			return;
		}
		mHouse = mUiEngine.getHouse();
		mInflater = getLayoutInflater(savedInstanceState);
		mUiEngine.addStatusObserver(this);
		mUiEngine.addXMLObserver(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");

		List<Room> rooms = null;
		if (mHouse != null) {
			rooms = mHouse.getRooms();
			if (rooms != null && rooms.size() > 0) {
				if (mRoomDropDownAdapter != null) {
					mRoomDropDownAdapter = null;
				}
				mRoomDropDownAdapter = new RoomMenuAdapter(mContext, this,
						rooms);

				mRoomCurrent = rooms.get(0);
				mRoomDropDownAdapter.setSelected(0);
			}
		}

		if (mRoomDropDownAdapter != null) {
			refreshListByPosition(mRoomDropDownAdapter.getSelected());
		}
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.my_remote_screen,
				container, false);
		mTxtRoomName = (TextView) view.findViewById(R.id.txtTitle);
		mTxtRoomName.setSelected(true);
		mTxtRoomName.setOnClickListener(this);

		mPager = (ViewPager) view.findViewById(R.id.viewpager);
		mPager.setOffscreenPageLimit(1);
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);

		this.initialiseTabHost(savedInstanceState);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set
																				// state
		}
		// Intialise ViewPager
		this.initialiseViewPager();

		return view;
	}

	/*
	 * protected void onSaveInstanceState(Bundle outState) {
	 * outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab
	 * selected super.onSaveInstanceState(outState); }
	 */

	List<Fragment> mFragments;

	private void initialiseViewPager() {
		Log.d(TAG, "initialiseViewPager");
		mFragments = new Vector<Fragment>();
		mFragments.add(new RemoteTVScreen(this));
		mFragments.add(new RemoteCameraScreen(this));
		mFragments.add(new RemoteAirConditionerScreen(this));
		//
	}

	private void showViewPager() {
		this.mPagerAdapter = new MyRemoteAdapter(mContext,
				getFragmentManager(), mFragments);
		this.mPager.setAdapter(this.mPagerAdapter);
		this.mPager.setOnPageChangeListener(this);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost.setup();
		TabInfo tabInfo = null;
		AddTab(mContext, this.mTabHost, this.mTabHost.newTabSpec("Tab1")
				.setIndicator(this.getString(R.string.remote_tv)),
				(tabInfo = new TabInfo("Tab1", RemoteTVScreen.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		AddTab(mContext, this.mTabHost, this.mTabHost.newTabSpec("Tab2")
				.setIndicator(this.getString(R.string.remote_camera)),
				(tabInfo = new TabInfo("Tab2", RemoteCameraScreen.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		AddTab(mContext, this.mTabHost, this.mTabHost.newTabSpec("Tab3")
				.setIndicator(this.getString(R.string.remote_aircondition)),
				(tabInfo = new TabInfo("Tab3",
						RemoteAirConditionerScreen.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		// this.onTabChanged("Tab1");
		//
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private void AddTab(Activity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(activity));
		tabHost.addTab(tabSpec);
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
		mMenu = menu;
		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setTitle(mTitleId);
		mActionBarV7.setDisplayShowTitleEnabled(true);

		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		inflater.inflate(R.menu.my_remote_menu, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		int type = mUiEngine.getRemoteType();

		refreshRemoteType(mMenu, type);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				return true;
			case R.id.control_menu:
				refreshRemoteType(mMenu, configManager.REMOTE_CONTROL);
				mUiEngine.setRemoteType(configManager.REMOTE_CONTROL);
				return true;
			case R.id.update_menu:
				refreshRemoteType(mMenu, configManager.REMOTE_UPDATE);
				mUiEngine.setRemoteType(configManager.REMOTE_UPDATE);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void refreshRemoteType(Menu menu, int type) {
		if (menu == null)
			return;
		MenuItem item;
		String title;
		mRemoteStatus = type;
		switch (type) {
		case configManager.REMOTE_UPDATE:
			item = menu.findItem(R.id.control_menu);
			item.setChecked(false);
			item.setIcon(R.drawable.ic_launcher);

			item = menu.findItem(R.id.update_menu);
			item.setChecked(true);

			title = mRoomCurrent.getName();
			mTxtRoomName.setText(title + " - " + item.getTitle());

			break;
		default:
			item = menu.findItem(R.id.control_menu);
			item.setChecked(true);

			title = mRoomCurrent.getName();
			mTxtRoomName.setText(title + " - " + item.getTitle());

			item = menu.findItem(R.id.update_menu);
			item.setChecked(false);

			break;
		}

	}

	private boolean refreshListByPosition(int position) {
		if (mRoomDropDownAdapter == null)
			return false;
		mRoomCurrent = (Room) mRoomDropDownAdapter.getItem(position);
		if (mRoomCurrent == null) {
			return false;
		}
		return refreshListByRoom(mRoomCurrent);
	}

	private boolean refreshListByRoom(Room room) {
		if (room == null) {
			return false;
		}
		String status = "";
		switch (mRemoteStatus) {
		case configManager.REMOTE_UPDATE:
			status = mContext.getString(R.string.update);
			break;
		default:
			status = mContext.getString(R.string.control);
			break;
		}

		mTxtRoomName.setText(room.getName() + " - " + status);

		List<Control> devices = room.getControls();
		ArrayList<Device> items = new ArrayList<Device>();
		Device item = null;
		int size = devices.size();
		for (int i = 0; i < size; i++) {

			item = devices.get(i);
			if (item instanceof LampRoot || item instanceof DoorLock
					|| item instanceof RollerShutter) {
				items.add(item);
			}
		}
		return true;
	}

	@Override
	public void savedXML() {
		mRoomMenuListener = this;
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				List<Room> rooms = null;
				if (mHouse != null) {
					rooms = mHouse.getRooms();
					int size = rooms.size();
					if (rooms != null && size > 0) {
						if (mRoomDropDownAdapter != null) {
							mRoomDropDownAdapter = null;
						}
						mRoomDropDownAdapter = new RoomMenuAdapter(mContext,
								mRoomMenuListener, rooms);
						mRoomCurrent = rooms.get(0);
						mRoomDropDownAdapter.setSelected(0);
					}
				}

				if (mRoomDropDownAdapter != null) {
					rooms = null;
					if (mHouse != null) {
						rooms = mHouse.getRooms();

						if (rooms != null && rooms.size() > 0) {
							int selected = mRoomDropDownAdapter.getSelected();
							Room room = (Room) mRoomDropDownAdapter
									.getItem(selected);
							int positionInRooms = 0;
							if (room != null) {
								positionInRooms = mHouse
										.getPositionRoomById(positionInRooms);
							}
							mRoomDropDownAdapter = new RoomMenuAdapter(
									mContext, mRoomMenuListener, rooms);
							mRoomCurrent = rooms.get(positionInRooms);
							mRoomDropDownAdapter.setSelected(positionInRooms);
						}
					}
					refreshListByPosition(mRoomDropDownAdapter.getSelected());
				}

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

	@Override
	public void changeStatusDevice() {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "changeStatusDevice");
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mUiEngine.removeStatusObserver(this);
		mUiEngine.removeXMLObserver(this);

	}

	public void setUpdateRemote(String roomId, String sensorId, String deviceId) {
		Bundle bundle = new Bundle();
		bundle.putString(configManager.ROOM_ID, roomId);
		bundle.putString(configManager.SENSOR_ID, sensorId);
		bundle.putString(configManager.DEVICE_ID, deviceId);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_LEARN_INFRARED;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	public void setControlRemote(String roomId, String sensorId, String deviceId) {
		Bundle bundle = new Bundle();
		bundle.putString(configManager.ROOM_ID, roomId);
		bundle.putString(configManager.SENSOR_ID, sensorId);
		bundle.putString(configManager.DEVICE_ID, deviceId);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_TRANSMIT_INFRARED;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	public void showSheduleRemote() {

	}

	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 * 
	 * @author mwho
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(<span
		 *      class="skimlinks-unlinked">java.lang.String</span>)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	public void onTabChanged(String tag) {
		// TabInfo newTab = <span
		// class="skimlinks-unlinked">this.mapTabInfo.get(tag</span>);
		int pos = this.mTabHost.getCurrentTab();
		this.mPager.setCurrentItem(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see <span class="skimlinks-unlinked">android.support.v4.view.ViewPager.
	 * OnPageChangeListener#onPageScrolled(int</span>, float, int)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see <span class="skimlinks-unlinked">android.support.v4.view.ViewPager.
	 * OnPageChangeListener#onPageSelected(int</span>)
	 */
	@Override
	public void onPageSelected(int position) {
		this.mTabHost.setCurrentTab(position);
		// //

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see <span class="skimlinks-unlinked">android.support.v4.view.ViewPager.
	 * OnPageChangeListener#onPageScrollStateChanged(int</span>)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// Log.d(TAG, "onViewScrolledComplete" + isShow);
		/*
		 * if(isShow){ showViewPager(); }
		 */
	}

	@Override
	public void onPageSelected() {
		Log.d(TAG, "onPageSelected");
		if (mRoomDropDownAdapter != null) {
			mRoomDropDownAdapter.setSelected(mUiEngine.getRoomCurrentIndex());
			refreshListByPosition(mRoomDropDownAdapter.getSelected());
		}
		showViewPager();
	}

	public Room getCurrentRoom() {
		if (mRoomDropDownAdapter == null)
			return null;
		return (Room) mRoomDropDownAdapter.getItem(mRoomDropDownAdapter
				.getSelected());
	}

	@Override
	public void onRoomSelected(int selected) {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mRoomDropDownAdapter == null)
					return;
				refreshListByPosition(mRoomDropDownAdapter.getSelected());
			}
		});

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
