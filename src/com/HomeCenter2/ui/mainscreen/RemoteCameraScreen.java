package com.HomeCenter2.ui.mainscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.mainS.MyRemotesScreen;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class RemoteCameraScreen extends Fragment implements
		View.OnClickListener {

	private static final String TAG = "RemoteCameraScreen";
	HomeCenter2 mContext;
	MyRemotesScreen mParentsScreen = null;
	HomeCenterUIEngine mUiEngine = null;

	public RemoteCameraScreen(MyRemotesScreen screen) {
		super();
		mParentsScreen = screen;
	}

	ImageButton mPrevious, mPause, mNext, mRecord, mPlay, mStop, mPower;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mContext = (HomeCenter2) getActivity();
		RegisterService service = RegisterService.getService();
		if (service != null) {
			mUiEngine = service.getUIEngine();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.remote_camera_screen,
				container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mPrevious = (ImageButton) view.findViewById(R.id.imbCameraPrevious);
		mPause = (ImageButton) view.findViewById(R.id.imbCameraPause);

		mNext = (ImageButton) view.findViewById(R.id.imbCameraNext);

		mRecord = (ImageButton) view.findViewById(R.id.imbCameraRecord);
		mPlay = (ImageButton) view.findViewById(R.id.imbCameraPlay);
		mStop = (ImageButton) view.findViewById(R.id.imbCameraStop);

		mPower = (ImageButton) view.findViewById(R.id.imbPower);

		mPrevious.setOnClickListener(this);
		mPause.setOnClickListener(this);
		mNext.setOnClickListener(this);

		mRecord.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mStop.setOnClickListener(this);

		mPower.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String id = "00";
		switch (v.getId()) {
		case R.id.imbCameraPrevious:
			id = "26";
			break;
		case R.id.imbCameraPause:
			id = "27";
			break;

		case R.id.imbCameraNext:
			id = "28";
			break;
		case R.id.imbCameraRecord:
			id = "29";
			break;
		case R.id.imbCameraPlay:
			id = "30";
			break;
		case R.id.imbPower:
			id = "31";
			break;
		default:
			break;
		}
		if (mUiEngine != null && id.compareTo("00") != 0) {
			int type = mUiEngine.getRemoteType();
			Room room = mParentsScreen.getCurrentRoom();
			if (room != null) {
				if (type == configManager.REMOTE_CONTROL) {
					Log.d(TAG, "remote control");
					mParentsScreen.setControlRemote(String.valueOf(1),
							String.valueOf(1), id);
				} else if (type == configManager.REMOTE_UPDATE) {
					Log.d(TAG, "remote control");
					mParentsScreen.setUpdateRemote(String.valueOf(1),
							String.valueOf(1), id);
				} else if (type == configManager.REMOTE_SHEDULE) {
					Log.d(TAG, "schedule control");

					showShedule(room, id);

				}
			}
		}

	}

	private void showShedule(Room room, String buttonId) {
		Bundle bundle = new Bundle();

		bundle.putSerializable(configManager.ROOM_BUNDLE, room);
		bundle.putString(configManager.BUTTON_BUNDLE, buttonId);
		bundle.putBoolean(configManager.IS_DEVICE_BUNDLE, false);

		DetailDeviceScreen fragment = DetailDeviceScreen
				.initializeDetailDeviceScreen(bundle, -1,
						(SlidingBaseActivity) getActivity());
		HomeCenter2 activity = (HomeCenter2) getActivity();
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);
	}

}
