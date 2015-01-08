package com.nk.bloxmania;

import android.os.Bundle;

public class GameActivity extends CustomActivity {
	GameView gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		gv = ((GameView)findViewById(R.id.imageView1));
	}
	
	@Override
	public void onBackPressed() {
		killThread();
		startCustomActivity(LevelSelectionActivity.class);
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
	
	@Override
	protected void onDestroy() {
		killThread();
		saveLevelSettings();
		super.onDestroy();
	}
	
	protected void saveLevelSettings(){
		GameEngine.DEATH_COUNT = 0;
	}
	
	void killThread(){
		gv.finish();
	}
}
