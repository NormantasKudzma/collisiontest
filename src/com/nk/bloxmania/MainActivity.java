package com.nk.bloxmania;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.collisiontest.R;

public class MainActivity extends Activity{
	public static final int BASE_BUTTON_SIZE = 72;
	public static final int FONT_SIZE = BASE_BUTTON_SIZE / 2;
	Typeface font;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		setUpInterface();
		customizeBackground();
		LevelManager.countLevels(this);
		LevelManager.countBackgrounds(this);
	}
	
	@Override
	protected void onDestroy() {
		saveSettings();
		super.onDestroy();
	}
	
	void customizeBackground(){
		ScrollBackgroundView sbv = (ScrollBackgroundView)findViewById(R.id.menu_scroll_background);
		sbv.setScrollSpeedX(0.5f);
		sbv.setScrollSpeedY(0.5f);
	}
	
	void setUpInterface(){
		TextView tv = (TextView)findViewById(R.id.version);
		tv.setTypeface(font);
		
		Button b1 = (Button)findViewById(R.id.button_play);
		b1.setTypeface(font);
		b1.setTextSize(FONT_SIZE);
		b1.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = new Intent(MainActivity.this, LevelSelectionActivity.class);
				startActivity(i);
				return true;
			}
		});
		
		Button b2 = (Button)findViewById(R.id.button_options);
		b2.setTypeface(font);
		b2.setTextSize(FONT_SIZE);
		b2.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// CALL OPTIONS
				return false;
			}
		});
		
		Button b3 = (Button)findViewById(R.id.button_about);
		b3.setTypeface(font);
		b3.setTextSize(FONT_SIZE);
		b3.setOnTouchListener(new View.OnTouchListener() {
			
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
