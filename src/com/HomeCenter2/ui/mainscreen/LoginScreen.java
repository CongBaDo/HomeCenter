package com.HomeCenter2.ui.mainscreen;

import java.net.Socket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.HomeCenter2.HCRequest;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.data.Password;
import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.ui.DialogFragmentWrapper;
import com.HomeCenter2.ui.listener.ConnectSocketListener;
import com.HomeCenter2.ui.listener.LoginListener;

public class LoginScreen extends Fragment implements View.OnClickListener,
		OnCheckedChangeListener, LoginListener, ConnectSocketListener,
		DialogFragmentWrapper.OnCreateDialogFragmentListener {

	public static final String TAG = "LoginActivity";
	LinearLayout mLayoutAppName;
	EditText mNameEdt, mPasswordEdt, mSeverNameRemoteEdt, mPortNameRemoteEdt,
			mServerNameLocalEdt, mPortNameLocalEdt;
	ImageButton mHelpIbtn;
	CheckBox mShowPasswordCb, mcbRemote, mcbLocal;
	Dialog mDialog = null, mDialogChangeServer = null;
	View mServerDialogView;
	LayoutInflater mLayoutInflater = null;
	private boolean isNeedLogin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreateContentView");

		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				uiEngine.addLoginObserver(this);
				uiEngine.addConnectSocketObserver(this);
				//nganguyen fix log in
//				Socket socket = uiEngine.getSocket().getSocket();
//				if (socket != null && !socket.isConnected()) {
//					uiEngine.startSocket();
//				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		View view = (View) inflater.inflate(R.layout.login_screen, null);
		mLayoutAppName = (LinearLayout) view.findViewById(R.id.layout_app_name);
		Button btn = (Button) view.findViewById(R.id.btnConnect);
		btn.setOnClickListener(this);

		mNameEdt = (EditText) view.findViewById(R.id.editUserNameLogin);
		mPasswordEdt = (EditText) view.findViewById(R.id.editPasswordLogin);
		mShowPasswordCb = (CheckBox) view.findViewById(R.id.cbShowPassword);
		mShowPasswordCb.setOnCheckedChangeListener(this);

		ImageButton helpIbtn = (ImageButton) view.findViewById(R.id.ibtnHelp);
		helpIbtn.setOnClickListener(this);
		return view;
	}

	private void doLogin() {
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				Socket socket = uiEngine.getSocket().getSocket();
				if (socket == null || !socket.isConnected()) {
					uiEngine.startSocket();
				} else {
					login();
				}
			}
		}
	}

	private void login() {
		Password ps = new Password();
		ps.setName(mNameEdt.getText().toString());
		ps.setPassword(mPasswordEdt.getText().toString());
		Bundle bundle = new Bundle();
		bundle.putSerializable(configManager.PASSWORD_BUNDLE, ps);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_LOGIN_ACTION;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConnect:
			DialogFragmentWrapper.showDialog(this.getFragmentManager(),
					LoginScreen.this, configManager.DIALOG_PROCESS);
			/*
			 * Password ps = new Password(); String name =
			 * mNameEdt.getText().toString(); String pw =
			 * mPasswordEdt.getText().toString(); Log.d(TAG, "name:" + name +
			 * ", pw: " + pw); ps.setName(mNameEdt.getText().toString());
			 * ps.setPassword(mPasswordEdt.getText().toString());
			 */

			doLogin();
			break;
		case R.id.ibtnHelp:
			DialogFragmentWrapper.showDialog(this.getFragmentManager(),
					LoginScreen.this, configManager.DIALOG_SERVER_LOGIN);
			break;
		default:
			break;
		}
	}

	private Dialog showFailLoginDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.warning)
				.setMessage(R.string.login_fail)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		return mDialog;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cbRemote:
			setRemoteChecked(isChecked);
			break;
		case R.id.cbLocal:
			setRemoteChecked(!isChecked);
			break;
		default:
			if (isChecked == true) {
				mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			} else {
				mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.left_to_right_in_enter);
		mLayoutAppName.startAnimation(animation);
	}

	@Override
	public void eventLogined(final boolean isConnected) {
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "eventLogined");
				DialogFragmentWrapper.removeDialog(getFragmentManager(),
						configManager.DIALOG_PROCESS);
				if (!isConnected) {
					DialogFragmentWrapper.showDialog(getFragmentManager(),
							LoginScreen.this, configManager.DIALOG_FAIL_LOGIN);
				} else {
					getActivity().finish();
				}
			}
		});
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case configManager.DIALOG_PROCESS:
			Dialog dialog = createProgressDialog();
			//dialog.setRetainInstance(true);

			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		case configManager.DIALOG_FAIL_LOGIN:
			return showFailLoginDialog();
		case configManager.DIALOG_SERVER_LOGIN:
			return showServerDialog();
		case configManager.DIALOG_FAIL_CONNECT_SOCKET:
			return showFailServiceDialog();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RegisterService service = RegisterService.getService();
		if (service != null) {
			HomeCenterUIEngine uiEngine = service.getUIEngine();
			if (uiEngine != null) {
				uiEngine.removeLoginObserver(this);
				uiEngine.removeConnectSocketObserver(this);
				if (!uiEngine.isLogined()) {
					uiEngine.closeSocket();
				}
			}
		}
		if (mDialogChangeServer != null) {
			mDialogChangeServer.dismiss();
			mDialogChangeServer = null;
		}
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	private Dialog createProgressDialog() {
		AlertDialog dialog = null;
		dialog = new ProgressDialog(getActivity());
		if (dialog != null) {
			dialog.setMessage(getString(R.string.login_progress));
			((ProgressDialog) dialog)
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		return dialog;
	}

	private Dialog showServerDialog() {
		if (mDialogChangeServer != null) {
			mDialogChangeServer.dismiss();
		}
		mServerDialogView = mLayoutInflater.inflate(
				R.layout.server_login_dialog, null);
		mSeverNameRemoteEdt = (EditText) mServerDialogView
				.findViewById(R.id.edtServerRemote);
		mPortNameRemoteEdt = (EditText) mServerDialogView
				.findViewById(R.id.edtPortRemote);

		mServerNameLocalEdt = (EditText) mServerDialogView
				.findViewById(R.id.edtServerLocal);
		mPortNameLocalEdt = (EditText) mServerDialogView
				.findViewById(R.id.edtPortLocal);

		mcbLocal = (CheckBox) mServerDialogView.findViewById(R.id.cbLocal);
		mcbLocal.setOnCheckedChangeListener(this);
		
		mcbRemote = (CheckBox) mServerDialogView.findViewById(R.id.cbRemote);
		mcbRemote.setOnCheckedChangeListener(this);

		Bundle bundle = XMLHelper.readConfigFileXml();
		if (bundle != null) {
			String value;
			value = bundle.getString(configManager.SERVER_REMOTE);
			if (!TextUtils.isEmpty(value)) {
				mSeverNameRemoteEdt.setText(value);
			}

			int port = bundle.getInt(configManager.PORT_REMOTE, -1);
			if (port > 0) {
				mPortNameRemoteEdt.setText(String.valueOf(port));
			}

			value = bundle.getString(configManager.SERVER_LOCAL);
			if (!TextUtils.isEmpty(value)) {
				mServerNameLocalEdt.setText(value);
			}

			port = bundle.getInt(configManager.PORT_LOCAL, -1);
			if (port > 0) {
				mPortNameLocalEdt.setText(String.valueOf(port));
			}

			port = bundle.getInt(configManager.IP_TYPE, -1);
			boolean isRemote = (port > 0 ? true : false);
			setRemoteChecked(isRemote);
		}

		mDialogChangeServer = new AlertDialog.Builder(getActivity())
				.setView(mServerDialogView).setTitle(R.string.server)
				.setPositiveButton(R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogFragmentWrapper.showDialog(
								LoginScreen.this.getFragmentManager(),
								LoginScreen.this, configManager.DIALOG_PROCESS);
						doChangeService();
					}
				}).setNegativeButton(R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialogChangeServer.dismiss();
					}
				}).create();

		return mDialogChangeServer;
	}

	private Dialog showFailServiceDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.warning)
				.setMessage(R.string.server_fail)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								if (mDialogChangeServer != null) {
									mDialogChangeServer.show();
								}
							}
						}).create();
		return mDialog;
	}

	private void doChangeService() {
		Bundle bundle = new Bundle();

		// save remote
		String value = mSeverNameRemoteEdt.getText().toString();
		if (!TextUtils.isEmpty(value)) {
			bundle.putString(configManager.SERVER_REMOTE, value);
		}
		value = mPortNameRemoteEdt.getText().toString();
		int portValue = -1;
		if (!TextUtils.isEmpty(value)) {
			portValue = Integer.valueOf(value);
		}
		if (portValue > 0) {
			bundle.putInt(configManager.PORT_REMOTE, portValue);
		}

		// save local
		value = mServerNameLocalEdt.getText().toString();
		if (!TextUtils.isEmpty(value)) {
			bundle.putString(configManager.SERVER_LOCAL, value);
		}
		value = mPortNameLocalEdt.getText().toString();
		portValue = -1;
		if (!TextUtils.isEmpty(value)) {
			portValue = Integer.valueOf(value);
		}
		if (portValue > 0) {
			bundle.putInt(configManager.PORT_LOCAL, portValue);
		}

		// type
		boolean isRemote = mcbRemote.isChecked();
		bundle.putInt(configManager.IP_TYPE, isRemote ? 1 : 0);

		Message message = Message.obtain();
		message.setData(bundle);
		message.what = HCRequest.REQUEST_CHANGE_SERVICE_ADDRESS;
		RegisterService.getHomeCenterUIEngine().sendMessage(message);

	}

	@Override
	public void socketConnected(final boolean isConnected) {
		Log.d(TAG, "socketConnected");
		this.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (mDialogChangeServer != null) {
					if (!isConnected) {
						DialogFragmentWrapper.removeDialog(
								getFragmentManager(),
								configManager.DIALOG_PROCESS);
						DialogFragmentWrapper.showDialog(getFragmentManager(),
								LoginScreen.this,
								configManager.DIALOG_FAIL_CONNECT_SOCKET);
					} else {
						mDialogChangeServer.dismiss();
						mDialogChangeServer = null;
						if (isNeedLogin) {
							login();
						} else {
							DialogFragmentWrapper.removeDialog(
									getFragmentManager(),
									configManager.DIALOG_PROCESS);
						}
					}
				} else {
					login();
				}

			}
		});

	}

	public void setRemoteChecked(boolean isRemote) {
		mcbLocal.setChecked(!isRemote);
		mcbRemote.setChecked(isRemote);
	}
}
