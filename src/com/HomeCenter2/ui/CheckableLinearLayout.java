package com.HomeCenter2.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements
		Checkable {
	private boolean isChecked;
	private List<Checkable> checkableViews;
	
	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);		
		initialise(attrs);
	}

	public CheckableLinearLayout(Context context, int checkableId) {
		super(context);
		initialise(null);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {		
		this.isChecked = isChecked;
		for (Checkable c : checkableViews) {
			c.setChecked(isChecked);
		}
	}

	public void toggle() {
		this.isChecked = !this.isChecked;
		for (Checkable c : checkableViews) {
			c.toggle();
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	private void initialise(AttributeSet attrs) {
		this.isChecked = false;
		this.checkableViews = new ArrayList<Checkable>(5);
		
	}

	private void findCheckableChildren(View v) {
		if (v instanceof Checkable) {
			this.checkableViews.add((Checkable) v);
		}

		if (v instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) v;
			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; ++i) {
				findCheckableChildren(vg.getChildAt(i));
			}
		}
	}

}
