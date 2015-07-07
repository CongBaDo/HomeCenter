package com.HomeCenter2.ui.mainscreen;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.StatusRelationship;
import com.HomeCenter2.ui.DatePickerFragment;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.TimePickerFragment;
import com.HomeCenter2.ui.adapter.MyDeviceTypeAdapter;
import com.HomeCenter2.ui.listener.GetStatusRelationshipListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class ScheduleRemoteScreen extends RADialerMainScreenAbstract implements
		OnClickListener, GetStatusRelationshipListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener {

	private final int CHANGE_TYPE_DIALOG = 0;
	private static StatusRelationship mSR = null;	
	private LayoutInflater mInflater = null;

	public ScheduleRemoteScreen(int title, String tag,SlidingBaseActivity context) {
		super(ScheduleRemoteScreen.class, title, tag,context);
	}

	public static final String TAG = "TMT ScheduleRemoteScreen";
	public static Bundle mBundle;
	public static ScheduleRemoteScreen m_instance = null;
	public String mButton;
	public Room mRoom = null;
	private ImageView mIcon;
	Time mNow;

	CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun, cbSmartEnergy,
			cbTemp1, cbTemp2, cbLight1, cbLight2, cbSmoke1, cbSmoke2,
			cbMotion1, cbMotion2, cbMotion3, cbMotion4, cbDoor1, cbDoor2,
			cbDoor3, cbDoor4;

	Button btnTurnOnOffWhen;
	EditText  mDayS01, mTimeS01, mDayS02, mTimeS02, mDayS03, mTimeS03, mDayS04, mTimeS04;

	ScrollView scrollView = null;	

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
		mRoom = (Room) mBundle.getSerializable(configManager.ROOM_BUNDLE);
		mButton = mBundle.getString(configManager.BUTTON_BUNDLE);
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {				
				uiEngine.addGettingSRObserver(this);
			}
		}
		View view = inflater.inflate(R.layout.remote_schedule_screen, container,
				false);
		initView(view);		
		return view;
	}

	private void initView(View view) {

		scrollView = (ScrollView) view.findViewById(R.id.scrv_detail_device);

		mNow = new Time();
		mNow.setToNow();

		// time

		mDayS01 = (EditText) view.findViewById(R.id.editDayS01);
		mDayS01.setOnClickListener(this);		
		configManager.setDate(mDayS01, mNow.monthDay, mNow.month + 1, mNow.year);

		mTimeS01 = (EditText) view.findViewById(R.id.editTimeS01);
		mTimeS01.setOnClickListener(this);
		configManager.setTime(mTimeS01, mNow.minute, mNow.hour);

		mDayS02 = (EditText) view.findViewById(R.id.editDayS02);
		mDayS02.setOnClickListener(this);
		configManager.setDate(mDayS02, mNow.monthDay, mNow.month + 1, mNow.year);

		mTimeS02 = (EditText) view.findViewById(R.id.editTimeS02);
		mTimeS02.setOnClickListener(this);
		configManager.setTime(mTimeS02, mNow.minute, mNow.hour);
		
		mDayS03 = (EditText) view.findViewById(R.id.editDayS03);
		mDayS03.setOnClickListener(this);
		configManager.setDate(mDayS03, mNow.monthDay, mNow.month + 1, mNow.year);

		mTimeS03 = (EditText) view.findViewById(R.id.editTimeS03);
		mTimeS03.setOnClickListener(this);
		configManager.setTime(mTimeS03, mNow.minute, mNow.hour);
		
		mDayS04 = (EditText) view.findViewById(R.id.editDayS04);
		mDayS04.setOnClickListener(this);
		configManager.setDate(mDayS04, mNow.monthDay, mNow.month + 1, mNow.year);

		mTimeS04 = (EditText) view.findViewById(R.id.editTimeS04);
		mTimeS04.setOnClickListener(this);
		configManager.setTime(mTimeS04, mNow.minute, mNow.hour);


		// device

		cbSmartEnergy = (CheckBox) view.findViewById(R.id.cbSmartEnergy);

		cbTemp1 = (CheckBox) view.findViewById(R.id.cbTemp1);
		cbTemp2 = (CheckBox) view.findViewById(R.id.cbTemp2);
		cbLight1 = (CheckBox) view.findViewById(R.id.cbLight1);
		cbLight2 = (CheckBox) view.findViewById(R.id.cbLight2);
		cbSmoke1 = (CheckBox) view.findViewById(R.id.cbSmoke1);
		cbSmoke2 = (CheckBox) view.findViewById(R.id.cbSmoke2);
		cbMotion1 = (CheckBox) view.findViewById(R.id.cbMotion1);
		cbMotion2 = (CheckBox) view.findViewById(R.id.cbMotion2);
		cbMotion3 = (CheckBox) view.findViewById(R.id.cbMotion3);
		cbMotion4 = (CheckBox) view.findViewById(R.id.cbMotion4);
		cbDoor1 = (CheckBox) view.findViewById(R.id.cbDoor1);
		cbDoor2 = (CheckBox) view.findViewById(R.id.cbDoor2);
		cbDoor3 = (CheckBox) view.findViewById(R.id.cbDoor3);
		cbDoor4 = (CheckBox) view.findViewById(R.id.cbDoor4);

		btnTurnOnOffWhen = (Button) view.findViewById(R.id.btnTurnOnOffWhen);
		btnTurnOnOffWhen.setOnClickListener(this);

		if (mRoom != null) {
			List<Sensor> listDevices = mRoom.getSensors();
			Device device = null;
			int size = listDevices.size();
			for (int i = 0; i < size; i++) {
				device = listDevices.get(i);
				int deviceId = device.getId();
				if (deviceId == configManager.TEMPERATURE_1) {
					cbTemp1.setText(device.getName());
				} else if (deviceId == configManager.TEMPERATURE_2) {
					cbTemp2.setText(device.getName());
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

	public static ScheduleRemoteScreen initializeDetailDeviceScreen(
			Bundle bundle, int titleId, String tag,SlidingBaseActivity mContext) {
		mBundle = bundle;
		if (m_instance == null) {
			m_instance = new ScheduleRemoteScreen(titleId, tag, mContext);
		}
		mSR = null;
		return m_instance;
	}

	public void save() {
		String relationTrigger = "";
		// sensor 1
		relationTrigger += (cbTemp1.isChecked() ? 1 : 0);
		relationTrigger += (cbLight1.isChecked() ? 1 : 0);
		relationTrigger += (cbSmoke1.isChecked() ? 1 : 0);
		relationTrigger += (cbMotion1.isChecked() ? 1 : 0);
		relationTrigger += (cbMotion2.isChecked() ? 1 : 0);
		relationTrigger += (cbDoor1.isChecked() ? 1 : 0);
		relationTrigger += (cbDoor2.isChecked() ? 1 : 0);
		// sensor 2
		relationTrigger += (cbTemp2.isChecked() ? 1 : 0);
		relationTrigger += (cbLight2.isChecked() ? 1 : 0);
		relationTrigger += (cbSmoke2.isChecked() ? 1 : 0);
		relationTrigger += (cbMotion3.isChecked() ? 1 : 0);
		relationTrigger += (cbMotion4.isChecked() ? 1 : 0);
		relationTrigger += (cbDoor3.isChecked() ? 1 : 0);
		relationTrigger += (cbDoor4.isChecked() ? 1 : 0);

		// action
		String trigger = "0";
		String strTrigger = btnTurnOnOffWhen.getText().toString();
		if (strTrigger.equals(getString(R.string.turned_on_when))) {
			trigger = "1";
		} else {
			trigger = "0";
		}

		// time on/off
		relationTrigger += "1" + trigger + ",";
		int hourOn, minsOn, monthOn, dayOn;
		int hourOff, minsOff, monthOff, dayOff, timeOn, timeOff;

		hourOn = 1;
		minsOn = 1;
		monthOn = 1;
		dayOn = 1;
		hourOff = 1;
		minsOff = 1;
		monthOff = 1;
		dayOff = 1;

		/*
		 * hourOn = whHourTurnOn.getCurrentItem() + 1; minsOn =
		 * whMinuteTurnOn.getCurrentItem() + 1; monthOn =
		 * whMonthTurnOn.getCurrentItem() + 1; dayOn =
		 * whDayTurnOn.getCurrentItem() + 1;
		 */
		Log.d(TAG, "Time on: hour: " + hourOn + ", min: " + minsOn + ", month:"
				+ monthOn + " , dayOn: " + dayOn);

		/*
		 * hourOff = whHourTurnOff.getCurrentItem() + 1; minsOff =
		 * whMinuteTurnOff.getCurrentItem() + 1; monthOff =
		 * whMonthTurnOff.getCurrentItem() + 1; dayOff =
		 * whDayTurnOff.getCurrentItem() + 1;
		 */
		timeOn = (hourOn * 60) + minsOn;
		timeOff = (hourOff * 60) + minsOff;

		if (timeOn / 10 < 10) {
			relationTrigger += "000" + timeOn + ",";
		} else if (timeOn / 10 < 100) {
			relationTrigger += "00" + timeOn + ",";
		} else if (timeOn / 10 < 1000) {
			relationTrigger += "0" + timeOn + ",";
		} else {
			relationTrigger += timeOn + ",";
		}

		if (timeOff / 10 < 10) {
			relationTrigger += "000" + timeOff + ",";
		} else if (timeOff / 10 < 100) {
			relationTrigger += "00" + timeOff + ",";
		} else if (timeOff / 10 < 1000) {
			relationTrigger += "0" + timeOff + ",";
		} else {
			relationTrigger += timeOff + ",";
		}

		// day and month

		relationTrigger += dayOn + ",";
		relationTrigger += monthOn + ",";
		relationTrigger += dayOff + ",";
		relationTrigger += monthOff + ",";

		relationTrigger += (cbMon.isChecked() ? 1 : 0);
		relationTrigger += (cbTue.isChecked() ? 1 : 0);
		relationTrigger += (cbWed.isChecked() ? 1 : 0);
		relationTrigger += (cbThu.isChecked() ? 1 : 0);
		relationTrigger += (cbFri.isChecked() ? 1 : 0);
		relationTrigger += (cbSat.isChecked() ? 1 : 0);
		relationTrigger += (cbSun.isChecked() ? 1 : 0);

		relationTrigger += (cbSmartEnergy.isChecked() ? 1 : 0);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTurnOnOffWhen:
			String value = btnTurnOnOffWhen.getText().toString();
			if (value.equals(mContext.getString(R.string.turned_on_when))) {
				btnTurnOnOffWhen.setText(mContext
						.getString(R.string.turned_off_when));
			} else {
				btnTurnOnOffWhen.setText(mContext
						.getString(R.string.turned_on_when));
			}
			break;
		case R.id.editDayS01:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTimeS01:
			showTimePickerDialog((EditText) v);
			break;
		case R.id.editDayS02:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTimeS02:
			showTimePickerDialog((EditText) v);
			break;
		case R.id.editDayS03:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTimeS03:
			showTimePickerDialog((EditText) v);
			break;
		case R.id.editDayS04:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTimeS04:
			showTimePickerDialog((EditText) v);
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
				refresh();
			}
		});
	}

	public void refresh() {
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
		/*cbMon.setChecked(mSR.isMon());
		cbTue.setChecked(mSR.isTue());
		cbWed.setChecked(mSR.isWed());
		cbThu.setChecked(mSR.isThu());
		cbFri.setChecked(mSR.isFri());
		cbSat.setChecked(mSR.isSat());
		cbSun.setChecked(mSR.isSun());
*/
		cbSmartEnergy.setChecked(mSR.isSE());

		btnTurnOnOffWhen.setText(mSR.isTrigger() ? mContext
				.getString(R.string.turned_on_when) : mContext
				.getString(R.string.turned_off_when));

		cbTemp1.setChecked(mSR.isTemp1());
		cbLight1.setChecked(mSR.isLight1());
		cbSmoke1.setChecked(mSR.isSmoke1());
		cbMotion1.setChecked(mSR.isMotion1());
		cbMotion2.setChecked(mSR.isMotion2());
		cbDoor1.setChecked(mSR.isDoor1());
		cbDoor2.setChecked(mSR.isDoor2());

		cbTemp2.setChecked(mSR.isTemp2());
		cbLight2.setChecked(mSR.isLight2());
		cbSmoke2.setChecked(mSR.isSmoke2());
		cbMotion3.setChecked(mSR.isMotion3());
		cbMotion4.setChecked(mSR.isMotion4());
		cbDoor3.setChecked(mSR.isDoor3());
		cbDoor4.setChecked(mSR.isDoor4());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				uiEngine.removeGettingSRObserver(this);
			}
		}
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setTitle(R.string.media_button);
		mActionBarV7.setDisplayShowTitleEnabled(true);
		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		inflater.inflate(R.menu.my_parameter_menu, menu);
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
			case R.id.save_menu:
				save();
				isSelected = true;
				break;
			}
		}
		return isSelected;
	}

	public AlertDialog mDialog = null;
	ListView mAllAppsLV = null;
	MyDeviceTypeAdapter mAppsAdapter;


	@Override
	public Dialog onCreateDialog(int id) {
		return null;
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	public void showTimePickerDialog(EditText v) {
		DialogFragment newFragment = new TimePickerFragment(v);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void showDayPickerDialog(EditText v) {
		DialogFragment newFragment = new DatePickerFragment(v);
		newFragment.show(getFragmentManager(), "datePicker");
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
