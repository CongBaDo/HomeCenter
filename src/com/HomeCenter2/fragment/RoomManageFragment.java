package com.HomeCenter2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.HomeCenter2.R;

public class RoomManageFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "RoomManageFragment";
	
	private TextView tvTitle;
	private int position;
	
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
    }
    
    private void initData(){
    	tvTitle.setText("Page "+position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
