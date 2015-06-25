package com.HomeCenter2.ui.mainS;

import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DatePickerFragment;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyEnergyScreen extends RADialerMainScreenAbstract implements
		View.OnClickListener {

	private static final String TAG = "MyEnergyScreen";
	HomeCenterUIEngine mUiEngine = null;	
	House mHouse = null;
	LayoutInflater mInflater;
	

	public MyEnergyScreen(int title, String tag,  SlidingBaseActivity context) {
		super(MyEnergyScreen.class, title, tag, context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterService service = RegisterService.getService();
		if (service != null) {
			mUiEngine = service.getUIEngine();
		}
		if (mUiEngine == null) {
			return;
		}
		mHouse = mUiEngine.getHouse();
		mInflater = getLayoutInflater(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {		
		super.onDestroy();

	}


	public void saveConfig(Bundle bundle) {
		Message message = Message.obtain();
		message.setData(bundle);
		message.what= HCRequest.REQUEST_SET_DEVICE_ADDRESS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
		
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.my_energy_screen,
				container, false);
		initView(view);
		return view;
	}

	@Override
	public void onScreenSlidingCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (!isVisible())
			return;
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();
		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		mActionBarV7.setTitle(mTitleId);
		mActionBarV7.setDisplayShowTitleEnabled(true);

		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected : " + item.getItemId() + " , home: "
				+ android.R.id.home + ", visible: " + isVisible());
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				break;
			}
		}
		return true;
	}

	EditText mFromExt, mToExt, mDurationExt, mTotal;
	TextView mRoom1Tv, mRoom2Tv, mRoom3Tv, mRoom4Tv, mRoom5Tv, mRoom6Tv,
			mRoom7Tv, mRoom8Tv, mEnegryR1Tv, mEnegryR2Tv, mEnegryR3Tv,
			mEnegryR4Tv, mEnegryR5Tv, mEnegryR6Tv, mEnegryR7Tv, mEnegryR8Tv;

	private void initView(View view) {
		mFromExt = (EditText) view.findViewById(R.id.editFrom);
		mToExt = (EditText) view.findViewById(R.id.editTo);
		mDurationExt = (EditText) view.findViewById(R.id.editDuration);
		mTotal = (EditText) view.findViewById(R.id.editTotal);

		mFromExt.setOnClickListener(this);
		mToExt.setOnClickListener(this);

		mRoom1Tv = (TextView) view.findViewById(R.id.tvNamRoom1);
		mRoom2Tv = (TextView) view.findViewById(R.id.tvNamRoom2);
		mRoom3Tv = (TextView) view.findViewById(R.id.tvNamRoom3);
		mRoom4Tv = (TextView) view.findViewById(R.id.tvNamRoom4);
		mRoom5Tv = (TextView) view.findViewById(R.id.tvNamRoom5);
		mRoom6Tv = (TextView) view.findViewById(R.id.tvNamRoom6);
		mRoom7Tv = (TextView) view.findViewById(R.id.tvNamRoom7);
		mRoom8Tv = (TextView) view.findViewById(R.id.tvNamRoom8);

		mEnegryR1Tv = (TextView) view.findViewById(R.id.tvEnergyRoom1);
		mEnegryR2Tv = (TextView) view.findViewById(R.id.tvEnergyRoom2);
		mEnegryR3Tv = (TextView) view.findViewById(R.id.tvEnergyRoom3);
		mEnegryR4Tv = (TextView) view.findViewById(R.id.tvEnergyRoom4);
		mEnegryR5Tv = (TextView) view.findViewById(R.id.tvEnergyRoom5);
		mEnegryR6Tv = (TextView) view.findViewById(R.id.tvEnergyRoom6);
		mEnegryR7Tv = (TextView) view.findViewById(R.id.tvEnergyRoom7);
		mEnegryR8Tv = (TextView) view.findViewById(R.id.tvEnergyRoom8);

		if (mHouse == null)
			return;
		List<Room> rooms = mHouse.getRooms();
		if (rooms != null && rooms.size() == 8) {
			mRoom1Tv.setText(rooms.get(0).getName());
			mRoom2Tv.setText(rooms.get(1).getName());
			mRoom3Tv.setText(rooms.get(2).getName());
			mRoom4Tv.setText(rooms.get(3).getName());
			mRoom5Tv.setText(rooms.get(4).getName());
			mRoom6Tv.setText(rooms.get(5).getName());
			mRoom7Tv.setText(rooms.get(6).getName());
			mRoom8Tv.setText(rooms.get(7).getName());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editFrom:
			showDayPickerDialog((EditText) v);
			break;
		case R.id.editTo:
		showDayPickerDialog((EditText) v);
		break;
		default:
			break;
		}
	}

	public void showDayPickerDialog(EditText v) {
		DialogFragment newFragment = new DatePickerFragment(v);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub
		
	}
}
