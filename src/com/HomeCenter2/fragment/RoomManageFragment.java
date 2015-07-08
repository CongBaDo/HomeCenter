package com.HomeCenter2.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.HomeCenter2.ui.adapter.ToolAdapter;
import com.etsy.android.grid.StaggeredGridView;

public class RoomManageFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "RoomManageFragment";
	
	private TextView tvTitle;
	private int position;
	private StaggeredGridView gridToolLeft, gridToolRight;
	private ImageView imgProcessLeft, imgProcessRight;
	private boolean isLeftCollapse, isRightCollapse;
	private ToolAdapter adapterLeft, adapterRight;
	private LinearLayout containToolLeft, containToolRight;
	private Room room;
	House mHouse = null;
	HomeCenterUIEngine mUiEngine = null;
	private View footerToolRight;
	private ScheduleImageView imgToolOn, imgToolOff;
	private ImageView imgMic;
	
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
//		mUiEngine.addStatusObserver(getActivity());
//		mUiEngine.addXMLObserver(getActivity());
		mHouse = mUiEngine.getHouse();

        position = getArguments().getInt("no_page");
        room = mHouse.getRooms().get(position);
        
        Log.w(TAG, "ROOM "+room.getName()+" cs "+room.getControls());
//        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//            mContent = savedInstanceState.getString(KEY_CONTENT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_room_manager, container, false);
    	
    	initUI(v);
    	initData();
    	
        return v;
    }
    
    private void initUI(View view){
    	tvTitle = (TextView)view.findViewById(R.id.title_room);
    	gridToolLeft = (StaggeredGridView)view.findViewById(R.id.grid_tool_left);
    	gridToolRight = (StaggeredGridView)view.findViewById(R.id.grid_tool_right);
    	
    	imgProcessLeft = (ImageView)view.findViewById(R.id.img_expand_close_left);
    	imgProcessRight = (ImageView)view.findViewById(R.id.img_expand_close_right);
    	
    	containToolLeft = (LinearLayout)view.findViewById(R.id.contain_tool_left);
    	containToolRight = (LinearLayout)view.findViewById(R.id.contain_tool_right);
    	
    	LayoutInflater inflater = LayoutInflater.from(getActivity());
		footerToolRight = inflater.inflate(R.layout.footer_tool_right, null);
		imgMic = (ImageView)footerToolRight.findViewById(R.id.img_tool_mic);
		imgToolOff = (ScheduleImageView)footerToolRight.findViewById(R.id.img_tool_off);
		imgToolOn = (ScheduleImageView)footerToolRight.findViewById(R.id.img_tool_on);
		
		imgMic.setOnClickListener(this);
		imgToolOff.setOnClickListener(this);
		imgToolOn.setOnClickListener(this);
		gridToolRight.addFooterView(footerToolRight);
    	
    	tvTitle.setOnClickListener(this);
    	imgProcessLeft.setOnClickListener(this);
    	imgProcessRight.setOnClickListener(this);
    }
    
    private void initData(){
    	tvTitle.setText(room.getName());
    	
    	adapterLeft =  new ToolAdapter(getActivity(), room.getSensors());
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_room:
			Intent intent = new Intent(getActivity(), RoomListActivity.class);
			startActivityForResult(intent, configManager.RESULT_ROOM_INDEX);//(intent);
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
	
	private void manageLeftArrow(){
		if(isLeftCollapse){
			isLeftCollapse = false;
			imgProcessLeft.setBackgroundResource(R.drawable.icon_arrow_next);
		}else{
			isLeftCollapse = true;
			imgProcessLeft.setBackgroundResource(R.drawable.icon_arrow_back);
		}
		processLeftView(containToolLeft, isLeftCollapse);
	}
	
	private void manageRightArrow(){
		if(isRightCollapse){
			isRightCollapse = false;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_back);
		}else{
			isRightCollapse = true;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_next);
		}
		processRightView(containToolRight, isRightCollapse);
	}
	
	public void processRightView(View v, boolean isCollapse) {
		
		float width = v.getWidth();
		
		Log.e(TAG, "processRightView "+width+" "+HomeScreenSetting.ScreenW);
		if (isCollapse) {
			ObjectAnimator translationRight = ObjectAnimator.ofFloat(v, "X", HomeScreenSetting.ScreenW - width);
			translationRight.setDuration(500);
			translationRight.start();
		} else {
			ObjectAnimator translationLeft = ObjectAnimator.ofFloat(v, "X", HomeScreenSetting.ScreenW - width/2);
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
			ObjectAnimator translationLeft = ObjectAnimator.ofFloat(v, "X", -width/2);
			translationLeft.setDuration(500);
			translationLeft.start();
		}
	}
}
