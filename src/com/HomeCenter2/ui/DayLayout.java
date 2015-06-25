package com.HomeCenter2.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HomeCenter2.R;

public class DayLayout extends LinearLayout implements Checkable {

	private boolean isChecked;
	private boolean isAdded;

	// private List<Checkable> checkableViews;

	public boolean isAdded() {
		return isAdded;
	}

	public DayLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise(attrs);
	}

	public DayLayout(Context context, int checkableId) {
		super(context);
		initialise(null);
	}

	private void initialise(AttributeSet attrs) {
		this.setChecked(false);

	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	public void toggle() {
		setChecked(!this.isChecked);

		/*
		 * for (Checkable c : checkableViews) { c.toggle(); }
		 */
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	private void findCheckableChildren(View v) {
		if (isChecked) {
			if (v instanceof TextView) {
				TextView tv = ((TextView) v);
				tv.setTypeface(Typeface.DEFAULT_BOLD);
				if(isAdded){
					tv.setTextColor(getResources().getColor(R.color.black));
				}else{
					tv.setTextColor(getResources().getColor(R.color.grey));
				}
			}
			if (v instanceof ImageView) {
				v.setBackgroundColor(getResources().getColor(
						android.R.color.holo_blue_bright));
			}

		} else {
			if (v instanceof TextView) {
				TextView tv = ((TextView) v);
				tv.setTypeface(Typeface.DEFAULT);
				tv.setTextColor(getResources().getColor(R.color.grey_light));
			}
			if (v instanceof ImageView) {
				v.setBackgroundColor(getResources().getColor(R.color.grey_light));
				// v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
			}
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
