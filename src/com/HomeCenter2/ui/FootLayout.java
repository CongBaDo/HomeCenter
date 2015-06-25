package com.HomeCenter2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.ui.ScheduleImageView.onCheckChangedListener;

public class FootLayout extends LinearLayout implements Checkable {

	private boolean isChecked;
	private boolean isAdded;
	private onCheckChangedListener mListener = null;

	// private List<Checkable> checkableViews;

	public FootLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise(attrs);
	}

	public FootLayout(Context context, int checkableId) {
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
		if (mListener != null) {
			mListener.onCheckChanged(this, this.isChecked);
		}

	}

	public void toggle() {
		setChecked(!this.isChecked);
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
		if (isChecked) { // open body
			if (v instanceof ImageView) {
				((ImageView) v).setImageResource(R.drawable.ic_action_collapse);
			}
			if (v instanceof TextView) {
				TextView tv = ((TextView) v);
				tv.setVisibility(View.GONE);
			}
		} else { // close body
			if (v instanceof ImageView) {
				((ImageView) v).setImageResource(R.drawable.ic_action_expand);
			}
			if (v instanceof TextView) {
				TextView tv = ((TextView) v);
				tv.setVisibility(View.VISIBLE);
				if (isAdded) {
					tv.setTextColor(getResources().getColor(R.color.black));
				} else {
					tv.setTextColor(getResources().getColor(R.color.grey));
				}
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

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	public void setOnCheckChangedListener(onCheckChangedListener listener) {
		mListener = listener;
	}

	public interface onCheckChangedListener {
		public void onCheckChanged(View view, boolean isChecked);
	}
}
