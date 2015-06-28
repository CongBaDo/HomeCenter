package com.HomeCenter2.ui.menuscreen;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.ListView;

import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.adapter.MainMenuAdapter;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenEntry;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MainScreen extends RADialerMainScreenAbstract implements
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener {

	public static final String TAG = "MainScreen";

	public MainScreen(int title, String tag, SlidingBaseActivity context) {
		super(MainScreen.class, title, tag, context);
	}

	Menu mMenu;
	MenuInflater mMenuInflater;
	Dialog mDialog = null;

	ListView mMainMenuLV;
	MainMenuAdapter mAdpater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int width = mContext.mWidth
				- getResources().getDimensionPixelSize(
						R.dimen.width_menu_when_open);
		int height = mContext.mHeigth;
		if (width > height) {
			width = mContext.mHeigth;
			height = mContext.mWidth;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "KeypadScreen.onConfigurationChanged(): newConfig="
				+ newConfig);
	}

	private void switchContentView(ScreenEntry entry) {
		if (mContext != null) {
			mContext.switchContentView(entry, false);
		}
	}

	private void refresh(boolean isLogined) {
		mContext.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				RegisterService service = RegisterService.getService();
				if (service != null) {
					HomeCenterUIEngine uiEngine = service.getUIEngine();
					if (uiEngine != null) {
						boolean isLogined = uiEngine.isLogined();
						/*
						 * btn1.setEnabled(isLogined); //
						 * btn2.setEnabled(isLogined);
						 * btn3.setEnabled(isLogined); //
						 * btn4.setEnabled(isLogined);
						 * btn5.setEnabled(isLogined);
						 * btn6.setEnabled(isLogined);
						 * btn7.setEnabled(isLogined);
						 * btn8.setEnabled(isLogined);
						 * btn9.setEnabled(isLogined);
						 * btn10.setEnabled(isLogined);
						 * btn11.setEnabled(isLogined);
						 * btn12.setEnabled(isLogined);
						 */
					}
				}
			}
		});
	}

	public void registerObserver() {
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				Log.d(TAG, "registerObserver has uiEngine is not null");
				// uiEngine.addConnectSocketObserver(this);
				refresh(uiEngine.getSocket().isTcpconnect());
			}
		}
	}

	@Override
	public void onDestroy() {
		/*
		 * RegisterService service = RegisterService.getService(); if (service
		 * == null) return; HomeCenterUIEngine uiEngine = service.getUIEngine();
		 * if (uiEngine != null) { uiEngine.removeConnectSocketObserver(this); }
		 */

		super.onDestroy();
	}

	/*
	 * private Dialog showFailConnectSocketDialog() { if (mDialog != null) {
	 * mDialog.dismiss(); } mDialog = new AlertDialog.Builder(mContext)
	 * .setTitle(R.string.warning) .setMessage(R.string.connect_socket_fail)
	 * .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss();
	 * tvConnect.setText(mContext.getString(R.string.connect)); } }).create();
	 * return mDialog; }
	 */
	@Override
	public Dialog onCreateDialog(int id) {
		/*
		 * switch (id) {
		 * 
		 * case configManager.DIALOG_FAIL_CONNECT_SOCKET: return
		 * showFailConnectSocketDialog(); }
		 */
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		
		Log.e(TAG, "onItemClick "+position+" "+ScreenManager.MENU_SCREEN_ENTRIES[position].getTag());
		if (mContext.isMenuShowing()) {
			mAdpater.updateCurrentPosition(position);

			ScreenEntry entry = (ScreenEntry) mAdpater.getItem(position);
			if (entry != null) {
				switchContentView(entry);
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu: " + isVisible());
		if (!isVisible())
			return;

		mMenu = menu;
		mMenuInflater = inflater;
		menu.clear();

		mActionBarV7.setTitle(R.string.app_name);

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setDisplayShowTitleEnabled(true);

		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);
		mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		inflater.inflate(R.menu.my_main_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void onShowOptionMenu() {
		Log.d(TAG, "onShowOptionMenu()");
		onCreateOptionsMenu(mMenu, mMenuInflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean isSelected = false;
		Log.d("TMT " + TAG, "onOptionsItemSelected" + isSelected);

		if (mContext.getSlidingMenu().getSlidingPaneLayout().isOpen()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				((SlidingBaseActivity) getActivity()).clickHomeMenu();
				isSelected = true;
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_menu_screen, container,
				false);
		mMainMenuLV = (ListView) view.findViewById(R.id.lvMainMenu);
		mAdpater = new MainMenuAdapter(mContext,
				ScreenManager.MENU_SCREEN_ENTRIES);
		mMainMenuLV.setAdapter(mAdpater);

		mMainMenuLV.setOnItemClickListener(this);
		mMainMenuLV.setSelected(true);
		registerObserver();
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

	public MainMenuAdapter getAdapter() {
		return mAdpater;
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub

	}

}
