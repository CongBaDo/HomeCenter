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
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.LampRoot;
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
	private static ImageView mIcon;
	private static LinearLayout mLinear = null;

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

		mLinear = (LinearLayout) view.findViewById(R.id.linearDevices);
		mLinear.setVisibility(View.GONE);

		mDialog = new AlertDialog.Builder(context)
				.setTitle(R.string.device)
				.setView(view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Bundle bundle = new Bundle();
								String name = mNameEdit.getText().toString();
								if (!TextUtils.isEmpty(name)) {
									mDevice.setName(name);
									bundle.putInt("id", mDevice.getId());
									bundle.putString(configManager.NAME_BUNDLE,
											name);
									RegisterService service = RegisterService
											.getService();
									if (service == null)
										return;
									HomeCenterUIEngine uiEngine = service
											.getUIEngine();
									if (uiEngine == null) {
										return;
									}
									uiEngine.saveRoom(mRoom);
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
}
