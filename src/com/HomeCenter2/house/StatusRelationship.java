package com.HomeCenter2.house;

import android.text.format.Time;

import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.MyTime;

public class StatusRelationship {
	private int roomId;
	private int devId;
	private boolean isTemp1;
	private boolean isLight1;
	private boolean isSmoke1;
	private boolean isMotion1;
	private boolean isMotion2;
	private boolean isDoor1;
	private boolean isDoor2;

	private boolean isTemp2;
	private boolean isLight2;
	private boolean isSmoke2;
	private boolean isMotion3;
	private boolean isMotion4;
	private boolean isDoor3;
	private boolean isDoor4;

	private boolean isTrigger;

	private boolean isSE;


	// Time schedule01
	private boolean isAddedScheduleS01;
	private MyTime timeS01;
	private MyDate dateS01;
	private boolean isOnS01;

	private boolean isMonS01;
	private boolean isTueS01;
	private boolean isWedS01;
	private boolean isThuS01;
	private boolean isFriS01;
	private boolean isSatS01;
	private boolean isSunS01;

	// Time schedule02
	private boolean isAddedScheduleS02;
	private MyTime timeS02;
	private MyDate dateS02;
	private boolean isOnS02;

	private boolean isMonS02;
	private boolean isTueS02;
	private boolean isWedS02;
	private boolean isThuS02;
	private boolean isFriS02;
	private boolean isSatS02;
	private boolean isSunS02;

	// Time schedule03
	private boolean isAddedScheduleS03;
	private MyTime timeS03;
	private MyDate dateS03;
	private boolean isOnS03;

	private boolean isMonS03;
	private boolean isTueS03;
	private boolean isWedS03;
	private boolean isThuS03;
	private boolean isFriS03;
	private boolean isSatS03;
	private boolean isSunS03;

	// Time schedule04
	private boolean isAddedScheduleS04;
	private MyTime timeS04;
	private MyDate dateS04;
	private boolean isOnS04;

	private boolean isMonS04;
	private boolean isTueS04;
	private boolean isWedS04;
	private boolean isThuS04;
	private boolean isFriS04;
	private boolean isSatS04;
	private boolean isSunS04;

	public StatusRelationship() {
		roomId = 0;
		devId = 0;
		isTemp1 = false;
		isLight1 = false;
		isSmoke1 = false;
		isMotion1 = false;
		isMotion2 = false;
		isDoor1 = false;
		isDoor2 = false;

		isTemp2 = false;
		isLight2 = false;
		isSmoke2 = false;
		isMotion3 = false;
		isMotion4 = false;
		isDoor3 = false;
		isDoor4 = false;

		isTrigger = false;
		isSE = false;

		isAddedScheduleS01 = false;

		Time now = new Time();
		now.setToNow();

		// time schedule 01
		timeS01 = new MyTime(now.minute, now.hour);
		dateS01 = new MyDate(now.monthDay, now.month + 1, now.year);

		isOnS01 = false;

		isMonS01 = false;
		isTueS01 = false;
		isWedS01 = false;
		isThuS01 = false;
		isFriS01 = false;
		isSatS01 = false;
		isSunS01 = false;

		// time schedule 02
		timeS02 = new MyTime(now.minute, now.hour);
		dateS02 = new MyDate(now.monthDay, now.month, now.year);

		isOnS02 = false;

		isMonS02 = false;
		isTueS02 = false;
		isWedS02 = false;
		isThuS02 = false;
		isFriS02 = false;
		isSatS02 = false;
		isSunS02 = false;

		// time schedule 03
		timeS03 = new MyTime(now.minute, now.hour);
		dateS03 = new MyDate(now.monthDay, now.month, now.year);

		isOnS03 = false;

		isMonS03 = false;
		isTueS03 = false;
		isWedS03 = false;
		isThuS03 = false;
		isFriS03 = false;
		isSatS03 = false;
		isSunS03 = false;

		// time schedule 04
		timeS04 = new MyTime(now.minute, now.hour);
		dateS04 = new MyDate(now.monthDay, now.month, now.year);

		isOnS04 = false;

		isMonS04 = false;
		isTueS04 = false;
		isWedS04 = false;
		isThuS04 = false;
		isFriS04 = false;
		isSatS04 = false;
		isSunS04 = false;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getDevId() {
		return devId;
	}

	public void setDevId(int devId) {
		this.devId = devId;
	}

	public boolean isTemp1() {
		return isTemp1;
	}

	public void setTemp1(boolean isTemp1) {
		this.isTemp1 = isTemp1;
	}

	public boolean isLight1() {
		return isLight1;
	}

	public void setLight1(boolean isLight1) {
		this.isLight1 = isLight1;
	}

	public boolean isSmoke1() {
		return isSmoke1;
	}

	public void setSmoke1(boolean isSmoke1) {
		this.isSmoke1 = isSmoke1;
	}

	public boolean isMotion1() {
		return isMotion1;
	}

	public void setMotion1(boolean isMotion1) {
		this.isMotion1 = isMotion1;
	}

	public boolean isMotion2() {
		return isMotion2;
	}

	public void setMotion2(boolean isMotion2) {
		this.isMotion2 = isMotion2;
	}

	public boolean isDoor1() {
		return isDoor1;
	}

	public void setDoor1(boolean isDoor1) {
		this.isDoor1 = isDoor1;
	}

	public boolean isDoor2() {
		return isDoor2;
	}

	public void setDoor2(boolean isDoor2) {
		this.isDoor2 = isDoor2;
	}

	public boolean isTemp2() {
		return isTemp2;
	}

	public void setTemp2(boolean isTemp2) {
		this.isTemp2 = isTemp2;
	}

	public boolean isLight2() {
		return isLight2;
	}

	public void setLight2(boolean isLight2) {
		this.isLight2 = isLight2;
	}

	public boolean isSmoke2() {
		return isSmoke2;
	}

	public void setSmoke2(boolean isSmoke2) {
		this.isSmoke2 = isSmoke2;
	}

	public boolean isMotion3() {
		return isMotion3;
	}

	public void setMotion3(boolean isMotion3) {
		this.isMotion3 = isMotion3;
	}

	public boolean isMotion4() {
		return isMotion4;
	}

	public void setMotion4(boolean isMotion4) {
		this.isMotion4 = isMotion4;
	}

	public boolean isDoor3() {
		return isDoor3;
	}

	public void setDoor3(boolean isDoor3) {
		this.isDoor3 = isDoor3;
	}

	public boolean isDoor4() {
		return isDoor4;
	}

	public void setDoor4(boolean isDoor4) {
		this.isDoor4 = isDoor4;
	}

	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}

	public boolean isSE() {
		return isSE;
	}

	public void setSE(boolean isSE) {
		this.isSE = isSE;
	}

	public boolean isAddedScheduleS01() {
		return isAddedScheduleS01;
	}

	public void setAddedScheduleS01(boolean isAddedScheduleS01) {
		this.isAddedScheduleS01 = isAddedScheduleS01;
	}

	public MyTime getTimeS01() {
		return timeS01;
	}

	public void setTimeS01(MyTime timeS01) {
		this.timeS01 = timeS01;
	}

	public MyDate getDateS01() {
		return dateS01;
	}

	public void setDateS01(MyDate dateS01) {
		this.dateS01 = dateS01;
	}

	public boolean isOnS01() {
		return isOnS01;
	}

	public void setOnS01(boolean isOnS01) {
		this.isOnS01 = isOnS01;
	}

	public boolean isMonS01() {
		return isMonS01;
	}

	public void setMonS01(boolean isMonS01) {
		this.isMonS01 = isMonS01;
	}

	public boolean isTueS01() {
		return isTueS01;
	}

	public void setTueS01(boolean isTueS01) {
		this.isTueS01 = isTueS01;
	}

	public boolean isWedS01() {
		return isWedS01;
	}

	public void setWedS01(boolean isWedS01) {
		this.isWedS01 = isWedS01;
	}

	public boolean isThuS01() {
		return isThuS01;
	}

	public void setThuS01(boolean isThuS01) {
		this.isThuS01 = isThuS01;
	}

	public boolean isFriS01() {
		return isFriS01;
	}

	public void setFriS01(boolean isFriS01) {
		this.isFriS01 = isFriS01;
	}

	public boolean isSatS01() {
		return isSatS01;
	}

	public void setSatS01(boolean isSatS01) {
		this.isSatS01 = isSatS01;
	}

	public boolean isSunS01() {
		return isSunS01;
	}

	public void setSunS01(boolean isSunS01) {
		this.isSunS01 = isSunS01;
	}

	public boolean isAddedScheduleS02() {
		return isAddedScheduleS02;
	}

	public void setAddedScheduleS02(boolean isAddedScheduleS02) {
		this.isAddedScheduleS02 = isAddedScheduleS02;
	}

	public MyTime getTimeS02() {
		return timeS02;
	}

	public void setTimeS02(MyTime timeS02) {
		this.timeS02 = timeS02;
	}

	public MyDate getDateS02() {
		return dateS02;
	}

	public void setDateS02(MyDate dateS02) {
		this.dateS02 = dateS02;
	}

	public boolean isOnS02() {
		return isOnS02;
	}

	public void setOnS02(boolean isOnS02) {
		this.isOnS02 = isOnS02;
	}

	public boolean isMonS02() {
		return isMonS02;
	}

	public void setMonS02(boolean isMonS02) {
		this.isMonS02 = isMonS02;
	}

	public boolean isTueS02() {
		return isTueS02;
	}

	public void setTueS02(boolean isTueS02) {
		this.isTueS02 = isTueS02;
	}

	public boolean isWedS02() {
		return isWedS02;
	}

	public void setWedS02(boolean isWedS02) {
		this.isWedS02 = isWedS02;
	}

	public boolean isThuS02() {
		return isThuS02;
	}

	public void setThuS02(boolean isThuS02) {
		this.isThuS02 = isThuS02;
	}

	public boolean isFriS02() {
		return isFriS02;
	}

	public void setFriS02(boolean isFriS02) {
		this.isFriS02 = isFriS02;
	}

	public boolean isSatS02() {
		return isSatS02;
	}

	public void setSatS02(boolean isSatS02) {
		this.isSatS02 = isSatS02;
	}

	public boolean isSunS02() {
		return isSunS02;
	}

	public void setSunS02(boolean isSunS02) {
		this.isSunS02 = isSunS02;
	}

	public boolean isAddedScheduleS03() {
		return isAddedScheduleS03;
	}

	public void setAddedScheduleS03(boolean isAddedScheduleS03) {
		this.isAddedScheduleS03 = isAddedScheduleS03;
	}

	public MyTime getTimeS03() {
		return timeS03;
	}

	public void setTimeS03(MyTime timeS03) {
		this.timeS03 = timeS03;
	}

	public MyDate getDateS03() {
		return dateS03;
	}

	public void setDateS03(MyDate dateS03) {
		this.dateS03 = dateS03;
	}

	public boolean isOnS03() {
		return isOnS03;
	}

	public void setOnS03(boolean isOnS03) {
		this.isOnS03 = isOnS03;
	}

	public boolean isMonS03() {
		return isMonS03;
	}

	public void setMonS03(boolean isMonS03) {
		this.isMonS03 = isMonS03;
	}

	public boolean isTueS03() {
		return isTueS03;
	}

	public void setTueS03(boolean isTueS03) {
		this.isTueS03 = isTueS03;
	}

	public boolean isWedS03() {
		return isWedS03;
	}

	public void setWedS03(boolean isWedS03) {
		this.isWedS03 = isWedS03;
	}

	public boolean isThuS03() {
		return isThuS03;
	}

	public void setThuS03(boolean isThuS03) {
		this.isThuS03 = isThuS03;
	}

	public boolean isFriS03() {
		return isFriS03;
	}

	public void setFriS03(boolean isFriS03) {
		this.isFriS03 = isFriS03;
	}

	public boolean isSatS03() {
		return isSatS03;
	}

	public void setSatS03(boolean isSatS03) {
		this.isSatS03 = isSatS03;
	}

	public boolean isSunS03() {
		return isSunS03;
	}

	public void setSunS03(boolean isSunS03) {
		this.isSunS03 = isSunS03;
	}

	public boolean isAddedScheduleS04() {
		return isAddedScheduleS04;
	}

	public void setAddedScheduleS04(boolean isAddedScheduleS04) {
		this.isAddedScheduleS04 = isAddedScheduleS04;
	}

	public MyTime getTimeS04() {
		return timeS04;
	}

	public void setTimeS04(MyTime timeS04) {
		this.timeS04 = timeS04;
	}

	public MyDate getDateS04() {
		return dateS04;
	}

	public void setDateS04(MyDate dateS04) {
		this.dateS04 = dateS04;
	}

	public boolean isOnS04() {
		return isOnS04;
	}

	public void setOnS04(boolean isOnS04) {
		this.isOnS04 = isOnS04;
	}

	public boolean isMonS04() {
		return isMonS04;
	}

	public void setMonS04(boolean isMonS04) {
		this.isMonS04 = isMonS04;
	}

	public boolean isTueS04() {
		return isTueS04;
	}

	public void setTueS04(boolean isTueS04) {
		this.isTueS04 = isTueS04;
	}

	public boolean isWedS04() {
		return isWedS04;
	}

	public void setWedS04(boolean isWedS04) {
		this.isWedS04 = isWedS04;
	}

	public boolean isThuS04() {
		return isThuS04;
	}

	public void setThuS04(boolean isThuS04) {
		this.isThuS04 = isThuS04;
	}

	public boolean isFriS04() {
		return isFriS04;
	}

	public void setFriS04(boolean isFriS04) {
		this.isFriS04 = isFriS04;
	}

	public boolean isSatS04() {
		return isSatS04;
	}

	public void setSatS04(boolean isSatS04) {
		this.isSatS04 = isSatS04;
	}

	public boolean isSunS04() {
		return isSunS04;
	}

	public void setSunS04(boolean isSunS04) {
		this.isSunS04 = isSunS04;
	}

}
