package com.nk.bloxmania;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
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
	
	protected float scaleW, scaleH;
	
	protected boolean done = false;
	protected volatile boolean paused = false;
	protected long sleepInterval = 40;
	protected long trivialInterval = 10;
	
	public ScrollBackgroundView(Context c, AttributeSet attrs){
		super(c, attrs);
		
		Display d = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = d.getWidth();
		screenHeight = d.getHeight();
		bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mainCanvas = new Canvas(bitmap);
		setKeepScreenOn(true);
		
		scaleW = Scale.getWidthScale(screenWidth);
		scaleH = Scale.getHeightScale(screenHeight);
		
		bgPivotX = screenWidth;
		bgPivotY = 0;
		initialize();	
	}

	protected void initialize(){
		int i = Integer.parseInt((String) getTag());
		loadBackground(i);
		new Thread(this).start();
	}
	
	@SuppressWarnings("deprecation")
	protected void loadBackground(int i){
		Resources res = getResources();
		int resId;
		
		resId = res.getIdentifier("bg" + i, "drawable", getContext().getPackageName());
	
		if (resId != 0){
			try {
				InputStream is = res.openRawResource(resId);
		        bgImg = BitmapFactory.decodeStream(is);
		        bgImg = Bitmap.createScaledBitmap(bgImg, screenWidth, screenHeight, false);
		        setBackgroundDrawable(new BitmapDrawable(res, bgImg));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Log.w("nk", "Loaded background resource, id: " + resId);
	}
	
	@Override
	public void run(){
		Log.w("nk", "Scrolling background started.");
		while (!done && getVisibility() != View.GONE){
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
		mainCanvas.save();
		mainCanvas.translate(-bgPivotX, bgPivotY);
		mainCanvas.drawBitmap(bgImg, 0,  0, null);
		mainCanvas.translate(0, -screenHeight);
		mainCanvas.drawBitmap(bgImg, 0, 0, null);
		mainCanvas.translate(screenWidth, 0);
		mainCanvas.drawBitmap(bgImg, 0, 0, null);
		mainCanvas.translate(0, screenHeight);
		mainCanvas.drawBitmap(bgImg, 0, 0, null);
		mainCanvas.restore();
		
		moveBackground();
	}
	
	protected void moveBackground(){
		bgPivotX += bgPivotXDelta;
		bgPivotY += bgPivotYDelta;
		if (bgPivotX >= screenWidth){
			bgPivotX = 0;
		}
		if (bgPivotY >= screenHeight){
			bgPivotY = 0;
		}
	}
	
	public void setScrollSpeedX(float value){
		bgPivotXDelta = value;
	}
	
	public void setScrollSpeedY(float value){
		bgPivotYDelta = value;
	}
	
	public synchronized void isDone(boolean done){
		this.done = done;
	}
		
	public void finish(){
		isDone(true);
	}
}
