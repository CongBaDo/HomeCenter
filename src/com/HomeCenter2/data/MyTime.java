package com.HomeCenter2.data;

public class MyTime {
	private int hour;
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	private int minute;
	
	public MyTime(int minute , int hour){
		this.hour = hour;
		this.minute = minute;
	}
	
	public String parseToString(){
		StringBuffer buffer = new StringBuffer();
		String hour, minute;
		hour = configManager.convertIdToString(this.hour);
		minute = configManager.convertIdToString(this.minute);
		
		buffer.append(hour);
		buffer.append(":");
		
		buffer.append(minute);
		return buffer.toString();			
	}
}
