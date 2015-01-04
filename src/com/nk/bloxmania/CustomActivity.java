package com.nk.bloxmania;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

public class CustomActivity extends Activity{
	public static final int BIG_SIZE = 72;
	public static final int SMALL_SIZE = BIG_SIZE / 2;

	protected static MusicManager musicManager = null;
	
	protected Typeface font;
	protected ScrollBackgroundView sbv;
	protected int scrollBackgroundResource = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		
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
	protected void onDestroy() {
		saveSettings();
		super.onDestroy();
	}
	
	protected void saveSettings(){
		LevelManager.updateDeaths(GameView.selectedLevel, GameEngine.DEATH_COUNT);
		GameEngine.DEATH_COUNT = 0;
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
