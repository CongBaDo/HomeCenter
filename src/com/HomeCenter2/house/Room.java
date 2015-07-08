package com.HomeCenter2.house;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class Room implements Serializable {
	private int id;
	private String lockId;
	private String idSever;
	private String name;
	private int areaId;
	private List<Control> controls;
	private List<Sensor> sensors;
	private boolean active = false;
	private boolean activeTemp = false;
	private int energy = 0;
	private int state;
	private int icon;
	private boolean otherType;

	public Room() {
		id = -1;
		idSever = "";
		name = "";
		areaId = -1;
		setState(configManager.ROOM_OFF_ALL);
		controls = new ArrayList<Control>();
		sensors = new ArrayList<Sensor>();
		active = false;
		setLockId("");
		setState(configManager.ROOM_OFF_ALL);
		setOtherType(false);
	}

	public boolean isOtherType() {
		return otherType;
	}

	public void setOtherType(boolean otherType) {
		this.otherType = otherType;
	}

	public Room(int id, String idServer, String name, int floorId, int picture,
			boolean isOtherType) {
		this.id = id;
		this.idSever = idServer;
		this.name = name;
		this.areaId = floorId;
		setState(configManager.ROOM_OFF_ALL);
		controls = new ArrayList<Control>();
		sensors = new ArrayList<Sensor>();
		active = false;
		setLockId("");
		setState(configManager.ROOM_OFF_ALL);
		setOtherType(isOtherType);
	}

	public Room(int id, String idServer, String name, int floorId, int picture) {
		this.id = id;
		this.idSever = idServer;
		this.name = name;
		this.areaId = floorId;
		setState(configManager.ROOM_OFF_ALL);
		controls = new ArrayList<Control>();
		sensors = new ArrayList<Sensor>();
		active = false;
		setLockId("");
		setState(configManager.ROOM_OFF_ALL);
		setOtherType(false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addDevice(Device device) {
		if (device instanceof Control) {
			if (controls != null && controls != null) {
				device.setRoomId(this.id);
				controls.add((Control)device);
			}
		} else {
			if (sensors != null && sensors != null) {
				device.setRoomId(this.id);
				sensors.add((Sensor) device);
			}
		}
	}

	public Element addRoomToXML(Document document) {
		Element childElement = document.createElement(configManager.ROOM);
		
		Element em;

		em = document.createElement(configManager.ID);
		em.appendChild(document.createTextNode(String.valueOf(getId())));
		childElement.appendChild(em);

		em = document.createElement(configManager.ID_SERVER);
		String str = getIdSever();
		if (TextUtils.isEmpty(str)) {
			str = "";
		}
		em.appendChild(document.createTextNode(str));
		childElement.appendChild(em);

		em = document.createElement(configManager.NAME);
		em.appendChild(document.createTextNode(getName()));
		childElement.appendChild(em);

		em = document.createElement(configManager.ACTIVE);
		if (isActive()) {
			em.appendChild(document.createTextNode("1"));
		} else {
			em.appendChild(document.createTextNode("0"));
		}

		em = document.createElement(configManager.IS_OTHER_TYPE);
		if (isOtherType()) {
			em.appendChild(document.createTextNode("1"));
		} else {
			em.appendChild(document.createTextNode("0"));
		}

		childElement.appendChild(em);

		return childElement;

	}

	public static final String TAG = "TMT Room";

	public void readRoomFromXML(NodeList lst, List<Object> objects) {

		int sizeChild = lst.getLength();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		String[] lockIds = null;
		if (uiEngine.getHouse() != null) {
			lockIds = uiEngine.getHouse().getLockIds();
		}
		int size = 0;
		if (lockIds != null) {
			size = lockIds.length;
		}

		for (int j = 0; j < sizeChild; j++) {
			Node fsNode = lst.item(j);
			if (fsNode != null) {
				String name = fsNode.getNodeName();
				String value;
				if (name.equals(configManager.ID)) {
					value = fsNode.getFirstChild().getNodeValue();
					this.setId(Integer.valueOf(value));
				} else if (name.equals(configManager.NAME)) {
					value = fsNode.getFirstChild().getNodeValue();
					this.setName(value);
				} else if (name.equals(configManager.DEVICE)) {
					NodeList lstChild = fsNode.getChildNodes();
					if (lstChild != null) {
						getRooms(lstChild, objects);
					}
				} else if (name.equals(configManager.ACTIVE)) {
					value = fsNode.getFirstChild().getNodeValue();
					if (Integer.valueOf(value) == 0) {
						setActive(false);
					} else {
						setActive(true);
					}
				} else if (name.equals(configManager.ID_SERVER)) {
					// child
					Node node = fsNode.getFirstChild();
					if (node != null) {
						value = fsNode.getFirstChild().getNodeValue();
						setIdSever(value);
					}
				}
				if (size > 0 && size > j && lockIds[j] != null) {
					setLockId(lockIds[j]);
				}
			}
		}
		/*
		 * Log.d(TAG, "readRoomFromXML::" + "id :" + this.id + " , name:" +
		 * this.name + " , active: " + this.isActive() + ", device:" +
		 * this.devices.size());
		 */
	}

	private void getRooms(NodeList lst, List<Object> objects) {
		Node fsNode = lst.item(0);
		if (fsNode != null) {
			String name = fsNode.getNodeName().toString();
			String value = fsNode.getFirstChild().getNodeValue();
			if (TextUtils.isEmpty(value))
				return;
			if (name.equals(configManager.TYPE)) {
				int type = Integer.parseInt(value);
				Device device = null;
				boolean isControl = false;
				switch (type) {
				case configManager.ON_OFF:
					device = new LampRoot();
					isControl = true;
					break;
				case configManager.LIGHT:
					device = new Light();
					break;
				case configManager.TEMPERATURE:
					device = new Temperature();
					break;
				case configManager.MOTION:
					device = new Motion();
					break;
				case configManager.DOOR_STATUS:
					device = new DoorStatus();
					break;
				case configManager.DOOR_LOCK:
					device = new DoorLock();
					isControl = true;
					break;
				case configManager.ROLLER_SHUTTER:
					device = new RollerShutter();
					isControl = true;
					break;
				case configManager.SMOKE:
					device = new Smoke();
					break;
				case configManager.CAMERA:
					device = new Camera();
					isControl = true;
					break;
				default:
					break;
				}
				if (device != null) {
					objects.add(device);
					if (isControl) {
						this.getControls().add((Control) device);
					} else {
						this.getSensors().add((Sensor) device);
					}
					device.setRoomId(this.id);
					device.readDeviceFromXML(lst);
				}
			}
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void addAllObjectInHome(List<Object> objects) {
		// controls
		int size = controls.size();
		for (int i = 0; i < size; i++) {
			objects.add(controls.get(i));
		}

		// sensors
		size = sensors.size();
		for (int i = 0; i < size; i++) {
			objects.add(sensors.get(i));
		}

	}

	public boolean isActiveTemp() {
		return activeTemp;
	}

	public void setActiveTemp(boolean activeTemp) {
		this.activeTemp = activeTemp;
	}

	public void refreshActiveTempByActiveValue() {
		this.activeTemp = this.active;
	}

	public void refreshActiveByActiveTempValue() {
		this.active = this.activeTemp;
	}

	private void refreshDevicesByActive() {

	}

	public void refreshDevicesInRoom(boolean isActive) {
		if (isActive) {
			int size = controls.size();
			Device device = null;
			for (int i = 0; i < size; i++) {
				device = controls.get(i);
				device.refreshActiveTempByActiveValue();
			}

			size = sensors.size();
			for (int i = 0; i < size; i++) {
				device = sensors.get(i);
				device.refreshActiveTempByActiveValue();
			}
		} else {
			int size = controls.size();
			Device device = null;
			for (int i = 0; i < size; i++) {
				device = controls.get(i);
				device.refreshActiveByActiveTempValue();
			}

			size = sensors.size();
			for (int i = 0; i < size; i++) {
				device = sensors.get(i);
				device.refreshActiveByActiveTempValue();
			}
		}

	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void addCamera() {
		switch (id) {
		case 1:
			controls.add(new Camera(1, "camera " + id + "1", true));
			controls.add(new Camera(1, "camera " + id + "2", true));
			controls.add(new Camera(1, "camera " + id + "3", true));
			controls.add(new Camera(1, "camera " + id + "4", true));
			break;
		case 2:
			controls.add(new Camera(1, "camera " + id + "5", true));
			controls.add(new Camera(1, "camera " + id + "6", true));
			controls.add(new Camera(1, "camera " + id + "7", true));
			break;
		case 3:
			controls.add(new Camera(1, "camera " + id + "8", true));
			controls.add(new Camera(1, "camera " + id + "9", true));
			break;
		case 4:
			controls.add(new Camera(1, "camera " + id + "10", true));
			controls.add(new Camera(1, "camera " + id + "11", true));
			break;
		default:
			controls.add(new Camera(1, "camera " + id + "13", true));
			controls.add(new Camera(1, "camera " + id + "14", true));
			controls.add(new Camera(1, "camera " + id + "15", true));
			controls.add(new Camera(1, "camera " + id + "16", true));
			break;
		}
	}

	public String getIdSever() {
		return idSever;
	}

	public void setIdSever(String idSever) {
		this.idSever = idSever;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		switch (state) {
		case configManager.ROOM_OFF_ALL:
			icon = R.drawable.ic_room_orange;
			break;
		case configManager.ROOM_ON_ALL:
			icon = R.drawable.ic_room_orange;
			break;
		case configManager.ROOM_ON_OFF:
			icon = R.drawable.ic_room_orange;
			break;
		}
		this.state = state;
	}

	public void putStateControl() {
		int size = controls.size();
		Device item = null;
		int isState = configManager.ROOM_OFF_ALL;
		int countOn = 0;
		int count = 0;
		for (int i = 0; i < size; i++) {
			item = controls.get(i);
			if (item instanceof LampRoot) {
				count++;
				if (((LampRoot) item).isState()) {
					isState = configManager.ROOM_ON_OFF;
					countOn++;
				}
			} else if (item instanceof RollerShutter) {
				count++;
				if (((RollerShutter) item).getRoller() > 0) {
					isState = configManager.ROOM_ON_OFF;
					countOn++;
				}
			}
		}
		if (isState == configManager.ROOM_ON_OFF && count == countOn) {
			isState = configManager.ROOM_ON_ALL;
		}
		setState(isState);
	}

	public int getIcon() {
		return icon;
	}

	public Device getControlById(String id) {
		if (controls == null) {
			return null;
		}
		int size = controls.size();
		for (int i = 0; i < size; i++) {
			Device device = controls.get(i);
			if (id.compareTo(String.valueOf(device.getId())) == 0) {
				return device;
			}
		}
		return null;
	}

	public DoorLock getDoorLock() {
		if (controls == null) {
			return null;
		}
		int size = controls.size();
		for (int i = 0; i < size; i++) {
			Device device = controls.get(i);
			if (device instanceof DoorLock) {
				return (DoorLock) device;
			}
		}
		return null;
	}

	public String getLockId() {
		return lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	public List<Control> getControls() {
		return controls;
	}

	public void setControls(List<Control> controls) {
		this.controls = controls;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

}
