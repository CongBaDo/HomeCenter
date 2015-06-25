package com.HomeCenter2.ui.menuscreen;

import android.util.Log;

import com.HomeCenter2.R;
import com.HomeCenter2.ui.slidingmenu.framework.ActionBar.MenuCallBack;
import com.HomeCenter2.ui.slidingmenu.framework.RADialerMenuScreenAbstract;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class XMLDevicesMenuScreen extends RADialerMenuScreenAbstract {

	public XMLDevicesMenuScreen(int title,  String tag, SlidingBaseActivity context) {
		super(XMLDevicesMenuScreen.class, title, tag, context);
		setRetainInstance(true);
	}

	public XMLDevices mSettingDevice = null;
	@Override
	protected void setContentFromFragment() {
		mSettingDevice = new XMLDevices(mContext);
		setContentForScreen(mSettingDevice);
	}

	@Override
	public void loadHeader() {
		//super.createHeader(R.layout.screen_header_one_right_icon);

	}

	@Override
	public void setContentMenu() {
		/*super.setHomeAction(new MenuCallBack() {
			@Override
			public void doAction() {				
				popBackStack();				
			}
		});		
		super.mActionBar.setMenuImageViewCallBack(R.id.img_right_icon,
				R.drawable.add_icon, new MenuCallBack() {

					@Override
					public void doAction() {
						Log.d("TMT SettingDeviceScreen", "doAction");
						mSettingDevice.saveSetting();
					}
			
		});*/
	}

	@Override
	public void onScreenSlidingCompleted() {
		// TODO Auto-generated method stub

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
