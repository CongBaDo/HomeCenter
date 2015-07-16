package com.HomeCenter2.ui.mainscreen;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.format.Time;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.MyTime;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Control;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DeviceTypeOnOff;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.StatusRelationship;
import com.HomeCenter2.ui.DatePickerFragment;
import com.HomeCenter2.ui.DayLayout;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.ExpandableHightListView;
import com.HomeCenter2.ui.FootLayout;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.TimePickerFragment;
import com.HomeCenter2.ui.listener.GetStatusRelationshipListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class DetailDeviceScreen extends RADialerMainScreenAbstract implements
		OnClickListener, GetStatusRelationshipListener,
		OnCheckedChangeListener, OnItemClickListener, onCheckChangedListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		FootLayout.onCheckChangedListener, XMLListener {

	private static StatusRelationship mSR = null;

	private boolean mIsDevice = false;
	private HomeCenterUIEngine uiEngine;
	private Device mDevice = null;

	public DetailDeviceScreen(int title, String tag, SlidingBaseActivity context) {
		super(DetailDeviceScreen.class, title, tag, context);
	}

	public static final String TAG = "DetailDeviceScreen";
	public static Bundle mBundle;
	public static DetailDeviceScreen m_instance = null;
	public int roomId;
	private String deviceId = "";
	private String name = "";

	Room room = null;

	// TextView cbShedule02, cbShedule03, cbShedule04, cbInfomation;

	// Information
	private TextView mNameTv;
	private EditText mNameEdit;
	private FootLayout lnFootInfo;
	private LinearLayout lnBodyInfo, mlnInfoDetail;
	ImageButton btnSynInfo, btnType01, btnType02, btnType03, btnType04,
			btnType05;
	// Schedule 01
	private DayLayout cbMonS1, cbTueS1, cbWedS1, cbThuS1, cbFriS1, cbSatS1,
			cbSunS1;
	private ScheduleImageView swOnOffS1; // TMT
	private ImageButton btnSyncS01;
	private ScheduleImageView cbAddDeleteS1;
	private TextView mDayS01On, mTimeS01On, mDayOnWeekS01;
	private FootLayout lnFootS01;
	private LinearLayout lnHeaderS01, lnBodyS01;
	private RadioButton rbDMYS01, rbDayS01;

	// Schedule 02
	private DayLayout cbMonS2, cbTueS2, cbWedS2, cbThuS2, cbFriS2, cbSatS2,
			cbSunS2;
	private ScheduleImageView swOnOffS2;
	private ImageButton btnSyncS02;
	private ScheduleImageView cbAddDeleteS2;
	private TextView mDayS02On, mTimeS02On, mDayOnWeekS02;
	private FootLayout lnFootS02;
	private LinearLayout lnHeaderS02, lnBodyS02;
	private RadioButton rbDMYS02, rbDayS02;

	// Schedule 03
	private DayLayout cbMonS3, cbTueS3, cbWedS3, cbThuS3, cbFriS3, cbSatS3,
			cbSunS3;
	private ScheduleImageView swOnOffS3;
	private ImageButton btnSyncS03;
	private ScheduleImageView cbAddDeleteS3;
	private TextView mDayS03On, mTimeS03On, mDayOnWeekS03;
	private FootLayout lnFootS03;
	private LinearLayout lnHeaderS03, lnBodyS03;
	private RadioButton rbDMYS03, rbDayS03;

	// Schedule 04
	private DayLayout cbMonS4, cbTueS4, cbWedS4, cbThuS4, cbFriS4, cbSatS4,
			cbSunS4;
	private ScheduleImageView swOnOffS4;
	private ImageButton btnSyncS04;
	private ScheduleImageView cbAddDeleteS4;
	private TextView mDayS04On, mTimeS04On, mDayOnWeekS04;
	private FootLayout lnFootS04;
	private LinearLayout lnHeaderS04, lnBodyS04;
	private RadioButton rbDMYS04, rbDayS04;

	// Turn when
	private FootLayout lnFootWhen;
	private LinearLayout lnBodyWhen;
	ImageButton btnSynWhen;
	CheckBox cbSmartEnergy, cbTemp1, cbTemp2, cbLight1, cbLight2, cbSmoke1,
			cbSmoke2, cbMotion1, cbMotion2, cbMotion3, cbMotion4, cbDoor1,
			cbDoor2, cbDoor3, cbDoor4;
	ImageButton btnTurnWhen;

	LinearLayout mlnDevice, mlnInfomation;
	ExpandableHightListView mScheduleLV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterService service = RegisterService.getService();
		if (service != null) {
			uiEngine = service.getUIEngine();
		}
		setHasOptionsMenu(true);
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		if (uiEngine != null) {
			uiEngine.addGettingSRObserver(this);
			uiEngine.addXMLObserver(this);
		}

		Log.d(TAG, "onCreateConentView");
		mIsDevice = mBundle.getBoolean(configManager.IS_DEVICE_BUNDLE);
		if (mIsDevice) {
			mDevice = (Device) mBundle
					.getSerializable(configManager.DEVICE_BUNDLE);
			roomId = mDevice.getRoomId();
			int id = mDevice.getId();
			deviceId = configManager.convertDeviceIdToString(id);
			name = mDevice.getName();
			room = (Room) uiEngine.getHouse().getRoomsById(roomId);
			if (mActionBarV7 != null) {
				mActionBarV7.setTitle(room.getName());
			}
		} else {
			room = (Room) mBundle.getSerializable(configManager.ROOM_BUNDLE);
			roomId = room.getId();
			deviceId = mBundle.getString(configManager.BUTTON_BUNDLE);
			name = mContext.getString(R.string.schedule) + " " + deviceId;
		}

		View view = inflater.inflate(R.layout.detail_device_screen, container,
				false);
		initView(view);
		return view;
	}

	private void initSchedule01(View view, Time now) {
		lnHeaderS01 = (LinearLayout) view.findViewById(R.id.lnHeaderS01);

		lnFootS01 = (FootLayout) view.findViewById(R.id.lnFootS01);
		lnFootS01.setOnClickListener(this);
		lnFootS01.setOnCheckChangedListener(this);
		lnFootS01.setAdded(true);

		mDayOnWeekS01 = (TextView) view.findViewById(R.id.tvDayInWeekS01);

		lnBodyS01 = (LinearLayout) view.findViewById(R.id.lnBodyS01);
		setAnimation(lnBodyS01, View.GONE);

		// On - Off
		swOnOffS1 = (ScheduleImageView) view.findViewById(R.id.swS01); // TMT
		swOnOffS1.setSrcCheched(R.drawable.ic_lamp_blk);
		swOnOffS1.setSrcNonChecked(R.drawable.ic_lamp_wht);
		swOnOffS1.setOnClickListener(this);
		swOnOffS1.setOnCheckChangedListener(this);
		swOnOffS1.setChecked(false);

		// Add - Delete
		cbAddDeleteS1 = (ScheduleImageView) view.findViewById(R.id.imgOnOffS01); // TMT
		cbAddDeleteS1.setSrcCheched(R.drawable.ic_turn_on123);
		cbAddDeleteS1.setSrcNonChecked(R.drawable.ic_turn_off123);
		cbAddDeleteS1.setOnClickListener(this);
		cbAddDeleteS1.setOnCheckChangedListener(this);

		if (mIsDevice) {
			swOnOffS1.setVisibility(View.VISIBLE);
		} else {
			swOnOffS1.setVisibility(View.GONE);
		}

		// Time
		mDayS01On = (TextView) view.findViewById(R.id.editDayS01);
		mDayS01On.setOnClickListener(this);
		configManager.setDate(mDayS01On, now.monthDay, now.month + 1, now.year);

		mTimeS01On = (TextView) view.findViewById(R.id.editTimeS01);
		mTimeS01On.setOnClickListener(this);
		configManager.setTime(mTimeS01On, now.minute, now.hour);

		// repeat
		cbMonS1 = (DayLayout) view.findViewById(R.id.cbMonS1);
		cbMonS1.setOnClickListener(this);

		cbTueS1 = (DayLayout) view.findViewById(R.id.cbTueS1);
		cbTueS1.setOnClickListener(this);

		cbWedS1 = (DayLayout) view.findViewById(R.id.cbWedS1);
		cbWedS1.setOnClickListener(this);

		cbThuS1 = (DayLayout) view.findViewById(R.id.cbThuS1);
		cbThuS1.setOnClickListener(this);

		cbFriS1 = (DayLayout) view.findViewById(R.id.cbFriS1);
		cbFriS1.setOnClickListener(this);

		cbSatS1 = (DayLayout) view.findViewById(R.id.cbSatS1);
		cbSatS1.setOnClickListener(this);

		cbSunS1 = (DayLayout) view.findViewById(R.id.cbSunS1);
		cbSunS1.setOnClickListener(this);

		cbMonS1.setChecked(false);
		cbTueS1.setChecked(false);
		cbWedS1.setChecked(false);
		cbThuS1.setChecked(false);
		cbSatS1.setChecked(false);
		cbSunS1.setChecked(false);

		btnSyncS01 = (ImageButton) view.findViewById(R.id.btnSyncS01);
		btnSyncS01.setOnClickListener(this);

		// swOnOffS1.setChecked(false);//nganguyen changed ??? the same in 2,3,4

		// TMT
		rbDMYS01 = (RadioButton) view.findViewById(R.id.rbtnDMY01);
		rbDMYS01.setOnCheckedChangeListener(this);
		rbDayS01 = (RadioButton) view.findViewById(R.id.rbtnDay01);
		rbDayS01.setOnCheckedChangeListener(this);

	}

	private void initSchedule02(View view, Time now) {
		lnHeaderS02 = (LinearLayout) view.findViewById(R.id.lnHeaderS02);

		lnFootS02 = (FootLayout) view.findViewById(R.id.lnFootS02);
		lnFootS02.setOnClickListener(this);
		lnFootS02.setOnCheckChangedListener(this);
		lnFootS02.setAdded(true);

		mDayOnWeekS02 = (TextView) view.findViewById(R.id.tvDayInWeekS02);

		lnBodyS02 = (LinearLayout) view.findViewById(R.id.lnBodyS02);
		setAnimation(lnBodyS02, View.GONE);

		// On - Off
		swOnOffS2 = (ScheduleImageView) view.findViewById(R.id.swS02); // TMT
		swOnOffS2.setSrcCheched(R.drawable.ic_lamp_blk);
		swOnOffS2.setSrcNonChecked(R.drawable.ic_lamp_wht);
		swOnOffS2.setOnClickListener(this);
		swOnOffS2.setOnCheckChangedListener(this);
		swOnOffS2.setChecked(false);

		// Add - Delete
		cbAddDeleteS2 = (ScheduleImageView) view.findViewById(R.id.imgOnOffS02);

		cbAddDeleteS2.setSrcCheched(R.drawable.ic_turn_on123);
		cbAddDeleteS2.setSrcNonChecked(R.drawable.ic_turn_off123);
		cbAddDeleteS2.setOnClickListener(this);
		cbAddDeleteS2.setOnCheckChangedListener(this);

		if (mIsDevice) {
			swOnOffS2.setVisibility(View.VISIBLE);
		} else {
			swOnOffS2.setVisibility(View.GONE);
		}

		// Time
		mDayS02On = (TextView) view.findViewById(R.id.editDayS02);
		mDayS02On.setOnClickListener(this);
		configManager.setDate(mDayS02On, now.monthDay, now.month + 1, now.year);

		mTimeS02On = (TextView) view.findViewById(R.id.editTimeS02);
		mTimeS02On.setOnClickListener(this);
		configManager.setTime(mTimeS02On, now.minute, now.hour);

		// repeat
		cbMonS2 = (DayLayout) view.findViewById(R.id.cbMonS2);
		cbMonS2.setOnClickListener(this);

		cbTueS2 = (DayLayout) view.findViewById(R.id.cbTueS2);
		cbTueS2.setOnClickListener(this);

		cbWedS2 = (DayLayout) view.findViewById(R.id.cbWedS2);
		cbWedS2.setOnClickListener(this);

		cbThuS2 = (DayLayout) view.findViewById(R.id.cbThuS2);
		cbThuS2.setOnClickListener(this);

		cbFriS2 = (DayLayout) view.findViewById(R.id.cbFriS2);
		cbFriS2.setOnClickListener(this);

		cbSatS2 = (DayLayout) view.findViewById(R.id.cbSatS2);
		cbSatS2.setOnClickListener(this);

		cbSunS2 = (DayLayout) view.findViewById(R.id.cbSunS2);
		cbSunS2.setOnClickListener(this);

		cbMonS2.setChecked(false);
		cbTueS2.setChecked(false);
		cbWedS2.setChecked(false);
		cbThuS2.setChecked(false);
		cbSatS2.setChecked(false);
		cbSunS2.setChecked(false);

		btnSyncS02 = (ImageButton) view.findViewById(R.id.btnSyncS02);
		btnSyncS02.setOnClickListener(this);

		// cbAddDeleteS2.setChecked(false);
		// swOnOffS2.setChecked(false);

		rbDMYS02 = (RadioButton) view.findViewById(R.id.rbtnDMY02);
		rbDMYS02.setOnCheckedChangeListener(this);
		rbDayS02 = (RadioButton) view.findViewById(R.id.rbtnDay02);
		rbDayS02.setOnCheckedChangeListener(this);
	}

	private void initSchedule03(View view, Time now) {
		lnHeaderS03 = (LinearLayout) view.findViewById(R.id.lnHeaderS03);

		lnFootS03 = (FootLayout) view.findViewById(R.id.lnFootS03);
		lnFootS03.setOnClickListener(this);
		lnFootS03.setOnCheckChangedListener(this);
		lnFootS03.setAdded(true);

		mDayOnWeekS03 = (TextView) view.findViewById(R.id.tvDayInWeekS03);

		lnBodyS03 = (LinearLayout) view.findViewById(R.id.lnBodyS03);
		setAnimation(lnBodyS03, View.GONE);

		// On - Off
		swOnOffS3 = (ScheduleImageView) view.findViewById(R.id.swS03); // TMT
		swOnOffS3.setSrcCheched(R.drawable.ic_lamp_blk);
		swOnOffS3.setSrcNonChecked(R.drawable.ic_lamp_wht);
		swOnOffS3.setOnClickListener(this);
		swOnOffS3.setOnCheckChangedListener(this);
		swOnOffS3.setChecked(false);

		// Add - Delete
		cbAddDeleteS3 = (ScheduleImageView) view.findViewById(R.id.imgOnOffS03);
		cbAddDeleteS3.setSrcCheched(R.drawable.ic_turn_on123);
		cbAddDeleteS3.setSrcNonChecked(R.drawable.ic_turn_off123);
		cbAddDeleteS3.setOnClickListener(this);
		cbAddDeleteS3.setOnCheckChangedListener(this);

		if (mIsDevice) {
			swOnOffS3.setVisibility(View.VISIBLE);
		} else {
			swOnOffS3.setVisibility(View.GONE);
		}

		// Time
		mDayS03On = (TextView) view.findViewById(R.id.editDayS03);
		mDayS03On.setOnClickListener(this);
		configManager.setDate(mDayS03On, now.monthDay, now.month + 1, now.year);

		mTimeS03On = (TextView) view.findViewById(R.id.editTimeS03);
		mTimeS03On.setOnClickListener(this);
		configManager.setTime(mTimeS03On, now.minute, now.hour);

		// repeat

		cbMonS3 = (DayLayout) view.findViewById(R.id.cbMonS3);
		cbMonS3.setOnClickListener(this);

		cbTueS3 = (DayLayout) view.findViewById(R.id.cbTueS3);
		cbTueS3.setOnClickListener(this);

		cbWedS3 = (DayLayout) view.findViewById(R.id.cbWedS3);
		cbWedS3.setOnClickListener(this);

		cbThuS3 = (DayLayout) view.findViewById(R.id.cbThuS3);
		cbThuS3.setOnClickListener(this);

		cbFriS3 = (DayLayout) view.findViewById(R.id.cbFriS3);
		cbFriS3.setOnClickListener(this);

		cbSatS3 = (DayLayout) view.findViewById(R.id.cbSatS3);
		cbSatS3.setOnClickListener(this);

		cbSunS3 = (DayLayout) view.findViewById(R.id.cbSunS3);
		cbSunS3.setOnClickListener(this);

		cbMonS3.setChecked(false);
		cbTueS3.setChecked(false);
		cbWedS3.setChecked(false);
		cbThuS3.setChecked(false);
		cbSatS3.setChecked(false);
		cbSunS3.setChecked(false);

		btnSyncS03 = (ImageButton) view.findViewById(R.id.btnSyncS03);
		btnSyncS03.setOnClickListener(this);

		// cbAddDeleteS3.setChecked(false);
		// swOnOffS3.setChecked(false);

		// TMT
		rbDMYS03 = (RadioButton) view.findViewById(R.id.rbtnDMY03);
		rbDMYS03.setOnCheckedChangeListener(this);
		rbDayS03 = (RadioButton) view.findViewById(R.id.rbtnDay03);
		rbDayS03.setOnCheckedChangeListener(this);

	}

	private void initSchedule04(View view, Time now) {
		lnHeaderS04 = (LinearLayout) view.findViewById(R.id.lnHeaderS04);

		lnFootS04 = (FootLayout) view.findViewById(R.id.lnFootS04);
		lnFootS04.setOnClickListener(this);
		lnFootS04.setOnCheckChangedListener(this);
		lnFootS04.setAdded(true);

		mDayOnWeekS04 = (TextView) view.findViewById(R.id.tvDayInWeekS04);

		lnBodyS04 = (LinearLayout) view.findViewById(R.id.lnBodyS04);
		setAnimation(lnBodyS04, View.GONE);

		// On - Off
		swOnOffS4 = (ScheduleImageView) view.findViewById(R.id.swS04);
		swOnOffS4.setSrcCheched(R.drawable.ic_lamp_blk);
		swOnOffS4.setSrcNonChecked(R.drawable.ic_lamp_wht);
		swOnOffS4.setOnClickListener(this);
		swOnOffS4.setOnCheckChangedListener(this);
		swOnOffS4.setChecked(false);

		// Add - Delete
		cbAddDeleteS4 = (ScheduleImageView) view.findViewById(R.id.imgOnOffS04);
		cbAddDeleteS4.setSrcCheched(R.drawable.ic_turn_on123);
		cbAddDeleteS4.setSrcNonChecked(R.drawable.ic_turn_off123);
		cbAddDeleteS4.setOnClickListener(this);
		cbAddDeleteS4.setOnCheckChangedListener(this);

		if (mIsDevice) {
			cbAddDeleteS4.setVisibility(View.VISIBLE);
		} else {
			cbAddDeleteS4.setVisibility(View.GONE);
		}

		// Time
		mDayS04On = (TextView) view.findViewById(R.id.editDayS04);
		mDayS04On.setOnClickListener(this);
		configManager.setDate(mDayS04On, now.monthDay, now.month + 1, now.year);

		mTimeS04On = (TextView) view.findViewById(R.id.editTimeS04);
		mTimeS04On.setOnClickListener(this);
		configManager.setTime(mTimeS04On, now.minute, now.hour);

		// repeat
		cbMonS4 = (DayLayout) view.findViewById(R.id.cbMonS4);
		cbMonS4.setOnClickListener(this);

		cbTueS4 = (DayLayout) view.findViewById(R.id.cbTueS4);
		cbTueS4.setOnClickListener(this);

		cbWedS4 = (DayLayout) view.findViewById(R.id.cbWedS4);
		cbWedS4.setOnClickListener(this);

		cbThuS4 = (DayLayout) view.findViewById(R.id.cbThuS4);
		cbThuS4.setOnClickListener(this);

		cbFriS4 = (DayLayout) view.findViewById(R.id.cbFriS4);
		cbFriS4.setOnClickListener(this);

		cbSatS4 = (DayLayout) view.findViewById(R.id.cbSatS4);
		cbSatS4.setOnClickListener(this);

		cbSunS4 = (DayLayout) view.findViewById(R.id.cbSunS4);
		cbSunS4.setOnClickListener(this);

		cbMonS4.setChecked(false);
		cbTueS4.setChecked(false);
		cbWedS4.setChecked(false);
		cbThuS4.setChecked(false);
		cbSatS4.setChecked(false);
		cbSunS4.setChecked(false);

		btnSyncS04 = (ImageButton) view.findViewById(R.id.btnSyncS04);
		btnSyncS04.setOnClickListener(this);

		// cbAddDeleteS4.setChecked(false);
		// swOnOffS4.setChecked(false);

		// TMT
		rbDMYS04 = (RadioButton) view.findViewById(R.id.rbtnDMY04);
		rbDMYS04.setOnCheckedChangeListener(this);
		rbDayS04 = (RadioButton) view.findViewById(R.id.rbtnDay04);
		rbDayS04.setOnCheckedChangeListener(this);
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
		if (mIsDevice) {
			mlnDevice.setVisibility(View.VISIBLE);
			cbSmartEnergy = (CheckBox) view
					.findViewById(R.id.cbSmartEnergy_device);

			cbTemp1 = (CheckBox) view.findViewById(R.id.cbTemp1_device);
			cbTemp2 = (CheckBox) view.findViewById(R.id.cbTemp2_device);
			cbLight1 = (CheckBox) view.findViewById(R.id.cbLight1_device);
			cbLight2 = (CheckBox) view.findViewById(R.id.cbLight2_device);
			cbSmoke1 = (CheckBox) view.findViewById(R.id.cbSmoke1_device);
			cbSmoke2 = (CheckBox) view.findViewById(R.id.cbSmoke2_device);
			cbMotion1 = (CheckBox) view.findViewById(R.id.cbMotion1_device);
			cbMotion2 = (CheckBox) view.findViewById(R.id.cbMotion2_device);
			cbMotion3 = (CheckBox) view.findViewById(R.id.cbMotion3_device);
			cbMotion4 = (CheckBox) view.findViewById(R.id.cbMotion4_device);
			cbDoor1 = (CheckBox) view.findViewById(R.id.cbDoor1_device);
			cbDoor2 = (CheckBox) view.findViewById(R.id.cbDoor2_device);
			cbDoor3 = (CheckBox) view.findViewById(R.id.cbDoor3_device);
			cbDoor4 = (CheckBox) view.findViewById(R.id.cbDoor4_device);

			cbTemp1.setOnCheckedChangeListener(this);
			cbTemp2.setOnCheckedChangeListener(this);
			cbLight1.setOnCheckedChangeListener(this);
			cbLight2.setOnCheckedChangeListener(this);
			cbSmoke1.setOnCheckedChangeListener(this);
			cbSmoke2.setOnCheckedChangeListener(this);
			cbMotion1.setOnCheckedChangeListener(this);
			cbMotion2.setOnCheckedChangeListener(this);
			cbMotion3.setOnCheckedChangeListener(this);
			cbMotion4.setOnCheckedChangeListener(this);
			cbDoor1.setOnCheckedChangeListener(this);
			cbDoor2.setOnCheckedChangeListener(this);
			cbDoor3.setOnCheckedChangeListener(this);
			cbDoor4.setOnCheckedChangeListener(this);

			cbSmartEnergy.setOnCheckedChangeListener(this);

			setDeviceChecked(cbSmartEnergy, false);
			setDeviceChecked(cbTemp1, false);
			setDeviceChecked(cbLight1, false);
			setDeviceChecked(cbSmoke1, false);
			setDeviceChecked(cbMotion1, false);
			setDeviceChecked(cbMotion2, false);
			setDeviceChecked(cbDoor1, false);
			setDeviceChecked(cbDoor2, false);
			setDeviceChecked(cbTemp2, false);
			setDeviceChecked(cbLight2, false);
			setDeviceChecked(cbSmoke2, false);
			setDeviceChecked(cbMotion3, false);
			setDeviceChecked(cbMotion4, false);
			setDeviceChecked(cbDoor3, false);
			setDeviceChecked(cbDoor4, false);
			setDeviceChecked(cbSmartEnergy, false);

			btnTurnWhen = (ImageButton) view.findViewById(R.id.btnSyncDevices);
			btnTurnWhen.setOnClickListener(this);

			Room room = (Room) uiEngine.getHouse().getRoomsById(roomId);

			if (room != null) {
				List<Sensor> listDevices = room.getSensors();
				Device device = null;
				int size = listDevices.size();
				for (int i = 0; i < size; i++) {
					device = listDevices.get(i);
					int deviceId = device.getId();
					if (deviceId == configManager.TEMPERATURE_1) {
						cbTemp1.setText(device.getName());
					} else if (deviceId == configManager.TEMPERATURE_2) {
						cbTemp2.setText(device.getName());
					} else if (deviceId == configManager.SMOKE_1) {
						cbSmoke1.setText(device.getName());
					} else if (deviceId == configManager.SMOKE_2) {
						cbSmoke2.setText(device.getName());
					} else if (deviceId == configManager.LIGHT_1) {
						cbLight1.setText(device.getName());
					} else if (deviceId == configManager.LIGHT_2) {
						cbLight2.setText(device.getName());
					} else if (deviceId == configManager.MOTION_1) {
						cbMotion1.setText(device.getName());
					} else if (deviceId == configManager.MOTION_2) {
						cbMotion2.setText(device.getName());
					} else if (deviceId == configManager.MOTION_3) {
						cbMotion3.setText(device.getName());
					} else if (deviceId == configManager.MOTION_4) {
						cbMotion4.setText(device.getName());
					} else if (deviceId == configManager.DOOR_STATUS_1) {
						cbDoor1.setText(device.getName());
					} else if (deviceId == configManager.DOOR_STATUS_2) {
						cbDoor2.setText(device.getName());
					} else if (deviceId == configManager.DOOR_STATUS_3) {
						cbDoor3.setText(device.getName());
					} else if (deviceId == configManager.DOOR_STATUS_4) {
						cbDoor4.setText(device.getName());
					}
				}
			}
		} else {
			mlnDevice.setVisibility(View.GONE);
		}
	}

	private void initView(View view) {
		initInformationDetail(view);

		Time now = new Time();
		now.setToNow();

		initSchedule01(view, now);
		initSchedule02(view, now);
		initSchedule03(view, now);
		initSchedule04(view, now);

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

	public static DetailDeviceScreen initializeDetailDeviceScreen(
			Bundle bundle, int titleId, SlidingBaseActivity mContext) {
		mBundle = bundle;
		if (m_instance == null) {
			m_instance = new DetailDeviceScreen(titleId,
					ScreenManager.DETAIL_DEVICE_TAG, mContext);
		}
		mSR = null;
		return m_instance;
	}

	private void getDetailDevice() {
		DialogFragmentWrapper.showDialog(getFragmentManager(),
				DetailDeviceScreen.this, configManager.DIALOG_PROCESS);
		Bundle bundle = new Bundle();
		bundle.putBoolean(configManager.IS_DEVICE_BUNDLE, mIsDevice);
		bundle.putBoolean(configManager.HAS_GET_SCHEDULE, true);

		bundle.putInt(configManager.ROOM_ID, roomId);
		bundle.putString(configManager.DEVICE_ID, deviceId);

		Log.e(TAG, "getDetailDevice::room id: " + roomId + " , devId: "
				+ deviceId);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_GET_DEVICE_STATUS_RELATIONSHIP;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	/*
	 * 1 --> Add/Delete schedule (1,0) 0 --> on/off device (1,0) 20 --> minute
	 * (1-59) 1 --> hour (1-23) 31 --> day (1-31) 12 --> month (1-12) 0000000
	 * --> dow (0-1) //mon #8014# --> schedule index: 8 -->room; #r8014#r
	 * //remote
	 */

	private String saveShedule01(String devId) {
		StringBuffer relationTrigger = new StringBuffer();
		boolean isChecked = false;

		// 1 --> Add/Delete schedule (1,0)
		isChecked = cbAddDeleteS1.isChecked();
		relationTrigger.append(isChecked ? 1 : 0);
		relationTrigger.append(configManager.COMMAS);

		// 0 --> on/off device (1,0)
		if (mIsDevice) {
			isChecked = swOnOffS1.isChecked();
			relationTrigger.append(isChecked ? 1 : 0);
			relationTrigger.append(configManager.COMMAS);

		} else {
			relationTrigger.append('r');
			relationTrigger.append(configManager.COMMAS);
		}

		// 20 --> minute (1-59)
		// 1 --> hour (1-23)
		MyTime time = (MyTime) mTimeS01On.getTag();
		if (time != null) {
			relationTrigger.append(time.getMinute());
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append(time.getHour());
			relationTrigger.append(configManager.COMMAS);
		}

		// 31 --> day (1-31)
		// 12 --> month (1-12) TMT
		if (rbDMYS01.isChecked()) {
			MyDate date = (MyDate) mDayS01On.getTag();
			if (date != null) {
				relationTrigger.append(date.getDay());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(date.getMonth());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
			}
		} else {
			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			// repeat days
			relationTrigger.append(cbMonS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbTueS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbWedS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbThuS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbFriS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbSatS1.isChecked() ? 1 : 0);
			relationTrigger.append(cbSunS1.isChecked() ? 1 : 0);
		}
		// #8014# --> schedule index: 8 -->room;
		relationTrigger.append(configManager.COMMAS);

		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);

		relationTrigger.append(roomId);
		relationTrigger.append(devId);
		relationTrigger.append(1);
		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);
		return relationTrigger.toString();

	}

	private String saveShedule02(String devId) {
		StringBuffer relationTrigger = new StringBuffer();
		boolean isChecked = false;

		// 1 --> Add/Delete schedule (1,0)
		isChecked = cbAddDeleteS2.isChecked();
		relationTrigger.append(isChecked ? 1 : 0);
		relationTrigger.append(configManager.COMMAS);

		// 0 --> on/off device (1,0)
		if (mIsDevice) {
			isChecked = swOnOffS2.isChecked();

			relationTrigger.append(isChecked ? 1 : 0);
			relationTrigger.append(configManager.COMMAS);

		} else {
			relationTrigger.append('r');
			relationTrigger.append(configManager.COMMAS);
		}

		// 20 --> minute (1-59)
		// 1 --> hour (1-23)
		MyTime time = (MyTime) mTimeS02On.getTag();
		if (time != null) {
			relationTrigger.append(time.getMinute());
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append(time.getHour());
			relationTrigger.append(configManager.COMMAS);
		}

		// 31 --> day (1-31)
		// 12 --> month (1-12)
		if (rbDMYS02.isChecked()) {
			MyDate date = (MyDate) mDayS02On.getTag();
			if (date != null) {
				relationTrigger.append(date.getDay());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(date.getMonth());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
			}
		} else {
			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			// repeat days
			relationTrigger.append(cbMonS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbTueS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbWedS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbThuS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbFriS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbSatS2.isChecked() ? 1 : 0);
			relationTrigger.append(cbSunS2.isChecked() ? 1 : 0);

		}
		// #8014# --> schedule index: 8 -->room;
		relationTrigger.append(configManager.COMMAS);

		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);

		relationTrigger.append(roomId);
		relationTrigger.append(devId);
		relationTrigger.append(2);
		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);
		return relationTrigger.toString();

	}

	// private String saveShedule02(String devId) {
	// StringBuffer relationTrigger = new StringBuffer();
	// boolean isChecked = false;
	//
	// // 1 --> Add/Delete schedule (1,0)
	// isChecked = cbAddDeleteS2.isChecked();
	// relationTrigger.append(isChecked ? 1 : 0);
	// relationTrigger.append(configManager.COMMAS);
	//
	// // 0 --> on/off device (1,0)
	// if (mIsDevice) {
	// isChecked = swOnOffS2.isChecked();
	//
	// relationTrigger.append(isChecked ? 1 : 0);
	// relationTrigger.append(configManager.COMMAS);
	//
	// } else {
	// relationTrigger.append('r');
	// relationTrigger.append(configManager.COMMAS);
	// }
	//
	// // 20 --> minute (1-59)
	// // 1 --> hour (1-23)
	// MyTime time = (MyTime) mTimeS02On.getTag();
	// if (time != null) {
	// relationTrigger.append(time.getMinute());
	// relationTrigger.append(configManager.COMMAS);
	//
	// relationTrigger.append(time.getHour());
	// relationTrigger.append(configManager.COMMAS);
	// }
	//
	// // 31 --> day (1-31)
	// // 12 --> month (1-12)
	// MyDate date = (MyDate) mDayS02On.getTag();
	// if (date != null) {
	// relationTrigger.append(date.getDay());
	// relationTrigger.append(configManager.COMMAS);
	//
	// relationTrigger.append(date.getMonth());
	// relationTrigger.append(configManager.COMMAS);
	// }
	//
	// // repeat days
	// relationTrigger.append(cbMonS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbTueS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbWedS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbThuS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbFriS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbSatS2.isChecked() ? 1 : 0);
	// relationTrigger.append(cbSunS2.isChecked() ? 1 : 0);
	//
	// // #8014# --> schedule index: 8 -->room;
	// relationTrigger.append(configManager.COMMAS);
	// relationTrigger.append(configManager.SHIFT3);
	//
	// relationTrigger.append(roomId);
	// relationTrigger.append(devId);
	// relationTrigger.append(2);
	// relationTrigger.append(configManager.SHIFT3);
	// return relationTrigger.toString();
	// }

	private String saveShedule03(String devId) {
		StringBuffer relationTrigger = new StringBuffer();
		boolean isChecked = false;

		// 1 --> Add/Delete schedule (1,0)
		isChecked = cbAddDeleteS3.isChecked();
		relationTrigger.append(isChecked ? 1 : 0);
		relationTrigger.append(configManager.COMMAS);

		// 0 --> on/off device (1,0)
		if (mIsDevice) {
			isChecked = swOnOffS3.isChecked();
			relationTrigger.append(isChecked ? 1 : 0);
			relationTrigger.append(configManager.COMMAS);

		} else {
			relationTrigger.append('r');
			relationTrigger.append(configManager.COMMAS);
		}

		// 20 --> minute (1-59)
		// 1 --> hour (1-23)
		MyTime time = (MyTime) mTimeS03On.getTag();
		if (time != null) {
			relationTrigger.append(time.getMinute());
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append(time.getHour());
			relationTrigger.append(configManager.COMMAS);
		}

		// 31 --> day (1-31)
		// 12 --> month (1-12)
		if (rbDMYS03.isChecked()) {
			MyDate date = (MyDate) mDayS03On.getTag();
			if (date != null) {
				relationTrigger.append(date.getDay());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(date.getMonth());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
			}
		} else {
			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			// repeat days
			relationTrigger.append(cbMonS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbTueS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbWedS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbThuS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbFriS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbSatS3.isChecked() ? 1 : 0);
			relationTrigger.append(cbSunS3.isChecked() ? 1 : 0);
		}

		// #8014# --> schedule index: 8 -->room;
		relationTrigger.append(configManager.COMMAS);

		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);

		relationTrigger.append(roomId);
		relationTrigger.append(devId);
		relationTrigger.append(3);
		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);
		return relationTrigger.toString();

	}

	private String saveShedule04(String devId) {
		StringBuffer relationTrigger = new StringBuffer();
		boolean isChecked = false;

		// 1 --> Add/Delete schedule (1,0)
		isChecked = cbAddDeleteS4.isChecked();
		relationTrigger.append(isChecked ? 1 : 0);
		relationTrigger.append(configManager.COMMAS);

		// 0 --> on/off device (1,0)
		if (mIsDevice) {
			isChecked = swOnOffS4.isChecked();
			relationTrigger.append(isChecked ? 1 : 0);
			relationTrigger.append(configManager.COMMAS);

		} else {
			relationTrigger.append('r');
			relationTrigger.append(configManager.COMMAS);
		}

		// 20 --> minute (1-59)
		// 1 --> hour (1-23)
		MyTime time = (MyTime) mTimeS04On.getTag();
		if (time != null) {
			relationTrigger.append(time.getMinute());
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append(time.getHour());
			relationTrigger.append(configManager.COMMAS);
		}

		// 31 --> day (1-31)
		// 12 --> month (1-12)
		if (rbDMYS04.isChecked()) {
			MyDate date = (MyDate) mDayS04On.getTag();
			if (date != null) {
				relationTrigger.append(date.getDay());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(date.getMonth());
				relationTrigger.append(configManager.COMMAS);

				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
				relationTrigger.append(0);
			}
		} else {
			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			relationTrigger.append("*");
			relationTrigger.append(configManager.COMMAS);

			// repeat days
			relationTrigger.append(cbMonS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbTueS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbWedS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbThuS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbFriS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbSatS4.isChecked() ? 1 : 0);
			relationTrigger.append(cbSunS4.isChecked() ? 1 : 0);

		}

		// #8014# --> schedule index: 8 -->room;
		relationTrigger.append(configManager.COMMAS);

		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);

		relationTrigger.append(roomId);
		relationTrigger.append(devId);
		relationTrigger.append(4);

		if (mIsDevice)
			relationTrigger.append(configManager.SHIFT3);
		else
			relationTrigger.append(configManager.PER);

		return relationTrigger.toString();
	}

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
		isChecked = Boolean.valueOf(cbTemp1.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbLight1.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbSmoke1.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion1.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion2.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor1.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor2.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		// sensor 2
		isChecked = Boolean.valueOf(cbTemp2.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbLight2.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbSmoke2.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion3.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbMotion4.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor3.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		isChecked = Boolean.valueOf(cbDoor4.isChecked());
		relationTrigger.append(isChecked ? 1 : 0);

		return relationTrigger.toString();

	}

	/*
	 * xx20=8,01,1,0,20,1,31,12,0000000,#8014# / :stOK 8 --> room index (1-8) 01
	 * --> device# (01-10)
	 */

	public void save(int type) {
		DialogFragmentWrapper.showDialog(getFragmentManager(),
				DetailDeviceScreen.this, configManager.DIALOG_PROCESS);

		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, roomId);
		bundle.putString(configManager.DEVICE_ID, deviceId);
		bundle.putInt(configManager.TYPE, type);

		String roonSchedule = roomId + configManager.COMMAS + deviceId
				+ configManager.COMMAS;
		switch (type) {
		case configManager.TYPE_SHEDULE_01:
			roonSchedule += saveShedule01(deviceId);
			bundle.putString(configManager.SHEDULE, roonSchedule);
			break;
		case configManager.TYPE_SHEDULE_02:
			roonSchedule += saveShedule02(deviceId);
			bundle.putString(configManager.SHEDULE, roonSchedule);
			break;

		case configManager.TYPE_SHEDULE_03:
			roonSchedule += saveShedule03(deviceId);
			bundle.putString(configManager.SHEDULE, roonSchedule);
			break;
		case configManager.TYPE_SHEDULE_04:
			roonSchedule += saveShedule04(deviceId);
			bundle.putString(configManager.SHEDULE, roonSchedule);
			break;
		case configManager.TYPE_TURN_WHEN_DEVICE:
			if (mIsDevice) {

				boolean isChecked = cbSmartEnergy.isChecked();
				String roomTurn = roomId + configManager.COMMAS
						+ (isChecked ? "1" : "0") + configManager.COMMAS
						+ deviceId + configManager.COMMAS;

				roomTurn += saveTurnWhenDevice();
				bundle.putString(configManager.SHEDULE, roomTurn);

				bundle.putString(configManager.TURN_SMART_ENERGY,
						(isChecked ? "1" : "0"));
			} else {
				bundle.putString(configManager.SHEDULE, "");
				bundle.putString(configManager.TURN_SMART_ENERGY, "0");
			}
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
		// Schedule 01
		case R.id.editDayS01:
			showDayPickerDialog((TextView) v);
			break;
		case R.id.editTimeS01:
			showTimePickerDialog((TextView) v);
			break;

		case R.id.imgOnOffS01: {
			cbAddDeleteS1.toggle();
			break;
		}
		case R.id.swS01: // TMT
			swOnOffS1.toggle();
			break;
		case R.id.swS02: // TMT
			swOnOffS2.toggle();
			break;
		case R.id.swS03: // TMT
			swOnOffS3.toggle();
			break;
		case R.id.swS04: // TMT
			swOnOffS4.toggle();
			break;
		case R.id.cbMonS1:
			cbMonS1.toggle();
			break;
		case R.id.cbTueS1:
			cbTueS1.toggle();
			break;
		case R.id.cbWedS1:
			cbWedS1.toggle();
			break;
		case R.id.cbThuS1:
			cbThuS1.toggle();
			break;
		case R.id.cbFriS1:
			cbFriS1.toggle();
			break;
		case R.id.cbSatS1:
			cbSatS1.toggle();
			break;
		case R.id.cbSunS1:
			cbSunS1.toggle();
			break;

		case R.id.lnFootS01:
			lnFootS01.toggle();
			break;

		// Schedule 02
		case R.id.editDayS02:
			showDayPickerDialog((TextView) v);
			break;
		case R.id.editTimeS02:
			showTimePickerDialog((TextView) v);
			break;

		case R.id.imgOnOffS02: {
			cbAddDeleteS2.toggle();
			break;
		}
		case R.id.cbMonS2:
			cbMonS2.toggle();
			break;
		case R.id.cbTueS2:
			cbTueS2.toggle();
			break;
		case R.id.cbWedS2:
			cbWedS2.toggle();
			break;
		case R.id.cbThuS2:
			cbThuS2.toggle();
			break;
		case R.id.cbFriS2:
			cbFriS2.toggle();
			break;
		case R.id.cbSatS2:
			cbSatS2.toggle();
			break;
		case R.id.cbSunS2:
			cbSunS2.toggle();
			break;

		case R.id.lnFootS02:
			lnFootS02.toggle();
			break;
		// Schedule 03
		case R.id.editDayS03:
			showDayPickerDialog((TextView) v);
			break;
		case R.id.editTimeS03:
			showTimePickerDialog((TextView) v);
			break;

		case R.id.imgOnOffS03: {
			cbAddDeleteS3.toggle();
			break;
		}
		case R.id.cbMonS3:
			cbMonS3.toggle();
			break;
		case R.id.cbTueS3:
			cbTueS3.toggle();
			break;
		case R.id.cbWedS3:
			cbWedS3.toggle();
			break;
		case R.id.cbThuS3:
			cbThuS3.toggle();
			break;
		case R.id.cbFriS3:
			cbFriS3.toggle();
			break;
		case R.id.cbSatS3:
			cbSatS3.toggle();
			break;
		case R.id.cbSunS3:
			cbSunS3.toggle();
			break;

		case R.id.lnFootS03:
			lnFootS03.toggle();
			break;

		// Schedule 04
		case R.id.editDayS04:
			showDayPickerDialog((TextView) v);
			break;
		case R.id.editTimeS04:
			showTimePickerDialog((TextView) v);
			break;

		case R.id.imgOnOffS04: {
			cbAddDeleteS4.toggle();
			break;
		}
		case R.id.cbMonS4:
			cbMonS4.toggle();
			break;
		case R.id.cbTueS4:
			cbTueS4.toggle();
			break;
		case R.id.cbWedS4:
			cbWedS4.toggle();
			break;
		case R.id.cbThuS4:
			cbThuS4.toggle();
			break;
		case R.id.cbFriS4:
			cbFriS4.toggle();
			break;
		case R.id.cbSatS4:
			cbSatS4.toggle();
			break;
		case R.id.cbSunS4:
			cbSunS4.toggle();
			break;

		case R.id.lnFootS04:
			lnFootS04.toggle();
			break;

		// Information
		case R.id.lnFootInfo:
			lnFootInfo.toggle();
			break;

		// Turn when
		case R.id.lnFootWhen:
			lnFootWhen.toggle();
			break;

		case R.id.btnSyncS01:
			save(configManager.TYPE_SHEDULE_01);
			break;
		case R.id.btnSyncS02:
			save(configManager.TYPE_SHEDULE_02);
			break;
		case R.id.btnSyncS03:
			save(configManager.TYPE_SHEDULE_03);
			break;
		case R.id.btnSyncS04:
			save(configManager.TYPE_SHEDULE_04);
			break;
		case R.id.btnSyncDevices:
			save(configManager.TYPE_TURN_WHEN_DEVICE);
			break;
		case R.id.btnSyncInfo:
			saveInformation();
			break;
		default:
			break;
		}
	}

	@Override
	public void recieveStatusRelationship(StatusRelationship sr) {
		mSR = sr;
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "recieveStatusRelationship");
				refresh();
			}
		});
	}

	private void refresh() {
		if (mSR == null) {
			return;
		}
		/*
		 * whHourTurnOn.setCurrentItem(mSR.getHourOn() - 1);
		 * whMinuteTurnOn.setCurrentItem(mSR.getMinsOn() - 1);
		 * 
		 * whMonthTurnOn.setCurrentItem(mSR.getMonthOn() - 1);
		 * whDayTurnOn.setCurrentItem(mSR.getDayOn() - 1);
		 * 
		 * whHourTurnOff.setCurrentItem(mSR.getHourOff() - 1);
		 * whMinuteTurnOff.setCurrentItem(mSR.getMinsOff() - 1);
		 * 
		 * whMonthTurnOff.setCurrentItem(mSR.getMonthOff() - 1);
		 * whDayTurnOff.setCurrentItem(mSR.getDayOff() - 1);
		 */
		refreshSchedule01();
		refreshSchedule02();
		refreshSchedule03();
		refreshSchedule04();

		refreshSchduleDevice();

		DialogFragmentWrapper.removeDialog(getFragmentManager(),
				configManager.DIALOG_PROCESS);
	}

	private void refreshSchduleDevice() {
		if (mIsDevice) {
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
	}

	private void refreshSchedule01() {
		MyTime time = mSR.getTimeS01();
		configManager.setTime(mTimeS01On, time.getMinute(), time.getHour());

		MyDate date = mSR.getDateS01();
		configManager.setDate(mDayS01On, date.getDay(), date.getMonth(),
				date.getYear());

		swOnOffS1.setChecked(mSR.isOnS01());

		cbAddDeleteS1.setChecked(mSR.isAddedScheduleS01());

		cbMonS1.setChecked(mSR.isMonS01());
		cbTueS1.setChecked(mSR.isTueS01());
		cbWedS1.setChecked(mSR.isWedS01());
		cbThuS1.setChecked(mSR.isThuS01());
		cbFriS1.setChecked(mSR.isFriS01());
		cbSatS1.setChecked(mSR.isSatS01());
		cbSunS1.setChecked(mSR.isSunS01());
	}

	private void refreshSchedule02() {
		MyTime time = mSR.getTimeS02();
		configManager.setTime(mTimeS02On, time.getMinute(), time.getHour());

		MyDate date = mSR.getDateS02();
		configManager.setDate(mDayS02On, date.getDay(), date.getMonth(),
				date.getYear());

		swOnOffS2.setChecked(mSR.isOnS02());

		cbAddDeleteS2.setChecked(mSR.isAddedScheduleS02());

		cbMonS2.setChecked(mSR.isMonS02());
		cbTueS2.setChecked(mSR.isTueS02());
		cbWedS2.setChecked(mSR.isWedS02());
		cbThuS2.setChecked(mSR.isThuS02());
		cbFriS2.setChecked(mSR.isFriS02());
		cbSatS2.setChecked(mSR.isSatS02());
		cbSunS2.setChecked(mSR.isSunS02());
	}

	private void refreshSchedule03() {
		MyTime time = mSR.getTimeS03();
		configManager.setTime(mTimeS03On, time.getMinute(), time.getHour());

		MyDate date = mSR.getDateS03();
		configManager.setDate(mDayS03On, date.getDay(), date.getMonth(),
				date.getYear());

		swOnOffS3.setChecked(mSR.isOnS03());

		cbAddDeleteS3.setChecked(mSR.isAddedScheduleS03());

		cbMonS3.setChecked(mSR.isMonS03());
		cbTueS3.setChecked(mSR.isTueS03());
		cbWedS3.setChecked(mSR.isWedS03());
		cbThuS3.setChecked(mSR.isThuS03());
		cbFriS3.setChecked(mSR.isFriS03());
		cbSatS3.setChecked(mSR.isSatS03());
		cbSunS3.setChecked(mSR.isSunS03());
	}

	private void refreshSchedule04() {
		MyTime time = mSR.getTimeS04();
		configManager.setTime(mTimeS04On, time.getMinute(), time.getHour());

		MyDate date = mSR.getDateS04();
		configManager.setDate(mDayS04On, date.getDay(), date.getMonth(),
				date.getYear());

		swOnOffS4.setChecked(mSR.isOnS04());

		cbAddDeleteS4.setChecked(mSR.isAddedScheduleS04());

		cbMonS4.setChecked(mSR.isMonS04());
		cbTueS4.setChecked(mSR.isTueS04());
		cbWedS4.setChecked(mSR.isWedS04());
		cbThuS4.setChecked(mSR.isThuS04());
		cbFriS4.setChecked(mSR.isFriS04());
		cbSatS4.setChecked(mSR.isSatS04());
		cbSunS4.setChecked(mSR.isSunS04());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (uiEngine != null) {
			uiEngine.removeGettingSRObserver(this);
			uiEngine.removeXMLObserver(this);
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		if (mIsDevice) {
			if (room != null) {
				mActionBarV7.setTitle(room.getName());
			}
		} else {
			mActionBarV7.setTitle(name);
		}
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
		if (mIsDevice == true) {
			mActionBarV7.setIcon(mDevice.getIcon());
		}
	}

	@Override
	public void onPageDeselected() {
		Log.d(TAG, "onPageDeSelected");
		mActionBarV7.setIcon(R.drawable.ic_protection_totale);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		// Schedule devices
		case R.id.cbTemp1_device:
		case R.id.cbTemp2_device:
		case R.id.cbLight1_device:
		case R.id.cbLight2_device:
		case R.id.cbSmoke1_device:
		case R.id.cbSmoke2_device:
		case R.id.cbMotion1_device:
		case R.id.cbMotion2_device:
		case R.id.cbMotion3_device:
		case R.id.cbMotion4_device:
		case R.id.cbDoor1_device:
		case R.id.cbDoor2_device:
		case R.id.cbDoor3_device:
		case R.id.cbDoor4_device:
		case R.id.cbSmartEnergy_device:
			CheckBox viewDevice = (CheckBox) buttonView;
			setDeviceChecked(viewDevice, isChecked);
			break;
		case R.id.rbtnDMY01:// TMT
			radioBtnChanged(rbDMYS01, isChecked, rbDayS01);
			break;
		case R.id.rbtnDay01:
			radioBtnChanged(rbDayS01, isChecked, rbDMYS01);
			break;
		case R.id.rbtnDMY02:
			radioBtnChanged(rbDMYS02, isChecked, rbDayS02);
			break;
		case R.id.rbtnDay02:
			radioBtnChanged(rbDayS02, isChecked, rbDMYS02);
			break;
		case R.id.rbtnDMY03:
			radioBtnChanged(rbDMYS03, isChecked, rbDayS03);
			break;
		case R.id.rbtnDay03:
			radioBtnChanged(rbDayS03, isChecked, rbDMYS03);
			break;
		case R.id.rbtnDMY04:
			radioBtnChanged(rbDMYS04, isChecked, rbDayS04);
			break;
		case R.id.rbtnDay04:
			radioBtnChanged(rbDayS04, isChecked, rbDMYS04);
			break;
		default:
			break;
		}
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
	private void setDeviceChecked(CheckBox cb, boolean isChecked) {
		cb.setChecked(isChecked);
		if (isChecked) {
			cb.setTypeface(Typeface.DEFAULT_BOLD);
			cb.setTextColor(Color.parseColor("#00BFAf"));// (Color.RED);
		} else {
			cb.setTypeface(Typeface.DEFAULT);
			cb.setTextColor(Color.BLACK);
		}
	}

	/*
	 * DayLayout cbMonS1, cbTueS1, cbWedS1, cbThuS1, cbFriS1, cbSatS1, cbSunS1;
	 * 
	 * 
	 * TextView mDayS01On, mTimeS01On; FootLayout lnFootS01; LinearLayout
	 * lnHeaderS01, lnBodyS01;
	 */

	private void setAddedS01(boolean isChecked) {
		if (isChecked) {
			lnHeaderS01
					.setBackgroundResource(R.drawable.restangle_corner_top_yellow_bright);
			mTimeS01On.setTextColor(mContext.getResources().getColor(
					R.color.white));
			mDayS01On.setTextColor(mContext.getResources().getColor(
					R.color.black));
		} else {
			lnHeaderS01
					.setBackgroundResource(R.drawable.restangle_corner_top_grey);
			mTimeS01On.setTextColor(mContext.getResources().getColor(
					R.color.grey));
			mDayS01On.setTextColor(mContext.getResources().getColor(
					R.color.grey));
		}
		cbAddDeleteS1.setBackgroundResource(android.R.color.transparent);

		cbMonS1.setAdded(isChecked);
		cbTueS1.setAdded(isChecked);
		cbWedS1.setAdded(isChecked);
		cbThuS1.setAdded(isChecked);
		cbFriS1.setAdded(isChecked);
		cbSatS1.setAdded(isChecked);
		cbSunS1.setAdded(isChecked);

		lnFootS01.setAdded(isChecked);
	}

	private void setAddedS02(boolean isChecked) {
		if (isChecked) {
			lnHeaderS02
					.setBackgroundResource(R.drawable.restangle_corner_top_yellow_bright);
			mTimeS02On.setTextColor(mContext.getResources().getColor(
					R.color.white));
			mDayS02On.setTextColor(mContext.getResources().getColor(
					R.color.black));

		} else {
			lnHeaderS02
					.setBackgroundResource(R.drawable.restangle_corner_top_grey);
			mTimeS02On.setTextColor(mContext.getResources().getColor(
					R.color.grey));
			mDayS02On.setTextColor(mContext.getResources().getColor(
					R.color.grey));

		}
		cbAddDeleteS2.setBackgroundResource(android.R.color.transparent);

		cbMonS2.setAdded(isChecked);
		cbTueS2.setAdded(isChecked);
		cbWedS2.setAdded(isChecked);
		cbThuS2.setAdded(isChecked);
		cbFriS2.setAdded(isChecked);
		cbSatS2.setAdded(isChecked);
		cbSunS2.setAdded(isChecked);

		lnFootS02.setAdded(isChecked);
	}

	private void setAddedS03(boolean isChecked) {
		if (isChecked) {
			lnHeaderS03
					.setBackgroundResource(R.drawable.restangle_corner_top_yellow_bright);
			mTimeS03On.setTextColor(mContext.getResources().getColor(
					R.color.white));
			mDayS03On.setTextColor(mContext.getResources().getColor(
					R.color.black));

		} else {
			lnHeaderS03
					.setBackgroundResource(R.drawable.restangle_corner_top_grey);
			mTimeS03On.setTextColor(mContext.getResources().getColor(
					R.color.grey));
			mDayS03On.setTextColor(mContext.getResources().getColor(
					R.color.grey));

		}

		cbAddDeleteS3.setBackgroundResource(android.R.color.transparent);

		cbMonS3.setAdded(isChecked);
		cbTueS3.setAdded(isChecked);
		cbWedS3.setAdded(isChecked);
		cbThuS3.setAdded(isChecked);
		cbFriS3.setAdded(isChecked);
		cbSatS3.setAdded(isChecked);
		cbSunS3.setAdded(isChecked);

		lnFootS03.setAdded(isChecked);
	}

	private void setAddedS04(boolean isChecked) {
		if (isChecked) {
			lnHeaderS04
					.setBackgroundResource(R.drawable.restangle_corner_top_yellow_bright);
			mTimeS04On.setTextColor(mContext.getResources().getColor(
					R.color.white));
			mDayS04On.setTextColor(mContext.getResources().getColor(
					R.color.black));

		} else {
			lnHeaderS04
					.setBackgroundResource(R.drawable.restangle_corner_top_grey);
			mTimeS04On.setTextColor(mContext.getResources().getColor(
					R.color.grey));
			mDayS04On.setTextColor(mContext.getResources().getColor(
					R.color.grey));

		}
		cbAddDeleteS4.setBackgroundResource(android.R.color.transparent);

		cbMonS4.setAdded(isChecked);
		cbTueS4.setAdded(isChecked);
		cbWedS4.setAdded(isChecked);
		cbThuS4.setAdded(isChecked);
		cbFriS4.setAdded(isChecked);
		cbSatS4.setAdded(isChecked);
		cbSunS4.setAdded(isChecked);

		lnFootS04.setAdded(isChecked);
	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		switch (view.getId()) {
		// Schedule 01
		case R.id.imgOnOffS01:
			setAddedS01(isChecked);
			break;
		case R.id.lnFootS01:
			if (isChecked) {
				setAnimation(lnBodyS01, View.VISIBLE);
			} else {
				setFootTextS01();
				setAnimation(lnBodyS01, View.GONE);
			}
			break;
		// Schedule 02
		case R.id.imgOnOffS02:
			setAddedS02(isChecked);
			break;
		case R.id.lnFootS02:
			if (isChecked) {
				setAnimation(lnBodyS02, View.VISIBLE);
			} else {
				setFootTextS02();
				setAnimation(lnBodyS02, View.GONE);
			}
			break;
		// Schedule 03
		case R.id.imgOnOffS03:
			setAddedS03(isChecked);
			break;
		case R.id.lnFootS03:
			if (isChecked) {
				setAnimation(lnBodyS03, View.VISIBLE);
			} else {
				setFootTextS03();
				setAnimation(lnBodyS03, View.GONE);
			}
			break;
		// Schedule 04
		case R.id.imgOnOffS04:
			setAddedS04(isChecked);
			break;
		case R.id.lnFootS04:
			if (isChecked) {
				setAnimation(lnBodyS04, View.VISIBLE);
			} else {
				setFootTextS04();
				setAnimation(lnBodyS04, View.GONE);
			}
			break;

		case R.id.lnFootInfo:
			if (isChecked) {
				setAnimation(lnBodyInfo, View.VISIBLE);
			} else {
				setAnimation(lnBodyInfo, View.GONE);
			}
			break;

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

	private void setFootTextS01() {
		StringBuffer buffer = new StringBuffer();
		boolean isFirst = true;
		int count = 0;
		if (cbMonS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.mon));
			count++;
		}
		if (cbTueS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.tue));
			count++;
		}
		if (cbWedS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.wed));
			count++;
		}
		if (cbThuS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.thu));
			count++;
		}
		if (cbFriS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.fri));
			count++;
		}
		if (cbSatS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sat));
			count++;
		}
		if (cbSunS1.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sun));
			count++;
		}
		if (count == 7) {
			mDayOnWeekS01.setText(R.string.everyday);
		} else {
			mDayOnWeekS01.setText(buffer.toString());
		}
	}

	private void setFootTextS02() {
		StringBuffer buffer = new StringBuffer();
		boolean isFirst = true;
		int count = 0;
		if (cbMonS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.mon));
			count++;
		}
		if (cbTueS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.tue));
			count++;
		}
		if (cbWedS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.wed));
			count++;
		}
		if (cbThuS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.thu));
			count++;
		}
		if (cbFriS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.fri));
			count++;
		}
		if (cbSatS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sat));
			count++;
		}
		if (cbSunS2.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sun));
			count++;
		}
		if (count == 7) {
			mDayOnWeekS02.setText(R.string.everyday);
		} else {
			mDayOnWeekS02.setText(buffer.toString());
		}
	}

	private void setFootTextS03() {
		StringBuffer buffer = new StringBuffer();
		boolean isFirst = true;
		int count = 0;
		if (cbMonS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.mon));
			count++;
		}
		if (cbTueS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.tue));
			count++;
		}
		if (cbWedS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.wed));
			count++;
		}
		if (cbThuS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.thu));
			count++;
		}
		if (cbFriS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.fri));
			count++;
		}
		if (cbSatS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sat));
			count++;
		}
		if (cbSunS3.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sun));
			count++;
		}
		if (count == 7) {
			mDayOnWeekS03.setText(R.string.everyday);
		} else {
			mDayOnWeekS03.setText(buffer.toString());
		}
	}

	private void setFootTextS04() {
		StringBuffer buffer = new StringBuffer();
		boolean isFirst = true;
		int count = 0;
		if (cbMonS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.mon));
			count++;
		}
		if (cbTueS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.tue));
			count++;
		}
		if (cbWedS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.wed));
			count++;
		}
		if (cbThuS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.thu));
			count++;
		}
		if (cbFriS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.fri));
			count++;
		}
		if (cbSatS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sat));
			count++;
		}
		if (cbSunS4.isChecked()) {
			if (!isFirst) {
				buffer.append(configManager.COMMAS);
			} else {
				isFirst = false;
			}
			buffer.append(mContext.getString(R.string.sun));
			count++;
		}
		if (count == 7) {
			mDayOnWeekS04.setText(R.string.everyday);
		} else {
			mDayOnWeekS04.setText(buffer.toString());
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

	private void initInformationDetail(View view) {
		mNameTv = (TextView) view.findViewById(R.id.txtNameInfo);

		lnBodyInfo = (LinearLayout) view.findViewById(R.id.lnBodyInfo);
		setAnimation(lnBodyInfo, View.GONE);

		lnFootInfo = (FootLayout) view.findViewById(R.id.lnFootInfo);
		lnFootInfo.setOnClickListener(this);
		lnFootInfo.setOnCheckChangedListener(this);

		mlnInfoDetail = (LinearLayout) view.findViewById(R.id.lnInfoDevice);

		mlnInfomation = (LinearLayout) view.findViewById(R.id.lnInfo);

		btnSynInfo = (ImageButton) view.findViewById(R.id.btnSyncInfo);
		btnSynInfo.setOnClickListener(this);

		mNameEdit = (EditText) view.findViewById(R.id.editNameAddDevice);

		lnFootInfo.setChecked(false);
	}

	private void saveInformation() {
		Bundle bundle = new Bundle();
		String name = mNameEdit.getText().toString();
		if (!TextUtils.isEmpty(name)) {
			mDevice.setName(name);
			bundle.putInt("id", mDevice.getId());
			bundle.putString(configManager.NAME_BUNDLE, name);
			RegisterService service = RegisterService.getService();
			if (service == null)
				return;
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine == null) {
				return;
			}
			Room room = (Room) uiEngine.getHouse().getRoomsById(roomId);

			List<Control> devices = room.getControls();
			for (int i = 0; i < devices.size(); i++) {
				Device dv = devices.get(i);
				if (dv.getId() == mDevice.getId()) {
					dv.setName(name);
				}
			}
			uiEngine.saveRoom(room);
		}
	}

	public void setAnimation(LinearLayout linear, int visible) {
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

	@Override
	public void savedXML() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mIsDevice && mDevice != null) {
					String name = mDevice.getName();
					mNameEdit.setText(name, TextView.BufferType.EDITABLE);
					mNameTv.setText(name);

				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mIsDevice && mDevice != null) {
			mlnInfoDetail.setVisibility(View.VISIBLE);
			String name = mDevice.getName();
			Log.d(TAG, "TMT name: " + name);
			mNameTv.setText(name);
			mNameEdit.setText(name);
		} else {
			mlnInfoDetail.setVisibility(View.GONE);
		}
	}

	private void radioBtnChanged(RadioButton rbChange, boolean isChecked,
			RadioButton rbRelative) {
		rbChange.setOnCheckedChangeListener(null);
		rbRelative.setOnCheckedChangeListener(null);
		if (isChecked) {
			rbRelative.setChecked(false);
		} else {
			rbRelative.setChecked(true);
		}
		rbChange.setOnCheckedChangeListener(this);
		rbRelative.setOnCheckedChangeListener(this);

	}
}
