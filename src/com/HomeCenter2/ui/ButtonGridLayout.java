package com.HomeCenter2.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.HomeCenter2.R;

/**
 * Create a 4x3 grid of dial buttons.
 * 
 * It was easier and more efficient to do it this way than use standard layouts.
 * It's perfectly fine (and actually encouraged) to use custom layouts rather
 * than piling up standard layouts.
 * 
 * The horizontal and vertical spacings between buttons are controlled by the
 * amount of padding (attributes on the ButtonGridLayout element): - horizontal
 * = left + right padding and - vertical = top + bottom padding.
 * 
 * This class assumes that all the buttons have the same size. The buttons will
 * be bottom aligned in their view on layout.
 * 
 * Invocation: onMeasure is called first by the framework to know our size. Then
 * onLayout is invoked to layout the buttons.
 */

// TODO: Blindly layout the buttons w/o checking if we overrun the
// bottom-right corner.

public class ButtonGridLayout extends ViewGroup {
	static private final String TAG = "ButtonGridLayout";
	static private final int COLUMNS = 7;
	static private final int ROWS = 1;
	// static private final int NUM_CHILDREN = ROWS * COLUMNS;
	static private final int NUM_CHILDREN = 7;

	private View[] mButtons = new View[NUM_CHILDREN];

	// This what the fields represent (height is similar):
	// PL: mPaddingLeft
	// BW: mButtonWidth
	// PR: mPaddingRight
	//
	// mWidthInc
	// <-------------------->
	// PL BW PR
	// <----><--------><---->
	// --------
	// | |
	// | button |
	// | |
	// --------
	//
	// We assume mPaddingLeft == mPaddingRight == 1/2 padding between
	// buttons.
	//
	// mWidth == COLUMNS x mWidthInc

	// Width and height of a button
	private int mButtonWidth;
	private int mButtonHeight;

	// Width and height of a button + padding.
	private int mWidthInc;
	private int mHeightInc;

	// Height of the dialpad. Used to align it at the bottom of the
	// view.
	private int mWidth;
	private int mHeight;
	private int mButtonMargin;

	public ButtonGridLayout(Context context) {
		super(context);
	}

	public ButtonGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ButtonGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Cache the buttons in a member array for faster access. Compute the
	 * measurements for the width/height of buttons. The inflate sequence is
	 * called right after the constructor and before the measure/layout phase.
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		final View[] buttons = mButtons;
		for (int i = 0; i < NUM_CHILDREN; i++) {
			buttons[i] = getChildAt(i);
			// Measure the button to get initialized.
			buttons[i]
					.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			buttons[i].setSoundEffectsEnabled(false);
		}

		// Cache the measurements.
		final View child = buttons[0];
		mButtonWidth = child.getMeasuredWidth();
		mButtonHeight = child.getMeasuredHeight();
		mWidthInc = mButtonWidth + getPaddingLeft() + getPaddingRight();
		mHeightInc = mButtonHeight + getPaddingTop() + getPaddingBottom();
		mWidth = COLUMNS * mWidthInc;
		mHeight = ROWS * mButtonHeight + (ROWS + 1) * getPaddingBottom();
	}

	/**
	 * Set the background of all the children. Typically a selector to change
	 * the background based on some combination of the button's attributes (e.g
	 * pressed, enabled...)
	 * 
	 * @param resid
	 *            Is a resource id to be used for each button's background.
	 */
	public void setChildrenBackgroundResource(int resid) {
		final View[] buttons = mButtons;
		for (int i = 0; i < NUM_CHILDREN; i++) {
			buttons[i].setBackgroundResource(resid);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final View[] buttons = mButtons;
		final int buttonWidth = mButtonWidth;
		final int buttonHeight = mButtonHeight;
		final int widthInc = mWidthInc;
		final int heightInc = mHeightInc;

		int i = 0;
		// The last row is bottom aligned.
		int y = mButtonMargin * 2;
		for (int row = 0; row < ROWS; row++) {
			int x = mButtonMargin;
			for (int col = 0; col < COLUMNS; col++) {
				buttons[i].layout(x, y, x + buttonWidth, y + buttonHeight);
				x += widthInc;
				i++;
				if (i >= NUM_CHILDREN) {
					break;
				}
			}
			y += heightInc;
			if (i >= NUM_CHILDREN) {
				break;
			}
		}
	}

	/**
	 * This method is called twice in practice. The first time both with and
	 * height are constraint by AT_MOST. The second time, the width is still
	 * AT_MOST and the height is EXACTLY. Either way the full width/height
	 * should be in mWidth and mHeight and we use 'resolveSize' to do the right
	 * thing.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(mWidth, widthMeasureSpec);
		final int height = resolveSize(mHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	public void setKeypadButtonWidthAndHeight(int screenWidth, int screenHeight) {
		Resources res = getResources();

		int headerHeight = res
				.getDimensionPixelSize(R.dimen.header_menu_height);
		int callDigitsHeight = res
				.getDimensionPixelSize(R.dimen.keypad_number_height);
		int callBtnHeight = res
				.getDimensionPixelSize(R.dimen.call_button_height);
		mButtonMargin = res.getDimensionPixelSize(R.dimen.ic_number_margin);

		mWidth = screenWidth - mButtonMargin;
		mHeight = screenHeight - headerHeight - callDigitsHeight
				- callBtnHeight - mButtonMargin * 4;

		mWidthInc = Math.round((float) mWidth / (float) COLUMNS);
		mHeightInc = Math.round((float) mHeight / (float) ROWS);
		mButtonWidth = mWidthInc - mButtonMargin;
		mButtonHeight = (mHeightInc+ (mButtonMargin * 2)) - mButtonMargin * 4; //TMT test
		// setDialPadBackgroundResource();
		invalidate();
	}

	public void setButtonWidthAndHeight(int width, int height) {
		final View[] buttons = mButtons;
		final View child = buttons[0];
		mButtonWidth = width;
		if (mButtonWidth == LayoutParams.FILL_PARENT
				|| mButtonWidth == LayoutParams.WRAP_CONTENT
				|| mButtonWidth == LayoutParams.MATCH_PARENT) {
			mButtonWidth = child.getMeasuredWidth();
		}
		mButtonHeight = height;
		if (mButtonHeight == LayoutParams.FILL_PARENT
				|| mButtonHeight == LayoutParams.WRAP_CONTENT
				|| mButtonHeight == LayoutParams.MATCH_PARENT) {
			mButtonHeight = child.getMeasuredHeight();
		}
		mButtonMargin = getPaddingLeft();
		mWidthInc = mButtonWidth + mButtonMargin;
		mHeightInc = mButtonHeight + getPaddingTop();
		mWidth = COLUMNS * mWidthInc;
		mHeight = ROWS * mButtonHeight + (ROWS + 1) * getPaddingBottom();	
		invalidate();
	}

	public int getButtonWidth() {
		return mButtonWidth;
	}
}
