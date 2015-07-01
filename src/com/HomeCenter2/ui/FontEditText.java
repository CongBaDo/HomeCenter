package com.HomeCenter2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.HomeCenter2.HomeCenter2Activity;

public class FontEditText extends EditText {

	public FontEditText(Context context) {
		super(context);
		initFont(context);
	}

	public FontEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFont(context);
	}

	public FontEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFont(context);
	}

	private void initFont(Context context) {
		HomeCenter2Activity.initFont(context, this);
	}
}
