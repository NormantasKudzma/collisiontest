package com.nk.bloxmania;

import java.lang.ref.WeakReference;

import android.os.Bundle;

public class GameActivity extends CustomActivity {
	WeakReference<GameView> gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		gv = new WeakReference<GameView>(((GameView)findViewById(R.id.imageView1)));
	}
	
	@Override
	public void onBackPressed() {
		killThread();
		startCustomActivity(LevelSelectionActivity.class);
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
		if (gv != null && gv.get() != null){
			gv.get().finish();
			gv.clear();
			gv = null;
		}
	}
}
