package com.nk.bloxmania;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

public class CustomActivity extends Activity{
	public static final int BIG_SIZE = 72;
	public static final int SMALL_SIZE = BIG_SIZE / 2;
	
	protected Typeface font;
	protected ScrollBackgroundView sbv;
	protected int scrollBackgroundResource = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
	}
	
	protected void customizeBackground(float dx, float dy){
		sbv = (ScrollBackgroundView)findViewById(scrollBackgroundResource);
		sbv.setScrollSpeedX(dx);
		sbv.setScrollSpeedY(dy);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		saveSettings();
		finish();
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
