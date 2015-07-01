package com.HomeCenter2.activity;

import com.HomeCenter2.R;
import com.HomeCenter2.ui.adapter.RoomAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RoomListActivity extends FragmentActivity implements OnItemClickListener{

	private static final String TAG = "RoomListActivity";
	
	private GridView gridRooms;
	private RoomAdapter roomAdapter;
	
	@Override
	public void onCreate(Bundle arg0){
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_room_list);
		
		gridRooms = (GridView)findViewById(R.id.grid_room);
		gridRooms.setOnItemClickListener(this);
		
		roomAdapter = new RoomAdapter(getApplicationContext());
		gridRooms.setAdapter(roomAdapter);
		
		getActionBar().setTitle("Rooms");
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
        intent.putExtra("position", position);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
//			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//			Intent intent = getIntent();
////			            intent.putExtra("widthInfo", s);
//            setResult(RESULT_OK, intent);

			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
