package com.nk.bloxmania;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class GameView extends ScrollBackgroundView implements Runnable,  SurfaceHolder.Callback {
	private GameEngine engine;
	private SurfaceHolder holder;
	
	// Text display variables
	private int newBlack = 0xff000001;
	private int newWhite = 0xfffffffe;
	private int deathCounterX;				// Death counter's position
	private int deathCounterY;
	private int modTextX;
	private String modText = null;
	private int bigTextSz = 120;
	private int medTextSz = 60;
	private int smallTextSz = 40;
	
	// Thread management variables
	long longSleepInterval = 1200;

	// Rotation and sensor variables
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private CustomSensorListener mSensorListener;
	float screenRotation = 0;				// Direction to move player on the next keyframe
	
	// Level related variables
	boolean levelCompleted = false;
	boolean gameOver = false;
	public static int selectedLevel = 0;
	private float levelCompletionDelta = 64;
	ArrayList<GameButton> buttons = new ArrayList<GameButton>();
	Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/disposable_droid.ttf");
	
	public GameView(Context c, AttributeSet attrs){
		super(c, attrs);
		initialize();
		// tutorial level has tutorial background, else normal bg
		if (selectedLevel == 0){
			loadBackground(selectedLevel);
		}
		else {
			loadBackground(((selectedLevel % (LevelManager.NUM_BACKGROUNDS - 1)) + 1));
		}

		engine = new GameEngine(screenWidth, screenHeight, bitmap, this);
		
		mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorListener = new CustomSensorListener();
		mSensorManager.registerListener(mSensorListener, mSensor, (int)(sleepInterval * 1000));
		setScrollSpeedY(0);
		((CustomActivity)getContext()).getWindow().setBackgroundDrawable(null);
		setBackgroundResource(0);

		getHolder().addCallback(this);
	}
	
	@Override
	protected void initialize() {
		deathCounterX = screenWidth * 3 / 18;
		deathCounterY = screenHeight * 1 / 9;
		modTextX = screenWidth * 15 / 18;
		
		// Scaling here
		bigTextSz = (int) (scaleH * bigTextSz);
		medTextSz = (int) (scaleH * medTextSz);
		smallTextSz = (int) (scaleH * smallTextSz);
		levelCompletionDelta *= scaleH;
	}
	
	void drawPlayer(){
		// Draw body
		paint.setColor(GameEngine.getPlayerColor());
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
		long t0, t1, delta;
		try {
			Log.w("GameView", "DRAW THREAD STARTED");
			loadLevel();
			
			lockDrawAndPost();
			
			// Countdown
			drawBackground();
			showCenteredText("Get ready..");
			lockDrawAndPost();
			Thread.sleep(longSleepInterval);
			
			drawBackground();
			showCenteredText("GO!");
			lockDrawAndPost();
			Thread.sleep(longSleepInterval);

			while (!done){
				// Move player, check collisions, draw everything
				t0 = System.currentTimeMillis();
				performDraws();
				screenRotation = mSensorListener.getValue();
				engine.movePlayerVertical();
				engine.movePlayerHorizontal(screenRotation);
				drawPlayer();
				showGameTexts();
				lockDrawAndPost();
				
				t1 = System.currentTimeMillis();
				delta = t0 + sleepInterval - t1;
				if (delta < trivialInterval){
					delta = trivialInterval;
				}
				Thread.sleep(delta);
			}
			unregisterMotionListener();
			setUpButtonListeners();
			if (levelCompleted){
				if (selectedLevel + 1 == LevelManager.LEVELS_UNLOCKED){
					LevelManager.LEVELS_UNLOCKED++;
				}
				LevelManager.saveSettings();
				showCenteredText(GameButton.COMPLETED);
				showLeftButton(GameButton.CONTINUE);
			}
			else {
				LevelManager.updateDeaths(selectedLevel);
				showCenteredText(GameButton.GAME_OVER);
				showLeftButton(GameButton.RESTART);
			}
			showRightButton(GameButton.MENU);
			lockDrawAndPost();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
		
	void setUpButtonListeners(){
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
		        				unregisterTouchListener();
		        				if (i.text.equals(GameButton.MENU)){
		        					// Add up deaths, show main menu
		        					GameEngine.DEATH_COUNT = 0;
		        					finish();
		        					CustomActivity host = (CustomActivity)getContext();
		        					host.startCustomActivity(LevelSelectionActivity.class);
		        					return true;
		        				}
		        				if (levelCompleted){
		        					// Add up deaths, start next level
		        					GameEngine.DEATH_COUNT = 0;
		        					selectedLevel++;
		        					recreate();
		        					return true;
		        				}
		        				else {
		        					// Increment deaths, restart level
		        					recreate();
		        					return true;
		        				}
		        			}
		        		}
		        	}
			    }
				return false;
			}			
		});
	}
	
	void showLeftButton(String txt){
		int xPos = screenWidth / 4;
		int yPos = screenHeight * 4 / 5;
		int h = medTextSz;
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
	
	void showGameTexts(){
		String str = "Deaths : " + LevelManager.getDeaths(selectedLevel);
		showText(str, deathCounterX, deathCounterY, smallTextSz);
		
		if (modText != null){
			showText(modText, modTextX, deathCounterY, smallTextSz);
		}
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
		int viewPortX = (int)engine.getViewPortX();
		ArrayList<GameBlock> level = engine.getLevel();
		RectF r;
		RectF rr = new RectF();
		for (GameBlock gb : engine.getLevel()){
			r = gb.r;
			// Figure is out of bounds (to the right), haven't reached that part, done drawing
			if (r.left > viewPortX + screenWidth - 1){
				break;
			}
			// Figure is out of bounds (to the left), try next figure
			if (r.right < viewPortX){
				continue;
			}
			
			rr.set(0, r.top, 0, r.bottom);
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
		checkLevelEnd(viewPortX, engine.x, (int)level.get(level.size()-1).r.right);
	}
	
	void lockDrawAndPost(){
		Canvas c = holder.lockCanvas();
		if (c != null){
			c.drawBitmap(bitmap, 0, 0, null);	
			holder.unlockCanvasAndPost(c);
		}
		else {
			Log.wtf("nk", "Could not lock canvas!");
		}
	}
	
	void checkLevelEnd(int viewPortX, int plrX, int lastBlockX){
		if (viewPortX + plrX > lastBlockX + levelCompletionDelta){
			levelCompleted = true;
			done = true;
		}
	}
	
	@Override
	public void finish() {
		unregisterTouchListener();
		unregisterMotionListener();
		super.finish();
	}
	
	protected void unregisterTouchListener(){
		setOnTouchListener(null);
	}
	
	protected void unregisterMotionListener(){
		if (mSensorManager != null){
			mSensorManager.unregisterListener(mSensorListener, mSensor);
			mSensorListener = null;
			mSensorManager = null;
			mSensor = null;
		}
	}

	public void recreate(){
		finish();
		CustomActivity host = (CustomActivity)getContext();
		host.startCustomActivity(GameActivity.class);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		new Thread(this).start();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (holder != null){
			holder.removeCallback(this);
		}
		this.holder = null;
		finish();
	}
	
	public void setGameOver(){
		gameOver = true;
	}

	public void setInfoText(String s){
		modText = s;
	}
}
