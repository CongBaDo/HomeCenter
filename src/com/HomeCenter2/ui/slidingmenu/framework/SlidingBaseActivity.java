package com.HomeCenter2.ui.slidingmenu.framework;

import java.util.Vector;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.ISlidingListener;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.ui.menuscreen.MainScreen;

public class SlidingBaseActivity extends ActionBarActivity implements
		OnPageChangeListener {

	private static String TAG = "SlidingBaseActivity";
	SlidingBase mSlidingMenu;
	private ActionBarDrawerToggle mDrawerToggle;
	protected MainScreen mMainMenuScreen;
	protected InputMethodManager mIMethodManager;
	Vector<ISlidingListener> mSlidingObserver = new Vector<ISlidingListener>();

	private View mOverlayView;
	private int mTitleRes;
	protected ListFragment mFrag;

	ViewManager mViewManager;
	SlidingPaneLayout paneLayout = null;
	View mPopUpKeypad;

	private OnPageScrolledCompleteListener mOnPageScrolledCompleteListener;
	private boolean mTablet = true;
	private ViewGroup mActionBar = null;

	public SlidingBaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTablet(isTabletDevice(this));
		// setTablet(true);

		caculateScreenMetrics();
		/*
		 * if(mTablet == true){ paneLayout = (SlidingPaneLayout)
		 * getLayoutInflater() .inflate(R.layout.sliding_main_tablet, null);
		 * }else{ paneLayout = (SlidingPaneLayout) getLayoutInflater()
		 * .inflate(R.layout.sliding_main, null); }
		 */
		paneLayout = (SlidingPaneLayout) getLayoutInflater().inflate(
				R.layout.sliding_main, null);
		setContentView(paneLayout);

		paneLayout.post(new Runnable() {
			@Override
			public void run() {
				// moveActionBarToSlidingMenu();
			}
		});

		mIMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		mViewManager = null;
		initializeViewManager(this);

		mSlidingMenu = new SlidingBase(this);
		mSlidingMenu.setSlidingPaneLayout(paneLayout);
		initSlidingMenu();

		setOnPageScrolledCompleteListener((OnPageScrolledCompleteListener) this);

		// setContent(mViewManager.getViewPager());

		mPopup = new PopupWindow(this);

		/*
		 * if (isTablet()) { ScreenEntry entry = new
		 * ScreenEntry(ScreenManager.CONTACTS_GROUP_ID,
		 * ScreenManager.FAVORITES_TAB_ID, R.string.tab_bar_title_favorites,
		 * R.drawable.sidebar_favorites_icon, FavoritesScreen.class); Fragment
		 * fragment = (RADialerScreenAbstract) ScreenManager
		 * .getInstance().createScreenByPrimaryEntry(entry, this);
		 * 
		 * Fragment fragment = new TempFragment(); mPopUpKeypad =
		 * this.getLayoutInflater().inflate(R.layout.popup_keypad, null, false);
		 * FrameLayout layout = (FrameLayout) mPopUpKeypad
		 * .findViewById(R.id.popupKeypad);
		 * getSupportFragmentManager().beginTransaction().replace(
		 * layout.getId(), fragment); }
		 */
	}

	public void initializeViewManager(SlidingBaseActivity context) {
		// initialize the ViewManager instance
		mViewManager = new ViewManager(context,
				this.getSupportFragmentManager());
	}

	public ViewManager getViewManager() {
		return mViewManager;
	}

	public SlidingBase getSlidingMenu() {
		return mSlidingMenu;
	}

	public void setSlidingMenu(SlidingBase mSlidingView) {
		this.mSlidingMenu = mSlidingView;
	}

	public void onSlidingMenuClosed() {
		hideKeyBoard();
		// notify current page
		if (mViewManager.getCurrentPage() >= 0) {
			Fragment currentPage = mViewManager.getPageAdapter().getItem(
					mViewManager.getCurrentPage());
			if (currentPage instanceof PageChangeAware) {
				PageChangeAware a = (PageChangeAware) currentPage;
				a.onMenuClosed();
			}
		}
	}

	public void onSlidingMenuOpened() {
		hideKeyBoard();
		// notify current page
		if (mViewManager.getCurrentPage() >= 0) {
			Fragment currentPage = mViewManager.getPageAdapter().getItem(
					mViewManager.getCurrentPage());
			if (currentPage instanceof PageChangeAware) {
				PageChangeAware a = (PageChangeAware) currentPage;
				a.onMenuOpened();
			}
		}
	}

	public void hideKeyBoard() {
		/*
		 * Should we consider a fragment where we will hide keyboard!!!
		 */
		if (mViewManager.getViewPager() == null) {
			Log.d(TAG,
					"HomeCenter2.hideKeyBoard(): viewManager.getViewPager() == null!!!, cancel hideKeyBoard\n");
			return;
		}

		String currentPageTag = mViewManager.getTagCurrentPage();
		Log.d(TAG, "HomeCenter2.hideKeyBoard(): currentPageTag="
				+ currentPageTag + "\n");
		displaySoftKeyBoard(mIMethodManager, mViewManager.getViewPager(), false);
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

	/**
	 * Switch main screen content to the fragment
	 * 
	 * @param fragment
	 *            The fragment will be switched
	 * @param fragmentTAG
	 *            The fragment ID
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 */
	public void switchContentView(Fragment fragment, String fragmentTAG,
			boolean isScroll) {
		String currentPageTag = mViewManager.getTagCurrentPage();
		// Check the curent page
		if (currentPageTag.equals(fragmentTAG)) {
			mSlidingMenu.showContent();
			return;
		}
		if (fragment != null) {
			mViewManager.addView(fragment, fragmentTAG, true, true);
			mViewManager.showView(fragmentTAG, isScroll, true); // addToBackStack
																// = true as
																// default for
																// adding a new
																// fragment
			mSlidingMenu.showContent();
		}
	}

	/**
	 * Switch main screen content to the screen with specific position in
	 * ScreenManager
	 * 
	 * @param position
	 *            The position of the screen entry in ScreenManager
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 */
	public void switchContentView(int position, boolean isScroll) {
		ScreenEntry entry = ScreenManager.getInstance()
				.getScreenEntryByPosition(position);

		if (entry != null) {
			switchContentView(entry, isScroll);
		}
	}

	/**
	 * Switch main screen content to ScreenEntry entry
	 * 
	 * @param entry
	 *            The screen entry that hold the fragment to switch
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 */
	Fragment framgnet1 = null;

	public void switchContentView(ScreenEntry entry, boolean isScroll) {
		String fragmentTag = entry.getTag();
		String currentPageTag = mViewManager.getTagCurrentPage();
		// Check the curent page

		if (currentPageTag != null && currentPageTag.equals(fragmentTag)) {
			mSlidingMenu.showContent();
			return;
		}
		int pos = mViewManager.getPositionByTag(fragmentTag);
		Fragment fragment = null;
		if (pos == -1) {
			fragment = (RADialerScreenAbstract) ScreenManager.getInstance()
					.createScreenByPrimaryEntry(entry, this);

			switchContentView((RADialerScreenAbstract) fragment,
					entry.getTag(), isScroll, true);
			// addToBackStack = true as default for adding a new fragment
		} else {
			fragment = mViewManager.getPageByPosition(pos);
			Log.d(TAG, "showView 295::" + pos + ", page: " + (fragment == null));
			mViewManager.showView(fragmentTag, isScroll, true); // addToBackStack
																// = true as
																// default for
																// adding a new
																// fragment
			mSlidingMenu.showContent();

		}
	}

	public boolean switchToPreviousContentView(boolean isScroll) {
		return mViewManager.popBackStackView(isScroll);
	}

	public boolean switchToPreviousContentView() {
		return mViewManager.popBackStackView();
	}

	public void switchContentView(ScreenEntry entry, boolean isScroll,
			boolean addToBackStack, boolean isFragmentPersistent, Bundle bundle) {
		String tag = entry.getTag();
		String currentPageTag = mViewManager.getTagCurrentPage();
		Log.d(TAG, "switchContentView::440:: id : " + tag + " , current: "
				+ currentPageTag);
		// Check the curent page
		if (currentPageTag != null && currentPageTag.equals(tag)) {
			getSlidingMenu().showContent();
			return;
		}
		int pos = mViewManager.getPositionByTag(tag);
		/*
		 * if (pos == -1) { if (tag.equals(ScreenManager.ROOM_IN_HOUSE_TAG)) {
		 * DevicesInRoomScreen fragment =
		 * DevicesInRoomScreen.initializeDetailRoomScreen( bundle, -1,
		 * (SlidingBaseActivity) this); switchContentView(fragment,
		 * ScreenManager.ROOM_IN_HOUSE_TAG, true, true, false); } }
		 */
	}

	/**
	 * Switch main screen content to the fragment RADialerScreenAbstract type
	 * without providing the fragment tag. The fragment tag/id will be get
	 * automatically by its title id. This method will set the fragment as
	 * persistent type as default
	 * 
	 * @param fragment
	 *            The fragment will be switched.
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 * @param addToBackStack
	 *            Put the fragment into the fragment stack for
	 *            switchToPreviousContentView()
	 */
	public void switchContentView(RADialerScreenAbstract fragment,
			boolean isScroll, boolean addToBackStack) {

		switchContentView(fragment, isScroll, addToBackStack, true);
	}

	/**
	 * Switch main screen content to the fragment RADialerScreenAbstract type
	 * without providing the fragment tag. The fragment tag/id will be get
	 * automatically by its title id.
	 * 
	 * @param fragment
	 *            The fragment will be switched.
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 * @param addToBackStack
	 *            Put the fragment into the fragment stack for
	 *            switchToPreviousContentView()
	 * @param isFragmentPersistent
	 *            Mark the fragment is persistent or not. If the fragment is not
	 *            persistent mean that its content will be changed or updated
	 *            later for eg. detail contact
	 */
	public void switchContentView(RADialerScreenAbstract fragment,
			boolean isScroll, boolean addToBackStack,
			boolean isFragmentPersistent) {

		if (fragment != null) {
			String fragmentTag = fragment.getFragmentTag();
			mViewManager.addView(fragment, fragmentTag, true,
					isFragmentPersistent);
			mViewManager.showView(fragmentTag, isScroll, addToBackStack);
			mSlidingMenu.showContent();
		}
	}

	/**
	 * Switch main screen content to the fragment with default set the fragment
	 * is persistent one
	 * 
	 * @param fragment
	 *            The fragment will be switched
	 * @param fragmentId
	 *            The fragment ID
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 * @param addToBackStack
	 *            Put the fragment into the fragment stack for
	 *            switchToPreviousContentView()
	 */
	public void switchContentView(RADialerScreenAbstract fragment,
			String fragmentTag, boolean isScroll, boolean addToBackStack) {

		switchContentView(fragment, fragmentTag, isScroll, addToBackStack, true);
	}

	/**
	 * Switch main screen content to the fragment
	 * 
	 * @param fragment
	 *            The fragment will be switched
	 * @param fragmentId
	 *            The fragment ID
	 * @param isScroll
	 *            Enable or disable the fragment animation internally. If we
	 *            call the switchContentView from the menu screen, this
	 *            parameter should be set to false If we call the
	 *            switchContentView from the main screen, this parameter should
	 *            be set to true if we want to see fragment transition
	 *            animation.
	 * @param addToBackStack
	 *            Put the fragment into the fragment stack for
	 *            switchToPreviousContentView()
	 * @param isFragmentPersistent
	 *            Mark the fragment is persistent or not. If the fragment is not
	 *            persistent mean that its content will be changed or updated
	 *            later for eg. detail contact
	 */
	public void switchContentView(Fragment fragment, String fragmentTag,
			boolean isScroll, boolean addToBackStack,
			boolean isFragmentPersistent) {
		if (fragment != null) {
			mViewManager.addView(fragment, fragmentTag, true,
					isFragmentPersistent);
			mViewManager.showView(fragmentTag, isScroll, addToBackStack);
			mSlidingMenu.showContent();
		}
	}

	public void addSlidingObserver(ISlidingListener observer) {
		mSlidingObserver.remove(observer);
		mSlidingObserver.add(observer);
	}

	public void removeSlidingObserver(ISlidingListener observer) {
		mSlidingObserver.remove(observer);
	}

	private void notifyCloseMenu() {
		int size = mSlidingObserver.size();
		for (int i = 0; i < size; i++) {
			mSlidingObserver.elementAt(i).onCloseMenu();
		}
	}

	public void clickHomeMenu() {
		mSlidingMenu.clickHomeButton();
	}

	/*
	 * public void setOverlayContentView(View view) {
	 * mViewAbove.setOverlayView(view); }
	 * 
	 * public void setOverlayContent(View view) {
	 * mViewAbove.setOverlayContent(view); }
	 * 
	 * public void removeOverlayView() { mViewAbove.removeOverlayView(); }
	 * 
	 * public void setContent(View v) { // there is view changing if
	 * (mOverlayView != null) { this.removeView(mOverlayView); mOverlayContent =
	 * null; mOverlayView = null; } if (mContentView != null) {
	 * this.removeView(mContentView); } mContentView = v; addView(mContentView);
	 * }
	 * 
	 * public void setOverlayView(View v) { if (mOverlayView != null) {
	 * this.removeView(mOverlayView); mOverlayContent = null; mOverlayView =
	 * null; } mOverlayView = v; addView(mOverlayView); }
	 * 
	 * public void removeOverlayView() { if (mOverlayView != null) {
	 * this.removeView(mOverlayView); } mOverlayContent = null; mOverlayView =
	 * null; }
	 */

	public void setOnPageScrolledCompleteListener(
			OnPageScrolledCompleteListener mOnPageScrolledCompleteListener) {
		this.mOnPageScrolledCompleteListener = mOnPageScrolledCompleteListener;
	}

	public OnPageScrolledCompleteListener getOnPageScrolledCompleteListener() {
		return mOnPageScrolledCompleteListener;
	}

	public void initSlidingMenu() {

		SlidingPaneLayout paneLayout = mSlidingMenu.getSlidingPaneLayout();
		mSlidingMenu.setKeepContentView((LinearLayout) paneLayout
				.findViewById(R.id.actionbar_content_pane));
		mSlidingMenu.setContentPane((FrameLayout) paneLayout
				.findViewById(R.id.content_pane));
		mSlidingMenu.setContent((FrameLayout) paneLayout
				.findViewById(R.id.content_view));
		mSlidingMenu.setMenuPane((FrameLayout) paneLayout
				.findViewById(R.id.home_pane));
		mSlidingMenu.setShadow((View) paneLayout
				.findViewById(R.id.shadow_content));

		paneLayout.setSliderFadeColor(Color.TRANSPARENT);
		paneLayout.setPanelSlideListener(new SliderListener());

	}

	private class SliderListener extends
			SlidingPaneLayout.SimplePanelSlideListener {

		@Override
		public void onPanelOpened(View panel) {
			Log.d(TAG, "onPanelOpened");
			supportInvalidateOptionsMenu(); // creates call to
			// onPrepareOptionsMenu()
			onSlidingMenuOpened();
			updateShadow();
			mMainMenuScreen.onShowOptionMenu();
		}

		@Override
		public void onPanelClosed(View panel) {
			Log.d(TAG, "onPanelClose");
			supportInvalidateOptionsMenu(); // creates call to
			// onPrepareOptionsMenu()
			onSlidingMenuClosed();
			updateShadow();
			notifyCloseMenu();
			RegisterService service = RegisterService.getService();

			if (service != null) {
				Log.d(TAG, "onPanelClose service " + service);

				HomeCenterUIEngine uiEngine = service.getUIEngine();
				uiEngine.notifyPageScrolledObserver(true);
			}

			// mOnPageScrolledCompleteListener.onViewScrolledComplete();

		}

		@Override
		public void onPanelSlide(View view, float v) {
		}
	}

	private void moveActionBarToSlidingMenu() {
		Log.d("TAG", "moveActionBarToSlidingMenu");
		TypedArray a = this.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();

		// move everything into the SlidingMenu
		ViewGroup decor = (ViewGroup) this.getWindow().getDecorView();
		mActionBar = (ViewGroup) decor.getChildAt(0);
		// save ActionBar themes that have transparent assets
		mActionBar.setBackgroundResource(background);
		decor.removeView(mActionBar);

		SlidingPaneLayout slidingPaneLayout = getSlidingMenu()
				.getSlidingPaneLayout();

		ViewGroup viewParent = (ViewGroup) slidingPaneLayout.getParent();
		viewParent.removeView(slidingPaneLayout);
		decor.addView(slidingPaneLayout);

		LinearLayout layoutContent = getSlidingMenu().getKeepContentView();
		layoutContent.addView(mActionBar);
		updateHeigthActionBar();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d("TAG", "onPostCreate::" + mViewManager);
		getSlidingMenu().setContent(mViewManager.getViewPager());
		mMainMenuScreen = new MainScreen(R.string.app_name,
				ScreenManager.MAIN_MENU_TAG, this);
		getSlidingMenu().setMenu(mMainMenuScreen);
		// hack - init some initial main screen
		// viewManager.initViewPager();
		mViewManager.getViewPager().setOnPageChangeListener(this);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public int getActionBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("action_bar_container",
				"id", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private boolean isTabletDevice(Context activityContext) {
		boolean device_large = ((activityContext.getResources()
				.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		if (device_large) {
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
					|| metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
					|| metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
					|| metrics.densityDpi == DisplayMetrics.DENSITY_TV
					|| metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
				Log.d(TAG, "isTabletDevice true");
				return true;
			}
		}
		Log.d(TAG, "isTabletDevice false");
		return false;
		/*
		 * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) return
		 * true;
		 * 
		 * return false;
		 */
	}

	public boolean isTablet() {
		return mTablet;
	}

	public void setTablet(boolean mTablet) {
		this.mTablet = mTablet;
	}

	private PopupWindow mPopup;

	private boolean isPopUpShowing() {
		return mPopup.isShowing();
	}

	private void dismissPopupMenu() {
		if (mPopup != null && mPopup.isShowing() == true)
			mPopup.dismiss();
	}

	public int mWidth;
	public int mHeigth;

	public void caculateScreenMetrics() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		mWidth = displaymetrics.widthPixels;
		mHeigth = displaymetrics.heightPixels;
	}

	private void updateHeigthActionBar() {
		if (mActionBar == null) {
			return;
		}
		int heightNotificationBar = getStatusBarHeight();
		int heightActionBar = 0;

		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
			heightActionBar = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}

		Log.d("TAG", "heightnotification:" + heightNotificationBar
				+ " , height actionBar :" + heightActionBar
				+ " , height actionBar :");

		mActionBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(heightNotificationBar + heightActionBar)));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		updateHeigthActionBar();
		Log.d(TAG, "onConfigurationChanged");
		/*
		 * if (isTablet()) { if (newConfig.orientation ==
		 * Configuration.ORIENTATION_LANDSCAPE) {
		 * mSlidingMenu.getShadow().setVisibility(View.VISIBLE); } else if
		 * (mSlidingMenu.getSlidingPaneLayout().isOpen()) {
		 * mSlidingMenu.getShadow().setVisibility(View.VISIBLE); } else {
		 * mSlidingMenu.getShadow().setVisibility(View.GONE); } }
		 */
	}

	public void updateShadow() {
		// if (mSlidingMenu.getSlidingPaneLayout().isOpen()) {
		// mSlidingMenu.getShadow().setVisibility(View.VISIBLE);
		// } else {
		// mSlidingMenu.getShadow().setVisibility(View.GONE);
		// }
	}

	public void panelChangedNotSlide() {
		Log.d(TAG, "panelChangedNotSlide");
		supportInvalidateOptionsMenu();
		onSlidingMenuOpened();

		onSlidingMenuClosed();
		notifyCloseMenu();
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			uiEngine.notifyPageScrolledObserver(true);
		}
	}

	public int getMenuActionBarHeight() {
		return mActionBar.getHeight();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean isMenuShowing() {
		return mSlidingMenu.isMenuShowing();
	}

}
