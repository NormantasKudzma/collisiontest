package com.nk.collisiontest;

import com.example.collisiontest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		setButtonListeners();
		LevelManager.countLevels(getApplicationContext());
	}
	
	@Override
	protected void onDestroy() {
		saveSettings();
		super.onDestroy();
	}
	
	void setButtonListeners(){
		findViewById(R.id.button_play).setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = new Intent(MainActivity.this, LevelSelectionActivity.class);
				startActivity(i);
				return true;
			}
		});
		
		findViewById(R.id.button_options).setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// CALL OPTIONS
				return false;
			}
		});
		
		findViewById(R.id.button_about).setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(i);
				return true;
			}
		});
	}

	void saveSettings(){
		LevelManager.updateDeaths(GameView.selectedLevel, GameEngine.DEATH_COUNT);
		GameEngine.DEATH_COUNT = 0;
		LevelManager.saveSettings();
	}
}
