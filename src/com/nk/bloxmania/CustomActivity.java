package com.nk.bloxmania;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;

public class CustomActivity extends Activity{
	public static int BIG_SIZE = 72;
	public static int SMALL_SIZE = BIG_SIZE / 2;
	protected static float scale = -1;
	
	protected static MusicManager musicManager = null;
	
	protected Typeface font;
	protected ScrollBackgroundView sbv;
	protected int scrollBackgroundResource = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		
		if (scale == -1){
			int height = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
			scale = Scale.getUniformScale(height);
			BIG_SIZE = (int)(BIG_SIZE * scale);
			SMALL_SIZE = BIG_SIZE / 2;
		}
		
		if (CustomActivity.musicManager == null){
			CustomActivity.musicManager = new MusicManager(getApplicationContext());
		}
	}
	
	protected void customizeBackground(float dx, float dy){
		sbv = (ScrollBackgroundView)findViewById(scrollBackgroundResource);
		sbv.setScrollSpeedX(dx);
		sbv.setScrollSpeedY(dy);
	}
		
	@Override
	protected void onPause() {
		CustomActivity.musicManager.pause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		CustomActivity.musicManager.resume();
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		CustomActivity.musicManager.resume();
		super.onStart();
	}
		
	@Override
	public void finish() {
		if (sbv != null){
			sbv.finish();
		}
		super.finish();
	}
	
	protected void startCustomActivity(Class<?> cls){
		Intent i = new Intent(this, cls);
		startActivity(i);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
