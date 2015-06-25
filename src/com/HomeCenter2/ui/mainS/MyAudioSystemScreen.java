package com.HomeCenter2.ui.mainS;

import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.AudioHC;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.CustomSpinner;
import com.HomeCenter2.ui.ScheduleImageView;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;
import com.HomeCenter2.ui.adapter.MyAudioAdapter;
import com.HomeCenter2.ui.adapter.MyAudioItem;
import com.HomeCenter2.ui.listener.AudioListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class MyAudioSystemScreen extends RADialerMainScreenAbstract implements
		View.OnClickListener, AudioListener, onCheckChangedListener {

	private static final String TAG = "MyAudioSystemScreen";
	HomeCenterUIEngine mUiEngine = null;
	House mHouse = null;
	LayoutInflater mInflater;

	ListView mListView;
	MyAudioAdapter mAdpater;
	List<AudioHC> mAudioHC = null;
	
	Menu mMenu;
	public boolean mIsSpeaking = false;

	// all room
	String[] mInput;
	ArrayAdapter<String> adp1;

	ScheduleImageView mAllRoomOn, mAllRoomOff;	
	Spinner mSpinInput;
	AudioHC mAudioAll;

	private int[] mAudioArray;
	private ImageView mRoomImg;

	public MyAudioSystemScreen(int title, String tag,
			SlidingBaseActivity context) {
		super(MyAudioSystemScreen.class, title, tag, context);
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
		mAudioHC = mUiEngine.createAudio();
		int size = mAudioHC.size();
		mAudioArray = new int[size];
		
		for(int i = 0; i< size; i++){
			mAudioArray[i] = (mAudioHC.get(i).isState() ? 1: 0);
		}
		mUiEngine.addAudioObserver(this);
		mInflater = getLayoutInflater(savedInstanceState);
		
		mInput = mContext.getResources().getStringArray(R.array.audioTemp);
		size = mAudioHC.size();		
		mInput = new String[size];
		AudioHC item=  null; 
		Room room = null;
		for(int i = 0; i< size ; i++){
			item = mAudioHC.get(i);
			room = item.getRoom();
			mInput[i]= room.getName();
		}		
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mUiEngine != null) {
			mUiEngine.removeAudioObserver(this);
		}
	}

	public void saveConfig(Bundle bundle) {
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_DEVICE_ADDRESS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = (View) inflater.inflate(R.layout.my_audio_screen,
				container, false);
		mRoomImg = (ImageView) view.findViewById(R.id.imgIcon);		
		mRoomImg.setImageResource(R.drawable.ic_room_orange);
		
		mListView = (ListView) view.findViewById(R.id.lvAudio);
		mAdpater = new MyAudioAdapter(MyAudioSystemScreen.this, mContext, mAudioHC);
		mListView.setAdapter(mAdpater);

		mAllRoomOn = (ScheduleImageView) view.findViewById(R.id.imgOn);
		mAllRoomOff = (ScheduleImageView) view.findViewById(R.id.imgOff);

		mAudioAll = new AudioHC();
		Room room = new Room();
		room.setId(configManager.ROOM_ALL_ID);
		mAudioAll.setRoom(room);
		mAudioAll.setState(false);
		mAudioAll.setInput(0);

		mSpinInput = (Spinner) view.findViewById(R.id.spnRoom);
		adp1 = new ArrayAdapter<String>(mContext,
				R.layout.textview_spinner, mInput);
		mSpinInput.setAdapter(adp1);
		mSpinInput.setSelection(mAudioAll.getInput());

		mAllRoomOn.setOnClickListener(this);
		mAllRoomOn.setSrcCheched(R.drawable.btn_on_icon);
		mAllRoomOn.setSrcNonChecked(R.drawable.btn_on_icon);
		mAllRoomOn.setOnCheckChangedListener(this);

		mAllRoomOff.setOnClickListener(this);
		mAllRoomOff.setSrcCheched(R.drawable.btn_off_icon);
		mAllRoomOff.setSrcNonChecked(R.drawable.btn_off_icon);
		mAllRoomOff.setOnCheckChangedListener(this);
		
		//getAudio();
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
		inflater.inflate(R.menu.my_device_menu, menu);
		mMenu = menu;
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				onClickHome();
				break;
			case R.id.speak_Menu:
				mIsSpeaking = true;
				resetMenu();
				mContext.controlSpeech();
				return true;

			case R.id.stop_speak_Menu:
				mIsSpeaking = false;
				resetMenu();
				return true;
			}
		}
		return true;
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub
		//getAudio();
	}

	@Override
	public void onPageDeselected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgOn:
			((ScheduleImageView) v).toggle();
			break;
		case R.id.imgOff:
			((ScheduleImageView) v).toggle();
			break;
		default:
			break;
		}
	}

	private void onOffAudio(boolean isChecked) {
		Log.d(TAG, "onOffAudio");
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, mAudioAll.getRoom().getId());
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);
		if (isChecked) {
			bundle.putInt(configManager.DEVICE_ID,
					mSpinInput.getSelectedItemPosition() + 1);
		} else {
			bundle.putInt(configManager.DEVICE_ID, 0);
		}
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_AUDIO;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	private void getAudio() {
		Message message = Message.obtain();
		message.what = HCRequest.REQUEST_GET_AUDIO;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void audioGot(int[] audios) {	
			mAudioArray = audios;		
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				boolean isOpen = isInputSpinnerOpening();				
				if(isOpen || mAudioArray == null){
					return ;
				}
				int size = mAudioHC.size();
				AudioHC audio = null;
				boolean isCheckedAll = true;
				int inputAll = 1;
				if (size > 0) {
					inputAll = mAudioArray[0];
				}
				for (int i = 0; i < size; i++) {
					audio = mAudioHC.get(i);
					audio.setInput(mAudioArray[i]);
					if (inputAll != mAudioArray[i]) {
						isCheckedAll = false;
					}
				}
				if (isCheckedAll == true) {
					mAudioAll.setInput(inputAll);
				} else {
					mAudioAll.setInput(mAudioArray[0]);
				}
				mAudioAll.setState(isCheckedAll);
				
				mAllRoomOn.setOnCheckChangedListener(null);
				mAllRoomOn.setChecked(isCheckedAll);
				mAllRoomOn.setOnCheckChangedListener(MyAudioSystemScreen.this);
				
				mAllRoomOff.setOnCheckChangedListener(null);
				mAllRoomOff.setChecked(!isCheckedAll);
				mAllRoomOff.setOnCheckChangedListener(MyAudioSystemScreen.this);
				
				mAdpater = null;
				mAdpater = new MyAudioAdapter(MyAudioSystemScreen.this,mContext, mAudioHC);
				mListView.setAdapter(mAdpater);
			}
		});

	}

	@Override
	public void onCheckChanged(View view, boolean isChecked) {
		switch (view.getId()) {
		case R.id.imgOn:
			onOffAudio(true);	
			break;
		case R.id.imgOff:
			onOffAudio(false);	
			break;
		default:
			break;
		}
		
	}
	
	
	public boolean isInputSpinnerOpening(){
		boolean isOpen= false;
		int size = mAdpater != null ? mAdpater.getCount(): 0;
		MyAudioItem item  = null;
		View view = null;
		for(int i = 0; i <size ; i++){
			view = getViewByPosition(i, mListView);
			if(view != null){
				item  = (MyAudioItem) view.getTag();
				if(item!= null && item.spinInput!= null){					
					if(item.spinInput.hasBeenOpened()){						
						isOpen = true;
						break;
					}
					
				}
			}
		}
		return isOpen;
	}
	
	public View getViewByPosition(int pos, ListView listView) {
	    final int firstListItemPosition = listView.getFirstVisiblePosition();
	    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

	    if (pos < firstListItemPosition || pos > lastListItemPosition ) {
	        return listView.getAdapter().getView(pos, null, listView);
	    } else {
	        final int childIndex = pos - firstListItemPosition;
	        return listView.getChildAt(childIndex);
	    }
	}
	
	////////////////////////speak

	private void resetMenu() {
		if (mMenu.size() >= 2) {
			MenuItem itemSpeak = mMenu.findItem(R.id.speak_Menu);
			MenuItem itemStop = mMenu.findItem(R.id.stop_speak_Menu);
			if (mIsSpeaking) {
				itemSpeak.setVisible(false);
				itemStop.setVisible(true);
			} else {
				itemSpeak.setVisible(true);
				itemStop.setVisible(false);
			}
		}
	}

	public void onOffBySpeak(int position) {
		Toast.makeText(mContext, "Speak : " + position, Toast.LENGTH_SHORT)
				.show();
		Log.d("TMT", "speak : " + position);
		switch (position) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:		
			AudioHC device = mAudioHC.get(position -1);
			onOffAudio(device, true, position -1);
			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:		
			AudioHC deviceOff = mAudioHC.get(position -10 - 1);
			onOffAudio(deviceOff, false, position -10 - 1);			
			break;
		case 0:
			onOffAudio(true);
			break;
		case 21:
			onOffAudio(false);			
		default:

			break;
		}
	}
	
	private void onOffAudio(AudioHC device, boolean isChecked, int position) {
		Log.d(TAG, "onOffAudio: position in adapter: " + position);
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoom().getId());
		bundle.putBoolean(configManager.ON_OFF_ACTION, isChecked);
		if (isChecked) {
			bundle.putInt(configManager.DEVICE_ID, position + 1);
		} else {
			bundle.putInt(configManager.DEVICE_ID, 0);
		}
		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_AUDIO;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	public int[] getAudioArray(){
		return mAudioArray;
	}
}
