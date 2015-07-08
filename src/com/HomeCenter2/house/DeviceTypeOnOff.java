package com.HomeCenter2.house;

public class DeviceTypeOnOff {
	private int id;
	private String name;
	private int iconOn;
	private int iconOff;

	public DeviceTypeOnOff(int id, String name, int iconOn, int iconOff) {
		setId(id);
		setName(name);
		setIconOn(iconOn);
		setIconOff(iconOff);
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

	public int getIconOn() {
		return iconOn;
	}

	public void setIconOn(int iconOn) {
		this.iconOn = iconOn;
	}

	public int getIconOff() {
		return iconOff;
	}

	public void setIconOff(int iconOff) {
		this.iconOff = iconOff;
	}

}
