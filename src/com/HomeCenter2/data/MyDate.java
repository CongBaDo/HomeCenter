package com.HomeCenter2.data;

import android.text.format.Time;


public class MyDate {
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	private int day;
	private int month;
	private int year;
	
	public MyDate(int day, int month, int year){
		this.day = day;
		this.month = month;
		this.year = year;		
	}
	
	
	public MyDate(int day, int month){
		this.day = day;
		this.month = month;
				
		Time now = new Time();
		now.setToNow();
		this.year = now.year;		
	}
	
	public String parseToString(){
		StringBuffer buffer = new StringBuffer();
		String month, day, year;
		month= configManager.convertIdToString(this.month);
		day= configManager.convertIdToString(this.day);
		year= configManager.convertIdToString(this.year);
		buffer.append(month);
		buffer.append("/");
		buffer.append(day);
		buffer.append("/");
		buffer.append(year);
		return buffer.toString();				
	}
}
