package com.HomeCenter2.activity;

import java.io.File;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.HomeScreenSetting;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.imageprocessing.PhotoSortrView;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.adapter.OnOffTypeAdapter;
import com.HomeCenter2.ui.adapter.OnOffTypeAdapter.LongCallback;
import com.HomeCenter2.ui.adapter.ToolAdapter;
import com.HomeCenter2.utils.FileUtils;
import com.HomeCenter2.utils.HCUtils;
import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DeviceProcessActivity extends FragmentActivity implements
		OnClickListener {

	private static final String TAG = "DeviceProcessActivity";

	private TextView tvTitle;
	private int position;
	private StaggeredGridView gridToolLeft, gridToolRight;
	private ImageView imgProcessRight;
	private boolean isRightCollapse;
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
	private PhotoSortrView magicView;
	private RelativeLayout containMagicView;
	private int currentId = -1;
	private String rootDataPath;
	private String roomSide;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TMT os
		Log.d(TAG, "onCreate");
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_process_room_image);

		containMagicView = (RelativeLayout) findViewById(R.id.contain_tools);

		magicView = new PhotoSortrView(getApplicationContext());
		magicView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		magicView.setBackgroundColor(Color.TRANSPARENT);

		containMagicView.addView(magicView);

		mUiEngine = RegisterService.getHomeCenterUIEngine();
		// mUiEngine.addStatusObserver(this);
		// mUiEngine.addXMLObserver(this);
		mHouse = mUiEngine.getHouse();

		position = getIntent().getExtras().getInt("no_page");
		room = mHouse.getRooms().get(position);
		roomSide = getIntent().getExtras().getString(configManager.KEY_ROOM_SIDE);

		Log.w(TAG, "ROOM " + room.getName() + " cs ");

		initUI();
		initData();
	}

	private void loadGridToolLeftView() {
		gridToolLeft = new StaggeredGridView(getApplicationContext());
		gridToolLeft.setColumnCount(1);
		// gridToolLeft.set÷
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		gridToolLeft.setLayoutParams(params);
		containToolLeft.addView(gridToolLeft);
	}

	private void initUI() {
		tvTitle = (TextView) findViewById(R.id.title_room);
		// gridToolLeft = (StaggeredGridView) findViewById(R.id.grid_tool_left);

		gridToolRight = (StaggeredGridView) findViewById(R.id.grid_tool_right);

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
		imgMain = (ImageView) findViewById(R.id.image_showed);
		findViewById(R.id.contain_thumb).setVisibility(View.GONE);

		imgMic.setOnClickListener(this);
		imgToolOff.setOnClickListener(this);
		imgToolOn.setOnClickListener(this);
		gridToolRight.addFooterView(footerToolRight);

		tvTitle.setOnClickListener(this);
		imgProcessRight.setOnClickListener(this);

		loadGridToolLeftView();
	}

	private void initData() {
		tvTitle.setText(room.getName());

		adapterLeft = new OnOffTypeAdapter(this, configManager.OnOffTypes);
		adapterLeft.setCallback(new LongCallback() {

			@Override
			public void longCallback(int pos, int id) {
				// TODO Auto-generated method stub
				Log.e(TAG, "longCallback " + pos);
				currentId = id;
			}
		});
		// adapterLeft.getItem(position)

		adapterRight = new ToolAdapter(this, room.getSensors());

		gridToolLeft.setAdapter(adapterLeft);
		// gridToolLeft.setOnItemClickListener(new )
		gridToolRight.setAdapter(adapterRight);

		gridToolRight.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				processRightView(containToolRight, isRightCollapse);
			}
		});

		// HCUtils.getFilePath("left.png", room.getName());
		Log.i(TAG,
				"onCreateView "
						+ HCUtils.getFilePath(configManager.IMAGE_LEFT,
								room.getName()));
		String filePath = getIntent().getExtras().getString(
				configManager.INTENT_PATH_FILE);
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().displayImage("file:///" + filePath, imgMain);

		findViewById(R.id.contain_tools)
				.setOnDragListener(new MyDragListener());

		if(roomSide.equals(configManager.ROOM_LEFT)){
			rootDataPath = configManager.FOLDERNAME + "/"
					+ room.getName().replace(" ", "") + "/left_data.txt";
			
		}else{
			
			rootDataPath = configManager.FOLDERNAME + "/"
					+ room.getName().replace(" ", "") + "/right_data.txt";
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Log.d(TAG, "onResume");

		ArrayList<String> savedData = FileUtils.getDataFile(rootDataPath);

		Log.v(TAG, "onResume " + savedData.size());

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
		ImageView img = new ImageView(getApplicationContext());
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

		Log.e(TAG, "processRightView " + width + " "
				+ HomeScreenSetting.ScreenW);
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

	class MyDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {

			// Handles each of the expected events
			switch (event.getAction()) {

			// signal for the start of a drag and drop operation.

			case DragEvent.ACTION_DRAG_LOCATION:
				// Log.e(TAG, "ACTION_DRAG_LOCATION "+
				// event.getX()+" "+event.getY());
				break;
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;

			// the drag point has entered the bounding box of the View
			case DragEvent.ACTION_DRAG_ENTERED:
				// v.setBackground(targetShape); //change the shape of the view
				break;

			// the user has moved the drag shadow outside the bounding box of
			// the View
			case DragEvent.ACTION_DRAG_EXITED:
				// v.setBackground(normalShape); //change the shape of the view
				// back to normal
				break;

			// drag shadow has been released,the drag point is within the
			// bounding box of the View
			case DragEvent.ACTION_DROP:
				// if the view is the bottomlinear, we accept the drag item
				if (v == findViewById(R.id.contain_tools)) {
					View view = (View) event.getLocalState();

					String data = currentId + ";" + event.getX() + ";"
							+ event.getY() + ";" + view.getWidth() + ";"
							+ view.getHeight();

					File myDir = new File(configManager.FOLDERNAME + "/"
							+ room.getName().replace(" ", ""));

					myDir.mkdirs();
					
					File file;
					if(roomSide.equals(configManager.ROOM_LEFT)){
						file = new File(myDir, "left_data.txt");
					}else{
						file = new File(myDir, "right_data.txt");
					}
					
					
					if (file.exists()) {
						String content = FileUtils.readTextFile(rootDataPath);
						Log.e(TAG, "Exist " + content);
						data = content + data;
					}

					FileUtils.saveData2File(data, DeviceProcessActivity.this,
							room.getName(), roomSide);

					// Log.e(TAG, "Location "+ event.getX()+" "+event.getY()+
					// " "+view.getWidth()+" "+view.getHeight()+" "+view.getId());

					magicView.addImage(view.getBackground(), getResources(),
							event.getX(), event.getY(), room.getName(),
							view.getWidth(), view.getHeight(), currentId, roomSide);
					containToolLeft.removeAllViews();

					loadGridToolLeftView();
					adapterLeft = new OnOffTypeAdapter(
							DeviceProcessActivity.this,
							configManager.OnOffTypes);
					adapterLeft.setCallback(new LongCallback() {

						@Override
						public void longCallback(int pos, int id) {
							// TODO Auto-generated method stub
							Log.e(TAG, "longCallback " + pos + " " + id);
							currentId = id;
						}
					});
					gridToolLeft.setAdapter(adapterLeft);

				} else {
					View view = (View) event.getLocalState();
					view.setVisibility(View.VISIBLE);
					Context context = getApplicationContext();
					Toast.makeText(context, "You can't drop the image here",
							Toast.LENGTH_LONG).show();
					break;
				}
				break;

			// the drag and drop operation has concluded.
			case DragEvent.ACTION_DRAG_ENDED:
				// v.setBackground(normalShape); //go back to normal shape

			default:
				break;
			}
			return true;
		}
	}

}
