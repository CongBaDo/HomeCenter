package com.HomeCenter2.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CategoryView extends RelativeLayout {
	 
    public CategoryView(Context context) {
        super(context);
    } 
 
    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    } 
 
    public CategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    } 
 
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
        
        
    } 
} 