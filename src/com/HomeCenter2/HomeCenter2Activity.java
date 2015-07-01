/*
 * HomeCenter2.java
 *
 * ************************************************************************
 *     Copyright ï¿½ 2010 Agito Networks, Inc.
 * ************************************************************************
 * NOTICE:
 * This document contains information that is confidential and proprietary
 * to Agito Networks Inc. No part of this document may be reproduced in any
 * form whatsoever without written prior approval by Agito Networks, Inc.
 * ************************************************************************
 *
 * Author:    Tim Olson
 */

package com.HomeCenter2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.fragment.RoomManageFragment;
import com.HomeCenter2.house.House;
import com.HomeCenter2.ui.listener.ConnectSocketListener;
import com.HomeCenter2.ui.mainS.MyAudioSystemScreen;
import com.HomeCenter2.ui.mainS.MyDevicesScreen;
import com.HomeCenter2.ui.mainS.RoomManagerScreenFragment;
import com.HomeCenter2.ui.menuscreen.MainScreen;
import com.HomeCenter2.ui.slidingmenu.framework.OnPageScrolledCompleteListener;
import com.HomeCenter2.ui.slidingmenu.framework.PageChangeAware;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerSettingScreenAbstract.OnPreferenceAttachedListener;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenEntry;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;
import com.HomeCenter2.ui.slidingmenu.framework.ViewManager;

public class HomeCenter2Activity extends SlidingBaseActivity implements
		OnPageScrolledCompleteListener, OnPreferenceAttachedListener,
		OnSharedPreferenceChangeListener, ConnectSocketListener {
	public static ViewManager viewManager;
	public static final String PREF_NAME = "HomeScreenSharedPref";

	static HomeCenter2Activity app_context;
	static final String TAG = "HomeCenter2";
	HomeCenterUIEngine uiEngine;

	static final public boolean ANDROID_SIMULATOR = false;
	private static boolean m_isLibsLoaded = false;

	// private SettingAndGlobalSearchContent mMainMenuScreen;
	RegisterService mService = null;

	Window win;
	private ImageView mSplashImageView;
	private InputMethodManager mIMethodManager;
	public int mWidth;
	public int mHeigth;
	public int mNotificationBarHeight;
	private int tabIndex = -1;
	private boolean mIsInForeground;

	public static boolean isLibsLoaded() {
		return m_isLibsLoaded;
	}

	static final public int RADIALER_MENU_GROUP_ID = 10;
	static final public int KEYPAD_DIALER_ACTIVITY_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 1;
	static final public int CONTACTS_ACTIVITY_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 2;
	static final public int RECENT_ACTIVITY_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 3;
	static final public int DIRECTORY_ACTIVITY_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 4;
	static final public int RECENT_ACTIVITY_MENU_IMSESSION_GROUP_ID = RADIALER_MENU_GROUP_ID + 5;
	static final public int RECENT_ACTIVITY_MENU_CALL_GROUP_ID = RADIALER_MENU_GROUP_ID + 6;
	static final public int RECENT_CALL_LOG_DETAIL_GROUP_ID = RADIALER_MENU_GROUP_ID + 7;
	static final public int PRESENCE_ACTIVITY_MENU_ONLINE_GROUP_ID = RADIALER_MENU_GROUP_ID + 8;
	static final public int PRESENCE_ACTIVITY_MENU_OFFLINE_GROUP_ID = RADIALER_MENU_GROUP_ID + 9;
	static final public int CHAT_GROUP_ID = RADIALER_MENU_GROUP_ID + 10;
	static final public int IN_CALL_GROUP_ID = RADIALER_MENU_GROUP_ID + 11;
	static final public int TODAY_ACTIVITY_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 12;
	static final public int PARTICIPANT_PICKER_MENU_GROUP_ID = RADIALER_MENU_GROUP_ID + 13;
	static final public int MAX_GROUP = RADIALER_MENU_GROUP_ID + 14;

	static final public String SHOW_TAB = "show_tab";
	static final public String HIDE_TAB = "hide_tab";
	static final public int SHOW_DIAL_PAD_TAB = 1;
	static final public int SHOW_RECENT_TAB = 2;
	static final public int SHOW_RECENT_TAB_IM_CONVERSATION = 3;
	static final public int SHOW_PRESENCE_TAB = 4;
	static final public int SHOW_CAS_TAB = 5;

	static final String IMAGE_VERSION_KEY = "imageVersion";
	static final String IMAGE_URL_KEY = "imageUrl";
	static final public int PROVISION_REQUEST = 1;
	static final public int SETTING_REQUEST = 10;

	// public static final int DIALOG_PROGRESS =
	// ProvisionActivity.DIALOG_PROGRESS;
	public static final int DIALOG_PROGRESS = 0;
	public static final int DIALOG_DOWNLOAD_PROGRESS = DIALOG_PROGRESS + 5;
	public static final int DIALOG_INVALID_CONFIG = DIALOG_PROGRESS + 6;
	public static final int DIALOG_WIFI_POLICY = DIALOG_PROGRESS + 7;
	public static final int DIALOG_TRANSFER_CHOICE = DIALOG_PROGRESS + 8;
	public static final int DIALOG_CLIENT_ENABLE_ALERTING = DIALOG_PROGRESS + 9;

	private final static int MAX_DISPLAY_TAB_ITEM = 5;

	private int mTabItemHeightInPx, mTabItemWidthInPx;
	private static Typeface appFontTypeface;
	private String forcedSwitchScreenTag = null;

	private int mCurrentPageSelectedPosition = -1;
	private boolean newPageSelected = false;
	private boolean internalPageChanged = false;
	private boolean mIsAppInBackground = false;

	private boolean startedFromProvisionActivity;
	private int index = 0;

	protected void onNewIntent(Intent intent) {
		Log.d(TAG, "HomeCenter2.onNewIntent(): Replacing the intent.\n");
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "HomeCenter2.onStop()\n");
		super.onStop();
		mIsAppInBackground = true;
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "HomeCenter2.onPause()\n");
		super.onPause();
		HomeCenterUIEngine uiEngine = getUiEngine();
		if (uiEngine != null) {
			uiEngine.onActivityPause(this);
		}
		if (viewManager != null) {
			tabIndex = viewManager.getCurrentPage();
		}
		mIsInForeground = false;
	}

	private String m_lastDigits = "";

	public void saveLastDigits(String digits) {
		m_lastDigits = digits;
	}

	public String getLastDigits() {
		return m_lastDigits;
	}

	@Override
	public void onDestroy() {
		// uiEngine.removeConnectSocketObserver(this);
		super.onDestroy();
		RegisterService sevice = RegisterService.getService();
		if (sevice != null) {
			HomeCenterUIEngine uiEngine = sevice.getUIEngine();
			if (uiEngine != null) {
				Log.i(TAG, "HomeCenter2.onDestroy() - close socket\n");
				uiEngine.closeSocket();
			}
		}

	}

	public HomeCenter2Activity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//TMT os
		Log.d(TAG, "onCreate");
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		app_context = this; // may get jni calls from native threads that need
							// HomeCenter2.getContext()

		win = getWindow();
		caculateScreenMetrics();
		caculateNotificationBarHeight();
		super.onCreate(savedInstanceState);
		initLibsAndServices();
		viewManager = getViewManager();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		HomeScreenSetting.ScreenH = metrics.heightPixels;
		HomeScreenSetting.ScreenW = metrics.widthPixels;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		refresh();

	}

	public static Activity getContext() {
		return app_context;
	}

	public static HomeCenter2Activity getInstance() {
		return app_context;
	}

	/**
	 * This method used to exit application completely.
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.e(TAG, "onActivityResult Nemo "+requestCode+" "+resultCode);
		
		switch (requestCode) {
		
		case configManager.RESULT_ROOM_INDEX:
			Fragment frag = viewManager.getPageAdapter().getItem(0);
			Log.v(TAG, "onActivityResult frag "+frag.getTag());
			if (frag instanceof RoomManagerScreenFragment) {
				Log.w(TAG, "onActivityResult Nemo "+requestCode);
			}
			break;
		
		case configManager.PROVISION_REQUEST:
			if (this != null) {
				if (RegisterService.getService() != null
						&& RegisterService.getService().getUIEngine()
								.isLogined()) {
					refresh();
				} else {
					shutdownService(this);
				}

			}
			break;
		case configManager.RESULT_SPEECH:
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String speak = text.get(0);
				if (TextUtils.isEmpty(speak))
					break;
				int number = converText(speak);
				Fragment currentPage = viewManager.getPageAdapter().getItem(
						viewManager.getCurrentPage());
				if (currentPage instanceof MyDevicesScreen) {
					MyDevicesScreen page = (MyDevicesScreen) currentPage;
					page.onOffBySpeak(number);
				} else if (currentPage instanceof MyAudioSystemScreen) {
					MyAudioSystemScreen audio = (MyAudioSystemScreen) currentPage;
					audio.onOffBySpeak(number);

				}
			}
			break;
		}

	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		// TODO Auto-generated method stub
	}

	public static void initFont(Context context, TextView view) {
		view.setTypeface(appFontTypeface, Typeface.NORMAL);
	}

	public static void initFont(Context context, EditText view) {
		view.setTypeface(appFontTypeface, Typeface.NORMAL);
	}

	public static void initFont(Context context, Button view) {
		view.setTypeface(appFontTypeface, Typeface.NORMAL);
	}

	public static void initFont(Context context, ToggleButton view) {
		view.setTypeface(appFontTypeface, Typeface.NORMAL);
	}

	public static void initFont(Context context, RadioButton view) {
		view.setTypeface(appFontTypeface, Typeface.NORMAL);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == 0 && internalPageChanged) {
			onViewScrolledComplete(true);
			internalPageChanged = false;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		internalPageChanged = true;
	}

	@Override
	public void onPageSelected(int position) {
		// notify main menu screen

		if (mCurrentPageSelectedPosition != position) {
			newPageSelected = true;
			if (mCurrentPageSelectedPosition >= 0) {
				// notify current page that it is being 'deselected'
				Fragment currentPage = viewManager.getPageAdapter().getItem(
						mCurrentPageSelectedPosition);
				if (currentPage instanceof PageChangeAware) {
					PageChangeAware a = (PageChangeAware) currentPage;
					a.onPageDeselected();
				}
			}
		}

		mCurrentPageSelectedPosition = position;
	}

	@Override
	public void onViewScrolledComplete(boolean isScrool) {

		/*
		 * if (mRoomScreen instanceof PageChangeAware) { PageChangeAware a =
		 * (PageChangeAware) mRoomScreen; // NOTE: It may happen that the
		 * fragment has not yet been // initialized at this stage. // (Example:
		 * place a call from the dialer, press Transfer and select // 'To
		 * Someone Else'. // A new keypad instance is created and this function
		 * gets called // for it, even before onCreate().) // In that case we
		 * really should not be calling // onScreenSlidingCompleted() here... //
		 * How can we detect that..? a.onScreenSlidingCompleted(); }
		 */
	}

	public boolean isAppRestoredFromBackground() {
		return mIsAppInBackground;
	}

	public void clearStateAppRestoreFromBackground() {
		mIsAppInBackground = false;
	}

	public void setScreenOn() {
		// win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		win.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		win.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		win.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		win.addFlags(WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES);
		win.setFormat(PixelFormat.RGBX_8888);
	}

	public void setTouchModeSlidingMenu(boolean isScroll) {

	}

	public void displaySoftKeyBoard(InputMethodManager iMM, View view,
			boolean isShow) {
		if (isShow) {
			iMM.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		} else {
			iMM.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static String HOME_SCREEN = "home_screen";
	public static String ROOM_SCREEN = "room_screen";

	// values is temp to demo
	/*
	 * public static List<Room> mRooms= null; public static List<Area> mFloors =
	 * null;
	 */

	/*
	 * private List<Room> getAllRooms(){ List<Room> rooms = new
	 * ArrayList<Room>(); Room room = new Room(); int i= 0; room = new Room( i++
	 * , "Room" + i , 0, R.drawable.checkbox); rooms.add(room);
	 * mFloors.get(0).addRoomToFloor(room);
	 * 
	 * 
	 * room = new Room( i++ , "Room" + i , 0, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(0).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 0, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(0).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 1, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(1).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 1, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(1).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 4, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(4).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 4, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(4).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 4, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(4).addRoomToFloor(room);
	 * 
	 * room = new Room( i++ , "Room" + i , 4, R.drawable.checkbox);
	 * rooms.add(room); mFloors.get(4).addRoomToFloor(room);
	 * 
	 * return rooms; }
	 */
	/*
	 * private List<Area> getAllFloor(){ List<Area> floors = new
	 * ArrayList<Area>(); Area floor = null; for(int i = 0; i< 5; i++){ floor =
	 * new Area(i, "Area" + i , new ArrayList<Room>()); floors.add(floor); }
	 * return floors; }
	 */

	public void caculateScreenMetrics() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		mWidth = displaymetrics.widthPixels;
		mHeigth = displaymetrics.heightPixels;
	}

	public void caculateNotificationBarHeight() {
		mNotificationBarHeight = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			mNotificationBarHeight = getResources().getDimensionPixelSize(
					resourceId);
		}
	}

	public void onSlidingMenuClosed() {
		Log.d(TAG, "onSlidingMenuClosed");
		hideKeyBoard();
		// notify main menu screen
		if (mMainMenuScreen != null) {
			// mMainMenuScreen.onMenuClosed();
		}

		// notify current page
		if (viewManager.getCurrentPage() >= 0) {
			Fragment currentPage = viewManager.getPageAdapter().getItem(
					viewManager.getCurrentPage());
			if (currentPage instanceof PageChangeAware) {
				PageChangeAware a = (PageChangeAware) currentPage;
				a.onMenuClosed();
			}
		}
	}

	public void onSlidingMenuOpened() {
		hideKeyBoard();
		// notify main menu screen
		if (mMainMenuScreen != null) {
			// mMainMenuScreen.onMenuOpened();
		}

		// notify current page
		if (viewManager.getCurrentPage() >= 0) {
			Fragment currentPage = viewManager.getPageAdapter().getItem(
					viewManager.getCurrentPage());
			if (currentPage instanceof PageChangeAware) {
				PageChangeAware a = (PageChangeAware) currentPage;
				a.onMenuOpened();
			}
		}
	}

	public static void setClientEnabled(Context context, boolean enabled) {
		SharedPreferences sharedPreference = null;

		if (context != null) {
			sharedPreference = context.getSharedPreferences(PREF_NAME,
					Activity.MODE_PRIVATE);
		}

		if (sharedPreference != null) {
			Editor editor = sharedPreference.edit();
			editor.putBoolean(configManager.RA_CLIENT_ENABLED, enabled);
			editor.commit();
		}
	}

	public static boolean isClientEnabled(Context context) {
		boolean clientEnabled = true;
		SharedPreferences sharedPreference = null;

		if (context != null) {
			sharedPreference = context.getSharedPreferences(PREF_NAME,
					Activity.MODE_PRIVATE);
		}
		if (sharedPreference != null) {
			clientEnabled = sharedPreference.getBoolean(
					configManager.RA_CLIENT_ENABLED, true);
		}

		return clientEnabled;
	}

	private void completeProvisioningAndExit() {
		HomeCenter2Activity.setClientEnabled(this, true);
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((RegisterService.LocalBinder) service).getService();
			if (mService != null && mService.getUIEngine() != null) {
				if (mMainMenuScreen != null) {
					mMainMenuScreen.registerObserver();
				}
			}
			Log.e(TAG, "service connected");
			// reloadTabs
			initDevice();
			refresh();

			boolean clientEnabled = isClientEnabled(HomeCenter2Activity.this);
			if (!TextUtils.isEmpty(forcedSwitchScreenTag)) {
				tabIndex = viewManager.getScreenIndex(forcedSwitchScreenTag);
				tabIndex = tabIndex >= 0 ? tabIndex : 0;
				viewManager.setCurrentPage(tabIndex);
				forcedSwitchScreenTag = null;
			}
		}
	};

	private void initLibsAndServices() {
		if (RegisterService.getService() == null) {
			Log.d(TAG, "initLibsAndServices():: start service");
			Intent intent = new Intent(this, RegisterService.class);
			startService(intent);

			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		/**/
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
	}

	public void refresh() {
		uiEngine = RegisterService.getHomeCenterUIEngine();
		Log.d(TAG, "refresh::uiEngine is " + uiEngine);
		if (uiEngine != null) {
			uiEngine.onActivityResume(this);
			Intent intent = getIntent();
			boolean lauchedFromHistory = false;
			if (intent != null) {
				int flag = intent.getFlags();
				lauchedFromHistory = ((flag & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0);
				Log.d(TAG, "onResume(): lauchedFromHistory="
						+ lauchedFromHistory);
				if (lauchedFromHistory) {

				}
			}
			/*
			 * if (lauchedFromHistory == false) { initDevice(); }
			 */
			mIMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			Bundle forceOpenScreenBundle = null;
			if (uiEngine != null) {
				forceOpenScreenBundle = uiEngine.getForceOpenScreenBundle();
			}
			boolean isShowDefault = false;
			String showTabValue = null;
			int index = -1;
			if (forceOpenScreenBundle != null) {
				String tag = forceOpenScreenBundle
						.getString(configManager.SHOW_SCREEN);

				Log.d(TAG, "onResume:: get current::" + TextUtils.isEmpty(tag)
						+ " value::" + tag);
				if (!TextUtils.isEmpty(tag)) {

					ScreenEntry entry = ScreenManager.getInstance()
							.getScreenEntryByTag(tag);
					if (entry != null) {
						Log.d(TAG,
								"onResume:: get current screen::entry != null"
										+ getString(entry.getTitleId()));

					}
					isShowDefault = true;
				} else {
				}
			} else if (uiEngine.isLogined()) {

				isShowDefault = true;

				if (showTabValue != null) {
					forcedSwitchScreenTag = showTabValue;
				} else {
					index = tabIndex;
					if (index < 0) {
						index = 1;
					}
				}
				tabIndex = -1;

			}
			if (index >= 0) {
				MainScreen mainMenu = getMainMenu();
				viewManager.showView(index, false);
			} else if (isShowDefault == false) {
				// nganguyen comment to by pass login
//				Intent intent2 = new Intent(this, LoginActivity.class);
//				this.startActivityForResult(intent2,
//						configManager.PROVISION_REQUEST);
			}
		}
	}

	public MainScreen getMainMenu() {
		return mMainMenuScreen;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:

			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	private class SliderListener extends
			SlidingPaneLayout.SimplePanelSlideListener {

		@Override
		public void onPanelOpened(View panel) {
			Toast.makeText(panel.getContext(), "Opened", Toast.LENGTH_SHORT)
					.show();
			supportInvalidateOptionsMenu();
		}

		@Override
		public void onPanelClosed(View panel) {
			Toast.makeText(panel.getContext(), "Closed", Toast.LENGTH_SHORT)
					.show();
			supportInvalidateOptionsMenu();
		}

		@Override
		public void onPanelSlide(View view, float v) {
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void shutdownService(Activity activity) {
		// how can we do a 'graceful shutdown'?
		RegisterService service = RegisterService.getService();
		if (service != null) {

		}

		if (app_context != null) {
			HomeCenter2Activity app = (HomeCenter2Activity) app_context;
			app.disconnectFromService();
		}

		if (activity != null) {
			Log.i(TAG, "RADialer.shutdownService(): Stopping the service.\n");
			activity.stopService(new Intent(activity, RegisterService.class));

			// can we somehow wait until the service has fully stopped..?
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			this.finish();
		}
	}

	private void disconnectFromService() {
		if (mService != null && mService.getUIEngine() != null) {
			// mService.getUIEngine().removeObserver(this);
		}

		if (mConnection != null) {
			Log.d(TAG,
					"RADialer.disconnectFromService(): Unbinding from the service.\n");
			try {
				unbindService(mConnection);
			} catch (RuntimeException runEx) {
				Log.e(TAG,
						"RADialer.disconnectFromService(): RuntimeException during unbindService: "
								+ runEx.getStackTrace() + ".\n");
			}
			mConnection = null;
		}
	}

	@Override
	public void socketConnected(boolean isConnected) {
		Log.d(TAG, "socketconnected::" + isConnected);
		if (isConnected == true) {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.d(TAG, "socketConnected: ");
					// tvConnect.setText(R.string.disconnect);
				}
			});
		} else {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					/*
					 * DialogFragmentWrapper.showDialog(getFragmentManager(),
					 * this, configManager.DIALOG_FAIL_CONNECT_SOCKET);
					 */

					// tvConnect.setText(R.string.connect);
					Log.d(TAG, "socket is not connected");
				}
			});
		}
	}

	private HomeCenterUIEngine getUiEngine() {
		RegisterService service = RegisterService.getService();
		HomeCenterUIEngine uiEngine = (service == null) ? null : service
				.getUIEngine();
		return uiEngine;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		MainScreenPagerAdapter adapter = viewManager.getPagerAdapter();
		Fragment fragment = adapter.getItem(viewManager.getCurrentPage());
		if (fragment != null) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				// Is SideBar opened ? OR is fragment showed a part?
				if (this.getSlidingMenu().isMenuShowing()) {
					this.getSlidingMenu().clickHomeButton();
					return true;
				} else {
					if (viewManager.isBackStackEmpty()) {
						// should move to back
						moveTaskToBack(true);
						mIsAppInBackground = true;
						return true;
					} else {
						HomeCenter2Activity activity = (HomeCenter2Activity) fragment
								.getActivity();
						if (activity != null) {
							activity.switchToPreviousContentView(true);

						}

						return true;
					}
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	public void doExit(Activity activity) {

		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				uiEngine.closeSocket();
			}
		}

		if (activity != null) {
			shutdownService(activity);
			activity.finish();
		}
	}

	private void initDevice() {
		uiEngine = RegisterService.getHomeCenterUIEngine();
		List<Object> objects = XMLHelper.readFileXml();
		Log.d(TAG, "initDevice");
		House house = null;
		if (objects != null && objects.size() > 0) {
			house = uiEngine.getHouse();
			if (house == null) {
				house = new House(objects);
				uiEngine.setHouse(house);
			} else {
				house.setHouse(objects);
			}

		} else {
			house = new House();
			uiEngine.setHouse(house);
			objects = uiEngine.initAllDevices();
			if (objects != null && objects.size() > 0) {
				XMLHelper.createDeviceFileXml(objects);
				initDevice();
			}
		}
	}

	public void controlSpeech() {

		/*
		 * Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		 * intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		 * 
		 * try { startActivityForResult(intent, configManager.RESULT_SPEECH);
		 * //Log.e(TAG, "Speech Text : " + txtText.getText()); } catch
		 * (ActivityNotFoundException a) {
		 * Toast.makeText(this,"Ops! Your device doesn't support Speech to Text"
		 * , Toast.LENGTH_SHORT).show();
		 * 
		 * }
		 */
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean state = false;
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				boolean isStart = false;

				Fragment currentPage = viewManager.getPageAdapter().getItem(
						viewManager.getCurrentPage());
				if (currentPage instanceof MyDevicesScreen) {
					MyDevicesScreen deviceScreen = (MyDevicesScreen) currentPage;
					isStart = deviceScreen.mIsSpeaking;
				} else if (currentPage instanceof MyAudioSystemScreen) {
					MyAudioSystemScreen audioScreen = (MyAudioSystemScreen) currentPage;
					isStart = audioScreen.mIsSpeaking;
				}

				if (isStart) {
					state = !state;
				}

				while (state) {
					try {
						startActivityForResult(intent,
								configManager.RESULT_SPEECH);
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					currentPage = viewManager.getPageAdapter().getItem(
							viewManager.getCurrentPage());
					if (currentPage instanceof MyDevicesScreen) {
						MyDevicesScreen deviceScreen = (MyDevicesScreen) currentPage;
						isStart = deviceScreen.mIsSpeaking;
					} else if (currentPage instanceof MyAudioSystemScreen) {
						MyAudioSystemScreen audioScreen = (MyAudioSystemScreen) currentPage;
						isStart = audioScreen.mIsSpeaking;
					}

					if (!isStart) {
						state = !state;
					}

				}
			}
		});

		th.start();
	}

	String[] OnLight = {"allon buthead butthead buthat butit startall starton startown stockon muthead holdon onon onown onour bậthết bàihát bệnhh ",
			 "butthenmark butthenmall butthenmalt butthenmod butthennot butthenmore butdenmark butthenmode butthenmult butthatmod onone allone anwar R1 en1 onrun bậtđèn1 bệnhviện1 bệnhđen1 mắtđen1 mạchđiện1 batman1 bánđèn1 bạchđàn1 bệnhnhân1 đền1 mạchđèn1 bàiđến1 bậtloa1 bật1 loa1",
			 "butthenI mydadhi butthenhigh ontwo alltwo onto allto until undo alto o2 r2 en2 an2 m2 undo i'mtoo anto untold bậtđèn2 bệnhhay bệnhviện2 bệnhđen2 mắtđen2 bậtđènhay mạchđiện2 batman2 bánđèn2 bạchđàn2 đặttênhay bệnhnhân2 đền2 mạchđèn2 bàiđến2 bậtloa2 bậtloahay bệnhlà2 bật2 loa2",					
			 "butthenby butthenbye butthenbuy on3 onthree country all3 r3 en3 arm3 audrey 13 andre entre on20 i'mfree ontree bậtđèn3 bệnhviện3 bệnhđen3 mắtđen3 mạchđiện3 batman3 bánđèn3 bạchđàn3 bệnhnhân3 đền3 mạchđèn3 bàiđến3 bậtloa3 bật3 loa3",						   
			 "butinboth butthenvote butthenboth butthenbored butthenballs butthenbowl butthenboard butthenbull butdanbull butthenborn butthenball onfour on4 onfor onfar arefar onphone onfall allfall onfloor r4 en4 i'msore unfall i'mfar onfault bậtđèn4 bệnhviện4 bệnhđen4 mắtđen4 mạchđiện4 batman4 bánđèn4 bạchđàn4 bệnhnhân4 đền4 7đề4 mạchđèn4 bàiđến4 bậtloa4 bật4 loa4",						  
			 "butthatnumber buttheirnumber butthenumber mydadnam butthennumb butthatnumb onfive onfire onvine i'mfine a5 alpine 15 empire en5 i'mfine onfine onfri bậtđèn5 bệnhviện5 bệnhđen5 mắtđen5 mạchđiện5 batman5 bánđèn5 bạchđàn5 bệnhnhân5 đền5 mạchđèn5 bàiđến5 bậtloa5 bật5 loa5",						  
			 "butthenciao butthenfell butthentell butthentalk butthensaw butthensow butthensouth butandsow butdanceout onsix i'msick onsick onset onsec onsex auntsex allsex on6 unfit unsafe auntie answer onsite bậtđèn6 bệnhviện6 bệnhviệnsau bệnhđen6 bệnhđensau bánđènsau bánđènsao mắtđen6 đếnsau mạchđiện6 batman6 bánđènsau bánđèn6 bạchđàn6 bệnhnhân6 đền6 mạchđèn6 bàiđếnsau 7đềsau 7đề6 bậtloa6 bật6 loa6 loasao",						 
			 "butinvite butstandby butthenbite on7 onseven seventeen 17 i'mseven bậtđèn7 bệnhviện7 bệnhviện7a bệnhđen7 mắtđen7 mạchđiện7 batman7 bánđèn7 bạchđàn7 bệnhnhân7 đền7 mạchđèn7 bàiđến7 bậtloa7 bật7 loa7 loabãi loabài",
			 "butthentom butthentime butintime butdancetime oneight ona alleight all8 on8th onh bậtđèn8 bệnhviện8 bệnhđen8 mắtđen8 mạchđiện8 batman8 bánđèn8 bạchđàn8 bệnhnhân8 đền8 mạchđèn8 bàiđến8 bậtloa8 bật8 loa8",
			 "buttatdid butangie buttheng butnt butthenteen  butthengn buttheengine butthenshedid butingene butthentien butthenkid butthatkid butthenkeen butdancescene butthencheese buttheneat butthencheat butthencheated butthenteeth onnine online allnight oni onnight 49 on9 bậtđèn9 bệnhviện9 bệnhđen9 mắtđen9 mạchđiện9 batman9 bánđèn9 bạchđàn9 bậtđènpin bánđènpin dântrí bệnhnhân9 đền9 mạchđèn9 bàiđến9 bật9",
			 "butdanmalloy butthenmarie butthenmy butthenmuy butemily butthenmuray butdancemuray butthenmolloy butdancemolloy onten alltan auntanne all10 ontan onpan auntann on10 i'm10 fontaine content onfan ben10 bậtđèn10 bệnhviện10 bệnhđen10 mắtđen10 mạchđiện10 batman10 bánđèn10 bạchđàn10 ben đền10 bệnhnhân10 mạchđèn10 bàiđến10 bật10 ",
			 "offone 01 upone upline awkward op1 tắtđèn1 tắmbiển1 thanhtuyền1 tếtđến1 bánđèn1 tậpđoàn1 tắtlà1 tắtloa1 tắt1",
			 "off2 octo offtwo upto uptwo offto ops2 opto op2 tắtđèn2 tắmbiển2 thanhtuyền2 tếtđến2 bánđèn2 từđiểnhay tậpđoàn2 tắtlà2 tắtloa2 tắt2",
			 "off3 uptree up3 offthree ops3 op3 austri octree optree oaktree upgrade opry tắtđèn3 tắmbiển3 thanhtuyền3 tếtđến3 bánđèn3 tậpđoàn3 tắtlà3 tắtloa3 tắt3",
			 "off4 upfor offfour offfar opfox awful op4 ops4 offer opfer upfar offshore uptor howfar tắtđèn4 tắmbiển4 thanhtuyền4 tếtđến4 bánđèn4 đêmbuồn tậpđoàn4 tắtlà4 tắtloa4 tắt4",
			 "off5 offby offbye allfine offwifi offI optifine offphi oppi offfi alpha off fri offside upside opfine op5 tắtđèn5 tắtđènnam tắtđènlan tắmbiển5 thanhtuyền5 tếtđến5 bánđèn5 miềnnam tócđẹpnam tậpđoàn5 tắtlà5 tắtloa5 tắt5",
			 "off6 offset optic austin offsix opsec offsick offsync upset offsticks upsticks tắtđèn6 tắtđènbáo tắmbiển6 thanhtuyền6 tếtđến6 bánđèn6 tậptin6 tậpđoàn6 tắtlà6 tắtloa6 tắtloasau tắt6",
			 "off7 07 all7 alpha7 upseven up7 opt7 op7 of7 tắtđèn7 tắmbiển7 thanhtuyền7 tếtđến7 bánđèn7 tậpđoàn7 tắtlà7 tắtloa7 tắt7",
			 "off8 08 offeight offa upeight fa popa upa tắtđèn8 tătđènsáng tắmbiển8 thanhtuyền8 tếtđến8 bánđèn8 tậpđoàn8 tắtlà8 tắtloa8 toploa8 tắt8",
			 "off9 09 offnight offnice offline offnine upnight offside up9 op9 i'mnice upknight tắtđèn9 8đến9 tắmbiển9 thanhtuyền9 tếtđến9 bánđèn9 tậpđoàn9 tắt9",
			 "off10 optain obtain op10 optan offtan octane octan offpants uptown top10 opten ophan often offten ofpan of10 tắtđèn10 tắmbiển10 thanhtuyền10 tếtđến10 tếtđếnngười bánđèn10 tậpđoàn10 tắt10",
			 "offall upall tophat futhhead fathead stopit stopall stopon stopahead tắthết tóchát tạmhết xóahết "};	

	public int converText(String s) {
		s = s.replaceAll("\\s", "");
		s = s.toLowerCase();

		for (int i = 0; i < 22; i++) {
			if (OnLight[i].indexOf(s) != -1) {
				return i;
			}
		}
		return -1;
	}
}
