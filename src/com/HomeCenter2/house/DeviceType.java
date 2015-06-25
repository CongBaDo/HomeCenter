package com.HomeCenter2.house;

import com.HomeCenter2.data.configManager;

public class DeviceType {
	private int type;
	private String name;
	
	public DeviceType(){
		type = configManager.NORMAL;
		setName("");
	}
	
	public DeviceType(int type, String name){
		this.type = type;
		this.setName(name);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
