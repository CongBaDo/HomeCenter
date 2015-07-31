package com.HomeCenter2.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.HomeScreenSetting;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.activity.RoomListActivity;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.OnOffTypeAdapter;
import com.HomeCenter2.ui.adapter.ToolAdapter;
import com.HomeCenter2.utils.HCUtils;
import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class RoomManageFragment extends Fragment implements OnClickListener {

	private static final String TAG = "RoomManageFragment";

	private TextView tvTitle;
	private int position;
	private StaggeredGridView gridToolLeft, gridToolRight;
	private ImageView imgProcessRight;
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
	private boolean isShowed = false;
	private ImageView imgMain, imgThumb;
	private enum IMAGE_THUMB{Left, Right, None};
	private IMAGE_THUMB thumbType = IMAGE_THUMB.None;
	private File fileRight, fileLeft;

	public static RoomManageFragment newInstance(int position) {
		RoomManageFragment f = new RoomManageFragment();
		Bundle args = new Bundle();
		args.putInt("no_page", position);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUiEngine = RegisterService.getHomeCenterUIEngine();
		if (mUiEngine == null) {
			((HomeCenter2Activity) getActivity()).doExit(getActivity());
		}
		// mUiEngine.addStatusObserver(getActivity());
		// mUiEngine.addXMLObserver(getActivity());
		mHouse = mUiEngine.getHouse();

		position = getArguments().getInt("no_page");
		room = mHouse.getRooms().get(position);
		isShowed = getArguments().getBoolean(configManager.ARGUMENT_IS_SHOWED);

		Log.w(TAG, "ROOM " + room.getName() + " cs " + room.getControls());
		// if ((savedInstanceState != null) &&
		// savedInstanceState.containsKey(KEY_CONTENT)) {
		// mContent = savedInstanceState.getString(KEY_CONTENT);
		// }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_room_manager, container,
				false);

		initUI(v);
		initData(v);

		return v;
	}

	private void initUI(View view) {
		tvTitle = (TextView) view.findViewById(R.id.title_room);
		gridToolLeft = (StaggeredGridView) view
				.findViewById(R.id.grid_tool_left);
		gridToolRight = (StaggeredGridView) view
				.findViewById(R.id.grid_tool_right);

		imgProcessRight = (ImageView) view
				.findViewById(R.id.img_expand_close_right);

		containToolLeft = (LinearLayout) view
				.findViewById(R.id.contain_tool_left);
		containToolRight = (LinearLayout) view
				.findViewById(R.id.contain_tool_right);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		footerToolRight = inflater.inflate(R.layout.footer_tool_right, null);
		imgMic = (ImageView) footerToolRight.findViewById(R.id.img_tool_mic);
		imgToolOff = (ScheduleImageView) footerToolRight
				.findViewById(R.id.img_tool_off);
		imgToolOn = (ScheduleImageView) footerToolRight
				.findViewById(R.id.img_tool_on);
		imgMain = (ImageView)view.findViewById(R.id.image_showed);
		imgThumb = (ImageView)view.findViewById(R.id.img_changable);
		imgThumb.setOnClickListener(this);

		imgMic.setOnClickListener(this);
		imgToolOff.setOnClickListener(this);
		imgToolOn.setOnClickListener(this);
		gridToolRight.addFooterView(footerToolRight);

		tvTitle.setOnClickListener(this);
		imgProcessRight.setOnClickListener(this);
		
		if(!isShowed){
			containToolLeft.setVisibility(View.GONE);
		}
	}

	private void initData(View v) {
		tvTitle.setText(room.getName());

		adapterLeft = new OnOffTypeAdapter(getActivity(),
				configManager.OnOffTypes);

		adapterRight = new ToolAdapter(getActivity(), room.getSensors());

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
		ImageLoader.getInstance().displayImage("file:///"+HCUtils.getFilePath(configManager.IMAGE_LEFT, room.getName()), imgMain);
		
		String filePathRight = HCUtils.getFilePath(configManager.IMAGE_RIGHT, room.getName());
		String filePathLeft = HCUtils.getFilePath(configManager.IMAGE_LEFT, room.getName());
		fileRight = new File(filePathRight);
		fileLeft = new File(filePathLeft);
		
		if(fileRight.exists()){
			v.findViewById(R.id.contain_thumb).setVisibility(View.VISIBLE);
			loadBitFromPath(fileRight, thumbType);
		}else{
			v.findViewById(R.id.contain_thumb).setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putString(KEY_CONTENT, mContent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_room:
			Intent intent = new Intent(getActivity(), RoomListActivity.class);
			startActivityForResult(intent, configManager.RESULT_ROOM_INDEX);// (intent);
			break;
			
		case R.id.img_changable:
			if(thumbType == IMAGE_THUMB.Left){
				thumbType = IMAGE_THUMB.Right;
				loadBitFromPath(fileRight, thumbType);
			}else{
				thumbType = IMAGE_THUMB.Left;
				loadBitFromPath(fileLeft, thumbType);
			}
			break;

		case R.id.img_tool_mic:

			break;

		case R.id.img_tool_off:

			break;

		case R.id.img_tool_on:

			break;

		case R.id.img_expand_close_right:
			manageRightArrow();
			break;

		default:
			break;
		}
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
	
	private void loadBitFromPath(File file, IMAGE_THUMB type){
		FileInputStream fisR;
		try {
			fisR = new FileInputStream(file.getAbsoluteFile());
			
			Bitmap imageBitmapR = BitmapFactory.decodeStream(fisR);
	        
	        BitmapFactory.Options optionsR = new BitmapFactory.Options();
			optionsR.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), optionsR);//decodeResource(getResources(), R.id.myimage, options);
			int imageHeightR = optionsR.outHeight;
			int imageWidthR = optionsR.outWidth;
			
			int imageHR = (HomeScreenSetting.ScreenH/15)*imageHeightR/imageWidthR;

	        imageBitmapR = Bitmap.createScaledBitmap(imageBitmapR, HomeScreenSetting.ScreenH , imageHR, false);

	        if (type == IMAGE_THUMB.Left) {
				imgThumb.setImageBitmap(imageBitmapR);
				ImageLoader.getInstance().displayImage("file:///"+HCUtils.getFilePath(configManager.IMAGE_RIGHT, room.getName()), imgMain);
			} else {
				imgThumb.setImageBitmap(imageBitmapR);
				ImageLoader.getInstance().displayImage("file:///"+HCUtils.getFilePath(configManager.IMAGE_LEFT, room.getName()), imgMain);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
