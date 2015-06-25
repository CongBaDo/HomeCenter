package com.HomeCenter2;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class PInfo {
	private static final String TAG = "PInfo";
	private String appname = "";
	private String pname = "";
	private String versionName = "";
	private int versionCode = 0;
	private Drawable icon;

	public void prettyPrint() {
		Log.v(TAG, appname + "\t" + pname + "\t" + versionName + "\t"
				+ versionCode);
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
}
