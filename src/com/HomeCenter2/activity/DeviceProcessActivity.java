package com.HomeCenter2.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.HomeScreenSetting;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.OnOffTypeAdapter;
import com.HomeCenter2.ui.adapter.ToolAdapter;
import com.HomeCenter2.utils.HCUtils;
import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DeviceProcessActivity extends FragmentActivity implements OnClickListener{

	private static final String TAG = "DeviceProcessActivity";
	
	private TextView tvTitle;
	private int position;
	private StaggeredGridView gridToolLeft, gridToolRight;
	private ImageView imgProcessLeft, imgProcessRight;
	private boolean isLeftCollapse, isRightCollapse;
	private OnOffTypeAdapter adapterLeft;
	private ToolAdapter adapterRight;
	private LinearLayout containToolLeft, containToolRight;
	private Room room;
	House mHouse = null;
	HomeCenterUIEngine mUiEngine = null;
	private View footerToolRight;
	private ScheduleImageView imgToolOn, imgToolOff;
	private ImageView imgMic;
	private ImageView imgMain;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TMT os
		Log.d(TAG, "onCreate");
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		setContentView(R.layout.fragment_room_manager);
		
		mUiEngine = RegisterService.getHomeCenterUIEngine();
		// mUiEngine.addStatusObserver(this);
		// mUiEngine.addXMLObserver(this);
		mHouse = mUiEngine.getHouse();

		position = getIntent().getExtras().getInt("no_page");
		room = mHouse.getRooms().get(position);

		Log.w(TAG, "ROOM " + room.getName() + " cs " );
		
		initUI();
		initData();
	}
	
	private void initUI() {
		tvTitle = (TextView) findViewById(R.id.title_room);
		gridToolLeft = (StaggeredGridView) findViewById(R.id.grid_tool_left);
		gridToolRight = (StaggeredGridView) findViewById(R.id.grid_tool_right);

		imgProcessLeft = (ImageView) findViewById(R.id.img_expand_close_left);
		imgProcessRight = (ImageView) findViewById(R.id.img_expand_close_right);

		containToolLeft = (LinearLayout) findViewById(R.id.contain_tool_left);
		containToolRight = (LinearLayout) findViewById(R.id.contain_tool_right);

		LayoutInflater inflater = LayoutInflater.from(this);
		footerToolRight = inflater.inflate(R.layout.footer_tool_right, null);
		imgMic = (ImageView) footerToolRight.findViewById(R.id.img_tool_mic);
		imgToolOff = (ScheduleImageView) footerToolRight
				.findViewById(R.id.img_tool_off);
		imgToolOn = (ScheduleImageView) footerToolRight
				.findViewById(R.id.img_tool_on);
		imgMain = (ImageView)findViewById(R.id.image_showed);
		findViewById(R.id.contain_thumb).setVisibility(View.GONE);

		imgMic.setOnClickListener(this);
		imgToolOff.setOnClickListener(this);
		imgToolOn.setOnClickListener(this);
		gridToolRight.addFooterView(footerToolRight);

		tvTitle.setOnClickListener(this);
		imgProcessLeft.setOnClickListener(this);
		imgProcessRight.setOnClickListener(this);
	}

	private void initData() {
		tvTitle.setText(room.getName());

		adapterLeft = new OnOffTypeAdapter(this,
				configManager.OnOffTypes);

		adapterRight = new ToolAdapter(this, room.getSensors());

		gridToolLeft.setAdapter(adapterLeft);
		gridToolRight.setAdapter(adapterRight);

		gridToolLeft.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				processLeftView(containToolLeft, isLeftCollapse);
			}
		});

		gridToolRight.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				processRightView(containToolRight, isRightCollapse);
			}
		});
		
//		HCUtils.getFilePath("left.png", room.getName());
		Log.i(TAG, "onCreateView "+HCUtils.getFilePath(configManager.IMAGE_LEFT, room.getName()));
		String filePath = getIntent().getExtras().getString(configManager.INTENT_PATH_FILE);
		ImageLoader.getInstance().displayImage("file:///"+filePath, imgMain);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_room:
			Intent intent = new Intent(this, RoomListActivity.class);
			startActivityForResult(intent, configManager.RESULT_ROOM_INDEX);// (intent);
			break;
			
		case R.id.img_tool_mic:

			break;

		case R.id.img_tool_off:

			break;

		case R.id.img_tool_on:

			break;

		case R.id.img_expand_close_left:
			manageLeftArrow();
			break;

		case R.id.img_expand_close_right:
			manageRightArrow();
			break;

		default:
			break;
		}
	}
	
	private void manageLeftArrow() {
		if (isLeftCollapse) {
			isLeftCollapse = false;
			imgProcessLeft.setBackgroundResource(R.drawable.icon_arrow_next);
		} else {
			isLeftCollapse = true;
			imgProcessLeft.setBackgroundResource(R.drawable.icon_arrow_back);
		}
		processLeftView(containToolLeft, isLeftCollapse);
	}

	private void manageRightArrow() {
		if (isRightCollapse) {
			isRightCollapse = false;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_back);
		} else {
			isRightCollapse = true;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_next);
		}
		processRightView(containToolRight, isRightCollapse);
	}

	public void processRightView(View v, boolean isCollapse) {
		
		float width = v.getWidth();
		
		Log.e(TAG, "processRightView "+width+" "+HomeScreenSetting.ScreenW);
		if (isCollapse) {
			ObjectAnimator translationRight = ObjectAnimator.ofFloat(v, "X",
					HomeScreenSetting.ScreenW - width);
			translationRight.setDuration(500);
			translationRight.start();
		} else {
			ObjectAnimator translationLeft = ObjectAnimator.ofFloat(v, "X",
					HomeScreenSetting.ScreenW - width / 2);
			translationLeft.setDuration(500);
			translationLeft.start();
		}
	}

	public void processLeftView(View v, boolean isCollapse) {
		float width = v.getWidth();
		if (isCollapse) {
			ObjectAnimator translationRight = ObjectAnimator.ofFloat(v, "X", 0);
			translationRight.setDuration(500);
			translationRight.start();
		} else {
			ObjectAnimator translationLeft = ObjectAnimator.ofFloat(v, "X",
					-width / 2);
			translationLeft.setDuration(500);
			translationLeft.start();
		}
	}
}