package com.nk.bloxmania;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class ScrollBackgroundView extends SurfaceView implements Runnable {
	protected Paint paint = new Paint();		// 
	protected Bitmap bitmap;					//
	protected Canvas mainCanvas; 				// Bitmap's canvas
	protected int screenWidth, screenHeight;	// Screen dimensions
	protected Bitmap bgImg;					// Background image as bitmap
	protected float bgPivotX;					// Current position of pivot for background image
	protected float bgPivotY;					// Current position of pivot for background image
	protected float bgPivotXDelta = 2;			// Position change per frame for background image (horizontal)
	protected float bgPivotYDelta = 1;			// Position change per frame for background image (vertical)
	protected RectF bgRect = new RectF();
	
	protected boolean done = false;
	protected volatile boolean paused = false;
	protected long sleepInterval = 33;
	
	public ScrollBackgroundView(Context c, AttributeSet attrs){
		super(c, attrs);
		
		Point sz = new Point();
		((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(sz);
		screenWidth = sz.x;
		screenHeight = sz.y;
		bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mainCanvas = new Canvas(bitmap);
		setKeepScreenOn(true);
		
		bgPivotX = screenWidth;
		bgPivotY = 0;
		initialize();	
	}

	protected void initialize(){
		bgImg = ((BitmapDrawable)getBackground()).getBitmap();
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (getVisibility() != View.GONE){
			try {
				drawBackground();
				postInvalidate();
				Thread.sleep(sleepInterval);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		Log.w("nk", "Scrolling background stopped.");
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}
	
	protected void drawBackground(){
		bgRect.set(bgPivotX - screenWidth, bgPivotY - screenHeight, bgPivotX, bgPivotY);
		RectF leftBottom = new RectF(bgRect.left, bgRect.bottom, bgRect.right, bgRect.bottom + bgRect.height());
		RectF rightTop = new RectF(bgRect.right, bgRect.top, bgRect.right + bgRect.width(), bgRect.bottom);
		RectF rightBottom = new RectF(bgRect.right, bgRect.bottom, rightTop.right, leftBottom.bottom);
		mainCanvas.drawBitmap(bgImg, null, bgRect, paint);
		mainCanvas.drawBitmap(bgImg, null, leftBottom, paint);
		mainCanvas.drawBitmap(bgImg, null, rightTop, paint);
		mainCanvas.drawBitmap(bgImg, null, rightBottom, paint);
		
		bgPivotX -= bgPivotXDelta;
		bgPivotY += bgPivotYDelta;
		if (bgPivotX <= 0){
			bgPivotX = screenWidth;
		}
		if (bgPivotY >= screenHeight){
			bgPivotY = 0;
		}
	}

	/*protected void drawBackground(){
		bgRect.set(bgPivotX - screenWidth, 0, bgPivotX, screenHeight);
		mainCanvas.drawBitmap(bgImg, null, bgRect, paint);
		if (bgPivotX < screenWidth){
			mainCanvas.drawBitmap(bgImg, bgPivotX, 0, paint);
		}
		
		bgPivotX -= bgPivotXDelta;
		if (bgPivotX <= 0){
			bgPivotX = screenWidth;
		}
	}*/
	
	public void setScrollSpeedX(float value){
		bgPivotXDelta = value;
	}
	
	public void setScrollSpeedY(float value){
		bgPivotYDelta = value;
	}
	
	public synchronized void isDone(boolean done){
		this.done = done;
	}
	
	protected void clearCanvas(){
		mainCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	}
}
