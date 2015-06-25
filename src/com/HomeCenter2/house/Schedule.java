package com.HomeCenter2.house;

import java.util.Date;

import android.text.format.Time;

public class Schedule {
	public boolean isSheduleOn() {
		return isSheduleOn;
	}
	public void setSheduleOn(boolean isSheduleOn) {
		this.isSheduleOn = isSheduleOn;
	}
	public Time getTimeOn() {
		return timeOn;
	}
	public void setTimeOn(Time timeOn) {
		this.timeOn = timeOn;
	}
	public boolean isSheduleOff() {
		return isSheduleOff;
	}
	public void setSheduleOff(boolean isSheduleOff) {
		this.isSheduleOff = isSheduleOff;
	}
	public Time getTimeOff() {
		return timeOff;
	}
	public void setTimeOff(Time timeOff) {
		this.timeOff = timeOff;
	}
	public boolean isMonRepeat() {
		return isMonRepeat;
	}
	public void setMonRepeat(boolean isMonRepeat) {
		this.isMonRepeat = isMonRepeat;
	}
	public boolean isTueRepeat() {
		return isTueRepeat;
	}
	public void setTueRepeat(boolean isTueRepeat) {
		this.isTueRepeat = isTueRepeat;
	}
	public boolean isWebRepeat() {
		return isWebRepeat;
	}
	public void setWebRepeat(boolean isWebRepeat) {
		this.isWebRepeat = isWebRepeat;
	}
	public boolean isThuRepeat() {
		return isThuRepeat;
	}
	public void setThuRepeat(boolean isThuRepeat) {
		this.isThuRepeat = isThuRepeat;
	}
	public boolean isFriRepeat() {
		return isFriRepeat;
	}
	public void setFriRepeat(boolean isFriRepeat) {
		this.isFriRepeat = isFriRepeat;
	}
	public boolean isSatRepeat() {
		return isSatRepeat;
	}
	public void setSatRepeat(boolean isSatRepeat) {
		this.isSatRepeat = isSatRepeat;
	}
	public boolean isSunRepeat() {
		return isSunRepeat;
	}
	public void setSunRepeat(boolean isSunRepeat) {
		this.isSunRepeat = isSunRepeat;
	}
	private boolean isSheduleOn;
	private Time timeOn;

	private boolean isSheduleOff;
	private Time timeOff;

	private boolean isMonRepeat;
	private boolean isTueRepeat;
	private boolean isWebRepeat;
	private boolean isThuRepeat;
	private boolean isFriRepeat;
	private boolean isSatRepeat;
	private boolean isSunRepeat;

	public Schedule(){
		timeOn = new Time();
		timeOn.setToNow();
		
		timeOff = new Time();
		timeOff.setToNow();
		
		setSheduleOn(false);
		setSheduleOff(false);
		setMonRepeat(false);
		setTueRepeat(false);
		setWebRepeat(false);
		setThuRepeat(false);
		setFriRepeat(false);
		setSatRepeat(false);
		setSunRepeat(false);
	}
}
