package com.HomeCenter2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.data.ClientSocket;
import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.MyTime;
import com.HomeCenter2.data.Password;
import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Area;
import com.HomeCenter2.house.AudioHC;
import com.HomeCenter2.house.Camera;
import com.HomeCenter2.house.Cock;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.DoorStatus;
import com.HomeCenter2.house.Fan;
import com.HomeCenter2.house.Fridge;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.KeyDoorLock;
import com.HomeCenter2.house.Lamp;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.Light;
import com.HomeCenter2.house.Motion;
import com.HomeCenter2.house.PlugDevice;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Smoke;
import com.HomeCenter2.house.StatusRelationship;
import com.HomeCenter2.house.Temperature;
import com.HomeCenter2.ui.listener.AudioListener;
import com.HomeCenter2.ui.listener.ClockListener;
import com.HomeCenter2.ui.listener.ConfigListener;
import com.HomeCenter2.ui.listener.ConnectSocketListener;
import com.HomeCenter2.ui.listener.GetStatusKeyLockListener;
import com.HomeCenter2.ui.listener.GetStatusRelationshipListener;
import com.HomeCenter2.ui.listener.LoginListener;
import com.HomeCenter2.ui.listener.StatusListener;
import com.HomeCenter2.ui.listener.XMLListener;
import com.HomeCenter2.ui.slidingmenu.framework.OnPageScrolledCompleteListener;

public class HomeCenterUIEngine extends Handler {

	public static final String TAG = "HomeCenterUIEngine";
	private ClientSocket mSocket = null;
	String mRoomID = "1";
	String mDevID = "0";
	Timer msgTimerDevice;
	MyTask mTaskTimerDevice;
	private House mHouse = null;

	boolean test = true;
	private boolean mAppInForeground = false;
	boolean mIsLogined = false;

	private int mRemoteType = configManager.REMOTE_CONTROL;

	private int mRoomCurrentIndex;

	private Device[] mDeviceType = null;

	public boolean isLogined() {
		return mIsLogined;
	}

	public void setIsLogined(boolean isLogined) {
		this.mIsLogined = isLogined;
	}

	public HomeCenterUIEngine(Looper looper) {
		super(looper);
		setSocket(new ClientSocket());
		mHouse = new House();
		mRoomCurrentIndex = 0;
//		startSocket();//nganguyen
		initDeviceTypes();
	}

	private Vector<ConnectSocketListener> mSocketListener = new Vector<ConnectSocketListener>();
	private Vector<LoginListener> mLoginListener = new Vector<LoginListener>();

	private Vector<StatusListener> mStatusListener = new Vector<StatusListener>();

	private Vector<XMLListener> mXMLListener = new Vector<XMLListener>();

	private Vector<GetStatusRelationshipListener> mGettingSRListeners = new Vector<GetStatusRelationshipListener>();

	private Vector<OnPageScrolledCompleteListener> mPageScrolledCompleteListener = new Vector<OnPageScrolledCompleteListener>();

	private Vector<GetStatusKeyLockListener> mStatuKeyLockListeners = new Vector<GetStatusKeyLockListener>();

	private Vector<ConfigListener> mConfigListeners = new Vector<ConfigListener>();

	private Vector<ClockListener> mClockListeners = new Vector<ClockListener>();

	private Vector<AudioListener> mAudioListeners = new Vector<AudioListener>();

	private Bundle forceOpenScreenBundle = null;

	public ClientSocket getSocket() {
		return mSocket;
	}

	public void setSocket(ClientSocket mSocket) {
		this.mSocket = mSocket;
	}

	private Activity mCurrentActivity = null;

	public String sendAndReceiveMessage(int id, String roomID, String devID,
			String message) {
		String receive = "";
		try {
			receive = sendAndReceiveMessage(id, roomID, "", devID, message);
		} catch (InterruptedIOException e) {
			Log.i(TAG, "sendAndReceiveMessage: " + e.toString());
		}
		return receive;
	}

	public String sendAndReceiveMessage(int id, String roomID, String sensorId,
			String devID, String message) throws InterruptedIOException {

		char[] receivebuf = new char[configManager.MAX_BUFFER_LENGTH];
		String receiveMessage = "";
		if (mSocket.getInBuffer() == null
				&& id != configManager.SET_DEVICE_STATUS_RELATIONSHIP) {
			Log.d(TAG, "The socket is not init yet");
			return "";
		}
		sendcommand(id, roomID, sensorId, devID, message);
		boolean isFailed = false;
		// nganguyen comment to by pass login
//		try {
//			mSocket.getSocket().setSoTimeout(configManager.SOCKET_TIME_OUT);
//			mSocket.setInBuffer(new BufferedReader(new InputStreamReader(
//					mSocket.getSocket().getInputStream())));
//			mSocket.getInBuffer().read(receivebuf, 0,
//					configManager.MAX_BUFFER_LENGTH);
//		} catch (IOException e) {
//			Log.i(TAG, "sendAndReceiveMessage: reading buffer was failed - "
//					+ e.toString());
//			e.printStackTrace();
//			isFailed = false;
//		} catch (NetworkOnMainThreadException e) {
//			e.printStackTrace();
//			isFailed = false;
//		}
//		finally {
			if (isFailed == true) {
				receiveMessage = null;
			} else {
				receiveMessage = new String(receivebuf);
			}
//		}
		return receiveMessage;
	}

	public boolean login(Bundle bundle) {
		Password password = (Password) bundle
				.getSerializable(configManager.PASSWORD_BUNDLE);
		// nganguyen comment to by pass login
//		boolean result = psswd(password);
		boolean result = true;
		Log.d(TAG, "login - result : " + result);
		if (result) {
			getInformation();
			// startTimer();
		}
		return result;
	}

	// 9
	// 21 1111 1111 11 2522 5200 0100 0100 1701
	public void setStatusForDevice(int i, String status, Device device) {
		switch (i) {
		case configManager.LIGHT_1: // 0
			if (status.length() >= configManager.LIGHT_1_POSITION
					&& device instanceof Light) {
				Light light1 = (Light) device;
				char temp = status.charAt(configManager.LIGHT_1_POSITION);
				String tempStr = String.valueOf(temp);
				try {
					int light = Integer.parseInt(tempStr);
					light1.setLight(light);					
				} catch (Exception e) {
					// TODO: handle exception
					Log.d(TAG,
							"setStatusForDevice - device: LIGHT_1 , status: null");
				}
			}
			break;
		case configManager.LIGHT_2: //
			if (status.length() >= configManager.LIGHT_2_POSITION
					&& device instanceof Light) {
				Light light2 = (Light) device;
				char temp = status.charAt(configManager.LIGHT_2_POSITION);
				String tempStr = String.valueOf(temp);
				try {
					int light = Integer.parseInt(tempStr);
					light2.setLight(light);					
				} catch (Exception e) {
					Log.d(TAG,
							"setStatusForDevice - device: LIGHT_2 , status: null");
				}
			}
			break;
		case configManager.DOOR_STATUS_1: // 2
			if (status.length() >= configManager.DOOR_STATUS_1_POSITION
					&& device instanceof DoorStatus) {
				DoorStatus ds1 = (DoorStatus) device;
				boolean state1 = (status
						.charAt(configManager.DOOR_STATUS_1_POSITION) != '0');
				ds1.setState(state1);		
			}
			break;
		case configManager.DOOR_STATUS_2:
			if (status.length() >= configManager.DOOR_STATUS_2_POSITION
					&& device instanceof DoorStatus) {
				DoorStatus ds2 = (DoorStatus) device;
				boolean state2 = (status
						.charAt(configManager.DOOR_STATUS_2_POSITION) != '0');
				ds2.setState(state2);				
			}
			break;
		case configManager.DOOR_STATUS_3:
			if (status.length() >= configManager.DOOR_STATUS_3_POSITION
					&& device instanceof DoorStatus) {
				DoorStatus ds3 = (DoorStatus) device;
				boolean state3 = (status
						.charAt(configManager.DOOR_STATUS_3_POSITION) != '0');
				ds3.setState(state3);				
			}
			break;
		case configManager.DOOR_STATUS_4:
			if (status.length() >= configManager.DOOR_STATUS_4_POSITION
					&& device instanceof DoorStatus) {
				DoorStatus ds4 = (DoorStatus) device;
				boolean state4 = (status
						.charAt(configManager.DOOR_STATUS_4_POSITION) != '0');
				ds4.setState(state4);				
			}
			break;
		case configManager.MOTION_1:
			if (status.length() >= configManager.MOTION_1_POSITION
					&& device instanceof Motion) {
				Motion ms1 = (Motion) device;
				boolean state5 = (status
						.charAt(configManager.MOTION_1_POSITION) != '0');
				ms1.setState(state5);				
			}
			break;
		case configManager.MOTION_2:
			if (status.length() >= configManager.MOTION_2_POSITION
					&& device instanceof Motion) {
				Motion ms2 = (Motion) device;
				boolean state6 = (status
						.charAt(configManager.MOTION_2_POSITION) != '0');
				ms2.setState(state6);				
			}
			break;
		case configManager.MOTION_3:
			if (status.length() >= configManager.MOTION_3_POSITION
					&& device instanceof Motion) {
				Motion ms3 = (Motion) device;
				boolean state7 = (status
						.charAt(configManager.MOTION_3_POSITION) != '0');
				ms3.setState(state7);				
			}
			break;
		case configManager.MOTION_4:
			if (status.length() >= configManager.MOTION_4_POSITION
					&& device instanceof Motion) {
				Motion ms4 = (Motion) device;
				boolean state8 = (status
						.charAt(configManager.MOTION_4_POSITION) != '0');
				ms4.setState(state8);				
			}
			break;
		case configManager.TEMPERATURE_1:
			if (status.length() >= configManager.TEMPERATURE_1_POSITION
					&& device instanceof Temperature) {
				Temperature temp11 = (Temperature) device;
				String temp = status
						.substring(
								configManager.TEMPERATURE_1_POSITION,
								(configManager.TEMPERATURE_1_POSITION + configManager.SIZE_TEMPERATURE));
				if (TextUtils.isEmpty(temp) == false) {
					try {
						int temperature = Integer.parseInt(temp);
						if (temperature != configManager.DEFAULT_NONE_TEMPERATURE) {
							temp11.setTemperature(temperature
									- configManager.DOWN_TEMPERATURE);
						} else {
							temp11.setTemperature(configManager.DEFAULT_NONE_TEMPERATURE);
						}

					} catch (Exception e) {
					}
				}
			}
			break;
		case configManager.TEMPERATURE_2:
			if (status.length() >= configManager.TEMPERATURE_2_POSITION
					&& device instanceof Temperature) {
				Temperature temp22 = (Temperature) device;
				String temp2 = status
						.substring(
								configManager.TEMPERATURE_2_POSITION,
								(configManager.TEMPERATURE_2_POSITION + configManager.SIZE_TEMPERATURE));
				if (TextUtils.isEmpty(temp2) == false) {
					try {
						int temperature = Integer.parseInt(temp2);
						if (temperature != configManager.DEFAULT_NONE_TEMPERATURE) {
							temp22.setTemperature(Integer.parseInt(temp2)
									- configManager.DOWN_TEMPERATURE);
						} else {
							temp22.setTemperature(configManager.DEFAULT_NONE_TEMPERATURE);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			break;
		case configManager.LAMP_1:
			if (status.length() >= configManager.LAMP_1
					&& device instanceof LampRoot) {
				LampRoot lamp1 = (LampRoot) device;
				boolean state11 = (status.charAt(configManager.LAMP_1_POSITION) == '1');
				lamp1.setState(state11);
			}
			break;
		case configManager.LAMP_2:
			if (status.length() >= configManager.LAMP_2
					&& device instanceof LampRoot) {
				LampRoot lamp2 = (LampRoot) device;
				boolean state12 = (status.charAt(configManager.LAMP_2_POSITION) == '1');
				lamp2.setState(state12);				
			}
			break;
		case configManager.LAMP_3:
			if (status.length() >= configManager.LAMP_3
					&& device instanceof LampRoot) {
				LampRoot lamp3 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_3_POSITION) == '1');
				lamp3.setState(state);				
			}
			break;
		case configManager.LAMP_4:
			if (status.length() >= configManager.LAMP_4
					&& device instanceof LampRoot) {
				LampRoot lamp4 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_4_POSITION) == '1');
				lamp4.setState(state);				
			}
			break;
		case configManager.LAMP_5:
			if (status.length() >= configManager.LAMP_5
					&& device instanceof LampRoot) {
				LampRoot lamp5 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_5_POSITION) == '1');
				lamp5.setState(state);				
			}
			break;
		case configManager.LAMP_6:
			if (status.length() >= configManager.LAMP_6
					&& device instanceof LampRoot) {
				LampRoot lamp6 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_6_POSITION) == '1');
				lamp6.setState(state);				
			}
			break;
		case configManager.LAMP_7:
			if (status.length() >= configManager.LAMP_7
					&& device instanceof LampRoot) {
				LampRoot lamp7 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_7_POSITION) == '1');
				lamp7.setState(state);				
			}
			break;
		case configManager.LAMP_8:
			if (status.length() >= configManager.LAMP_8
					&& device instanceof LampRoot) {
				LampRoot lamp8 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_8_POSITION) == '1');
				lamp8.setState(state);				
			}
			break;
		case configManager.LAMP_9:
			if (status.length() >= configManager.LAMP_1_POSITION
					&& device instanceof LampRoot) {
				LampRoot lamp9 = (LampRoot) device;
				boolean state = (status.charAt(configManager.LAMP_9_POSITION) == '1');
				lamp9.setState(state);				
			}
			break;
		case configManager.FAN_1:
			if (status.length() >= configManager.FAN_1
					&& device instanceof LampRoot) {
				LampRoot fan = (LampRoot) device;
				boolean state = (status.charAt(configManager.FAN_1_POSITION) == '1');
				fan.setState(state);				
			}
			break;
		case configManager.ROLLER_SHUTTER_1:
			/*
			 * if(status.length() >= configManager.ROLLER_SHUTTER_1){
			 * RollerShutter roller1 = (RollerShutter) device; boolean state=
			 * (status.charAt(configManager.ROLLER_STATUS_1_POSITION) == '1');
			 * Log.d(TAG,
			 * "setStatusForDevice - device: ROLLER_SHUTTER_1, status: " +
			 * state); roller1.set(state); }
			 */
			break;
		case configManager.DOOR_LOCK_1:
			if (status.length() >= configManager.DOOR_LOCK_1
					&& device instanceof DoorLock) {
				DoorLock doorlock = (DoorLock) device;
				boolean state = false;
				/*
				 * state = (status .charAt(configManager.DOOR_LOCK_1_POSITION)
				 * == '1'); doorlock.setState(state);
				 */				
			}
			break;
		case configManager.SMOKE_1:
			if (status.length() > configManager.SMOKE_1
					&& device instanceof Smoke) {
				Smoke smoke = (Smoke) device;
				boolean stateSmoke = (status.charAt(configManager.SMOKE_1) == '1');
				smoke.setState(stateSmoke);				
			}
			break;
		case configManager.SMOKE_2:
			if (status.length() > configManager.SMOKE_2
					&& device instanceof Smoke) {
				Smoke smoke = (Smoke) device;
				boolean stateSmoke = (status.charAt(configManager.SMOKE_2) == '1');
				smoke.setState(stateSmoke);				
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Server
	 */
	public boolean sendcommand(int cmdtype, String rommID, String sensorId,
			String devID, String status) {
		return sendcommand(cmdtype, rommID, sensorId, devID, status, "", "");
	}

	public boolean sendCommand(int cmdtype, String sensorId, String mode,
			String index) {
		return sendcommand(cmdtype, "", sensorId, mDevID, "", mode, index);
	}

	// Send commands to server for Room status update //
	public boolean sendcommand(int cmdtype, String roomID, String sensorID,
			String devID, String status, String mode, String index) {
		/*
		 * if ( ns.CanWrite.Equals(true) && ns.CanWrite.Equals(true) ) {
		 */
		boolean isCharFinish = false;
		try {
			char[] cmdbuf2 = null;
			String cmd = "";

			switch (cmdtype) // send cmd to server
			{
			case configManager.LOGIN_ACTION: // 0// password
				cmd = "00=";
				cmd += status;
				isCharFinish = false;
				break;
			case configManager.GET_ROOM_ADDRESS_ACTION: // 1 // get device //
				cmd = "01=";
				cmd += status;
				break;
			case configManager.GET_ADMIN_ACTION: // 2 // get admin //
				cmd = "02=";
				cmd += status;
				break;
			case configManager.SET_ROOM_ADDRESS: // 3
				cmd = "03=";
				cmd += status;
				break;
			case configManager.SET_ADMIN_ADDRESS: // 4
				cmd = "04=";
				cmd += status;
				break;
			case configManager.GET_ROOM_STATUS: // 5 //Get room status - update
												// room's devices info//
				cmd = "05=";
				cmd += roomID;
				isCharFinish = true;
				break;
			case configManager.SET_DEVICE_STATUS: // 6
				cmd = "06=";
				cmd += roomID + ",1," + devID + ',' + status;
				break;
			case configManager.LEARN_INFRARED: // 7
				cmd = "07=";
				cmd += roomID + "," + sensorID + "," + devID;
				break;
			case configManager.TRANSMIT_INFRARED: // 8
				cmd = "08=";
				cmd += roomID + "," + sensorID + "," + devID;
				break;
			case configManager.GET_DEVICE_STATUS_RELATIONSHIP: // 9 //Get device
																// relationship
				cmd = "09=" + roomID + ",1," + devID;
				break;
			case configManager.SET_DEVICE_STATUS_RELATIONSHIP: // 10
				cmd = "10=" + status;
				break;
			case configManager.GET_AUDIO: // 11
				cmd = "11=";
				cmd += "0";
				break;
			case configManager.SET_AUDIO: // 12
				cmd = "12=";
				cmd += roomID + "," + devID + "," + status;
				break;
			case configManager.SET_LOCK_DOOR: // 13
				cmd = "13=" + roomID + "," + status + devID;
				break;
			case configManager.SET_LOCK_PASSWORD: // 14
				cmd = "14=" + roomID + "," + devID + status;
				break;
			case configManager.GET_LOCK_DOOR: // 15
				cmd = "15=";
				cmd += roomID;
				break;
			case configManager.SET_TIME_OFF: // 16
				cmd = "16=";
				cmd += roomID + ",1," + devID + status;
				break;
			case configManager.GET_TIME_OFF: // 17
				cmd = "17=";
				cmd += roomID;
				break;

			case configManager.SET_CLOCK: // 18
				cmd = "18=" + status;
				break;
			case configManager.GET_CLOCK: // 19
				cmd = "19=0";
				break;
			case configManager.SET_SHEDULE_DEVICE_RELATIONSHIP: // 20
				cmd = "20=" + status;
				break;
			case configManager.GET_SHEDULE_DEVICE_RELATIONSHIP:
				// 21 : get time shedule relationship
				cmd = "21=" + status;
				break;

			case configManager.GET_LOCK_ID_ACTION:
				// 22 get lock Id
				cmd = "22=";
				cmd += status;
				break;

			case configManager.SET_LOCK_ID_ACTION: // 23
				cmd = "23=";
				cmd += status;
				break;

			default:
				break;
			}

			int cmdLen = cmd.length() + 2;
			String strCmdLen = cmdLen >= 10 ? String.valueOf(cmdLen) : "0"
					+ String.valueOf(cmdLen);
			cmd = strCmdLen + cmd;

			cmdbuf2 = new char[cmd.length()];
			cmd.getChars(0, cmd.length(), cmdbuf2, 0);

			Log.d(TAG, "sendcommand::cmd::" + cmd);
			if (mSocket.getOutPrint() != null) {
				mSocket.getOutPrint().print(cmdbuf2);
				mSocket.getOutPrint().flush();
			}
		} catch (Exception ex) {
			Log.d(TAG, "sendcommand::" + ex.toString());
		}
		return isCharFinish;
	}

	public void startSocket() {
		if (mSocket != null && !isLogined()) {
			try {
				listenerSocket();
			} catch (IOException e) {
				Log.d(TAG, "startSocket is failed :: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	public void listenerSocket() throws IOException {
		closeSocket();
		// server.accept returns a client connection
		ThreadSocket w = new ThreadSocket(mSocket);
		Thread t = new Thread(w);
		t.start();
	}

	public void closeSocket() {
		try {
			if (mSocket != null) {
				if (mSocket.getSocket() != null) {
					Socket socket = mSocket.getSocket();
					socket.close();
					mSocket.setTcpconnect(false);
					stopTimerDevice();
					mIsLogined = false;
				}
			}
			Log.d(TAG, "CloseSocket: Closed Connection to Server");
		} catch (IOException e) {
			Log.d(TAG, "CloseSocket: Error: " + e.toString());
			e.printStackTrace();
		}
	}

	public void addConnectSocketObserver(ConnectSocketListener observer) {
		mSocketListener.remove(observer);
		mSocketListener.add(observer);
	}

	public void removeConnectSocketObserver(ConnectSocketListener observer) {
		mSocketListener.remove(observer);
	}

	public void notifyConnectSocketObserver(boolean isConnected) {
		int size = mSocketListener.size();
		ConnectSocketListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mSocketListener.elementAt(i);
			if (observer != null) {
				observer.socketConnected(isConnected);
			}
		}
	}

	public void addLoginObserver(LoginListener observer) {
		mLoginListener.remove(observer);
		mLoginListener.add(observer);
	}

	public void removeLoginObserver(LoginListener observer) {
		mLoginListener.remove(observer);
	}

	public void notifyLoginListener(boolean isLogined) {
		int size = mLoginListener.size();
		LoginListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mLoginListener.elementAt(i);
			if (observer != null) {
				observer.eventLogined(isLogined);
			}
		}
	}

	public void addStatusObserver(StatusListener observer) {
		mStatusListener.remove(observer);
		mStatusListener.add(observer);
	}

	public void removeStatusObserver(StatusListener observer) {
		mStatusListener.remove(observer);
	}

	public void notifyStatusObserver() {
		int size = mStatusListener.size();
		StatusListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mStatusListener.elementAt(i);
			if (observer != null) {
				observer.changeStatusDevice();
			}
		}
	}

	public void addXMLObserver(XMLListener observer) {
		mXMLListener.remove(observer);
		mXMLListener.add(observer);
	}

	public void removeXMLObserver(XMLListener observer) {
		mXMLListener.remove(observer);
	}

	public void notifyXMLObserver() {
		int size = mXMLListener.size();
		XMLListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mXMLListener.elementAt(i);
			if (observer != null) {
				observer.savedXML();
			}
		}
	}

	public void addGettingSRObserver(GetStatusRelationshipListener observer) {
		mGettingSRListeners.remove(observer);
		mGettingSRListeners.add(observer);
	}

	public void removeGettingSRObserver(GetStatusRelationshipListener observer) {
		mGettingSRListeners.remove(observer);
	}

	public void notifyGettingSRObserver(StatusRelationship sr) {
		int size = mGettingSRListeners.size();
		GetStatusRelationshipListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mGettingSRListeners.elementAt(i);
			if (observer != null) {
				observer.recieveStatusRelationship(sr);
			}
		}
	}

	public void addPageScrolledRObserver(OnPageScrolledCompleteListener observer) {
		mPageScrolledCompleteListener.remove(observer);
		mPageScrolledCompleteListener.add(observer);
	}

	public void removePageScrolledObserver(
			OnPageScrolledCompleteListener observer) {
		mPageScrolledCompleteListener.remove(observer);
	}

	public void notifyPageScrolledObserver(boolean isShow) {
		int size = mPageScrolledCompleteListener.size();
		OnPageScrolledCompleteListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mPageScrolledCompleteListener.elementAt(i);
			if (observer != null) {
				observer.onViewScrolledComplete(isShow);
			}
		}
	}

	public List<Object> initAllDevices() {
		List<Object> objects = new ArrayList<Object>();
		Area area = null;
		Room room = null;
		Device device = null;

		for (int i = 0; i < configManager.MAX_AREA; i++) {
			area = new Area();
			area.setId(i + 1);
			area.setName(HomeCenter2.getContext().getString(R.string.area)
					+ " " + i);
			objects.add(area);

			for (int j = 0; j < configManager.MAX_ROOM_GSM_IN_AREA; j++) {

				room = new Room();
				room.setId(j + 1);

				area.addRoomToArea(room);
				// test
				room.setActive(true);
				int temp = j - configManager.MAX_ROOM_IN_AREA;

				if (temp >= 0) {
					room.setOtherType(true);
					objects.add(room);
					switch (temp) {
					case 0:
						String strAudio = HomeCenter2.getContext().getString(
								R.string.room_audio);
						room.setName(strAudio);
						break;
					case 1:
						String strGSM = HomeCenter2.getContext().getString(
								R.string.room_gsm);
						room.setName(strGSM);

						break;
					default:
						break;
					}
				} else {
					room.setName(HomeCenter2.getContext().getString(
							R.string.room)
							+ " " + (j + 1));
					room.setOtherType(false);
					objects.add(room);
					for (int k = 0; k < configManager.MAX_DEVICE_IN_ROOM; k++) {
						device = initDevice(k);
						device.setName(device.getName() + (j + 1) + (k + 1));
						device.setId(k);
						room.addDevice(device);
						objects.add(device);
					}
				}
			}
		}
		Log.d(TAG, "initAllDevices:: new list devices is created:: size:: "
				+ objects.size());
		return objects;
	}

	public List<Object> getAllObjectInHome() {
		return mHouse.getAllObjectInHome();
	}

	public Device initDevice(int id) {
		Log.d(TAG, "initDevice::id:" + id);
		Context context = HomeCenter2.getContext();
		if (context == null)
			return null;
		switch (id) {
		case configManager.TEMPERATURE_1:
		case configManager.TEMPERATURE_2:
			Temperature temp = new Temperature();
			temp.setName(context.getString(R.string.temperature));
			return temp;
		case configManager.LIGHT_1:
		case configManager.LIGHT_2:
			Light light = new Light();
			light.setName(context.getString(R.string.light));
			return light;

		case configManager.DOOR_STATUS_1:
		case configManager.DOOR_STATUS_2:
		case configManager.DOOR_STATUS_3:
		case configManager.DOOR_STATUS_4:
			DoorStatus ds = new DoorStatus();
			ds.setName(context.getString(R.string.doorstatus));
			return ds;

		case configManager.MOTION_1:
		case configManager.MOTION_2:
		case configManager.MOTION_3:
		case configManager.MOTION_4:
			Motion motion = new Motion();
			motion.setName(context.getString(R.string.motion));
			return motion;

		case configManager.LAMP_1:
		case configManager.LAMP_2:
		case configManager.LAMP_3:
		case configManager.LAMP_4:
		case configManager.LAMP_5:
		case configManager.LAMP_6:
		case configManager.LAMP_7:
		case configManager.LAMP_8:
		case configManager.LAMP_9:
			Lamp lamp = new Lamp();
			lamp.setName(context.getString(R.string.lamp));
			return lamp;
		case configManager.FAN_1:
			Fan fan = new Fan();
			fan.setName(context.getString(R.string.fan));
			return fan;
		case configManager.DOOR_LOCK_1:
			DoorLock doorLock = new DoorLock();
			doorLock.setName(context.getString(R.string.doorlock));
			return doorLock;
		case configManager.ROLLER_SHUTTER_1:
			RollerShutter roller = new RollerShutter();
			roller.setName(context.getString(R.string.rollerShutter));
			return roller;
		case configManager.SMOKE_1:
		case configManager.SMOKE_2:
			Smoke smoke = new Smoke();
			smoke.setName(context.getString(R.string.smoke));
			return smoke;
		case configManager.CAMERA_1:
		case configManager.CAMERA_2:
		case configManager.CAMERA_3:
		case configManager.CAMERA_4:
			Camera camera = new Camera();
			camera.setName(context.getString(R.string.camera));
			return camera;
		default:
			break;
		}
		return null;
	}

	public boolean isAppInForeground() {
		return mAppInForeground;
	}

	public void setAppInForeground(boolean appInForeground) {
		this.mAppInForeground = appInForeground;
	}

	public void onActivityResume(Activity activity) {
		setCurrentActivity(activity);
		mAppInForeground = true;
	}

	public void onActivityPause(Activity activity) {
		setCurrentActivity(null);
	}

	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	public void setCurrentActivity(Activity mCurrentActivity) {
		this.mCurrentActivity = mCurrentActivity;
	}

	public House getHouse() {
		return mHouse;
	}

	public void setHouse(House mHouse) {
		this.mHouse = mHouse;
	}

	public Bundle getForceOpenScreenBundle() {
		return forceOpenScreenBundle;
	}

	public void setForceOpenScreenBundle(Bundle forceOpenScreenBundle) {
		this.forceOpenScreenBundle = forceOpenScreenBundle;
	}

	// new

	public static String MENU_SCREEN = "menu_screen";
	public static String MAIN_SCREEN = "main_screen";
	public static String IDENTITY_PRIMARY_STACK_FRAGMENT = "identity_primary_statck_selection";
	public static String GLOBAL_SEARCH_FRAGMENT = "identity_primary_statck_selection_menu_screen";

	public void saveRoom(Room room) {
		if (room == null) {
			return;
		}
		room.refreshDevicesInRoom(false);
		List<Object> objects = getHouse().getAllObjectInHome();
		Log.d(TAG, "saveRoom:: " + objects.size());
		if (objects != null && objects.size() > 0) {
			boolean result = XMLHelper.createDeviceFileXml(objects);
			if (result == true) {
				notifyXMLObserver();
			}
		}
	}

	public void getAllRoomsStatus() {
		Log.d(TAG, "getAllRoomsStatus:: ");
		if (mHouse == null)
			return;
		List<Room> rooms = mHouse.getRooms();
		int sizeRoom = rooms.size();
		Room room = null;
		for (int i = 0; i < sizeRoom; i++) {
			room = rooms.get(i);
			if (room != null) {
				getRoomStatus(room.getId());
			}
		}
	}

	public int getRemoteType() {
		return mRemoteType;
	}

	public void setRemoteType(int mRemoteType) {
		this.mRemoteType = mRemoteType;
	}

	/*
	 * Actions to server
	 */

	/**
	 * ID= 0 Get PW for login
	 * 
	 * @param ps
	 * @return
	 */
	public boolean psswd(Password ps) {
		Log.d(TAG, "psswd: " + ps.getName() + ", pw: " + ps.getPassword());
		String pass = ps.getName() + ';' + ps.getPassword();

		for (int i = 0; i < configManager.MAX_TIME_SEND_AGAIN; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.LOGIN_ACTION, mRoomID, mDevID, pass);
			if (receiveMessage == null) {
				return false;
			}
			// test
			// String receiveMessage = ":pwdOK";
			Log.d(TAG, "psswd: receive: " + receiveMessage);
			String[] response = receiveMessage.split("\\r|\\n|!");

			if (response[0].equals(":pwdOK")) {
				// closeSocket();
				mIsLogined = true;
				response = null;
				return true;
			}
		}

		return false;
	}

	/**
	 * ID = 1 Get address of rooms
	 */
	public void getRoomAddress() {

		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_ROOM_ADDRESS_ACTION, "1", "1", "dev");
		if (receiveMessage == null) {
			return;
		}
		Log.d(TAG, "getRoomAddress- receiveMessage:" + receiveMessage);

		// test
		// old: String receiveMessage =
		// "880081,880082,880083,880084,880085,880086,880087,880088";
		// new
		// String receiveMessage = "80,81,82,83,84,85,86,87,88,89";
		String[] response = receiveMessage.split("\\r|\\n|,");
		String[] deviceIDs = mHouse.getDeviceID();
		int size = response.length;

		for (int i = 0; i < size; i++) {
			deviceIDs[i] = response[i].trim();
		}

		// id = 0 or id = 9 is GSM and other

		List<Room> rooms = mHouse.getRooms();
		if (rooms == null)
			return;
		int sizeRoom = rooms.size();
		// debug
		Log.d(TAG, "getRoomAddress- size getrooms:" + sizeRoom);
		// debug
		Room room = null;
		for (int i = 0; i < sizeRoom && i < size; i++) {
			room = rooms.get(i);
			room.setId(i + 1);
			room.setIdSever(deviceIDs[i + 1]);
			saveRoom(room);
		}

		List<Room> others = mHouse.getOtherRooms();
		int sizeOther = others.size();

		if (configManager.MAX_ROOM_IN_AREA < size && sizeOther >= 2) {
			room = others.get(configManager.POSITION_AUDIO);
			room.setIdSever(deviceIDs[size - 1]);

			room = others.get(configManager.POSITION_GSM);
			room.setIdSever(deviceIDs[0]);
		}

		/*
		 * List<Room> rooms = mHouse.getRooms(); int sizeRoom = rooms.size();
		 * Room room = null; for(int i = 0; i< sizeRoom && i < size; i++){ room
		 * = rooms.get(i); room.setId(i); saveRoom(room); }
		 */

	}

	/**
	 * ID = 2 Get admin info
	 */
	public void getadm() {

		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_ADMIN_ACTION, "1", "1", "adm");
		Log.d(TAG, "getadm- receiveMessage: " + receiveMessage);
		// test
		// String receiveMessage =
		// "user,123,192.168.102.1,4,4,65,0901234567,0123456789,0838456789,Alarm!!";
		if (receiveMessage == null) {
			return;
		}
		String[] response = receiveMessage.split("\\r|\\n|,");

		if (response.length >= 10) {
			Password pw = mHouse.getPassword();
			if (!TextUtils.isEmpty(response[0].trim())) {
				pw.setName(response[0].trim());
			}
			if (!TextUtils.isEmpty(response[1].trim())) {
				pw.setPassword(response[1].trim());
			}

			if (!TextUtils.isEmpty(response[3].trim())) {
				mHouse.setLightLevelTrigger(Integer.parseInt(response[3].trim()));
			}
			if (!TextUtils.isEmpty(response[4].trim())) {
				mHouse.setSmokeLevelTrigger(Integer.parseInt(response[4].trim()));
			}
			if (!TextUtils.isEmpty(response[5].trim())) {
				mHouse.setTemperatureTrigger(Integer.parseInt(response[5]
						.trim()));
			}
			mHouse.setPhone01(response[6].trim());
			mHouse.setPhone02(response[7].trim());
			mHouse.setPhone03(response[8].trim());
			mHouse.setGsm(response[9].trim());
		}
	}

	/**
	 * ID= 3 , 4 Set device (room) config, admin config
	 * 
	 * @param bundle
	 * @return
	 */

	public boolean setRoomInfomation(Bundle bundle) {
		boolean result = true;
		String device = bundle.getString(configManager.SAVE_ROOMIDS);
		String lock = bundle.getString(configManager.SAVE_LOCKIDS);
		Log.d(TAG, "setRoomInfomation:" + device + ", lock id: " + lock);
		setRoomAddress(device);
		setLockId(lock);
		getInformationRoom();
		return result;
	}

	private boolean setRoomAddress(String device) {
		boolean result = false;
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_ROOM_ADDRESS, "1", "1", device);
			Log.d(TAG, "setRoomAddress-device - " + i + " : " + device
					+ " , receiveMessage: " + receiveMessage);
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n|!");
			if (response[0].contains(":sttOK")) {
				i = 2;
				result = true;
				// Log.d(TAG, "saveConfig:: saved devices");
			}
		}
		return result;
	}

	public boolean setAdminConfig(Bundle bundle) {
		boolean result = false;
		for (int i = 0; i < 2; i++) {
			String config = bundle.getString(configManager.SAVE_CONFIG);
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_ADMIN_ADDRESS, "1", "1", config);
			Log.d(TAG, "setRoomConfig - receiveMessage: " + receiveMessage);
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n|!");
			if (response[0].contains(":sttOK")) {
				i = 2;
				result = true;
				// write ip.txt
			}
		}
		if (result == true) {
			getInformationAdmin();
		}
		return result;
	}

	/**
	 * ID = 5 Get Room status
	 * 
	 * @param roomId
	 * @return
	 */
	public String getRoomStatus(int roomId) {
		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_ROOM_STATUS, String.valueOf(roomId), "0", "0");
		if (receiveMessage == null) {
			return "";
		}
		// test old
		//String receiveMessage = ":gs880081;32101101111103025200000000001687";
		//String receiveMessage = ":gs88;21100111001125225200010001001701";

		Log.d("TMT room", "getRoomStatus: " + roomId + " - receiveMessage :"
				+ receiveMessage);
		String[] response = receiveMessage.split(";|:|\\r|\\n|!");
		if (response.length >= 3) {
			String strStatusDevice = response[2];
			if (!TextUtils.isEmpty(strStatusDevice)
					&& strStatusDevice.length() == 32) {
				Log.d("TMT room", "getRoomStatus: text" + strStatusDevice);
				Room room = mHouse.getRoomsById((roomId));
				if (room != null) {
					List<Device> devices = room.getDevices();
					int size = devices.size();
					Device device = null;
					for (int i = 0; i < size; i++) {
						device = devices.get(i);
						setStatusForDevice(i, strStatusDevice, device);
					}
					room.putState();
				}
				notifyStatusObserver();
			}
		}
		return receiveMessage;
	}

	/**
	 * ID= 6 Set device status
	 * 
	 * @param bundle
	 * @return
	 */
	public boolean setOnOffDevice(Bundle bundle) {
		boolean result = true;
		int roomId = bundle.getInt(configManager.ROOM_ID);
		int id = bundle.getInt(configManager.DEVICE_ID);
		String devId = configManager.convertDeviceIdToString(id);
		boolean isOn = bundle.getBoolean(configManager.ON_OFF_ACTION);

		String stringIsOn = isOn ? "1" : "0";

		Log.d(TAG, "setOnOffDevice:: roomId:: " + roomId + " , devId: " + devId
				+ " , isOn: " + stringIsOn);

		String receiveMessage = "";
		String[] response = null;

		for (int i = 0; i < 2; i++) {
			receiveMessage = sendAndReceiveMessage(
					configManager.SET_DEVICE_STATUS, String.valueOf(roomId),
					devId, stringIsOn);
			if (receiveMessage == null) {
				continue;
			}
			response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("O")
					|| response[0].contains("k");

			Log.d(TAG, "setOnOffDevice: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":ssOk") || isContain == true))
			/*
			 * if (response != null && (response[0].equals(":ssOk") ||
			 * (response.length >=2 && (receiveMessage.length() > 36) &&
			 * (response[0].equals("")) && (response[2].length() >= 32))))
			 */{
				i = 2;
				result = true;
			}
		}

		return result;
	}

	/**
	 * ID = 7 Learn Infrared
	 */
	public boolean updateRemote(Bundle bundle) {
		boolean result = false;
		if (bundle == null) {
			return false;
		}
		String roomId, sensorId, deviceId;
		roomId = bundle.getString(configManager.ROOM_ID);
		sensorId = bundle.getString(configManager.SENSOR_ID);
		deviceId = bundle.getString(configManager.DEVICE_ID);

		Log.d(TAG, "updateRemote:: roomId:: " + roomId + " , sensorId: "
				+ sensorId + " , devId: " + deviceId);

		String receiveMessage = "";
		String[] response = null;

		for (int i = 0; i < 2; i++) {
			try {
				receiveMessage = sendAndReceiveMessage(
						configManager.LEARN_INFRARED, roomId, sensorId,
						deviceId, "");
			} catch (InterruptedIOException e) {
				e.printStackTrace();
			} finally {
				if (receiveMessage == null) {
					continue;
				}
				response = receiveMessage.split("\\r|\\n");
				Log.d(TAG, "updateRemote - "
						+ (response != null ? response[0] : "response"));
				if (response != null && (response[0].equals(":liOk")))
				/*
				 * if (response != null && (response[0].equals(":ssOk") ||
				 * (response.length >=2 && (receiveMessage.length() > 36) &&
				 * (response[0].equals("")) && (response[2].length() >= 32))))
				 */{
					i = 2;
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * ID= 8 Transmit Infrared
	 * 
	 * @return
	 */
	public boolean controlRemote(Bundle bundle) {
		boolean result = false;

		if (bundle == null) {
			return false;
		}
		String roomId, sensorId, deviceId;
		roomId = bundle.getString(configManager.ROOM_ID);
		sensorId = bundle.getString(configManager.SENSOR_ID);
		deviceId = bundle.getString(configManager.DEVICE_ID);

		Log.d(TAG, "controlRemote::set status::6:: roomId:: " + roomId
				+ " , sensorId: " + sensorId + " , devId: " + deviceId);

		String receiveMessage = null;
		String[] response = null;

		for (int i = 0; i < 2; i++) {
			try {
				receiveMessage = sendAndReceiveMessage(
						configManager.TRANSMIT_INFRARED, roomId, sensorId,
						deviceId, "");
			} catch (InterruptedIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (receiveMessage == null) {
					continue;
				}
				response = receiveMessage.split("\\r|\\n");
				Log.d(TAG, "controlRemote-"
						+ (response != null ? response[0] : "response"));
				if (response != null && (response[0].equals(":tiOk")))
				/*
				 * if (response != null && (response[0].equals(":ssOk") ||
				 * (response.length >=2 && (receiveMessage.length() > 36) &&
				 * (response[0].equals("")) && (response[2].length() >= 32))))
				 */{
					i = 2;
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * ID = 9 get device status relationship (detail device)
	 * 
	 * @param bundle
	 */

	/*
	 * 1821=2,1,08,#1084# / 20,1,31,12,0000000,1 //phut,h,d,m,rep,on 1/off
	 * 
	 * 2 --> room index (1-8) 1 --> active (1) 08 --> device# (01-10) #2084# -->
	 * schedule index (1-4)
	 */

	public StatusRelationship getTimeShedule(int roomId, String devId,
			int number, StatusRelationship sr, boolean isDevice) {
		String status = "";
		if (isDevice)
			status = configManager.SHIFT3 + roomId + devId + number
					+ configManager.SHIFT3;
		else
			status = configManager.PER + roomId + devId + number
					+ configManager.PER;

		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_SHEDULE_DEVICE_RELATIONSHIP,
				String.valueOf(roomId), devId, status);
		// receiveMessage = "20,1,31,12,0001010,r";

		if (receiveMessage == null) {
			return sr;
		}

		/*
		 * if (number == 1) { receiveMessage = "20,1,31,12,0001010,1"; } else {
		 * receiveMessage = "7,4,12,3,0001010,0"; }
		 */
		// 26 18 13 11 * 0
		Log.d(TAG, "getTimeShedule::receiveMessage/" + receiveMessage);
		String[] response = receiveMessage.split("\\r|\\n| ");
		if (response.length >= 6) {
			String valueResponseAtPosition = "";

			int min, hour, day, month;

			min = Integer.parseInt(response[configManager.SR_MINUTE]);
			hour = Integer.parseInt(response[configManager.SR_HOUR]);

			MyTime time = new MyTime(min, hour);

			day = Integer.parseInt(response[configManager.SR_DAY]);
			month = Integer.parseInt(response[configManager.SR_MONTH]);

			MyDate date = new MyDate(day, month);

			// repeat
			boolean mon, tue, wed, thu, fri, sat, sun;
			mon = tue = wed = thu = fri = sat = sun = false;

			valueResponseAtPosition = response[configManager.SR_REPEAT_POSITION];
			if (valueResponseAtPosition
					.compareTo(configManager.NOT_SET_DAY_SCHEDULE) != 0) {
				String[] days = valueResponseAtPosition.split(",");
				int sizeDays = days.length;
				for (int i = 0; i < sizeDays; i++) {
					String dayString = days[i];
					if (!TextUtils.isEmpty(dayString)) {
						try {
							int dayInt = Integer.parseInt(dayString);
							switch (dayInt) {
							case configManager.SR_MON_POSITION:
								mon = true;
								break;
							case configManager.SR_TUE_POSITION:
								Log.d(TAG, "1");
								tue = true;
								break;
							case configManager.SR_WED_POSITION:
								wed = true;
								break;
							case configManager.SR_THU_POSITION:
								thu = true;
								break;
							case configManager.SR_FRI_POSITION:
								fri = true;
								break;
							case configManager.SR_SAT_POSITION:
								sat = true;
								break;
							case configManager.SR_SUN_POSITION:
								sun = true;
								break;
							}
						} catch (Exception ex) {
						}
					}
				}
			}
			// on/off
			int on = 1;
			String sOn = response[configManager.SR_IS_ON];
			if (!sOn.equalsIgnoreCase("r")) {
				try {
					on = Integer.parseInt(sOn);
				} catch (Exception ex) {

				}
			}

			switch (number) {
			case 1:
				sr.setTimeS01(time);
				sr.setDateS01(date);
				sr.setMonS01(mon);
				sr.setTueS01(tue);
				sr.setWedS01(wed);
				sr.setThuS01(thu);
				sr.setFriS01(fri);
				sr.setSatS01(sat);
				sr.setSunS01(sun);
				sr.setOnS01(on == 1 ? true : false);
				sr.setAddedScheduleS01(true);
				break;
			case 2:
				sr.setTimeS02(time);
				sr.setDateS02(date);
				sr.setMonS02(mon);
				sr.setTueS02(tue);
				sr.setWedS02(wed);
				sr.setThuS02(thu);
				sr.setFriS02(fri);
				sr.setSatS02(sat);
				sr.setSunS02(sun);
				sr.setOnS02(on == 1 ? true : false);
				sr.setAddedScheduleS02(true);
				break;
			case 3:
				sr.setTimeS03(time);
				sr.setDateS03(date);
				sr.setMonS03(mon);
				sr.setTueS03(tue);
				sr.setWedS03(wed);
				sr.setThuS03(thu);
				sr.setFriS03(fri);
				sr.setSatS03(sat);
				sr.setSunS03(sun);
				sr.setOnS03(on == 1 ? true : false);
				sr.setAddedScheduleS03(true);
				break;

			case 4:
				sr.setTimeS04(time);
				sr.setDateS04(date);
				sr.setMonS04(mon);
				sr.setTueS04(tue);
				sr.setWedS04(wed);
				sr.setThuS04(thu);
				sr.setFriS04(fri);
				sr.setSatS04(sat);
				sr.setSunS04(sun);
				sr.setOnS04(on == 1 ? true : false);
				sr.setAddedScheduleS04(true);
				break;
			default:
				break;
			}

		}
		return sr;
	}

	public StatusRelationship getDeviceTurnWhen(int roomId, String devId,
			StatusRelationship sr) {
		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_DEVICE_STATUS_RELATIONSHIP,
				String.valueOf(roomId), devId, "1");
		if (receiveMessage == null) {
			return sr;
		}
		// receiveMessage = ":gr888:999910100000000000"; 2: so thu tu den, 2: ko quan tam, 14: device
		Log.d(TAG, "getDeviceTurnWhen 1::receive/" + receiveMessage);
		String[] response = receiveMessage.split(";|:");
		if (response.length >= 3) {
			String valueResponseAtPosition = response[2];
			char valueAtPosition;
			valueResponseAtPosition = valueResponseAtPosition.substring(4, valueResponseAtPosition.length()-1);
			Log.d(TAG, "getDeviceTurnWhen 1::receive/" + valueResponseAtPosition + " , size: " + valueResponseAtPosition.length());
			if (valueResponseAtPosition.length() >= (configManager.SR_DOOR4_POSITION+1)) {
				// temp1
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_TEMP1_POSITION);
				sr.setTemp1(valueAtPosition == '1' ? true : false);

				// light1
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_LIGHT1_POSITION);
				sr.setLight1(valueAtPosition == '1' ? true : false);

				// smoke1
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_SMOKE1_POSITION);
				sr.setSmoke1(valueAtPosition == '1' ? true : false);

				// motion1
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_MOTION1_POSITION);
				sr.setMotion1(valueAtPosition == '1' ? true : false);

				// motion2
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_MOTION2_POSITION);
				sr.setMotion2(valueAtPosition == '1' ? true : false);

				// door1
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_DOOR1_POSITION);
				sr.setDoor1(valueAtPosition == '1' ? true : false);

				// door2
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_DOOR2_POSITION);
				sr.setDoor2(valueAtPosition == '1' ? true : false);

				// temp2
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_TEMP2_POSITION);
				sr.setTemp2(valueAtPosition == '1' ? true : false);

				// light2
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_LIGHT2_POSITION);
				sr.setLight2(valueAtPosition == '1' ? true : false);

				// smoke2
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_SMOKE2_POSITION);
				sr.setSmoke2(valueAtPosition == '1' ? true : false);

				// motion3
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_MOTION3_POSITION);
				sr.setMotion3(valueAtPosition == '1' ? true : false);

				// motion4
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_MOTION4_POSITION);
				sr.setMotion4(valueAtPosition == '1' ? true : false);

				// door3
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_DOOR3_POSITION);
				sr.setDoor3(valueAtPosition == '1' ? true : false);

				// door4
				valueAtPosition = valueResponseAtPosition
						.charAt(configManager.SR_DOOR4_POSITION);
				sr.setDoor4(valueAtPosition == '1' ? true : false);

				// trigger
				/*
				 * valueAtPosition = valueResponseAtPosition
				 * .charAt(configManager.SR_TRIGGER_POSITION);
				 * sr.setTrigger(valueAtPosition == '1' ? true : false);
				 */
			}
		}
		return sr;
	}

	public void getDeviceStatusRelationship(Bundle bundle) {
		int roomId = bundle.getInt(configManager.ROOM_ID);
		String devId = bundle.getString(configManager.DEVICE_ID);
		boolean isDevice = bundle.getBoolean(configManager.IS_DEVICE_BUNDLE);
		boolean hasGetSchedule = bundle
				.getBoolean(configManager.HAS_GET_SCHEDULE);

		Log.d(TAG, "getDeviceStatusRelationship:: roomId:: " + roomId
				+ " , devId: " + devId);
		StatusRelationship sr = new StatusRelationship();
		if (hasGetSchedule) {

			sr.setAddedScheduleS01(false);
			sr.setAddedScheduleS02(false);
			sr.setAddedScheduleS03(false);
			sr.setAddedScheduleS04(false);

			sr = getTimeShedule(roomId, devId, 1, sr, isDevice);
			sr = getTimeShedule(roomId, devId, 2, sr, isDevice);
			sr = getTimeShedule(roomId, devId, 3, sr, isDevice);
			sr = getTimeShedule(roomId, devId, 4, sr, isDevice);
		}
		if (isDevice) {
			sr = getDeviceTurnWhen(roomId, devId, sr);
		}
		sr = getTimerOff(roomId, devId, sr);

		notifyGettingSRObserver(sr);

	}

	/**
	 * ID = 10 Set device relationship (save detail device)
	 * 
	 * @param bundle
	 * @return
	 */
	public boolean setDeviceRelationship(Bundle bundle) {
		int roomId = bundle.getInt(configManager.ROOM_ID);
		String devId = bundle.getString(configManager.DEVICE_ID);
		Bundle bundleGetDevice = new Bundle();
		boolean isDevice = false;
		boolean isSchedule = false;
		int isType = bundle.getInt(configManager.TYPE);
		switch (isType) {
		case 0:
		case 1:
		case 2:
		case 3:
			String schedule = bundle.getString(configManager.SHEDULE);
			setSchedule(roomId, devId, schedule);
			isSchedule = true;

			break;
		case 4:
			String turnWhenDevice = bundle.getString(configManager.SHEDULE);
			if (TextUtils.isEmpty(turnWhenDevice) == false) {
				isDevice = true;
				setTurnWhenDevice(roomId, devId, turnWhenDevice);
				isSchedule = false;
			}
			break;

		default:
			break;
		}
		 String se = bundle.getString(configManager.TURN_SMART_ENERGY);

		 setTimerOff(roomId, devId, se);

		bundleGetDevice.putInt(configManager.ROOM_ID, roomId);
		bundleGetDevice.putString(configManager.DEVICE_ID, devId);
		bundleGetDevice.putBoolean(configManager.IS_DEVICE_BUNDLE, isDevice);
		bundleGetDevice.putBoolean(configManager.HAS_GET_SCHEDULE, isSchedule);
		getDeviceStatusRelationship(bundleGetDevice);
		return true;
	}

	// :stOK
	private boolean setSchedule(int roomId, String devId, String schedule) {
		String receiveMessage = "";
		for (int i = 0; i < 2; i++) {
			receiveMessage = sendAndReceiveMessage(
					configManager.SET_SHEDULE_DEVICE_RELATIONSHIP,
					String.valueOf(roomId), devId, schedule);
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("t")
					|| response[0].contains("O") || response[0].contains("k");

			Log.d(TAG, "setSchedule: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":stOk") || isContain == true))
			/*
			 * if (response != null && (response[0].equals(":ssOk") ||
			 * (response.length >=2 && (receiveMessage.length() > 36) &&
			 * (response[0].equals("")) && (response[2].length() >= 32))))
			 */{
				i = 2;
				return true;
			}
		}
		return false;
	}

	// :srOk
	private boolean setTurnWhenDevice(int roomId, String devId, String schedule) {
		String receiveMessage = "";
		for (int i = 0; i < 2; i++) {
			receiveMessage = sendAndReceiveMessage(
					configManager.SET_DEVICE_STATUS_RELATIONSHIP,
					String.valueOf(roomId), devId, schedule);
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("r")
					|| response[0].contains("O") || response[0].contains("k");

			Log.d(TAG, "setTurnWhenDevice: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":srOk") || isContain == true))
			/*
			 * if (response != null && (response[0].equals(":ssOk") ||
			 * (response.length >=2 && (receiveMessage.length() > 36) &&
			 * (response[0].equals("")) && (response[2].length() >= 32))))
			 */{
				i = 2;
				return true;

			}
		}
		return false;
	}

	/**
	 * ID = 11 Get Media (get audio)
	 * 
	 * @return
	 */
	public boolean getAudio() {
		boolean result = false;
		
		for (int k = 0; k < 2; k++) {
			Log.d(TAG, "getAudio: " + k);
			String receiveMessage = sendAndReceiveMessage(
					configManager.GET_AUDIO, "0", "0", "0");
			// test old
			//receiveMessage = ":gs66;25483651,192";
			if(k==0)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			if (receiveMessage == null) {
				continue;
			}

			Log.d(TAG, "getMedia - receiveMessage :" + receiveMessage + ", k: " + k);

			String[] response = receiveMessage.split(";|,");
			String str = "";
			Log.d(TAG, "getMedia - receiveMessage : size: " + response.length) ;
			if (response.length >= 3) {
				str = response[1];
				Log.d(TAG, "getMedia - receiveMessage Audio : str: " + str);
				if (TextUtils.isEmpty(str) == false && str.length() >= 8) {
					k = 1;
					result = true;

					int[] values = new int[8];
					if (result == true && TextUtils.isEmpty(str) == false) {
						for (int i = 0; i < 8; i++) {
							int temp = 0;
							try {
								temp = Integer.parseInt(String.valueOf(str
										.charAt(i)));
							} catch (Exception ex) {
								temp = 0;
							}
							Log.d(TAG, "getMedia - receiveMessage Audio : " + i
									+ " value: " + temp);
							values[i] = temp;
						}
					} else {
						for (int i = 0; i < 8; i++) {
							values[i] = 1;
						}
					}
					notifyAudioObserver(values);
				}
			}

		}
		return result;
	}

	/**
	 * ID = 12 Set media (set audio)
	 * 
	 * @return
	 */
	public boolean setAudio(Bundle bundle) {

		int roomId = bundle.getInt(configManager.ROOM_ID);

		int id = bundle.getInt(configManager.DEVICE_ID);
		String devId = String.valueOf(id);
		boolean isOn = bundle.getBoolean(configManager.ON_OFF_ACTION);
		String stringIsOn = isOn ? "1" : "0";

		boolean result = false;
		if (roomId == configManager.ROOM_ALL_ID) {
			for (int i = 1; i <= 8; i++) {
				setAudio(i, devId, stringIsOn);
				SystemClock.sleep(500);
			}
			result = true;

		} else {
			result = setAudio(roomId, devId, stringIsOn);
		}
		if (result == true) {
			getAudio();
		}
		return result;
	}

	private boolean setAudio(int roomId, String devId, String stringIsOn) {
		boolean result = false;
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_AUDIO, String.valueOf(roomId), devId,
					stringIsOn);
			if (receiveMessage == null) {
				return false;
			}
			Log.d(TAG, "setAudio:: roomId:: " + roomId + " , devId: " + devId
					+ ",  receiveMessage: " + receiveMessage);
			// receiveMessage = ":smOk";
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("m")
					|| response[0].contains("O") || response[0].contains("k");

			Log.d(TAG, "setAudio: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":smOk") || isContain == true)) {
				i = 2;
				result = true;

			}
		}
		return result;
	}

	/**
	 * ID = 13 Set Lock Door 1013=7,201 room,mode[deviceId] + index: 2 digit 01
	 * - 10 + Example: ":sl88;304" response: ":slOk"
	 * 
	 * @return
	 */
	public boolean setLockDoor(Bundle bundle) {
		int id = bundle.getInt(configManager.DEVICE_ID);
		String devId = configManager.convertIdToString(id);
		int roomId = bundle.getInt(configManager.ROOM_ID);
		String status = bundle.getString(configManager.MODE_STATUS);
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_LOCK_DOOR, String.valueOf(roomId), devId,
					status);
			Log.d(TAG, "setLockDoor:: roomId:: " + roomId + " , devId: "
					+ devId + ",  receiveMessage: " + receiveMessage);
			if (receiveMessage == null) {
				continue;
			}
			// receiveMessage = ":slOk";
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("l")
					|| response[0].contains("O") || response[0].contains("k");

			Log.d(TAG, "setLockDoor: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":slOk") || isContain == true)) {
				i = 2;
				return true;

			}
		}
		return false;
	}

	/**
	 * ID = Get Lock Door
	 * 
	 * @return
	 */
	public boolean getLockDoor(Bundle bundle) {
		int roomId = bundle.getInt(configManager.ROOM_ID);
		int id = bundle.getInt(configManager.DEVICE_ID);
		String devId = configManager.convertDeviceIdToString(id);
		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_LOCK_DOOR, String.valueOf(roomId), devId, "1");
		//receiveMessage = ":gl220021;1111100000";
		if (receiveMessage == null) {
			return false;
		}
		Log.d(TAG, "getLockDoor::roomId:: " + roomId + " , devId: " + devId
				+ " receiveMessage " + receiveMessage + " , id: " + id);
		String[] response = receiveMessage.split("\\r|\\n|;");
		if (response.length >= 2) {
			Room room = mHouse.getRoomsById(roomId);
			DoorLock door = (DoorLock) room.getDeviceById(String.valueOf(configManager.DOOR_LOCK_1));
			List<KeyDoorLock> keys = door.getKeys();
			int size = keys.size();
			String status = response[1];
			int sizeStatus = status.length();
			int value = 0;
			KeyDoorLock keyIem = null;
			for (int i = 0; i < size && i<sizeStatus; i++) {				
				keyIem = keys.get(i);				
				value = Integer.parseInt(String.valueOf(status.charAt(i)));
				keyIem.setState(value == 1 ? true : false);
			}
			notifyKeyLockObserver();
			return true;
		}
		return false;
	}

	/**
	 * ID= 17 Set Clock (Time)
	 * 
	 * @return
	 */
	public boolean setClock(Bundle bundle) {
		String clock = bundle.getString(configManager.SAVE_CLOCK);

		boolean result = false;
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_CLOCK, "1", "1", clock);
			Log.d(TAG, "setClock-receiveMessage: " + receiveMessage);
			if (receiveMessage == null) {
				continue;
			}
			// receiveMessage = ":skOK";
			String[] response = receiveMessage.split("\\r|\\n|!");
			if (response[0].contains(":skOK")) {
				i = 2;
				result = true;
			}
		}
		return result;
	}

	/**
	 * Sun Aug 4 10:12:20 ICT 2014
	 * 
	 * @return
	 */

	public boolean getClock() {
		boolean isClock = false;

		String receiveMessage = sendAndReceiveMessage(configManager.GET_CLOCK,
				"1", "1", "1");

		// String receiveMessage = "Sun Aug 4  10:12:20 ICT 2014";
		if (receiveMessage == null) {
			return false;
		}
		Log.d(TAG, "getClock- receiveMessage the first: " + receiveMessage);
		receiveMessage = receiveMessage.trim();
		//Log.d(TAG, "getClock- receiveMessage: " + receiveMessage);

		receiveMessage = receiveMessage.replaceAll("\\s+", " ");
		String[] response = receiveMessage.split("\\r|\\n|:| ");

		int month = 1, day = 1, year = 1, hour = 0, min = 0;
		if (response.length >= 7) {
			// receiveMessage = "Sun Aug 14 10:12:20 ICT 2014";
			month = getMonth(response[1]);
			day = Integer.parseInt(response[2]);
			year = Integer.parseInt(response[response.length - 1]);

			hour = Integer.parseInt(response[3]);
			min = Integer.parseInt(response[4]);

			MyDate date = new MyDate(day, month, year);
			mHouse.setDate(date);

			MyTime time = new MyTime(min, hour);
			mHouse.setTime(time);
			isClock = true;
		}

		return isClock;
	}

	private int getMonth(String month) {
		if (month.compareTo("Jan") == 0) {
			return 1;
		} else if (month.compareTo("Feb") == 0) {
			return 2;
		} else if (month.compareTo("Mar") == 0) {
			return 3;
		} else if (month.compareTo("Apr") == 0) {
			return 4;
		} else if (month.compareTo("May") == 0) {
			return 5;
		} else if (month.compareTo("Jun") == 0) {
			return 6;
		} else if (month.compareTo("Jul") == 0) {
			return 7;
		} else if (month.compareTo("Aug") == 0) {
			return 8;
		} else if (month.compareTo("Sep") == 0) {
			return 9;
		} else if (month.compareTo("Oct") == 0) {
			return 10;
		} else if (month.compareTo("Nov") == 0) {
			return 11;
		} else if (month.compareTo("Dec") == 0) {
			return 12;
		}
		return 1;
	}

	/**
	 * ID= 16 Set Timer Off 12- Set Timer Off:
	 * "1215=<roomId>,<sensorId>,<deviceId><active>"; + response: ":toOk" + Set
	 * deviceId off after 10 minute + active: 0,1 + Examples: "1215=2,1,81"
	 * response: ":toOk"
	 * 
	 * @return
	 */
	public boolean setTimerOff(int roomId, String devId, String schedule) {
		String receiveMessage = "";
		for (int i = 0; i < 2; i++) {
			receiveMessage = sendAndReceiveMessage(configManager.SET_TIME_OFF,
					String.valueOf(roomId), devId, schedule);
			if (receiveMessage == null) {
				continue;
			}
			// receiveMessage = ":toOk";
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("t") || response[0].contains("o")
					|| response[0].contains("O") || response[0].contains("k");

			Log.d(TAG, "setTimerOff: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":toOk") || isContain == true)) {
				i = 2;
				return true;

			}
		}
		return false;
	}

	/**
	 * ID= 17 Get Timer Off
	 * 
	 * @return
	 */
	public StatusRelationship getTimerOff(int roomId, String devId,
			StatusRelationship sr) {
		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_TIME_OFF, String.valueOf(roomId), devId, "1");
		if (receiveMessage == null) {
			return sr;
		}
		// receiveMessage = ":gt220021;1111100000";
		Log.d(TAG, "getDeviceRelationship- receiveMessage:" + receiveMessage);
		String[] response = receiveMessage.split(";");
		if (response.length >= 3) {
			String valueResponseAtPosition = response[1];
			if (valueResponseAtPosition.length() == 10) {
				int id = Integer.parseInt(devId);
				int on = Integer.parseInt(String
						.valueOf(valueResponseAtPosition.charAt(id)));
				sr.setSE(on == 1 ? true : false);
			}
		}
		return sr;
	}

	public boolean saveServiceToXML(Bundle bundle) {
		boolean isSaved = XMLHelper.createConfigFileXml(bundle);
		if (isSaved) {
			startSocket();
			return true;
		}
		return false;
	}

	public int getRoomCurrentIndex() {
		return mRoomCurrentIndex;
	}

	public void setRoomCurrentIndex(int roomCurrentIndex) {
		this.mRoomCurrentIndex = roomCurrentIndex;
	}

	public int getRoomIdByIndex(int index) {
		if (mHouse == null) {
			return configManager.NO_INDEX_ROOM;
		}
		if (index < mHouse.getRooms().size()) {
			Room room = mHouse.getRooms().get(index);
			if (room != null) {
				return room.getId();
			}
		}
		return -1;
	}

	public List<AudioHC> createAudio() {
		List<AudioHC> audios = new ArrayList<AudioHC>();
		if (mHouse == null)
			return audios;
		List<Room> rooms = mHouse.getRooms();
		if (rooms == null)
			return audios;
		int size = rooms.size();
		AudioHC audio = null;
		Room room;
		for (int i = 0; i < size; i++) {
			room = rooms.get(i);
			if (room == null)
				continue;
			audio = new AudioHC();
			audio.setRoom(room);
			audios.add(audio);
		}
		return audios;
	}

	public Device[] getDeviceTypes() {
		return mDeviceType;
	}

	private void initDeviceTypes() {
		mDeviceType = new Device[5];
		RegisterService service = RegisterService.getService();
		if (service == null)
			return;
		mDeviceType[0] = new Lamp(service.getResources().getString(
				R.string.lamp));
		mDeviceType[1] = new Fan(service.getResources().getString(R.string.fan));
		mDeviceType[2] = new Fridge(service.getResources().getString(
				R.string.fridge));
		mDeviceType[3] = new PlugDevice(service.getResources().getString(
				R.string.plug_device));
		mDeviceType[4] = new Cock(service.getResources().getString(
				R.string.cock));
	}

	// all infomations are gotten
	public void getInformation() {
		getRoomAddress();
		getadm();
		getLockId();
		getAudio();
	}

	public void getInformationAdmin() {
		getadm();
	}

	public void getInformationRoom() {
		getRoomAddress();
		getLockId();
	}

	public void addKeyLockObserver(GetStatusKeyLockListener observer) {
		mStatuKeyLockListeners.remove(observer);
		mStatuKeyLockListeners.add(observer);
	}

	public void removeKeyLockObserver(GetStatusKeyLockListener observer) {
		mStatuKeyLockListeners.remove(observer);
	}

	public void notifyKeyLockObserver() {
		int size = mStatuKeyLockListeners.size();
		GetStatusKeyLockListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mStatuKeyLockListeners.elementAt(i);
			if (observer != null) {
				observer.recieveKeyLockStatus();
			}
		}
	}

	/**
	 * get Lock Id
	 */
	/**
	 * ID = 1 Get address of rooms
	 */
	public void getLockId() {
		String receiveMessage = sendAndReceiveMessage(
				configManager.GET_LOCK_ID_ACTION, "1", "1", "lck");
		if (receiveMessage == null) {
			return;
		}
		// test
		// receiveMessage =
		// "880081,880082,880083,880084,880085,880086,880087,880088";
		// new
		// String receiveMessage = "80,81,82,83,84,85,86,87,88,89";
		Log.d(TAG, "getLockId - receiveMessage:" + receiveMessage);
		String[] response = receiveMessage.split("\\r|\\n|,");
		String[] lockIDs = mHouse.getLockIds();
		int sizeLock = lockIDs.length;
		int size = response.length;

		Log.d(TAG, "getLockId - size:" + size + " , size lock: " + sizeLock);
		for (int i = 0; i < size && i < sizeLock; i++) {

			lockIDs[i] = response[i].trim();
		}

		// id = 0 or id = 9 is GSM and other

		List<Room> rooms = mHouse.getRooms();
		int sizeRoom = rooms.size();
		Room room = null;
		for (int i = 0; i < sizeRoom && i < size; i++) {
			room = rooms.get(i);
			room.setLockId(lockIDs[i + 1]);
			saveRoom(room);
		}

		/*
		 * List<Room> rooms = mHouse.getRooms(); int sizeRoom = rooms.size();
		 * Room room = null; for(int i = 0; i< sizeRoom && i < size; i++){ room
		 * = rooms.get(i); room.setId(i); saveRoom(room); }
		 */

	}

	public boolean setLockId(String status) {
		boolean isSet = false;
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_LOCK_ID_ACTION, "1", "1", status);
			Log.d(TAG, "setLockId- receiveMessage: " + receiveMessage);
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n|!");

			if (response[0].contains(":sttOK")) {
				i = 2;
				isSet = true;
				// Log.d(TAG, "saveConfig:: saved devices");
			}
		}
		return isSet;
	}

	/**
	 * 2014=8,08123456 :spOK
	 * 
	 * @param bundle
	 * @return
	 */
	public boolean setPasswordKey(Bundle bundle) {
		int id = bundle.getInt(configManager.DEVICE_ID);
		String devId = configManager.convertIdToString(id);
		int roomId = bundle.getInt(configManager.ROOM_ID);
		String status = bundle.getString(configManager.PASSWORD_KEY);
		String name = bundle.getString(configManager.NAME);
		if(TextUtils.isEmpty(name) == false){			
		}
		for (int i = 0; i < 2; i++) {
			String receiveMessage = sendAndReceiveMessage(
					configManager.SET_LOCK_PASSWORD, String.valueOf(roomId),
					devId, status);
			// receiveMessage = ":spOk";
			if (receiveMessage == null) {
				continue;
			}
			String[] response = receiveMessage.split("\\r|\\n");
			boolean isContain = response[0].contains(":")
					|| response[0].contains("s") || response[0].contains("p")
					|| response[0].contains("O") || response[0].contains("k");
			Log.d(TAG, "setPasswordKey - roomId:: " + roomId + " , devId: "
					+ devId + ", receiveMessage" + receiveMessage
					+ ", response: "
					+ (response != null ? response[0] : "response")
					+ " , match:" + isContain);

			if (response != null
					&& (response[0].equals(":slOk") || isContain == true)) {
				i = 2;
				return true;

			}
		}
		return false;
	}

	public void addConfigObserver(ConfigListener observer) {
		mConfigListeners.remove(observer);
		mConfigListeners.add(observer);
	}

	public void removeConfigObserver(ConfigListener observer) {
		mConfigListeners.remove(observer);
	}

	public void notifyConfigObserver(Boolean result) {
		int size = mConfigListeners.size();
		ConfigListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mConfigListeners.elementAt(i);
			if (observer != null) {
				observer.configSaved(result);
			}
		}
	}

	public void addClockObserver(ClockListener observer) {
		mClockListeners.remove(observer);
		mClockListeners.add(observer);
	}

	public void removeClockObserver(ClockListener observer) {
		mClockListeners.remove(observer);
	}

	public void notifyClockObserver(Boolean result) {
		int size = mClockListeners.size();
		ClockListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mClockListeners.elementAt(i);
			if (observer != null) {
				observer.clockGot(result);
			}
		}
	}

	public void addAudioObserver(AudioListener observer) {
		mAudioListeners.remove(observer);
		mAudioListeners.add(observer);
	}

	public void removeAudioObserver(AudioListener observer) {
		mAudioListeners.remove(observer);
	}
	
	public void notifyAudioObserver(int[] result) {
		int size = mAudioListeners.size();
		AudioListener observer = null;
		for (int i = 0; i < size; i++) {
			observer = mAudioListeners.elementAt(i);
			if (observer != null) {
				observer.audioGot(result);
			}
		}
	}

	@Override
	public void handleMessage(Message msg) {
		Log.e(TAG, "handleMessage:: " + msg.what);
		boolean mResult = false;
		switch (msg.what) {
		case HCRequest.REQUEST_LOGIN_ACTION:
			mResult = login(msg.getData());
			notifyLoginListener(mResult);
			break;
		case HCRequest.REQUEST_SET_ROOM_ADDRESS: // 3
			mResult = setRoomInfomation(msg.getData());
			notifyConfigObserver(mResult);
			break;

		case HCRequest.REQUEST_SET_CONFIG: // 19
			// should test
			mResult = setAdminConfig(msg.getData());
			notifyConfigObserver(mResult);
			break;
		case HCRequest.REQUEST_SET_ADMIN_ADDRESS: // 4
			// can delete
			/*
			 * service.getUIEngine().sendMessageTest(
			 * configManager.SET_ADMIN_ADDRESS, "");
			 */
			break;
		case HCRequest.REQUEST_GET_ROOM_STATUS: // 5
			/*
			 * Log.d(TAG, "get device status"); getRoomStatus(msg.getData());
			 */
			break;
		case HCRequest.REQUEST_SET_DEVICE_STATUS: // 6
			mResult = setOnOffDevice(msg.getData());
			// to do

			break;
		case HCRequest.REQUEST_LEARN_INFRARED: // 7 Checked
			Log.d(TAG, "update remote");
			mResult = updateRemote(msg.getData());
			break;
		case HCRequest.REQUEST_TRANSMIT_INFRARED: // 8
			Log.d(TAG, "control remote");
			mResult = controlRemote(msg.getData());
			break;
		case HCRequest.REQUEST_GET_DEVICE_STATUS_RELATIONSHIP: // 9
			Log.d(TAG, "get device relationship");
			getDeviceStatusRelationship(msg.getData());
			break;
		case HCRequest.REQUEST_SET_DEVICE_STATUS_RELATIONSHIP: // 10
			mResult = setDeviceRelationship(msg.getData());
			break;
		case HCRequest.REQUEST_GET_AUDIO: // 11
			mResult = getAudio();
			break;
		case HCRequest.REQUEST_SET_AUDIO: // 12
			mResult = setAudio(msg.getData());

			break;
		case HCRequest.REQUEST_SET_PASSWORD_KEY: // 14
			mResult = setPasswordKey(msg.getData());
			break;
		case HCRequest.REQUEST_SET_TIME_OFF: // 15

			break;
		case HCRequest.REQUEST_GET_ALL_ROOM_STATUS: // 16
			getAllRoomsStatus();
			notifyStatusObserver();
			break;

		case HCRequest.REQUEST_SET_DEVICE_ADDRESS: // 3
			Log.d(TAG, "REQUEST_SET_DEVICE_ADDRESS");
			mResult = setAdminConfig(msg.getData());

			break;

		case HCRequest.REQUEST_CHANGE_SERVICE_ADDRESS:
			mResult = saveServiceToXML(msg.getData());
			break;

		case HCRequest.REQUEST_GET_LOCK_STATUS: // 20
			Log.d(TAG, "get device relationship");
			getLockDoor(msg.getData());
			break;
		case HCRequest.REQUEST_SET_LOCK_STATUS: // 21
			Log.d(TAG, "set device relationship");
			mResult = setLockDoor(msg.getData());
			break;
		case HCRequest.REQUEST_SET_TIME_SERVER: // 22
			Log.d(TAG, "set time server");
			mResult = setClock(msg.getData());
			if (mResult) {
				mResult = getClock();
				notifyClockObserver(mResult);
			}
			break;
		case HCRequest.REQUEST_GET_TIME_SERVER: // 23
			Log.d(TAG, "get time server");
			mResult = getClock();
			notifyClockObserver(mResult);
			break;
		default:
			break;
		}
	}

	public List<Room> getAllRoom() {
		List<Room> allRooms = new ArrayList<Room>();

		List<Room> rooms = mHouse.getRooms();
		int size = rooms.size();
		for (int i = 0; i < size; i++) {
			allRooms.add(rooms.get(i));
		}

		List<Room> otherRooms = mHouse.getOtherRooms();
		int otherSize = otherRooms.size();
		for (int i = 0; i < otherSize; i++) {
			allRooms.add(otherRooms.get(i));
		}

		return allRooms;
	}

	public Boolean isStartedTimerDevice = false;

	class MyTask extends TimerTask {
		// times member represent calling times.
		public void run() {
			int id = getRoomIdByIndex(mRoomCurrentIndex);
			if (id != configManager.NO_INDEX_ROOM) {
				getRoomStatus(id);
			}
		}
	}

	public void startTimerDevice() {
		Log.i(TAG, "startTimer");
		if (msgTimerDevice == null) {
			msgTimerDevice = new Timer();
		}
		if (mTaskTimerDevice == null) {
			mTaskTimerDevice = new MyTask();
		}
		if (isStartedTimerDevice == false) {

			msgTimerDevice.schedule(mTaskTimerDevice, 1,
					configManager.MAX_TIME_REQUEST_AGAIN);
			isStartedTimerDevice = true;
		}
	}

	public void stopTimerDevice() {
		Log.i(TAG, "stopTimer");
		if (msgTimerDevice != null && isStartedTimerDevice == true) {
			msgTimerDevice.cancel();
			msgTimerDevice.purge();
			msgTimerDevice = null;
			mTaskTimerDevice.cancel();
			mTaskTimerDevice = null;
			isStartedTimerDevice = false;
		}
	}

	class MyTaskAudio extends TimerTask {
		// times member represent calling times.
		public void run() {
			int id = getRoomIdByIndex(mRoomCurrentIndex);
			if (id != configManager.NO_INDEX_ROOM) {
				getAudio();
			}
		}
	}

	Timer msgTimerAudio;
	MyTaskAudio mTaskTimerAudio;
	public Boolean isStartedTimerAudio = false;

	public void startTimerAudio() {
		Log.i(TAG, "startTimerAudio");
		if (msgTimerAudio == null) {
			msgTimerAudio = new Timer();
		}
		if (mTaskTimerAudio == null) {
			mTaskTimerAudio = new MyTaskAudio();
		}
		if (isStartedTimerAudio == false) {
			msgTimerAudio.schedule(mTaskTimerAudio, 1, 8000);
			isStartedTimerAudio = true;
		}
	}

	public void stopTimerAuido() {
		Log.i(TAG, "stopTimer");
		if (msgTimerAudio != null && isStartedTimerAudio == true) {
			msgTimerAudio.cancel();
			msgTimerAudio.purge();
			msgTimerAudio = null;
			mTaskTimerAudio.cancel();
			mTaskTimerAudio = null;
			isStartedTimerAudio = false;
		}
	}

}
