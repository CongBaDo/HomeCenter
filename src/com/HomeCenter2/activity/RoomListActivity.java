package com.HomeCenter2.activity;

import com.HomeCenter2.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RoomListActivity extends FragmentActivity implements OnItemClickListener{

	private static final String TAG = "RoomListActivity";
	
	private GridView gridRooms;
	
	@Override
	public void onCreate(Bundle arg0){
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_room_list);
		
		gridRooms = (GridView)findViewById(R.id.grid_room);
		gridRooms.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
