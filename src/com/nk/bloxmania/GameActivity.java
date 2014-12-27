package com.nk.bloxmania;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity {
	GameView gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		gv = ((GameView)findViewById(R.id.imageView1));
	}
	
	@Override
	public void onBackPressed() {
		saveSettings();
		killThread();
		Intent i = new Intent(this, LevelSelectionActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	protected void onPause() {
		gv.paused = true;
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		gv.paused = false;
		super.onResume();
	}
	
	void saveSettings(){
		LevelManager.updateDeaths(GameView.selectedLevel, GameEngine.DEATH_COUNT);
		GameEngine.DEATH_COUNT = 0;
	}
	
	@Override
	protected void onDestroy() {
		killThread();
		super.onDestroy();
	}
	
	void killThread(){
		gv.finish();
	}
}
