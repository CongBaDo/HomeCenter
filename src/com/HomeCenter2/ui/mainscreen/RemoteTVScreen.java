package com.HomeCenter2.ui.mainscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.Button;
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

public class RemoteTVScreen extends Fragment implements View.OnClickListener,
		OnLongClickListener {

	private static final String TAG = "RemoteTVScreen";
	MyRemotesScreen mParentsScreen = null;
	HomeCenterUIEngine mUiEngine = null;

	public RemoteTVScreen(MyRemotesScreen screen) {
		super();
		mParentsScreen = screen;
	}

	ImageButton mReturn, mMenu, mTool, mInfo,// 1
			mChannelTop, // 2
			mVolumnLeft, mTv, mVolumnRight, // 3
			mPower, mChannelBottom, // 4
			mNu1, mNu2, mNu3, // 5
			mNu4, mNu5, mNu6, // 6
			mNu7, mNu8, mNu9, // 7
			mNuC, mNu0, mNuT, // 8

			mVolumeUp, mVolumeDown, mSlilent, mBook, mCHUP, mCHDown;

	Button mExit, mMute;
	ImageButton mSource;

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
		View view = (View) inflater.inflate(R.layout.remote_tv_screen,
				container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mReturn = (ImageButton) view.findViewById(R.id.imbReturn);
		mMenu = (ImageButton) view.findViewById(R.id.imbMenu);
		mTool = (ImageButton) view.findViewById(R.id.imbTool);
		mInfo = (ImageButton) view.findViewById(R.id.imbInfo);
		mExit = (Button) view.findViewById(R.id.imbExit);

		mMute = (Button) view.findViewById(R.id.imbMute);
		mChannelTop = (ImageButton) view.findViewById(R.id.imbChanelTop);
		mSource = (ImageButton) view.findViewById(R.id.imbSource);

		mVolumnLeft = (ImageButton) view.findViewById(R.id.imbVolumnLeft);
		mTv = (ImageButton) view.findViewById(R.id.imbTV);
		mVolumnRight = (ImageButton) view.findViewById(R.id.imbVolumnRight);

		mPower = (ImageButton) view.findViewById(R.id.imbPower);
		mChannelBottom = (ImageButton) view.findViewById(R.id.imbChanel_bottom);

		mNu1 = (ImageButton) view.findViewById(R.id.imbNu1);
		mNu2 = (ImageButton) view.findViewById(R.id.imbNu2);
		mNu3 = (ImageButton) view.findViewById(R.id.imbNu3);

		mNu4 = (ImageButton) view.findViewById(R.id.imbNu4);
		mNu5 = (ImageButton) view.findViewById(R.id.imbNu5);
		mNu6 = (ImageButton) view.findViewById(R.id.imbNu6);

		mNu7 = (ImageButton) view.findViewById(R.id.imbNu7);
		mNu8 = (ImageButton) view.findViewById(R.id.imbNu8);
		mNu9 = (ImageButton) view.findViewById(R.id.imbNu9);

		mNuC = (ImageButton) view.findViewById(R.id.imbNuC);
		mNu0 = (ImageButton) view.findViewById(R.id.imbNu0);
		mNuT = (ImageButton) view.findViewById(R.id.imbNuT);

		mVolumeUp = (ImageButton) view.findViewById(R.id.imbVUp);
		mVolumeDown = (ImageButton) view.findViewById(R.id.imbVDown);
		mSlilent = (ImageButton) view.findViewById(R.id.imbSilent);
		mBook = (ImageButton) view.findViewById(R.id.imbBook);
		mCHUP = (ImageButton) view.findViewById(R.id.imbCHUp);
		mCHDown = (ImageButton) view.findViewById(R.id.imbCHDown);

		mReturn.setOnClickListener(this);
		mMenu.setOnClickListener(this);
		mTool.setOnClickListener(this);
		mInfo.setOnClickListener(this);
		mExit.setOnClickListener(this);

		mMute.setOnClickListener(this);
		mChannelTop.setOnClickListener(this);
		mSource.setOnClickListener(this);

		mVolumnLeft.setOnClickListener(this);
		mTv.setOnClickListener(this);
		mVolumnRight.setOnClickListener(this);

		mPower.setOnClickListener(this);
		mChannelBottom.setOnClickListener(this);

		mNu1.setOnClickListener(this);
		mNu2.setOnClickListener(this);
		mNu3.setOnClickListener(this);

		mNu4.setOnClickListener(this);
		mNu5.setOnClickListener(this);
		mNu6.setOnClickListener(this);

		mNu7.setOnClickListener(this);
		mNu8.setOnClickListener(this);
		mNu9.setOnClickListener(this);

		mNuC.setOnClickListener(this);
		mNu0.setOnClickListener(this);
		mNuT.setOnClickListener(this);

		mVolumeUp.setOnClickListener(this);
		mVolumeDown.setOnClickListener(this);
		mSlilent.setOnClickListener(this);
		mBook.setOnClickListener(this);
		mCHUP.setOnClickListener(this);
		mCHDown.setOnClickListener(this);
		
		mReturn.setOnLongClickListener(this);
		mMenu.setOnLongClickListener(this);
		mTool.setOnLongClickListener(this);
		mInfo.setOnLongClickListener(this);
		mExit.setOnLongClickListener(this);

		mMute.setOnLongClickListener(this);
		mChannelTop.setOnLongClickListener(this);
		mSource.setOnLongClickListener(this);

		mVolumnLeft.setOnLongClickListener(this);
		mTv.setOnLongClickListener(this);
		mVolumnRight.setOnLongClickListener(this);

		mPower.setOnLongClickListener(this);
		mChannelBottom.setOnLongClickListener(this);

		mNu1.setOnLongClickListener(this);
		mNu2.setOnLongClickListener(this);
		mNu3.setOnLongClickListener(this);

		mNu4.setOnLongClickListener(this);
		mNu5.setOnLongClickListener(this);
		mNu6.setOnLongClickListener(this);

		mNu7.setOnLongClickListener(this);
		mNu8.setOnLongClickListener(this);
		mNu9.setOnLongClickListener(this);

		mNuC.setOnLongClickListener(this);
		mNu0.setOnLongClickListener(this);
		mNuT.setOnLongClickListener(this);

		mVolumeUp.setOnLongClickListener(this);
		mVolumeDown.setOnLongClickListener(this);
		mSlilent.setOnLongClickListener(this);
		mBook.setOnLongClickListener(this);
		mCHUP.setOnLongClickListener(this);
		mCHDown.setOnLongClickListener(this);

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
					mParentsScreen
							.setControlRemote(String.valueOf(room.getId()),
									String.valueOf(1), id);
				} else if (type == configManager.REMOTE_UPDATE) {
					Log.d(TAG, "remote control");
					mParentsScreen
							.setUpdateRemote(String.valueOf(room.getId()),
									String.valueOf(1), id);
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
		HomeCenter2Activity activity = (HomeCenter2Activity) getActivity();
		activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
				true, true, false);

		/*
		 * ScheduleRemoteScreen fragment = ScheduleRemoteScreen
		 * .initializeDetailDeviceScreen(bundle,
		 * -1,ScreenManager.SCHEDULE_REMOTE_TAG, (SlidingBaseActivity)
		 * getActivity()); HomeCenter2 activity = (HomeCenter2) getActivity();
		 * activity.switchContentView(fragment, ScreenManager.DETAIL_DEVICE_TAG,
		 * true, true, false);
		 */
	}

	private String getIdButton(View v) {
		String id = "00";
		switch (v.getId()) {
		case R.id.imbReturn:
			id = "01";
			break;
		case R.id.imbMenu:
			id = "02";
			break;
		case R.id.imbTool:
			id = "03";
			break;
		case R.id.imbInfo:
			id = "04";
			break;
		case R.id.imbExit:
			id = "05";
			break;
		case R.id.imbMute:
			id = "06";
			break;
		case R.id.imbChanelTop:
			id = "07";
			break;
		case R.id.imbSource:
			id = "08";
			break;

		case R.id.imbVolumnLeft:
			id = "09";
			break;
		case R.id.imbTV:
			id = "10";
			break;
		case R.id.imbVolumnRight:
			id = "11";
			break;

		case R.id.imbPower:
			id = "12";
			break;
		case R.id.imbChanel_bottom:
			id = "13";
			break;

		case R.id.imbNu1:
			id = "14";
			break;
		case R.id.imbNu2:
			id = "15";
			break;
		case R.id.imbNu3:
			id = "16";
			break;

		case R.id.imbNu4:
			id = "17";
			break;
		case R.id.imbNu5:
			id = "18";
			break;
		case R.id.imbNu6:
			id = "19";
			break;

		case R.id.imbNu7:
			id = "20";
			break;
		case R.id.imbNu8:
			id = "21";
			break;
		case R.id.imbNu9:
			id = "22";
			break;

		case R.id.imbNu0:
			id = "23";
			break;
		case R.id.imbNuC:
			id = "24";
			break;
		case R.id.imbNuT:
			id = "25";
			break;

		case R.id.imbVUp:
			id = "42";
			break;

		case R.id.imbVDown:
			id = "43";
			break;

		case R.id.imbSilent:
			id = "44";
			break;

		case R.id.imbBook:
			id = "45";
			break;

		case R.id.imbCHUp:
			id = "46";
			break;

		case R.id.imbCHDown:
			id = "47";
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