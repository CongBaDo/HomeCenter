package com.HomeCenter2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.house.DeviceType;
import com.HomeCenter2.house.DeviceTypeOnOff;
import com.HomeCenter2.house.Schedule;
import com.HomeCenter2.ui.DialogFragmentWrapper;

public class configManager {

	public static String FOLDERNAME;// =
									// Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + "/Devices";
	public static String DEVICE_FILENAME = "devices.xml";
	public static String CONFIG_FILENAME = "configs.xml";

	public static String RA_CLIENT_ENABLED = "clientEnabled";
	
	public static final String IMAGE_LEFT = "left.jpeg";
	public static final String IMAGE_RIGHT = "right.jpeg";

	/**
	 * Config
	 */
	public static String CONFIG = "config";
	public static String SERVER_REMOTE = "server_remote";
	public static String PORT_REMOTE = "port_remote";

	public static String SERVER_LOCAL = "server_local";
	public static String PORT_LOCAL = "port_local";
	public static String IP_TYPE = "ip_type";

	/**
	 * Device
	 */
	public static String DEVICES = "Devices";

	public static String HOUSE = "house";
	public static String AREA = "area";
	public static String ROOM = "room";
	public static String DEVICE = "device";

	public static String TYPE = "type";
	public static String TYPE_ON_OFF = "typeOnOff";
	public static String ID = "id";
	public static String NAME = "mNameTV";
	public static String STATUS = "status";
	public static String ACTIVE = "active";
	public static String TEMPERA = "tempera";
	public static String LIGH = "light";
	public static String ROLLER = "rooler";
	public static String LINK_APP = "linkApp";
	public static String ID_SERVER = "idServer";
	public static String IS_OTHER_TYPE = "isOtherType";

	public static final String SHOW_SCREEN = "force_show_screen";
	public static final String SHOW_ROOM = "show_room";
	public static final String SHOW_DETAIL_DEVICE = "show_detail_device";
	public static final String SCREEN_ID = "id_screen";

	// device Id
	/*
	 * public static final String LIGHT_1_ID = "0L1"; public static final String
	 * LIGHT_2_ID = "0L2"; public static final String DOOR_STATUS_1_ID = "0DS1";
	 * public static final String DOOR_STATUS_2_ID = "0DS2"; public static final
	 * String DOOR_STATUS_3_ID = "0DS3"; public static final String
	 * DOOR_STATUS_4_ID = "0DS4"; public static final String MOTION_1_ID =
	 * "0M1"; public static final String MOTION_2_ID = "0M2"; public static
	 * final String MOTION_3_ID = "0M3"; public static final String MOTION_4_ID
	 * = "0M4"; public static final String TEMPERATURE_1_ID = "0T1"; public
	 * static final String TEMPERATURE_2_ID = "0T2"; public static final String
	 * LAMP_1_ID = "01"; public static final String LAMP_2_ID = "02"; public
	 * static final String LAMP_3_ID = "03"; public static final String
	 * LAMP_4_ID = "04"; public static final String LAMP_5_ID = "05"; public
	 * static final String LAMP_6_ID = "06"; public static final String
	 * LAMP_7_ID = "07"; public static final String LAMP_8_ID = "08"; public
	 * static final String LAMP_9_ID = "09"; public static final String
	 * LAMP_10_ID = "10"; public static final String DOOR_LOCK_ID = "un"; public
	 * static final String ROLLER_DOWN_ID = "do";
	 */

	public static final int NORMAL = 0;
	public static final int LIGHT = NORMAL + 2;
	public static final int TEMPERATURE = NORMAL + 3;
	public static final int MOTION = NORMAL + 4;
	public static final int DOOR_STATUS = NORMAL + 5;
	public static final int ROLLER_SHUTTER = NORMAL + 7;
	public static final int DOOR_LOCK = NORMAL + 8;
	public static final int SMOKE = NORMAL + 11;
	public static final int CAMERA = NORMAL + 12;
	public static final int ON_OFF = NORMAL + 14;

	// device type id
	public static final int TYPE_LAMP = 0;
	public static final int TYPE_FAN = TYPE_LAMP + 1;
	public static final int TYPE_FRIDGE = TYPE_LAMP + 2;
	public static final int TYPE_PLUG_DEVICE = TYPE_LAMP + 3;
	public static final int TYPE_COCK = TYPE_LAMP + 4;
	

	public static final int TYPE_REMOTE_TV = TYPE_LAMP + 5;
	public static final int TYPE_REMOTE_CAMERA = TYPE_LAMP + 6;
	public static final int TYPE_REMOTE_AIR = TYPE_LAMP + 7;
	public static final int TYPE_CAMERA = TYPE_LAMP + 8;
	public static final int TYPE_DOORCLOCK = TYPE_LAMP + 9;
	public static final int TYPE_ROLLER_SHUTTER = TYPE_LAMP + 10;

	
	// Postion of status relationship 20,1,31,12,0000000,1 //phut,h,d,m,rep,on
	public static final int SR_MINUTE = 0;
	public static final int SR_HOUR = SR_MINUTE + 1;
	public static final int SR_DAY = SR_MINUTE + 2;
	public static final int SR_MONTH = SR_MINUTE + 3;
	public static final int SR_REPEAT_POSITION = SR_MINUTE + 4;
	public static final int SR_IS_ON = SR_MINUTE + 5;

	public static final int SR_MON_POSITION = 0;
	public static final int SR_TUE_POSITION = SR_MON_POSITION + 1;
	public static final int SR_WED_POSITION = SR_MON_POSITION + 2;
	public static final int SR_THU_POSITION = SR_MON_POSITION + 3;
	public static final int SR_FRI_POSITION = SR_MON_POSITION + 4;
	public static final int SR_SAT_POSITION = SR_MON_POSITION + 5;
	public static final int SR_SUN_POSITION = SR_MON_POSITION + 6;
	public static final int SR_SE_POSITION = SR_MON_POSITION + 7;

	// // Postion of status relationship device
	public static final int SR_TEMP1_POSITION = 0;
	public static final int SR_LIGHT1_POSITION = SR_TEMP1_POSITION + 1;
	public static final int SR_SMOKE1_POSITION = SR_TEMP1_POSITION + 2;
	public static final int SR_MOTION1_POSITION = SR_TEMP1_POSITION + 3;
	public static final int SR_MOTION2_POSITION = SR_TEMP1_POSITION + 4;
	public static final int SR_DOOR1_POSITION = SR_TEMP1_POSITION + 5;
	public static final int SR_DOOR2_POSITION = SR_TEMP1_POSITION + 6;

	public static final int SR_TEMP2_POSITION = SR_TEMP1_POSITION + 7;
	public static final int SR_LIGHT2_POSITION = SR_TEMP1_POSITION + 8;
	public static final int SR_SMOKE2_POSITION = SR_TEMP1_POSITION + 9;
	public static final int SR_MOTION3_POSITION = SR_TEMP1_POSITION + 10;
	public static final int SR_MOTION4_POSITION = SR_TEMP1_POSITION + 11;
	public static final int SR_DOOR3_POSITION = SR_TEMP1_POSITION + 12;
	public static final int SR_DOOR4_POSITION = SR_TEMP1_POSITION + 13;

	public static final int DEVICE_TYPE = 0;
	public static final int ROOM_TYPE = 1;
	public static final int AREA_TYPE = 2;

	public static List<DeviceTypeOnOff> OnOffTypes = null;

	public static final int NUM_DEVICE_IN_PAGE = 4;

	public static final int MAX_CONTROL_IN_ROOM = 16;
	public static final int MAX_SENSOR_IN_ROOM = 14;
	public static final int MAX_AREA = 1;
	public static final int MAX_ROOM_IN_AREA = 8;
	public static final int MAX_VISIBLE_ITEM_WHEEL_WIDGET = 3;
	public static final int MAX_ROOM_GSM_IN_AREA = 10;

	public static final String ROOM_BUNDLE = "room";
	public static final String DEVICE_BUNDLE = "device";
	public static final String IS_DEVICE_BUNDLE = "isRemote";
	public static final String BUTTON_BUNDLE = "isButton";
	public static final String HAS_GET_SCHEDULE = "hasGetDevice";

	public static final int DIALOG_PROCESS = 0;
	public static final int DIALOG_WARNING_WHEN_DISIBLE_ROOM = DIALOG_PROCESS + 1;
	public static final int DIALOG_ADDED_ROOM = DIALOG_PROCESS + 2;
	public static final int DIALOG_ADD_CONTENT_ROOM = DIALOG_PROCESS + 3;
	public static final int DIALOG_WARNING_WHEN_ENABLE_ROOM = DIALOG_PROCESS + 4;

	public static final int DIALOG_CHOOSE_DEVICE_TYPE = DIALOG_PROCESS + 5;
	public static final int DIALOG_WARNING_WHEN_DISIBLE_DEVICE = DIALOG_PROCESS + 6;
	public static final int DIALOG_ADD_CONTENT_DEVICE = DIALOG_PROCESS + 7;

	public static final int DIALOG_FAIL_LOGIN = DIALOG_PROCESS + 8;

	public static final int DIALOG_SAVE_CONFIG_SUCCESS = DIALOG_PROCESS + 9;
	public static final int DIALOG_SAVE_CONFIG_FAIL = DIALOG_PROCESS + 10;
	public static final int DIALOG_SET_ON_OFF_DEVICE_FAIL = DIALOG_PROCESS + 11;

	public static final int DIALOG_FAIL_CONNECT_SOCKET = DIALOG_PROCESS + 12;

	public static final int DIALOG_CHANGE_ROOM_NAME = DIALOG_PROCESS + 13;

	public static final int DIALOG_CHANGE_NAME = DIALOG_PROCESS + 14;

	public static final int DIALOG_ROOM_MENU = DIALOG_PROCESS + 15;

	public static final int DIALOG_SERVER_LOGIN = DIALOG_PROCESS + 16;

	public static final int PROVISION_REQUEST = 0;
	public static final int RESULT_SPEECH = 1;
	public static final int RESULT_ROOM_INDEX = 113;

	public static final int NO_INDEX_ROOM = -1;

	// room state
	public static final int ROOM_OFF_ALL = 0;
	public static final int ROOM_ON_ALL = 1;
	public static final int ROOM_ON_OFF = 2;

	public static String[] hours = null;
	public static String[] mins = null;
	public static String[] days = null;
	public static String[] months = null;

	/**
	 * Socket
	 */
	public static final int SERVER_PORT = 2000;
	// public static final String SERVER_IP = "27.2.150.121";
	public static final String SERVER_IP = "192.168.1.1";
	public static final int MAX_BUFFER_LENGTH = 180;

	/**
	 * Action
	 */
	public static final int LOGIN_ACTION = 0;
	public static final int GET_ROOM_ADDRESS_ACTION = LOGIN_ACTION + 1;
	public static final int GET_ADMIN_ACTION = LOGIN_ACTION + 2;
	public static final int SET_ROOM_ADDRESS = LOGIN_ACTION + 3; // save device
																	// address
	public static final int SET_ADMIN_ADDRESS = LOGIN_ACTION + 4; // save
																	// server,
																	// phone,
																	// mNameTV,...
	public static final int GET_ROOM_STATUS = LOGIN_ACTION + 5;
	public static final int SET_DEVICE_STATUS = LOGIN_ACTION + 6;
	public static final int LEARN_INFRARED = LOGIN_ACTION + 7;
	public static final int TRANSMIT_INFRARED = LOGIN_ACTION + 8;
	public static final int GET_DEVICE_STATUS_RELATIONSHIP = LOGIN_ACTION + 9;
	public static final int SET_DEVICE_STATUS_RELATIONSHIP = LOGIN_ACTION + 10;

	public static final int GET_AUDIO = LOGIN_ACTION + 11;
	public static final int SET_AUDIO = LOGIN_ACTION + 12;

	public static final int SET_LOCK_DOOR = LOGIN_ACTION + 13;
	public static final int SET_LOCK_PASSWORD = LOGIN_ACTION + 14;
	public static final int GET_LOCK_DOOR = LOGIN_ACTION + 15;

	public static final int SET_TIME_OFF = LOGIN_ACTION + 16;
	public static final int GET_TIME_OFF = LOGIN_ACTION + 17;

	public static final int SET_CLOCK = LOGIN_ACTION + 18;
	public static final int GET_CLOCK = LOGIN_ACTION + 19;

	public static final int SET_SHEDULE_DEVICE_RELATIONSHIP = LOGIN_ACTION + 20;
	public static final int GET_SHEDULE_DEVICE_RELATIONSHIP = LOGIN_ACTION + 21;

	public static final int GET_LOCK_ID_ACTION = LOGIN_ACTION + 22;
	public static final int SET_LOCK_ID_ACTION = LOGIN_ACTION + 23;

	// address

	// address

	// the device order in a room
	public static final int TEMPERATURE_1 = 0;
	public static final int TEMPERATURE_2 = TEMPERATURE_1 + 1;

	public static final int LIGHT_1 = TEMPERATURE_1 + 2;

	public static final int LIGHT_2 = TEMPERATURE_1 + 3;
	public static final int DOOR_STATUS_1 = TEMPERATURE_1 + 4;
	public static final int DOOR_STATUS_2 = TEMPERATURE_1 + 5;
	public static final int DOOR_STATUS_3 = TEMPERATURE_1 + 6;
	public static final int DOOR_STATUS_4 = TEMPERATURE_1 + 7;
	public static final int MOTION_1 = TEMPERATURE_1 + 8;
	public static final int MOTION_2 = TEMPERATURE_1 + 9;
	public static final int MOTION_3 = TEMPERATURE_1 + 10;
	public static final int MOTION_4 = TEMPERATURE_1 + 11;

	public static final int LAMP_1 = TEMPERATURE_1 + 12;
	public static final int LAMP_2 = TEMPERATURE_1 + 13;
	public static final int LAMP_3 = TEMPERATURE_1 + 14;
	public static final int LAMP_4 = TEMPERATURE_1 + 15;
	public static final int LAMP_5 = TEMPERATURE_1 + 16;
	public static final int LAMP_6 = TEMPERATURE_1 + 17;
	public static final int LAMP_7 = TEMPERATURE_1 + 18;
	public static final int LAMP_8 = TEMPERATURE_1 + 19;
	public static final int LAMP_9 = TEMPERATURE_1 + 20;
	public static final int FAN_1 = TEMPERATURE_1 + 21;
	public static final int DOOR_LOCK_1 = TEMPERATURE_1 + 22;
	public static final int ROLLER_SHUTTER_1 = TEMPERATURE_1 + 23;
	public static final int SMOKE_1 = TEMPERATURE_1 + 24;
	public static final int SMOKE_2 = TEMPERATURE_1 + 25;

	public static final int CAMERA_1 = TEMPERATURE_1 + 26;
	public static final int CAMERA_2 = TEMPERATURE_1 + 27;
	public static final int CAMERA_3 = TEMPERATURE_1 + 28;
	public static final int CAMERA_4 = TEMPERATURE_1 + 29;

	// the device order in the "get status" message receive.

	public static final int LIGHT_1_POSITION = 0; //
	public static final int LIGHT_2_POSITION = LIGHT_1_POSITION + 1;
	public static final int DOOR_STATUS_1_POSITION = LIGHT_1_POSITION + 2;
	public static final int DOOR_STATUS_2_POSITION = LIGHT_1_POSITION + 3;
	public static final int DOOR_STATUS_3_POSITION = LIGHT_1_POSITION + 4;
	public static final int DOOR_STATUS_4_POSITION = LIGHT_1_POSITION + 5;

	public static final int MOTION_1_POSITION = LIGHT_1_POSITION + 6;
	public static final int MOTION_2_POSITION = LIGHT_1_POSITION + 7;
	public static final int MOTION_3_POSITION = LIGHT_1_POSITION + 8;
	public static final int MOTION_4_POSITION = LIGHT_1_POSITION + 9;

	public static final int SMOKE_1_POSITION = LIGHT_1_POSITION + 10;
	public static final int SMOKE_2_POSITION = LIGHT_1_POSITION + 11;

	public static final int TEMPERATURE_1_POSITION = LIGHT_1_POSITION + 12;
	public static final int TEMPERATURE_2_POSITION = LIGHT_1_POSITION + 15;

	public static final int LAMP_1_POSITION = LIGHT_1_POSITION + 18;
	public static final int LAMP_2_POSITION = LIGHT_1_POSITION + 19;
	public static final int LAMP_3_POSITION = LIGHT_1_POSITION + 20;
	public static final int LAMP_4_POSITION = LIGHT_1_POSITION + 21;
	public static final int LAMP_5_POSITION = LIGHT_1_POSITION + 22;
	public static final int LAMP_6_POSITION = LIGHT_1_POSITION + 23;
	public static final int LAMP_7_POSITION = LIGHT_1_POSITION + 24;
	public static final int LAMP_8_POSITION = LIGHT_1_POSITION + 25;
	public static final int LAMP_9_POSITION = LIGHT_1_POSITION + 26;
	public static final int FAN_1_POSITION = LIGHT_1_POSITION + 27;

	public static final int ROLLER_STATUS_1_POSITION = LIGHT_1_POSITION + 28;
	public static final int DOOR_LOCK_1_POSITION = LIGHT_1_POSITION + 29;
	public static final int ENERGY_POSITION = LIGHT_1_POSITION + 30;
	//

	public static final int DISTANCE_MOTION4_TO_TEMP2 = TEMPERATURE_1
			- MOTION_4;
	public static final int DISTANCE_TEMP1_TO_TEMP2 = TEMPERATURE_2
			- TEMPERATURE_1;
	public static final int DISTANCE_TEMP2_TO_LAMP1 = LAMP_1 - TEMPERATURE_2;

	/*
	 * public static final int DOOR_LOCK_ON_IMAGE =; public static final int
	 * DOOR_LOCK_OFF_IMAGE =;
	 * 
	 * public static final int DOOR_STATUS_ON_IMAGE =; public static final int
	 * DOOR_STATUS_OFF_IMAGE =;
	 * 
	 * public static final int FAN_ON_IMAGE =; public static final int
	 * FAN_OFF_IMAGE =;
	 * 
	 * public static final int FRIDGE_ON_IMAGE =; public static final int
	 * FRIDGE_OFF_IMAGE =;
	 * 
	 * public static final int LAMP_ON_IMAGE =; public static final int
	 * LAMP_OFF_IMAGE =;
	 * 
	 * public static final int PLUG_DEVICE_ON_IMAGE =; public static final int
	 * PLUG_DEVICE_OFF_IMAGE =;
	 * 
	 * public static final int LIGHT_ON_IMAGE =; public static final int
	 * LIGHT_OFF_IMAGE =;
	 * 
	 * public static final int MOTION_ON_IMAGE =; public static final int
	 * MOTION_OFF_IMAGE =;
	 * 
	 * public static final int ROLLER_SHUTTER_ON_IMAGE =; public static final
	 * int ROLLER_SHUTTER_OFF_IMAGE =;
	 * 
	 * public static final int SMOKE_ON_IMAGE =; public static final int
	 * SMOKE_OFF_IMAGE =;
	 */

	public static final int REMOTE_CONTROL = 1;
	public static final int REMOTE_UPDATE = 2;	

	// Bundle

	// update remote : 7
	public static final String ROOM_ID = "RoomID";
	public static final String SENSOR_ID = "SensorID";
	public static final String DEVICE_ID = "DeviceID";
	public static final String ON_OFF_ACTION = "OnOffAction";

	// bundle schedule
	public static final String SHEDULE = "Shedule";

	public static final int TYPE_SHEDULE_01 = 0;
	public static final int TYPE_SHEDULE_02 = 1;
	public static final int TYPE_SHEDULE_03 = 2;
	public static final int TYPE_SHEDULE_04 = 3;
	public static final int TYPE_TURN_WHEN_DEVICE = 4;

	public static final String TURN_SMART_ENERGY = "SmartEnergy";

	// Config
	public static final String SAVE_CONFIG = "SaveConfig";
	public static final String SAVE_DEVICE = "SaveDevice";
	public static final String SAVE_CLOCK = "SaveCLock";

	public static final String PASSWORD_BUNDLE = "Password";

	public static final String NAME_BUNDLE = "NameDevice";

	// Schedule
	public static final int MAX_SCHEDULE = 4;

	// Room all
	public static final int ROOM_ALL_ID = 0;
	// Key
	public static final int MAX_KEY = 10;

	public static final String COMMAS = ",";
	public static final String SHIFT3 = "#";
	public static final String PER = "%";

	public static final int KEY_0 = 0;
	public static final int KEY_1 = KEY_0 + 1;
	public static final int KEY_2 = KEY_0 + 2;
	public static final int KEY_3 = KEY_0 + 3;
	public static final int KEY_4 = KEY_0 + 4;
	public static final int KEY_5 = KEY_0 + 5;
	public static final int KEY_6 = KEY_0 + 6;
	public static final int KEY_7 = KEY_0 + 7;
	public static final int KEY_8 = KEY_0 + 8;
	public static final int KEY_9 = KEY_0 + 9;

	// Bundle doorlock
	public static final String MODE_STATUS = "mode";
	public static final String PASSWORD_KEY = "password_key";

	// Mode for doorlcok
	public static final String MODE_CLOSE = "1";
	public static final String MODE_OPEN = "2";
	public static final String MODE_SET_ID = "3";
	public static final String MODE_DELETE_ID = "4";
	public static final String MODE_LEARN_IR = "5";

	public static final int NUMBER_SENSOR_DEFAULT = 11;
	public static final String ZERO = "0";
	
	public static final String ARGUMENT_IS_SHOWED = "is_show";

	/**
	 * list devices have in house
	 */
	private static void createDeviceTypes() {
		OnOffTypes = new ArrayList<DeviceTypeOnOff>();
		Context context = HomeCenter2Activity.getContext();
		if (context == null) {
			return;
		}

		RegisterService service = RegisterService.getService();
		if (service == null)
			return;

		DeviceTypeOnOff item = new DeviceTypeOnOff(TYPE_LAMP, service
				.getResources().getString(R.string.lamp),
				R.drawable.ic_lamp_wht, R.drawable.ic_lamp_blk);
		OnOffTypes.add(item);

		item = new DeviceTypeOnOff(TYPE_FAN, service.getResources().getString(
				R.string.fan), R.drawable.ic_fan_wht_43,
				R.drawable.ic_fan_blk_43);
		OnOffTypes.add(item);

		item = new DeviceTypeOnOff(TYPE_FRIDGE, service.getResources()
				.getString(R.string.fridge), R.drawable.ic_fridge_wht_64,
				R.drawable.ic_fridge_blk_64);
		OnOffTypes.add(item);

		item = new DeviceTypeOnOff(TYPE_PLUG_DEVICE, service.getResources()
				.getString(R.string.plug_device), R.drawable.ic_menu_prise_wht,
				R.drawable.ic_menu_prise_blk);
		OnOffTypes.add(item);

		item = new DeviceTypeOnOff(TYPE_COCK, service.getResources().getString(
				R.string.cock), R.drawable.ic_cock_wht_30,
				R.drawable.ic_cock_blk);
		OnOffTypes.add(item);

		item = new DeviceTypeOnOff(TYPE_REMOTE_TV, service.getResources()
				.getString(R.string.remote_tv), R.drawable.ic_remote,
				R.drawable.ic_lamp_blk);
		OnOffTypes.add(item);
		
		item = new DeviceTypeOnOff(TYPE_REMOTE_CAMERA, service.getResources()
				.getString(R.string.remote_camera), R.drawable.ic_camera_wht,
				R.drawable.ic_camera_wht);
		OnOffTypes.add(item);
		
		item = new DeviceTypeOnOff(TYPE_REMOTE_AIR, service.getResources()
				.getString(R.string.remote_aircondition), R.drawable.ic_24,
				R.drawable.ic_24);
		OnOffTypes.add(item);
		
		item = new DeviceTypeOnOff(TYPE_CAMERA, service.getResources()
				.getString(R.string.camera), R.drawable.ic_surveillance,
				R.drawable.ic_surveillance);
		OnOffTypes.add(item);
		
		item = new DeviceTypeOnOff(TYPE_DOORCLOCK, service.getResources()
				.getString(R.string.doorlock), R.drawable.lock_locked_grayscale,
				R.drawable.lock_locked_grayscale);
		OnOffTypes.add(item);
		
		item = new DeviceTypeOnOff(TYPE_ROLLER_SHUTTER, service.getResources()
				.getString(R.string.rollerShutter), R.drawable.ic_objet_volet,
				R.drawable.ic_objet_volet);
		OnOffTypes.add(item);
		
	}

	public static void initConfigManager() {
		int i = 0;
		hours = new String[24];
		for (i = 0; i < 24; i++) {
			hours[i] = String.valueOf(i + 1);
		}

		mins = new String[60];
		for (i = 0; i < 60; i++) {
			mins[i] = String.valueOf(i + 1);
		}

		days = new String[31];
		for (i = 0; i < 31; i++) {
			days[i] = String.valueOf(i + 1);
		}

		months = new String[12];
		for (i = 0; i < 12; i++) {
			months[i] = String.valueOf(i + 1);
		}

		createDeviceTypes();
	}

	public static void setDate(TextView view, int day, int month, int year) {
		if (view == null) {
			return;
		}
		MyDate date = new MyDate(day, month, year);
		String str = date.parseToString();
		view.setTag(date);
		view.setText(str);
	}

	public static void setTime(TextView view, int minute, int hour) {
		if (view == null) {
			return;
		}
		MyTime time = new MyTime(minute, hour);
		String str = time.parseToString();
		view.setTag(time);
		view.setText(str);
	}

	public static void setDate(EditText view, int day, int month, int year) {
		if (view == null) {
			return;
		}
		MyDate date = new MyDate(day, month, year);
		String str = date.parseToString();
		view.setTag(date);
		view.setText(str);
	}

	public static void setTime(EditText view, int minute, int hour) {
		if (view == null) {
			return;
		}
		MyTime time = new MyTime(minute, hour);
		String str = time.parseToString();
		view.setTag(time);
		view.setText(str);
	}

	public static String formatTime(Time time) {
		String str = time.monthDay + "/" + time.month + "/" + time.year + " - "
				+ time.hour + ":" + time.minute + ":" + time.second;
		return str;
	}

	public static String formatRepeat(Context context, Schedule schedule) {
		String str = "";
		boolean isStart = true;
		StringBuffer buffer = new StringBuffer();
		if (schedule.isMonRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.mon));
		}

		if (schedule.isTueRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.tue));
		}

		if (schedule.isWebRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.wed));
		}

		if (schedule.isThuRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.thu));
		}

		if (schedule.isFriRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.fri));
		}

		if (schedule.isSatRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.sat));
		}

		if (schedule.isSunRepeat()) {
			if (isStart == false) {
				buffer.append(", ");
			}
			buffer.append(context.getString(R.string.sun));
		}

		str = buffer.toString();

		return str;

	}

	public static void getListViewSize(ListView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);
		// print height of adapter on log
		Log.i("height of listItem:", String.valueOf(totalHeight));
	}

	public static String convertDeviceIdToString(int id) {
		String pos = "";
		if (id == 0) {
			pos = "00";
		} else {
			int temp = id - configManager.NUMBER_SENSOR_DEFAULT;

			if (temp > 0 && temp < 10) {
				pos = "0" + String.valueOf(temp);
			} else {
				pos = String.valueOf(temp);
			}
		}
		return pos;
	}

	public static String convertIdToString(int id) {
		String pos = "";

		if (id > 0 && id < 10) {
			pos = "0" + String.valueOf(id);
		} else {
			pos = String.valueOf(id);
		}
		return pos;
	}

	public static final int CONFIG_DEVICE = 0;
	public static final int CONFIG_ROOM = CONFIG_DEVICE + 1;

	public static final String SAVE_ROOMIDS = "SaveRoomIds";
	public static final String SAVE_LOCKIDS = "SaveLockIds";

	public static final int SOCKET_TIME_OUT = 1000;
	public static final int MAX_TIME_REQUEST_AGAIN = 2000;
	public static final int MAX_TIME_SEND_AGAIN = 2;

	public static final int SIZE_TEMPERATURE = 3;
	public static final int DOWN_TEMPERATURE = 3;
	public static final int DEFAULT_NONE_TEMPERATURE = 252;

	// Audio
	public static final int POSITION_AUDIO = 0;
	public static final int POSITION_GSM = 1;

	// Schedule
	public static final String NOT_SET_DAY_SCHEDULE = "*";

	public static final String ACTIVE_KEY = "activeKey";
	public static final String POSITON_KEY = "positionKey";

}
