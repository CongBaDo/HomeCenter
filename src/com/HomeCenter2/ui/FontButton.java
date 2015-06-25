package com.HomeCenter2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.HomeCenter2.HomeCenter2;

public class FontButton extends Button {

	public FontButton(Context context) {
		super(context);
		initFont(context);
	}

	public FontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFont(context);
	}

	public FontButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFont(context);
	}

	private void initFont(Context context) {
		HomeCenter2.initFont(context, this);
	}
}
