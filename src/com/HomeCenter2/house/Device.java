package com.HomeCenter2.house;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.HomeCenter2.data.configManager;

public abstract class Device implements Serializable {

	public static final String TAG = "Device";
	private int id;
	private String deviceId;
	private String name;
	private int roomId;
	private boolean active;
	private boolean activeTemp = false;
	private List<Schedule> schedules;
	protected int icon;

	public Device() {
		id = 0;
		deviceId = "0";
		setName("");
		roomId = 0;
		setActive(false);
		schedules = new ArrayList<Schedule>();
	}

	public Device(int id, String name) {
		setId(id);
		setName(name);
		setActive(false);
		deviceId = "0";
		roomId = 0;
		schedules = new ArrayList<Schedule>();
	}

	public Device(int id, String name, int room) {
		setId(id);
		setName(name);
		setRoomId(room);
		setActive(false);
		deviceId = "0";
		schedules = new ArrayList<Schedule>();
	}

	public void initSchedules() {
		for (int i = 0; i < configManager.MAX_SCHEDULE; i++) {
			schedules.add(new Schedule());
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	abstract public void addDeviceToXML(Document document, Element rootElement);

	abstract public void readDeviceFromXML(NodeList lst);

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActiveTemp() {
		return activeTemp;
	}

	public void setActiveTemp(boolean activeTemp) {
		this.activeTemp = activeTemp;
	}

	public void refreshActiveTemp() {
		this.activeTemp = this.active;
	}

	public void refreshActiveTempByActiveValue() {
		this.activeTemp = this.active;
	}

	public void refreshActiveByActiveTempValue() {
		this.active = this.activeTemp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getIcon() {
		return icon;
	}

	public abstract void setOnIcon();

	public abstract void setOffIcon();

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		if (this.schedules == null) {
			this.schedules.clear();
			this.schedules = null;
		}

		this.schedules = schedules;
	}
}
