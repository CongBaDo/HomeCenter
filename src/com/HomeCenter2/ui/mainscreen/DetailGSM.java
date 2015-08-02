package com.HomeCenter2.ui.mainscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.StatusRelationship;
import com.HomeCenter2.ui.DatePickerFragment;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.FootLayout;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.TimePickerFragment;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class DetailGSM extends RADialerMainScreenAbstract implements
		OnClickListener, OnCheckedChangeListener, OnItemClickListener,
		onCheckChangedListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		FootLayout.onCheckChangedListener {

	private static StatusRelationship mSR = null;
	private LayoutInflater mInflater = null;
	private HomeCenterUIEngine uiEngine;

	public DetailGSM(int title, String tag, SlidingBaseActivity context) {
		super(DetailGSM.class, title, tag, context);
	}

	public static final String TAG = "TMT DetailDeviceScreen";
	public static Bundle mBundle;
	public static DetailGSM m_instance = null;
	public int roomId;
	private String deviceId = "";
	private String name = "";
	LinearLayout mlnDevice, mlnInfomation, mlnSchedule01, mlnSchedule02,
			mlnSchedule03, mlnSchedule04;

	// Turn when
	private FootLayout lnFootWhen;
	private LinearLayout lnBodyWhen;
	ImageButton btnSynWhen;
	TextView cbSmartEnergy, cbTemp1, cbTemp2, cbLight1, cbLight2, cbSmoke1,
			cbSmoke2, cbMotion1, cbMotion2, cbMotion3, cbMotion4, cbDoor1,
			cbDoor2, cbDoor3, cbDoor4;
	ImageButton btnTurnWhen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(mContext);
		setHasOptionsMenu(true);

	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateConentView");

		View view = inflater.inflate(R.layout.detail_device_screen, container,
				false);

		LinearLayout layout = null;
		layout = (LinearLayout) view.findViewById(R.id.lnInfoDevice);
		layout.setVisibility(View.GONE);

		layout = (LinearLayout) view.findViewById(R.id.lnDetailS01);
		layout.setVisibility(View.GONE);

		layout = (LinearLayout) view.findViewById(R.id.lnDetailS02);
		layout.setVisibility(View.GONE);

		layout = (LinearLayout) view.findViewById(R.id.lnDetailS03);
		layout.setVisibility(View.GONE);

		layout = (LinearLayout) view.findViewById(R.id.lnDetailS04);
		layout.setVisibility(View.GONE);

		initView(view);
		return view;
	}

	private void initTurnWhen(View view) {
		lnBodyWhen = (LinearLayout) view.findViewById(R.id.lnBodyWhen);
		setAnimation(lnBodyWhen, View.GONE);

		lnFootWhen = (FootLayout) view.findViewById(R.id.lnFootWhen);
		lnFootWhen.setOnClickListener(this);
		lnFootWhen.setOnCheckChangedListener(this);
		lnFootWhen.setChecked(false);

		// device
		mlnDevice = (LinearLayout) view.findViewById(R.id.lnWhenDevice);

		mlnDevice.setVisibility(View.VISIBLE);
		cbSmartEnergy = (TextView) view.findViewById(R.id.cbSmartEnergy_device);

		cbSmartEnergy.setOnClickListener(this);

		btnTurnWhen = (ImageButton) view.findViewById(R.id.btnSyncDevices);
		btnTurnWhen.setOnClickListener(this);
	}

	private void initView(View view) {
		initTurnWhen(view);
	}

	@Override
	public void onScreenSlidingCompleted() {
		Log.d(TAG, "onScreenCompleted");

	}

	@Override
	public void loadHeader() {

	}

	@Override
	public void setContentMenu() {

	}

	public static DetailGSM initializeDetailGSM(Bundle bundle, int titleId,
			SlidingBaseActivity mContext) {
		mBundle = bundle;
		if (m_instance == null) {
			m_instance = new DetailGSM(titleId,
					ScreenManager.DETAIL_DEVICE_TAG, mContext);
		}
		mSR = null;
		return m_instance;
	}

	private void getDetailDevice() {
		/*
		 * DialogFragmentWrapper.showDialog(getFragmentManager(),
		 * DetailGSM.this, configManager.DIALOG_PROCESS); Bundle bundle = new
		 * Bundle(); bundle.putBoolean(configManager.IS_DEVICE_BUNDLE,
		 * mIsDevice); bundle.putBoolean(configManager.HAS_GET_SCHEDULE, true);
		 * 
		 * bundle.putInt(configManager.ROOM_ID, roomId);
		 * bundle.putString(configManager.DEVICE_ID, deviceId);
		 * 
		 * Log.e(TAG, "getDetailDevice::room id: " + roomId + " , devId: " +
		 * deviceId);
		 * 
		 * Message message = Message.obtain(); message.setData(bundle);
		 * message.what = HCRequest.REQUEST_GET_DEVICE_STATUS_RELATIONSHIP;
		 * RegisterService.getHomeCenterUIEngine().sendMessage(message);
		 */}

	/*
	 * 1 --> Add/Delete schedule (1,0) 0 --> on/off device (1,0) 20 --> minute
	 * (1-59) 1 --> hour (1-23) 31 --> day (1-31) 12 --> month (1-12) 0000000
	 * --> dow (0-1) //mon #8014# --> schedule index: 8 -->room;
	 */

	/*
	 * 8 --> room index 1-8 1 --> 10' off 0-1 //smart en 01 --> device# 01-10 11
	 * --> action + active time 11 //ko 14_01 --> status 0-1
	 */

	private String saveTurnWhenDevice() {
		StringBuffer relationTrigger = new StringBuffer();
		boolean isChecked;

		// 11 --> action + active time 11 //ko
		relationTrigger.append("11");
		relationTrigger.append(configManager.COMMAS);

		// 14_01 --> status 0-1
		isChecked = Boolean.valueOf(cbTemp1.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbLight1.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbSmoke1.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion1.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion2.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor1.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor2.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		// sensor 2
		isChecked = Boolean.valueOf(cbTemp2.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbLight2.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbSmoke2.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion3.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion4.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor3.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor4.getTag().toString());
		relationTrigger.append(isChecked ? 1 : 0);

		return relationTrigger.toString();

	}

	/*
	 * xx20=8,01,1,0,20,1,31,12,0000000,#8014# / :stOK 8 --> room index (1-8) 01
	 * --> device# (01-10)
	 */

	public void save(int type) {
		DialogFragmentWrapper.showDialog(getFragmentManager(), DetailGSM.this,
				configManager.DIALOG_PROCESS);

		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, roomId);
		bundle.putString(configManager.DEVICE_ID, deviceId);
		bundle.putInt(configManager.TYPE, type);

		String roonSchedule = roomId + configManager.COMMAS + deviceId
				+ configManager.COMMAS;
		switch (type) {
		case configManager.TYPE_TURN_WHEN_DEVICE:
			/*
			 * if (mIsDevice) {
			 * 
			 * boolean isChecked = Boolean.parseBoolean(cbSmartEnergy.getTag()
			 * .toString()); String roomTurn = roomId + configManager.COMMAS +
			 * (isChecked ? "1" : "0") + configManager.COMMAS + deviceId +
			 * configManager.COMMAS;
			 * 
			 * roomTurn += saveTurnWhenDevice();
			 * bundle.putString(configManager.SHEDULE, roomTurn);
			 * 
			 * 
			 * boolean isChecked = Boolean.parseBoolean(cbSmartEnergy.getTag()
			 * .toString()); bundle.putString(configManager.TURN_SMART_ENERGY,
			 * (isChecked ? "1" : "0"));
			 * 
			 * } else { bundle.putString(configManager.SHEDULE, "");
			 * bundle.putString(configManager.TURN_SMART_ENERGY, "0"); }
			 */
			break;

		default:
			break;
		}

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_DEVICE_STATUS_RELATIONSHIP;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// Turn when
		case R.id.lnFootWhen:
			lnFootWhen.toggle();
			break;

		// Schedule devices
		case R.id.cbSmartEnergy_device:
			TextView viewDevice = (TextView) v;
			boolean isChecked = Boolean.valueOf(viewDevice.getTag().toString());
			setDeviceChecked(viewDevice, !isChecked);
			break;

		case R.id.btnSyncDevices:
			save(configManager.TYPE_TURN_WHEN_DEVICE);
			break;
		default:
			break;
		}
	}

	private void refresh() {
		if (mSR == null) {
			return;
		}
		refreshSchduleDevice();

		DialogFragmentWrapper.removeDialog(getFragmentManager(),
				configManager.DIALOG_PROCESS);
	}

	private void refreshSchduleDevice() {
		setDeviceChecked(cbTemp1, mSR.isTemp1());
		setDeviceChecked(cbLight1, mSR.isLight1());
		setDeviceChecked(cbSmoke1, mSR.isSmoke1());
		setDeviceChecked(cbMotion1, mSR.isMotion1());
		setDeviceChecked(cbMotion2, mSR.isMotion2());
		setDeviceChecked(cbDoor1, mSR.isDoor1());
		setDeviceChecked(cbDoor2, mSR.isDoor2());
		setDeviceChecked(cbTemp2, mSR.isTemp2());
		setDeviceChecked(cbLight2, mSR.isLight2());
		setDeviceChecked(cbSmoke2, mSR.isSmoke2());
		setDeviceChecked(cbMotion3, mSR.isMotion3());
		setDeviceChecked(cbMotion4, mSR.isMotion4());
		setDeviceChecked(cbDoor3, mSR.isDoor3());
		setDeviceChecked(cbDoor4, mSR.isDoor4());
		setDeviceChecked(cbSmartEnergy, mSR.isSE());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setTitle(R.string.gsm_message);
		mActionBarV7.setDisplayShowTitleEnabled(true);
		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

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

			}
		}
		return isSelected;
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	public void showTimePickerDialog(TextView v) {
		DialogFragment newFragment = new TimePickerFragment(v);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void showDayPickerDialog(TextView v) {
		DialogFragment newFragment = new DatePickerFragment(v);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onPageSelected() {
		Log.d(TAG, "onPageSelected");
		getDetailDevice();
	}

	@Override
	public void onPageDeselected() {
		Log.d(TAG, "onPageDeSelected");
		mActionBarV7.setIcon(R.drawable.ic_protection_totale);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	}

	// LinearLayout ln

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*
		 * ScheduleHolderView holderview = (ScheduleHolderView)view.getTag();
		 * if(holderview== null) return; int visible =
		 * holderview.detailScheduleLn.getVisibility();
		 * holderview.detailScheduleLn.setVisibility(visible== View.VISIBLE?
		 * View.GONE : View.VISIBLE);
		 * DetailDeviceScreen.setListViewHeightBasedOnChildren(mScheduleLV);
		 */
		// mScheduleLV.requestLayout();
		/*
		 * int visible = ln.getVisibility();
		 * 
		 * if(visible== View.VISIBLE){ ln.setVisibility( View.GONE); Animation
		 * animation = AnimationUtils.loadAnimation(getActivity(),
		 * R.anim.left_to_right_pop_exit); ln.startAnimation(animation); }else{
		 * ln.setVisibility( View.VISIBLE); Animation animation =
		 * AnimationUtils.loadAnimation(getActivity(),
		 * R.anim.left_to_right_in_enter); ln.startAnimation(animation); }
		 */

	}

	/**
	 * Schedule Checked
	 */
	private void setSheduleChecked(TextView textview, boolean isChecked) {
		if (textview != null) {
			textview.setTag(isChecked);
			refreshScheduleText(textview, isChecked);
		}
	}

	private void refreshScheduleText(TextView buttonView, boolean isChecked) {
		if (isChecked) {
			buttonView.setTypeface(Typeface.DEFAULT_BOLD);
			buttonView
					.setTextColor(getResources().getColor(R.color.blue_light));
		} else {
			buttonView.setTypeface(Typeface.DEFAULT);
			buttonView.setTextColor(Color.BLACK);
		}
	}

	/**
	 * TurnWhen Checked
	 */
	private void setDeviceChecked(TextView textview, boolean isChecked) {
		textview.setTag(isChecked);
		if (isChecked) {
			textview.setTypeface(Typeface.DEFAULT_BOLD);
			textview.setTextColor(Color.RED);
		} else {
			textview.setTypeface(Typeface.DEFAULT);
			textview.setTextColor(Color.BLACK);
		}
	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		switch (view.getId()) {
		case R.id.lnFootWhen:
			if (isChecked) {
				setAnimation(lnBodyWhen, View.VISIBLE);
			} else {
				setAnimation(lnBodyWhen, View.GONE);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_PROCESS:
			Dialog dialog = createProgressDialog();
			// dialog.setRetainInstance(true);

			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		default:
			break;
		}
		return null;
	}

	private Dialog createProgressDialog() {
		AlertDialog dialog = null;
		dialog = new ProgressDialog(getActivity());
		if (dialog != null) {
			dialog.setMessage(getString(R.string.login_progress));
			((ProgressDialog) dialog)
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		return dialog;
	}

	private void setAnimation(LinearLayout linear, int visible) {
		if (visible == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.top_to_bottom_in_enter);
			linear.startAnimation(animation);
		} else {

			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.bottom_to_top_pop_exit);
			linear.startAnimation(animation);
		}
		linear.setVisibility(visible);
	}

	public void setAnimationTypeDevice(LinearLayout linear, int visible) {
		/*
		 * if(visible ==View.VISIBLE){ Animation animation =
		 * AnimationUtils.loadAnimation(mContext,
		 * R.anim.left_to_right_pop_exit); linear.startAnimation(animation);
		 * }else{
		 * 
		 * Animation animation = AnimationUtils.loadAnimation(mContext,
		 * R.anim.left_to_right_in_enter); linear.startAnimation(animation); }
		 */
		linear.setVisibility(visible);
	}

	@Override
	public void onResume() {
		super.onResume();

	}
}
