package com.HomeCenter2.ui.slidingmenu.framework;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.R;
import com.HomeCenter2.ui.slidingmenu.framework.ActionBar.IActionBarListener;

public abstract class RADialerScreenAbstract extends DialogFragment implements
		IActionBarListener, PageChangeAware {

	private final static String TAG = "RADialerScreenAbstract";
	/**
	 * Name of the shared preferences file to use for storing suppressed
	 * overlays.
	 */
	public final static String SHARED_PREFS_NAME = "RADialerSharedPref";

	/**
	 * Shared Settings Key for storing 'Don't show again' preference values.
	 */
	private final static String RA_I_HELP_OVERLAYS_HIDDEN = "HelpOverlaysHidden";

	// bit index for suppressing individual screens when storing a single
	// settings integer
	public final static int HELP_SCREEN_INDEX_KEYPAD = 1;
	public final static int HELP_SCREEN_INDEX_MAIN_MENU = 2;
	// public final static int HELP_SCREEN_INDEX_VOICEMAIL = 3;
	public final static int HELP_SCREEN_INDEX_BUDDIES = 4;

	protected static final int POP_UP_LISTVIEW_ROW_WIDTH = 250;// in dip
	protected static final int POP_UP_LISTVIEW_ROW_HEIGHT = 50;// in dip

	public static String HELP_SCREEN_STR(int helpScreenIndex) {
		switch (helpScreenIndex) {
		case HELP_SCREEN_INDEX_KEYPAD:
			return "HELP_SCREEN_INDEX_KEYPAD";
		case HELP_SCREEN_INDEX_MAIN_MENU:
			return "HELP_SCREEN_INDEX_MAIN_MENU";
			// case HELP_SCREEN_INDEX_VOICEMAIL: return
			// "HELP_SCREEN_INDEX_VOICEMAIL";
		case HELP_SCREEN_INDEX_BUDDIES:
			return "HELP_SCREEN_INDEX_BUDDIES";
		default:
			return "(unrecognized)";
		}
	}

	protected android.support.v7.app.ActionBar mActionBarV7;

	protected PopupWindow mPopup;
	protected HomeCenter2 mContext;
	protected ActionBar mActionBar;
	protected View helpOverlayContainer;
	protected ListView mVideosPopup;
	private boolean isDialog = false;

	protected Class<?> mClassName;
	protected int mTitleId;
	protected String mTag;

	protected int mWidth;
	protected int mHeigth;
	protected int mNotificationBarHeight;
	private boolean isDeleteFinished = false;

	protected boolean isMenuClosed = true;
	private OnGlobalLayoutListener mGlobalLayoutListener;

	public abstract void loadHeader();

	public abstract void setContentMenu();

	public RADialerScreenAbstract(Class<?> clas, int title, String tag,
			SlidingBaseActivity context) {
		mClassName = clas;
		mTitleId = title;
		mContext = (HomeCenter2) context;
		mActionBar = null;
		mTag = tag;
	}

	public HomeCenter2 getRadialerContext() {
		return mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.loadHeader();
		if (mActionBar != null)
			setFragmentActionBar(mActionBar);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void createHeader() {
		Log.d("TAG", "createHeader");
		if (!isDialog) {
			mActionBar = new ActionBar(
					mClassName.getSimpleName().toLowerCase(), mTitleId);
			mActionBar.mListener = this;
		}
	}

	protected void createHeader(int resourceId) {
		Log.d("TAG", "createHeader");
		if (!isDialog) {
			mActionBar = new ActionBar(resourceId, mTitleId);
			mActionBar.mListener = this;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPopup = new PopupWindow(getActivity());
		caculateScreenMetrics();
		caculateNotificationBarHeight();
		mActionBarV7 = mContext.getSupportActionBar();
		/*if (mActionBarV7 != null) {
			ColorDrawable colorDrawable = new ColorDrawable(
					Color.parseColor("#0C2F45"));
			mActionBarV7.setBackgroundDrawable(colorDrawable);
		}*/
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreated() {
		setContentMenu();
	}

	@Override
	public void onResume() {
		super.onResume();

		boolean isMenuClosed = !mContext.getSlidingMenu().isMenuShowing();

		// register onGlobalLayout() to detect when the view layout is about to
		// be loaded
		registerViewLayoutDidLoad();

		/*
		 * if(RegisterService.getService() != null &&
		 * RegisterService.getService().getUIEngine() != null) {
		 * RegisterService.
		 * getService().getUIEngine().resumePendingRequestMessage(); }
		 */

	}

	@Override
	public void onMenuClosed() {

	}

	@Override
	public void onMenuOpened() {

	}

	private void registerViewLayoutDidLoad() {

		final View v = mContext.getSlidingMenu().getContent();
		mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// the onGlobalLayout() may be called many times till view
				// layout's controls is loaded
				// so we assume that when the content screen has been scrolled
				// complete that the time
				// we call onViewLayoutDidLoad() to show helpoverlay
				v.getViewTreeObserver().removeGlobalOnLayoutListener(
						mGlobalLayoutListener);

				// call any child's override routine
				// onViewLayoutDidLoad();
			}
		};
		v.getViewTreeObserver()
				.addOnGlobalLayoutListener(mGlobalLayoutListener);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// AxLog.d("RADialerScreenAbstract.onConfigurationChaged(): this=%s newConfig=%s\n",
		// this, newConfig);
		caculateScreenMetrics();
	}

	/**
	 * Override this to do things (e.g. show an overlay help screen) when this
	 * view is about to be shown, after the initial layout has been completed
	 * for the first time or the layout has been loaded after the app restore
	 * from background
	 * 
	 * @return true for initial layout loaded, false if the layout resume from
	 *         background. (For now, the return value is not really matter...)
	 */
	/*
	 * protected boolean onViewLayoutDidLoad() { if
	 * (mContext.isAppRestoredFromBackground() && getUserVisibleHint()) {
	 * mContext.getSlidingMenu().showMenu(false);
	 * 
	 * return false; } return true; }
	 */

	protected void setFragmentActionBar(Fragment actionBar) {
		// In the case of device which has been re-launched from background such
		// as receiving call from background
		// the state of fragment can not be restored, therefore, change from
		// commit() to commitAllowingStateLoss()
		// to avoid
		// "java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState"
	}

	public int getTitleId() {
		return mTitleId;
	}

	protected void caculateScreenMetrics() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		mWidth = displaymetrics.widthPixels;
		mHeigth = displaymetrics.heightPixels;
	}

	protected int caculateNotificationBarHeight() {
		mNotificationBarHeight = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			mNotificationBarHeight = getResources().getDimensionPixelSize(
					resourceId);
		}
		return mNotificationBarHeight;
	}

	@Override
	public abstract void onScreenSlidingCompleted();

	protected boolean isPopUpShowing() {
		return mPopup.isShowing();
	}

	protected void dismissPopupMenu() {
		if (mPopup != null && mPopup.isShowing() == true)
			mPopup.dismiss();
	}

	protected void setDeletedWhenFinish(boolean isDelete) {
		isDeleteFinished = isDelete;
	}

	protected boolean isDeleteWhenFinish() {
		return isDeleteFinished;
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * Helper to find the view with the specified ID in this view or one of the
	 * parent views.
	 * 
	 * @param id
	 * @return null if not found.
	 */
	protected View findViewByIdInParents(int id) {
		return findViewByIdInParents(getView(), id);
	}

	/**
	 * Helper to find the view with the specified ID in this view or one of the
	 * parent views.
	 * 
	 * @param id
	 * @return null if not found.
	 */
	public static View findViewByIdInParents(View startView, int id) {
		View v = startView;
		if (v == null) {
			return null;
		}
		View foundView = v.findViewById(id);
		while (foundView == null) {
			ViewParent p = v.getParent();
			if (p instanceof View) {
				v = (View) p;
				foundView = v.findViewById(id);
			} else {
				break;
			}
		}
		return foundView;
	}

	protected View findAnyViewByIdInParents(int[] ids) {
		return findAnyViewByIdInParents(getView(), ids);
	}

	/**
	 * Helper to find a view with one of the specified IDs in this view or one
	 * of the parent views.
	 * 
	 * @param ids
	 * @return null if not found.
	 */
	public static View findAnyViewByIdInParents(View startView, int[] ids) {
		View v = startView;
		if (v == null) {
			return null;
		}
		for (int id : ids) {
			View foundView = v.findViewById(id);
			if (foundView != null) {
				return foundView;
			}
		}
		while (true) {
			ViewParent p = v.getParent();
			if (p instanceof View) {
				v = (View) p;
				for (int id : ids) {
					View foundView = v.findViewById(id);
					if (foundView != null) {
						return foundView;
					}
				}
			} else {
				break;
			}
		}
		return null;
	}

	public void showPopupMenu(ListView view, int screenHeight,
			boolean isKeepCurrentState) {
		if (mPopup.isShowing()) {
			if (!isKeepCurrentState) {
				mPopup.dismiss();
			}
		} else if (isKeepCurrentState) {
			return;
		}
		view.setBackgroundColor(Color.GRAY);
		view.setScrollingCacheEnabled(false);
		view.setSmoothScrollbarEnabled(true);
		view.setFastScrollEnabled(true);
		view.setScrollbarFadingEnabled(false);

		mPopup.dismiss();
		Resources r = getResources();
		int margin = 10;// 10dip
		int marginPX = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, margin, r.getDisplayMetrics());
		int rowHeightPx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, POP_UP_LISTVIEW_ROW_HEIGHT,
				r.getDisplayMetrics());
		int popupWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, POP_UP_LISTVIEW_ROW_WIDTH,
				r.getDisplayMetrics());
		int numberItem = view.getAdapter().getCount();
		// increase one for a horizontal line of Listview Item
		int popupHeight = (rowHeightPx + 1) * numberItem;
		int OFFSET_X = mWidth - popupWidth - marginPX;
		int OFFSET_Y = screenHeight;

		int maxHeight = mHeigth - OFFSET_Y;
		if (popupHeight > maxHeight)
			popupHeight = maxHeight;

		// Clear the default translucent background
		mPopup.setBackgroundDrawable(new BitmapDrawable());
		// Displaying the popup at the specified location, + offsets.
		mPopup.setContentView(view);
		mPopup.setWidth(popupWidth);
		mPopup.setHeight(popupHeight);
		mPopup.setFocusable(true);
		mPopup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);
	}

	protected ListView createPopupMenu(String[] menuItems) {
		ListView listPopUp = new ListView(mContext);
		Resources r = getResources();
		int heightPx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, POP_UP_LISTVIEW_ROW_HEIGHT,
				r.getDisplayMetrics());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, heightPx);
		listPopUp.setLayoutParams(lp);

		PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(mContext,
				android.R.layout.simple_expandable_list_item_1,
				android.R.id.text1, menuItems);
		listPopUp.setAdapter(popupMenuAdapter);
		return listPopUp;
	}

	public static class PopupMenuAdapter extends ArrayAdapter<String> {
		// private String[] menuItem;
		private int rowHeight;

		public PopupMenuAdapter(Context context, int resource,
				int textViewResourceId, String[] menu) {
			super(context, resource, textViewResourceId, menu);
			// menuItem = menu;
			rowHeight = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, POP_UP_LISTVIEW_ROW_HEIGHT,
					context.getResources().getDisplayMetrics());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View mView = convertView;

			if (mView == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mView = vi.inflate(R.layout.popup_menu_item, null);
			}

			TextView text = (TextView) mView.findViewById(R.id.textView);
			text.setHeight(rowHeight);
			if (getItem(position) != null) {
				text.setTextColor(Color.WHITE);
				text.setText(getItem(position));
			}
			return mView;
		}
	}

	public boolean isDialog() {
		return isDialog;
	}

	public void setDialog(boolean isDialog) {
		this.isDialog = isDialog;
	}

	public String getFragmentTag() {
		return mTag;
	}

	public void setFragmentTag(String mTag) {
		this.mTag = mTag;
	}
}
