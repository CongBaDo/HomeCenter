package com.HomeCenter2.ui.mainscreen;

import java.util.List;
import java.util.concurrent.locks.Lock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.MyTime;
import com.HomeCenter2.data.Password;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DatePickerFragment;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.TimePickerFragment;
import com.HomeCenter2.ui.listener.ClockListener;
import com.HomeCenter2.ui.listener.ConfigListener;
import com.HomeCenter2.ui.listener.LoginListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class ParameterConfigDevice extends RADialerMainScreenAbstract implements
		LoginListener, DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnCheckedChangeListener, OnClickListener, ClockListener {

	public static final String TAG = "TMT ParameterConfigDevice";

	public ParameterConfigDevice(int title, String tag,
			SlidingBaseActivity context) {
		super(ParameterConfigDevice.class, title, tag, context);
	}

	EditText mEditAdmin, mEditPassword,  mEditLight, mEditSmoke,
			mEditTemp, mEditPhone1, mEditPhone2, mEditPhone3, mEditGSM;

	public static ParameterConfigDevice nInstance = null;
	String[] mRoomIds;
	List<Room> mRooms;
	Dialog mDialog = null;

	CheckBox cbShowPassword;

	EditText mDay, mTime;

	Button mSync;

	public static ParameterConfigDevice initializeParameterConfigDevice(
			int titleId, String tag, SlidingBaseActivity mContext) {

		if (nInstance == null) {
			nInstance = new ParameterConfigDevice(titleId, tag, mContext);
		}
		return nInstance;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.parameter_config_device,
				container, false);
		mEditAdmin = (EditText) view.findViewById(R.id.editAdmin);
		mEditPassword = (EditText) view.findViewById(R.id.editPassword);
		cbShowPassword = (CheckBox) view.findViewById(R.id.cbShowPassword);
		cbShowPassword.setOnCheckedChangeListener(this);
		
		mEditLight = (EditText) view.findViewById(R.id.editLightLevelTrigger);
		mEditSmoke = (EditText) view.findViewById(R.id.editSmokeLevelTrigger);
		mEditTemp = (EditText) view.findViewById(R.id.editTemperatureTrigger);
		mEditPhone1 = (EditText) view.findViewById(R.id.editPhone01);
		mEditPhone2 = (EditText) view.findViewById(R.id.editPhone02);
		mEditPhone3 = (EditText) view.findViewById(R.id.editPhone03);
		mEditGSM = (EditText) view.findViewById(R.id.editGSMMessage);

		mDay = (EditText) view.findViewById(R.id.editDay);
		mDay.setOnClickListener(this);

		mTime = (EditText) view.findViewById(R.id.editTime);
		mTime.setOnClickListener(this);

		mSync = (Button) view.findViewById(R.id.btnSync);
		mSync.setOnClickListener(this);

		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null) {
			return null;
		}
		uiEngine.addLoginObserver(this);
		uiEngine.addClockObserver(this);
		getTimeServer();
		refreshContentView();
		return view;

	}

	@Override
	public void onScreenSlidingCompleted() {
		// TODO Auto-generated method stub
		getTimeServer();
		refreshContentView();
	}

	@Override
	public void loadHeader() {

	}

	@Override
	public void setContentMenu() {

	}

	private void refershTimer(House house) {
		MyTime time = house.getTime();
		configManager.setTime(mTime, time.getMinute(), time.getHour());

		MyDate date = house.getDate();
		configManager.setDate(mDay, date.getDay(), date.getMonth(),
				date.getYear());
	}

	private void refreshContentView() {
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null) {
			return;
		}

		House house = uiEngine.getHouse();

		mRoomIds = house.getDeviceID();
		mRooms = house.getRooms();

		refershTimer(house);

		Log.e(TAG, "refreshContentView::" + house.getPassword().getName());
		if (!TextUtils.isEmpty(house.getPassword().getName()))
			mEditAdmin.setText(house.getPassword().getName());

		if (!TextUtils.isEmpty(house.getPassword().getPassword()))
			mEditPassword.setText(house.getPassword().getPassword());
		
		String temp = null;
		temp = String.valueOf(house.getLightLevelTrigger());
		Log.d(TAG, "temp: " + temp);
		if (!TextUtils.isEmpty(temp))
			mEditLight.setText(temp);
		else
			mEditLight.setText("0");

		temp = String.valueOf(String.valueOf(house.getSmokeLevelTrigger()));
		if (!TextUtils.isEmpty(temp))
			mEditSmoke.setText(temp);
		else
			mEditSmoke.setText(0);

		temp = String.valueOf(String.valueOf(house.getTemperatureTrigger()));
		if (!TextUtils.isEmpty(temp))
			mEditTemp.setText(String.valueOf(house.getTemperatureTrigger()));
		else
			mEditTemp.setText(0);

		if (!TextUtils.isEmpty(house.getPhone01()))
			mEditPhone1.setText(house.getPhone01());

		if (!TextUtils.isEmpty(house.getPhone02()))
			mEditPhone2.setText(house.getPhone02());

		if (!TextUtils.isEmpty(house.getPhone03()))
			mEditPhone3.setText(house.getPhone03());

		if (!TextUtils.isEmpty(house.getGsm()))
			mEditGSM.setText(house.getGsm());

		int i = 1;
	}

	@Override
	public void eventLogined(boolean isConnected) {
		mContext.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (ParameterConfigDevice.this.isVisible()) {
					refreshContentView();
				}

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine == null)
			return;
		uiEngine.removeLoginObserver(this);
		uiEngine.removeClockObserver(this);

	}

	public void saveConfig() {
		RegisterService service = RegisterService.getService();
		HomeCenterUIEngine uiEngine = null;
		House house = null;
		if (service != null) {
			uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				house = uiEngine.getHouse();
				if (house != null) {
					Password ps = house.getPassword();
					ps.setName(mEditAdmin.getText().toString().trim());
					ps.setPassword(mEditPassword.getText().toString().trim());
				}
			}
		}
		/*
		 * if (uiEngine.isLogined() == false) { return; }
		 */
		Bundle bundle = new Bundle();
		StringBuffer strConfigBuffer = new StringBuffer();

		strConfigBuffer.append(mEditAdmin.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditPassword.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditLight.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditSmoke.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditTemp.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditPhone1.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditPhone2.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditPhone3.getText().toString());
		strConfigBuffer.append(",");
		strConfigBuffer.append(mEditGSM.getText().toString());

		bundle.putString(configManager.SAVE_CONFIG, strConfigBuffer.toString());
		Log.d(TAG, "SaveDevices:" + " , config:" + strConfigBuffer.toString());
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_CONFIG;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);

	}

	private void appendBufferByNumber(StringBuffer buffer, int value) {
		if (value > 0 && value < 10) {
			buffer.append('0');
		}
	}

	private void appendBufferYearByNumber(StringBuffer buffer, int value) {
		if (value > 0 && value < 10) {
			buffer.append("000");
		} else if (value >= 10 && value < 100) {
			buffer.append("00");
		} else if (value >= 100 && value < 1000) {
			buffer.append('0');
		}
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_SET_ON_OFF_DEVICE_FAIL:
			return showSetOnOffDeviceFail();
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

	public void showDialog(int action) {
		DialogFragmentWrapper.showDialog(getFragmentManager(), this, action);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cbShowPassword:
			if (isChecked == true) {
				mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			return;
		default:
			break;
		}
		/*
		 * Bundle bundle = new Bundle(); int roomId = 0;
		 * bundle.putInt(configManager.ROOM_ID, roomId);
		 * bundle.putString(configManager.DEVICE_ID, "00");
		 * bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked); Log.e(TAG,
		 * "onCheckedChanged::room id: " + roomId + " , isOn: " + isChecked);
		 * 
		 * Message message = Message.obtain(); message.setData(bundle);
		 * message.what= HCRequest.REQUEST_SET_DEVICE_STATUS;
		 * RegisterService.getHomeCenterUIEngine().sendMessage(message);
		 */
	}

	private Dialog showSetOnOffDeviceFail() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = new AlertDialog.Builder(mContext)
				.setTitle(R.string.warning)
				.setMessage(R.string.set_on_off_fail)
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

	public void resultSetDevice(boolean isSetOnOffDevice) {
		if (isSetOnOffDevice == false) {
			DialogFragmentWrapper.showDialog(getFragmentManager(), this,
					configManager.DIALOG_SET_ON_OFF_DEVICE_FAIL);
		} else {

		}
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editDay:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTime:
			showTimePickerDialog((EditText) v);
			break;
		case R.id.btnSync:
			syncTimeServer();
			break;
		}
	}

	public void showTimePickerDialog(EditText v) {
		DialogFragment newFragment = new TimePickerFragment(v);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void showDayPickerDialog(EditText v) {
		DialogFragment newFragment = new DatePickerFragment(v);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void syncTimeServer() {
		MyTime time = (MyTime) mTime.getTag();

		/*
		 * 1717=123456789012 / :skOK
		 * 
		 * 1234 --> year (2014) 56 --> month (01-12) 78 --> day (01-31) 90 -->
		 * hour (01-23) 12 --> minute (01-59)
		 */

		Bundle bundle = new Bundle();
		StringBuffer strConfigBuffer = new StringBuffer();
		MyDate date = (MyDate) mDay.getTag();
		if (date != null) {
			int year = date.getYear();
			appendBufferYearByNumber(strConfigBuffer, year);
			strConfigBuffer.append(year);

			int month = date.getMonth();
			appendBufferByNumber(strConfigBuffer, month);
			strConfigBuffer.append(month);

			int day = date.getDay();
			appendBufferByNumber(strConfigBuffer, day);
			strConfigBuffer.append(day);
		}

		if (time != null) {
			int hour = time.getHour();
			appendBufferByNumber(strConfigBuffer, hour);
			strConfigBuffer.append(hour);

			int min = time.getMinute();
			appendBufferByNumber(strConfigBuffer, min);
			strConfigBuffer.append(min);
		}

		Log.d(TAG, "syncTimeServer");
		bundle.putString(configManager.SAVE_CLOCK, strConfigBuffer.toString());
		HCRequest hc = new HCRequest();
		hc.setRequestType(HCRequest.REQUEST_SET_TIME_SERVER);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_TIME_SERVER;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	private void getTimeServer() {
		Log.d(TAG, "getTimeServer");
		Message message = Message.obtain();
		message.what = HCRequest.REQUEST_GET_TIME_SERVER;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void clockGot(boolean result) {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (ParameterConfigDevice.this.isVisible()) {
					HomeCenterUIEngine uiEngine = RegisterService
							.getHomeCenterUIEngine();
					if (uiEngine == null) {
						return;
					}
					House house = uiEngine.getHouse();
					refershTimer(house);
				}
			}
		});
	}

}
