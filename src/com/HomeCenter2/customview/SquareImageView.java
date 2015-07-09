package com.HomeCenter2.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {
	 
    public SquareImageView(Context context) {
        super(context);
    } 
 
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    } 
 
    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    } 
 
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
        
        
    } 
} 