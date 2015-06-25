package com.HomeCenter2.house;

import java.util.ArrayList;
import java.util.List;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.MyTime;
import com.HomeCenter2.data.Password;
import com.HomeCenter2.data.configManager;

import android.R.integer;
import android.text.format.Time;
import android.util.Log;

public class House {

	private List<Area> areas;
	private List<Room> rooms;
	


	public List<Room> getOtherRooms() {
		List<Room> others = new ArrayList<Room>();
		if(rooms == null)
			return others;
		int size = rooms.size();
		
		for(int i = configManager.MAX_ROOM_IN_AREA ; i< configManager.MAX_ROOM_GSM_IN_AREA && i< size; i++){
			others.add(rooms.get(i));
		}
		return others;
	}

	private List<Device> devices;

	private String[] deviceIDs = new String[configManager.MAX_ROOM_GSM_IN_AREA];
	private String[] lockIds = new String[configManager.MAX_ROOM_GSM_IN_AREA];

	private Password password;
	private String ipAddress;
	private int port;
	private int lightLevelTrigger;
	private int smokeLevelTrigger;
	private int temperatureTrigger;
	private String phone01;
	private String phone02;
	private String phone03;
	private String gsm;

	private MyTime time;
	private MyDate date;
	
	public static final String TAG = "TMT House";

	public House() {
		areas = new ArrayList<Area>();
		rooms = new ArrayList<Room>();
		devices = new ArrayList<Device>();
		
		setPassword(new Password());
		ipAddress = "";
		lightLevelTrigger = 0;
		smokeLevelTrigger = 0;
		temperatureTrigger = 0;
		phone01 = "";
		phone02 = "";
		phone03 = "";
		gsm = "";
		port = -1;

		Time now = new Time();
		now.setToNow();

		time = new MyTime(now.minute, now.hour);
		date = new MyDate(now.monthDay, now.month + 1, now.year);
	}

	public House(List<Object> objects) {
		setPassword(new Password());
		ipAddress = "";
		lightLevelTrigger = 0;
		smokeLevelTrigger = 0;
		temperatureTrigger = 0;
		phone01 = "";
		phone02 = "";
		phone03 = "";
		gsm = "";
		port = -1;
		Log.d(TAG, "House init function");
		setHouse(objects);
		
		Time now = new Time();
		now.setToNow();

		time = new MyTime(now.minute, now.hour);
		date = new MyDate(now.monthDay, now.month + 1, now.year);
	}

	public void setHouse(List<Object> objects) {		
		areas = new ArrayList<Area>();
		rooms = new ArrayList<Room>();
		devices = new ArrayList<Device>();

		int size = objects.size();
		Log.d(TAG, "setHouse: size" + size);
		Object object = null;
		for (int i = 0; i < size; i++) {
			object = objects.get(i);
			if (object instanceof Area) {
				Area area = (Area) object;
				areas.add(area);
				List<Room> lsroom = area.getRooms();
				int sizeroom = lsroom.size();
				for (int j = 0; j < sizeroom; j++) {
					Room roomArea = lsroom.get(j);
					List<Device> devices = roomArea.getDevices();
					int sizedevice = devices.size();
					for (int k = 0; k < sizedevice; k++) {
						Device device = devices.get(k);
					}
				}

			} else if (object instanceof Room) {
				Room room = (Room) object;
				Log.d(TAG, "setHouse: addRoom" + room.getName());
				rooms.add(room);
			} else if (object instanceof Device) {
				Device device = (Device) object;
				devices.add(device);
			}
		}
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if(uiEngine!= null){
			uiEngine.notifyXMLObserver();
		}
		/*
		 * //test for(int i = 0; i< areas.size(); i++){ for(int j= 0; j<
		 * rooms.size(); j++) { Room room = rooms.get(j); room.addCamera(); } }
		 */
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<Room> getRooms() {
		List<Room> others = new ArrayList<Room>();
		if(rooms == null)
			return others;
		int size = rooms.size();
		for(int i = 0 ; i< configManager.MAX_ROOM_IN_AREA && i< size; i++){
			others.add(rooms.get(i));
		}
		return others;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public int getLightLevelTrigger() {
		return lightLevelTrigger;
	}

	public void setLightLevelTrigger(int lightLevelTrigger) {
		this.lightLevelTrigger = lightLevelTrigger;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getSmokeLevelTrigger() {
		return smokeLevelTrigger;
	}

	public void setSmokeLevelTrigger(int smokeLevelTrigger) {
		this.smokeLevelTrigger = smokeLevelTrigger;
	}

	public int getTemperatureTrigger() {
		return temperatureTrigger;
	}

	public void setTemperatureTrigger(int temperatureTrigger) {
		this.temperatureTrigger = temperatureTrigger;
	}

	public String getPhone01() {
		return phone01;
	}

	public void setPhone01(String phone01) {
		this.phone01 = phone01;
	}

	public String getPhone02() {
		return phone02;
	}

	public void setPhone02(String phone02) {
		this.phone02 = phone02;
	}

	public String getPhone03() {
		return phone03;
	}

	public void setPhone03(String phone03) {
		this.phone03 = phone03;
	}

	public String getGsm() {
		return gsm;
	}

	public void setGsm(String gsm) {
		this.gsm = gsm;
	}

	public String[] getDeviceID() {
		return deviceIDs;
	}

	public void setDeviceID(String[] deviceID) {
		this.deviceIDs = deviceID;
	}

	public Room getRoomsById(int id) {
		int size = rooms.size();
		Room room;
		for (int i = 0; i < size; i++) {
			room = rooms.get(i);
			if (room.getId() == id) {
				return room;
			}
		}
		return null;
	}
	
	public int getPositionRoomById(int id) {
		int size = rooms.size();
		Room room;
		for (int i = 0; i < size; i++) {
			room = rooms.get(i);
			if (room.getId() == id) {
				return i;
			}
		}
		return 0;
	}

	public List<Object> getAllObjectInHome() {
		List<Object> objects = new ArrayList<Object>();
		int size = areas.size();
		Area area = null;
		for (int i = 0; i < size; i++) {
			area = areas.get(i);
			objects.add(area);
			area.addAllObjectInHome(objects);
		}
		return objects;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MyTime getTime() {
		return time;
	}

	public void setTime(MyTime time) {
		this.time = time;
	}

	public MyDate getDate() {
		return date;
	}

	public void setDate(MyDate date) {
		this.date = date;
	}

	public String[] getLockIds() {
		return lockIds;
	}

	public void setLockIds(String[] lockIds) {
		this.lockIds = lockIds;
	}

}
