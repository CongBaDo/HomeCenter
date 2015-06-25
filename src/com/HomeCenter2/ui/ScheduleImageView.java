package com.HomeCenter2.ui;

import com.HomeCenter2.R;

import android.content.Context;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ScheduleImageView extends ImageView implements OnClickListener{
	
	private static final String TAG = "ScheduleImageView";
	private boolean isChecked = false;
	private onCheckChangedListener mListener = null;	

	private int srcCheched;
	private int srcNonChecked;
	private int drawableChecked, drawableNonChecked;//nganguyen added

	public onCheckChangedListener getmListener() {
		return mListener;
	}

	public void setmListener(onCheckChangedListener mListener) {
		this.mListener = mListener;
	}


	public void setSrcCheched(int srcCheched) {
		this.srcCheched = srcCheched;
	}
	
	public void setSrcNonChecked(int srcNonChecked) {
		this.srcNonChecked = srcNonChecked;
	}
	
	public void setSrcCheched(int srcCheched, int srcDrawable) {
		this.drawableChecked = srcDrawable;
		this.srcCheched = srcCheched;
	}
	
	public void setSrcNonChecked(int srcNonChecked, int srcDrawable) {
		this.drawableNonChecked = srcDrawable;
		this.srcNonChecked = srcNonChecked;
	}
	

	public ScheduleImageView(Context context) {
		super(context);		
	}

	public ScheduleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void toggle(){
		setChecked(!this.isChecked);
	}

	public ScheduleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onClick(View v) {		
		
	}
	
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {

		this.isChecked = isChecked;
		if(isChecked){
			this.setImageResource(srcCheched);//(drawableChecked);
		}else{
			this.setImageResource(srcNonChecked);//(drawableNonChecked);
		}
		if(mListener != null){
			mListener.onCheckChanged(this, isChecked);
		}
	}
	
	public void setOnCheckChangedListener(onCheckChangedListener listener){
		mListener = listener;
	}
	
	public interface onCheckChangedListener{
		public void onCheckChanged(View view, boolean isChecked);
	}
	
	
}