package com.nk.bloxmania;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class GameView extends ScrollBackgroundView implements Runnable, SensorEventListener {
	GameEngine engine;
	
	// Text display variables
	int newBlack = 0xff000001;
	int newWhite = 0xfffffffe;
	int deathCounterX;				// Death counter's position
	int deathCounterY;
	int bigTextSz = 120;
	int medTextSz = 60;
	int smallTextSz = 40;
	
	// Thread management variables
	long longSleepInterval = 1200;

	// Rotation and sensor variables
	private SensorManager mSensorManager;
	private Sensor mSensor;
	float screenRotation = 0;				// Direction to move player on the next keyframe
	
	// Level related variables

	boolean levelCompleted = false;
	public static int selectedLevel = 0;
	ArrayList<GameButton> buttons = new ArrayList<GameButton>();
	Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/disposable_droid.ttf");
	
	public GameView(Context c, AttributeSet attrs){
		super(c, attrs);
		mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mSensor, 50000);		
		setScrollSpeedY(0);
		
		new Thread(this).start();
	}
	
	@Override
	protected void initialize() {
		Resources res = getResources();
		int resId;
		// tutorial level has tutorial background, else normal bg
		if (selectedLevel == 0){
			resId = res.getIdentifier("bg" + selectedLevel, "drawable", getContext().getPackageName());
		}
		else {
			resId = res.getIdentifier("bg" + ((selectedLevel % (LevelManager.NUM_BACKGROUNDS - 1)) + 1), "drawable", getContext().getPackageName());
		}
		setBackgroundResource(resId);
		try {
			InputStream is = res.openRawResource(resId);
	        bgImg = BitmapFactory.decodeStream(is);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		Log.w("nk", "Loaded background resource, id: " + resId);
		
		deathCounterX = screenWidth * 3 / 18;
		deathCounterY = screenHeight * 1 / 9;
		
		engine = new GameEngine(screenWidth, screenHeight, bitmap, this);
	}
	
	void drawPlayer(){
		// Draw body
		paint.setColor(engine.getPlayerColor());
		Rect r = engine.getPlayerRect();
		mainCanvas.drawRect(r, paint);
		// Draw face
		paint.setColor(newBlack);
		int w = engine.w;
		int h = engine.h;
		float xt = r.left + w / 3;
		float x2t = r.left + 2 * w / 3;
		float yt = r.top + h / 3;
		float y2t = r.top + 2 * h /3;
		mainCanvas.drawRect(xt - 2, yt - 2, xt + 2, yt + 2, paint);
		mainCanvas.drawRect(x2t - 2, yt - 2, x2t + 2, yt + 2, paint);
		mainCanvas.drawRect(xt, y2t - 2, x2t, y2t + 2, paint);
	}

	@Override
	public void run() {
		try {
			Log.w("GameView", "DRAW THREAD STARTED");
			loadLevel();
			
			// Countdown
			clearCanvas();
			showCenteredText("Get ready..");
			postInvalidate();
			Thread.sleep(longSleepInterval);
			clearCanvas();
			showCenteredText("GO!");
			postInvalidate();
			Thread.sleep(longSleepInterval);
			
			while (!done){
				// Move player, check collisions, draw everything
				while (paused){
					wait();
				}
				performDraws();
				engine.movePlayerVertical();
				engine.movePlayerHorizontal(screenRotation);
				drawPlayer();
				showDeathsText();
				postInvalidate();
				Thread.sleep(sleepInterval);
			}
			setOnTouchListener(new View.OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent e) {
					int pid = e.getPointerId(0);
				    int action = e.getActionMasked();
				    int x = (int)e.getX(pid);
		        	int y = (int)e.getY(pid);
				    switch (action){
			        	case MotionEvent.ACTION_DOWN: { 
			        		for (GameButton i : buttons){
			        			if (i.r.contains(x, y)){
			        				if (i.text.equals("Back to menu")){
			        					// Add up deaths, show main menu
			        					LevelManager.updateDeaths(selectedLevel, GameEngine.DEATH_COUNT);
			        					LevelManager.saveSettings();
			        					GameEngine.DEATH_COUNT = 0;
			        					if (levelCompleted && selectedLevel + 1 == LevelManager.LEVELS_UNLOCKED){
				        					LevelManager.LEVELS_UNLOCKED++;
			        					}
			        					Intent intent = new Intent(getContext(), MainActivity.class);
			        		            ((Activity)getContext()).startActivity(intent);
			        					return true;
			        				}
			        				if (levelCompleted){
			        					// Add up deaths, start next level
			        					LevelManager.updateDeaths(selectedLevel, GameEngine.DEATH_COUNT);
			        					GameEngine.DEATH_COUNT = 0;
			        					if (selectedLevel + 1 == LevelManager.LEVELS_UNLOCKED){
				        					LevelManager.LEVELS_UNLOCKED++;
			        					}
			        					selectedLevel++;
			        					Activity host = (Activity) getContext();
			        					host.recreate();
			        					return true;
			        				}
			        				else {
			        					// Increment deaths, restart level
			        					LevelManager.updateDeaths(selectedLevel, GameEngine.DEATH_COUNT);
			        					Activity host = (Activity) getContext();
			        					host.recreate();
			        					return true;
			        				}
			        			}
			        		}
			        	}
				    }
					return false;
				}
				
			});
			if (levelCompleted){
				showCenteredText("Level completed!");
				showLeftButton("Continue");
			}
			else {
				showCenteredText("You died..");
				showLeftButton("Restart");
			}
			showRightButton("Back to menu");
			postInvalidate();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	void showLeftButton(String txt){
		int xPos = screenWidth / 4;
		int yPos = screenHeight * 4 / 5;
		int h = 60;
		int w = showText(txt, xPos, yPos, h);
		GameButton b = new GameButton();
		b.r = new Rect(xPos - w, yPos - h, xPos + 2 * w, yPos);
		b.text = txt;
		buttons.add(b);
	}
	
	void showRightButton(String txt){
		int xPos = screenWidth * 3 / 4;
		int yPos = screenHeight * 4 / 5;
		int h = medTextSz;
		int w = showText(txt, xPos, yPos, h);
		GameButton b = new GameButton();
		b.r = new Rect(xPos - w, yPos - h, xPos + 2 * w, yPos);
		b.text = txt;
		buttons.add(b);
	}
	
	void showCenteredText(String txt){
		int xPos = screenWidth / 2;
		int yPos = screenHeight / 2 - 20;
		showText(txt, xPos, yPos, bigTextSz);
	}
	
	int showText(String txt, int xPos, int yPos, int sz){
		return showText(txt, xPos, yPos, sz, newBlack, newWhite);
	}
	
	// Shows text at requested position and returns its half width in px
	int showText(String txt, int xPos, int yPos, int sz, int clr1, int clr2){
		paint.setTextSize(sz);
		paint.setTypeface(font);

		int xCorrected = (int) (xPos - paint.measureText(txt) / 2);
		paint.setColor(clr1);
		mainCanvas.drawText(txt, xCorrected + 3, yPos + 3, paint);
		paint.setColor(clr2);
		mainCanvas.drawText(txt, xCorrected, yPos, paint);
		return xCorrected;
	}
	
	void showDeathsText(){
		String str = "Deaths : " + LevelManager.getDeaths(selectedLevel);
		showText(str, deathCounterX, deathCounterY, smallTextSz);
	}
	
	void performDraws(){
		drawBackground();
		drawLevel();
	}

	void loadLevel(){
		engine.setCurrentLevel(selectedLevel);
		engine.loadLevel(getResources());
	}
	
	void drawLevel(){
		paint.setColor(Color.BLACK);
		int viewPortX = engine.getViewPortX();
		ArrayList<GameBlock> level = engine.getLevel();
		for (GameBlock gb : level){
			Rect r = gb.r;
			// Figure is out of bounds (to the right), haven't reached that part, done drawing
			if (r.left > viewPortX + screenWidth - 1){
				break;
			}
			// Figure is out of bounds (to the left), try next figure
			if (r.right < viewPortX){
				continue;
			}
			
			Rect rr = new Rect();
			rr.top = r.top;
			rr.bottom = r.bottom;
			if (r.left >= viewPortX){
				rr.left = r.left - viewPortX;
			}
			if (r.right <= viewPortX + screenWidth){
				rr.right = r.right - viewPortX;
			}
			else {
				rr.right = screenWidth;
			}
			if (gb.isMoving()){
				gb.frame(rr.left);
			}
			mainCanvas.drawRect(rr, paint);
		}
		
		engine.moveViewPort();
		checkLevelEnd(viewPortX, engine.x, level.get(level.size()-1).r.right);
	}
	
	void checkLevelEnd(int viewPortX, int plrX, int lastBlockX){
		if (viewPortX + plrX > lastBlockX + 64){
			levelCompleted = true;
			done = true;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		screenRotation = event.values[1];
	}
}
