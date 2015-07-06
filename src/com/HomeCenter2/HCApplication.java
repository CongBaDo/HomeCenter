package com.HomeCenter2;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.HomeCenter2.data.configManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class HCApplication extends Application {

	private static final String TAG = "MyApplication";
	private static HCApplication singleton;

	public static HCApplication getInstance() {
		if (singleton == null) {
			singleton = new HCApplication();
		}
		return singleton;
	}

	public String userAgent;
	public String deviceId;
	private FragmentActivity currentActivity;
	
	public FragmentActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(FragmentActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		
		configManager.FOLDERNAME = Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName();
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager()
					.getPackageInfo(this.getPackageName(), 0);
			userAgent = "Shelfie/" + pInfo.versionCode + "("
					+ android.os.Build.MODEL + ";"
					+ android.os.Build.VERSION.RELEASE + ")";
			Log.e(TAG, "USerAgent " + userAgent);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		deviceId = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
//		String deviceId =  Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
//		Log.e(TAG, "DEvice id "+deviceId+" - "+uuid);
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.bg_noimage)
				// EDIT HERE
				.bitmapConfig(Config.ARGB_8888)
				.displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.NONE)
				.handler(new Handler()).build();

		// ImageLoader.getInstance().setSessionId(ReaderSettings.SessionID,
		// ReaderSettings.PublisherID+"");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(options).threadPoolSize(7)
				.threadPriority(Thread.NORM_PRIORITY - 1).build();
		ImageLoader.getInstance().init(config);
	}
}
