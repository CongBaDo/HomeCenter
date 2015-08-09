/**
 * PhotoSorterView.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * TODO: Add OpenGL acceleration.
 * 
 * --
 * 
 * Released under the MIT license (but please notify me if you use this code, so that I can give your project credit at
 * http://code.google.com/p/android-multitouch-controller ).
 * 
 * MIT license: http://www.opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.HomeCenter2.imageprocessing;

import java.util.ArrayList;

import com.HomeCenter2.multitouch.controller.MultiTouchController;
import com.HomeCenter2.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import com.HomeCenter2.multitouch.controller.MultiTouchController.PointInfo;
import com.HomeCenter2.multitouch.controller.MultiTouchController.PositionAndScale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PhotoSortrView extends View implements MultiTouchObjectCanvas<PhotoSortrView.Img> {
	
	private static final String TAG = "PhotoSortrView";

//	private static final int[] IMAGES = { R.drawable.m74hubble, R.drawable.catarina, R.drawable.tahiti, R.drawable.sunset, R.drawable.lake };
//	private ArrayList<Integer> resIdImages;
	Resources res;
	private Bitmap bit;
	private ArrayList<Img> mImages = new ArrayList<Img>();
	
	private int currentIndex = 0;

	// --

	private MultiTouchController<Img> multiTouchController = new MultiTouchController<Img>(this);

	private PointInfo currTouchPoint = new PointInfo();

	private boolean mShowDebugInfo = true;

	private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

	private int mUIMode = UI_MODE_ROTATE;

	// --

	private Paint mLinePaintTouchPointCircle = new Paint();

	// ---------------------------------------------------------------------------------------------------

	public PhotoSortrView(Context context) {
		this(context, null);
	}

	public PhotoSortrView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PhotoSortrView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
//	public void addImage(int resId){
//		resIdImages.add(resId);
//		mImages.add(new Img(resId, res));
//	}
	
	public void addImage(Drawable drawable, Resources res){
		mImages.add(new Img(drawable, res));
		currentIndex++;
	}
	
	public void drawText(String gText){
		
		Bitmap bit = drawableToBitmap(mImages.get(currentIndex - 1).getDrawable());
		bit = drawTextToBitmap(bit, gText);
		Drawable dra = new BitmapDrawable(getResources(), bit);
		mImages.get(currentIndex-1).updateDrawable(dra);
		
	}
	
	private Bitmap drawTextToBitmap(Bitmap bit, String gText) {
		float scale = getResources().getDisplayMetrics().density;

		android.graphics.Bitmap.Config bitmapConfig = bit.getConfig();
		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bit = bit.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bit);
//		canvas = new Canvas
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.BLACK);
		// text size in pixels
		paint.setTextSize((int) (20 * scale));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bit.getWidth() - bounds.width()) / 2 - 20;
		int y = (bit.getHeight() + bounds.height()) / 2;

		canvas.drawText(gText, x, y, paint);

		return bit;
	}
	
	private Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	public Bitmap mergeBitmap(Bitmap bit){
    	Log.v(TAG, "BITMAP  "+bit.getWidth()+" "+bit.getHeight()+" "+mImages.size());
    	for(int i = 0; i < mImages.size(); i++){
    		//Log.e(TAG, "BIT MO "+mImages.get(i).getMinX()+" "+i);
    		bit = mergeBitmap(bit, mImages.get(i).getBitmap(), (int)mImages.get(i).getMinX(), (int)mImages.get(i).getMinY());
    	}
    	return bit;
    }
	
	public Bitmap getBGBitmap(){
		Log.v(TAG, "BITMAP  "+mImages.get(0).getBitmap().getWidth());
		
		int startX = 0, startY = 0;
		
		if(mImages.get(0).getMinX() < 0){
			startX = 0;
		}else{
			startX = (int)mImages.get(0).getMinX();
		}
		
		if(mImages.get(0).getMinY() < 0){
			startY = 0;
		}else{
			startY = (int)mImages.get(0).getMinY();
		}
		
		Bitmap bit = Bitmap.createBitmap(mImages.get(0).getBitmap(), startX, 
				startY, (int)(mImages.get(0).getWidth()*mImages.get(0).getScaleX()), (int)(mImages.get(0).getHeight()*mImages.get(0).getScaleY()));
		return bit;
	}
	
	public Bitmap mergeBitmap(Bitmap original, Bitmap above, int left, int top) {
		Log.e(TAG, "merge "+left +" "+top);
		 int width, height = 0; 
		 width = original.getWidth(); 
		 height = original.getHeight(); 
		 
		 Log.e(TAG, "merge "+width +" "+height);
		 Bitmap outBitmap = Bitmap.createBitmap(width, height, original.getConfig()); 
		 
		 if(above != null){
			 Canvas comboImage = new Canvas(outBitmap); 
			 comboImage.drawBitmap(original, 0, 0, null);
			 comboImage.drawBitmap(above, left, top, null);
			 return outBitmap; 
		 }else{
			 return original;
		 }
  }

	private void init(Context context) {
		res = context.getResources();

		mLinePaintTouchPointCircle.setColor(Color.YELLOW);
		mLinePaintTouchPointCircle.setStrokeWidth(5);
		mLinePaintTouchPointCircle.setStyle(Style.STROKE);
		mLinePaintTouchPointCircle.setAntiAlias(true);
		setBackgroundColor(Color.BLACK);
	}

	/** Called by activity's onResume() method to load the images */
	public void loadImages(Context context) {
		Resources res = context.getResources();
		int n = mImages.size();
		for (int i = 0; i < n; i++)
			mImages.get(i).load(res);
	}

	/** Called by activity's onPause() method to free memory used for loading the images */
	public void unloadImages() {
		//currentIndex = 0;
		int n = mImages.size();
		for (int i = 0; i < n; i++)
			mImages.get(i).unload();
	}
	
	public void reset(){
		if(mImages.size() > 0){
			for (int i = 0; i < mImages.size(); i++){
				mImages.get(i).unload();
			}
		}
		currentIndex = 0;
		mImages.clear();
	}

	// ---------------------------------------------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int n = mImages.size();
		for (int i = 0; i < n; i++)
			mImages.get(i).draw(canvas);
		if (mShowDebugInfo)
			drawMultitouchDebugMarks(canvas);
	}

	// ---------------------------------------------------------------------------------------------------

	public void trackballClicked() {
		mUIMode = (mUIMode + 1) % 3;
		invalidate();
	}

	private void drawMultitouchDebugMarks(Canvas canvas) {
		if (currTouchPoint.isDown()) {
			float[] xs = currTouchPoint.getXs();
			float[] ys = currTouchPoint.getYs();
			float[] pressures = currTouchPoint.getPressures();
			int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
			for (int i = 0; i < numPoints; i++)
				canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80, mLinePaintTouchPointCircle);
			if (numPoints == 2)
				canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	/** Pass touch events to the MT controller */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return multiTouchController.onTouchEvent(event);
	}

	/** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
	public Img getDraggableObjectAtPoint(PointInfo pt) {
		float x = pt.getX(), y = pt.getY();
		int n = mImages.size();
		for (int i = n - 1; i >= 0; i--) {
			Img im = mImages.get(i);
			if (im.containsPoint(x, y))
				return im;
		}
		return null;
	}

	/**
	 * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
	 * and a drag operation is starting. Called with null when drag op ends.
	 */
	public void selectObject(Img img, PointInfo touchPoint) {
		Log.i(TAG, "selectObject");
		currTouchPoint.set(touchPoint);
		if (img != null) {
			// Move image to the top of the stack when selected
			mImages.remove(img);
			mImages.add(img);
		} else {
			// Called with img == null when drag stops.
		}
		invalidate();
	}

	/** Get the current position and scale of the selected image. Called whenever a drag starts or is reset. */
	public void getPositionAndScale(Img img, PositionAndScale objPosAndScaleOut) {
		// FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
		objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
				(img.getScaleX() + img.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(), img.getScaleY(),
				(mUIMode & UI_MODE_ROTATE) != 0, img.getAngle());
	}

	/** Set the position and scale of the dragged/stretched image. */
	public boolean setPositionAndScale(Img img, PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
		currTouchPoint.set(touchPoint);
		boolean ok = img.setPos(newImgPosAndScale);
		if (ok)
			invalidate();
		return ok;
	}

	// ----------------------------------------------------------------------------------------------
	public interface ImgListener{
		public void completeEventTouch();
	}

	class Img {
		
		ImgListener listener;
		private int resId;

		private Drawable drawable;
		private Bitmap bit;
		private boolean firstLoad;

		private int width, height, displayWidth, displayHeight;

		private float centerX, centerY, scaleX, scaleY, angle;

		private float minX, maxX, minY, maxY;

		private static final float SCREEN_MARGIN = 100;

//		public Img(int resId, Resources res) {
//			this.resId = resId;
//			this.firstLoad = true;
//			getMetrics(res);
//		}
		
		public Img(Drawable drawable, Resources res){
			this.drawable = drawable;
			this.firstLoad = true;
			getMetrics(res);
			
			load(res);
			invalidate();
//			selec
		}
		
//		public Img(Drawable drawabel, Resources res){
//			this.drawable = drawabel;
//			this.firstLoad = true;
//			getMetrics(res);
//		}
		
		public void setListener(ImgListener listener){
			this.listener = listener;
		}

		private void getMetrics(Resources res) {
			DisplayMetrics metrics = res.getDisplayMetrics();
			// The DisplayMetrics don't seem to always be updated on screen rotate, so we hard code a portrait
			// screen orientation for the non-rotated screen here...
			// this.displayWidth = metrics.widthPixels;
			// this.displayHeight = metrics.heightPixels;
			this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
					metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
			this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
					metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
		}

		/** Called by activity's onResume() method to load the images */
		public void load(Resources res) {
			getMetrics(res);
//			this.drawable = res.getDrawable(resId);
			this.width = drawable.getIntrinsicWidth();
			this.height = drawable.getIntrinsicHeight();
			float cx, cy, sx, sy;
			if (firstLoad) {
				cx = displayWidth/2;//SCREEN_MARGIN * 2 ;//+ (float) (Math.random() * (displayWidth - 2 * SCREEN_MARGIN));
				cy = displayHeight/2 - 100;//SCREEN_MARGIN * 2;// + (float) (Math.random() * (displayHeight - 2 * SCREEN_MARGIN));
				float sc = 1;//(float) (Math.max(displayWidth, displayHeight) / (float) Math.max(width, height) * Math.random() * 0.3 + 0.2);
				sx = sy = sc;
				firstLoad = false;
			} else {
				// Reuse position and scale information if it is available
				// FIXME this doesn't actually work because the whole activity is torn down and re-created on rotate
				cx = this.centerX;
				cy = this.centerY;
				sx = this.scaleX;
				sy = this.scaleY;
				// Make sure the image is not off the screen after a screen rotation
				if (this.maxX < SCREEN_MARGIN)
					cx = SCREEN_MARGIN;
				else if (this.minX > displayWidth - SCREEN_MARGIN)
					cx = displayWidth - SCREEN_MARGIN;
				if (this.maxY > SCREEN_MARGIN)
					cy = SCREEN_MARGIN;
				else if (this.minY > displayHeight - SCREEN_MARGIN)
					cy = displayHeight - SCREEN_MARGIN;
			}
			setPos(cx, cy, sx, sy, 0.0f);
		}

		/** Called by activity's onPause() method to free memory used for loading the images */
		public void unload() {
			this.drawable = null;
		}

		/** Set the position and scale of an image in screen coordinates */
		public boolean setPos(PositionAndScale newImgPosAndScale) {
			return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
					.getScaleX() : newImgPosAndScale.getScale(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
					: newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
			// FIXME: anisotropic scaling jumps when axis-snapping
			// FIXME: affine-ize
			// return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), newImgPosAndScale.getScaleAnisotropicX(),
			// newImgPosAndScale.getScaleAnisotropicY(), 0.0f);
		}

		/** Set the position and scale of an image in screen coordinates */
		private boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
			float ws = (width / 2) * scaleX, hs = (height / 2) * scaleY;
			int newWidth = (int) (width*scaleX);
			float newMinX = centerX - ws, newMinY = centerY - hs, newMaxX = centerX + ws, newMaxY = centerY + hs;
			if (newMinX > displayWidth - SCREEN_MARGIN || newMaxX < SCREEN_MARGIN || newMinY > displayHeight - SCREEN_MARGIN
					|| newMaxY < SCREEN_MARGIN)
				return false;
			this.centerX = centerX;
			this.centerY = centerY;
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			this.angle = angle;
			this.minX = newMinX;
			this.minY = newMinY;
			this.maxX = newMaxX;
			this.maxY = newMaxY;
//			this.width = newWidth;
			return true;
		}

		/** Return whether or not the given screen coords are inside this image */
		public boolean containsPoint(float scrnX, float scrnY) {
			// FIXME: need to correctly account for image rotation
			return (scrnX >= minX && scrnX <= maxX && scrnY >= minY && scrnY <= maxY);
		}

		public void draw(Canvas canvas) {
			canvas.save();
			float dx = (maxX + minX) / 2;
			float dy = (maxY + minY) / 2;
			drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
			canvas.translate(dx, dy);
//			canvas.rotate(angle * 180.0f / (float) Math.PI);
			canvas.translate(-dx, -dy);
			drawable.draw(canvas);
//			canvas.drawt
//			drawable.
//			minX = drawable.getBounds().left;
//			minY = drawable.getBounds().top;
//			width = 
//			drawable
			
			canvas.restore();
			
//			float ws = (width / 2) * scaleX, hs = (height / 2) * scaleY;
//			int newWidth = (int) (width*scaleX);
//			float newMinX = centerX - ws, newMinY = centerY - hs, newMaxX = centerX + ws, newMaxY = centerY + hs;
			
			Log.e(TAG, "DRAW A "+minX+" "+maxX+" "+minY+" "+maxY + " CENTER "+ centerX+" "+centerY+" AGLE "+width*scaleX);
//			bit = convertToBitmap(drawable, width, height);
//			canvas.drawBitmap(bit, new Matrix(), new Paint());
//			canvas.draw
//			invalidate();
		}

		public Drawable getDrawable() {
			return drawable;
		}
		
		public void updateDrawable(Drawable dra){
			drawable = dra;
			invalidate();
		}
		
		public Bitmap getBitmap(){
			bit = convertToBitmap(drawable, width, height);
//			bit = zoomBitmap(bit, angle * 180.0f / (float) Math.PI, scaleX, scaleY);
			
			return bit;
		}
		
		private Bitmap zoomBitmap(Bitmap bitmap, float degree, float scaleX, float scaleY){
			Log.i(TAG, "zoombitmap "+scaleX);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
//			matrix.postScale(scaleX, scaleY);
//			matrix.postRotate(degree);
//			matrix.get
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			return resizedBitmap;
		}
		
		private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
		    Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(mutableBitmap);
		    drawable.setBounds(0, 0, widthPixels, heightPixels);
		    drawable.draw(canvas);

		    return mutableBitmap;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public float getCenterX() {
			return centerX;
		}

		public float getCenterY() {
			return centerY;
		}

		public float getScaleX() {
			return scaleX;
		}

		public float getScaleY() {
			return scaleY;
		}

		public float getAngle() {
			return angle;
		}

		// FIXME: these need to be updated for rotation
		public float getMinX() {
			return minX;
		}

		public float getMaxX() {
			return maxX;
		}

		public float getMinY() {
			return minY;
		}

		public float getMaxY() {
			return maxY;
		}
	}

	@Override
	public boolean pointInObjectGrabArea(PointInfo touchPoint, Img obj) {
		// TODO Auto-generated method stub
		return false;
	}
}
