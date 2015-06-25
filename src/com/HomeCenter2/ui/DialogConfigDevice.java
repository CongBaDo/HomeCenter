package com.HomeCenter2.ui;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Cock;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.Fan;
import com.HomeCenter2.house.Fridge;
import com.HomeCenter2.house.Lamp;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.PlugDevice;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.ui.adapter.MyDeviceTypeAdapter;
import com.HomeCenter2.ui.mainS.MyDevicesScreen;

public class DialogConfigDevice {

	public static final String TAG = "TMT DialogConfigDevice";
	private static Dialog mDialog = null;
	private static EditText mNameEdit;
	private static Device mDevice;
	private static Room mRoom;
	private static Fragment mFragment;
	private static ListView mAllAppsLV;
	private static MyDeviceTypeAdapter mAppsAdapter;
	private static ImageView mIcon;
	private static LinearLayout mLinear = null;
	private static int mPosition = 0;	

	public static Dialog showContentDeviceDialog(Fragment fragment,
			Context context, LayoutInflater inflater, Room room, Device device) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mFragment = fragment;

		mRoom = room;
		mDevice = device;
		View view = inflater.inflate(R.layout.add_device_dialog, null);
		
		mNameEdit = (EditText) view.findViewById(R.id.editNameAddDevice);
		mNameEdit.setText(mDevice.getName());

		mIcon = (ImageView) view.findViewById(R.id.imgRoom);
		mIcon.setImageResource(mDevice.getIcon());
		
		mAllAppsLV = (ListView) view.findViewById(R.id.lvDevice);
		mLinear = (LinearLayout)view.findViewById(R.id.linearDevices);
		if (device instanceof LampRoot || device instanceof RollerShutter) {
			
			mLinear.setVisibility(View.VISIBLE);
			HomeCenterUIEngine uiEngine = RegisterService
					.getHomeCenterUIEngine();
			mAppsAdapter = new MyDeviceTypeAdapter(context,
					uiEngine.getDeviceTypes());
			mAllAppsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mAllAppsLV.setAdapter(mAppsAdapter);
			int pos = getCurrentType(mDevice);
			mPosition = pos;
			mAllAppsLV.setItemChecked(pos, true);

			mAllAppsLV.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					mPosition = position;
					resetDeviceType(false);
				}
			});
		}else{
			mLinear.setVisibility(View.GONE);
		}
		mDialog = new AlertDialog.Builder(context)
				.setTitle(R.string.device)
				.setView(view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// device.setActiveTemp(true);
								// setEditImage(true);
								if (mFragment instanceof MyDevicesScreen){
									resetDeviceType(true);
								}

								Bundle bundle = new Bundle();
								String name = mNameEdit.getText().toString();								
								if (!TextUtils.isEmpty(name)) {
									mDevice.setName(name);									
									bundle.putInt( "id", mDevice.getId());
									bundle.putString(configManager.NAME_BUNDLE , name);									

									if (mDevice instanceof Lamp) {
										bundle.putInt("TypeDevice",
												configManager.LAMP);
									} else if (mDevice instanceof Fan) {
										bundle.putInt("TypeDevice",
												configManager.FAN);										
									}  else if (mDevice instanceof Cock) {
										bundle.putInt("TypeDevice",
												configManager.COCK);
									}else if (mDevice instanceof Fridge) {
										bundle.putInt("TypeDevice",
												configManager.FRIDGE);
									} else if (mDevice instanceof PlugDevice) {
										bundle.putInt("TypeDevice",
												configManager.PLUG_DEVICE);
									}

									// mNameTV.setText(name);
									RegisterService service = RegisterService
											.getService();
									if (service == null)
										return;
									HomeCenterUIEngine uiEngine = service
											.getUIEngine();
									if (uiEngine == null) {
										return;
									}
									
									List<Device> devices = mRoom.getDevices();
									for (int i = 0; i < devices.size(); i++) {
										Device dv = devices.get(i);
										if (dv.getId() == mDevice.getId()) {
											dv.setName(name);
											if (mDevice instanceof Lamp) {
												dv = (Lamp) mDevice;
												devices.set(i, dv);
												break;
											} else if (mDevice instanceof Fan) {
												dv = (Fan) mDevice;
												devices.set(i, dv);
												break;
											} else if (mDevice instanceof Fridge) {
												dv = (Fridge) mDevice;
												devices.set(i, dv);
												break;
											} else if (mDevice instanceof PlugDevice) {
												dv = (PlugDevice) mDevice;
												devices.set(i, dv);
												break;
											}

										}
									}
									
									uiEngine.saveRoom(mRoom);
								}

							/*	if (mFragment instanceof MyDevicesScreen) {
									((MyDevicesScreen) mFragment)
											.saveConfig(bundle);

								}*/ /*
								 * else if (mFragment instanceof
								 * MySensorsScreen) { ((MySensorsScreen)
								 * mFragment) .saveConfig(bundle); }
								 */

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

	private static int getCurrentType(Device device) {
		if (device instanceof Fan) {
			return 1;
		} else if (device instanceof Fridge) {
			return 2;
		} else if (device instanceof PlugDevice) {
			return 3;
		}
		return 0;
	}

	public static void resetDeviceType(boolean isSave) {
		Device device = (Device) mAppsAdapter.getItem(mPosition);
		Device temp = null;
		if (isSave) {
			if (device instanceof Lamp) {
				temp = new Lamp();				 
				((Lamp)temp).copyToDevice((LampRoot) mDevice);
			} else if (device instanceof Fan) {
				temp = new Fan();
				((Fan) temp).copyToDevice((LampRoot) mDevice);

			} else if (device instanceof Cock) {
				temp = new Cock();
				((Cock) temp).copyToDevice((LampRoot) mDevice);

			} else if (device instanceof Fridge) {
				temp = new Fridge();
				((Fridge) temp).copyToDevice((LampRoot) mDevice);

			} else if (device instanceof PlugDevice) {
				temp = new PlugDevice();
				((PlugDevice) temp).copyToDevice((LampRoot) mDevice);

			}			
			if(temp!= null){
				mDevice = temp;
			}
			mIcon.setImageResource(mDevice.getIcon());
			if (mDevice instanceof Lamp) {
				Log.d(TAG, "lamp:");
			} else if (mDevice instanceof Fan) {
				Log.d(TAG, "fan:");
			} else if (mDevice instanceof Fridge) {
				Log.d(TAG, "fridge:");
			} else if (mDevice instanceof PlugDevice) {
				Log.d(TAG, "plugdevice:");
			}
		} else {
			mIcon.setImageResource(device.getIcon());
		}
	}

}
