package com.HomeCenter2.ui.mainS;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.adapter.MyParameterAdapter;
import com.HomeCenter2.ui.listener.ConfigListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.mainscreen.ParameterConfigDevice;
import com.HomeCenter2.ui.mainscreen.ParameterConfigRoom;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyParametersScreen extends RADialerMainScreenAbstract implements
		XMLListener, TabHost.OnTabChangeListener,
		ViewPager.OnPageChangeListener, ConfigListener , DialogFragmentWrapper.OnCreateDialogFragmentListener{

	private static final String TAG = "TMT MyParametersScreen";

	LayoutInflater mInflater;
	private TabHost mTabHost;
	private ViewPager mPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private MyParameterAdapter mPagerAdapter;
	private Bundle mBundle = null;
	int isTypeParameter = configManager.CONFIG_DEVICE;
	private Dialog mDialog = null;
	
	private static final String TAG_CONFIG = "Parameter_Config";
	private static final String TAG_ROOM = "Parameter_Room";
	private int mCurrentTab = 0;

	public MyParametersScreen(int title, String tag, SlidingBaseActivity context) {
		super(MyParametersScreen.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = getLayoutInflater(savedInstanceState);
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null) {
			return;
		}
		uiEngine.addConfigObserver(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.my_parameters_screen,
				container, false);
		mPager = (ViewPager) view.findViewById(R.id.viewpager);
		mPager.setOffscreenPageLimit(1);
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mBundle = savedInstanceState;
		initialiseTabHost(savedInstanceState);

		/*
		 * if (savedInstanceState != null) {
		 * mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //
		 * set }
		 */
		mCurrentTab = 0;
		mTabHost.setCurrentTab(mCurrentTab);
		// Intialise ViewPager
		// this.initialiseViewPager();

		return view;
	}

	/*
	 * protected void onSaveInstanceState(Bundle outState) {
	 * outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab
	 * selected super.onSaveInstanceState(outState); }
	 */

	ParameterConfigDevice mDeviceFragment = null;
	ParameterConfigRoom mRoomFragment = null;
	List<Fragment> mFragments = null;

	private void initialiseViewPager() {
		Log.d(TAG, "initialiseViewPager");
		if (mFragments == null) {
			mFragments = new Vector<Fragment>();

			mDeviceFragment = new ParameterConfigDevice(R.string.device,
					ScreenManager.PARAMETER_CONFIG_TAG, mContext);
			mDeviceFragment.setFragmentTag(TAG_CONFIG);
			mFragments.add(mDeviceFragment);

			mRoomFragment = new ParameterConfigRoom(R.string.room,
					ScreenManager.PARAMETER_ROOM_TAG, mContext);
			mRoomFragment.setFragmentTag(TAG_ROOM);
			mFragments.add(mRoomFragment);

			Log.d(TAG, "initialiseViewPager: show adapter");
			this.mPagerAdapter = new MyParameterAdapter(mContext,
					getFragmentManager(), mFragments);
			this.mPager.setAdapter(this.mPagerAdapter);
			this.mPager.setOnPageChangeListener(this);
			this.mPager.setCurrentItem(mCurrentTab);
			mTabHost.setCurrentTab(mCurrentTab);
		}

	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost.setup();
		TabInfo tabInfo = null;
		AddTab(mContext, this.mTabHost, this.mTabHost.newTabSpec("TabP1")
				.setIndicator("Config Device"), (tabInfo = new TabInfo("TabP1",
				ParameterConfigDevice.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		AddTab(mContext, this.mTabHost, this.mTabHost.newTabSpec("TabP2")
				.setIndicator("Config Room"), (tabInfo = new TabInfo("TabP2",
				ParameterConfigRoom.class, args)));
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

	/*
	 * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater
	 * inflater) { menu.clear(); mActionBarV7.setHomeButtonEnabled(true);
	 * mActionBarV7.setDisplayHomeAsUpEnabled(true);
	 * mActionBarV7.setTitle(mTitleId);
	 * mActionBarV7.setDisplayShowTitleEnabled(true);
	 * 
	 * mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
	 * ActionBar.DISPLAY_SHOW_TITLE);
	 * 
	 * inflater.inflate(R.menu.my_parameter_menu, menu);
	 * super.onCreateOptionsMenu(menu, inflater); }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { if
	 * (isVisible()) { switch (item.getItemId()) { case android.R.id.home:
	 * onClickHome(); break; } } return true; }
	 */

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
		mCurrentTab = this.mTabHost.getCurrentTab();
		this.mPager.setCurrentItem(mCurrentTab);
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
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
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
	public void savedXML() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null)
			return;
		uiEngine.removeConfigObserver(this);
				
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

		inflater.inflate(R.menu.my_parameter_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				break;
			case R.id.save_menu:
				Log.d(TAG, "onOptionsItemSelected");
				switch (mCurrentTab) {
				case 0:
					if (mDeviceFragment != null
							&& mDeviceFragment.isVisible() == true) {
						((ParameterConfigDevice) mDeviceFragment).saveConfig();
					}
					break;
				case 1:
					if (mRoomFragment != null
							&& mRoomFragment.isVisible() == true) {
						((ParameterConfigRoom) mRoomFragment).save();
						((ParameterConfigRoom) mRoomFragment).saveConfig();
					}
					break;

				default:
					break;
				}
				break;
			}
		}
		return true;
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {

	}

	@Override
	public void onPageSelected() {
		Log.d(TAG, "onPageSelected");
		this.initialiseViewPager();

	}

	@Override
	public void onPageDeselected() {
		Log.d(TAG, "onPageDeselected");
		if (mFragments != null) {
			mPager.setAdapter(null);
			mPagerAdapter = null;
			mFragments.clear();
			if(this.getFragmentManager()== null){
				return; 
			}
			Fragment frConfig = this.getFragmentManager().findFragmentByTag(
					TAG_CONFIG);
			if (frConfig != null) {
				this.getFragmentManager().beginTransaction().remove(frConfig)
						.commit();
			}
			Fragment frRoom = this.getFragmentManager().findFragmentByTag(
					TAG_ROOM);
			if (frRoom != null) {
				this.getFragmentManager().beginTransaction().remove(frRoom)
						.commit();
			}
			mFragments = null;
		}
	}
	
	@Override
	public void configSaved(boolean result) {
		if(result){
			DialogFragmentWrapper.showDialog(
					getFragmentManager(),
					this,
					configManager.DIALOG_SAVE_CONFIG_SUCCESS);
		}else{
			DialogFragmentWrapper.showDialog(
					getFragmentManager(),
					this,
					configManager.DIALOG_SAVE_CONFIG_FAIL);			
		}
		
	}
	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_SAVE_CONFIG_SUCCESS:
			return showSaveConfigSuccess(true);
		case configManager.DIALOG_SAVE_CONFIG_FAIL:
			return showSaveConfigSuccess(false);
		default:
			break;
		}
		return null;
			
		}
	private Dialog showSaveConfigSuccess(boolean isSaveSuccess) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		int message = (isSaveSuccess == true ? R.string.save_config_success
				: R.string.save_config_fail);
		mDialog = new AlertDialog.Builder(mContext)
				.setTitle(R.string.warning)
				.setMessage(message)
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

}
