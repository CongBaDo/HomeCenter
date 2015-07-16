package com.HomeCenter2.ui.mainscreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.HomeScreenSetting;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.customview.CategoryView;
import com.HomeCenter2.customview.HeaderFooterGridView;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.KeyDoorLock;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.adapter.KeyDoorLockAdapter;
import com.HomeCenter2.ui.adapter.KeyDoorLockAdapter.ClickCallback;
import com.HomeCenter2.ui.listener.GetStatusKeyLockListener;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMainScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.ScreenManager;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;
import com.HomeCenter2.utils.FileUtils;
import com.HomeCenter2.utils.HCUtils;
import com.HomeCenter2.utils.ImageProcessDialog;
import com.HomeCenter2.utils.ImageProcessDialog.ACTION;
import com.HomeCenter2.utils.ImageProcessDialog.ImageDialogListener;

public class DetailDoorLockScreen extends RADialerMainScreenAbstract implements
		DialogFragmentWrapper.OnCreateDialogFragmentListener,
		OnItemClickListener, GetStatusKeyLockListener, OnItemLongClickListener,
		OnClickListener {

	private final int CHANGE_TYPE_DIALOG = 0;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	private LayoutInflater mInflater = null;
	private int imageW;

	private enum IMAGE_POS {
		LEFT, RIGHT
	}

	private IMAGE_POS imagePos = IMAGE_POS.LEFT;

	public DetailDoorLockScreen(int title, String tag,
			SlidingBaseActivity context) {
		super(DetailDoorLockScreen.class, title, tag, context);
	}

	public static final String TAG = "TMT DetailDeviceScreen";
	public static Bundle mBundle;
	public static DetailDoorLockScreen m_instance = null;
	public DoorLock mDevice = null;
	public Room mRoom = null;
	private Uri mImageCaptureUri;
	private CategoryView containLeft, containRight;

	ScrollView scrollView = null;

	HeaderFooterGridView mKeyLV;

	KeyDoorLockAdapter mKeyAdapter;

	Dialog mDialog = null;
	private View header;
	private ImageView imgThumbLeft, imgThumbRight;
	private File leftFile, rightFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(mContext);
		Log.d(TAG, "onCreateConentView ");
		mDevice = (DoorLock) mBundle
				.getSerializable(configManager.DEVICE_BUNDLE);
		int roomId = mDevice.getRoomId();
		Log.v(TAG, "onCreateConentView "+mDevice.getName());
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			House mHouse = uiEngine.getHouse();
			if (mHouse != null) {
				mRoom = mHouse.getRoomsById(roomId);
			}
		}
		setHasOptionsMenu(true);
		Log.v(TAG, "onCreateConentView "+mRoom.getName());
		imageW = HomeScreenSetting.ScreenW / 2 - HomeScreenSetting.ScreenW/20;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			uiEngine.addKeyLockObserver(this);
		}

		View view = inflater.inflate(R.layout.detail_door_lock_screen,
				container, false);
		initUI(view, inflater);
		initData();
		return view;
	}
	
	private void initData(){
//		Log.v(TAG, "initData "+)
		
		leftFile = new File(HCUtils.getFilePath(configManager.IMAGE_LEFT, mRoom.getName()));
		rightFile = new File(HCUtils.getFilePath(configManager.IMAGE_RIGHT, mRoom.getName()));
		
		loadBitFromPath(rightFile, IMAGE_POS.RIGHT);
		loadBitFromPath(leftFile, IMAGE_POS.LEFT);
		
		getDetailDoorLock();
	}

	private void initUI(View view, LayoutInflater inflater) {
		mKeyLV = (HeaderFooterGridView) view.findViewById(R.id.lvDoorLock);
		header = inflater.inflate(R.layout.header_room_detail, null);
		mKeyLV.addHeaderView(header);

		mKeyAdapter = new KeyDoorLockAdapter(mContext, mDevice);
		mKeyLV.setAdapter(mKeyAdapter);
		mKeyAdapter.setClickCallback(new ClickCallback() {
			
			@Override
			public void clickPosCallback(int pos) {
				// TODO Auto-generated method stub
				
			}
		});
		mKeyLV.setOnItemClickListener(this);
		mKeyLV.setNumColumns(5);
		mKeyLV.setOnItemLongClickListener(this);

		imgThumbLeft = (ImageView) header
				.findViewById(R.id.image_thumb_left);
		imgThumbRight = (ImageView) header
				.findViewById(R.id.image_thumb_right);
		
		containLeft = (CategoryView)header.findViewById(R.id.contain_left);
		containRight = (CategoryView)header.findViewById(R.id.contain_right);
		
		containLeft.setOnClickListener(this);
		containRight.setOnClickListener(this);

		imgThumbLeft.setOnClickListener(this);
		imgThumbRight.setOnClickListener(this);
	}

	@Override
	public void onScreenSlidingCompleted() {
	}

	@Override
	public void loadHeader() {

	}

	@Override
	public void setContentMenu() {

	}

	public static DetailDoorLockScreen initializeDetailDeviceScreen(
			Bundle bundle, int titleId, SlidingBaseActivity mContext) {
		mBundle = bundle;
		if (m_instance == null) {
			m_instance = new DetailDoorLockScreen(titleId,
					ScreenManager.DETAIL_DEVICE_TAG, mContext);
		}
		return m_instance;
	}

	private void getDetailDoorLock() {
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, mDevice.getRoomId());
		bundle.putInt(configManager.DEVICE_ID, mDevice.getId());

		Log.e(TAG, "getDetailDoorLock::room id: " + mDevice.getRoomId()
				+ " , devId: " + mDevice.getId());

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_GET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
		if (uiEngine != null) {
			uiEngine.removeKeyLockObserver(this);
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		menu.clear();

		mActionBarV7.setHomeButtonEnabled(true);
		mActionBarV7.setDisplayHomeAsUpEnabled(true);
		if (mRoom != null) {
			mActionBarV7.setTitle(mRoom.getName());
		}
		mActionBarV7.setDisplayShowTitleEnabled(true);
		mActionBarV7.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		mActionBarV7.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		inflater.inflate(R.menu.my_detail_doorlock_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected:" + isVisible() + " , is: "
				+ (android.R.id.home == item.getItemId()));
		boolean isSelected = false;
		if (isVisible()) {
			switch (item.getItemId()) {
			case android.R.id.home:
				backToPreviousScreen();
				isSelected = true;
				break;
			case R.id.remote_menu:
				setModeDoorLock(mDevice);
				break;
			}
		}
		return isSelected;
	}

	@Override
	public void onPageSelected() {
		// TODO Auto-generated method stub
		getDetailDoorLock();
		mActionBarV7.setIcon(mDevice.getIcon());

	}

	@Override
	public void onPageDeselected() {
		mActionBarV7.setIcon(R.drawable.ic_protection_totale);
	}

	private void refreshTextColor(TextView tv) {
		boolean isChecked = Boolean.parseBoolean(tv.getTag().toString());
		setTextColor(tv, isChecked);
	}

	private void setTextColor(TextView tv, Boolean isChecked) {
		if (isChecked == true) {
			isChecked = false;
			tv.setTypeface(Typeface.DEFAULT);
			tv.setTextColor(getResources().getColor(R.color.black));

		} else {
			isChecked = true;
			tv.setTypeface(Typeface.DEFAULT_BOLD);
			tv.setTextColor(getResources().getColor(R.color.blue_light));

		}
		tv.setTag(isChecked);

	}

	@Override
	public void onViewScrolledComplete(boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.v(TAG, "onItemClick "+position);
//		 KeyDoorLock key = (KeyDoorLock) mKeyAdapter.getItem(position);
//		 if(key!= null){ 
//			 key.setStatus(!key.isStatus());
//			 setDetailDevice(key,!key.isStatus()); 
//		 }
		mKeyAdapter.setClickPos(position);
		mKeyAdapter.notifyDataSetChanged();
	}

	@Override
	public void recieveKeyLockStatus() {
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mKeyAdapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_CHANGE_ROOM_NAME:
			return showContentDeviceDialog();
		default:
			break;
		}
		return null;
	}

	EditText mPasswordEdit;
	ImageView mKeyImage;

	int mPosition = 0;

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		mPosition = position;
		DialogFragmentWrapper.showDialog(getFragmentManager(), this,
				configManager.DIALOG_CHANGE_ROOM_NAME);
		return true;
	}

	public Dialog showContentDeviceDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		View view = mInflater.inflate(R.layout.add_lock_name_dialog, null);
		mPasswordEdit = (EditText) view.findViewById(R.id.editPasswordKey);
		mPasswordEdit.setText("");

		mKeyImage = (ImageView) view.findViewById(R.id.imgRoom);
		KeyDoorLock mKeyCurrent = (KeyDoorLock) mKeyAdapter.getItem(mPosition);
		mKeyImage.setImageResource(mKeyCurrent.getIcon());

		mDialog = new AlertDialog.Builder(this.getActivity())
				.setTitle(mKeyCurrent.getNameKey())
				.setView(view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String pw = mPasswordEdit.getText().toString();
								if (!TextUtils.isEmpty(pw)) {
									Bundle bundle = new Bundle();
									bundle.putString(
											configManager.PASSWORD_KEY, pw);
									bundle.putString(configManager.NAME, "tmt");
									setPasswordKey(bundle);
									mKeyAdapter.notifyDataSetChanged();
									// save();
									mDialog.dismiss();
								}

							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mDialog.dismiss();
							}
						}).create();
		return mDialog;
	}

	public void saveConfig(Bundle bundle) {
		/*
		 * destroyTask(); HCRequest hc = new HCRequest();
		 * hc.setRequestType(HCRequest.REQUEST_SET_DEVICE_ADDRESS);
		 * mNameRequestTask = new SocketRequestTask(this); if (mNameRequestTask
		 * != null) { mNameRequestTask.setRequest(hc);
		 * mNameRequestTask.setObject(bundle); mNameRequestTask.execute(hc); }
		 */
	}

	private void setModeDoorLock(Device device) {
		Bundle bundle = new Bundle();
		bundle.putInt(configManager.ROOM_ID, device.getRoomId());
		bundle.putString(configManager.MODE_STATUS, configManager.MODE_LEARN_IR);
		bundle.putInt(configManager.DEVICE_ID, 1);

		Log.e(TAG, "getDetailDevice::room id: " + device.getRoomId()
				+ " , devId: " + device.getDeviceId());

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_LOCK_STATUS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	private void setPasswordKey(Bundle bundle) {
		bundle.putInt(configManager.ROOM_ID, mDevice.getRoomId());
		int pos = mPosition + 1;
		bundle.putInt(configManager.DEVICE_ID, pos);
		Log.e(TAG, "getDetailDevice::room id: " + mDevice.getRoomId()
				+ " , devId: " + pos + ", pw:");

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_SET_PASSWORD_KEY;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_thumb_left:
		case R.id.contain_left:
			showDialog(IMAGE_POS.LEFT);
			break;

		case R.id.image_thumb_right:
		case R.id.contain_right:
			showDialog(IMAGE_POS.RIGHT);
			break;

		default:
			break;
		}
	}

	private void showDialog(IMAGE_POS type) {
		imagePos = type;
		ImageProcessDialog dialog = new ImageProcessDialog(getActivity(),
				new ImageDialogListener() {

					@SuppressLint("NewApi")
					@Override
					public void shareCallback(ACTION shareType) {
						// TODO Auto-generated method stub
						if (shareType == ACTION.DELETE_IMAGE) {
							if(imagePos == IMAGE_POS.LEFT){
								FileUtils.deleteFile(leftFile);
							}else{
								FileUtils.deleteFile(rightFile);
							}
							
							resetImageView(imagePos);
						} else if (shareType == ACTION.EDIT_DEVICE) {

						} else if (shareType == ACTION.TAKE_PHOTO) {
							onCallCamera();
						} else if (shareType == ACTION.USE_LIBRARY) {
							onCallGallery();
						}
					}

					@Override
					public void dismissListener() {
						// TODO Auto-generated method stub

					}
				});
		dialog.showRadialDialog();
	}
	
	public void resetImageView(IMAGE_POS type){
		if(type == IMAGE_POS.LEFT){
			imgThumbLeft.setImageBitmap(null);
		}else{
			imgThumbRight.setImageBitmap(null);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_OK)
			return;
		File myDir = new File(configManager.FOLDERNAME + "/"+mRoom.getName().replace(" ", ""));
		String fname = null;//"bbz_avatar.jpg";
		if(this.imagePos == IMAGE_POS.LEFT){
			fname = configManager.IMAGE_LEFT;
			leftFile = new File(myDir, fname);
		}else{
			fname = configManager.IMAGE_RIGHT;
			rightFile = new File(myDir, fname);
		}
		
		myDir.mkdirs();
		File file = new File(myDir, fname);
		if (file.exists()) {
			Log.e(TAG, "Exist");
			file.delete();
		} else {
			Log.v(TAG, "No Exist");
		}

		switch (requestCode) {

		case PICK_FROM_CAMERA:
			mImageCaptureUri = data.getData();
			Bitmap bmAvatar = (Bitmap) data.getExtras().get("data");

			try {
				FileOutputStream out = new FileOutputStream(file);
				bmAvatar.compress(Bitmap.CompressFormat.JPEG, 80, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			loadBitFromPath(file, imagePos);
			break;
		case PICK_FROM_FILE:
			mImageCaptureUri = data.getData();

			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			try {
				bmAvatar = BitmapFactory.decodeStream(
						getActivity().getContentResolver().openInputStream(
								mImageCaptureUri), null, bmpFactoryOptions);
				bmpFactoryOptions.inJustDecodeBounds = false;
				Bitmap bit = BitmapFactory.decodeStream(
						getActivity().getContentResolver().openInputStream(
								mImageCaptureUri), null, bmpFactoryOptions);

				ExifInterface exif = new ExifInterface(FileUtils.getPath(
						getActivity(), mImageCaptureUri));
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 1);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
				} else if (orientation == 3) {
					matrix.postRotate(180);
				} else if (orientation == 8) {
					matrix.postRotate(270);
				}

				bmAvatar = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
						bit.getHeight(), matrix, true);

				FileOutputStream out = new FileOutputStream(file);
				bmAvatar.compress(Bitmap.CompressFormat.JPEG, 80, out);
				out.flush();
				out.close();
				
				loadBitFromPath(file, imagePos);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}
	}
	
	private void loadBitFromPath(File file, IMAGE_POS type){
		FileInputStream fisR;
		try {
			fisR = new FileInputStream(file.getAbsoluteFile());
			
			Bitmap imageBitmapR = BitmapFactory.decodeStream(fisR);
	        
	        BitmapFactory.Options optionsR = new BitmapFactory.Options();
			optionsR.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), optionsR);//decodeResource(getResources(), R.id.myimage, options);
			int imageHeightR = optionsR.outHeight;
			int imageWidthR = optionsR.outWidth;
			
			int imageHR = imageW*imageHeightR/imageWidthR;

	        imageBitmapR = Bitmap.createScaledBitmap(imageBitmapR, imageW , imageHR, false);

	        if (type == IMAGE_POS.LEFT) {
				imgThumbLeft.setImageBitmap(imageBitmapR);
			} else {
				imgThumbRight.setImageBitmap(imageBitmapR);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onCallGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_FROM_FILE);
	}

	private void onCallCamera() {
		Log.e(TAG, "onCallCamera");
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
	}
}
