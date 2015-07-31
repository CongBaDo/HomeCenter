package com.HomeCenter2.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.HomeCenter2.R;

public class ImageProcessDialog implements OnClickListener {

	public enum ACTION {TAKE_PHOTO, USE_LIBRARY, EDIT_DEVICE, DELETE_IMAGE, CANCEL};
	
	public static final int TYPE_BLANK = 100;
	public static final int TYPE_EXIST = 101;
	private static final String TAG = "ImageProcessDialog";
	private Activity context;
	private Dialog dialog;
	private int SCW, SCH;
	private String title, message, subject;
	private int type;

	public interface ImageDialogListener {
		public void dismissListener();
		public void shareCallback(ACTION shareType);
	}
	
	private ImageDialogListener callback;

	public ImageProcessDialog(Activity context, ImageDialogListener callback) {
		this.context = context;
		this.callback = callback;
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		SCH = metrics.heightPixels;
		SCW = metrics.widthPixels;
	}
	
	public void setContent(String message, String subject){
		this.subject = subject;
		this.message = message;
	}

	public void showRadialDialog(int type) {

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View v = mInflater.inflate(R.layout.dialog_process_image, null, false);
		dialog.setContentView(v);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView takePhoto = (TextView)v.findViewById(R.id.tv_take_photo);
		TextView library = (TextView) v.findViewById(R.id.tv_library);
		TextView editDevice = (TextView) v.findViewById(R.id.tv_edit_device);
		TextView deleteImage = (TextView) v.findViewById(R.id.tv_delete_image);
		TextView cancel = (TextView) v.findViewById(R.id.tv_cancel);
		
		if(type == TYPE_BLANK){
			v.findViewById(R.id.contain_full_case).setVisibility(View.GONE);
		}

		takePhoto.setOnClickListener(this);
		library.setOnClickListener(this);
		editDevice.setOnClickListener(this);
		deleteImage.setOnClickListener(this);
		cancel.setOnClickListener(this);

		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

		wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
		dialog.show();
		loadAnimation(v);

	}

	@SuppressLint("NewApi")
	private void loadAnimation(View mainV) {
		if (mainV == null) {
			return;
		}
		mainV.setAlpha(1f);
		float h = mainV.getHeight();
		float w = mainV.getWidth();
		float x = mainV.getX();
		float y = mainV.getY();

		mainV.setY(h);

		ViewPropertyAnimator vpa = mainV.animate().x(x).y(y);

		vpa.setDuration(500);
		vpa.setInterpolator(new OvershootInterpolator());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_library:
			dialog.dismiss();
			callback.shareCallback(ACTION.USE_LIBRARY);
			break;

		case R.id.tv_edit_device:
			dialog.dismiss();
			callback.shareCallback(ACTION.EDIT_DEVICE);
			break;

		case R.id.tv_delete_image:
			callback.shareCallback(ACTION.DELETE_IMAGE);
			dialog.dismiss();
			break;
			
		case R.id.tv_take_photo:
			callback.shareCallback(ACTION.TAKE_PHOTO);
			dialog.dismiss();

		case R.id.tv_cancel:
			dialog.dismiss();
			break;

		default:
			break;
		}
	}
	
	
}
