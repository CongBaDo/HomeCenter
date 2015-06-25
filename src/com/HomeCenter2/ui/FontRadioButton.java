package com.HomeCenter2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.HomeCenter2.HomeCenter2;

public class FontRadioButton extends RadioButton {

	public FontRadioButton(Context context) {
		super(context);
		initFont(context);
	}

	public FontRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFont(context);
	}

	public FontRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFont(context);
	}

	private void initFont(Context context) {
		HomeCenter2.initFont(context, this);
	}
}
