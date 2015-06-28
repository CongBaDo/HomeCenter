package com.HomeCenter2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.HomeCenter2.R;
import com.HomeCenter2.ui.adapter.ToolAdapter;

public class RoomManageFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "RoomManageFragment";
	
	private TextView tvTitle;
	private int position;
	private GridView gridToolLeft, gridToolRight;
	private ImageView imgProcessLeft, imgProcessRight;
	private boolean isLeftCollapse, isRightCollapse;
	private ToolAdapter adapterLeft, adapterRight;
	private FrameLayout frameLeft, frameRight;
	
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

        position = getArguments().getInt("no_page");
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
    	gridToolLeft = (GridView)view.findViewById(R.id.grid_tool_left);
    	gridToolRight = (GridView)view.findViewById(R.id.grid_tool_right);
    	gridToolLeft.setNumColumns(1);
    	gridToolRight.setNumColumns(1);
    	
    	imgProcessLeft = (ImageView)view.findViewById(R.id.img_expand_close_left);
    	imgProcessRight = (ImageView)view.findViewById(R.id.img_expand_close_right);
    	
    	frameLeft = (FrameLayout)view.findViewById(R.id.frame_left);
    	frameRight = (FrameLayout)view.findViewById(R.id.frame_right);
    	
    	tvTitle.setOnClickListener(this);
    	imgProcessLeft.setOnClickListener(this);
    	imgProcessRight.setOnClickListener(this);
    }
    
    private void initData(){
    	tvTitle.setText("Page "+position);
    	
    	adapterLeft = new ToolAdapter(getActivity());
    	adapterRight = new ToolAdapter(getActivity());
    	
    	gridToolLeft.setAdapter(adapterLeft);
    	gridToolRight.setAdapter(adapterRight);
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
			gridToolLeft.setNumColumns(2);
			Toast.makeText(getActivity(), "Expand", Toast.LENGTH_SHORT).show();
		}else{
			isLeftCollapse = true;
			imgProcessLeft.setBackgroundResource(R.drawable.icon_arrow_back);
			gridToolLeft.setNumColumns(1);
			Toast.makeText(getActivity(), "Collapse", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void manageRightArrow(){
		LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		if(isRightCollapse){
			isRightCollapse = false;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_back);
			gridToolRight.setNumColumns(2);
			Toast.makeText(getActivity(), "Expand", Toast.LENGTH_SHORT).show();
		}else{
			isRightCollapse = true;
			imgProcessRight.setBackgroundResource(R.drawable.icon_arrow_next);
			gridToolRight.setNumColumns(1);
			Toast.makeText(getActivity(), "Collapse", Toast.LENGTH_SHORT).show();
			
		}
//		gridToolRight.setLayoutParams(params);
		frameRight.setLayoutParams(params);
	}

	private void updateToolView(){
		
	}
}
