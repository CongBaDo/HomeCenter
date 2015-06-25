package com.HomeCenter2;

import android.os.Bundle;
import android.util.Log;

import com.HomeCenter2.data.configManager;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class HCStringRequest extends StringRequest implements
		Response.Listener<String>, Response.ErrorListener {
	public static final String TAG = "HCStringRequest";
	Bundle bundle = null;

	public HCStringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url,listener,errorListener);	
		bundle = (Bundle) this.getTag();
	}
	
	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "arg0" + arg0);
	}

	@Override
	public void onResponse(String arg0) {
		Log.d(TAG, "arg0" + arg0);
		String devId = bundle.getString(configManager.DEVICE_ID);
		Boolean on = bundle.getBoolean(configManager.ON_OFF_ACTION);
		Log.d(TAG, "devId :" + devId + ", on:" + on);
	}
}
