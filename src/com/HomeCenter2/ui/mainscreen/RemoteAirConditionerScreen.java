package com.HomeCenter2.ui.mainscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.mainS.MyRemotesScreen;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class RemoteAirConditionerScreen extends Fragment implements
		View.OnClickListener, OnLongClickListener {

	private static final String TAG = "RemoteAirConditionerScreen";
	MyRemotesScreen mParentsScreen = null;
	HomeCenterUIEngine mUiEngine = null;

	public RemoteAirConditionerScreen(MyRemotesScreen screen) {
		super();
		mParentsScreen = screen;
	}

	ImageButton m18, m20, m22, m24, mAirUp, mFan, mSwing, mAirDown, mOn, mOff;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		RegisterService service = RegisterService.getService();
		if (service != null) {
			mUiEngine = service.getUIEngine();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (View) inflater.inflate(
				R.layout.remote_air_conditioner_screen, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		m18 = (ImageButton) view.findViewById(R.id.imbAir18);
		m20 = (ImageButton) view.findViewById(R.id.imbAir20);
		m22 = (ImageButton) view.findViewById(R.id.imbAir22);
		m24 = (ImageButton) view.findViewById(R.id.imbAir24);

		mAirUp = (ImageButton) view.findViewById(R.id.imbAirUp);

		mFan = (ImageButton) view.findViewById(R.id.imbAirFan);
		mSwing = (ImageButton) view.findViewById(R.id.imbAirSwing);

		mAirDown = (ImageButton) view.findViewById(R.id.imbAirDown);

		mOn = (ImageButton) view.findViewById(R.id.imbAirON);
		mOff = (ImageButton) view.findViewById(R.id.imbAirOFF);

		m18.setOnClickListener(this);
		m20.setOnClickListener(this);
		m22.setOnClickListener(this);
		m24.setOnClickListener(this);

		mAirUp.setOnClickListener(this);

		mFan.setOnClickListener(this);
		mSwing.setOnClickListener(this);

		mAirDown.setOnClickListener(this);

		mOn.setOnClickListener(this);
		mOff.setOnClickListener(this);

		m18.setOnLongClickListener(this);
		m20.setOnLongClickListener(this);
		m22.setOnLongClickListener(this);
		m24.setOnLongClickListener(this);

		mAirUp.setOnLongClickListener(this);

		mFan.setOnLongClickListener(this);
		mSwing.setOnLongClickListener(this);

		mAirDown.setOnLongClickListener(this);

		mOn.setOnLongClickListener(this);
		mOff.setOnLongClickListener(this);

		mOff.setOnLongClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String id = getIdButton(v);
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
				} 			}
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
		HomeCenter2Activity activity = (HomeCenter2Activity) getActivity();
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);
	}

	private String getIdButton(View v) {
		String id = "00";
		switch (v.getId()) {
		case R.id.imbAir18:
			id = "32";
			break;
		case R.id.imbAir20:
			id = "33";
			break;
		case R.id.imbAir22:
			id = "34";
			break;
		case R.id.imbAir24:
			id = "35";
			break;
		case R.id.imbAirUp:
			id = "36";
			break;
		case R.id.imbAirFan:
			id = "37";
			break;
		case R.id.imbAirSwing:
			id = "38";
			break;
		case R.id.imbAirDown:
			id = "39";
			break;
		case R.id.imbAirON:
			id = "40";
			break;
		case R.id.imbAirOFF:
			id = "41";
			break;
		default:
			break;
		}
		return id;
	}

	@Override
	public boolean onLongClick(View v) {
		String id = getIdButton(v);
		if (mUiEngine != null && id.compareTo("00") != 0) {
			Room room = mParentsScreen.getCurrentRoom();
			if (room != null) {
				Log.d(TAG, "schedule control");
				showShedule(room, id);
			}
		}
		return false;
	}
}
