package com.HomeCenter2.ui.slidingmenu.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Adapter;

import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.MainScreenPagerAdapter;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.contact.util.BitmapUtil;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.ui.adapter.MainMenuAdapter;
import com.HomeCenter2.ui.menuscreen.MainScreen;

public class ViewManager {

	private static final String TAG = "ViewManager";
	private UninterceptableViewPager mViewPager = null;
	FragmentManager mFragmentManager = null;
	private MainScreenPagerAdapter mPagerAdapter = null;

	private ArrayList<Fragment> mListFragments;

	HashMap<String, Integer> mViewMap;	
	public ArrayList<Fragment> mNonPersistentViewList;

	Integer mLastViewPosition;
	int mCurrentPage = -1;

	ActionBarActivity mContext;
	// Maintains a stack of views on the right-hand-side main content for
	// back-tracking
	private Stack<Integer> mBackStack = new Stack<Integer>();

	/**
	 * @return true if the back stack cannot be used to navigate back from the
	 *         current screen.
	 */
	public synchronized boolean isBackStackEmpty() {
		for (Integer page : mBackStack) {
			if (isBackAllowedToPage(page)) {
				return false;
			}
		}
		return true;
	}

	public ViewManager(ActionBarActivity context,
			FragmentManager fragmentManager) {
		mContext = context;

		mViewMap = new HashMap<String, Integer>();
		mNonPersistentViewList = new ArrayList<Fragment>();
		mLastViewPosition = 0;
		mListFragments = new ArrayList<Fragment>();

		initViewManager();

		mViewPager = new UninterceptableViewPager(context);

		mPagerAdapter = new MainScreenPagerAdapter(context, this,
				fragmentManager, mListFragments);

		mFragmentManager = fragmentManager;

		// bind the mDeviceTypeAdapter to viewpager
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setId("RVP".hashCode());
		mViewPager.setOffscreenPageLimit(1);
		disableInternalPageSliding();

	}

	private static Bitmap mSplashBM;

	public static Bitmap getSplashImage(Activity activity) {
		if (mSplashBM == null) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			int width = displaymetrics.widthPixels;
			mSplashBM = BitmapUtil.createScaledBitmap(activity.getResources(),
					R.drawable.splash_screen_portrait, width, height);
		}
		return mSplashBM;
	}

	private void disableInternalPageSliding() {

		mViewPager.beginFakeDrag();

		try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext());
			mScroller.set(mViewPager, scroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	public void refreshAllViews() {
		mPagerAdapter.notifyDataSetChanged();
	}

	public MainScreenPagerAdapter getPageAdapter() {
		return mPagerAdapter;
	}

	public boolean removeViewMapItem(String keyTag) {
		Set set = mViewMap.keySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Object o = itr.next();
			String key = o.toString();
			if (key.equals(keyTag)) {
				itr.remove();
				return true;
			}
		}
		return false;
	}

	public boolean updateViewMapKeyTag(String oldKeyTag, String newKeyTag) {
		Integer pos = mViewMap.get(oldKeyTag);
		if (pos != null) {
			removeViewMapItem(oldKeyTag);
			mViewMap.put(newKeyTag, pos);
			return true;
		}
		return false;
	}

	public void addView(Fragment fragment, String tag,
			boolean isValidatedAdapater, boolean isFragmentPersistent) {
		Integer pos = mViewMap.get(tag);
		if (pos == null) {
			mListFragments.add(fragment);
			mViewMap.put(tag, mLastViewPosition);
			mLastViewPosition++;
			;

			if (!isFragmentPersistent) {
				if (!mNonPersistentViewList.contains(fragment)) {
					mNonPersistentViewList.add(fragment);
				}
			}
		} else {
			/*
			 * if view/fragment is added already then ask if that view's
			 * layout/content need to be updated dynamically or it is persistent
			 * in view's layout/content
			 */

			if (!isFragmentPersistent) {
				if (!mNonPersistentViewList.contains(fragment)) {
					mNonPersistentViewList.add(fragment);
				}
				// this just only work if we mark the fragment need to be
				// refreshed in getItemPosition
				mListFragments.set(pos, fragment);
				isValidatedAdapater = true;
			} else {
				if (mNonPersistentViewList.contains(fragment)) {
					Log.d(TAG, "addView 180");
					mNonPersistentViewList.remove(fragment);
				}
			}
		}

		if (isValidatedAdapater) {
			refreshAllViews();
		}
	}

	public void showView(String tag, boolean isScroll) {
		showView(tag, isScroll, false);
	}

	public void showView(String tag, boolean isScroll, boolean addToBackStack) {
		Integer pos = mViewMap.get(tag);
		if (pos != null) {
			showView(pos, isScroll, addToBackStack);
		}
	}

	public void showView(Integer pos, boolean isScroll) {
		showView(pos, isScroll, false);
	}

	private boolean isBackAllowedToPage(int page) {
		if (page < 0) {
			Log.e(TAG,
					"ViewManager.isPagePermitted(): Invalid page index. page=\n"
							+ page);
			return false;
		}
		if (page >= mListFragments.size()) {
			Log.e(TAG,
					"ViewManager.isPagePermitted(): Invalid page index. page="
							+ page + "mListFragments.size="
							+ mListFragments.size());
			return false;
		}

		Fragment f = mListFragments.get(page);
		return true;
	}

	public void showView(Integer pos, boolean isScroll, boolean addToBackStack) {		
		Log.d(TAG, "showView::pos::" + pos + " , mCurrentPage::" + mCurrentPage);
		if (pos >= 0) {
			boolean isResumed = true;
			if (mCurrentPage != pos && mCurrentPage != -1) {
				// notify current page that it is being de-selected
				Fragment f = mListFragments.get(mCurrentPage);
				if (f instanceof PageChangeAware) {
					isResumed = f.isResumed();
					Log.d(TAG, "showView(): current=" + mCurrentPage
							+ " - isInLayout=" + f.isInLayout() + " isVisible="
							+ f.isVisible() + " isHidden=" + f.isHidden()
							+ " isRemoving=" + f.isRemoving() + " isResumed="
							+ isResumed);
					PageChangeAware a = (PageChangeAware) f;
					a.onPageDeselected();
				}
			}
			if (isScroll && !isResumed) {
				/**
				 * In case of fragment is not resumed, mViewPager.setCurrentItem
				 * will have no effect. InCallScreen will be frozen if call is
				 * disconnected and even currentItem is set to KEYPAD fragment
				 * is not resumed, In common, it is because fragment is under an
				 * another fragment or another application such as native dialer
				 * Note: smoothScroll = false as below, onScreenSlidingCompleted
				 * event will NOT be called.
				 * */
				mViewPager.setCurrentItem(pos, false);
			} else {
				mViewPager.setCurrentItem(pos, isScroll);
			}
			
			setCurrentPage(pos, addToBackStack);
			HomeCenter2 activity =(HomeCenter2)mContext;
			
			String tag = getTagCurrentPage();
			int positionInList = ScreenManager.getInstance().getPositionInListByTag(tag);
			Log.d(TAG, "mcurrentPage:" + mCurrentPage + " , tag: " + tag + " , pos: " + positionInList );
			HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
			if (uiEngine != null) {
				if (tag == ScreenManager.MY_DEVICE_TAG
						|| tag == ScreenManager.MY_SENSORS_TAB) {
					uiEngine.startTimerDevice();
					//uiEngine.stopTimerAuido();
				} else if(tag == ScreenManager.MY_AUDIO_TAB){
					uiEngine.stopTimerDevice();
					//uiEngine.startTimerAudio();
					uiEngine.getAudio();
				}else{
					uiEngine.stopTimerDevice();
					//uiEngine.stopTimerAuido();
				}
				

			}
			MainScreen mainScreen = activity.getMainMenu();
			if (mainScreen != null) {
				MainMenuAdapter adapter = mainScreen.getAdapter();
				if (adapter != null) {
					adapter.updateCurrentPosition(positionInList);
				}
			}
			
			Fragment f = mListFragments.get(pos);
			if (f instanceof PageChangeAware) {
				PageChangeAware a = (PageChangeAware) f;
				a.onPageSelected();
			}
			/*
			 * RegisterService service = RegisterService.getService(); if
			 * (service != null) { RADialerUIEngine engine =
			 * service.getUIEngine(); if (engine != null) { boolean
			 * isInCallScreenJustUpFromBg =
			 * engine.isInCallScreenJustUpFromBackground(); if
			 * (isInCallScreenJustUpFromBg) { if (!(f instanceof InCallScreen))
			 * { engine.setInCallScreenJustUpFromBackground(false); } } } }
			 */
		}
	}

	public synchronized boolean popBackStackView(boolean isScroll) {
		while (!mBackStack.empty()) {
			int prevPage = mBackStack.pop();
			Log.d(TAG, "ViewManager::pre::" + prevPage);
			if (isBackAllowedToPage(prevPage)) {
				Log.d(TAG, "popBackStackView true");
				showView(prevPage, isScroll, false);
				return true;
			}
			Log.d(TAG,
					"ViewManager.popBackStackView(): Back navigation to page not permitted, skipping this page. prevPage="
							+ prevPage);
		}
		return false;
	}

	public boolean popBackStackView() {
		return popBackStackView(true);
	}

	public ViewPager getViewPager() {
		return mViewPager;
	}

	public int getLastPosition() {
		return (int) mLastViewPosition;
	}

	public int getPositionByTag(String tag) {
		Object obj = mViewMap.get(tag);
		if (obj == null)
			return -1;

		return mViewMap.get(tag);
	}

	public int getScreenIndex(String tag) {
		Integer pos = mViewMap.get(tag);

		if (pos >= 0) {
			return pos;
		}
		return -1;
	}

	public void setCurrentPage(int id) {
		setCurrentPage(id, false);
	}

	public Fragment getPageByPosition(int pos) {
		return mListFragments.get(pos);
	}

	public synchronized void setCurrentPage(int id, boolean addToBackStack) {
		if (addToBackStack && mCurrentPage != -1) {
			mBackStack.push(mCurrentPage);
		}

		mCurrentPage = id;
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public String getTagCurrentPage() {
		for (String key : mViewMap.keySet()) {
			int currentPagePosition = mViewMap.get(key);
			if (currentPagePosition == mCurrentPage) {
				return key;
			}
		}

		return null;
	}

	public boolean isPageActive(Fragment page) {
		Fragment currentPage = getPageByPosition(mCurrentPage);
		return (currentPage != null && currentPage.equals(page));
	}

	public MainScreenPagerAdapter getPagerAdapter() {
		return mPagerAdapter;
	}

	public void setPagerAdapter(MainScreenPagerAdapter mPagerAdapter) {
		this.mPagerAdapter = mPagerAdapter;
	}

	public class UninterceptableViewPager extends ViewPager {

		public UninterceptableViewPager(Context context) {
			super(context);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return false;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			// Tell our parent to stop intercepting our events!s
			return false;
		}
	}

	public void initViewManager() {
		ScreenEntry entry = ScreenManager.getInstance().getScreenEntryByTagId(
				ScreenManager.MY_REMOTE_TAB_ID);
		if (entry != null) {
			Fragment fragment = (RADialerScreenAbstract) ScreenManager
					.getInstance().createScreenByPrimaryEntry(entry, mContext);
			addView(fragment, entry.getTag(), false,
					true);
		}

		entry = ScreenManager.getInstance().getScreenEntryByTagId(
				ScreenManager.MY_DEVICES_TAB_ID);
		if (entry != null) {
			Fragment fragment = (RADialerScreenAbstract) ScreenManager
					.getInstance().createScreenByPrimaryEntry(entry, mContext);
			addView(fragment, entry.getTag(), false,
					true);
		}
	}
}
