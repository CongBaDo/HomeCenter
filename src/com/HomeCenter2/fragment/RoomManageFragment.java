package com.HomeCenter2.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.HomeCenter2.HomeCenter2Activity;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.HomeScreenSetting;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.activity.RoomListActivity;
import com.HomeCenter2.customview.HorizontalListView;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.GroupBotoomTool;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.GroupBotoomTool.SPECIAL;
import com.HomeCenter2.imageprocessing.PhotoSortrView;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.BottomToolAdapter;
import com.HomeCenter2.ui.adapter.OnOffTypeAdapter;
import com.HomeCenter2.ui.adapter.ToolAdapter;
import com.HomeCenter2.utils.FileUtils;
import com.HomeCenter2.utils.HCUtils;
import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class RoomManageFragment extends Fragment implements OnClickListener {

	private static final String TAG = "RoomManageFragment";

	private TextView tvTitle;
	private int position;
	private HorizontalListView horizonListToolBottom;
	private ImageView imgProcessRight;
	private boolean isLeftCollapse, isRightCollapse;
	private OnOffTypeAdapter adapterLeft;
	private LinearLayout containToolLeft, containToolRight;
	private Room room;
	House mHouse = null;
	HomeCenterUIEngine mUiEngine = null;
	private View footerToolRight;
	private boolean isShowed = false;
	private ImageView imgMain, imgThumb;
	private enum IMAGE_THUMB{Left, Right, None};
	private IMAGE_THUMB thumbType = IMAGE_THUMB.None;
	private File fileRight, fileLeft;
	private ArrayList<GroupBotoomTool> bottomTools;
	private BottomToolAdapter adapterBottom;
	private String rootDataPathLeft, rootDataPathRight;
	private String roomSide;
	
	private PhotoSortrView magicView;
	private RelativeLayout containMagicView;

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
		
		loadToolInRoom(rootDataPathLeft);

		return v;
	}

	private void initUI(View view) {
		containMagicView = (RelativeLayout) view.findViewById(R.id.contain_tools);
		magicView = new PhotoSortrView(getActivity());
		magicView.setTouch(false);
		magicView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		magicView.setBackgroundColor(Color.TRANSPARENT);

		containMagicView.addView(magicView);
		
		tvTitle = (TextView) view.findViewById(R.id.title_room);

		imgProcessRight = (ImageView) view
				.findViewById(R.id.img_expand_close_right);

		containToolLeft = (LinearLayout) view
				.findViewById(R.id.contain_tool_left);
		containToolRight = (LinearLayout) view
				.findViewById(R.id.contain_tool_right);

		imgMain = (ImageView)view.findViewById(R.id.image_showed);
		imgThumb = (ImageView)view.findViewById(R.id.img_changable);
		
		horizonListToolBottom = (HorizontalListView)view.findViewById(R.id.grid_tool_right);
		
		imgThumb.setOnClickListener(this);

		tvTitle.setOnClickListener(this);
		imgProcessRight.setOnClickListener(this);
		
		if(!isShowed){
			containToolLeft.setVisibility(View.GONE);
		}
	}

	private void initData(View v) {
		tvTitle.setText(room.getName());
		bottomTools = new ArrayList<GroupBotoomTool>();
		
		for(int i = 0; i < room.getSensors().size(); i++){
			GroupBotoomTool tool = new GroupBotoomTool();
			
			Sensor[] sensor2 = new Sensor[2];
			sensor2[0] = room.getSensors().get(i);
			sensor2[1] = room.getSensors().get(i+1);
			tool.setDoubSensor(sensor2);
			tool.setSpecialToolType(SPECIAL.NONE);
			bottomTools.add(tool);
			i++;
		}
		
		GroupBotoomTool tool = new GroupBotoomTool();
		tool.setDoubSensor(null);
		tool.setSpecialToolType(SPECIAL.MIC);
		bottomTools.add(tool);
		
		tool = new GroupBotoomTool();
		tool.setDoubSensor(null);
		tool.setSpecialToolType(SPECIAL.ON);
		bottomTools.add(tool);
		
		tool = new GroupBotoomTool();
		tool.setDoubSensor(null);
		tool.setSpecialToolType(SPECIAL.OFF);
		bottomTools.add(tool);
		
		adapterBottom = new BottomToolAdapter(getActivity(), bottomTools);

		horizonListToolBottom.setAdapter(adapterBottom);
		horizonListToolBottom.post(new Runnable() {

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
		
		rootDataPathLeft = configManager.FOLDERNAME + "/"
				+ room.getName().replace(" ", "") + "/left_data.txt";
		
		
		
		if(fileRight.exists()){
			v.findViewById(R.id.contain_thumb).setVisibility(View.VISIBLE);
			loadBitFromPath(fileRight, thumbType);
			
			rootDataPathRight = configManager.FOLDERNAME + "/"
					+ room.getName().replace(" ", "") + "/right_data.txt";
		}else{
			v.findViewById(R.id.contain_thumb).setVisibility(View.INVISIBLE);
		}
		
			
		
	}
	
	private void loadToolInRoom(String rootDataPath){
		magicView.reset();
		ArrayList<String> savedData = FileUtils.getDataFile(rootDataPath);

		Log.v(TAG, "loadToolInRoom " + savedData.size());

		if (savedData.size() > 0) {
			for (int i = 0; i < savedData.size(); i++) {
				String[] values = savedData.get(i).split(";");
				int id = Integer.parseInt(values[0]);

				// String data = currentId+";"+
				// event.getX()+";"+event.getY()+";"+view.getWidth()+";"+view.getHeight();

				float coorX = Float.parseFloat(values[1]);
				float coorY = Float.parseFloat(values[2]);
				int width = Integer.parseInt(values[3]);
				int height = Integer.parseInt(values[4]);

				Log.d(TAG, "" + id + " " + coorX + " " + coorY + " " + width
						+ " " + height);

				addToolView(id, coorX, coorY, width, height);
			}
		}
	}
	
	private void addToolView(int id, float margLeft, float margTop, int width,
			int height) {
		ImageView img = new ImageView(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.setMargins((int) margLeft, (int) margTop, 0, 0);
		img.setLayoutParams(params);
		// containMagicView.addView(img);
		// configManager.OnOffTypes
		int resId = configManager.OnOffTypes.get(0).getIconOn();
		for (int i = 0; i < configManager.OnOffTypes.size(); i++) {
			// Log.v(TAG,
			// "addToolView ID "+configManager.OnOffTypes.get(i).getId());
			if (id == configManager.OnOffTypes.get(i).getId()) {
				// Log.e(TAG, "addToolView ID "+id);
				resId = configManager.OnOffTypes.get(i).getIconOn();
				// break;
			}
		}

		Drawable draw = getResources().getDrawable(resId);
		magicView.addImage(draw, getResources(), margLeft, margTop,
				room.getName(), width, height, resId, roomSide);
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
				loadToolInRoom(rootDataPathRight);
			}else{
				thumbType = IMAGE_THUMB.Left;
				loadBitFromPath(fileLeft, thumbType);
				loadToolInRoom(rootDataPathLeft);
			}
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

		float height = v.getHeight();

		Log.e(TAG, "processRightView " + height + " "
				+ HomeScreenSetting.ScreenH);
		if (isCollapse) {
			ObjectAnimator translationRight = ObjectAnimator.ofFloat(v, "Y",
					HomeScreenSetting.ScreenH - height - 2*getActivity().getActionBar().getHeight() - getStatusBarHeight());
			translationRight.setDuration(500);
			translationRight.start();
		} else {
			ObjectAnimator translationLeft = ObjectAnimator.ofFloat(v, "Y",
					HomeScreenSetting.ScreenH - 3*height/2 );
			translationLeft.setDuration(500);
			translationLeft.start();
		}
	}
	
	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
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
