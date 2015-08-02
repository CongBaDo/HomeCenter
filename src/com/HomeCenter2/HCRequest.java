package com.HomeCenter2;

public class HCRequest {
	public static final int REQUEST_NONE = -1;
	public static final int REQUEST_LOGIN_ACTION = 0;
	public static final int REQUEST_GET_DEVICE_ACTION = REQUEST_LOGIN_ACTION+1;
	public static final int REQUEST_GET_ADMIN_ACTION = REQUEST_LOGIN_ACTION+ 2;
	public static final int REQUEST_SET_ROOM_ADDRESS = REQUEST_LOGIN_ACTION + 3;
		
	// address
	public static final int REQUEST_SET_ADMIN_ADDRESS = REQUEST_LOGIN_ACTION + 4;
							
	public static final int REQUEST_GET_ROOM_STATUS = REQUEST_LOGIN_ACTION + 5;
	public static final int REQUEST_SET_DEVICE_STATUS = REQUEST_LOGIN_ACTION + 6;
	
	public static final int REQUEST_GET_DEVICE_STATUS_RELATIONSHIP = REQUEST_LOGIN_ACTION + 7;
	public static final int REQUEST_SET_DEVICE_STATUS_RELATIONSHIP = REQUEST_LOGIN_ACTION + 8;
	
	public static final int REQUEST_LEARN_INFRARED = REQUEST_LOGIN_ACTION + 9;
	public static final int REQUEST_TRANSMIT_INFRARED = REQUEST_LOGIN_ACTION + 10;
	
	public static final int REQUEST_GET_AUDIO = REQUEST_LOGIN_ACTION + 11;
	public static final int REQUEST_SET_AUDIO = REQUEST_LOGIN_ACTION + 12;
	
	public static final int REQUEST_SET_PASSWORD_KEY = REQUEST_LOGIN_ACTION + 14;
	
	public static final int REQUEST_SET_TIME_OFF = REQUEST_LOGIN_ACTION + 15;
	
	public static final int REQUEST_GET_ALL_ROOM_STATUS = REQUEST_LOGIN_ACTION + 16;
	public static final int REQUEST_SET_DEVICE_ADDRESS = REQUEST_LOGIN_ACTION + 17;
	
	public static final int REQUEST_CHANGE_SERVICE_ADDRESS = REQUEST_LOGIN_ACTION + 18;
	
	public static final int REQUEST_SET_CONFIG = REQUEST_LOGIN_ACTION + 19;
	
	public static final int REQUEST_GET_LOCK_STATUS = REQUEST_LOGIN_ACTION + 20;	
	public static final int REQUEST_SET_LOCK_STATUS = REQUEST_LOGIN_ACTION + 21;
	
	public static final int REQUEST_SET_TIME_SERVER = REQUEST_LOGIN_ACTION + 22;
	public static final int REQUEST_GET_TIME_SERVER = REQUEST_LOGIN_ACTION + 23;
	
	protected static final int HC_REQUEST_SUCCESS = 0;
	protected static final int HC_REQUEST_FAIL = HC_REQUEST_SUCCESS + 1;
	
	protected int requestType = REQUEST_NONE;

	public void setRequestType(int rt) {
		requestType = rt;
	}

	public int getRequestType() {
		return requestType;
	}
}
