package com.HomeCenter2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.HomeCenter2.data.configManager;
import com.HomeCenter2.ui.mainscreen.LoginScreen;

public class LoginActivity extends ActionBarActivity  {
	
	public static final String TAG = "LoginActivity";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreateContentView");
		setContentView(R.layout.login_activity);
		LoginScreen fragment = new LoginScreen();
		getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, fragment).commit();
		getActionBar().hide();
	}

}
