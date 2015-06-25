package com.HomeCenter2.house;

import com.HomeCenter2.R;

public class KeyDoorLock {
	private int id;
	private String idKey;
	private String nameKey;
	private boolean state;
	private int icon;
	
	public KeyDoorLock() {
		this.id = 0;
		this.idKey = "";
		this.nameKey = "Key";
		setState(false);
	}

	public KeyDoorLock(int id, String idKey, String nameKey, Boolean state) {
		this.id = id;
		this.idKey = idKey;
		this.nameKey = nameKey;
		setState(state);		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
		if(this.state == true){
			setOnIcon();
		}else{
			setOffIcon();
		}
	}

	public String getIdKey() {
		return idKey;
	}

	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public void setOnIcon() {
		setIcon(R.drawable.key_doorlock_on);
		
	}
	
	public void setOffIcon() {
		setIcon(R.drawable.key_doorlock_off);
		
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}