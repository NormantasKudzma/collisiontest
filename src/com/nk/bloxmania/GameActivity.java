package com.nk.bloxmania;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
		finish();
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
		((GameView)findViewById(R.id.imageView1)).isDone(true);
	}
}
