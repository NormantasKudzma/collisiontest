package com.nk.collisiontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.collisiontest.R;

public class GameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
	}
	
	@Override
	public void onBackPressed() {
		saveSettings();
		killThread();
		Intent i = new Intent(GameActivity.this, MainActivity.class);
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		((GameView)findViewById(R.id.imageView1)).paused = true;
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		((GameView)findViewById(R.id.imageView1)).paused = false;
		super.onResume();
	}
	
	void saveSettings(){
		LevelManager.updateDeaths(GameView.selectedLevel, GameEngine.DEATH_COUNT);
		GameEngine.DEATH_COUNT = 0;
		LevelManager.saveSettings();
	}
	
	@Override
	protected void onDestroy() {
		killThread();
		super.onDestroy();
	}
	
	void killThread(){
		((GameView)findViewById(R.id.imageView1)).done = true;
	}
}
