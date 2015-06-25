package com.HomeCenter2.ui;

import android.content.Context;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class MyImageView extends ImageView implements OnClickListener{

	public MyImageView(Context context) {
		super(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onClick(View v) {
		// do nothing
	}
}