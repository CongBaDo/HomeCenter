package com.HomeCenter2.house;

import com.HomeCenter2.R;

public class AudioHC {
	private Room room;
	private int input;
	private Boolean state;
	int icon;

	public AudioHC() {
		room = null;
		input = 0;
		setState(false);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getInput() {
		return input;
	}

	public void setInput(int input) {
		this.input = input;
	}

	public Boolean isState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
		if (this.state == true) {
			setOnIcon();
		} else {
			setOffIcon();
		}
	}

	public int getIcon() {
		return icon;
	}

	public void setOnIcon() {
		icon = R.drawable.icon_audio_normal;

	}

	public void setOffIcon() {
		icon = R.drawable.icon_audio_normal;

	}
}
