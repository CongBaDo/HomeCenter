package com.HomeCenter2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.data.configManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RegisterService extends Service{
	
	private final String TAG = "RegisterService";
	private final IBinder mBinder = new LocalBinder();
	static RegisterService mService = null;	
	private HomeCenterUIEngine mUIEngine;
	private configManager mConfigManger = null;	
	
	static public RegisterService getService() {return mService;}	
	public HomeCenterUIEngine getUIEngine() {return mUIEngine;}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return mBinder;
	}

	public class LocalBinder extends Binder {
		RegisterService getService() {
            return RegisterService.this;
        }
    }
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		mService = this;
		initialize();
		super.onCreate();		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {	
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mService = null;		
		super.onDestroy();
	}

	public void initialize() {		
		HandlerThread thread = new HandlerThread("MyHandlerThread");
		thread.start();
		mUIEngine = new HomeCenterUIEngine(thread.getLooper());
		configManager.initConfigManager();
	}

	public static HomeCenterUIEngine getHomeCenterUIEngine(){
			
		if (mService != null) {
			HomeCenterUIEngine uiEngine = mService.getUIEngine();
			if(uiEngine!= null)
				return uiEngine;			
		}
		return null;
		
	}

	private RequestQueue mRequestQueue;
	
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
	
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
