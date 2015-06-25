package com.HomeCenter2.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.ui.adapter.RoomMenuAdapter;

public class DialogRoomMenu {
	public static final int CHANGE_NAME_DIALOG = 0;
	private static Dialog mDialog = null;

	public static Dialog showContentDeviceDialog(Context context,
			LayoutInflater inflater,final RoomMenuAdapter mRoomDropDownAdapter) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		ListView lv= (ListView)inflater.inflate(R.layout.room_menu_dialog, null);
		lv.setAdapter(mRoomDropDownAdapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		if(mRoomDropDownAdapter== null)
			return null;
		mDialog = new AlertDialog.Builder(context)
				.setTitle(R.string.room)			
				.setSingleChoiceItems(mRoomDropDownAdapter,mRoomDropDownAdapter.getSelected(), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {						
						HomeCenterUIEngine uiEngine = RegisterService.getHomeCenterUIEngine();
						if(uiEngine!= null){
							uiEngine.setRoomCurrentIndex(which);
						}
						mRoomDropDownAdapter.setSelected(uiEngine.getRoomCurrentIndex());
						mDialog.dismiss();
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
